package com.nexteon.raj.covid.core.workflows;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.jcr.base.util.AccessControlUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.nexteon.raj.covid.core.db.EPassService;
import com.nexteon.raj.covid.core.entity.EPass;

@Component(service = ParticipantStepChooser.class, property = {
		"chooser.label=Rajasthan District Group Participant Step" })
public class DistrictParticipantStep implements ParticipantStepChooser {

	@Reference
	private EPassService ePassService;

	private static final Logger LOGGER = LoggerFactory.getLogger(DistrictParticipantStep.class);

	@Override
	public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {
		LOGGER.info("Inside the DistrictParticipantStep");
		String id = workItem.getContentPath().substring(workItem.getContentPath().indexOf(".") + 1,
				workItem.getContentPath().lastIndexOf("."));
		String groupOrUser = "admin";
		EPass epass = ePassService.getEPassData(Long.valueOf(id), "epass");
		LOGGER.info("Exception in DistrictParticipantStep - epass: {}",epass);
		if (null != epass) {
			groupOrUser = "covid-group-" + epass.getDistrict() + "-" + epass.getDepartment();
			LOGGER.info("User Group - epass: {}",groupOrUser);
			try {
				UserManager userManager = AccessControlUtil.getUserManager(workflowSession.adaptTo(Session.class));
				Authorizable group = userManager.getAuthorizable(groupOrUser);
				if (group != null) {
					return group.getID();
				}
			} catch (RepositoryException e) {
				LOGGER.info("Exception in DistrictParticipantStep: {}",e.getMessage());
				e.printStackTrace();
			}
			workItem.getMetaDataMap().put("epass", epass);
		}
		return groupOrUser;
	}

}
