/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.istanza;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.OneriCostrDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrDomandaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaSaveResponse;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper_CONT_COSTR;
import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;
import it.csi.moon.moonbobl.dto.moonfobl.DocumentoRiconoscimento;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.MapModuloAttributi;
import it.csi.moon.moonbobl.util.decodifica.DecodificaAzione;

/**
 * IstanzaDelegate_CONT_COSTR - Specializzazione.
 *  - getInitIstanzaByIdModulo : Bloccare l'accesso al modulo se è già stato superato i fondi del bando di 26M €, controllo effettuato sulla tab ext
 *  - saveUlteriori richiamato da saveIstanza per salvataggio ulteriore in moon_ext_oneri_costr_domanda via oneriCostrDomandaDAO
 * 
 * @author laurent
 *
 */
public class IstanzaDelegate_CONT_COSTR extends IstanzaDefaultDelegate implements IstanzaServiceDelegate {

	private final static String CLASS_NAME = "IstanzaDelegate_CONT_COSTR";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	private static final int CENTS = 100;
	private static final int MILIONI = 1000000;
	
	@Autowired
	OneriCostrDAO oneriCostrDomandaDAO;
	
	public IstanzaDelegate_CONT_COSTR() {
		log.debug("[" + CLASS_NAME + "::" + CLASS_NAME + "]");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	//
	// INIT ISTANZA
	//
	@Override
	public Istanza getInitIstanza(UserInfo user, ModuloVersionatoEntity moduloEntity, MapModuloAttributi attributi, IstanzaInitBLParams params, HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::getInitIstanza] IN moduloEntity: " + moduloEntity);
			
			// TODO validare 1.Stato modulo PUB e 2. accessibile dall'utente

			Long sumPagato = oneriCostrDomandaDAO.sumPagato();
			Long MAX_BANDO_CONT_COSTR = 26L * MILIONI * CENTS;

			if (sumPagato > MAX_BANDO_CONT_COSTR) {
				log.warn("[" + CLASS_NAME + "::getInitIstanza] Disponibilità economica prevista dal bando superata " + sumPagato + " > " + MAX_BANDO_CONT_COSTR);
				throw new BusinessException("Il modulo non è più disponibile in quanto la disponibilità economica prevista dal bando è stata superata.","MOONFOBL-001","AVVISO"); // 
			}

			Istanza istanza = moonsrvDAO.initIstanza(httpRequest.getRemoteAddr(), user, moduloEntity.getIdModulo(), moduloEntity.getIdVersioneModulo(), params);

			return istanza;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getInitIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore Init istanza per id");
		}
	}
	
	//
	// INVI ISTANZA
	//
	@Override
	protected void inviaUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, Long idProcesso) throws BusinessException {
		insertExtOneriCostr(user, response, idProcesso);
	}
//	protected void saveUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, boolean insertMode, boolean completaMode, Long idProcesso) throws BusinessException {
//		if (completaMode) {
//			insertExtOneriCostr(user, docRiconoscimento, response, idProcesso);
//		}
//	}
	

	private void insertExtOneriCostr(UserInfo user, IstanzaSaveResponse response, Long idProcesso) throws DAOException, BusinessException {
		DatiIstanzaHelper_CONT_COSTR oneriCostrHelper = new DatiIstanzaHelper_CONT_COSTR();
		try {
			// Preparazione Entity Ext (count effettuato senza l importo dell'istanza corrente salvata, ma non ancora inserita in ext)
			OneriCostrDomandaEntity entity = oneriCostrHelper.parse(String.valueOf(response.getIstanza().getData()));
			entity.setIdIstanza(response.getIstanza().getIdIstanza());
			entity.setCodiceIstanza(response.getIstanza().getCodiceIstanza());
			entity.setDataIns(new Date());
			
			// Preparazione Resonse
			response.setCodice("SUCCESS");
			response.setDescrizione("La sua istanza verrà verificata dall'ufficio comunale di competenza.");
			response.setIncludeDescrizioneInEmail(Boolean.TRUE);
			oneriCostrDomandaDAO.lock();
			Long sumPagato = oneriCostrDomandaDAO.sumPagato();
			Long MAX_BANDO_CONT_COSTR = 26L * MILIONI * CENTS;
			if (sumPagato > MAX_BANDO_CONT_COSTR) {
				response.setCodice("NO_OVER_LIMIT");
				response.setDescrizione("Attenzione, la sua istanza non può essere accolta in quanto la disponibilità economica prevista dal bando è stata superata.");
				log.warn("[" + CLASS_NAME + "::insertExtOneriCostr] OVER_LIMIT sumPagato="+sumPagato);
				
				cambiaStatoIstanza(user, response.getIstanza(), idProcesso, DecodificaAzione.RESPINGI_OVER_LIMIT); //, DecodificaStatoIstanza.NON_ACCOLTA
			} else {
				Long sumPagatoConPresunto = sumPagato + entity.getPrtImportoPresunto();
				if (sumPagatoConPresunto > MAX_BANDO_CONT_COSTR) {
					response.setCodice("SUCCESS_RESIDUO");
					response.setDescrizione("Attenzione, la sua istanza è stata accolta solo parzialmente in quanto la disponibilità economica prevista dal bando è stata superata.");
					Long residuo = MAX_BANDO_CONT_COSTR - entity.getPrtImportoPresunto();
					entity.setPrtImportoPresunto(residuo);
					entity.setBoImportoPagato(residuo);
					log.warn("[" + CLASS_NAME + "::insertExtOneriCostr] RESIDUO sumPagato="+sumPagato+"  impPresunto="+entity.getPrtImportoPresunto()+"  sumPagatoConPresunto="+sumPagatoConPresunto+"  residuo="+residuo);
				} else {
					// Controllo Univocita CF o PIVA
					Integer countCfPiva = oneriCostrDomandaDAO.countCfPiva(entity);
					if (countCfPiva>0) {
						response.setCodice("NO_ALREADY_SEND");
						response.setDescrizione("Attenzione, la sua istanza non può essere accolta in quanto il beneficiario risulta già beneficiario di un'istanza precedente.");
						log.warn("[" + CLASS_NAME + "::insertExtOneriCostr] ALREADY_SEND countCfPiva=" + countCfPiva + " for " + entity.getBenCodiceFiscale()!=null?entity.getBenCodiceFiscale():entity.getBenPiva());

						cambiaStatoIstanza(user, response.getIstanza(), idProcesso, DecodificaAzione.RESPINGI_DOPPIA); // , DecodificaStatoIstanza.RIFIUTATA
					}
				}
			}

			
			// Salvataggio in ext con la response
			entity.setFoCodiceResponse(response.getCodice());
			if (!response.getCodice().startsWith("SUCCESS")) {
				entity.setBoImportoPagato(0L);
			}
			Long idExtDomanda = oneriCostrDomandaDAO.insert(entity);
			entity.setIdExtDomanda(idExtDomanda);
			

		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::insertExtOneriCostr] Exception"+e.getMessage());
			throw new BusinessException();
		}
	}

	
	
}
