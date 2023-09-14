/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.List;
import java.util.Optional;

import it.csi.moon.commons.util.decodifica.DecodificaTipoRepositoryFile;


public class RepositoryFileFilter {

	public enum EnumFilterFlagEliminato {
		TUTTI,
		ELIMINATI,
		NONELIMINATI
	};
	
	private Long idFile;
	private String nomeFile;
	private EnumFilterFlagEliminato flagEliminato = EnumFilterFlagEliminato.NONELIMINATI;
	private Long idIstanza;
	private List<DecodificaTipoRepositoryFile> tipiFile;
	private Long idStoricoWorkflow;
	
	public Optional<Long> getIdFile() {
		return Optional.ofNullable(idFile);
	}
	public void setIdFile(Long idFile) {
		this.idFile = idFile;
	}
	public Optional<String> getNomeFile() {
		return Optional.ofNullable(nomeFile);
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public EnumFilterFlagEliminato getFlagEliminato() {
		return flagEliminato;
	}
	public void setFlagEliminata(EnumFilterFlagEliminato statoEliminato) {
		this.flagEliminato = statoEliminato;
	}
	public Optional<Long> getIdIstanza() {
		return Optional.ofNullable(idIstanza);
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Optional<List<DecodificaTipoRepositoryFile>> getTipiFile() {
		return Optional.ofNullable(tipiFile);
	}
	public void setTipiFile(List<DecodificaTipoRepositoryFile> list) {
		this.tipiFile = list;
	}
	public Optional<Long> getIdStoricoWorkflow() {
		return Optional.ofNullable(idStoricoWorkflow);
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
}
