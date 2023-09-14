/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dto;

import it.csi.moon.moonfobl.dto.moonfobl.ConsumerTypeEnum;

public class ImageConsumerParameterEntity extends ConsumerParameterEntity{
	
	private String id;
	private String altText;
	private String imageData;
	private String type = ConsumerTypeEnum.IMAGE.name();

	public ImageConsumerParameterEntity() {

	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the altText
	 */
	public String getAltText() {
		return altText;
	}

	/**
	 * @return the imageData
	 */
	public String getImageData() {
		return imageData;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param altText the altText to set
	 */
	public void setAltText(String altText) {
		this.altText = altText;
	}

	/**
	 * @param imageData the imageData to set
	 */
	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	
}
