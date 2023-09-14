/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.util.LoggerAccessor;

/**
 * Filter DTO usato per ordinare il risulato delle ricerche delle istanze
 * Example: /istanze?sort=-modified,+attoreIns
 * Il nome nella URI deve corrispondere al quello del DB non del JSON
 * Viene qui trasfmormato da camelCase a lowerCaseUnderscore
 * 
 *  - idIstanza
 *  - codiceIstanza
 *  - dataCreazione
 *  - cron.data_inizio
 *  - modified
 *  - attoreIns
 *  - attoreUpd
 *  
 * @see Istanza
 * @see IstanzaDAO
 * 
 * @author Laurent
 *
 */
public class IstanzeSorterBuilder {
	private static final String CLASS_NAME= "IstanzeSorterBuilder";
	private static final Logger log = LoggerAccessor.getLoggerBusiness();
	
	// required parameters
	private String uriSortParameter;
	
	public IstanzeSorterBuilder(String uriSortParameter) {
		this.uriSortParameter = uriSortParameter;
	}
	
	public Optional<IstanzeSorter> build() {
		try {
			if (StringUtils.isEmpty(this.uriSortParameter))
				return Optional.empty();
			
			List<String> orderByFields = new ArrayList<String>();
			
			String[] params = uriSortParameter.split(",");
			for (int i=0; i<params.length; i++) {
				String field = findFieldWithOrder(params[i]);
				if (field!=null) {
					orderByFields.add(field);
				}
			}
			
			if (orderByFields.isEmpty()) {
				log.error("[" + CLASS_NAME + "::build] orderByFields empty.");
				return Optional.empty();
			}
			
			IstanzeSorter sorter = new IstanzeSorter(uriSortParameter, orderByFields);
			return Optional.of(sorter);
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::build] errore generico build");
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
