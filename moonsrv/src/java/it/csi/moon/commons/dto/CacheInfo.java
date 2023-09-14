/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class CacheInfo {

	private String codice;
	private String nome;
	private int count;
	private Date lastReset;
	
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getLastReset() {
		return lastReset;
	}
	public void setLastReset(Date lastReset) {
		this.lastReset = lastReset;
	}

	@Override
	public String toString() {
		return "CacheInfo [codice=" + codice + ", nome=" + nome + ", count=" + count + ", lastReset=" + lastReset + "]";
	}
	
	
	
}
