/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

public class FruitoreEntity {
		
	private Integer idFruitore = null;
	private String descFruitore = null;
	

	public FruitoreEntity() {
		
	}
	
	public FruitoreEntity(Integer idFruitore, String descFruitore) {
		this.idFruitore = idFruitore;
		this.descFruitore = descFruitore;
	}
		
	public Integer getIdFruitore() {
		return idFruitore;
	}

	public void setIdFruitore(Integer idFruitore) {
		this.idFruitore = idFruitore;
	}
	
	public String getDescFruitore() {
		return descFruitore;
	}

	public void setDescFruitore(String descFruitore) {
		this.descFruitore = descFruitore;
	}



}
