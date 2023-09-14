/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dto;

public class ConsumerParameterEntity {
	
	private String idParameter;
	private String type;
	private String description;

	public ConsumerParameterEntity() {

	}

	
	

	/**
	 * @return the idParameter
	 */
	public String getIdParameter() {
		return idParameter;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	

	/**
	 * @param idParameter the idParameter to set
	 */
	public void setIdParameter(String idParameter) {
		this.idParameter = idParameter;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
