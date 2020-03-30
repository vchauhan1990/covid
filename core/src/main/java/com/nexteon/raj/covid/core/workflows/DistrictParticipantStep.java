package com.nexteon.raj.covid.core.workflows;

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
		EPass epass = ePassService.getEPassData(Long.valueOf(id),"epass");
		if (null != epass) {
			groupOrUser = "covid-group-" + epass.getDistrict() + "-" + epass.getDepartment();
			workItem.getMetaDataMap().put("epass", epass);
		}
		return groupOrUser;
	}

}
