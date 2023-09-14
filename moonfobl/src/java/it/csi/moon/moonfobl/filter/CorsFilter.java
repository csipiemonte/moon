/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

	private static final String ENABLECORS_INIT_PARAM = "enablecors";
	private static final String ORIGINCORS_INIT_PARAM = "origincors";
	private boolean enableCors = false;
	private String originCors = "*"; // Per CITFAC deve essere *

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		if (enableCors) {
			HttpServletResponse res = (HttpServletResponse) servletResponse;
			res.setHeader("Access-Control-Allow-Origin", originCors); // Per CITFAC deve essere *
			res.setHeader("Access-Control-Allow-Headers", "*");
			res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");			
		}
		chain.doFilter(servletRequest, servletResponse);

	}

	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String sEnableCors = filterConfig.getInitParameter(ENABLECORS_INIT_PARAM);
		if ("true".equals(sEnableCors)) {
			enableCors = true;
		} else {
			enableCors = false;
		}
		originCors = filterConfig.getInitParameter(ORIGINCORS_INIT_PARAM);
	}

	@Override
	public void destroy() {
	}

}