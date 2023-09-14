/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util;

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
	
	public static final String MOONPRINT_ENDPOINT = "moonprint.endpoint";
	public static final String MOONPRINT_USERNAME = "moonprint.username";
	public static final String MOONPRINT_PASSWORD = "moonprint.password";
	public static final String MOONPRINT_TIMEOUT = "moonprint.timeout";
	
	public static final String APIMAN_ENDPOINT = "apiman.endpoint";
	public static final String APIMAN_PROD_CONSUMERKEY = "apiman.prod.consumerKey";
	public static final String APIMAN_PROD_CONSUMERSECRET = "apiman.prod.consumerSecret";
	public static final String APIMAN_SANDBOX_CONSUMERKEY = "apiman.sandbox.consumerKey";
	public static final String APIMAN_SANDBOX_CONSUMERSECRET = "apiman.sandbox.consumerSecret";
	public static final String APIMAN_TIMEOUT = "apiman.timeout";
	
	public static final String APIMINT_ENDPOINT = "apimint.endpoint";
	public static final String APIMINT_PROD_CONSUMERKEY = "apimint.prod.consumerKey";
	public static final String APIMINT_PROD_CONSUMERSECRET = "apimint.prod.consumerSecret";
	public static final String APIMINT_SANDBOX_CONSUMERKEY = "apimint.sandbox.consumerKey";
	public static final String APIMINT_SANDBOX_CONSUMERSECRET = "apimint.sandbox.consumerSecret";
	public static final String APIMINT_TIMEOUT = "apimint.timeout";

	public static final String APIMONIT_APIMINT_URL = "apimonit.apimint.url";
	public static final String APIMONIT_APIMINT_CONSUMERKEY = "apimonit.apimint.consumerKey";
	public static final String APIMONIT_APIMINT_CONSUMERSECRET = "apimonit.apimint.consumerSecret";
	
	public static final String APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO = "demografia.anpr.clientProfile.cittadino";
	public static final String APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_OPERATORE = "demografia.anpr.clientProfile.operatore";
	public static final String APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_OPERATORE_AUTH = "demografia.anpr.clientProfile.operatore.auth";
	public static final String APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_NAZ_CITTADINO = "demografia.anpr.clientProfile.naz.cittadino";
	public static final String APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_NAZ_OPERATORE = "demografia.anpr.clientProfile.naz.operatore";
	public static final String APIMINT_TOPONOMASTICA_APP_ID = "toponomastica.appId";
	
	public static final String TOPO_APPLOGIC_ENDPOINT = "toponomastica.applogic.endpoint";
	public static final String TOPO_APPLOGIC_USERNAME = "toponomastica.applogic.username";
	public static final String TOPO_APPLOGIC_PASSWORD = "toponomastica.applogic.password";
	public static final String TOPO_APPLOGIC_TIMEOUT = "toponomastica.applogic.timeout";
	
	public static final String NOTIFY_ENDPOINT = "notify.endpoint";
	public static final String NOTIFY_PREFERENCES_ENDPOINT = "notify.preferences.endpoint";
	public static final String NOTIFY_STATUS_ENDPOINT = "notify.status.endpoint";
	public static final String NOTIFY_TOKEN = "notify.token";
	public static final String NOTIFY_TOKEN_PUSH = "notify.token_push";
	public static final String NOTIFY_SERVICE_NAME = "notify.service.name";
	
	public static final String MAIL_SUBJECT_PREFIX_AMBIENTE = "mail.suject.prefix.ambiente";
	
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_PORT = "mail.smtp.port";
	public static final String MAIL_SMTP_FROM = "mail.smtp.from";
	public static final String MAIL_SMTP_CONNECTIONTIMEOUT = "mail.smtp.connectiontimeout";
	public static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String MAIL_SMTP_AUTH_USERNAME = "mail.smtp.auth.username";
	public static final String MAIL_SMTP_AUTH_PASSWORD = "mail.smtp.auth.password";
	
	public static final String MAIL_SMTPS_HOST = "mail.smtps.host";
	public static final String MAIL_SMTPS_PORT = "mail.smtps.port";
	public static final String MAIL_SMTPS_FROM = "mail.smtps.from";
	public static final String MAIL_SMTPS_CONNECTIONTIMEOUT = "mail.smtps.connectiontimeout";
	public static final String MAIL_SMTPS_TIMEOUT = "mail.smtps.timeout";
	public static final String MAIL_SMTPS_AUTH = "mail.smtps.auth";
	public static final String MAIL_SMTPS_AUTH_USERNAME = "mail.smtps.auth.username";
	public static final String MAIL_SMTPS_AUTH_PASSWORD = "mail.smtps.auth.password";
	
	public static final String FTP_SERVER = "ftp.server";
	public static final String FTP_PORT = "ftp.port";
	public static final String FTP_USER = "ftp.user";
	public static final String FTP_PASSWORD = "ftp.password";
			
	public static final String PROTOCOLLO_STARDAS_WSDL = "protocollo.stardas.wsdl";
	public static final String PROTOCOLLO_STARDAS_APIMINT_PATH = "protocollo.stardas.apimint.path";

	public static final String WF_COSMO_APIMINT_ENDPOINT = "wf.cosmo.apimint.endpoint";
	public static final String WF_COSMO_APPLOGIC_ENDPOINT = "wf.cosmo.applogic.endpoint";
	public static final String WF_COSMO_APPLOGIC_USERNAME = "wf.cosmo.applogic.username";
	public static final String WF_COSMO_APPLOGIC_PASSWORD = "wf.cosmo.applogic.password";
	public static final String WF_COSMO_APPLOGIC_TIMEOUT = "wf.cosmo.applogic.timeout";

	public static final String TS_TROUBLETICKETING_USE_SANDBOX = "ts.troubleticketing.use.sandbox";
	public static final String TS_TROUBLETICKETING_APIMINT_PATH = "ts.troubleticketing.apimint.path";
	public static final String TS_OTRS_ENDPOINT = "ts.otrs.endpoint";
	public static final String TS_OTRS_USERNAME = "ts.otrs.username";
	public static final String TS_OTRS_PASSWORD = "ts.otrs.password";
	
	public static final String MOONFO_JMS_NAME = "moonfo.jms.name";
	
	public static final String EPAY_IUV_REST_APPLOGIC_ENDPOINT = "epay.iuv.applogic.endpoint";
	public static final String EPAY_IUV_REST_APPLOGIC_USERNAME = "epay.iuv.applogic.username";
	public static final String EPAY_IUV_REST_APPLOGIC_PASSWORD = "epay.iuv.applogic.password";
	public static final String EPAY_IUV_REST_APPLOGIC_TIMEOUT = "epay.iuv.applogic.timeout";
	
	public static final String AUDIT_ID_APP = "audit.id.app";
	public static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
	public static final String TMP_PATH_FS = "tmp.path.fs";
	
	public static final String DOQUI_INDEX_ENDPOINT="doqui.index.endpoint";
	public static final String DOQUI_INDEX_TENANT="doqui.index.tenant";
	public static final String DOQUI_INDEX_PASSWORD="doqui.index.password";
	public static final String DOQUI_INDEX_FRUITORE="doqui.index.fruitore";
	public static final String DOQUI_INDEX_ROOT="doqui.index.root";
	
	public static final String MAGGIOLI_PRT_SOAP_ENDPOINT = "maggioli.prt.soap.endpoint";
	
	public static final String PRAEDI_ENDPOINT = "praedi.endpoint";
	public static final String PRAEDI_USR = "praedi.username";
	public static final String PRAEDI_PWD = "praedi.password";
	public static final String PRAEDI_ENTE = "praedi.ente";
	public static final String PRAEDI_METHOD = "praedi.method";
	
	
	public static String readFromFile(String param) {
		Properties properties = new Properties();
		InputStream stream = EnvProperties.class.getClassLoader()
				.getResourceAsStream("/env.properties");
		String ret = "";
		try {
			properties.load(stream);
			ret = properties.getProperty(param);
//			if (param.endsWith(".endpoint") && LOG.isDebugEnabled()) {
//				LOG.debug(CLASS_NAME+"::readFromFile param:" + param + " ret:" + ret );
//			}
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
