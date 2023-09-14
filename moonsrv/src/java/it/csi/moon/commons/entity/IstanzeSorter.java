/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import it.csi.moon.commons.dto.Istanza;

/**
 * Filter DTO usato per ordinare il risulato delle ricerche delle istanze
 * <br>Example: /istanze?sort=-modified,+attoreIns
 * <br>Il nome nella URI deve corrispondere al quello del DB non del JSON
 * <br>Viene qui trasfmormato da camelCase a lowerCaseUnderscore
 * <br>
 * <ul>
 *   <li>idIstanza</li>
 *   <li>codiceIstanza</li>
 *   <li>dataCreazione</li>
 *   <li>attoreIns</li>
 *   <li>attoreUpd</li>
 * </ul>
 * @see Istanza
 * @see IstanzaDAO
 * 
 * @author Laurent
 *
 * @since 1.0.0
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

