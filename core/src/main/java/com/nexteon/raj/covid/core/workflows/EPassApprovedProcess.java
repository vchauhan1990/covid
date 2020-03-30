package com.nexteon.raj.covid.core.workflows;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.nexteon.raj.covid.core.db.EPassService;
import com.nexteon.raj.covid.core.entity.EPass;

@Component(service = WorkflowProcess.class, property = { "process.label=COVID E-Pass Approve" })
public class EPassApprovedProcess implements WorkflowProcess {

	@Reference
	private EPassService ePassService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DistrictParticipantStep.class);
	
	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		MetaDataMap map = item.getMetaDataMap();
		EPass epass = map.get("epass", EPass.class);
		if(epass == null) {
			String id = item.getContentPath().substring(item.getContentPath().indexOf(".") + 1,
					item.getContentPath().lastIndexOf("."));
			epass = ePassService.getEPassData(Long.valueOf(id),"epass");
			if(ePassService.saveEPassData(epass,"epassapproved")) {
				LOGGER.info("Application Approved:  "+ Long.valueOf(epass.getId()));
			} else {
				LOGGER.info("Error while Approving:  "+ Long.valueOf(epass.getId()));
			}
		}
	}


}
