package com.nexteon.raj.covid.core.workflows;

import org.osgi.service.component.annotations.Component;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;

@Component(service=WorkflowProcess.class, property = {"process.label=COVID E-Pass Reject"})
public class EPassRejectedProcess implements WorkflowProcess {

	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap metaDataMap) throws WorkflowException {
		
	}

}
