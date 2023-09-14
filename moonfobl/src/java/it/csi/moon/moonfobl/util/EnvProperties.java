/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class EnvProperties {
	
	private static final String CLASS_NAME = "EnvProperties";
	private static final Logger LOG = LoggerAccessor.getLoggerApplication();
	
	/**
	 * Ricava il tipo invio dal file env.properties in funzione dell ambinete (dev,test,coll,prod)
	 **/
	public static final String VERSION = "version";
	public static final String BUILD_TIME = "build.time";
	public static final String TARGET_LINE = "target";
	
	public static final String MOONSRV_ENDPOINT = "moonsrv.endpoint";
	public static final String MOONSRV_USERNAME = "moonsrv.username";
	public static final String MOONSRV_PASSWORD = "moonsrv.password";
	public static final String MOONSRV_TIMEOUT = "moonsrv.timeout";
	
	public static final String AUDIT_ID_APP = "audit.id.app";
	public static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
	public static final String DEV_MODE = "devmode";
	public static final String TSTMODE_REMAPIDENTITA = "tstmode.remapidentita";
	
	public static final String LOGOUT_URL_PREFIX = "logoutURL."; // seguito del nome portale completo
	
	
	public static String readFromFile(String param) {
		Properties properties = new Properties();
		InputStream stream = Constants.class.getClassLoader()
				.getResourceAsStream("/env.properties");
		String ret = "";
		try {
			properties.load(stream);
			ret = properties.getProperty(param);
			if (MOONSRV_ENDPOINT.equals(param)) {
				LOG.info("["+CLASS_NAME+"::readFromFile param:" + param + " ret:" + ret );
			}
		} catch (IOException e) {
			LOG.error("["+CLASS_NAME+"::readFromFile param:" + param + " ret:" + ret, e);
		}
		return ret;
	}
	
	public static String[] readArrayFromFile(String param) {
		Properties properties = new Properties();
		InputStream stream = EnvProperties.class.getClassLoader()
				.getResourceAsStream("/env.properties");
		String str = "";
		String ret[] = new String[0];
		try {
			properties.load(stream);
			str = properties.getProperty(param);
//			if (param.endsWith(".endpoint") && LOG.isDebugEnabled()) {
//				LOG.debug(CLASS_NAME+"::readListFromFile param:" + param + " str:" + str );
//			}
			ret = Arrays.asList(str.split(",")).stream()
				.filter(x -> !StringUtils.isBlank(x))
				.toArray(String[]::new);
		} catch (IOException e) {
			LOG.error(CLASS_NAME+"::readreadListFromFileFromFile param:" + param + " str:" + str, e);
		}
		return ret;
	}
	
	public static List<String> readListFromFile(String param) {
		Properties properties = new Properties();
		InputStream stream = EnvProperties.class.getClassLoader()
				.getResourceAsStream("/env.properties");
		String str = "";
		List<String> ret = new ArrayList<>();
		try {
			properties.load(stream);
			str = properties.getProperty(param);
//			if (param.endsWith(".endpoint") && LOG.isDebugEnabled()) {
//				LOG.debug(CLASS_NAME+"::readListFromFile param:" + param + " str:" + str );
//			}
			ret = Arrays.asList(str.split(",")).stream()
				.filter(x -> !StringUtils.isBlank(x))
				.collect(Collectors.toList());
		} catch (IOException e) {
			LOG.error(CLASS_NAME+"::readreadListFromFileFromFile param:" + param + " str:" + str, e);
		}
		return ret;
	}
	
	public static Integer readIntegerFromFile(String param) {
		String retStr = null;
		Integer ret = null;
		try {
			retStr = readFromFile(param);
			if (retStr!=null) {
				ret = Integer.parseInt(retStr.trim());
			}
		} catch (NumberFormatException e) {
			LOG.debug(CLASS_NAME+"::readFromFile NumberFormatException for param: " + param + "  value: " + retStr );
			// rimane null
		}
		return ret;
	}
	
}
