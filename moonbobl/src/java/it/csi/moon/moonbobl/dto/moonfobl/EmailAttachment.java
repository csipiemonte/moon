/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allegati == null) ? 0 : allegati.hashCode());
		result = prime * result + ((idIstanza == null) ? 0 : idIstanza.hashCode());
		result = prime * result + ((idFile == null) ? 0 : idFile.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailAttachment other = (EmailAttachment) obj;
		if (allegati == null) {
			if (other.allegati != null)
				return false;
		} else if (!allegati.equals(other.allegati))
			return false;
		if (idFile == null) {
			if (other.idFile != null)
				return false;
		} else if (!idFile.equals(other.idFile))
			return false;
		if (idIstanza == null) {
			if (other.idIstanza != null)
				return false;
		} else if (!idIstanza.equals(other.idIstanza))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "EmailAttachment [idIstanza=" + idIstanza + ", allegati=" + allegati + ", idFile="+ idFile+"]";
	}

}

