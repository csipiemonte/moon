/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;
import java.util.Date;


/**
 * 
 * @author Danilo Mosca
 *
 */
public class FruitoreDatiAzioneEntity {
	
	private Integer idFruitoreDatiAzione;
	private String codice;
	private String descrizione;
	private String identificativo;
	private Date data;
	private String numeroProtocollo = null;
	private Date dataProtocollo = null;
	private Long idIstanza;
	private Long idStoricoWorkflow;
	private String datiAzione;
	private String postAzioni;
	private String allegatiAzione;
	
	public Integer getIdFruitoreDatiAzione() {
		return idFruitoreDatiAzione;
	}
	public void setIdFruitoreDatiAzione(Integer idFruitoreDatiAzione) {
		this.idFruitoreDatiAzione = idFruitoreDatiAzione;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getIdentificativo() {
		return identificativo;
	}
	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}
	public Date getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long id_istanza) {
		this.idIstanza = id_istanza;
	}
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long id_storico_workflow) {
		this.idStoricoWorkflow = id_storico_workflow;
	}
	public String getDatiAzione() {
		return datiAzione;
	}
	public void setDatiAzione(String datiAzione) {
		this.datiAzione = datiAzione;
	}	
	public String getPostAzioni() {
		return postAzioni;
	}
	public void setPostAzioni(String postAzioni) {
		this.postAzioni = postAzioni;
	}	
	public String getAllegatiAzione() {
		return allegatiAzione;
	}
	public void setAllegatiAzione(String allegatiAzione) {
		this.allegatiAzione = allegatiAzione;
	}

	@Override
	public String toString() {
		return "FruitoreDatiAzioneEntity [idFruitoreDatiAzione=" + idFruitoreDatiAzione + ", codice=" + codice
				+ ", descrizione=" + descrizione + ", identificativo=" + identificativo + ", data=" + data
				+ ", numeroProtocollo=" + numeroProtocollo + ", dataProtocollo=" + dataProtocollo + ", idIstanza="
				+ idIstanza + ", idStoricoWorkflow=" + idStoricoWorkflow + ", datiAzione=" + datiAzione +  ", postAzioni=" + postAzioni +  ", allegatiAzione=" + allegatiAzione+ "]";
	}

}
