/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util;
import org.apache.log4j.Logger;

public class LoggerAccessor {
	
	enum ApplicationLayers {
		INTEGRATION(Constants.INTEGRATION_LAYER),
		BUSINESS(Constants.BUSINESS_LAYER),
		SECURITY(Constants.SECURITY_LAYER);
		
		private String displayName;
    	private ApplicationLayers(String name) {
    		this.displayName = name;
    	}
    	public String toString() {
    		return this.displayName;	
    	}
	}
	
	private static Logger getLogger(String layer) {
		return Logger.getLogger(layer);
	}
	
	public static Logger getLoggerApplication() {
		return getLogger(Constants.COMPONENT_NAME);
	}
	
	public static Logger getLoggerIntegration() {
		return getLogger(Constants.COMPONENT_NAME + "." + Constants.INTEGRATION_LAYER);
	}
	
	public static Logger getLoggerBusiness() {
		return getLogger(Constants.COMPONENT_NAME + "." + Constants.BUSINESS_LAYER);
	}
	
	public static Logger getLoggerSecurity() {
		return getLogger(Constants.COMPONENT_NAME + "." + Constants.SECURITY_LAYER);
	}
	
}
