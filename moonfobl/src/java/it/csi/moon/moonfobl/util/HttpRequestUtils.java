/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import it.csi.moon.commons.util.StrUtils;

public class HttpRequestUtils {

	private static final String CLASS_NAME = "HttpRequestUtils";
    private static final Logger LOG = LoggerAccessor.getLoggerApplication();
	
    private static final String REGEX_LAST_CHAR_IS_SLASH = "\\/$";
    
	private static final String UTF_8 = "UTF-8";
	private static final String ISO_8859_1 = "ISO-8859-1";
	private static final String WINDOWS_1252 = "windows-1252";
    
	// Header di authenticazione
	public static final String HEADER_SHIB_ID_IRIDE = "Shib-Iride-IdentitaDigitale";
	public static final String HEADER_MOON_ID_JWT = "Moon-Identita-JWT";
	
	// Header details from Shibboleth
	public static final String HEADER_SHIB_IDENTITA_PROVIDER = "Shib-Identita-Provider";
	public static final String HEADER_SHIB_JWT = "Shib-Identita-JWT";
	public static final String HEADER_SHIB_MAIL = "Shib-Mail";
	public static final String HEADER_SHIB_CODICEFISCALE = "Shib-Identita-CodiceFiscale";
	public static final String HEADER_SHIB_COGNOME = "Shib-Identita-Cognome";
	public static final String HEADER_SHIB_NOME = "Shib-Identita-Nome";
	public static final String HEADER_SHIB_IDENTITA_MATRICOLA = "Shib-Identita-Matricola";
	public static final String HEADER_SHIB_IDENTITA_RISCONTRO = "Shib-Identita-Riscontro";
	public static final String HEADER_SHIB_IDENTITY_PROVIDER = "Shib-Identity-Provider";
	public static final String HEADER_SHIB_IDENTITY_METHOD = "Shib-Identity-Method";
	public static final String HEADER_SHIB_IDENTITY_HANDLER = "Shib-Identity-Handler";
	public static final String HEADER_SHIB_IDENTITA_LIVAUTH = "Shib-Identita-LivAuth";
	public static final String HEADER_SHIB_IDENTITA_LOA = "Shib-Identita-loa";
	public static final String HEADER_CODICE_IDENTIFICATIVO_SPID = "Codice-identificativo-SPID";
	public static final String HEADER_CODICE_FISCALE_SPID = "Codice-fiscale-SPID";
	public static final String HEADER_SHIB_IDPURL = "Shib-idpUrl";
	public static final String HEADER_SHIB_COMMUNITY = "Shib-community";
	public static final String HEADER_SHIB_TIPO_RISORSA = "Shib-Tipo-Risorsa";
	
	public static final String HEADER_CLIENT_PROFILE = "client-profile";
	public static final String HEADER_MOON_EMBEDDED_PARAMS = "Moon-Embedded-JWT";

	//
	public static final String PARAM_GRUPPO_OPERATORE = "gope";
	public static final String PARAM_CODICE_ENTE = "codice_ente";
	public static final String PARAM_CODICE_MODULO = "codice_modulo";
	public static final String PARAM_CODICE_AMBITO = "amb";

	//
	public static final String SIMULATE_PORTALE_ID_MARKER = "Simulate-Portale";

	private static final Set<String> HEADERS_KEY_TOPRINT = Collections.unmodifiableSet(new HashSet<>(
		Arrays.asList(HEADER_SHIB_IDENTITY_PROVIDER, HEADER_SHIB_IDENTITY_METHOD, HEADER_SHIB_IDENTITY_HANDLER, HEADER_SHIB_IDENTITA_LIVAUTH, HEADER_SHIB_IDENTITA_LOA,
			HEADER_CODICE_IDENTIFICATIVO_SPID, HEADER_CODICE_FISCALE_SPID,
			HEADER_SHIB_IDPURL, HEADER_SHIB_COMMUNITY, HEADER_SHIB_IDENTITA_RISCONTRO, HEADER_SHIB_IDENTITA_MATRICOLA, HEADER_SHIB_MAIL, HEADER_SHIB_TIPO_RISORSA
        	)));

