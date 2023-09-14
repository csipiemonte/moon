/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Optional;

import it.csi.moon.moonbobl.business.service.impl.dao.RichiestaSupportoDAO;

/**
 * Filter DTO usato dal RichiestaSupportoDAO per le ricerche delle Richieste (Chat) di supporto
 * 
 * @see RichiestaSupportoDAO
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
public class RichiestaSupportoFilter {
	
	private Long idRichiestaSupporto;
	private Long idIstanza;
	private Long idModulo;
	private String flagInAttesaDiRisposta;
	private String descMittente;
	private String emailMittente;
	private String attoreIns;
	
	public RichiestaSupportoFilter() {
		super();
	}

	public Optional<Long> getIdRichiestaSupporto() {
		return Optional.ofNullable(idRichiestaSupporto);
	}
	public void setIdRichiestaSupporto(Long idRichiestaSupporto) {
		this.idRichiestaSupporto = idRichiestaSupporto;
	}
	public Optional<Long> getIdIstanza() {
		return Optional.ofNullable(idIstanza);
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Optional<Long> getIdModulo() {
		return Optional.ofNullable(idModulo);
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Optional<String> getFlagInAttesaDiRisposta() {
		return Optional.ofNullable(flagInAttesaDiRisposta);
	}
	public void setFlagInAttesaDiRisposta(String flagInAttesaDiRisposta) {
		this.flagInAttesaDiRisposta = flagInAttesaDiRisposta;
	}
	public Optional<String> getDescMittente() {
		return Optional.ofNullable(descMittente);
	}
	public void setDescMittente(String descMittente) {
		this.descMittente = descMittente;
	}
	public Optional<String> getEmailMittente() {
		return Optional.ofNullable(emailMittente);
	}
	public void setEmailMittente(String emailMittente) {
		this.emailMittente = emailMittente;
	}
	public Optional<String> getAttoreIns() {
		return Optional.ofNullable(attoreIns);
	}
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
	}

	@Override
	public String toString() {
		return "RichiestaSupportoFilter [idRichiestaSupporto=" + idRichiestaSupporto + ", idIstanza=" + idIstanza
				+ ", idModulo=" + idModulo + ", flagInAttesaDiRisposta=" + flagInAttesaDiRisposta + ", descMittente="
				+ descMittente + ", emailMittente=" + emailMittente + ", attoreIns=" + attoreIns + "]";
	}

}