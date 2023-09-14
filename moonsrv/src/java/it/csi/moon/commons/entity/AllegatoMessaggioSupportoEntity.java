/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

/**
 * Entity della tabella del file allegato ad un messaggio di supporto
 * <br>
 * <br>Tabella moon_fo_t_allegato_messaggio_supporto
 * <br>PK: idAllegatoMessaggioSupporto
 * <br>Usato per salvare gli file allegati
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
public class AllegatoMessaggioSupportoEntity {

	private Long idAllegatoMessaggioSupporto = null;
	private Long idMessaggioSupporto = null;
	private String nome = null;
	private Long dimensione = null;
	private byte[] contenuto = null;
	private String contentType = null;
	
	public AllegatoMessaggioSupportoEntity() {
	}

	public AllegatoMessaggioSupportoEntity(Long idAllegatoMessaggioSupporto, Long idMessaggioSupporto, String nome,
			Long dimensione, byte[] contenuto, String contentType) {
		super();
		this.idAllegatoMessaggioSupporto = idAllegatoMessaggioSupporto;
		this.idMessaggioSupporto = idMessaggioSupporto;
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
	public Long getIdMessaggioSupporto() {
		return idMessaggioSupporto;
	}
	public void setIdMessaggioSupporto(Long idMessaggioSupporto) {
		this.idMessaggioSupporto = idMessaggioSupporto;
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

}
