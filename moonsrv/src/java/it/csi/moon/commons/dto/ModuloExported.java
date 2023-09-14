/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.List;

import it.csi.moon.commons.entity.CategoriaEntity;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.ModuloProgressivoEntity;
import it.csi.moon.commons.entity.ModuloStrutturaEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.PortaleEntity;
import it.csi.moon.commons.entity.ProtocolloParametroEntity;

/**
 * Modulo Export / Import
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloExported {
	
	private ModuloExportInfo exportInformation;
	// Modulo (moon_io_d_modulo + versione)
	private ModuloVersionatoEntity modulo;
	// Cronologia (moon_io_r_cronologia_statomodulo) (serve solo lo stato)
	private StatoModulo stato;
	// Progressivo (moon_io_d_moduloprogressivo)
	private ModuloProgressivoEntity progressivo;
	// Struttura (moon_io_d_moduloprogressivo) (struttura & tipo)
	private ModuloStrutturaEntity struttura;
	// Categorie (moon_fo_r_categoria_modulo, moon_fo_d_categoria)
	private CategoriaEntity categoria;
	// Portale (moon_fo_r_portale_modulo, moon_fo_d_portale)
	private List<PortaleEntity> portali;
	// Attributi
	private List<ModuloAttributoEntity> attributi;
	// EntiArea (Protocollo)
	private List<EnteAreaModulo> entiAree;
	// ProtocolloParametri
	private List<ProtocolloParametroEntity> protocolloParametri;
	
	public ModuloExportInfo getExportInformation() {
		return exportInformation;
	}
	public void setExportInformation(ModuloExportInfo exportInformation) {
		this.exportInformation = exportInformation;
	}
	public ModuloVersionatoEntity getModulo() {
		return modulo;
	}
	public void setModulo(ModuloVersionatoEntity modulo) {
		this.modulo = modulo;
	}
	public StatoModulo getStato() {
		return stato;
	}
	public void setStato(StatoModulo stato) {
		this.stato = stato;
	}
	public ModuloProgressivoEntity getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(ModuloProgressivoEntity progressivo) {
		this.progressivo = progressivo;
	}
	public ModuloStrutturaEntity getStruttura() {
		return struttura;
	}
	public void setStruttura(ModuloStrutturaEntity struttura) {
		this.struttura = struttura;
	}
	public CategoriaEntity getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaEntity categoria) {
		this.categoria = categoria;
	}
	public List<PortaleEntity> getPortali() {
		return portali;
	}
	public void setPortali(List<PortaleEntity> portali) {
		this.portali = portali;
	}
	public List<ModuloAttributoEntity> getAttributi() {
		return attributi;
	}
	public void setAttributi(List<ModuloAttributoEntity> attributi) {
		this.attributi = attributi;
	}
	public List<EnteAreaModulo> getEntiAree() {
		return entiAree;
	}
	public void setEntiAree(List<EnteAreaModulo> entiAree) {
		this.entiAree = entiAree;
	}
	public List<ProtocolloParametroEntity> getProtocolloParametri() {
		return protocolloParametri;
	}
	public void setProtocolloParametri(List<ProtocolloParametroEntity> protocolloParametri) {
		this.protocolloParametri = protocolloParametri;
	}
	
}

