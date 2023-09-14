/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

/**
 * EmailAttachment
 * Contiene il riferimento al l'attachment da allegare ad una richiesta di email
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class EmailAttachment   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
	private Long idIstanza = null;
	private Boolean allegati = null;
	private Long idFile = null;
	private Long idNotifica = null;
	private Long idStoricoWorkflow = null;
	private Boolean istanza = null;
	private Boolean allegatiAzione = null;
	
	public EmailAttachment() {
		super();
	}

	public EmailAttachment(Long idIstanza) {
		super();
		this.idIstanza = idIstanza;
	}
	public EmailAttachment(Long idIstanza, Boolean allegati) {
		super();
		this.idIstanza = idIstanza;
		this.allegati = allegati;
	}
	public EmailAttachment(Long idIstanza, Long idStoricoWorkflow, Boolean allegati) {
		super();
		this.idIstanza = idIstanza;
		this.allegati = allegati;
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	
	public EmailAttachment(Long idIstanza, Long idStoricoWorkflow, Boolean istanza, Boolean allegati, Boolean allegatiAzione) {
		super();
		this.idIstanza = idIstanza;
		this.idStoricoWorkflow = idStoricoWorkflow;
		this.istanza = istanza;
		this.allegati = allegati;
		this.allegatiAzione = allegatiAzione;
	}
	
	
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Boolean getAllegati() {
		return allegati;
	}
	public void setAllegati(Boolean allegati) {
		this.allegati = allegati;
	}
	public Long getIdFile() {
		return idFile;
	}
	public void setIdFile(Long idFile) {
		this.idFile = idFile;
	}
	public Long getIdNotifica() {
		return idNotifica;
	}
	public void setIdNotifica(Long idNotifica) {
		this.idNotifica = idNotifica;
	}
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	public Boolean getIstanza() {
		return istanza;
	}

	public void setIstanza(Boolean istanza) {
		this.istanza = istanza;
	}

	public Boolean getAllegatiAzione() {
		return allegatiAzione;
	}

	public void setAllegatiAzione(Boolean allegatiAzione) {
		this.allegatiAzione = allegatiAzione;
	}

	@Override
	public String toString() {
		return "EmailAttachment [idIstanza=" + idIstanza + ", allegati=" + allegati + ", istanza=" + istanza + ", allegatiAzione=" + allegatiAzione+", idFile="+ idFile+ ", idNotifica="+ idNotifica+ ", idStoricoWorkflow="+ idStoricoWorkflow+"]";
	}

}

