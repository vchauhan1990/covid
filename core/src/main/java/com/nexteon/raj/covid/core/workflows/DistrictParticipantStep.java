package com.nexteon.raj.covid.core.workflows;

import java.util.GregorianCalendar;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.search.QueryBuilder;
import com.nexteon.raj.covid.core.entity.EPass;

@Component(service = ParticipantStepChooser.class, property = {
		"chooser.label=Rajasthan District Group Participant Step" })
public class DistrictParticipantStep implements ParticipantStepChooser {

	@Reference
	private QueryBuilder queryBuilder;

	private static final Logger LOGGER = LoggerFactory.getLogger(DistrictParticipantStep.class);

	@Override
	public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {
		LOGGER.info("Inside the DistrictParticipantStep");
		String id = workItem.getContentPath().substring(workItem.getContentPath().indexOf(".") + 1,
				workItem.getContentPath().lastIndexOf("."));
		Session session = workflowSession.adaptTo(Session.class);
		try {
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			Query query = queryManager.createQuery(
					"SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([/etc/covid/epass/data]) and [id] like '" + id
							+ "'",
					Query.JCR_SQL2);

			String groupOrUser = "admin";
			QueryResult result = query.execute();
			if (result.getNodes().hasNext()) {
				Node node = result.getNodes().nextNode();

				EPass epass = new EPass();
				epass.setId(Long.valueOf(node.getProperty("id").getString()));
				epass.setName(node.getProperty("name").getString());
				epass.setFathername(node.getProperty("fathername").getString());
				epass.setPhone(node.getProperty("phone").getString());
				epass.setEmail(node.getProperty("email").getString());
				epass.setDistrict(node.getProperty("district").getString());
				epass.setAddtype(node.getProperty("addtype").getString());
				epass.setAddress(node.getProperty("address").getString());
				epass.setGraampanchayat(node.hasProperty("graampanchayat")?node.getProperty("graampanchayat").getString():"");
				epass.setBlock(node.hasProperty("block")?node.getProperty("block").getString():"");
				epass.setCity(node.hasProperty("city")?node.getProperty("city").getString():"");
				epass.setPincode(node.hasProperty("pincode")?node.getProperty("pincode").getString():"");
				epass.setSelectId(node.getProperty("selectid").getString());
				epass.setVehicleNumber(node.getProperty("vehicleNumber").getString());
				epass.setVehicletype(node.getProperty("vehicletype").getString());
				epass.setFromDate((GregorianCalendar) node.getProperty("fromDate").getDate());
				epass.setToDate((GregorianCalendar) node.getProperty("toDate").getDate());
				epass.setDestinationtype(node.getProperty("destinationtype").getString());
				epass.setPurpose(node.getProperty("purpose").getString());
				epass.setDepartment(node.getProperty("department").getString());
				workItem.getWorkflow().getWorkflowData().getMetaDataMap().put("epass", epass);
				if (null != epass) {
					groupOrUser = "covid-group-" + epass.getDistrict() + "-" + epass.getDepartment();
				}
				return groupOrUser;
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return "admin";
	}

}
