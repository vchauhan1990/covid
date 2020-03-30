package com.nexteon.raj.covid.core.workflows;

import org.osgi.service.component.annotations.Component;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.nexteon.raj.covid.core.entity.EPass;

@Component(service = WorkflowProcess.class, property = { "process.label=COVID E-Pass Approve" })
public class EPassApprovedProcess implements WorkflowProcess {

	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		metaDataMap.get("epass", EPass.class);
		MetaDataMap map = item.getWorkflow().getWorkflowData().getMetaDataMap();
		EPass epass = map.get("epass", EPass.class);
	}


}
