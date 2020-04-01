/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.nexteon.raj.covid.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Reference;

import com.nexteon.raj.covid.core.db.EPassService;
import com.nexteon.raj.covid.core.entity.EPass;

@Model(adaptables = {SlingHttpServletRequest.class},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EPassRenderModel {

	@SlingObject
	private SlingHttpServletRequest request;
	
	@OSGiService
	private EPassService epassService;

	private boolean isWF;
	
	private EPass epass;
	
	public boolean isWF() {
		String selectors = request.getRequestPathInfo().getSelectorString();
		isWF = null != selectors && !StringUtils.isEmpty(selectors);
		return isWF;
	}
	public void setWF(boolean isWF) {
		this.isWF = isWF;
	}
	public EPass getEpass() {
		String selectors = request.getRequestPathInfo().getSelectorString();
		if(null != selectors && !selectors.equals("") ) {
			epass = epassService.getEPassData(Long.valueOf(selectors), "epass");
		}
		return epass;
	}
	public void setEpass(EPass epass) {
		this.epass = epass;
	}
	
}
