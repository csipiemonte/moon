/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import it.csi.moon.commons.dto.DatiAggiuntiviHeaders;

public class HeadersUtils {
	
	private static final String CLASS_NAME = "HeadersUtils";
	
	private static final String HEADER_NOREDIRECT = "No-Redirect";
	private static final String HEADER_CODICEIDENTIFICATIVOSPID = "Codice-identificativo-SPID";
	private static final String HEADER_HOST = "Host";
	private static final String HEADER_SHIBAUTHENTICATIONINSTANT = "Shib-Authentication-Instant";
	private static final String HEADER_SHIBCOMMUNITY = "Shib-community";
	private static final String HEADER_SHIBDATADINASCITA = "Shib-Data-di-nascita";
	private static final String HEADER_SHIBHANDLER = "Shib-Handler";
	private static final String HEADER_SHIBIDENTITACODICEFISCALE = "Shib-Identita-CodiceFiscale";
	private static final String HEADER_SHIBIDENTITACOGNOME = "Shib-Identita-Cognome";
	private static final String HEADER_SHIBIDENTITALIVAUTH = "Shib-Identita-LivAuth";
	private static final String HEADER_SHIBIDENTITALOA = "Shib-Identita-loa";
	private static final String HEADER_SHIBIDENTITAMATRICOLA = "Shib-Identita-Matricola";
	private static final String HEADER_SHIBIDENTITANOME = "Shib-Identita-Nome";
	private static final String HEADER_SHIBIDENTITAPROVIDER = "Shib-Identita-Provider";
	private static final String HEADER_SHIBIDENTITARISCONTRO = "Shib-Identita-Riscontro";
	private static final String HEADER_SHIBIDENTITATIMESTAMP = "Shib-Identita-TimeStamp";
	private static final String HEADER_SHIBIDENTITYPROVIDER = "Shib-Identity-Provider";
	private static final String HEADER_SHIBMAIL = "Shib-Mail";
	private static final String HEADER_SHIBEMAIL = "Shib-Email";
	private static final String HEADER_SHIBMOBILEPHONE = "Shib-Mobile-Phone";
	private static final String HEADER_SHIBTIPORISORSA = "Shib-Tipo-Risorsa";
	
	/**
	 * Legge gli Headers da salvare con l'istanza in DatiAggiuntiviHeaders
	 * 
	 * @param istanza
	 * @throws Exception
	 */
	public static DatiAggiuntiviHeaders readFromHeaders(HttpHeaders httpHeaders) {
		DatiAggiuntiviHeaders result = new DatiAggiuntiviHeaders();
		result.setCodiceidentificativoSPID(retrieveFromHeaders(httpHeaders,HEADER_CODICEIDENTIFICATIVOSPID));
		result.setHost(retrieveFromHeaders(httpHeaders,HEADER_HOST));
		result.setShibAuthenticationInstant(retrieveFromHeaders(httpHeaders,HEADER_SHIBAUTHENTICATIONINSTANT));
		result.setShibcommunity(retrieveFromHeaders(httpHeaders,HEADER_SHIBCOMMUNITY));
		result.setShibDatadinascita(retrieveFromHeaders(httpHeaders,HEADER_SHIBDATADINASCITA));
		result.setShibHandler(retrieveFromHeaders(httpHeaders,HEADER_SHIBHANDLER));
		result.setShibIdentitaCodiceFiscale(retrieveFromHeaders(httpHeaders,HEADER_SHIBIDENTITACODICEFISCALE));
		result.setShibIdentitaCognome(retrieveFromHeaders(httpHeaders,HEADER_SHIBIDENTITACOGNOME));
		result.setShibIdentitaLivAuth(retrieveFromHeaders(httpHeaders,HEADER_SHIBIDENTITALIVAUTH));
		result.setShibIdentitaloa(retrieveFromHeaders(httpHeaders,HEADER_SHIBIDENTITALOA));
		result.setShibIdentitaMatricola(retrieveFromHeaders(httpHeaders,HEADER_SHIBIDENTITAMATRICOLA));
		result.setShibIdentitaNome(retrieveFromHeaders(httpHeaders,HEADER_SHIBIDENTITANOME));
		result.setShibIdentitaProvider(retrieveFromHeaders(httpHeaders,HEADER_SHIBIDENTITAPROVIDER));
		result.setShibIdentitaRiscontro(retrieveFromHeaders(httpHeaders,HEADER_SHIBIDENTITARISCONTRO));
		result.setShibIdentitaTimeStamp(retrieveFromHeaders(httpHeaders,HEADER_SHIBIDENTITATIMESTAMP));
		result.setShibIdentityProvider(retrieveFromHeaders(httpHeaders,HEADER_SHIBIDENTITYPROVIDER));
		result.setShibMail(retrieveFromHeaders(httpHeaders,HEADER_SHIBMAIL));
		result.setShibEmail(retrieveFromHeaders(httpHeaders,HEADER_SHIBEMAIL));
		result.setShibMobilePhone(retrieveFromHeaders(httpHeaders,HEADER_SHIBMOBILEPHONE));
		result.setShibTipoRisorsa(retrieveFromHeaders(httpHeaders,HEADER_SHIBTIPORISORSA));
		return result;
	}
	
	private static String retrieveFromHeaders(HttpHeaders httpHeaders, String headerKey) {
		String result = null;
		List<String> values = httpHeaders.getRequestHeader(headerKey);
		if (values==null || values.size()<1) {
			return null;
		}
		try {
			result = new String(values.get(0).getBytes("ISO-8859-1"), "UTF-8");
			return result;
		} catch (java.io.UnsupportedEncodingException e) {
			return result;
		}
	}
	
	public static String retrieveHeaderNoRedirect(HttpHeaders httpHeaders) {
		return retrieveFromHeaders(httpHeaders, HEADER_NOREDIRECT);
	}
	
}
