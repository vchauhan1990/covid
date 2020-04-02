package com.nexteon.raj.covid.core.entity;

import java.sql.Date;

public class EPass {

	private long id;
	private String name;
	private String fathername;
	private String phone;
	private String email;
	private String addtype;
	private String address;
	private String graampanchayat;
	private String block;
	private String city;
	private String pincode;
	private String purpose;
	private Date fromDate;
	private Date toDate;
	private String district;
	private String vehicleNumber;
	private String vehicletype;
	private String fromJourney;
	private String toJourney;
	private String destinationtype;
	private String department;
	private String selectId;
	private String idPath;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFathername() {
		return fathername;
	}
	public void setFathername(String fathername) {
		this.fathername = fathername;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getIdPath() {
		return idPath;
	}
	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddtype() {
		return addtype;
	}
	public void setAddtype(String addtype) {
		this.addtype = addtype;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}
	public String getFromJourney() {
		return fromJourney;
	}
	public void setFromJourney(String fromJourney) {
		this.fromJourney = fromJourney;
	}
	public String getToJourney() {
		return toJourney;
	}
	public void setToJourney(String toJourney) {
		this.toJourney = toJourney;
	}
	public String getDestinationtype() {
		return destinationtype;
	}
	public void setDestinationtype(String destinationtype) {
		this.destinationtype = destinationtype;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getSelectId() {
		return selectId;
	}
	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}	
	public String getGraampanchayat() {
		return graampanchayat;
	}
	public void setGraampanchayat(String graampanchayat) {
		this.graampanchayat = graampanchayat;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	@Override
	public String toString() {
		return "EPass [id=" + id + ", name=" + name + ", fathername=" + fathername + ", phone=" + phone + ", email="
				+ email + ", addtype=" + addtype + ", address=" + address + ", graampanchayat=" + graampanchayat
				+ ", block=" + block + ", city=" + city + ", pincode=" + pincode + ", purpose=" + purpose
				+ ", fromDate=" + fromDate + ", toDate=" + toDate + ", district=" + district + ", vehicleNumber="
				+ vehicleNumber + ", vehicletype=" + vehicletype + ", fromJourney=" + fromJourney + ", toJourney="
				+ toJourney + ", destinationtype=" + destinationtype + ", department=" + department + ", selectId="
				+ selectId + ", idPath=" + idPath + "]";
	}
	
	
}
