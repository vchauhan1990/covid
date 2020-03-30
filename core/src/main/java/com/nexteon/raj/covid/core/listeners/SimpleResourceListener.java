package com.nexteon.raj.covid.core.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import com.nexteon.raj.covid.core.constants.PropertyConstants;

@Component(service = EventListener.class, immediate = true)
public class SimpleResourceListener implements EventListener {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private SlingRepository repository;

	private ResourceResolver resolver = null;
	private Session session = null;

	@Reference
	private WorkflowService workflowService;

	@Activate
	public void activate(ComponentContext context) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, "writeService");
		try {
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating the session: {}", e.getMessage());
		}
		try {
			session.getWorkspace().getObservationManager().addEventListener(this, Event.NODE_ADDED,
					PropertyConstants.AUTHOR_SERVER_EPASS_NODE_RESOURCE, true, null, null, false);
		} catch (RepositoryException e) {
			LOGGER.error("unable to register session", e);
			throw new Exception(e);
		}
	}

	@Override
	public void onEvent(EventIterator events) {
		try {
			while (events.hasNext()) {
				Event event = events.nextEvent();
				Resource resource = resolver.getResource(event.getPath());
				if (resource != null && resource.getValueMap().containsKey("id")) {
					startWorkflowProcess(session, PropertyConstants.EPASS_APPROVAL_WORKFLOW,
							PropertyConstants.AUTHOR_PAYLOAD_PAGE_PATH + "."
									+ resource.getValueMap().get("id").toString() + ".html");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred in the listeners:  {}", e);
		}

	}

	@Deactivate
	protected void deactivate() {
		if (resolver != null) {
			resolver.close();
		}
	}

	public void startWorkflowProcess(Session session, String workflowName, String workflowContent) {
		try {
			WorkflowSession wfSession = workflowService.getWorkflowSession(session);
			WorkflowModel wfModel = wfSession.getModel(workflowName);
			WorkflowData wfData = wfSession.newWorkflowData(PropertyConstants.NODE_PROP_JCR_PATH, workflowContent);
			wfSession.startWorkflow(wfModel, wfData);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while starting the workflow: {}", e.getMessage());
		}
	}

}
