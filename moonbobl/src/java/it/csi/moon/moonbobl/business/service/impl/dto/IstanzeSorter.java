/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;

/**
 * Filter DTO usato per ordinare il risulato delle ricerche delle istanze
 * Example: /istanze?sort=-modified,+attoreIns
 * Il nome nella URI deve corrispondere al quello del DB non del JSON
 * Viene qui trasfmormato da camelCase a lowerCaseUnderscore
 * 
 *  - idIstanza
 *  - codiceIstanza
 *  - dataCreazione
 *  - attoreIns
 *  - attoreUpd
 *  
 * @see Istanza
 * @see IstanzaDAO
 * 
 * @author Laurent
 *
 */
public class IstanzeSorter {
	
	private String uriSortParameter;
	private List<String> orderByFields;
	
	/**
	 * Construtor called from extern IstanzeSorterBuilder
	 * @param uriSortParameter
	 * @param orderByFields
	 */
	protected IstanzeSorter(String uriSortParameter, List<String> orderByFields) {
		this.uriSortParameter = uriSortParameter;
		this.orderByFields = orderByFields;
	}

	public String getUriSortParameter() {
		return uriSortParameter;
	}
	
	public List<String> getOrderByFields() {
		return orderByFields;
	}

	public String getOrderByFieldsJoinedForSQL() {
		return String.join(", ", orderByFields);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("uriSortParameter", uriSortParameter)
			.append("orderByFields",orderByFields)
			.toString();
	}
}
