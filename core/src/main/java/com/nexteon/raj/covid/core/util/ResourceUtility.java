package com.nexteon.raj.covid.core.util;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.jcr.Node;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexteon.raj.covid.core.constants.PropertyConstants;
import com.nexteon.raj.covid.core.entity.EPass;

public class ResourceUtility {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtility.class);
	
	public static EPass mapRequestToEpass(SlingHttpServletRequest request) throws IOException, ServletException{
		if(request == null) {
			return null;
		}
		EPass epass=new EPass();
		epass.setId(System.currentTimeMillis());
		epass.setName(request.getParameter("name"));
		epass.setFathername(request.getParameter("fathername"));
		epass.setPhone(request.getParameter("phone"));
		epass.setEmail(request.getParameter("email"));
		epass.setDistrict(request.getParameter("district"));
		epass.setAddtype(request.getParameter("addtype"));
		epass.setAddress(request.getParameter("address"));
		epass.setGraampanchayat(request.getParameter("graampanchayat")!=null?request.getParameter("graampanchayat"):"");
		epass.setBlock(request.getParameter("block")!=null?request.getParameter("block"):"");
		epass.setCity(request.getParameter("city")!=null?request.getParameter("city"):"");
		epass.setPincode(request.getParameter("pincode")!=null?request.getParameter("pincode"):"");
		epass.setSelectId(request.getParameter("selectid"));
		epass.setVehicleNumber(request.getParameter("vehicleNumber"));
		epass.setVehicletype(request.getParameter("vehicletype"));
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date date = format.parse(request.getParameter("fromDate"));
			GregorianCalendar fromDate =new GregorianCalendar();
			fromDate.setTime(date);
			epass.setFromDate(fromDate);
			Date date2 = format.parse(request.getParameter("toDate"));
			GregorianCalendar toDate =new GregorianCalendar();
			toDate.setTime(date2);
			epass.setToDate(toDate);
		} catch (Exception e) {
			LOGGER.info("Exception occurred while parsing the date in mapping method: {}", e.getMessage());
			return null;
		}
		epass.setDestinationtype(request.getParameter("destinationtype"));
		epass.setPurpose(request.getParameter("purpose"));
		epass.setDepartment(request.getParameter("department"));
		epass.setFromJourney(request.getParameter("fromJourney"));
		epass.setToJourney(request.getParameter("toJourney"));
		return epass;
	}
	
	
	public static void setValuesInPS(PreparedStatement pstmt, EPass epass) throws SQLException {
		pstmt.setString(1, String.valueOf(epass.getId()));
		pstmt.setString(2, epass.getName());
		pstmt.setString(3, epass.getFathername());
		pstmt.setString(4, epass.getPhone());
		pstmt.setString(5, epass.getEmail());
		pstmt.setString(6, epass.getAddtype());
		pstmt.setString(7, epass.getAddress());
		pstmt.setString(8, epass.getGraampanchayat());
		pstmt.setString(9, epass.getBlock());
		pstmt.setString(10, epass.getCity());
		pstmt.setString(11, epass.getPincode());
		pstmt.setString(12, epass.getPurpose());
		pstmt.setDate(13, new java.sql.Date(epass.getFromDate().getTimeInMillis()));
		pstmt.setDate(14, new java.sql.Date(epass.getToDate().getTimeInMillis()));
		pstmt.setString(15, epass.getDistrict());
		pstmt.setString(16, epass.getVehicleNumber());
		pstmt.setString(17, epass.getVehicletype());
		pstmt.setString(18, epass.getFromJourney());
		pstmt.setString(19, epass.getToJourney());
		pstmt.setString(20, epass.getDestinationtype());
		pstmt.setString(21, epass.getDepartment());
		pstmt.setString(22, epass.getSelectId());
		pstmt.setString(23, epass.getIdPath());
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
	
	public static EPass setEPassFromResultSet(ResultSet rs) throws IOException, ServletException, NumberFormatException, SQLException{
		EPass epass=new EPass();
		epass.setId(Long.valueOf(rs.getString("id")));
		epass.setName(rs.getString("name"));
		epass.setFathername(rs.getString("fathername"));
		epass.setPhone(rs.getString("phone"));
		epass.setEmail(rs.getString("email"));
		epass.setDistrict(rs.getString("district"));
		epass.setAddtype(rs.getString("addtype"));
		epass.setAddress(rs.getString("address"));
		epass.setGraampanchayat(rs.getString("graampanchayat")!=null?rs.getString("graampanchayat"):"");
		epass.setBlock(rs.getString("block")!=null?rs.getString("block"):"");
		epass.setCity(rs.getString("city")!=null?rs.getString("city"):"");
		epass.setPincode(rs.getString("pincode")!=null?rs.getString("pincode"):"");
		epass.setSelectId(rs.getString("selectid"));
		epass.setVehicleNumber(rs.getString("vehicleNumber"));
		epass.setVehicletype(rs.getString("vehicletype"));
		try {
			GregorianCalendar fromDate =new GregorianCalendar();
			fromDate.setTime(rs.getDate("fromDate"));
			epass.setFromDate(fromDate);
			GregorianCalendar toDate =new GregorianCalendar();
			toDate.setTime(rs.getDate("toDate"));
			epass.setToDate(toDate);
		} catch (Exception e) {
			LOGGER.info("Exception occurred while parsing the date in mapping method: {}", e.getMessage());
			return null;
		}
		epass.setDestinationtype(rs.getString("destinationtype"));
		epass.setPurpose(rs.getString("purpose"));
		epass.setDepartment(rs.getString("department"));
		epass.setFromJourney(rs.getString("fromJourney"));
		epass.setToJourney(rs.getString("toJourney"));
		return epass;
	}
}
