package com.nexteon.raj.covid.core.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
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
		return epass;
	}
	
}
