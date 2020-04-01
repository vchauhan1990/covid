package com.nexteon.raj.covid.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

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

import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import com.nexteon.raj.covid.core.constants.PropertyConstants;
import com.nexteon.raj.covid.core.db.EPassService;
import com.nexteon.raj.covid.core.entity.EPass;
import com.nexteon.raj.covid.core.util.ResourceUtility;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=",
		PropertyConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
		PropertyConstants.SLING_SERVLET_PATHS + "=" + PropertyConstants.E_PASS_WORKFLOW_SERVLET_URL })
public class EPassServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -7764065425263480569L;

	@Reference
	private ResourceResolverFactory resolverFactory;

	private ResourceResolver resolver = null;
	private Session session = null;
	
	@Reference
	private WorkflowService workflowService;

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
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating the session: {}", e.getMessage());
			response.setStatus(700);
			response.sendRedirect(PropertyConstants.AUTHOR_ERROR_PAGE_PATH);
		}
		boolean status = startWorkflowProcess(session, PropertyConstants.EPASS_APPROVAL_WORKFLOW,
				PropertyConstants.AUTHOR_PAYLOAD_PAGE_PATH + "." + Long.valueOf(request.getParameter("id")) + ".html");
		if(status) {
			response.setStatus(200);
		} else {
			response.setStatus(700);
		}
	}

	private boolean startWorkflowProcess(Session session, String workflowName, String workflowContent) {
		try {
			LOGGER.error("Value of wfsession: {} -- {}", workflowService, session);
			LOGGER.error("Workflow Name & Content: {} -- {}", workflowName, workflowContent);
			WorkflowSession wfSession = workflowService.getWorkflowSession(session);
			LOGGER.error("Workflow Session: {} -- {}", wfSession);
			WorkflowModel wfModel = wfSession.getModel(workflowName);
			LOGGER.error("Workflow Model: {} -- {}", wfModel);
			WorkflowData wfData = wfSession.newWorkflowData(PropertyConstants.NODE_PROP_JCR_PATH, workflowContent);
			LOGGER.error("Workflow Data: {} -- {}", wfData);
			wfSession.startWorkflow(wfModel, wfData);
			return true;
		} catch (Exception e) {
			LOGGER.error("Exception occurred while starting the workflow: {}", e.getMessage());
			return false;
		}
	}

}
