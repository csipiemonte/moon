/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Objects;

/**
 * Entity della gestione del progressivo di un modulo utilizzato nel salvattaggio delle istanze
 * Viene usato per il calcolo del 'codiceIstanza' durante primo salavataggio di un istanza
 * Deve essere presente un solo record per modulo 
 * 
 * Tabella moon_io_d_moduloprogressivo
 * PK: idModulo
 * 
 * @see ModuloEntity
 * @see IstanzaEntity#setCodiceIstanza()
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloProgressivoEntity {
	
	private Long idModulo;
	private Integer idTipoCodiceIstanza;
	private Long progressivo;
	private Integer annoRiferimento;
	private Integer lunghezza;
	
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Integer getIdTipoCodiceIstanza() {
		return idTipoCodiceIstanza;
	}
	public void setIdTipoCodiceIstanza(Integer idTipoCodiceIstanza) {
		this.idTipoCodiceIstanza = idTipoCodiceIstanza;
	}
	public Long getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(Long progressivo) {
		this.progressivo = progressivo;
	}
	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}
	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}
	public Integer getLunghezza() {
		return lunghezza;
	}
	public void setLunghezza(Integer lunghezza) {
		this.lunghezza = lunghezza;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(idModulo, idTipoCodiceIstanza, progressivo, annoRiferimento, lunghezza);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModuloProgressivoEntity prog = (ModuloProgressivoEntity) o;
		return Objects.equals(idModulo, prog.idModulo) &&
			Objects.equals(idTipoCodiceIstanza, prog.idTipoCodiceIstanza) &&
			Objects.equals(progressivo, prog.progressivo) &&
			Objects.equals(annoRiferimento, prog.annoRiferimento) &&
			Objects.equals(lunghezza, prog.lunghezza);
	}

	
	@Override
	public String toString() {
		return "ModuloProgressivoEntity [idModulo=" + idModulo + ", idTipoCodiceIstanza=" + idTipoCodiceIstanza + 
				", progressivo=" + progressivo + 
				", annoRiferimento="+ annoRiferimento + 
				", lunghezza=" + lunghezza + "]";
	}
	
}