	private static final String TS_PREFIX = "ts-";
	private static final String TST_PREFIX = "tst-";
	private static final Set<String> TST_PREFISSI = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(TST_PREFIX, TS_PREFIX)));
	
	private static final String TABS4 = " \t\t\t\t";

	/**
	 * Recupero di optional query parameters (codice_ente)
	 */
	public String getCodiceEnteRequestParam(HttpServletRequest httpreq) {
		return getRequestParameter(httpreq, PARAM_CODICE_ENTE);
	}
	/**
	 * Recupero di optional query parameters (gope - GruppoOperatore)
	 */
	public String getGruppoOperatoreRequestParam(HttpServletRequest httpreq) {
		return getRequestParameter(httpreq, PARAM_GRUPPO_OPERATORE);
	}
	/**
	 * Recupero dell'identita Shibbolteh dagli headers della richiesta http 'Shib-Iride-IdentitaDigitale'
	 * 
	 * @param httpreq l'HTTP request da dove estrarre gli headers
	 * @return la string del token Shib-Iride-IdentitaDigitale corrispondente all'identita digitale Shibboleth
	 */
	public String getShibToken(HttpServletRequest httpreq, boolean devmode) {
		return getHeaderOrParameter(httpreq, HEADER_SHIB_ID_IRIDE, devmode);
	}
	public String getMoonToken(HttpServletRequest httpreq, boolean devmode) {
		return getHeaderOrParameter(httpreq, HEADER_MOON_ID_JWT, devmode);
	}
	public String getMoonToken(HttpServletRequest httpreq) {
		return getHeaderOrParameter(httpreq, HEADER_MOON_ID_JWT, false);
	}
	
	
	private String getRequestParameter(HttpServletRequest httpreq, String paramName) {
		return (String) httpreq.getParameter(paramName);
	}
	

	public String getHeaderOrParameter(HttpServletRequest httpreq, String key, boolean devmode) {
		String marker = getStringEncoded( httpreq.getHeader(key) );
		if (marker == null) {
			marker = getRequestParameter(httpreq, key);
		}
		if (StringUtils.isEmpty(marker)) {
			LOG.warn("[" + CLASS_NAME + "::getHeaderOrParameter] key '" + key + "' NOT FOUND !");
			return null;
		}
		return marker;
	}

