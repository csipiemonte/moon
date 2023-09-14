/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util.demografia.anpr;

import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.SoggettoANPR;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.TipoCodiceFiscale;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.TipoComune;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.TipoGeneralita;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.TipoIdSchedaSoggettoComune;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.TipoLocalita;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.TipoLuogoEvento;
import it.csi.demografia.orchanpr.ricercheanpr.cxfclient.TipoResidenza;
import it.csi.moon.moonsrv.util.demografia.anpr.decodifica.EnumTipoIndirizzoT25;

/**
 * Class utility su SoggettoANPR *** OLD *** solo per uso con NAOSRV via WS
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 13/08/2018 - versione iniziale
 */
public final class SoggettoANPRUtils {

	public static TipoResidenza getResidenzaITAoESTERA(SoggettoANPR soggetto) {
		TipoResidenza residenza = getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RESIDENZA);
		residenza = residenza!=null?residenza:getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RETTIFICA_POST_ACCERTAMENTI);
		residenza = residenza!=null?residenza:getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RESIDENZA_ESTERA);
		residenza = residenza!=null?residenza:getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.REVISIONE_ONOMASTICA_STRADALE);
		return residenza;
	}
	
	public static TipoResidenza getResidenzaITA(SoggettoANPR soggetto) {
		TipoResidenza indirizzoResidenza = getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RESIDENZA);
		return indirizzoResidenza!=null?indirizzoResidenza:getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RETTIFICA_POST_ACCERTAMENTI);
	}
	
	public static TipoResidenza getResidenzaESTERA(SoggettoANPR soggetto) {
		TipoResidenza indirizzoResidenza = getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RESIDENZA_ESTERA);
		return indirizzoResidenza!=null?indirizzoResidenza:getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RETTIFICA_POST_ACCERTAMENTI);
	}
	
	public static TipoResidenza getResidenzaByTipoIndirizzo(SoggettoANPR soggetto, EnumTipoIndirizzoT25 tipoIndirizzo) {
		TipoResidenza result = null;
		if(soggetto !=null && soggetto.getResidenza() != null) {
			for(TipoResidenza tr : soggetto.getResidenza()) {
				if(tipoIndirizzo.isCorrectTipoIndirizzo(tr)) {
					result = tr;
				}
			}
		}
		return result;
	}
	
	
	public static TipoGeneralita cloneGeneralita(SoggettoANPR soggetto) {
		if(soggetto==null)
			return null;
		
		return cloneGeneralita(soggetto.getGeneralita());
	}
	
	public static TipoGeneralita cloneGeneralita(TipoGeneralita g) {
		if(g==null)
			return null;
		
		TipoGeneralita cloneGeneralita = new TipoGeneralita();
		cloneGeneralita.setAnnoEspatrio(g.getAnnoEspatrio());
		if(g.getCodiceFiscale()!=null) {
			TipoCodiceFiscale tcf = new TipoCodiceFiscale();
			tcf.setCodFiscale(g.getCodiceFiscale().getCodFiscale());
			tcf.setDataAttribuzioneValidita(g.getCodiceFiscale().getDataAttribuzioneValidita());
			tcf.setValiditaCF(g.getCodiceFiscale().getValiditaCF());
			cloneGeneralita.setCodiceFiscale(tcf);
		}
		cloneGeneralita.setCognome(g.getCognome());
		cloneGeneralita.setDataNascita(g.getDataNascita());
		cloneGeneralita.setIdLuogoNascitaNao(g.getIdLuogoNascitaNao());
		cloneGeneralita.setIdSchedaSoggettoANPR(g.getIdSchedaSoggettoANPR());
		if(g.getIdSchedaSoggettoComune()!=null) {
			TipoIdSchedaSoggettoComune idSogComune = new TipoIdSchedaSoggettoComune();
			idSogComune.setIdSchedaSoggetto(g.getIdSchedaSoggettoComune().getIdSchedaSoggetto());
			idSogComune.setIdSchedaSoggettoComuneIstat(g.getIdSchedaSoggettoComune().getIdSchedaSoggettoComuneIstat());
			cloneGeneralita.setIdSchedaSoggettoComune(idSogComune);
		}
		cloneGeneralita.setIdSoggettoNao(g.getIdSoggettoNao());
		cloneGeneralita.setIdStatoSoggettoNao(g.getIdStatoSoggettoNao());
		
		// luogoNascita
		TipoLuogoEvento luogoNascita = g.getLuogoNascita();
		if(luogoNascita!=null) {
			TipoLuogoEvento tle = new TipoLuogoEvento();
			if(luogoNascita.getComune()!=null) {
				TipoComune tc = new TipoComune();
				tc.setCodiceIstat(luogoNascita.getComune().getCodiceIstat());
				tc.setDescrizioneLocalita(luogoNascita.getComune().getDescrizioneLocalita());
				tc.setNomeComune(luogoNascita.getComune().getNomeComune());
				tc.setSiglaProvinciaIstat(luogoNascita.getComune().getSiglaProvinciaIstat());
				tle.setComune(tc);
			}
			if(luogoNascita.getLocalita()!=null) {
				TipoLocalita tl = new TipoLocalita();
				tl.setCodiceStato(luogoNascita.getLocalita().getCodiceStato());
				tl.setDescrizioneLocalita(luogoNascita.getLocalita().getDescrizioneLocalita());
				tl.setDescrizioneStato(luogoNascita.getLocalita().getDescrizioneStato());
				tl.setProvinciaContea(luogoNascita.getLocalita().getProvinciaContea());
				tle.setLocalita(tl);
			}
			tle.setLuogoEccezionale(luogoNascita.getLuogoEccezionale());
			cloneGeneralita.setLuogoNascita(tle);
		}
		cloneGeneralita.setNome(g.getNome());
		cloneGeneralita.setNote(g.getNote());
		cloneGeneralita.setNumeroIndividualeAIREFormattatoNao(g.getNumeroIndividualeAIREFormattatoNao());
		cloneGeneralita.setNumeroIndividualeAIRENao(g.getNumeroIndividualeAIRENao());
		cloneGeneralita.setNumeroIndividualeNao(g.getNumeroIndividualeNao());
		cloneGeneralita.setSenzaCognome(g.getSenzaCognome());
		cloneGeneralita.setSenzaGiorno(g.getSenzaGiorno());
		cloneGeneralita.setSenzaGiornoMese(g.getSenzaGiornoMese());
		cloneGeneralita.setSenzaNome(g.getSenzaNome());
		cloneGeneralita.setSesso(g.getSesso());
		cloneGeneralita.setSoggettoAIRE(g.getSoggettoAIRE());
		return cloneGeneralita;
	}
	
}
