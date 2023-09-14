/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Arrays;

/**
 * Allegato di un Messaggio di supporto (moon_fo_t_allegato_messaggio_supporto)
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
public class AllegatoMessaggioSupporto {

	private Long idAllegatoMessaggioSupporto = null;
	private String nome = null;
	private Long dimensione = null;
	private byte[] contenuto = null;
	private String contentType = null;
	
	public AllegatoMessaggioSupporto() {
	}

	public AllegatoMessaggioSupporto(Long idAllegatoMessaggioSupporto, String nome,
			Long dimensione, byte[] contenuto, String contentType) {
		super();
		this.idAllegatoMessaggioSupporto = idAllegatoMessaggioSupporto;
		this.nome = nome;
		this.dimensione = dimensione;
		this.contenuto = contenuto;
		this.contentType = contentType;
	}

	public Long getIdAllegatoMessaggioSupporto() {
		return idAllegatoMessaggioSupporto;
	}
	public void setIdAllegatoMessaggioSupporto(Long idAllegatoMessaggioSupporto) {
		this.idAllegatoMessaggioSupporto = idAllegatoMessaggioSupporto;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getDimensione() {
		return dimensione;
	}
	public void setDimensione(Long dimensione) {
		this.dimensione = dimensione;
	}
	public byte[] getContenuto() {
		return contenuto;
	}
	public void setContenuto(byte[] contenuto) {
		this.contenuto = contenuto;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "AllegatoMessaggioSupporto [idAllegatoMessaggioSupporto=" + idAllegatoMessaggioSupporto + ", nome="
				+ nome + ", dimensione=" + dimensione 
				+ ", contenuto=" + ((contenuto==null)?"null":(Arrays.toString(contenuto).substring(0, 10)+"... len="+contenuto.length))
				+ ", contentType=" + contentType + "]";
	}

	public String toStringFULL() {
		return "AllegatoMessaggioSupporto [idAllegatoMessaggioSupporto=" + idAllegatoMessaggioSupporto + ", nome="
				+ nome + ", dimensione=" + dimensione 
				+ ", contenuto=" + Arrays.toString(contenuto) 
				+ ", contentType=" + contentType + "]";
	}
}
