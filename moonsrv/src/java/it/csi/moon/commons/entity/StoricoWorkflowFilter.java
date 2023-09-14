/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Optional;


/**
 * Filter DTO usato da StoricoWorkflowDAO per le ricerche
 * 
 * @author Alberto
 *
 */
public class StoricoWorkflowFilter {

	private Long idProcesso;
	private Long idIstanza;
	private Integer idStatoWfPartenza;
	private Integer idStatoWfArrivo;
	private String codiceAzione;
	
	public StoricoWorkflowFilter() {
		super();
	}
	
	public Optional<Long> getIdProcesso() {
		return Optional.ofNullable(idProcesso);
	}
	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}
	public Optional<Long> getIdIstanza() {
		return Optional.ofNullable(idIstanza);
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Optional<Integer> getIdStatoWfPartenza() {
		return Optional.ofNullable(idStatoWfPartenza);
	}
	public void setIdStatoWfPartenza(Integer idStatoWfPartenza) {
		this.idStatoWfPartenza = idStatoWfPartenza;
	}
	public Optional<Integer> getIdStatoWfArrivo() {
		return Optional.ofNullable(idStatoWfArrivo);
	}
	public void setIdStatoWfArrivo(Integer idStatoWfArrivo) {
		this.idStatoWfArrivo = idStatoWfArrivo;
	}
	public Optional<String> getCodiceAzione() {
		return Optional.ofNullable(codiceAzione);
	}
	public void setCodiceAzione(String codiceAzione) {
		this.codiceAzione = codiceAzione;
	}

	public static Builder builder(){
        return new Builder();
    }
    
	//
	// INNER BUILDER
	public static class Builder {

		private Long _idProcesso;
		private Long _idIstanza;
		private Integer _idStatoWfPartenza;
		private Integer _idStatoWfArrivo;
		private String _codiceAzione;
		
		public Builder idProcesso(Long idProcesso) {
			this._idProcesso = idProcesso;
			return this;
		}
		public Builder idIstanza(Long idIstanza) {
			this._idIstanza = idIstanza;
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
		public Builder codiceAzione(String codiceAzione) {
			this._codiceAzione = codiceAzione;
			return this;
		}
		
		public StoricoWorkflowFilter build() {
			return new StoricoWorkflowFilter(this);
		}
	}
	
	private StoricoWorkflowFilter(Builder builder) {
		setIdProcesso(builder._idProcesso);
		setIdIstanza(builder._idIstanza);
		setIdStatoWfPartenza(builder._idStatoWfPartenza);
		setIdStatoWfArrivo(builder._idStatoWfArrivo);
		setCodiceAzione(builder._codiceAzione);
	}

	
	@Override
	public String toString() {
		return "WorkflowFilter [idProcesso=" + idProcesso + ", idIstanza=" + idIstanza + ", idStatoWfPartenza="
				+ idStatoWfPartenza + ", idStatoWfArrivo=" + idStatoWfArrivo + ", codiceAzione=" + codiceAzione + "]";
	}

}
