/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.epay.impl;

import it.csi.moon.commons.dto.CreaIuvResponse;
import it.csi.moon.commons.dto.EPayPagoPAParams;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

public interface EpayDelegate {

	public enum ConfKeys {
		CAUSALE("causale", String.class, "@@CODICE_ISTANZA@@", null),
		CODICE_FISCALE_ENTE("codice_fiscale_ente", String.class, "##codice_fiscale_ente##", null),
		TIPO_PAGAMENTO("tipo_pagamento", String.class, "##tipo_pagamento##", null),
		IMPORTO("importo", String.class, "##importo!Number##", null),

		CODICE_FISCALE_PIVA("codice_fiscale_piva", String.class, "##codice_fiscale_piva##", null),
		COGNOME("cognome", String.class, "@@COGNOME@@", null), 
		NOME("nome", String.class, "@@NOME@@", null), 
		RAGIONE_SOCIALE("ragione_sociale", String.class, "##ragione_sociale##", null),
		EMAIL("email", String.class, "##email##", null),
		;

		private <T> ConfKeys(String key, Class<T> clazz, String textDefaultValue, Boolean booleanDefaultValue) {
			this.key = key;
			this.clazz = clazz;
			this.textDefaultValue = textDefaultValue;
			this.booleanDefaultValue = booleanDefaultValue;
		}

		private String key;
		private Class clazz;
		private String textDefaultValue;
		private Boolean booleanDefaultValue;
		
		public String getKey() {
			return key;
		}
		public Class getClazz() {
			return clazz;
		}
		public String getTextDefaultValue() {
			return textDefaultValue;
		} 
		public boolean getBooleanDefaultValue() {
			return booleanDefaultValue;
		}
	};
	
	public CreaIuvResponse creaIUV() throws BusinessException;

	public CreaIuvResponse pagoPA(EPayPagoPAParams pagoPAParams) throws BusinessException;
	
}
