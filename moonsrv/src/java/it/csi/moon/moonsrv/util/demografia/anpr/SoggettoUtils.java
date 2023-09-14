/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util.demografia.anpr;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.apimint.demografia.v1.dto.Cittadinanza;
import it.csi.apimint.demografia.v1.dto.CivicoInterno;
import it.csi.apimint.demografia.v1.dto.CodiceFiscale;
import it.csi.apimint.demografia.v1.dto.Comune;
import it.csi.apimint.demografia.v1.dto.Generalita;
import it.csi.apimint.demografia.v1.dto.IdSchedaSoggettoComune;
import it.csi.apimint.demografia.v1.dto.Localita;
import it.csi.apimint.demografia.v1.dto.LuogoEvento;
import it.csi.apimint.demografia.v1.dto.Residenza;
import it.csi.apimint.demografia.v1.dto.Soggetto;
import it.csi.moon.moonsrv.util.demografia.anpr.decodifica.EnumTipoIndirizzoT25;

/**
 * Class utility su Soggetto (API Manager Outer servizi REST)
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 12/03/2020 - versione iniziale
 */
public final class SoggettoUtils {

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public Residenza getResidenzaITAoESTERA(Soggetto soggetto) {
		Residenza residenza = getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RESIDENZA);
		residenza = residenza!=null?residenza:getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RETTIFICA_POST_ACCERTAMENTI);
		residenza = residenza!=null?residenza:getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RESIDENZA_ESTERA);
		residenza = residenza!=null?residenza:getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.REVISIONE_ONOMASTICA_STRADALE);
		return residenza;
	}
	
	public Residenza getResidenzaITA(Soggetto soggetto) {
		Residenza indirizzoResidenza = getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RESIDENZA);
		return indirizzoResidenza!=null?indirizzoResidenza:getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RETTIFICA_POST_ACCERTAMENTI);
	}
	
	public Residenza getResidenzaESTERA(Soggetto soggetto) {
		Residenza indirizzoResidenza = getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RESIDENZA_ESTERA);
		return indirizzoResidenza!=null?indirizzoResidenza:getResidenzaByTipoIndirizzo(soggetto, EnumTipoIndirizzoT25.RETTIFICA_POST_ACCERTAMENTI);
	}
	
	public Residenza getResidenzaByTipoIndirizzo(Soggetto soggetto, EnumTipoIndirizzoT25 tipoIndirizzo) {
		Residenza result = null;
		if(soggetto !=null && soggetto.getResidenza() != null) {
			for(Residenza r : soggetto.getResidenza()) {
				if(tipoIndirizzo.isCorrectTipoIndirizzo(r) &&
					(result==null || isDataDecorrenzaResidenzaPiuRecente(r.getDataDecorrenzaResidenza(), result.getDataDecorrenzaResidenza())) ) {
						result = r;
				}
			}
		}
		return result;
	}
	
	private boolean isDataDecorrenzaResidenzaPiuRecente(Date dataDecorrenzaResidenzaToCompare,
			Date dataDecorrenzaResidenzaResult) {
		return (dataDecorrenzaResidenzaToCompare != null && dataDecorrenzaResidenzaResult != null && 
				dataDecorrenzaResidenzaToCompare.after(dataDecorrenzaResidenzaResult)) ? true : false;
	}

	public Generalita cloneGeneralita(Soggetto soggetto) {
		if(soggetto==null)
			return null;
		
		return cloneGeneralita(soggetto.getGeneralita());
	}
	
	public Generalita cloneGeneralita(Generalita g) {
		if(g==null)
			return null;
		
		Generalita cloneGeneralita = new Generalita();
		cloneGeneralita.setAnnoEspatrio(g.getAnnoEspatrio());
		if(g.getCodiceFiscale()!=null) {
			CodiceFiscale cf = new CodiceFiscale();
			cf.setCodFiscale(g.getCodiceFiscale().getCodFiscale());
			cf.setDataAttribuzioneValidita(g.getCodiceFiscale().getDataAttribuzioneValidita());
			cf.setValiditaCF(g.getCodiceFiscale().getValiditaCF());
			cloneGeneralita.setCodiceFiscale(cf);
		}
		cloneGeneralita.setCognome(g.getCognome());
		cloneGeneralita.setDataNascita(g.getDataNascita());
//		cloneGeneralita.setIdLuogoNascitaNao(g.getIdLuogoNascitaNao());
		cloneGeneralita.setIdSchedaSoggettoANPR(g.getIdSchedaSoggettoANPR());
		if(g.getIdSchedaSoggettoComune()!=null) {
			IdSchedaSoggettoComune idSogComune = new IdSchedaSoggettoComune();
			idSogComune.setIdSchedaSoggetto(g.getIdSchedaSoggettoComune().getIdSchedaSoggetto());
			idSogComune.setIdSchedaSoggettoComuneIstat(g.getIdSchedaSoggettoComune().getIdSchedaSoggettoComuneIstat());
			cloneGeneralita.setIdSchedaSoggettoComune(idSogComune);
		}
//		cloneGeneralita.setIdSoggettoNao(g.getIdSoggettoNao());
//		cloneGeneralita.setIdStatoSoggettoNao(g.getIdStatoSoggettoNao());
		
		// luogoNascita
		LuogoEvento luogoNascita = g.getLuogoNascita();
		if(luogoNascita!=null) {
			LuogoEvento tle = new LuogoEvento();
			if(luogoNascita.getComune()!=null) {
				Comune tc = new Comune();
				tc.setCodiceIstat(luogoNascita.getComune().getCodiceIstat());
				tc.setDescrizioneLocalita(luogoNascita.getComune().getDescrizioneLocalita());
				tc.setNomeComune(luogoNascita.getComune().getNomeComune());
				tc.setSiglaProvinciaIstat(luogoNascita.getComune().getSiglaProvinciaIstat());
				tle.setComune(tc);
			}
			if(luogoNascita.getLocalita()!=null) {
				Localita tl = new Localita();
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
//		cloneGeneralita.setNumeroIndividualeAIREFormattatoNao(g.getNumeroIndividualeAIREFormattatoNao());
//		cloneGeneralita.setNumeroIndividualeAIRENao(g.getNumeroIndividualeAIRENao());
//		cloneGeneralita.setNumeroIndividualeNao(g.getNumeroIndividualeNao());
		cloneGeneralita.setSenzaCognome(g.getSenzaCognome());
		cloneGeneralita.setSenzaGiorno(g.getSenzaGiorno());
		cloneGeneralita.setSenzaGiornoMese(g.getSenzaGiornoMese());
		cloneGeneralita.setSenzaNome(g.getSenzaNome());
		cloneGeneralita.setSesso(g.getSesso());
		cloneGeneralita.setSoggettoAIRE(g.getSoggettoAIRE());
		return cloneGeneralita;
	}
	
	
	
	/**
	 * 
	 * @param residenzaAnpr
	 * @return descrizione completa della via. es.:  VIA DELLA FONTANA 
	 */
	public String retrieveDescrizioneVia(Residenza residenzaAnpr) {
		return residenzaAnpr.getIndirizzo().getToponimo().getSpecie() + " " + residenzaAnpr.getIndirizzo().getToponimo().getDenominazioneToponimo();
	}
	
	/**
	 * 
	 * @param residenzaAnpr
	 * @return descrizione completa della via. es.:  VIA DELLA FONTANA 95 BIS Scala:A
	 */
	public String retrieveDescrizioneResidenzaCompleta(Residenza residenzaAnpr) {
		if (residenzaAnpr==null || residenzaAnpr.getIndirizzo()==null || residenzaAnpr.getIndirizzo().getToponimo()==null) {
			return null;
		}
		StringBuilder strB = new StringBuilder()
			.append(residenzaAnpr.getIndirizzo().getToponimo().getSpecie())
			.append(" ")
			.append(residenzaAnpr.getIndirizzo().getToponimo().getDenominazioneToponimo());
		
		if (residenzaAnpr.getIndirizzo().getNumeroCivico()!=null) {
			strB.append(" ").append(residenzaAnpr.getIndirizzo().getNumeroCivico().getNumero());
			if (StringUtils.isNotEmpty(residenzaAnpr.getIndirizzo().getNumeroCivico().getEsponente1())) 
				strB.append(" ").append(residenzaAnpr.getIndirizzo().getNumeroCivico().getEsponente1());
		}
		
		CivicoInterno civicoInterno = residenzaAnpr.getIndirizzo().getNumeroCivico().getCivicoInterno();
		if (civicoInterno!=null) {
			strB.append("  ");
			if (StringUtils.isNotEmpty(civicoInterno.getScala())) 
				strB.append(" Scala:").append(civicoInterno.getScala());
			if (StringUtils.isNotEmpty(civicoInterno.getInterno1())) 
				strB.append(" Int:").append(civicoInterno.getInterno1());
			if (StringUtils.isNotEmpty(civicoInterno.getEspInterno1())) 
				strB.append("/").append(civicoInterno.getEspInterno1());
		}
		
		return strB.toString();
	}
	
	/**
	 * 
	 */
	public String retrieveDescrizioneLuogo(LuogoEvento luogoEvento) {
		if(luogoEvento==null)
			return null;

		String luogo = null;
		String infoAdd = null; // siglaProviuncia OR NazioneEstera
		
		if(luogoEvento.getComune()!=null) {
			luogo = luogoEvento.getComune().getNomeComune();
			infoAdd = luogoEvento.getComune().getSiglaProvinciaIstat();
		} else if(luogoEvento.getLocalita()!=null) {
			luogo = luogoEvento.getLocalita().getDescrizioneLocalita();
			infoAdd = luogoEvento.getLocalita().getDescrizioneStato();
		} else if(luogoEvento.getLuogoEccezionale()!=null) {
			luogo = luogoEvento.getLuogoEccezionale();
		}
		
		if(infoAdd!=null) {
			luogo = luogo + " (" + infoAdd + ")";
		}
		return luogo;
	}
	

	/**
	 * Restituisce il SoggettoRichiedente sulla base del CF
	 * @param codiceFiscaleRichiedente
	 * @param soggettiFamigliaANPR
	 * @return SoggettoANPR
	 */
	public Soggetto findSoggettoRichiedente(String codiceFiscaleRichiedente, List<Soggetto> soggetti) {
		if (soggetti==null || soggetti.isEmpty()) 
			return null;
		for (Soggetto soggetto : soggetti) {
			if (codiceFiscaleRichiedente.equals(soggetto.getGeneralita().getCodiceFiscale().getCodFiscale())) {
				return soggetto;
			}
		}
		return null;
	}
	
	/**
	 * Translate la lista di cittadinanze di ANPR to String
	 * Prendiamo solo la prima Cittadiananza (per Cittadini di COTO è suffisciente aututalmente ; NAO fornisce ad ANPR solo una CITTD)
	 * 
	 * @param cittadinanza
	 * @return la descrizione della cittadinanza che corrisponde per anpr alla descrizione della nazione di appartenanza
	 */
	public String translateCittadinanzaANPR(List<Cittadinanza> cittadinanza) {
		if (cittadinanza==null || cittadinanza.isEmpty())
			return null;
		Cittadinanza cittadinanzaANPR = cittadinanza.get(0);
		return cittadinanzaANPR.getDescrizioneStato();
	}

	/**
	 * Translate una data da Date to String
	 * INPUT  es. "1973-09-21T00:00:00+0200"
	 * OUTPUT es. "21/09/1973"
	 * 
	 * Per il momento gestisce solo date complete, TODO nel caso di date incomplete ...
	 * 
	 * @param date
	 * @return la data in formato String DD/mm/YYYY
	 */
	public String translateData(Date date) {
		String result = sdf.format(date);
		return result;
	}
	
	public String translateData(Date date, SimpleDateFormat formato) {
		String result = formato.format(date);
		return result;
	}
	
	public String translateDateNew(Date date, SimpleDateFormat formato) {
		String result = "";
		if (date != null)
			result = formato.format(date);
		
		return result;
	}

	public String translateLuogoANPR(LuogoEvento luogoEvento) {
		if(luogoEvento==null)
			return null;

		String luogo = null;
		String infoAdd = null; // siglaProviuncia OR NazioneEstera
		
		if(luogoEvento.getComune()!=null) {
			luogo = luogoEvento.getComune().getNomeComune();
			infoAdd = luogoEvento.getComune().getSiglaProvinciaIstat();
		} else if(luogoEvento.getLocalita()!=null) {
			luogo = luogoEvento.getLocalita().getDescrizioneLocalita();
			infoAdd = luogoEvento.getLocalita().getDescrizioneStato();
		} else if(luogoEvento.getLuogoEccezionale()!=null) {
			luogo = luogoEvento.getLuogoEccezionale();
		}
		
		if(infoAdd!=null) {
			luogo = luogo + " (" + infoAdd + ")";
		}
		return luogo;
	}
	
	public boolean isMinorenne(Generalita generalita) {
		return getAge(generalita)<18;
	}
		
	public int getAge(Generalita generalita) {
		return Period.between(
				LocalDateTime.ofInstant(generalita.getDataNascita().toInstant(), ZoneId.systemDefault()).toLocalDate() , 
				LocalDateTime.now().toLocalDate() //.truncatedTo(ChronoUnit.DAYS)
			).getYears();
	}	
	
}
