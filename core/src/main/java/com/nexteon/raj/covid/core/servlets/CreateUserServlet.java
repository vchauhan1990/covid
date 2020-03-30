package com.nexteon.raj.covid.core.servlets;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexteon.raj.covid.core.constants.PropertyConstants;
import com.nexteon.raj.covid.core.entity.CovidUser;

@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=",
		PropertyConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
		PropertyConstants.SLING_SERVLET_PATHS + "=" + PropertyConstants.CREATE_USER_SERVLET_URL})
public class CreateUserServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -7052487346720258973L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserServlet.class);
	
	@Reference
	private ResourceResolverFactory resolverFactory;

	private ResourceResolver resolver = null;
	private Session session = null;
	private Group basegroup = null;
	
	@Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("User Creation Service Called");
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, "writeService");
		
		try {
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while creating the session: {}",e.getMessage());
		}
		
		String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		ObjectMapper mapper = new ObjectMapper();
		List<CovidUser> covidList = Arrays.asList(mapper.readValue(requestBody, CovidUser[].class));
		
		final UserManager userManager= resolver.adaptTo(UserManager.class);
		
		try {
			basegroup = userManager.createGroup("covid-base-group",new SimplePrincipal("covid-base-group"), null);
		} catch (RepositoryException e1) {
			e1.printStackTrace();
		}
		covidList.stream().forEach( covidUser -> {
			try {
				if(null==userManager.getAuthorizable(covidUser.getName())){
					if(covidUser.getType().equals("group")) {
						Group group = userManager.createGroup(covidUser.getName(),new SimplePrincipal(covidUser.getName()), null);
						basegroup.addMember(userManager.getAuthorizable(group.getPrincipal().getName()));
					}
					if(covidUser.getType().equals("user")) {
						User user=userManager.createUser(covidUser.getName(), covidUser.getPassword(),new SimplePrincipal(covidUser.getName()), null);
						Group group = (Group)(userManager.getAuthorizable(covidUser.getGroup()));
				        group.addMember(userManager.getAuthorizable(user.getPrincipal().getName()));
					}
				}
				session.save();
			} catch (RepositoryException e) {
				LOGGER.error("Exception occurred while creating the session: {}",e.getMessage());
			}
		});
		if (resolver != null) resolver.close();
	}
	
	private static class SimplePrincipal implements Principal {
        protected final String name;
        public SimplePrincipal(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("Principal name cannot be blank.");
            }
            this.name = name;
        }
        public String getName() {
            return name;
        }
        @Override
        public int hashCode() {
            return name.hashCode();
        }
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Principal) {
                return name.equals(((Principal) obj).getName());
            }
            return false;
        }
    }
	
}