//	private String encodeTokenUTF8(String marker) {
//		try {
//			// gestione dell'encoding  -- "ISO-8859-1"
//			String decodedMarker = new String(marker.getBytes(ISO_8859_1), UTF_8);
//			return decodedMarker;
//		} catch (java.io.UnsupportedEncodingException e) {
//			LOG.warn("[" + CLASS_NAME + "::encodeTokenUTF8] UnsupportedEncodingException ISO-8859-1 to UTF-8");
//			// se la decodifica non funziona comunque sempre meglio restituire 
//			// il marker originale non decodificato
//			return marker;
//		}
//	}

	/**
	 * Recupero dell'identita Shibbolteh dagli headers della richiesta http 'Shib-Iride-IdentitaDigitale'
	 *
	 * @param httpreq l'HTTP request da dove estrarre gli headers
	 * @return la string del token Shib-Iride-IdentitaDigitale corrispondente all'identita digitale Shibboleth
	 */

	public String getShibToken(HttpServletRequest httpreq) {
		return getHeaderOrParameter(httpreq, HEADER_SHIB_ID_IRIDE, false);
	}
	public String getClientProfile(HttpServletRequest httpreq) {
		return getHeaderOrParameter(httpreq, HEADER_CLIENT_PROFILE, false);
	}

	/**
	 * Recupero di optional query parameters (codice_modulo)
	 */
	public String getCodiceModuloRequestParam(HttpServletRequest httpreq) {
		return getRequestParameter(httpreq, PARAM_CODICE_MODULO);
	}
	
	public String getCodiceAmbitoRequestParam(HttpServletRequest httpreq) {
		return getRequestParameter(httpreq, PARAM_CODICE_AMBITO);
	}
	
	/**
	 * Recupero del GASP_ID dagli headers della richiesta http 'Shib-Identita-JWT'
	 * Il parmetro deve essere in questa class optionale.
	 * Non è valorizzato per gli utenti csi.demo, quindi c'è un remapping in seguito in questa class
	 * 
	 * @param httpreq l'HTTP request da dove estrarre gli headers
	 * @return la string del token Shib-Identita-JWT corrispondente al GASP_ID dell'utente collegato, null se non presente
	 */
	public String retrieveJWT(HttpServletRequest httpreq, boolean devmode) {
		String result = (String) httpreq.getHeader(HEADER_SHIB_JWT);
		if (result == null && devmode) {
			return getRequestParameter(httpreq, HEADER_SHIB_JWT);
		}
		
		if (StringUtils.isEmpty(result)) {
			LOG.warn("[" + CLASS_NAME + "::retrieveJWT] JWT_ID_MARKER "+HEADER_SHIB_JWT+" NOT FOUND !");
			logInfoAllHeaders(httpreq);
			return null;
		}
		LOG.debug("[" + CLASS_NAME + "::retrieveJWT] JWT_ID_MARKER "+HEADER_SHIB_JWT+" FOUND : " + result);
		return getStringEncoded(result);
	}


	/**
	 * Stampa nel log tutti headers della chiamata http in modalita INFO
	 * @param httpreq l'HTTP request da dove estrarre gli headers
	 */
	public void logInfoAllHeaders(HttpServletRequest httpreq) {
		Enumeration headerNames = httpreq.getHeaderNames();
		String value = null;
		while (headerNames.hasMoreElements()) {
		    String key = (String) headerNames.nextElement();
		    value = getStringEncoded( httpreq.getHeader(key));
		    LOG.info("[" + CLASS_NAME + "::logInfoAllHeaders] " + key + TABS4 + "==  " + value);
		}
	}
	
	/**
	 * Stampa nel log alcuni headers della chiamata http in modalita DEBUG
	 * @param httpreq l'HTTP request da dove estrarre gli headers
	 */
	public void logDebugPrintHeaders(HttpServletRequest httpreq) {
	    if (!LOG.isDebugEnabled()) return;
		Enumeration headerNames = httpreq.getHeaderNames();
		while (headerNames.hasMoreElements()) {
		    String key = (String) headerNames.nextElement();
		    if(HEADERS_KEY_TOPRINT.contains(key)) {
			    String value = httpreq.getHeader(key);
				value = getStringEncoded(value);
			    LOG.debug("[" + CLASS_NAME + "::logDebugPrintHeaders] " + key + TABS4 + "==  " + value);
		    }
		}
	}
	
	public String getShibIdentProvider(final HttpServletRequest httpRequest, boolean devmode) {
		String marker = (String) httpRequest.getHeader(HEADER_SHIB_IDENTITA_PROVIDER);
		if (marker == null && devmode) {
			return getRequestParameter(httpRequest, HEADER_SHIB_IDENTITA_PROVIDER);
		}
		
		if (StringUtils.isEmpty(marker)) {
			LOG.error("[" + CLASS_NAME + "::getShibIdentProvider] HEADER_SHIB_IDENTITA_PROVIDER "+HEADER_SHIB_IDENTITA_PROVIDER+" NOT FOUND !");
//			logInfoAllHeaders(httpRequest);
			return null;
		}
		LOG.debug("[" + CLASS_NAME + "::getShibIdentProvider] HEADER_SHIB_IDENTITA_PROVIDER "+HEADER_SHIB_IDENTITA_PROVIDER+" FOUND : " + marker);
		return getStringEncoded(marker);
	}

	public String retrieveMail(HttpServletRequest httpreq) {
		return getStringEncoded(httpreq.getHeader(HEADER_SHIB_MAIL));
	}
	public String retrieveCodiceFiscale(HttpServletRequest httpreq) {
		return getStringEncoded(httpreq.getHeader(HEADER_SHIB_CODICEFISCALE));
	}
	public String retrieveCognome(HttpServletRequest httpreq) {
		return getStringEncoded(httpreq.getHeader(HEADER_SHIB_COGNOME));
	}
	public String retrieveNome(HttpServletRequest httpreq) {
		return getStringEncoded(httpreq.getHeader(HEADER_SHIB_NOME));
	}
	public String retrieveMatricola(HttpServletRequest httpreq) {
		return getStringEncoded(httpreq.getHeader(HEADER_SHIB_IDENTITA_MATRICOLA));
	}

	public String getPortalName(HttpServletRequest httpRequest, boolean devmode) {
		String portalName = httpRequest.getServerName();
		if (devmode && "localhost".equals(portalName)) {
			String simulatePortaleDev = getRequestParameter(httpRequest, HttpRequestUtils.SIMULATE_PORTALE_ID_MARKER);
			LOG.debug("[" + CLASS_NAME + "::getPortalName] simulatePortaleDev=" + simulatePortaleDev);
			portalName = simulatePortaleDev!=null?simulatePortaleDev:portalName;
		}
		return removeTstPortaleName(portalName);
	}
	public String getPortalName(HttpServletRequest httpRequest) {
		return getPortalName(httpRequest, false);
	}
	
	public String removeTstPortaleName(String serverName) {
		if (StrUtils.isEmpty(serverName)) return null;
		String portalName = serverName;
		Pattern p = Pattern.compile(TST_PREFIX);
        Matcher m = p.matcher(serverName);
        if(m.find()) {
        	portalName = serverName.substring(4);
        }
        Pattern p2 = Pattern.compile(TS_PREFIX);
        Matcher m2 = p2.matcher(serverName);
        if(m2.find()) {
        	portalName = serverName.substring(3);
        }
        return portalName;
	}

	/**
	 * Remove il 'contesto' dalla RequestURI e l'ultimo / se presente
	 * Es. for input httpRequest.getRequestURI() like /moonfobl/restfacade/be/istanze/
	 * return /restfacade/be/istanze
	 * @param httpRequest
	 * @return path without context for filterCompare
	 */
	public static String extractPathToCompare(HttpServletRequest httpRequest) {
		return httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()).replaceAll(REGEX_LAST_CHAR_IS_SLASH, "");
	}

	protected String getStringEncoded(String value) {
		try {
			return (value==null)?null:new String(value.getBytes(WINDOWS_1252), UTF_8);
		} catch (java.io.UnsupportedEncodingException e) {
			LOG.warn("[" + CLASS_NAME + "::getStringEncoded] UnsupportedEncodingException WINDOWS_1252 to UTF-8 "+value);
			return value;
		}
	}
	
}
