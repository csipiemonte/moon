/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella instanza con riferimento allo stato di workflow per Api
 * <br> 
 * <br>
 * <br>Tabella moon_fo_t_istanza
 * <br>PK: idIstanza
 * 
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class IstanzaApiEntity {
	

	private String codiceIstanza; // MAXLENGTH(50)
	private String codiceFiscaleDichiarante = null;
	private String cognomeDichiarante = null;
	private String nomeDichiarante = null;
	private Date dataCreazione;
	private Integer idStatoWfArrivo;
	private String nomeStatoWfArrivo;
	
	public IstanzaApiEntity(String codiceIstanza, String codiceFiscaleDichiarante, String cognomeDichiarante,
			String nomeDichiarante, Date dataCreazione, Integer idStatoWfArrivo, String nomeStatoWfArrivo) {
		super();
		this.codiceIstanza = codiceIstanza;
		this.codiceFiscaleDichiarante = codiceFiscaleDichiarante;
		this.cognomeDichiarante = cognomeDichiarante;
		this.nomeDichiarante = nomeDichiarante;
		this.dataCreazione = dataCreazione;
		this.idStatoWfArrivo = idStatoWfArrivo;
		this.nomeStatoWfArrivo = nomeStatoWfArrivo;
	}
	public IstanzaApiEntity() {
		super();
	}
	
	
	
	/**
	 * @return the codiceIstanza
	 */
	public String getCodiceIstanza() {
		return codiceIstanza;
	}
	/**
	 * @return the codiceFiscaleDichiarante
	 */
	public String getCodiceFiscaleDichiarante() {
		return codiceFiscaleDichiarante;
	}
	/**
	 * @return the cognomeDichiarante
	 */
	public String getCognomeDichiarante() {
		return cognomeDichiarante;
	}
	/**
	 * @return the nomeDichiarante
	 */
	public String getNomeDichiarante() {
		return nomeDichiarante;
	}
	/**
	 * @return the dataCreazione
	 */
	public Date getDataCreazione() {
		return dataCreazione;
	}
	/**
	 * @return the idStatoWfArrivo
	 */
	public Integer getIdStatoWfArrivo() {
		return idStatoWfArrivo;
	}
	/**
	 * @return the nomeStatoWfArrivo
	 */
	public String getNomeStatoWfArrivo() {
		return nomeStatoWfArrivo;
	}
	/**
	 * @param codiceIstanza the codiceIstanza to set
	 */
	public void setCodiceIstanza(String codiceIstanza) {
		this.codiceIstanza = codiceIstanza;
	}
	/**
	 * @param codiceFiscaleDichiarante the codiceFiscaleDichiarante to set
	 */
	public void setCodiceFiscaleDichiarante(String codiceFiscaleDichiarante) {
		this.codiceFiscaleDichiarante = codiceFiscaleDichiarante;
	}
	/**
	 * @param cognomeDichiarante the cognomeDichiarante to set
	 */
	public void setCognomeDichiarante(String cognomeDichiarante) {
		this.cognomeDichiarante = cognomeDichiarante;
	}
	/**
	 * @param nomeDichiarante the nomeDichiarante to set
	 */
	public void setNomeDichiarante(String nomeDichiarante) {
		this.nomeDichiarante = nomeDichiarante;
	}
	/**
	 * @param dataCreazione the dataCreazione to set
	 */
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	/**
	 * @param idStatoWfArrivo the idStatoWfArrivo to set
	 */
	public void setIdStatoWfArrivo(Integer idStatoWfArrivo) {
		this.idStatoWfArrivo = idStatoWfArrivo;
	}
	/**
	 * @param nomeStatoWfArrivo the nomeStatoWfArrivo to set
	 */
	public void setNomeStatoWfArrivo(String nomeStatoWfArrivo) {
		this.nomeStatoWfArrivo = nomeStatoWfArrivo;
	}
	
	@Override
	public String toString() {
		return "IstanzaApiEntity [codiceIstanza=" + codiceIstanza + ", codiceFiscaleDichiarante="
				+ codiceFiscaleDichiarante + ", cognomeDichiarante=" + cognomeDichiarante + ", nomeDichiarante="
				+ nomeDichiarante + ", dataCreazione=" + dataCreazione + ", idStatoWfArrivo=" + idStatoWfArrivo
				+ ", nomeStatoWfArrivo=" + nomeStatoWfArrivo + "]";
	}

}
