/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;

import it.csi.moon.moonbobl.util.LoggerAccessor;

public class SanitizerFilter implements Filter {

	final static Logger LOG = LoggerAccessor.getLoggerSecurity();
	
	public void destroy() {

	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		LOG.debug("[SanitizerFilter::dofilter()]  BEGIN END");
		arg2.doFilter(new XSSRequestWrapper((HttpServletRequest) arg0), arg1);
		
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		LOG.debug("[SanitizerFilter::dofilter()] Init Filtro sanitize");
	}
	
	public static class XSSRequestWrapper extends HttpServletRequestWrapper {
		
		public XSSRequestWrapper(HttpServletRequest servletRequest) {
	        super(servletRequest);
	    }
		private transient Logger log = LoggerAccessor.getLoggerSecurity();
		
		@Override
		public String getRequestURI() {
			String requestUri = super.getRequestURI();
			return stripXSS(requestUri);
		}
		
		@Override
		public StringBuffer getRequestURL() {
			StringBuffer requestUrl = super.getRequestURL();
			String str = requestUrl.toString();
			StringBuffer sb = new StringBuffer();
	        sb.append(stripXSS(str));
			return sb;
		}
		
	    @Override
	    public String[] getParameterValues(String parameter) {
	        String[] values = super.getParameterValues(parameter);
	 
	        if (values == null) {
	            return null;
	        }
	 
	        int count = values.length;
	        String[] encodedValues = new String[count];
	        for (int i = 0; i < count; i++) {
	            encodedValues[i] = stripXSS(values[i]);
	        }
	 
	        return encodedValues;
	    }
	 
	    @Override
	    public String getParameter(String parameter) {
	        String value = super.getParameter(parameter);
	 
	        return stripXSS(value);
	    }
	 
	    @Override
	    public String getHeader(String name) {
	        String value = super.getHeader(name);
	        return stripXSS(value);
	    }
	    
	    @Override
	    public String getQueryString() {
	    	String query = super.getQueryString();
	    	if (query == null || query.length() == 0)
	    		return query;
	    	log.debug("[SanitizerFilter.XSSRequestWrapper::getQueryString()] QueryString to sanitize: " + query);
	    	
	    	if (query.startsWith("?")) {
	    		query = query.substring(1);
	    	}
	    	log.debug("[SanitizerFilter.XSSRequestWrapper::getQueryString()] QueryString Da elaborare: " + query);
	    	
	    	StringBuilder newQuery = new StringBuilder();
	    	String[] parts = query.split("&");
	    	for (String p : parts) {
	    		int pos = p.indexOf('=');
	    		newQuery.append("&");
	    		if (pos > 0) {
	    			try {
						newQuery.append(URLEncoder.encode(stripXSS(URLDecoder.decode(p.substring(0, pos),  "UTF-8")), "UTF-8"));
						newQuery.append("=");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						log.error("[SanitizerFilter.XSSRequestWrapper::getQueryString()] Errore Sanitize queryString " + e.getMessage(),e);
					}
	    		}
	    		try {
					newQuery.append(URLEncoder.encode(stripXSS(URLDecoder.decode(p.substring(pos+1),  "UTF-8")), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					log.error("[SanitizerFilter.XSSRequestWrapper::getQueryString()] Errore Sanitize queryString " + e.getMessage(),e);
				}
	    	}
	    	log.debug("[SanitizerFilter.XSSRequestWrapper::getQueryString()] QueryString elaborata ?" + newQuery.substring(1));
	    	return  newQuery.substring(1);   
	    }
	 
	    private String stripXSS(String value) {
	        if (value != null) {
	            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
	            // avoid encoded attacks.
	            // value = ESAPI.encoder().canonicalize(value);
	 
	            // Avoid null characters
	            value = value.replaceAll("", "");
	 
	            // Avoid anything between script tags
	            Pattern scriptPattern = Pattern.compile("<script\\s*>(.*?)</script>", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");
	 
	            // Avoid anything in a src='...' type of expression
	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	 
	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	 
	            // Remove any lonesome </script> tag
	            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");
	 
	            // Remove any lonesome <script ...> tag
	            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	 
	            // Avoid eval(...) expressions
	            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	 
	            // Avoid expression(...) expressions
	            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	 
	            // Avoid javascript:... expressions
	            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");
	 
	            // Avoid javascript:(html entity encoded)... expressions
	            scriptPattern = Pattern.compile("&#x6a;&#x61;&#x76;&#x61;&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;&#x3a;", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            // Avoid vbscript:... expressions
	            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");
	 
	            // Avoid onload= expressions
	            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	        }
	        return value;
	    }
		
	}
	
}
