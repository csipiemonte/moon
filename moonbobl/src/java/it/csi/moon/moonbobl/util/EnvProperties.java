/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class EnvProperties {
	
	private final static String CLASS_NAME = "EnvProperties";
	private static Logger log = LoggerAccessor.getLoggerApplication();
	
	/**
	 * Ricava il tipo invio dal file env.properties in funzione dell
	 * ambinete test presub prod
	 **/
	
	public final static String VERSION = "version";
	public final static String BUILD_TIME = "build.time";
	public static final String TARGET_LINE = "target";
	
	public final static String MOONSRV_ENDPOINT = "moonsrv.endpoint";
	public final static String MOONSRV_USERNAME = "moonsrv.username";
	public final static String MOONSRV_PASSWORD = "moonsrv.password";
	public final static String MOONSRV_TIMEOUT = "moonsrv.timeout";
	
	public static final String AUDIT_ID_APP = "audit.id.app";
	public final static String DEV_MODE = "devmode";
	
	public final static String LOGOUT_URL_PREFIX = "logoutURL."; // seguito del nome portale completo
	
	public final static String MOONPRINT_ENDPOINT = "moonprint.endpoint";
	public final static String MOONPRINT_USERNAME = "moonprint.username";
	public final static String MOONPRINT_PASSWORD = "moonprint.password";
	public final static String MOONPRINT_TIMEOUT = "moonprint.timeout";
	
	
	public static String readFromFile(String param) {
		Properties properties = new Properties();
		InputStream stream = Constants.class.getClassLoader()
				.getResourceAsStream("/env.properties");
		String ret = "";
		try {
			properties.load(stream);
			ret = properties.getProperty(param);
			if (MOONSRV_ENDPOINT.equals(param)) {
				log.info("["+CLASS_NAME+"::readFromFile param:" + param + " ret:" + ret );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}
