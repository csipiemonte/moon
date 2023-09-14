/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

import it.csi.moon.commons.util.decodifica.DecodificaTipoRepositoryFile;

/**
 * Entity della tabella di decodifica delle tipologie di repository_file
 * <br>
 * <br>Tabella moon_fo_d_tipo_repository_file
 * <br>PK: idTipoRepositoryFile
 * <br>Usato prevalentamente da enum DecodificaTipoRepositoryFile
 * 
 * @see DecodificaTipoRepositoryFile
 * @see RepositoryFileEntity#getIdTipologia()
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class TipoRepositoryFileEntity {

	public enum TipoInOut {IN(1), OUT(2);
		private Integer id;
		TipoInOut(Integer id) {this.id=id;}
		public Integer getId() { return id; }
	}
	
	private Integer idTipoRepositoryFile = null;
	private Integer tipoIngUsc;
	private String codiceTipoRepositoryFile = null;
	private String nomeTipoRepositoryFile = null;
	private String descrizioneTipoRepositoryFile = null;
	private String flAttivo = null;
	private Date dataUpd = null;
	private String attoreUpd = null;
	
	public Integer getIdTipoRepositoryFile() {
		return idTipoRepositoryFile;
	}
	public void setIdTipoRepositoryFile(Integer idTipoRepositoryFile) {
		this.idTipoRepositoryFile = idTipoRepositoryFile;
	}
	public Integer getTipoIngUsc() {
		return tipoIngUsc;
	}
	public void setTipoIngUsc(Integer tipoIngUsc) {
		this.tipoIngUsc = tipoIngUsc;
	}
	public String getCodiceTipoRepositoryFile() {
		return codiceTipoRepositoryFile;
	}
	public void setCodiceTipoRepositoryFile(String codiceTipoRepositoryFile) {
		this.codiceTipoRepositoryFile = codiceTipoRepositoryFile;
	}
	public String getNomeTipoRepositoryFile() {
		return nomeTipoRepositoryFile;
	}
	public void setNomeTipoRepositoryFile(String nomeTipoRepositoryFile) {
		this.nomeTipoRepositoryFile = nomeTipoRepositoryFile;
	}
	public String getDescrizioneTipoRepositoryFile() {
		return descrizioneTipoRepositoryFile;
	}
	public void setDescrizioneTipoRepositoryFile(String descrizioneTipoRepositoryFile) {
		this.descrizioneTipoRepositoryFile = descrizioneTipoRepositoryFile;
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
	
	@Override
	public String toString() {
		return "TipoRepositoryFileEntity [idTipoRepositoryFile=" + idTipoRepositoryFile + ", tipoIngUsc=" + tipoIngUsc
				+ ", codiceTipoRepositoryFile=" + codiceTipoRepositoryFile + ", nomeTipoRepositoryFile="
				+ nomeTipoRepositoryFile + ", descrizioneTipoRepositoryFile=" + descrizioneTipoRepositoryFile
				+ ", flAttivo=" + flAttivo + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}

}
