/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.moon.commons.dto.AllegatoMessaggioSupporto;
import it.csi.moon.commons.dto.MessaggioSupporto;
import it.csi.moon.commons.dto.RichiestaSupporto;
import it.csi.moon.commons.entity.AllegatoMessaggioSupportoEntity;
import it.csi.moon.commons.entity.MessaggioSupportoEntity;
import it.csi.moon.commons.entity.RichiestaSupportoEntity;
import it.csi.moon.commons.entity.RichiestaSupportoFilter;
import it.csi.moon.commons.mapper.MessaggioSupportoMapper;
import it.csi.moon.commons.mapper.RichiestaSupportoMapper;
import it.csi.moon.commons.util.decodifica.DecodificaProvenienzaMessagioSupporto;
import it.csi.moon.moonfobl.business.service.SupportoService;
import it.csi.moon.moonfobl.business.service.impl.dao.AllegatoMessaggioSupportoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MessaggioSupportoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.RichiestaSupportoDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.util.LoggerAccessor;
/**
 * Metodi di business relativi alle Richieste di Supporto
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 24/08/2020 - versione iniziale
 */
@Component
public class SupportoServiceImpl  implements SupportoService {
	
	private static final String CLASS_NAME = "FaqServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	public SupportoServiceImpl() {
	}

	@Autowired
	RichiestaSupportoDAO richiestaSupportoDAO;
	@Autowired
	MessaggioSupportoDAO messaggioSupportoDAO;
	@Autowired
	AllegatoMessaggioSupportoDAO allegatoMessaggioSupportoDAO;
	
	@Override
	public List<RichiestaSupporto> getElencoRichiestaSupporto(RichiestaSupportoFilter filter) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getElencoRichiestaSupporto] BEGIN IN filter="+filter);
			}
			List<RichiestaSupporto> result = richiestaSupportoDAO.find(filter).stream()
				.map(RichiestaSupportoMapper::buildFromEntity)
				.collect(Collectors.toList());
			for (RichiestaSupporto r : result) {
				r = completaDiMessaggi(r);
			}
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoRichiestaSupporto] Errore generico servizio getElencoFaq",ex);
			throw new ServiceException("Errore generico servizio elenco FAQ");
		}
	}

	@Override
	public RichiestaSupporto getRichiestaSupportoById(Long idRichiestaSupporto) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getRichiestaSupportoById] BEGIN IN idRichiestaSupporto="+idRichiestaSupporto);
		}
		RichiestaSupporto result = null;
		try {
			RichiestaSupportoEntity entity = richiestaSupportoDAO.findById(idRichiestaSupporto);
			result = RichiestaSupportoMapper.buildFromEntity(entity);
			result = completaDiMessaggi(result);
			
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getRichiestaSupportoById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getRichiestaSupportoById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca FAQ per id");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getRichiestaSupportoById] END result="+result);
			}
		}
	}

	private RichiestaSupporto completaDiMessaggi(RichiestaSupporto richiestaSupporto) {
		List<MessaggioSupporto> messaggi = messaggioSupportoDAO.findByIdRichista(richiestaSupporto.getIdRichiestaSupporto()).stream()
				.map(MessaggioSupportoMapper::buildFromEntity)
				.collect(Collectors.toList());
		richiestaSupporto.setMessaggi(messaggi);
		return richiestaSupporto;
	}

	/**
	 * Inserisci un messaggio di supporto, creando la richiesta se necessario
	 * L'oggetto in input deve sempre contenere solo un messagio : il messaggio da inserire
	 * Nal caso di FO (cittadino), puo essere ommesso la valorizzazione di FlagInAttesaDiRisposta e Provenienza
	 */
	@Override
	@Transactional
	public RichiestaSupporto createRichiestaSupporto(RichiestaSupporto richiesta) throws BusinessException {
		RichiestaSupporto result = null;
		RichiestaSupportoEntity richiestaDB = null;
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::createRichiestaSupporto] IN richiesta=" + richiesta);
			}
			Date now = new Date();
			MessaggioSupporto firstMsg = richiesta.getMessaggi().get(0);
			firstMsg.setProvenienza(firstMsg.getProvenienza()!=null?firstMsg.getProvenienza():DecodificaProvenienzaMessagioSupporto.FO.getCodice());
			
			// Richiesta
			insertOrUpdateRichiesta(richiesta, now);
			
			// Messaggio
			insertMessagioAndAllegati(richiesta, now);

			// result
			result  = getRichiestaSupportoById(richiesta.getIdRichiestaSupporto());
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::createRichiestaSupporto] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::createRichiestaSupporto] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore creazione richiesta supporto");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::createRichiestaSupporto] END result="+result);
			}
		}
	}

	private void insertOrUpdateRichiesta(RichiestaSupporto richiesta, Date now) {
		MessaggioSupporto firstMsg = richiesta.getMessaggi().get(0);
		String newflagInAttesaDiRisposta = null;
		RichiestaSupportoEntity richiestaDB;
		
		if (richiesta.getIdRichiestaSupporto()!=null) {
			richiestaDB = richiestaSupportoDAO.findById(richiesta.getIdRichiestaSupporto());
			validaRichiesta(richiesta, richiestaDB);
			if (DecodificaProvenienzaMessagioSupporto.FO.getCodice().equals(firstMsg.getProvenienza()) && "N".equals(richiestaDB.getFlagInAttesaDiRisposta())) {
				newflagInAttesaDiRisposta = "S";
			}
			if (DecodificaProvenienzaMessagioSupporto.BO.getCodice().equals(firstMsg.getProvenienza()) && "S".equals(richiestaDB.getFlagInAttesaDiRisposta())) {
				newflagInAttesaDiRisposta = "N";
			}
			// Aggiorna
			richiestaDB.setFlagInAttesaDiRisposta(newflagInAttesaDiRisposta!=null?newflagInAttesaDiRisposta:richiestaDB.getFlagInAttesaDiRisposta());
			richiestaDB.setDataUpd(now);
			richiestaDB.setAttoreUpd(firstMsg.getAttoreIns());
			richiestaSupportoDAO.update(richiestaDB);
		} else {
			RichiestaSupportoEntity richiestaE = new RichiestaSupportoEntity();
			richiestaE.setIdIstanza(richiesta.getIdIstanza());
			richiestaE.setIdModulo(richiesta.getIdModulo());
			richiestaE.setFlagInAttesaDiRisposta(richiesta.getFlagInAttesaDiRisposta()!=null?richiesta.getFlagInAttesaDiRisposta():"S");
			richiestaE.setDescMittente(richiesta.getDescMittente());
			richiestaE.setEmailMittente(richiesta.getEmailMittente());
			richiestaE.setDataIns(now);
			richiestaE.setAttoreIns(firstMsg.getAttoreIns());
			richiestaE.setDataUpd(now);
			richiestaE.setAttoreUpd(firstMsg.getAttoreIns());
			
			Long idRichiestaSupporto = richiestaSupportoDAO.insert(richiestaE);
			richiestaE.setIdRichiestaSupporto(idRichiestaSupporto);
			richiesta.setIdRichiestaSupporto(idRichiestaSupporto);
		}
	}

	private void validaRichiesta(RichiestaSupporto richiesta, RichiestaSupportoEntity richiestaDB) throws BusinessException {
		if (!richiestaDB.getIdIstanza().equals(richiesta.getIdIstanza()) ||
			!richiestaDB.getIdModulo().equals(richiesta.getIdModulo()) ) {
			throw new BusinessException("Not same Richiesta");
		}
	}

	private void insertMessagioAndAllegati(RichiestaSupporto richiesta, Date now) {
		MessaggioSupporto firstMsg = richiesta.getMessaggi().get(0);
		// Messaggio
		MessaggioSupportoEntity messaggioE = new MessaggioSupportoEntity();
		messaggioE.setIdRichiestaSupporto(richiesta.getIdRichiestaSupporto());
		messaggioE.setProvenienza(firstMsg.getProvenienza());
		messaggioE.setContenuto(firstMsg.getContenuto());
		messaggioE.setDataIns(now);
		messaggioE.setAttoreIns(firstMsg.getAttoreIns());
		
		Long idMessaggioSupporto = messaggioSupportoDAO.insert(messaggioE);
		messaggioE.setIdMessaggioSupporto(idMessaggioSupporto);
		
		// Allegato
		insertAllegatti(firstMsg.getAllegati(), idMessaggioSupporto);
	}
	
	private void insertAllegatti(List<AllegatoMessaggioSupporto> allegati, Long idMessaggioSupporto) {
		if (allegati==null || allegati.size()==0)
			return ;
		for (AllegatoMessaggioSupporto allegato : allegati) {
			AllegatoMessaggioSupportoEntity allegatoE = new AllegatoMessaggioSupportoEntity();
			allegatoE.setIdMessaggioSupporto(idMessaggioSupporto);
			allegatoE.setNome(allegato.getNome());
			allegatoE.setContenuto(allegato.getContenuto());
			allegatoE.setContentType(allegato.getContentType());
			allegatoE.setDimensione(allegato.getDimensione());
			idMessaggioSupporto = allegatoMessaggioSupportoDAO.insert(allegatoE);
		}
	}
	
}