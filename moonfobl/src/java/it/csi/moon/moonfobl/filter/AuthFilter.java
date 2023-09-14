/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import it.csi.moon.moonfobl.util.HttpRequestUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class AuthFilter implements Filter {

	private static final String CLASS_NAME = "AuthFilter";
	private Logger LOG = LoggerAccessor.getLoggerSecurity();
	
    private static final Set<String> ALLOWED_EXACT_PATHS = Collections.unmodifiableSet(new HashSet<>(
        	Arrays.asList("", 
        		"/restfacade/auth/login/idp/gasprp_salute",
        		"/restfacade/auth/login/idp/gasp_regione",
        		"/restfacade/auth/login/idp/gasp_coto",
        		"/restfacade/auth/login/idp/coto",
        		"/restfacade/auth/login/idp/provto",
        		"/restfacade/auth/login/idp/wrup",
        		"/restfacade/auth/login/idp/csi",
        		"/restfacade/auth/login/idp/intranet_icon",
        		"/restfacade/auth/login/request",
        		"/restfacade/auth/login/idp-social/GOOGLE",
        		"/restfacade/auth/login/idp/user-pwd",
        		"/restfacade/auth/login/idp/mode/google",
        		"/restfacade/auth/login/mode", // ModalitaAccessoCompilazioneDiretta like Modulistica
        		//
        		"/restfacade/emb/istanze", // /emb/istanze/{}/view   /emb/istanze/{}/edit
        		"/restfacade/emb/moduli"   // /emb/moduli/{codice_modulo}/{version_modulo}/new
       	)));

//	private static final String DEVMODE_INIT_PARAM = "devmode";
//	
//	private boolean devmode = false;
    
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		LOG.debug("[" + CLASS_NAME + "::dofilter]  BEGIN END");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = HttpRequestUtils.extractPathToCompare(request);
        boolean allowedPath = ALLOWED_EXACT_PATHS.contains(path);
		if (allowedPath) {
			LOG.info("[" + CLASS_NAME + "::doFilter] allowedPath " + path);
			try {
				chain.doFilter(servletRequest, servletResponse);
				LOG.info("[" + CLASS_NAME + "::doFilter] servletResponse " + servletResponse);
			} catch (IOException e) {
				LOG.error("[" + CLASS_NAME + "::doFilter] IOException " + e.getMessage());
			} catch (ServletException e) {
				LOG.error("[" + CLASS_NAME + "::doFilter] ServletException " + e.getMessage());
			}
		} else {
			LOG.error("[" + CLASS_NAME + "::doFilter] UnknownAuthURL " + path);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "MalformedPath");
		}
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
//        
//		String sDevmode = filterConfig.getInitParameter(DEVMODE_INIT_PARAM);
//		devmode = "true".equals(sDevmode)?true:false;
	}

	@Override
	public void destroy() {
	}

}