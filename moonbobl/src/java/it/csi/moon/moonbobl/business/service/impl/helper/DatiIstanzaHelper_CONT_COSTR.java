/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonNode;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrDomandaEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class DatiIstanzaHelper_CONT_COSTR extends DatiIstanzaHelper {
	
	private final static String CLASS_NAME = "DatiIstanzaHelper_CONT_COSTR";
	private final static Long MAX_CONTRIBUTO = 5000000L; // 50.000,00 EUR in cents
	
	public OneriCostrDomandaEntity parse(String datiIstanza) throws BusinessException {
		OneriCostrDomandaEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parse] dato da acquisire : nome del comune");
			initDataNode(datiIstanza);
			
			result = new OneriCostrDomandaEntity();
			result.setCodiceFiscale(dataNode.get("codiceFiscale")!=null?dataNode.get("codiceFiscale").getTextValue():null);
			result.setCognome(dataNode.get("cognome")!=null?dataNode.get("cognome").getTextValue():null);
			result.setNome(dataNode.get("nome")!=null?dataNode.get("nome").getTextValue():null);
			result.setEmail(dataNode.get("email")!=null?dataNode.get("email").getTextValue():null);
			result.setCellulare(dataNode.get("cellulare")!=null?dataNode.get("cellulare").getTextValue():null);
			result.setFlagDichiaranteBeneficiario(dataNode.get("ilRichiedenteEAncheBeneficiario")!=null?dataNode.get("ilRichiedenteEAncheBeneficiario").getTextValue():null); // S / N
			if ("S".equalsIgnoreCase(result.getFlagDichiaranteBeneficiario())) {
				// Ribalto i Dati del Dichiarante come Benificiario
				result.setBenTipologiaBeneficiario("personaFisica");
				result.setBenCodiceFiscale(result.getCodiceFiscale());
				result.setBenCognome(result.getCognome());
				result.setBenNome(result.getNome());
			} else {
				result.setBenTipologiaBeneficiario(dataNode.get("tipologiaBeneficiario")!=null?dataNode.get("tipologiaBeneficiario").getTextValue():null); // personaFisica, impresa
				if ("personaFisica".equalsIgnoreCase(result.getBenTipologiaBeneficiario())) {
					result.setBenCodiceFiscale(dataNode.get("codiceFiscaleBeneficiario")!=null?dataNode.get("codiceFiscaleBeneficiario").getTextValue():null);
					result.setBenCognome(dataNode.get("cognomeBeneficiario")!=null?dataNode.get("cognomeBeneficiario").getTextValue():null);
					result.setBenNome(dataNode.get("nomeBeneficiario")!=null?dataNode.get("nomeBeneficiario").getTextValue():null);
					
					result.setBenRagioneSociale(null);
					result.setBenPiva(null); // micro, piccola, media, grande
					result.setBenDataFineEsercizio(null); // dd-MM-yyyy
					result.setBenDimensione(null);
					result.setBenCodiceAteco(null);
					result.setBenFormaGiuridica(null);
					
					result.setBenSedeNazione(null);
					result.setBenSedeRegione(null);
					result.setBenSedeProvincia(null);
					result.setBenSedeComune(null);
					result.setBenSedeIndirizzo(null);
					result.setBenSedeCap(null);
				} 
				if ("impresa".equalsIgnoreCase(result.getBenTipologiaBeneficiario())) {
					result.setBenCodiceFiscale(null);
					result.setBenCognome(null);
					result.setBenNome(null);
					
					result.setBenRagioneSociale(dataNode.get("ragioneSocialeBeneficiario")!=null?dataNode.get("ragioneSocialeBeneficiario").getTextValue():null);
					result.setBenPiva(dataNode.get("partitaIvaBeneficiario")!=null?dataNode.get("partitaIvaBeneficiario").getTextValue():null); // micro, piccola, media, grande
					result.setBenDataFineEsercizio(parseDate(dataNode.get("dataFineEsercizioBeneficiario"))); // dd-MM-yyyy
					result.setBenDimensione(dataNode.get("dimensioneImpresaBeneficiario")!=null?dataNode.get("dimensioneImpresaBeneficiario").getTextValue():null);
					if (dataNode.get("codiceAtecoBeneficiario")!=null) {
						String nomeAteco = dataNode.get("codiceAtecoBeneficiario").get("nome").getTextValue();
						result.setBenCodiceAteco(nomeAteco.substring(0, 8));
					}
					result.setBenFormaGiuridica(dataNode.get("formaGiuridicaBeneficiario")!=null?dataNode.get("formaGiuridicaBeneficiario").get("nome").getTextValue():null);
					result.setBenSedeNazione(dataNode.get("nazioneSedeBeneficiario")!=null?dataNode.get("nazioneSedeBeneficiario").get("nome").getTextValue():null);
					result.setBenSedeRegione(dataNode.get("regioneSedeBeneficiario")!=null?dataNode.get("regioneSedeBeneficiario").get("nome").getTextValue():null);
					result.setBenSedeProvincia(dataNode.get("provinciaSedeBeneficiario")!=null?dataNode.get("provinciaSedeBeneficiario").get("nome").getTextValue():null);
					result.setBenSedeComune(dataNode.get("comuneSedeBeneficiario")!=null?dataNode.get("comuneSedeBeneficiario").get("nome").getTextValue():null);
					result.setBenSedeIndirizzo(dataNode.get("indirizzoSedeBeneficiario")!=null?dataNode.get("indirizzoSedeBeneficiario").getTextValue():null);
					result.setBenSedeCap(dataNode.get("capSedeBeneficiario")!=null?dataNode.get("capSedeBeneficiario").getTextValue():null);
				}
			}
			// Pratica
			result.setPrtCodiceProvincia(dataNode.get("provincia")!=null?String.format("%03d", dataNode.get("provincia").get("codice").getIntValue()):null);
			result.setPrtNomeProvincia(dataNode.get("provincia")!=null?dataNode.get("provincia").get("nome").getTextValue():null);
			result.setPrtCodiceComune(dataNode.get("comune")!=null?String.format("%06d", dataNode.get("comune").get("codice").getIntValue()):null);
			result.setPrtNomeComune(dataNode.get("comune")!=null?dataNode.get("comune").get("nome").getTextValue():null);
			result.setPrtIndirizzo(dataNode.get("indirizzo")!=null?dataNode.get("indirizzo").getTextValue():null);
			result.setPrtCap(dataNode.get("cap")!=null?dataNode.get("cap").getTextValue():null);
			result.setPrtTipologiaTitoloEdilizio(dataNode.get("tipologiaTitoloEdilizio")!=null?dataNode.get("tipologiaTitoloEdilizio").getTextValue():null);
			result.setPrtDataPresentazione(parseDate(dataNode.get("dataPresentazionePratica"))); // dd-MM-yyyy
			result.setPrtNumeroPratica(dataNode.get("numeroPratica")!=null?dataNode.get("numeroPratica").getTextValue():null);
			result.setPrtTipologiaIntervento(dataNode.get("tipologiaIntervento")!=null?dataNode.get("tipologiaIntervento").getTextValue():null); // costruito, nuovo
			if (dataNode.get("costoPresunto")!=null) {
				BigDecimal bd = dataNode.get("costoPresunto").getDecimalValue();
				Long costoPresuntoInteger = Long.parseLong("" + new BigDecimal((bd.doubleValue()*100)).setScale(0,RoundingMode.HALF_UP));
				result.setPrtCostoPresunto(costoPresuntoInteger);
			}
			
			if (result.getPrtCostoPresunto()!=null) {
				Long importoPresunto = Math.min( Math.round( result.getPrtCostoPresunto() * getFactor(result)), MAX_CONTRIBUTO );
				result.setPrtImportoPresunto(importoPresunto);
				result.setBoImportoPagato(importoPresunto);
			}
			
			return result;
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parse] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parse] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parse] OUT result:" + result);
		}
	}
	
	private Float getFactor(OneriCostrDomandaEntity entity) {
		Float result = 0.5f; // Default per nuovaCostruzione
		if (result!=null && "costruito".equals(entity.getPrtTipologiaIntervento())) result = 1.0f; // 100% per Intervento sul costruito
		return result;
	}

	private Date parseDate(JsonNode jsonNode) throws ParseException {
		if (jsonNode==null) return null;
		try {
			SimpleDateFormat sdf_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
			String date10 = jsonNode.getTextValue().substring(0,10);
			return sdf_yyyy_MM_dd.parse(date10);
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parseDate] ParseException sdf_dd_MM_yyyy with "+jsonNode);
			throw (e);
		}
	}

}
