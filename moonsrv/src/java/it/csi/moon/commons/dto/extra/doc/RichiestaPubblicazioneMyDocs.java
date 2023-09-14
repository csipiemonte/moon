/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.doc;

import java.util.Arrays;

import it.csi.moon.commons.entity.MyDocsRichiestaEntity;

public class RichiestaPubblicazioneMyDocs {

	public enum TipoDoc {
		ISTANZA(1, "Distinta\bdi\bpresentazione\b(pdf)", "Pratiche"), // replace space with unescape \b
//		ALLEGATO_ISTANZA(2, null, null), // NOT_USED
		DOCUMENTO_PA(3, "Documento\bPA\b(pdf)", "Pratiche"),
		;
		private Integer id;
		private String codiceTipologiaMydocs; // default tipologia descrizione, se non presente in conf attributi
		private String codiceAmbitoMydocs; // default ambito descrizione, se non presente in conf attributi
		TipoDoc(Integer id, String codiceTipologiaMydocs, String codiceAmbitoMydocs) {
			this.id=id; 
			this.codiceTipologiaMydocs=codiceTipologiaMydocs;
			this.codiceAmbitoMydocs=codiceAmbitoMydocs;
		}
		public Integer getId() { return id; }
		public String getCodiceTipologiaMydocs() { return codiceTipologiaMydocs; }
		public String getCodiceAmbitoMydocs() { return codiceAmbitoMydocs; }
		public boolean isCorrectId(Integer idTipoDocToCompare) {
			if (idTipoDocToCompare==null) return false;
			return getId().equals(idTipoDocToCompare);
		}
		public boolean isCorrect(MyDocsRichiestaEntity richiestaToCompare) {
			if (richiestaToCompare==null) return false;
			return isCorrectId(richiestaToCompare.getTipoDoc());
		}
	}
	
	// From MyDocs.Documento
	private byte[] content;
	private String descrizione;
	private String filename;
	private String cfCittadino;
	
	// From MOOn
	private Long idEnte;
	private Long idArea; // serve per la ricerca della conf (attributi)
	private TipoDoc tipoDoc; // serve per la ricerca della tipologia su MyDocs
	
	// For log (OPT)
	private Long idIstanza;
	private Long idAllegatoIstanza;
	private Long idFile;
	private Long idModulo;
	private Long idStoricoWorkflow;

	//
	private Integer idTipologiaMydocs;
	
	//
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getCfCittadino() {
		return cfCittadino;
	}
	public void setCfCittadino(String cfCittadino) {
		this.cfCittadino = cfCittadino;
	}
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public TipoDoc getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(TipoDoc tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Long getIdAllegatoIstanza() {
		return idAllegatoIstanza;
	}
	public void setIdAllegatoIstanza(Long idAllegatoIstanza) {
		this.idAllegatoIstanza = idAllegatoIstanza;
	}
	public Long getIdFile() {
		return idFile;
	}
	public void setIdFile(Long idFile) {
		this.idFile = idFile;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	public Integer getIdTipologiaMydocs() {
		return idTipologiaMydocs;
	}
	public void setIdTipologiaMydocs(Integer idTipologiaMydocs) {
		this.idTipologiaMydocs = idTipologiaMydocs;
	}
	
	@Override
	public String toString() {
		return "RichiestaPubblicazioneMyDocs [content=" + Arrays.toString(content) + ", descrizione=" + descrizione
				+ ", filename=" + filename + ", cfCittadino=" + cfCittadino + ", idEnte=" + idEnte + ", idArea="
				+ idArea + ", tipoDoc=" + tipoDoc + ", idIstanza=" + idIstanza + ", idAllegatoIstanza="
				+ idAllegatoIstanza + ", idFile=" + idFile + ", idModulo=" + idModulo + ", idStoricoWorkflow="
				+ idStoricoWorkflow + ", idTipologiaMydocs=" + idTipologiaMydocs + "]";
	}
  
}
