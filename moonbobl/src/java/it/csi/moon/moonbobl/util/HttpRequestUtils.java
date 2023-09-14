/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util;

import java.io.UnsupportedEncodingException;
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


public class HttpRequestUtils {

	private final static String CLASS_NAME = "HttpRequestUtils";
    private final static Logger log = LoggerAccessor.getLoggerApplication();
	
	// Header di authenticazione
	public static final String HEADER_SHIB_ID_MARKER = "Shib-Iride-IdentitaDigitale";
	public final static String HEADER_MOON_ID_JWT = "Moon-Identita-JWT";
	
	// Header details from Shibboleth
	public static final String HEADER_SHIB_IDENTITA_PROVIDER = "Shib-Identita-Provider";
	public static final String HEADER_SHIB_JWT = "Shib-Identita-JWT";
	public static final String HEADER_SHIB_MAIL = "Shib-Mail";
	
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
		Arrays.asList("Shib-Identity-Provider", "Shib-Identity-Method", "Shib-Identity-Handler", "Shib-Identita-LivAuth", "Shib-Identita-loa",
			"Codice-identificativo-SPID", "Codice-fiscale-SPID",
			"Shib-Identita-loa","Shib-idpUrl","Shib-community","Shib-Identita-Riscontro","Shib-Identita-Matricola","Shib-Mail","Shib-Tipo-Risorsa"
        	)));
	
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
		return getHeaderOrParameter(httpreq, HEADER_SHIB_ID_MARKER, devmode);
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
	
	public String getHeaderOrParameter(HttpServletRequest httpreq, String token, boolean devmode) {
		String marker = (String) httpreq.getHeader(token);
		if (marker == null) {
//			logInfoAllHeaders(httpreq);
//			if(devmode) {
				marker = getRequestParameter(httpreq, token);
//			}
		}
		if (StringUtils.isEmpty(marker)) {
			log.warn("[" + CLASS_NAME + "::getToken] token '" + token + "' NOT FOUND !");
			return null;
		}
		 return marker;
		// return encodeTokenUTF8(marker);
	}
	
	private String encodeTokenUTF8(String marker) {
		try {
			// gestione dell'encoding  -- "ISO-8859-1"
			String decodedMarker = new String(marker.getBytes("ISO-8859-1"), "UTF-8");
			return decodedMarker;
		} catch (java.io.UnsupportedEncodingException e) {
			log.warn("[" + CLASS_NAME + "::encodeTokenUTF8] UnsupportedEncodingException ISO-8859-1 to UTF-8");
			// se la decodifica non funziona comunque sempre meglio restituire 
			// il marker originale non decodificato
			return marker;
		}
	}

	/**
	 * Recupero dell'identita Shibbolteh dagli headers della richiesta http 'Shib-Iride-IdentitaDigitale'
	 * 
	 * @param httpreq l'HTTP request da dove estrarre gli headers
	 * @return la string del token Shib-Iride-IdentitaDigitale corrispondente all'identita digitale Shibboleth
	 */

	public String getShibToken(HttpServletRequest httpreq) {
		return getHeaderOrParameter(httpreq, HEADER_SHIB_ID_MARKER, false);
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
			log.error("[" + CLASS_NAME + "::retrieveJWT] JWT_ID_MARKER "+HEADER_SHIB_JWT+" NOT FOUND !");
			logInfoAllHeaders(httpreq);
			return null;
		}
		log.debug("[" + CLASS_NAME + "::retrieveJWT] JWT_ID_MARKER "+HEADER_SHIB_JWT+" FOUND : " + result);
		
		try {
			result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
			return result;
		} catch (java.io.UnsupportedEncodingException e) {
			log.warn("[" + CLASS_NAME + "::retrieveJWT] UnsupportedEncodingException ISO-8859-1 to UTF-8");
			return result;
		}
	}
	
	/**
	 * Stampa nel log tutti headers della chiamata http in modalita INFO
	 * @param httpreq l'HTTP request da dove estrarre gli headers
	 */
	public void logInfoAllHeaders(HttpServletRequest httpreq) {
		Enumeration headerNames = httpreq.getHeaderNames();
		while (headerNames.hasMoreElements()) {
		    String key = (String) headerNames.nextElement();
		    String value = httpreq.getHeader(key);
		    log.info("[" + CLASS_NAME + "::logInfoAllHeaders] " + key + " \t\t\t\t==  " + value);
		}
	}
	
	/**
	 * Stampa nel log alcuni headers della chiamata http in modalita DEBUG
	 * @param httpreq l'HTTP request da dove estrarre gli headers
	 */
	public void logDebugPrintHeaders(HttpServletRequest httpreq) {
	    if (!log.isDebugEnabled()) return;
		Enumeration headerNames = httpreq.getHeaderNames();
		while (headerNames.hasMoreElements()) {
		    String key = (String) headerNames.nextElement();
		    if(HEADERS_KEY_TOPRINT.contains(key)) {
			    String value = httpreq.getHeader(key);
			    try {
					value = new String( value.getBytes("ISO-8859-1"), "UTF-8");
			    	log.debug("[" + CLASS_NAME + "::logDebugPrintHeaders] " + key + " \t\t\t\t==  " + value);
				} catch (UnsupportedEncodingException e) {
					log.debug("[" + CLASS_NAME + "::logDebugPrintHeaders] UnsupportedEncodingException for " + key + " \t\t\t\t==  " + value);
				}
		    }
		}
	}
	
	
	public String getShibIdentProvider(final HttpServletRequest httpRequest, boolean devmode) {
		String marker = (String) httpRequest.getHeader(HEADER_SHIB_IDENTITA_PROVIDER);
		if (marker == null && devmode) {
			return getRequestParameter(httpRequest, HEADER_SHIB_IDENTITA_PROVIDER);
		}
		
		if (StringUtils.isEmpty(marker)) {
			log.error("[" + CLASS_NAME + "::getShibIdentProvider] HEADER_SHIB_IDENTITA_PROVIDER "+HEADER_SHIB_IDENTITA_PROVIDER+" NOT FOUND !");
//			logInfoAllHeaders(httpRequest);
			return null;
		}
		log.debug("[" + CLASS_NAME + "::getShibIdentProvider] HEADER_SHIB_IDENTITA_PROVIDER "+HEADER_SHIB_IDENTITA_PROVIDER+" FOUND : " + marker);
		
		try {
			// gestione dell'encoding
			String decodedMarker = new String(marker.getBytes("ISO-8859-1"), "UTF-8");
			return decodedMarker;
		} catch (java.io.UnsupportedEncodingException e) {
			log.warn("[" + CLASS_NAME + "::getShibIdentProvider] UnsupportedEncodingException ISO-8859-1 to UTF-8");
			// se la decodifica non funziona comunque sempre meglio restituire 
			// il marker originale non decodificato
			return marker;
		}
	}
	
	public String retrieveMail(HttpServletRequest httpreq) {
		String result = (String) httpreq.getHeader(HEADER_SHIB_MAIL);
		try {
			if(result!=null) {
				result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
			}
			return result;
		} catch (java.io.UnsupportedEncodingException e) {
			log.warn("[" + CLASS_NAME + "::retrieveMail] UnsupportedEncodingException ISO-8859-1 to UTF-8 "+result);
			return result;
		}
	}

	public String getPortalName(HttpServletRequest httpRequest, boolean devmode) {
		String portalName = httpRequest.getServerName();
		if (devmode && "localhost".equals(portalName)) {
			String simulatePortaleDev = getRequestParameter(httpRequest, HttpRequestUtils.SIMULATE_PORTALE_ID_MARKER);
			log.debug("[" + CLASS_NAME + "::getPortalName] simulatePortaleDev=" + simulatePortaleDev);
			portalName = simulatePortaleDev!=null?simulatePortaleDev:portalName;
		}
		return removeTstPortaleName(portalName);
	}
	public String getPortalName(HttpServletRequest httpRequest) {
		return getPortalName(httpRequest, false);
	}
	
	private String removeTstPortaleName(String serverName) {
		if (StrUtils.isEmpty(serverName)) return null;
		String portalName = serverName;
		Pattern p = Pattern.compile("tst-");
        Matcher m = p.matcher(serverName);
        if(m.find()) {
        	portalName = serverName.substring(4);
        }
        Pattern p2 = Pattern.compile("ts-");
        Matcher m2 = p2.matcher(serverName);
        if(m2.find()) {
        	portalName = serverName.substring(3);
        }
        return portalName;
	}
	
}