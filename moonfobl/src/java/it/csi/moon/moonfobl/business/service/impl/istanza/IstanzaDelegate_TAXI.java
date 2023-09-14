/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.istanza;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitBLParams;
import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.extra.demografia.DocumentoRiconoscimento;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.moonfobl.business.service.impl.dao.ext.impl.BuoniTaxiDAOimpl;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.ExtTaxiEntity;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * IstanzaDelegate_TAXI - Specializzazione.
 *  - getInitIstanza : Bloccare l'accesso al modulo se non residente a TORINO
 *  - inviaUlteriori richiamato da saveIstanza per salvataggio ulteriore in moon_ext_taxi via taxiDAO
 * 
 * @author laurent
 *
 */
public class IstanzaDelegate_TAXI extends IstanzaDefaultDelegate implements IstanzaServiceDelegate {

	private static final String CLASS_NAME = "IstanzaDelegate_TAXI";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private SecureRandom random;
	
	@Autowired
	BuoniTaxiDAOimpl buoniTaxiDAO;

	public IstanzaDelegate_TAXI() {
		LOG.debug("[" + CLASS_NAME + "::" + CLASS_NAME + "]");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public SecureRandom getRandom() {
		if (random==null) {
			random = new SecureRandom();
		}
		return random;
	}


	//
	// INIT ISTANZA
	//
	@Override
	public Istanza getInitIstanza(UserInfo user, ModuloVersionatoEntity moduloEntity, MapModuloAttributi attributi, IstanzaInitBLParams params, HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN moduloEntity: " + moduloEntity);

			// TODO Bloccare l'accesso al modulo se non residente a TORINO

			Istanza istanza = null;
			istanza = moonsrvDAO.initIstanza(httpRequest.getRemoteAddr(), user, moduloEntity.getIdModulo(), moduloEntity.getIdVersioneModulo(), params);

			return istanza;
						
		} 
		catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException(e);
		}
	}

	//
	// INVI ISTANZA
	//
	@Override
	protected void inviaUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, Long idProcesso) throws BusinessException {
		insertExtTaxi(user, response, idProcesso);
	}



	private void insertExtTaxi(UserInfo user, IstanzaSaveResponse response, Long idProcesso) throws DAOException, BusinessException {
		DatiIstanzaHelper datiHelper = new DatiIstanzaHelper();
		try {
			JsonNode data = datiHelper.initDataNode(response.getIstanza());
			if(!datiHelper.extractedBooleanValueFromDataNodeByKey("presenteToSolidale")) {

				boolean isFamigliare = false;
				//if(response.getIstanza().getModulo().getCodiceModulo().equalsIgnoreCase("TAXI_CI"))
				String s = datiHelper.extractedTextValueFromDataNodeByKey("appartenenteCategoria");
				LOG.debug("ATTENZIONE: appartenenteCategoria: " +s);
				if (s!=null)
					isFamigliare= datiHelper.extractedTextValueFromDataNodeByKey("appartenenteCategoria").equalsIgnoreCase("unFamigliare");
				LOG.debug("ATTENZIONE: isFamigliare: " +isFamigliare);
				String cf = "";
				String famigliareScelto = "";
				if (isFamigliare) {
					famigliareScelto = datiHelper.extractedTextValueFromDataNodeByKey("famiglia.famigliare.famigliare");
					cf = getFamigliareSceltoCf(famigliareScelto);
				}else 
					cf = datiHelper.extractedTextValueFromDataNodeByKey("richiedente.codiceFiscale");

				ExtTaxiEntity esistente = buoniTaxiDAO.findByCf(cf);
				if(esistente==null) {
					// Salvataggio in ext Taxi dei dati estratti dall'istanza via datiHelper
					ExtTaxiEntity entity = new ExtTaxiEntity();
					entity.setCodice_fiscale(cf);
					String nome="";
					String cognome="";
					String data_nascita="";
					if (isFamigliare) {
						nome=getFamigliareSceltoNome(famigliareScelto);
						cognome=getFamigliareSceltoCognome(famigliareScelto);
						data_nascita=getFamigliareSceltoDataNascita(famigliareScelto);
					}else {
						nome = datiHelper.extractedTextValueFromDataNodeByKey("richiedente.nome");
						cognome = datiHelper.extractedTextValueFromDataNodeByKey("richiedente.cognome");
						data_nascita = datiHelper.extractedTextValueFromDataNodeByKey("richiedente.dataNascita");
					}
					entity.setNome(nome);
					entity.setCognome(cognome);
					entity.setData_nascita(data_nascita);

					entity.setEmail(datiHelper.extractedTextValueFromDataNodeByKey("richiedente.contatto.email"));
					entity.setCellulare(datiHelper.extractedTextValueFromDataNodeByKey("richiedente.contatto.cellulare"));

					//toSoliidale altra via
					entity.setCategoria(createCategoria(datiHelper.extractedTextValueFromDataNodeByKey("categoria")));

					entity.setCodice_buono(generateCodiceBuono());
					entity.setCodice_fiscale_richiedente(datiHelper.extractedTextValueFromDataNodeByKey("richiedente.codiceFiscale"));
					Integer idExtTaxi = buoniTaxiDAO.insert(entity);
					Integer idRefIstanza = buoniTaxiDAO.insertRefIstanza(response.getIstanza().getIdIstanza(), idExtTaxi);

				}
				else {
					Integer idRefIstanza = buoniTaxiDAO.insertRefIstanza(response.getIstanza().getIdIstanza(), esistente.getId());
				}

			}else {
				String cf = datiHelper.extractedTextValueFromDataNodeByKey("richiedente.codiceFiscale");
				ExtTaxiEntity esistente = null;
				// Salvataggio in ext Taxi dei dati estrate dall'istanza via datiHelper
				ExtTaxiEntity entity = new ExtTaxiEntity();
				entity.setCodice_fiscale(cf);
				entity.setNome(datiHelper.extractedTextValueFromDataNodeByKey("richiedente.nome"));
				entity.setCognome(datiHelper.extractedTextValueFromDataNodeByKey("richiedente.cognome"));
				entity.setData_nascita(datiHelper.extractedTextValueFromDataNodeByKey("richiedente.dataNascita"));
				entity.setEmail(datiHelper.extractedTextValueFromDataNodeByKey("richiedente.contatto.email"));
				entity.setCellulare(datiHelper.extractedTextValueFromDataNodeByKey("richiedente.contatto.cellulare"));
				entity.setCategoria("toSolidale");
				entity.setCodice_buono(generateCodiceBuono());
				entity.setCodice_fiscale_richiedente(datiHelper.extractedTextValueFromDataNodeByKey("richiedente.codiceFiscale"));
				entity.setIdfamiglia(datiHelper.extractedTextValueFromDataNodeByKey("idFamiglia"));
				LOG.info("ATTENZIONE: HELPER FAMIGLIA: "+datiHelper.extractedTextValueFromDataNodeByKey("idFamiglia"));
				LOG.info("ATTENZIONE: ENTITY FAMIGLIA: " + entity.getIdfamiglia());

				ArrayList<Integer> idExtTaxiIntegers=new ArrayList<>();
				esistente = buoniTaxiDAO.findByCf(cf);
				if(esistente==null) {
					Integer idExtTaxi = buoniTaxiDAO.insert(entity);
					idExtTaxiIntegers.add(idExtTaxi);
					Integer idRefIstanza = buoniTaxiDAO.insertRefIstanza(response.getIstanza().getIdIstanza(), idExtTaxi);
				}
				else {
					Integer idRefIstanza = buoniTaxiDAO.insertRefIstanza(response.getIstanza().getIdIstanza(), esistente.getId());
				}
				ArrayList<HashMap<String,String>> famigliari = getFamigliari(data);
				for (HashMap<String, String> famigliare : famigliari) {
					String cfFamigliare =famigliare.get("cf");
					entity.setCodice_fiscale(cfFamigliare);
					entity.setNome(famigliare.get("nome"));
					entity.setCognome(famigliare.get("cognome"));
					entity.setData_nascita(famigliare.get("data"));
					entity.setCodice_buono(generateCodiceBuono());
					entity.setIdfamiglia(datiHelper.extractedTextValueFromDataNodeByKey("idFamiglia"));
					esistente = buoniTaxiDAO.findByCf(cfFamigliare);
					if(esistente==null) {
						Integer idExtTaxi = buoniTaxiDAO.insert(entity);
						idExtTaxiIntegers.add(idExtTaxi);
						Integer idRefIstanza = buoniTaxiDAO.insertRefIstanza(response.getIstanza().getIdIstanza(), idExtTaxi);
					}
					else {
						Integer idRefIstanza = buoniTaxiDAO.insertRefIstanza(response.getIstanza().getIdIstanza(), esistente.getId());
					}
				}


			}
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::insertExtTaxi] Exception"+e.getMessage());
			throw new BusinessException();
		}
	}

	private ArrayList<HashMap<String,String>> getFamigliari(JsonNode data) {
		ArrayList<HashMap<String,String>> lista = new ArrayList<>();
		if (data.has("listaFamigliari") && data.get("listaFamigliari") !=null && data.get("listaFamigliari").isArray()) {
			ArrayNode  nucleo = (ArrayNode)data.get("listaFamigliari");
			Iterator<JsonNode>it = nucleo.iterator();
			if(it!=null) {
				while (it.hasNext()) {
					HashMap<String,String> datiFamigliare = new HashMap<>();
					JsonNode componente =it.next() ;
					datiFamigliare.put("cf", componente.get("cf").asText());
					datiFamigliare.put("cognome", componente.get("cognome").asText());
					datiFamigliare.put("nome", componente.get("nome").asText());
					datiFamigliare.put("data", componente.get("data").asText());
					lista.add(datiFamigliare);
				}
			}
		}
		return lista;

	}

	private String createCategoria(String extractedTextValueFromDataNodeByKey) {
		String cat =""; 
		switch (extractedTextValueFromDataNodeByKey) {
			case "personeAffetteDaCecitaAssolutaODaDisabilitaMotoriaPermanente":
				cat = "CecitaODisabilita";
				break;
			case "personeConInvaliditaRiconosciutaPariOSuperioreAl65":
				cat = "Invalidita65";
				break;
			case "personeCheHannoCompiutoI75AnniDiEta":
				cat = "75Anni";
				break;
			default:
				cat = "";
				break;
		}
		return	cat;
	}

	private String generateCodiceBuono() {
		String alpha="ABCDFGHJKLMNPQRSTUVWXYZ";
		String number="23456789";
		String codice="";
		boolean presente=true;
		while(presente) {
			for (int i=0; i<2; i++) {
				codice+=alpha.charAt(getRandom().nextInt(alpha.length()));
			}
			for (int i=0; i<6; i++) {
				codice+=number.charAt(getRandom().nextInt(number.length()));
			}
			List<ExtTaxiEntity> ete = buoniTaxiDAO.findByCodiceBuono(codice);
			if(ete==null || ete.isEmpty())
				presente=false;
		}
		return codice;
	}

	private String getFamigliareSceltoDataNascita(String famigliareScelto) {
		String data_nascita = "";
		data_nascita=getValFromString(famigliareScelto, "Data di nascita: ", " - Codice fiscale:");
		return data_nascita;
	}

	private String getFamigliareSceltoCognome(String famigliareScelto) {
		String cognome = "";
		cognome=getValFromString(famigliareScelto, "Cognome: ", " - Data di nascita:");
		return cognome;
	}

	private String getFamigliareSceltoNome(String famigliareScelto) {
		String nome = "";
		nome=getValFromString(famigliareScelto, "Nome: ", " - Cognome:");
		return nome;
	}

	private String getFamigliareSceltoCf(String famigliareScelto) {
		String cf = "";
		cf=getValFromString(famigliareScelto, "Codice fiscale: ", "");
		return cf;
	}

	private String getValFromString(String famigliareScelto, String start, String end) {
		String result="";
		if(end.isBlank())
			result=famigliareScelto.substring(
					famigliareScelto.lastIndexOf(start) + start.length()
					);
		else
			result =famigliareScelto.substring(
					famigliareScelto.lastIndexOf(start) + start.length(), 
					famigliareScelto.lastIndexOf(end)
					);

		return result;
	}


}
