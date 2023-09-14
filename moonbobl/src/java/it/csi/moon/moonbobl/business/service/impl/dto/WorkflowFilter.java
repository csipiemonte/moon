/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Optional;


/**
 * Filter DTO usato dal WorkflowDAO per le ricerche del Workflow
 * escludiAzioniDiSistema DEFAULT true
 * 
 * @author Alberto
 *
 */
public class WorkflowFilter {

	private Long idProcesso;
	private Long idModulo;
	private Integer idStatoWfPartenza;
	private Integer idStatoWfArrivo;
	private Long idAzione;
	private Long idWorkflow;
	private String flagApi;
	private boolean escludiAzioniUtenteCompilante;
	private boolean escludiAzioniDiSistema;
	private Boolean isAutomatica;

	private Integer idTipoUtenteDestinatario;
	
	public WorkflowFilter() {
		super();
	}
	
	public Optional<Long> getIdProcesso() {
		return Optional.ofNullable(idProcesso);
	}
	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}
	public Optional<Long> getIdModulo() {
		return Optional.ofNullable(idModulo);
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Optional<Integer> getIdStatoWfPartenza() {
		return Optional.ofNullable(idStatoWfPartenza);
	}
	public void setIdStatoWfPartenza(Integer idStatoWfPartenza) {
		this.idStatoWfPartenza = idStatoWfPartenza;
	}
	public Long getIdWorkflow() {
		return idWorkflow;
	}
	public void setIdWorkflow(Long idWorkflow) {
		this.idWorkflow = idWorkflow;
	}
	public Optional<Integer> getIdStatoWfArrivo() {
		return Optional.ofNullable(idStatoWfArrivo);
	}
	public void setIdStatoWfArrivo(Integer idStatoWfArrivo) {
		this.idStatoWfArrivo = idStatoWfArrivo;
	}
	public Optional<Long> getIdAzione() {
		return Optional.ofNullable(idAzione);
	}
	public void setIdAzione(Long idAzione) {
		this.idAzione = idAzione;
	}
	public Optional<String> getFlagApi() {
		return Optional.ofNullable(flagApi);
	}
	public void setFlagApi(String flagApi) {
		this.flagApi = flagApi;
	}
	public boolean isEscludiAzioniUtenteCompilante() {
		return escludiAzioniUtenteCompilante;
	}
	public void setEscludiAzioniUtenteCompilante(boolean escludiAzioniUtenteCompilante) {
		this.escludiAzioniUtenteCompilante = escludiAzioniUtenteCompilante;
	}
	public Optional<Integer> getIdTipoUtenteDestinatario() {
		return Optional.ofNullable(idTipoUtenteDestinatario);
	}
	public void setIdTipoUtenteDestinatario(Integer idTipoUtenteDestinatario) {
		this.idTipoUtenteDestinatario = idTipoUtenteDestinatario;
	}
    public boolean isEscludiAzioniDiSistema() {
		return escludiAzioniDiSistema;
	}
	public void setEscludiAzioniDiSistema(boolean escludiAzioniDiSistema) {
		this.escludiAzioniDiSistema = escludiAzioniDiSistema;
	}
	public void setIsAutomatica(Boolean isAutomatica) {
		this.isAutomatica = isAutomatica;
	}
	public Optional<Boolean> isAutomatica() {
		return Optional.ofNullable(isAutomatica);
	}
	
	public static Builder builder(){
        return new Builder();
    }
    
	//
	// INNER BUILDER
	public static class Builder {

		private Long _idProcesso;
		private Long _idModulo;
		private Integer _idStatoWfPartenza;
		private Integer _idStatoWfArrivo;
		private Long _idAzione;
		private Long _idWorkflow;
		private String _flagApi;
		private boolean _escludiAzioniUtenteCompilante;
		private boolean _escludiAzioniDiSistema = true;
		private Integer _idTipoUtenteDestinatario;
		private Boolean _isAutomatica;
		
		public Builder idProcesso(Long idProcesso) {
			this._idProcesso = idProcesso;
			return this;
		}
		public Builder idModulo(Long idModulo) {
			this._idModulo = idModulo;
			return this;
		}
		public Builder idStatoWfPartenza(Integer idStatoWfPartenza) {
			this._idStatoWfPartenza = idStatoWfPartenza;
			return this;
		}
		public Builder idStatoWfArrivo(Integer idStatoWfArrivo) {
			this._idStatoWfArrivo = idStatoWfArrivo;
			return this;
		}
		public Builder idAzione(Long idAzione) {
			this._idAzione = idAzione;
			return this;
		}
		public Builder idWorkflow(Long idWorkflow) {
			this._idWorkflow = idWorkflow;
			return this;
		}
		public Builder flagApi(String flagApi) {
			this._flagApi = flagApi;
			return this;
		}
		public Builder escludiAzioniUtenteCompilante(boolean escludiAzioniUtenteCompilante) {
			this._escludiAzioniUtenteCompilante = escludiAzioniUtenteCompilante;
			return this;
		}
		public Builder escludiAzioniDiSistema(boolean escludiAzioniDiSistema) {
			this._escludiAzioniDiSistema = escludiAzioniDiSistema;
			return this;
		}
		public Builder idTipoUtenteDestinatario(Integer idTipoUtenteDestinatario) {
			this._idTipoUtenteDestinatario = idTipoUtenteDestinatario;
			return this;
		}
		public Builder isAutomatica(Boolean isAutomatica) {
			this._isAutomatica = isAutomatica;
			return this;
		}
		
		public WorkflowFilter build() {
			return new WorkflowFilter(this);
		}
	}
	
	private WorkflowFilter(Builder builder) {
		setIdProcesso(builder._idProcesso);
		setIdModulo(builder._idModulo);
		setIdStatoWfPartenza(builder._idStatoWfPartenza);
		setIdStatoWfArrivo(builder._idStatoWfArrivo);
		setIdAzione(builder._idAzione);
		setIdWorkflow(builder._idWorkflow);
		setFlagApi(builder._flagApi);
		setEscludiAzioniUtenteCompilante(builder._escludiAzioniUtenteCompilante);
		setEscludiAzioniDiSistema(builder._escludiAzioniDiSistema);
		setIdTipoUtenteDestinatario(builder._idTipoUtenteDestinatario);
		setIsAutomatica(builder._isAutomatica);
	}

	
	@Override
	public String toString() {
		return "WorkflowFilter [idProcesso=" + idProcesso + ", idModulo=" + idModulo + ", idStatoWfPartenza="
				+ idStatoWfPartenza + ", idStatoWfArrivo=" + idStatoWfArrivo + ", idAzione=" + idAzione
				+ ", idWorkflow=" + idWorkflow + ", flagApi=" + flagApi + ", escludiAzioniUtenteCompilante="
				+ escludiAzioniUtenteCompilante + ", escludiAzioniDiSistema="
				+ escludiAzioniDiSistema + ", isAutomatica="
				+ isAutomatica + "]";
	}

}
