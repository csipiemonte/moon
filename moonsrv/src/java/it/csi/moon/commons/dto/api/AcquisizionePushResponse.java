/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.Date;
import java.util.Objects;

/**
 * 
 * @author laurent
 *
 */
public class AcquisizionePushResponse {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [implicit-camel-case]

	private String codiceAzione = null;  // INTEGRAZIONE_FRUITORE_OK
	private String codice = null; // IMP / IMPW / IMPKO
	private String descrizione = null; // Importato / Importato con Warning / Errore di importazione
	private String identificativo = null; // numeroPratica generato dal Gestionale
	private Date data = null;
	private String numeroProtocollo = null;
	private Date dataProtocollo = null;
	
	private String codiceFiscaleOperatore = null;

	// nome originario nello yaml: codiceAzione
	public String getCodiceAzione() {
		return codiceAzione;
	}
	public void setCodiceAzione(String codiceAzione) {
		this.codiceAzione = codiceAzione;
	}

	/**
	 **/

	// nome originario nello yaml: codice
	public String getCodice() {
		return codice;
	}

	public void setCodice(String resultCode) {
		this.codice = resultCode;
	}

	/**
	 **/

	// nome originario nello yaml: descrizione
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String resultDescription) {
		this.descrizione = resultDescription;
	}

	/**
	 **/

	// nome originario nello yaml: identificativo
	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String resultReceiptId) {
		this.identificativo = resultReceiptId;
	}

	/**
	 **/

	// nome originario nello yaml: data
	public Date getData() {
		return data;
	}

	public void setData(Date resultTimestamp) {
		this.data = resultTimestamp;
	}

	/**
	 **/

	// nome originario nello yaml: numeroProtocollo
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	/**
	 **/

	// nome originario nello yaml: dataProtocollo
	public Date getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocolo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	
	/*
	 */
	public String getCodiceFiscaleOperatore() {
		return codiceFiscaleOperatore;
	}

	public void setCodiceFiscaleOperatore(String codiceFiscaleOperatore) {
		this.codiceFiscaleOperatore = codiceFiscaleOperatore;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AcquisizionePushResponse acquisizione = (AcquisizionePushResponse) o;
		return
		Objects.equals(codiceAzione, acquisizione.codiceAzione) 
			&& Objects.equals(codice, acquisizione.codice)
			&& Objects.equals(descrizione, acquisizione.descrizione)
			&& Objects.equals(identificativo, acquisizione.identificativo)
			&& Objects.equals(data, acquisizione.data)
			&& Objects.equals(numeroProtocollo, acquisizione.numeroProtocollo)
			&& Objects.equals(dataProtocollo, acquisizione.dataProtocollo)
			&& Objects.equals(codiceFiscaleOperatore, acquisizione.codiceFiscaleOperatore);
	}

	@Override
	public int hashCode() {
		return Objects.hash(/* outcome, */ codice, descrizione, identificativo, data);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AcquisizionePushResponse {\n");

		sb.append("    codiceAzione: ").append(toIndentedString(codiceAzione)).append("\n");
		sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
		sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
		sb.append("    identificativo: ").append(toIndentedString(identificativo)).append("\n");
		sb.append("    data: ").append(toIndentedString(data)).append("\n");
		sb.append("    numeroProtocollo: ").append(toIndentedString(numeroProtocollo)).append("\n");
		sb.append("    dataProtocollo: ").append(toIndentedString(dataProtocollo)).append("\n");
		sb.append("    codiceFiscaleOperatore: ").append(toIndentedString(codiceFiscaleOperatore)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
