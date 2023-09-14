/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

/**
 * Entity della tabella moon_bo_t_formio_custom_components 
 * <br>
 * <br>Tabella:  <code>moon_bo_t_formio_custom_components</code>
 * <br>PK:  <code>idComponent</code>
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class CustomComponentEntity {
	
	private String idComponent;
	private String jsonComponent;
	/**
	 * @return the idComponent
	 */
	public String getIdComponent() {
		return idComponent;
	}
	/**
	 * @return the jsonComponent
	 */
	public String getJsonComponent() {
		return jsonComponent;
	}
	/**
	 * @param idComponent the idComponent to set
	 */
	public void setIdComponent(String idComponent) {
		this.idComponent = idComponent;
	}
	/**
	 * @param jsonComponent the jsonComponent to set
	 */
	public void setJsonComponent(String jsonComponent) {
		this.jsonComponent = jsonComponent;
	}
	
}
