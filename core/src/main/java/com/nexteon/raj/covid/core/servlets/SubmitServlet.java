package com.nexteon.raj.covid.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexteon.raj.covid.core.constants.PropertyConstants;
import com.nexteon.raj.covid.core.db.EPassService;
import com.nexteon.raj.covid.core.entity.EPass;
import com.nexteon.raj.covid.core.util.ResourceUtility;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=",
		PropertyConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
		PropertyConstants.SLING_SERVLET_PATHS + "=" + PropertyConstants.E_PASS_SERVLET_URL })
public class SubmitServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -326458997541933415L;

	@Reference
	private EPassService ePassService;

	@Reference
	private ResourceResolverFactory resolverFactory;

	private ResourceResolver resolver = null;
	private Session session = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(SubmitServlet.class);

	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.info("Inside Submit Servlet");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, "writeService");
		try {
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while fetching the data: {}", e.getMessage());
		}
		EPass epass = ResourceUtility.mapRequestToEpass(request);
		if (null == epass || !uploadFileOnAEM(request, epass)) {
			response.sendRedirect(PropertyConstants.AUTHOR_ERROR_PAGE_PATH);
		} else {
			boolean status = ePassService.saveEPassData(epass, "epass");
			if (!status) {
				response.sendRedirect(PropertyConstants.AUTHOR_ERROR_PAGE_PATH);
			} else {
				if (resolver != null)
					resolver.close();
				new Thread(() -> {
					boolean workflowCalled = callAEMWorkflow(epass.getId());
					if (workflowCalled) {
						LOGGER.info("Success in wf Calling");
					} else {
						LOGGER.info("Error in wf Calling");
					}
				}).start();
				response.sendRedirect(PropertyConstants.AUTHOR_SUCCESS_PAGE_PATH + "?regno=" + epass.getId());
			}
		}

	}

	private boolean callAEMWorkflow(long epassid) {
		LOGGER.info("Calling AEM WF");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("vishal", "bhatia@123&"));
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setCredentialsProvider(credentialsProvider);
		HttpPost httppost = new HttpPost("http://10.70.241.11/bin/aem/raj/coid/run/workflow?id=" + epassid);
		try {
			CloseableHttpResponse response = httpclient.execute(httppost, localContext);
			LOGGER.info("Workflow Response: {}", response.toString());
			if (response.getStatusLine().getStatusCode() == 200) {
				LOGGER.info("Success in Calling AEM WF");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info("Error in Calling AEM WF");
		return false;
	}

	private boolean uploadFileOnAEM(SlingHttpServletRequest request, EPass epass) {
		try {
			final boolean isMultipart = org.apache.commons.fileupload.servlet.ServletFileUpload
					.isMultipartContent(request);
			if (isMultipart) {
				final java.util.Map<String, org.apache.sling.api.request.RequestParameter[]> params = request
						.getRequestParameterMap();
				for (final java.util.Map.Entry<String, org.apache.sling.api.request.RequestParameter[]> pairs : params
						.entrySet()) {
					final String k = pairs.getKey();
					if (k.equals("upload")) {
						final org.apache.sling.api.request.RequestParameter[] pArr = pairs.getValue();
						final org.apache.sling.api.request.RequestParameter param = pArr[0];
						final InputStream stream = param.getInputStream();
						if (!writeToDam(stream, epass.getId() + "-" + param.getFileName(), param.getContentType(), epass)
								.equals("")) {
							return true;
						} else {
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String writeToDam(InputStream is, String fileName, String fileType, EPass epass) {
		try {
			com.day.cq.dam.api.AssetManager assetMgr = resolver.adaptTo(com.day.cq.dam.api.AssetManager.class);
			String newFile = PropertyConstants.DAM_UPLOAD_BASE_PATH + fileName;
			assetMgr.createAsset(newFile, is, fileType, true);
			session.save();
			epass.setIdPath(newFile);
			return newFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
