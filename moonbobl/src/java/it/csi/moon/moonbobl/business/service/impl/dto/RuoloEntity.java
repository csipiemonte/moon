/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * Entity della tabella ruolo, utilizzato per la gestione degli accessi 
 * delle componente che lo richiedono : moonbuilder, moonbo
 * <br>
 * <br>Tabella:  <code>moon_fo_d_ruolo</code>
 * <br>PK:  <code>idRuolo</code>
 * <br>AK:  <code>codiceRuolo</code>
 * <br>
 * <br>Esempi:
 * <br>1 - ADMIN - Amministratore del sistema
 * <br>2 - RESP	- Responsabile di un ente
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class RuoloEntity {
	
	private Integer idRuolo;
	private String codiceRuolo;
	private String nomeRuolo;
	private String descrizioneRuolo;
	private String flAttivo;
	private Date dataUpd;
	private String attoreUpd;
	
	public RuoloEntity() {
		super();
	}

	public RuoloEntity(Integer idRuolo, String codiceRuolo, String nomeRuolo, String descrizioneRuolo,
			String flAttivo, Date dataUpd, String attoreUpd) {
		super();
		this.idRuolo = idRuolo;
		this.codiceRuolo = codiceRuolo;
		this.nomeRuolo = nomeRuolo;
		this.descrizioneRuolo = descrizioneRuolo;
		this.flAttivo = flAttivo;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Integer getIdRuolo() {
		return idRuolo;
	}

	public void setIdRuolo(Integer idRuolo) {
		this.idRuolo = idRuolo;
	}

	public String getCodiceRuolo() {
		return codiceRuolo;
	}

	public void setCodiceRuolo(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}

	public String getNomeRuolo() {
		return nomeRuolo;
	}

	public void setNomeRuolo(String nomeRuolo) {
		this.nomeRuolo = nomeRuolo;
	}

	public String getDescrizioneRuolo() {
		return descrizioneRuolo;
	}

	public void setDescrizioneRuolo(String descrizioneRuolo) {
		this.descrizioneRuolo = descrizioneRuolo;
	}

	public String getFlAttivo() {
		return flAttivo;
	}

	public void setFlAttivo(String flAttivo) {
		this.flAttivo = flAttivo;
	}

	public Date getDataUpd() {
		return dataUpd;
	}

	public void setDataUpd(Date dataUpd) {
		this.dataUpd = dataUpd;
	}

	public String getAttoreUpd() {
		return attoreUpd;
	}

	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}
	
	
}