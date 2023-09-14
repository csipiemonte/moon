/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import it.csi.moon.moonbobl.dto.moonfobl.DatiAggiuntiviHeaders;

public class HeadersUtils {
	
	private static final String CLASS_NAME = "HeadersUtils";
	
	private static final String HEADER_CodiceidentificativoSPID = "Codice-identificativo-SPID";
	private static final String HEADER_Host = "Host";
	private static final String HEADER_ShibAuthenticationInstant = "Shib-Authentication-Instant";
	private static final String HEADER_Shibcommunity = "Shib-community";
	private static final String HEADER_ShibDatadinascita = "Shib-Data-di-nascita";
	private static final String HEADER_ShibHandler = "Shib-Handler";
	private static final String HEADER_ShibIdentitaCodiceFiscale = "Shib-Identita-CodiceFiscale";
	private static final String HEADER_ShibIdentitaCognome = "Shib-Identita-Cognome";
	private static final String HEADER_ShibIdentitaLivAuth = "Shib-Identita-LivAuth";
	private static final String HEADER_ShibIdentitaloa = "Shib-Identita-loa";
	private static final String HEADER_ShibIdentitaMatricola = "Shib-Identita-Matricola";
	private static final String HEADER_ShibIdentitaNome = "Shib-Identita-Nome";
	private static final String HEADER_ShibIdentitaProvider = "Shib-Identita-Provider";
	private static final String HEADER_ShibIdentitaRiscontro = "Shib-Identita-Riscontro";
	private static final String HEADER_ShibIdentitaTimeStamp = "Shib-Identita-TimeStamp";
	private static final String HEADER_ShibIdentityProvider = "Shib-Identity-Provider";
	private static final String HEADER_ShibMail = "Shib-Mail";
	private static final String HEADER_ShibMobilePhone = "Shib-Mobile-Phone";
	private static final String HEADER_ShibTipoRisorsa = "Shib-Tipo-Risorsa";
	
	/**
	 * Legge gli Headers da salvare con l'istanza in DatiAggiuntiviHeaders
	 * 
	 * @param istanza
	 * @throws Exception
	 */
	public static DatiAggiuntiviHeaders readFromHeaders(HttpHeaders httpHeaders)  {
//		try {
			DatiAggiuntiviHeaders result = new DatiAggiuntiviHeaders();
			result.setCodiceidentificativoSPID(retrieveFromHeaders(httpHeaders,HEADER_CodiceidentificativoSPID));
			result.setHost(retrieveFromHeaders(httpHeaders,HEADER_Host));
			result.setShibAuthenticationInstant(retrieveFromHeaders(httpHeaders,HEADER_ShibAuthenticationInstant));
			result.setShibcommunity(retrieveFromHeaders(httpHeaders,HEADER_Shibcommunity));
			result.setShibDatadinascita(retrieveFromHeaders(httpHeaders,HEADER_ShibDatadinascita));
			result.setShibHandler(retrieveFromHeaders(httpHeaders,HEADER_ShibHandler));
			result.setShibIdentitaCodiceFiscale(retrieveFromHeaders(httpHeaders,HEADER_ShibIdentitaCodiceFiscale));
			result.setShibIdentitaCognome(retrieveFromHeaders(httpHeaders,HEADER_ShibIdentitaCognome));
			result.setShibIdentitaLivAuth(retrieveFromHeaders(httpHeaders,HEADER_ShibIdentitaLivAuth));
			result.setShibIdentitaloa(retrieveFromHeaders(httpHeaders,HEADER_ShibIdentitaloa));
			result.setShibIdentitaMatricola(retrieveFromHeaders(httpHeaders,HEADER_ShibIdentitaMatricola));
			result.setShibIdentitaNome(retrieveFromHeaders(httpHeaders,HEADER_ShibIdentitaNome));
			result.setShibIdentitaProvider(retrieveFromHeaders(httpHeaders,HEADER_ShibIdentitaProvider));
			result.setShibIdentitaRiscontro(retrieveFromHeaders(httpHeaders,HEADER_ShibIdentitaRiscontro));
			result.setShibIdentitaTimeStamp(retrieveFromHeaders(httpHeaders,HEADER_ShibIdentitaTimeStamp));
			result.setShibIdentityProvider(retrieveFromHeaders(httpHeaders,HEADER_ShibIdentityProvider));
			result.setShibMail(retrieveFromHeaders(httpHeaders,HEADER_ShibMail));
			result.setShibMobilePhone(retrieveFromHeaders(httpHeaders,HEADER_ShibMobilePhone));
			result.setShibTipoRisorsa(retrieveFromHeaders(httpHeaders,HEADER_ShibTipoRisorsa));
			return result;
//		} catch (Exception e) {
//		    e.printStackTrace();
//		    throw e;
//		}
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
}
