/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;

/**
 * Filter DTO usato per ordinare il risulato delle ricerche delle istanze
 * <br>Example: /istanze?sort=-modified,+attoreIns
 * <br>Il nome nella URI deve corrispondere al quello del DB non del JSON
 * <br>Viene qui trasfmormato da camelCase a lowerCaseUnderscore
 * <br>
 *  - idIstanza
 *  - codiceIstanza
 *  - dataCreazione
 *  - modified
 *  - attoreIns
 *  - attoreUpd
 *  
 * @see Istanza
 * @see IstanzaDAO
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzeSorterBuilder {
	private static final String CLASS_NAME= "IstanzeSorterBuilder";
	
	// required parameters
	private String uriSortParameter;
	
	public IstanzeSorterBuilder(String uriSortParameter) {
		this.uriSortParameter = uriSortParameter;
	}
	
	public Optional<IstanzeSorter> build() {
		try {
			if (StringUtils.isEmpty(this.uriSortParameter))
				return Optional.empty();
			
			List<String> orderByFields = new ArrayList<>();
			
			String[] params = uriSortParameter.split(",");
			for (int i=0; i<params.length; i++) {
				String field = findFieldWithOrder(params[i]);
				if (field!=null) {
					orderByFields.add(field);
				}
			}
			
			if (orderByFields.isEmpty()) {
				return Optional.empty();
			}
			
			IstanzeSorter sorter = new IstanzeSorter(uriSortParameter, orderByFields);
			return Optional.of(sorter);
		} catch (Exception e) {
			return Optional.empty();
		}
	}


	/**
	 * 
	 * @param fieldWithOpzionalSign
	 * @return null if field not present
	 */
	private String findFieldWithOrder(String fieldWithOpzionalSign) {
		String order = "ASC";
		String field = fieldWithOpzionalSign;
		if("-".equals(fieldWithOpzionalSign.subSequence(0, 1))) {
			order = "DESC";
			field = fieldWithOpzionalSign.substring(1);
		} else {
			if("+".equals(fieldWithOpzionalSign.subSequence(0, 1))) {
				field = fieldWithOpzionalSign.substring(1);
			}
		}
		if (StringUtils.isEmpty(field))
			return null;
		return toLowerCaseUnderscore(field) + " " + order;
	}
	
	public String toLowerCaseUnderscore(String camelCaseStr) {
		String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return camelCaseStr.replaceAll(regex, replacement)
                           .toLowerCase();
	}

}
