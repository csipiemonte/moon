/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FruitoreIstanzaDettagliata extends FruitoreIstanza{

	private String jsonString = null;
	private Date dataProtocollo = null;
	private String numeroProtocollo = null;
	private List<FruitoreAllegato> allegati = null;
	private FruitoreModuloCodiceVersione modulo = null;
	private FruitoreEnte ente = null;
	private String attoreIns = null;
	private FruitorePagamento pagamento = null;
	
	public FruitoreIstanzaDettagliata() {
		super();

	}
	
	public FruitoreIstanzaDettagliata(String jsonString, Date dataProtocollo, String numeroProtocollo,
			List<FruitoreAllegato> allegati, FruitoreModuloCodiceVersione modulo, FruitoreEnte ente, String attoreIns,
			FruitorePagamento pagamento) {
		super();
		this.jsonString = jsonString;
		this.dataProtocollo = dataProtocollo;
		this.numeroProtocollo = numeroProtocollo;
		this.allegati = allegati;
		this.modulo = modulo;
		this.ente = ente;
		this.attoreIns = attoreIns;
		this.pagamento = pagamento;
	}
	
	/**
	 * @return the jsonString
	 */
	public String getJsonString() {
		return jsonString;
	}
	/**
	 * @return the dataProtocollo
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataProtocollo() {
		return dataProtocollo;
	}
	/**
	 * @return the numeroProtocollo
	 */
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}
	/**
	 * @return the allegati
	 */
	public List<FruitoreAllegato> getAllegati() {
		return allegati;
	}
	/**
	 * @return the modulo
	 */
	public FruitoreModuloCodiceVersione getModulo() {
		return modulo;
	}
	/**
	 * @return the ente
	 */
	public FruitoreEnte getEnte() {
		return ente;
	}
	/**
	 * @return the attoreIns
	 */
	public String getAttoreIns() {
		return attoreIns;
	}
	/**
	 * @return the pagamento
	 */
	public FruitorePagamento getPagamento() {
		return pagamento;
	}
	/**
	 * @param jsonString the jsonString to set
	 */
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	/**
	 * @param dataProtocollo the dataProtocollo to set
	 */
	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	/**
	 * @param numeroProtocollo the numeroProtocollo to set
	 */
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}
	/**
	 * @param allegati the allegati to set
	 */
	public void setAllegati(List<FruitoreAllegato> allegati) {
		this.allegati = allegati;
	}
	/**
	 * @param modulo the modulo to set
	 */
	public void setModulo(FruitoreModuloCodiceVersione modulo) {
		this.modulo = modulo;
	}
	/**
	 * @param ente the ente to set
	 */
	public void setEnte(FruitoreEnte ente) {
		this.ente = ente;
	}
	/**
	 * @param attoreIns the attoreIns to set
	 */
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
	}
	/**
	 * @param pagamento the pagamento to set
	 */
	public void setPagamento(FruitorePagamento pagamento) {
		this.pagamento = pagamento;
	}
	
	@Override
	public String toString() {
		return "FruitoreIstanzaDettagliata [jsonString=" + jsonString + ", dataProtocollo=" + dataProtocollo
				+ ", numeroProtocollo=" + numeroProtocollo + ", allegati=" + allegati + ", modulo=" + modulo + ", ente="
				+ ente + ", attoreIns=" + attoreIns + ", pagamento=" + pagamento + "]";
	}

}
