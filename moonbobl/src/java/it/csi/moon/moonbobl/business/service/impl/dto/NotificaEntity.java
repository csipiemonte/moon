/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;
/**
 * Entity della tabella delle notifiche dalla PA verso il Dichiarante
 * <br>
 * <br>Tabella moon_fo_t_notifica
 * <br>PK: idNotifica
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class NotificaEntity {

	private Long idNotifica;
	private Long idIstanza;
	private String cfDestinatario;
	private String emailDestinatario;
	private String oggettoNotifica;
	private String testoNotifica;
	private Date dataInvio;
	private String esitoInvio;
	private String flagLetta;
	private String flagArchiviata;
	
	public NotificaEntity() {	
	}
	public NotificaEntity(Long idNotifica, Long idIstanza, String cfDestinatario, String emailDestinatario,
			String oggettoNotifica, String testoNotifica, Date dataInvio, String esitoInvio, String flagLetta,
			String flagArchiviata) {
		super();
		this.idNotifica = idNotifica;
		this.idIstanza = idIstanza;
		this.cfDestinatario = cfDestinatario;
		this.emailDestinatario = emailDestinatario;
		this.oggettoNotifica = oggettoNotifica;
		this.testoNotifica = testoNotifica;
		this.dataInvio = dataInvio;
		this.esitoInvio = esitoInvio;
		this.flagLetta = flagLetta;
		this.flagArchiviata = flagArchiviata;
	}
	
	public Long getIdNotifica() {
		return idNotifica;
	}
	public void setIdNotifica(Long idNotifica) {
		this.idNotifica = idNotifica;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public String getCfDestinatario() {
		return cfDestinatario;
	}
	public void setCfDestinatario(String cfDestinatario) {
		this.cfDestinatario = cfDestinatario;
	}
	public String getEmailDestinatario() {
		return emailDestinatario;
	}
	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}
	public String getOggettoNotifica() {
		return oggettoNotifica;
	}
	public void setOggettoNotifica(String oggettoNotifica) {
		this.oggettoNotifica = oggettoNotifica;
	}
	public String getTestoNotifica() {
		return testoNotifica;
	}
	public void setTestoNotifica(String testoNotifica) {
		this.testoNotifica = testoNotifica;
	}
	public Date getDataInvio() {
		return dataInvio;
	}
	public void setDataInvio(Date dataInvio) {
		this.dataInvio = dataInvio;
	}
	public String getEsitoInvio() {
		return esitoInvio;
	}
	public void setEsitoInvio(String esitoInvio) {
		this.esitoInvio = esitoInvio;
	}
	public String getFlagLetta() {
		return flagLetta;
	}
	public void setFlagLetta(String flagLetta) {
		this.flagLetta = flagLetta;
	}
	public String getFlagArchiviata() {
		return flagArchiviata;
	}
	public void setFlagArchiviata(String flagArchiviata) {
		this.flagArchiviata = flagArchiviata;
	}
	
	@Override
	public String toString() {
		return "NotificaEntity [idNotifica=" + idNotifica + ", idIstanza=" + idIstanza + ", cfDestinatario="
				+ cfDestinatario + ", emailDestinatario=" + emailDestinatario + ", oggettoNotifica=" + oggettoNotifica
				+ ", testoNotifica=" + testoNotifica + ", dataInvio=" + dataInvio + ", esitoInvio=" + esitoInvio
				+ ", flagLetta=" + flagLetta + ", flagArchiviata=" + flagArchiviata + "]";
	}

}
