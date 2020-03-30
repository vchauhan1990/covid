package com.nexteon.raj.covid.core.constants;

public class PropertyConstants {

	public static final String SLING_SERVLET_METHODS = "sling.servlet.methods";
	public static final String SLING_SERVLET_PATHS = "sling.servlet.paths";
	
	public static final String E_PASS_SERVLET_URL = "/bin/aem/raj/coid/epass";
	public static final String E_PASS_WORKFLOW_SERVLET_URL = "/bin/aem/raj/coid/run/workflow";
	public static final String CREATE_USER_SERVLET_URL = "/bin/aem/raj/coid/users";
	
	public static final String AUTHOR_PAYLOAD_PAGE_PATH = "/content/raj/covidinfo/hi/e-pass-form";
	public static final String AUTHOR_SUCCESS_PAGE_PATH = "/content/raj/covidinfo/hi/success.html";
	public static final String AUTHOR_ERROR_PAGE_PATH = "/content/raj/covidinfo/hi/error.html";
	public static final String AUTHOR_SERVER_EPASS_NODE_RESOURCE = "/etc/covid/epass/data";
		
	public static final String NODE_PROP_NT_UNSTRUCTURED = "nt:unstructured";
	public static final String NODE_PROP_JCR_PATH = "JCR_PATH";
	public static final String FORWARD_SLASH = "/";
	
	public static final String CONTENT_TYPE_HEADER = "Content-Type";
	public static final String APPLICATION_JSON = "application/json";
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";
	
	public static final String EPASS_APPROVAL_WORKFLOW = "/var/workflow/models/rajasthan-covid-epass-approval";
	public static final String DAM_UPLOAD_BASE_PATH = "/content/dam/raj/covid/users-data/";
	
	
}
