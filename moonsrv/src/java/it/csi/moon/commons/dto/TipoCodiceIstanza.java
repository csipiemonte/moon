/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;
/**
 * Decodifica delle tipologie di progressivo codice_istanza (moon_io_d_tipocodiceistanza)
 * <br>Viene usato al primo salvataggio di un instanza per generare il codice_istanza univoco
 * <br>
 * <br>Tabella moon_io_d_tipocodiceistanza
 * <br>PK: idTipoCodiceIstanza
 * <br>
 * <br>Esempi:
 * <br>1 - codice_modulo.versione.anno.prog	codice del modulo_versione_anno di creazione istanza_progressivo assoluto
 * <br>2 - codice_modulo.versione.anno.prog_annuale	codice del modulo_versione_anno di creazione istanza_progressivo annuale
 * <br>3 - codice_modulo.str5	codice del modulo_versione_stringa alfanumerica di 5 caratteri
 * <br>4 - codice_modulo.num8	codice del modulo_stringa numerica di 8 caratteri
 * <br>5 - prog	solo progressivo assoluto
 * <br>6 - codice_modulo.prog	codice del modulo_progressivo assoluto
 * <br>7 - codice_modulo.anno.prog	codice del modulo_anno di creazione istanza_progressivo assoluto
 * <br>8 - codice_modulo.anno.num7	codice del modulo_versione_anno di creazione istanza_numero di 7
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class TipoCodiceIstanza {

	private Integer idTipoCodiceIstanza = null;
	private String descCodice = null;
	private String descrizioneTipo = null;
	
	public TipoCodiceIstanza() {
		
	}
	
	public TipoCodiceIstanza(Integer idTipoCodiceIstanza, String descCodice, String descrizioneTipo) {
		this.idTipoCodiceIstanza = idTipoCodiceIstanza;
		this.descCodice = descCodice;
		this.descrizioneTipo = descrizioneTipo;
	}

	public Integer getIdTipoCodiceIstanza() {
		return idTipoCodiceIstanza;
	}

	public void setIdTipoCodiceIstanza(Integer idTipoCodiceIstanza) {
		this.idTipoCodiceIstanza = idTipoCodiceIstanza;
	}

	public String getDescCodice() {
		return descCodice;
	}

	public void setDescCodice(String descCodice) {
		this.descCodice = descCodice;
	}

	public String getDescrizioneTipo() {
		return descrizioneTipo;
	}

	public void setDescrizioneTipo(String descrizioneTipo) {
		this.descrizioneTipo = descrizioneTipo;
	}
	
}
