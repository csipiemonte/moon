/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import it.csi.moon.moonfobl.util.HttpRequestUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class RedirectIndexFilter implements Filter {

	private static final String CLASS_NAME = "RedirectIndexFilter";
	private Logger LOG = LoggerAccessor.getLoggerSecurity();
	
	private static final Set<String> ANGULAR_PATHS = Set.of(
    		"home",
    		"manage-form",
    		"notifiche",
    		"forbidden",
    		"error",
    		"aiuto",
    		"cookie",
    		"accessibilita",
    		"privacy"	
   	);
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = HttpRequestUtils.extractPathToCompare(request);
		LOG.debug("[" + CLASS_NAME + "::dofilter]  BEGIN END");
		String[] pathElements = path.split("/");
        boolean allowedPath = (pathElements.length > 1 && ANGULAR_PATHS.contains(pathElements[1]));
		if (allowedPath) {
			LOG.info("[" + CLASS_NAME + "::doFilter] allowedPath " + path);
			try {
				// devo ritornare index.html
				ServletContext sc = request.getSession().getServletContext();
				sc.getRequestDispatcher("/index.html").forward(request, response);
				LOG.info("[" + CLASS_NAME + "::doFilter] servletResponse " + path);
			} catch (IOException e) {
				LOG.error("[" + CLASS_NAME + "::doFilter] IOException " + e.getMessage());
			} catch (ServletException e) {
				LOG.info("[" + CLASS_NAME + "::doFilter] ServletException " + e.getMessage());
			}
		} else {
//			LOG.info("[" + CLASS_NAME + "::doFilter] Url non angular " + path);
			chain.doFilter(servletRequest, servletResponse);
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
