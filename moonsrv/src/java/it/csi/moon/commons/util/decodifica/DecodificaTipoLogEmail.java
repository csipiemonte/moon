/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.dto.LogEmail;
import it.csi.moon.commons.dto.TipoLogEmail;
import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica di TipoLogEmail, non c'è alla tabella specifica, viene salvato nel database in moon_fo_t_log_email.id_tipologia
 * 
 * @see LogEmail
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 04/09/2020 - versione iniziale
 */
public enum DecodificaTipoLogEmail implements DecodificaMOON {

	/** 1 - MOOnFO-PostSaveIstanzaTaskManager.SendEmailDichiaranteIstanzaTask */
	FO_INVIO_ISTANZA(1,"Conferma di invio istanza da MOOn FrontOffice"),
	/** 2 - MOOnBO-RichiestaItegrazione */
	BO_RICHIESTA_INTEGRAZIONE(2,"Richiesta di integrazione da MOOn BackOffice"),
	/** 3 - MOOnFO-WorkflowServiceImpl RispostaIntegrazione */
	FO_RISPOSTA_INTEGRAZIONE(3,"Conferma di invio della risposta di integrazione da MOOn FrontOffice"),
	/** 4 - MOOnBO-RespingiEmail */
	BO_RESPINGI_EMAIL(4,"Notifica di rispingimento da MOOn BackOffice"),
	/** 5 - MOOnBO-InviaComunicazione  */
    BO_INVIA_COMUNICAZIONE(5,"Invia communicazione da MOOn BackOffice"),
	/** 6 - MOOnBO-PostSaveIstanzaTaskManager.SendEmailDichiaranteIstanzaTask */
	BO_INVIO_ISTANZA(6,"Conferma di invio istanza da MOOn BackOffice"),
	/** 7 - Allegato inviato cambio stato via API */
	API_ALLEGATO_AZIONE_CAMBIO_STATO(7,"Allegato inviato cambio stato via API"),
	/** 8 - Allegato notifica via API */
	API_ALLEGATO_NOTIFICA(8, "Allegato notifica via API"),
	/** 9 - Callback di Protocollazione in ingresso da moonsrv */
	SRV_PRT_IN_CALLBACK(9, "Callback di Protocollazione in ingresso da moonsrv"),
	;
	
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	private final String descrizione;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaTipoLogEmail(Integer id, String descrizione) {
		this.id = id;
		this.descrizione = descrizione;
	}
	
	@Override
	public String getCodice() {
		return id.toString();
	}
	
	public Integer getId() {
		return id;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public TipoLogEmail getTipoLogEmail() {
		TipoLogEmail result = new TipoLogEmail();
		result.setCodice(name());
		result.setDescrizione(descrizione);
		return result;
	}
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaTipoLogEmail byCodice(String codice) {
		for(DecodificaTipoLogEmail d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	
	public static DecodificaTipoLogEmail byId(Integer id) {
		for(DecodificaTipoLogEmail d : values()) {
			if(d.id.equals(id)) {
				return d;
			}
		}
		return null;
	}
}
