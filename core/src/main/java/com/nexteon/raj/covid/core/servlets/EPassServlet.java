package com.nexteon.raj.covid.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.nexteon.raj.covid.core.constants.PropertyConstants;
import com.nexteon.raj.covid.core.entity.EPass;
import com.nexteon.raj.covid.core.util.ResourceUtility;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=",
		PropertyConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
		PropertyConstants.SLING_SERVLET_PATHS + "=" + PropertyConstants.E_PASS_SERVLET_URL })
public class EPassServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -7764065425263480569L;

	@Reference
	private ResourceResolverFactory resolverFactory;

	private ResourceResolver resolver = null;
	private Session session = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtility.class);

	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {

		LOGGER.info("EPass Service Called");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, "writeService");
		try {
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			Query query = queryManager.createQuery(
					"SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([/etc/covid/epass/data]) and [phone] like '" + request.getParameter("phone")
							+ "'",
					Query.JCR_SQL2);
			QueryResult result = query.execute();
			if (result.getNodes().hasNext()) {
				RequestDispatcher requestDis = request.getRequestDispatcher(PropertyConstants.AUTHOR_ERROR_PAGE_PATH);
				request.setAttribute("success", false);
				request.setAttribute("errorMsg", "Phone number already exists!");
				requestDis.forward(request, response);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating the session: {}", e.getMessage());
		}

		
		
		
		EPass epass = ResourceUtility.mapRequestToEpass(request);
		if(null == epass || !uploadFileOnAEM(request, epass)){
			RequestDispatcher requestDis = request.getRequestDispatcher(PropertyConstants.AUTHOR_ERROR_PAGE_PATH);
			request.setAttribute("success", false);
			request.setAttribute("errorMsg", "Something went wrong!");
			requestDis.forward(request, response);
		}
		try {
			Node baseNode = session.getNode(PropertyConstants.AUTHOR_SERVER_EPASS_NODE_RESOURCE);
			if (null != baseNode) {
				createEPassNode(baseNode, epass);
			}
			session.save();
			
		} catch (Exception e) {
			LOGGER.error("Exception occurred while fetching the data: {}", e.getMessage());
		}
		if(resolver!=null) resolver.close();
		RequestDispatcher requestDis = request.getRequestDispatcher(PropertyConstants.AUTHOR_SUCCESS_PAGE_PATH);
		request.setAttribute("success", true);
		request.setAttribute("applicationID", epass.getId());
		requestDis.forward(request, response);
		
	}

	private boolean uploadFileOnAEM(SlingHttpServletRequest request, EPass epass) {
		try {
			AssetManager assetManager = resolver.adaptTo(AssetManager.class);
			RequestParameter param = request.getRequestParameter("upload");
			Asset newAsset = assetManager.createAsset(PropertyConstants.DAM_UPLOAD_BASE_PATH + epass.getId() + "-" +param.getFileName(), param.getInputStream(), param.getContentType(), true);
			epass.setIdPath(newAsset.getPath());
			return true;
		} catch(Exception e) {
			return false;
		}		
		
	}
	
	private Node createEPassNode(Node baseNode, EPass epass) throws Exception {
		Node node = baseNode.addNode(String.valueOf(epass.getId()), PropertyConstants.NODE_PROP_NT_UNSTRUCTURED);
		node.setProperty("id", epass.getId());
		node.setProperty("name", epass.getName());
		node.setProperty("fathername", epass.getFathername());
		node.setProperty("phone", epass.getPhone());
		node.setProperty("email", epass.getEmail());
		node.setProperty("district", epass.getDistrict());
		node.setProperty("addtype", epass.getAddtype());
		node.setProperty("address", epass.getAddress());
		node.setProperty("graampanchayat", epass.getGraampanchayat());
		node.setProperty("block", epass.getBlock());
		node.setProperty("city", epass.getCity());
		node.setProperty("pincode", epass.getPincode());
		node.setProperty("selectid", epass.getSelectId());
		node.setProperty("idPath", epass.getIdPath());
		node.setProperty("vehicleNumber", epass.getVehicleNumber());
		node.setProperty("vehicletype", epass.getVehicletype());
		node.setProperty("fromDate", epass.getFromDate());
		node.setProperty("toDate", epass.getToDate());
		node.setProperty("destinationtype", epass.getDestinationtype());
		node.setProperty("purpose", epass.getPurpose());
		node.setProperty("department", epass.getDepartment());
		return node;
	}
	
}
