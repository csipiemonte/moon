/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.moon.commons.dto.CampoModulo;
import it.csi.moon.commons.dto.CreaIuvResponse;
import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitBLParams;
import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.Pagamento;
import it.csi.moon.commons.dto.ResponsePaginated;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.EpayRichiestaEntity;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.entity.WorkflowEntity;
import it.csi.moon.commons.mapper.IstanzaMapper;
import it.csi.moon.commons.mapper.IstanzaMapperApi;
import it.csi.moon.commons.mapper.PagamentoMapper;
import it.csi.moon.commons.mapper.PagamentoNotificaMapper;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.commons.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.commons.util.decodifica.DecodificaTipoModificaDati;
import it.csi.moon.moonfobl.business.service.AllegatiService;
import it.csi.moon.moonfobl.business.service.IstanzeService;
import it.csi.moon.moonfobl.business.service.ModuliService;
import it.csi.moon.moonfobl.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.AzioneDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.CodiceIstanzaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.EpayNotificaPagamentoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaPdfDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonfobl.business.service.impl.istanza.IstanzaServiceDelegate;
import it.csi.moon.moonfobl.business.service.impl.istanza.IstanzaServiceDelegateFactory;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonfobl.util.IstanzaUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * @author Laurent
 * Layer di logica servizi che richiama i DAO
 */
@Component
public class IstanzeServiceImpl implements IstanzeService {

	private static final String CLASS_NAME = "IstanzeServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	StatoDAO statoDAO;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	ModuliService moduliService;
	@Autowired
	AllegatiService allegatiService;
	@Autowired
	AllegatoDAO allegatoDAO;
	@Autowired
	ModuloProgressivoDAO moduloProgressivoDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	@Qualifier("nocache")
	ModuloAttributiDAO moduloAttributiDAO;
	@Autowired
	CodiceIstanzaDAO codiceIstanzaDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	AzioneDAO azioneDAO;
	@Autowired
	WorkflowDAO workflowDAO;
	@Autowired
	IstanzaPdfDAO istanzaPdfDAO;
	@Autowired
	EpayRichiestaDAO epayRichiestaDAO;
	@Autowired
	EpayNotificaPagamentoDAO epayNotificaPagamentoDAO;

	@Override
	public Istanza getIstanzaById(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaById] IN idIstanza: " + idIstanza);
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, entity);
			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
			
			String jsonDati = datiE.getDatiIstanza();	
		    jsonDati = IstanzaUtils.modificaHost(jsonDati);				
			datiE.setDatiIstanza(jsonDati);	
			
			StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(), entity.getIdVersioneModulo());
//			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
//			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);

			Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE/* ,
					versioneE*/ /*, attributi*/);
			istanza = completaIstanzaEpay(istanza);
			return istanza;

		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaById] ", e);
			throw e;
		}
	}

	@Override
	public Istanza getIstanzaByCd(UserInfo user, String codiceIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaByCd] IN codiceIstanza: " + codiceIstanza);
			IstanzaEntity entity = istanzaDAO.findByCd(codiceIstanza);
			validateUserAccess(user, entity);
			Long idIstanza = entity.getIdIstanza();
			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
			
			String jsonDati = datiE.getDatiIstanza();	
		    jsonDati = IstanzaUtils.modificaHost(jsonDati);				
			datiE.setDatiIstanza(jsonDati);	
			
			StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(), entity.getIdVersioneModulo());
//			MapModuloAttributi attributi = null;
//			if (!isApi(user)) {
//				List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
//				attributi = new MapModuloAttributi(attributiModuloE);
//			}

			Istanza istanza = isApi(user)?
					IstanzaMapperApi.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE/*, attributi*/):
					IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE/*, attributi*/);
			istanza = completaIstanzaEpay(istanza);
			return istanza;
			
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaByCd] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaByCd] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per cd");
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaByCd] ", be);
			throw be;
		}
	}
	
	@Override
	public Long getIdIstanzaByCd(UserInfo user, String codiceIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIdIstanzaByCd] IN codiceIstanza: " + codiceIstanza);
			IstanzaEntity entity = istanzaDAO.findByCd(codiceIstanza);
			validateUserAccess(user, entity);
			Long idIstanza = entity.getIdIstanza();		
			return idIstanza;			
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIdIstanzaByCd] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getIdIstanzaByCd] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per cd");
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIdIstanzaByCd] ", be);
			throw be;
		}
	}


	private boolean isApi(UserInfo user) {
		return user.getIdFruitore()!=null && user.isApi();
	}
	
	private Istanza completaIstanzaEpay(Istanza istanza) {
		try {
			List<EpayRichiestaEntity> richiesteE = epayRichiestaDAO.findByIdIstanza(istanza.getIdIstanza());
			if (richiesteE != null && richiesteE.size()>0) {
				EpayRichiestaEntity richiesta = richiesteE.get(richiesteE.size()-1);
				istanza.setCodiceAvviso(richiesta.getCodiceAvviso());
				istanza.setIuv(richiesta.getIuv());
				istanza.setDataEsitoPagamento(richiesta.getDataNotificaPagamento());
				istanza.setPagamenti(richiesteE.stream()
					.map( r -> {
						Pagamento res = PagamentoMapper.buildFromEpayRichiestaEntity(r);
						return completaPagamentoNotifica(res, r.getIdNotificaPagamento());
					})
					.collect(Collectors.toList()));
			}
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::completaIstanzaEpay] Exception but continue istanza:" + istanza.getIdIstanza(), e);
		}
		return istanza;
	}

	Pagamento completaPagamentoNotifica(Pagamento pagamento, Long idNotifica) {
		if (idNotifica == null)
			return pagamento;
		pagamento.setNotifica(PagamentoNotificaMapper.buildFromEntity(epayNotificaPagamentoDAO.findById(idNotifica)));
		return pagamento;
	}
	
	@Override
	public void validateUserAccess(UserInfo user, IstanzaEntity entity) throws BusinessException {
		boolean validate = user.getIdentificativoUtente().equals(entity.getIdentificativoUtente()) || 
				(user.getGruppoOperatoreFo() != null && user.getGruppoOperatoreFo().equals(entity.getGruppoOperatoreFo()));
		LOG.debug("[" + CLASS_NAME + "::validateUserAccess] user:" + user.getIdentificativoUtente() + "  entity:"
				+ entity.getIdentificativoUtente() + "  validate:" + validate);
		if (!validate) {
			throw new BusinessException("Data Istanze Security Access","MOONFOBL-10011");
		}
	}

	@Override
	public List<Istanza> getElencoIstanze(IstanzeFilter filter, Optional<IstanzeSorter> sorter)
			throws BusinessException {
		long start = System.currentTimeMillis();
		List<Istanza> elencoIstanze = new ArrayList<>();
		try {
			LOG.debug("[" + CLASS_NAME + "::getElencoIstanze] BEGIN");
			List<IstanzaEntity> elenco = istanzaDAO.find(filter, null, sorter);
			if (elenco != null && elenco.size() > 0) {
				statoDAO.initCache();
				moduloDAO.initCache();
				moduloAttributiDAO.initCache();
				for (IstanzaEntity entity : elenco) {
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),entity.getIdVersioneModulo());
//					List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO
//							.findByIdModulo(entity.getIdModulo());
//					MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);

					IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());
					// se l'istanza non e' in bozza cerco la data invio
					Date dataInvio = null;
					if (entity.getIdStatoWf().intValue() != 1) {
						IstanzaCronologiaStatiEntity datiInvio = istanzaDAO.findInvio(entity.getIdIstanza());
						if (datiInvio != null) {
							dataInvio = datiInvio.getDataInizio();
						}
					}
					Istanza istanza = IstanzaMapper.buildFromIstanzaEntityWithComuneDtInvio(entity, statoE, moduloE/*,
							attributi*/, null, dataInvio);

					elencoIstanze.add(istanza);
				}
			}
			return elencoIstanze;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoIstanze] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco istanza");
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start);
			LOG.debug("[" + CLASS_NAME + "::getElencoIstanze] END in " + sec + " milliseconds.");
		}
	}


	@Override
	public Istanza getInitIstanza(UserInfo user, Long idModulo, Long idVersioneModulo, IstanzaInitBLParams params, HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN idModulo: "+idModulo);
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN idVersioneModulo: "+idVersioneModulo);
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(idModulo, idVersioneModulo);
			validaModuloVersionatoPerCompilazione(moduloE);
			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
			IstanzaServiceDelegate istanzaServiceDelegate = new IstanzaServiceDelegateFactory().getDelegate(moduloE.getCodiceModulo());
			return istanzaServiceDelegate.getInitIstanza(user, moduloE, attributi, params, httpRequest);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore Init istanza per id");
		}
	}

	private void validaModuloVersionatoPerCompilazione(ModuloVersionatoEntity moduloE) throws BusinessException {
		if (!(DecodificaStatoModulo.byId(moduloE.getIdStato())).isCompilabile()) {
			LOG.error("[" + CLASS_NAME + "::validaModuloVersionatoForInit] Errore Modulo non piu compilabile "+moduloE.getIdModulo()+"/"+moduloE.getIdVersioneModulo());
			throw new BusinessException("Modulo non piu compilabile.","MOONFOBL-10010");
		}
	}

	@Override
	@Transactional
	public IstanzaSaveResponse saveIstanza(UserInfo user, Istanza istanza) throws BusinessException {
		return _saveIstanza(user, istanza);
	}

	@Override
	@Transactional
	public IstanzaSaveResponse saveIstanza(UserInfo user, Long idIstanza, Istanza istanza) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::saveIstanza] IN idIstanza: " + idIstanza);
		LOG.debug("[" + CLASS_NAME + "::saveIstanza] IN istanza: " + istanza);
		if (idIstanza == null || !idIstanza.equals(istanza.getIdIstanza())) {
			LOG.error("[" + CLASS_NAME + "::saveIstanza] idIstanza: " + idIstanza
					+ "  istanza.getIdIstanza():" + istanza.getIdIstanza());
			throw new BusinessException("Istanze Validation : idIstanza non corrispondente");
		}
		return _saveIstanza(user, istanza);
	}

	private IstanzaSaveResponse _saveIstanza(UserInfo user, Istanza istanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::saveIstanza] IN istanza: "+istanza);
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(istanza.getModulo().getIdModulo(), istanza.getModulo().getIdVersioneModulo());
			validaModuloVersionatoPerCompilazione(moduloE);
			//
			return _salvaIstanza(user, istanza, moduloE.getCodiceModulo());
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::saveIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore Init istanza per id");
		}
	}

	private IstanzaSaveResponse _salvaIstanza(UserInfo user, Istanza istanza, String codiceModulo) {
		IstanzaServiceDelegate istanzaServiceDelegate = new IstanzaServiceDelegateFactory().getDelegate(codiceModulo);
		return istanzaServiceDelegate.saveIstanza(user, istanza);
	}
	
	@Override
	@Transactional
	public Istanza deleteIstanza(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteIstanza] IN idIstanza: " + idIstanza);
			Date now = new Date();

			// Recupero l'istanza anche per verificare l'esistenza e l'accessibilita
			IstanzaEntity eIstanza = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, eIstanza);

			// ISTANZA
			eIstanza.setIdentificativoUtente(user.getIdentificativoUtente());
			eIstanza.setAttoreUpd(user.getCodFiscDichIstanza());
			eIstanza.setDataCreazione(now);
			eIstanza.setFlagEliminata("S");
			eIstanza.setIdStatoWf(DecodificaStatoIstanza.ELIMINATA.getIdStatoWf());
			eIstanza.setHashUnivocita(null);
			istanzaDAO.update(eIstanza);

			// CRON
			// LAST CRON
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(idIstanza);
			lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
			lastIstanzaCronologia.setDataFine(now);
			istanzaDAO.updateCronologia(lastIstanzaCronologia);

			// NEW CRON
			IstanzaCronologiaStatiEntity newIstanzaCronologia = new IstanzaCronologiaStatiEntity();
			newIstanzaCronologia.setIdIstanza(idIstanza);
			newIstanzaCronologia.setIdStatoWf(DecodificaStatoIstanza.ELIMINATA.getIdStatoWf());
			newIstanzaCronologia.setAttoreIns(user.getIdentificativoUtente());
			newIstanzaCronologia.setDataInizio(now);
			newIstanzaCronologia.setIdAzioneSvolta(DecodificaAzione.ELIMINA.getIdAzione());
			Long idCronologiaStati = istanzaDAO.insertCronologia(newIstanzaCronologia);
			newIstanzaCronologia.setIdCronologiaStati(idCronologiaStati);

			// DATI (essendo un cambio stato ricopio i dati precedente con la nuova cronologia)
			IstanzaDatiEntity eIstanzaDati = istanzaDAO.findDati(eIstanza.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setAttoreUpd(user.getIdentificativoUtente());
			eIstanzaDati.setDataUpd(now);
			eIstanzaDati.setIdCronologiaStati(newIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.INI.getIdTipoModifica());
			Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
			eIstanzaDati.setIdDatiIstanza(idIstanzaDati);

			// RETURN
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, newIstanzaCronologia.getIdCronologiaStati());
			StatoEntity statoE = DecodificaStatoIstanza.ELIMINATA.getStatoEntity();
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(eIstanza.getIdModulo(), eIstanza.getIdVersioneModulo());
//			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
//			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);

			Istanza istanzaDeleted = IstanzaMapper.buildFromIstanzaEntity(eIstanza, newIstanzaCronologia, eIstanzaDati,
					statoE, moduloE/*, attributi*/);

			return istanzaDeleted;

		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanza] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanza] ", e);
			throw e;
		}
	}
	
	
	@Override
	@Transactional
	public Istanza deleteIstanzaForApi(UserInfo user, String codiceIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteIstanzaForApi] IN codiceIstanza: " + codiceIstanza);
			Date now = new Date();

			// Recupero l'istanza anche per verificare l'esistenza e l'accessibilita
			IstanzaEntity eIstanza = istanzaDAO.findByCd(codiceIstanza);
			validateUserAccess(user, eIstanza);

			// ISTANZA
			eIstanza.setIdentificativoUtente(user.getIdentificativoUtente());
			eIstanza.setAttoreUpd(user.getCodFiscDichIstanza());
			eIstanza.setDataCreazione(now);
			eIstanza.setFlagEliminata("S");
			eIstanza.setIdStatoWf(DecodificaStatoIstanza.ELIMINATA.getIdStatoWf());
			
			eIstanza.setHashUnivocita(null);
			
			istanzaDAO.update(eIstanza);

			// CRON
			// LAST CRON
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(eIstanza.getIdIstanza());
			lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
			lastIstanzaCronologia.setDataFine(now);
			istanzaDAO.updateCronologia(lastIstanzaCronologia);

			// NEW CRON
			IstanzaCronologiaStatiEntity newIstanzaCronologia = null;
			newIstanzaCronologia = new IstanzaCronologiaStatiEntity();
			newIstanzaCronologia.setIdIstanza(eIstanza.getIdIstanza());
			newIstanzaCronologia.setIdStatoWf(DecodificaStatoIstanza.ELIMINATA.getIdStatoWf());
			newIstanzaCronologia.setAttoreIns(user.getIdentificativoUtente());
			newIstanzaCronologia.setDataInizio(now);
			newIstanzaCronologia.setIdAzioneSvolta(DecodificaAzione.ELIMINA.getIdAzione());
			Long idCronologiaStati = istanzaDAO.insertCronologia(newIstanzaCronologia);
			newIstanzaCronologia.setIdCronologiaStati(idCronologiaStati);

			// DATI (essendo un cambio stato ricopio i dati precedente con la nuova cronologia)
			IstanzaDatiEntity eIstanzaDati = istanzaDAO.findDati(eIstanza.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setAttoreUpd(user.getIdentificativoUtente());
			eIstanzaDati.setDataUpd(now);
			eIstanzaDati.setIdCronologiaStati(newIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.INI.getIdTipoModifica());
			Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
			eIstanzaDati.setIdDatiIstanza(idIstanzaDati);

			// RETURN
			IstanzaDatiEntity datiE = istanzaDAO.findDati(eIstanza.getIdIstanza(), newIstanzaCronologia.getIdCronologiaStati());
			StatoEntity statoE = DecodificaStatoIstanza.ELIMINATA.getStatoEntity();
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(eIstanza.getIdModulo(), eIstanza.getIdVersioneModulo());
//			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
//			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
			
			MapModuloAttributi attributi = null;

//			Istanza istanzaDeleted = IstanzaMapper.buildFromIstanzaEntity(eIstanza, newIstanzaCronologia, eIstanzaDati,
//					statoE, moduloE, attributi);
			
//			if (!isApi(user)) {
//				List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
//				attributi = new MapModuloAttributi(attributiModuloE);
//			}

			Istanza istanzaDeleted = isApi(user)?
					IstanzaMapperApi.buildFromIstanzaEntity(eIstanza, newIstanzaCronologia, datiE, statoE, moduloE/*, attributi*/):
					IstanzaMapper.buildFromIstanzaEntity(eIstanza, newIstanzaCronologia, datiE, statoE, moduloE/*, attributi*/);			

			return istanzaDeleted;

		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanzaForApi] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanzaForApi] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanzaForApi] ", e);
			throw e;
		}
	}	

	/**
	 * Permette l'update partial di un intanza su alcuni campi : - istanza
	 */
	@Override
	public Istanza patchIstanza(UserInfo user, Long idIstanza, Istanza partialIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::patchIstanza] IN idIstanza: " + idIstanza);
			LOG.debug("[" + CLASS_NAME + "::patchIstanza] IN partialIstanza: " + partialIstanza);
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, entity);

//			Date now = new Date();

			if (partialIstanza.getImportanza() != null) {
				entity.setImportanza(partialIstanza.getImportanza());
			}

			// Salva
			istanzaDAO.update(entity);

			// Rilegge l istanza
			Istanza istanza = getIstanzaById(user, idIstanza);
			return istanza;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::patchIstanzaById] Errore servizio patchIstanzaById", e);
			throw new BusinessException("Errore servizio aggiorna partial Istanza");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::patchIstanzaById] Errore generico servizio patchIstanzaById", ex);
			throw new BusinessException("Errore generico aggiorna partial Istanza");
		}
	}

	@Override
	@Transactional
	public Istanza riportaInBozza(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::riportaInBozza] IN idIstanza: " + idIstanza);
			Date now = new Date();
			IstanzaEntity eIstanza = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, eIstanza);
			// Valida se esiste workflowE dell'azione richiesta per il processo / istanza
			Long idProcesso = moduloDAO.findIdProcesso(eIstanza.getIdModulo());
			WorkflowEntity workflow = workflowDAO.findByProcessoAzione(idProcesso, eIstanza.getIdStatoWf(), DecodificaAzione.RIPORTA_IN_BOZZA.getIdAzione());

			// 1-BOZZA
			Integer idStatoWfPartenza = eIstanza.getIdStatoWf();
			Integer idStatoWfBozza = DecodificaStatoIstanza.BOZZA.getIdStatoWf();

			// ISTANZE
			eIstanza.setAttoreUpd(user.getIdentificativoUtente());
			eIstanza.setIdStatoWf(idStatoWfBozza);
			eIstanza.setCurrentStep(0);
			eIstanza.setHashUnivocita(null);
			istanzaDAO.update(eIstanza);

			// CRON
			// last CRON
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(eIstanza.getIdIstanza());
			lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
			lastIstanzaCronologia.setDataFine(now);
			istanzaDAO.updateCronologia(lastIstanzaCronologia);
			// new CRON
			IstanzaCronologiaStatiEntity eIstanzaCronologia = new IstanzaCronologiaStatiEntity();
			eIstanzaCronologia.setIdIstanza(eIstanza.getIdIstanza());
			eIstanzaCronologia.setIdStatoWf(idStatoWfBozza);
			eIstanzaCronologia.setAttoreIns(user.getIdentificativoUtente());
			eIstanzaCronologia.setDataInizio(now);
			azioneDAO.initCache();
			eIstanzaCronologia.setIdAzioneSvolta(azioneDAO.findIdByCd(DecodificaAzione.RIPORTA_IN_BOZZA.getCodice()));
			Long idCronologiaStati = istanzaDAO.insertCronologia(eIstanzaCronologia);
			eIstanzaCronologia.setIdCronologiaStati(idCronologiaStati);

			// DATI
			IstanzaDatiEntity eIstanzaDati = null;
			eIstanzaDati = istanzaDAO.findDati(eIstanza.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setDataUpd(now);
			DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
			eIstanzaDati.setDatiIstanza(datiIstanzaHelper.updateSubmitFalse(eIstanzaDati.getDatiIstanza())); // RIPORTA IN BAZZA
			eIstanzaDati.setIdCronologiaStati(eIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setIdStepCompilazione(null);
			eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.INI.getIdTipoModifica());
			Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
			eIstanzaDati.setIdDatiIstanza(idIstanzaDati);

			// STORICO WORKFLOW
			String descDestinatario = "compilatore";
			StoricoWorkflowEntity eStoricoWf = new StoricoWorkflowEntity(null, eIstanza.getIdIstanza(), idProcesso,
				DecodificaStatoIstanza.byIdStatoWf(idStatoWfPartenza), DecodificaStatoIstanza.BOZZA, DecodificaAzione.RIPORTA_IN_BOZZA,
				descDestinatario, now, user.getIdentificativoUtente());
			storicoWorkflowDAO.updateDataFine(now, eIstanza.getIdIstanza());
			Long idStoricoWf = storicoWorkflowDAO.insert(eStoricoWf);
			eStoricoWf.setIdStoricoWorkflow(idStoricoWf);

			// Elimino PDF se presente
			istanzaPdfDAO.deleteByIdIstanza(idIstanza);
			
			// Rilegge l istanza
			Istanza istanza = getIstanzaById(user, idIstanza);
			return istanza;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::riportaInBozza] Errore servizio riportaInBozza", e);
			throw new BusinessException("Errore servizio riportaInBozza");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::riportaInBozza] Errore generico servizio riportaInBozza", ex);
			throw new BusinessException("Errore generico riportaInBozza");
		}
	}

	
	@Override
	@Transactional
	public IstanzaSaveResponse invia(UserInfo user, Long idIstanza, String ipAddress) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::invia] IN user: "+user);
				LOG.debug("[" + CLASS_NAME + "::invia] IN idIstanza: "+idIstanza);
			}
			IstanzaEntity istanzaE = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, istanzaE);
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(istanzaE.getIdModulo(), istanzaE.getIdVersioneModulo());
			validaModuloVersionatoPerCompilazione(moduloE);
			//
			IstanzaServiceDelegate istanzaServiceDelegate = new IstanzaServiceDelegateFactory().getDelegate(moduloE.getCodiceModulo());
			return istanzaServiceDelegate.invia(user, istanzaE, moduloE, ipAddress);
			// Result
//			return getIstanzaById(user, idIstanza);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::invia] Errore invocazione DAO", e);
			throw new BusinessException("Errore Invia istanza");
		}	
	}
	

	
	private IstanzaSaveResponse compieAzioneGestisciPagamento(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::compieAzioneGestisciPagamento] IN user: "+user);
				LOG.debug("[" + CLASS_NAME + "::compieAzioneGestisciPagamento] IN idIstanza: "+idIstanza);
			}
			Istanza istanza = getIstanzaById(user, idIstanza);
			//
			IstanzaServiceDelegate istanzaServiceDelegate = new IstanzaServiceDelegateFactory().getDelegate(istanza.getModulo().getCodiceModulo()); //.getCodiceModulo());
			return istanzaServiceDelegate.gestisciPagamento(user, istanza);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::compieAzioneGestisciPagamento] Errore invocazione DAO", e);
			throw new BusinessException("Errore Invia istanza");
		}	
	}
	
	private IstanzaSaveResponse compieAzionePagaOnline(UserInfo user, Long idIstanza) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::compieAzionePagaOnline] IN user: "+user);
			LOG.debug("[" + CLASS_NAME + "::compieAzionePagaOnline] IN idIstanza: "+idIstanza);
		}
		Istanza istanza = getIstanzaById(user, idIstanza);
		//
		IstanzaServiceDelegate istanzaServiceDelegate = new IstanzaServiceDelegateFactory().getDelegate(istanza.getModulo().getCodiceModulo()); //.getCodiceModulo());
		return istanzaServiceDelegate.pagaOnline(user, istanza);
	}

	private IstanzaSaveResponse compieAzionePagaSportello(UserInfo user, Long idIstanza) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::compieAzionePagaSportello] IN user: "+user);
			LOG.debug("[" + CLASS_NAME + "::compieAzionePagaSportello] IN idIstanza: "+idIstanza);
		}
		Istanza istanza = getIstanzaById(user, idIstanza);
		//
		IstanzaServiceDelegate istanzaServiceDelegate = new IstanzaServiceDelegateFactory().getDelegate(istanza.getModulo().getCodiceModulo()); //.getCodiceModulo());
		return istanzaServiceDelegate.pagaSportello(user, istanza);
	}
	
	private IstanzaSaveResponse compieAzioneAnnulla(UserInfo user, Long idIstanza) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::compieAzioneAnnulla] IN user: "+user);
			LOG.debug("[" + CLASS_NAME + "::compieAzioneAnnulla] IN idIstanza: "+idIstanza);
		}
		Istanza istanza = getIstanzaById(user, idIstanza);
		//
		IstanzaServiceDelegate istanzaServiceDelegate = new IstanzaServiceDelegateFactory().getDelegate(istanza.getModulo().getCodiceModulo()); //.getCodiceModulo());
		return istanzaServiceDelegate.annulla(user, istanza);
	}
	
	@Override
	@Transactional
	public IstanzaSaveResponse compieAzione(UserInfo user, Long idIstanza, Long idAzione, String ipAddress) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::compieAzione] IN idIstanza: "+idIstanza+"  idAzione:"+idAzione);
			IstanzaEntity istanzaE = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, istanzaE);
			IstanzaSaveResponse result = null;
			switch (DecodificaAzione.byIdAzione(idAzione)) {
				case INVIA:
					result = invia(user, idIstanza, ipAddress);
					break;
				case RIPORTA_IN_BOZZA:
					Istanza istanzaInBozza = riportaInBozza(user, idIstanza);
					result = new IstanzaSaveResponse();
					result.setIstanza(istanzaInBozza);
					result.setCodice("SUCCESS");
					result.setDescrizione("Istanza riportata in bozza"); // "Istanza inviata correttamente.");
					break;
				case GESTISCI_PAGAMENTO:
					result = compieAzioneGestisciPagamento(user, idIstanza);
					break;
				case PAGA_ONLINE:
					result = compieAzionePagaOnline(user, idIstanza);
					break;
				case PAGA_SPORTELLO:
					result = compieAzionePagaSportello(user, idIstanza);
					break;
				case ANNULLA:
					result = compieAzioneAnnulla(user, idIstanza);
					break;
				default:
					LOG.error("[" + CLASS_NAME + "::compieAzione] NOT IMPLEMENTED idAzione:"+idAzione+" on idIstanza:"+idIstanza);
					throw new BusinessException("AZIONE NOT IMPLEMENTED");
			}
			return result;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] Errore invocazione DAO", e);
			throw new BusinessException("Errore compieAzione");
		}	
	}


/*
	@Override
	@Transactional
	public Istanza invia(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::invia] IN idIstanza: " + idIstanza);
			Date now = new Date();
			IstanzaEntity eIstanza = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, eIstanza);
			validaInvia(eIstanza); // TODO la validazione è da fare sul modulo moduloDAO via attributi

			// 10-COMPLETATA 2-INVIATA
			Integer idStatoWfBozza = DecodificaStatoIstanza.INVIATA.getIdStatoWf();

			// ISTANZE
			eIstanza.setAttoreUpd(user.getCodFisc());
			eIstanza.setDataProtocollo(null);
			eIstanza.setNumeroProtocollo(null);
			eIstanza.setIdStatoWf(idStatoWfBozza);
			eIstanza.setCurrentStep(0);
			istanzaDAO.update(eIstanza);

			// CRON
			IstanzaCronologiaStatiEntity eIstanzaCronologia = null;
			// last CRON
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(eIstanza.getIdIstanza());
			lastIstanzaCronologia.setAttoreUpd(user.getCodFisc());
			lastIstanzaCronologia.setDataFine(now);
			istanzaDAO.updateCronologia(lastIstanzaCronologia);
			// new CRON
			eIstanzaCronologia = new IstanzaCronologiaStatiEntity();
			eIstanzaCronologia.setIdIstanza(eIstanza.getIdIstanza());
			eIstanzaCronologia.setIdStatoWf(idStatoWfBozza);
			eIstanzaCronologia.setAttoreIns(user.getCodFisc());
			eIstanzaCronologia.setDataInizio(now);
			azioneDAO.initCache();
			eIstanzaCronologia.setIdAzioneSvolta(azioneDAO.findIdByCd(DecodificaAzione.INVIA.getCodice()));
			Long idCronologiaStati = istanzaDAO.insertCronologia(eIstanzaCronologia);
			eIstanzaCronologia.setIdCronologiaStati(idCronologiaStati);

			// DATI
			IstanzaDatiEntity eIstanzaDati = null;
			eIstanzaDati = istanzaDAO.findDati(eIstanza.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setDataUpd(now);
			DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
			eIstanzaDati.setDatiIstanza(datiIstanzaHelper.updateSubmitFalse(eIstanzaDati.getDatiIstanza()));
			eIstanzaDati.setIdCronologiaStati(eIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setIdStepCompilazione(null);
			eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.INI.getIdTipoModifica());
			Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
			eIstanzaDati.setIdDatiIstanza(idIstanzaDati);

			// STORICO WORKFLOW
			String descDestinatario = "compilatore";
			Integer idProcesso = moduloDAO.findIdProcesso(eIstanza.getIdModulo());
			StoricoWorkflowEntity eStoricoWf = new StoricoWorkflowEntity(null, eIstanza.getIdIstanza(), idProcesso,
				DecodificaStatoIstanza.COMPLETATA, DecodificaStatoIstanza.INVIATA, DecodificaAzione.INVIA,
				descDestinatario, now);
			Long idStoricoWf = storicoWorkflowDAO.insert(eStoricoWf);
			eStoricoWf.setIdStoricoWorkflow(idStoricoWf);

			// Rilegge l istanza
			Istanza istanza = getIstanzaById(user, idIstanza);
			return istanza;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::invia] Errore servizio invia", e);
			throw new BusinessException("Errore servizio invia");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::invia] Errore generico servizio invia", ex);
			throw new BusinessException("Errore generico invia");
		}
	}

	private void validaInvia(IstanzaEntity entity) throws BusinessException {
		boolean validate = DecodificaStatoIstanza.COMPLETATA.isCorrectStato(entity);
		LOG.debug("[" + CLASS_NAME + "::validaInvia] idIstanza:" + entity.getIdIstanza() + "  validate:" + validate);
		if (!validate) {
			throw new BusinessException("Istanze Non piu possibile inviare");
		}
	}
*/

	@Override
	public ResponsePaginated<Istanza> getElencoIstanzePaginated(IstanzeFilter filter, String filtroRicerca, Optional<IstanzeSorter> optSorter)
			throws BusinessException {
//		List<Istanza> elenco = istanzeService.getElencoIstanze(filter, optSorter);
		List<Istanza> elencoIstanze = new ArrayList<>();
		try {
			Integer totalElements = istanzaDAO.count(filter, filtroRicerca);
			List<IstanzaEntity> elenco = istanzaDAO.find(filter,filtroRicerca, optSorter);
			if (elenco != null && elenco.size() > 0) {
				moduloDAO.initCache();
				moduloAttributiDAO.initCache();
				for (IstanzaEntity entity : elenco) {
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
//					StatoEntity statoE = DecodificaStatoIstanza.byIdStatoWf(entity.getIdStatoWf()).getStatoEntity();
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(), entity.getIdVersioneModulo());
//					MapModuloAttributi attributi = retrieveMapAttributi(entity.getIdModulo());
					elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE/*, attributi*/));
				}
			}
			ResponsePaginated<Istanza> result = new ResponsePaginated<>();
			result.setItems(elencoIstanze);
			result.setPage((int) Math.floor(filter.getOffset().doubleValue() / filter.getLimit().doubleValue())); // 0 based
			result.setPageSize(filter.getLimit());
			result.setTotalElements(totalElements);
			result.setTotalPages(
					((Double) Math.ceil(totalElements.doubleValue() / filter.getLimit().doubleValue())).intValue());
			return result;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoIstanzePaginated] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco istanza paginate");
		}
	}

	protected MapModuloAttributi retrieveMapAttributi(Long idModulo) {
		List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(idModulo);
		try {
			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
			return attributi;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::retrieveMapAttributi] Exception MapModuloAttributi on idModulo = " + idModulo + " with " + attributiModuloE);
			throw new BusinessException("Errore MapModuloAttributi");
		}
	}

	@Override
	public byte[] getPdfIstanza(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			return moonsrvDAO.getPdfByIdIstanza(user, idIstanza);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getPdfIstanza] Errore invocazione DAO per moonsrv ", e);
			throw new BusinessException("Errore generazione pdf per istanza=" + idIstanza);
		}
	}
	
	@Override
	public byte[] getNotificaIstanza(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			return moonsrvDAO.getNotificaByIdIstanza(user, idIstanza);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getNotificaIstanza] Errore invocazione DAO per moonsrv ", e);
			throw new BusinessException("Errore getNotificaIstanza per istanza=" + idIstanza);
		}
	}
	
	@Override
	public byte[] getDocumentoByFormioNameFile(UserInfo user, String formioNameFile) throws BusinessException {
		try {
			return moonsrvDAO.getDocumentoByFormioNameFile(user, formioNameFile);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] Errore invocazione DAO per moonsrv ", e);
			throw new BusinessException("Errore getDocumentoByFormioNameFile per formioNameFile=" + formioNameFile);
		}
	}
	
	@Override
	public byte[] getDocumentoByIdFile(UserInfo user, Long idFile) throws BusinessException {
		try {
			return moonsrvDAO.getDocumentoByIdFile(user, idFile);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] Errore invocazione DAO per moonsrv ", e);
			throw new BusinessException("Errore getDocumentoByIdFile per idFile=" + idFile);
		}
	}
	
	@Override
	public Documento getDocumentoNotificaIstanza(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			return moonsrvDAO.getDocumentoNotificaByIdIstanza(user, idIstanza);
		} catch (DAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getDocumentoNotificaIstanza] Errore invocazione DAO per moonsrv per istanza=" + idIstanza);
			throw new BusinessException(e);
		}
	}

	@Override
	public IstanzaSaveResponse creaIUV(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			CreaIuvResponse creaIuvResponse = moonsrvDAO.creaIUV(idIstanza);
			if (StringUtils.isBlank(creaIuvResponse.getIuv())) {
				LOG.error("[" + CLASS_NAME + "::creaIUV] Errore invocazione DAO per moonsrv - IUV blank");
				throw new BusinessException("Errore creaIUV - IUV blank per istanza=" + idIstanza);
			}
			IstanzaSaveResponse result = new IstanzaSaveResponse();
			result.setCodice("SUCCESS");
			Istanza istanza = getIstanzaById(user, idIstanza);
			istanza.setCodiceAvviso(creaIuvResponse.getCodiceAvviso());
			istanza.setIuv(creaIuvResponse.getIuv());
			result.setIstanza(istanza);
			result.setUrlRedirect(creaIuvResponse.getUrlRedirect());
			return result;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::creaIUV] Errore invocazione DAO per moonsrv ", e);
			throw new BusinessException("Impossible richiedere un identificativo univoco di pagamento per l'istanza. Si prega di riprovare più tardi.","MOONFOBL-10810");
		}
	}

	public String inviaRispostaIntegrazioneCosmo(Long idIstanza) throws BusinessException {
		try {
			return moonsrvDAO.inviaRispostaIntegrazioneCosmo(idIstanza);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] Errore invocazione DAO per moonsrv ", e);
			throw new BusinessException("Errore inviaRispostaIntegrazioneCosmo per istanza=" + idIstanza);
		}
	}

	@Override
	@Transactional
	public IstanzaSaveResponse duplica(UserInfo user, Long idIstanza, Boolean duplicaAllegati, String ipAddress) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::duplica]  BEGIN ");
			Boolean versioniDiverse = false;
			Istanza istanzaDaDuplicare = getIstanzaById(user, idIstanza);
			verificaDuplicazione(user,istanzaDaDuplicare);
			
			// TODO gestire versioneModulo
			Long idVersioneModuloPubblicato = getIdVersioneModuloPubblicato(istanzaDaDuplicare.getModulo().getIdModulo());
			if( !idVersioneModuloPubblicato.equals(istanzaDaDuplicare.getModulo().getIdVersioneModulo()) ) {
				versioniDiverse = true;
			}
			istanzaDaDuplicare.getModulo().setIdVersioneModulo(idVersioneModuloPubblicato);

			// TODO GruppoOperatore -> gia lo fa saveIstanza
			
			//pulisce dati_istanza (true=duplica cn allegati)
			istanzaDaDuplicare = gestioneAllegatiEDataIstanzaPerDuplica(istanzaDaDuplicare,idIstanza,duplicaAllegati,versioniDiverse,ipAddress);
			
			//forse nn serve piu (new version formio???)
			//new DatiIstanzaHelper().updateSubmitFalse(eIstanzaDati.getDatiIstanza());
			
			istanzaDaDuplicare.setIdIstanza(null);
			istanzaDaDuplicare.setCodiceIstanza(null);
			istanzaDaDuplicare.setImportanza(0);
			istanzaDaDuplicare.setCurrentStep(0);
			istanzaDaDuplicare.setNumeroProtocollo(null);
			istanzaDaDuplicare.setDataProtocollo(null);
			istanzaDaDuplicare.setDatiAggiuntivi(null);
			istanzaDaDuplicare.getStato().setIdStato(DecodificaStatoIstanza.BOZZA.getIdStatoWf());
			istanzaDaDuplicare.setFlagArchiviata(false);
			istanzaDaDuplicare.setFlagEliminata(false);	
			
			if (Boolean.TRUE.equals(user.isOperatore())) {
				istanzaDaDuplicare.setCodiceFiscaleDichiarante(null);
				istanzaDaDuplicare.setNomeDichiarante(null);
				istanzaDaDuplicare.setCognomeDichiarante(null);				
			}
			
			//istanzaDaDuplicare.setFlagTest(false);
			istanzaDaDuplicare.setAttoreUpd(user.getIdentificativoUtente());					
			return _salvaIstanza(user, istanzaDaDuplicare, istanzaDaDuplicare.getModulo().getCodiceModulo());

		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::duplica] Errore invocazione DAO  ", e);
			throw new BusinessException("Errore duplica istanza ","MOONFOBL-100200");
		}
	}
	
	
	@Override
	@Transactional
	public Istanza duplicaForApi(UserInfo user, String codiceIstanza, Boolean duplicaAllegati) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::duplicaForApi]  BEGIN ");
			Boolean versioniDiverse = false;
			IstanzaEntity entity = istanzaDAO.findByCd(codiceIstanza);
			Istanza istanzaDaDuplicare = getIstanzaById(user, entity.getIdIstanza());
			verificaDuplicazione(user,istanzaDaDuplicare);
			
			// TODO gestire versioneModulo
			Long idVersioneModuloPubblicato = getIdVersioneModuloPubblicato(istanzaDaDuplicare.getModulo().getIdModulo());
			if( !idVersioneModuloPubblicato.equals(istanzaDaDuplicare.getModulo().getIdVersioneModulo()) ) {
				versioniDiverse = true;
			}
			istanzaDaDuplicare.getModulo().setIdVersioneModulo(idVersioneModuloPubblicato);

			// TODO GruppoOperatore -> gia lo fa saveIstanza
			
			//pulisce dati_istanza (true=duplica cn allegati)
			istanzaDaDuplicare = gestioneAllegatiEDataIstanzaPerDuplica(istanzaDaDuplicare,istanzaDaDuplicare.getIdIstanza(),duplicaAllegati,versioniDiverse, null);
			
			//forse nn serve piu (new version formio???)
			//new DatiIstanzaHelper().updateSubmitFalse(eIstanzaDati.getDatiIstanza());
			
			istanzaDaDuplicare.setIdIstanza(null);
			istanzaDaDuplicare.setCodiceIstanza(null);
			istanzaDaDuplicare.setImportanza(0);
			istanzaDaDuplicare.setCurrentStep(0);
			istanzaDaDuplicare.setNumeroProtocollo(null);
			istanzaDaDuplicare.setDataProtocollo(null);
			istanzaDaDuplicare.setDatiAggiuntivi(null);
			istanzaDaDuplicare.getStato().setIdStato(DecodificaStatoIstanza.BOZZA.getIdStatoWf());
			istanzaDaDuplicare.setFlagArchiviata(false);
			istanzaDaDuplicare.setFlagEliminata(false);	
			
			if (Boolean.TRUE.equals(user.isOperatore())) {
				istanzaDaDuplicare.setCodiceFiscaleDichiarante(null);
				istanzaDaDuplicare.setNomeDichiarante(null);
				istanzaDaDuplicare.setCognomeDichiarante(null);				
			}
			
			//istanzaDaDuplicare.setFlagTest(false);
			istanzaDaDuplicare.setAttoreUpd(user.getIdentificativoUtente());	
			
			IstanzaSaveResponse istanzaSaveResponse = _salvaIstanza(user, istanzaDaDuplicare, istanzaDaDuplicare.getModulo().getCodiceModulo());
			Istanza istanzaDuplicata = istanzaSaveResponse.getIstanza();
			
			Istanza istanzaForApi = IstanzaMapperApi.buildFromIstanzaDuplicata(istanzaDuplicata);
			return istanzaForApi;

		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::duplicaForApi] Errore invocazione DAO  ", e);
			throw new BusinessException("Errore duplicaForApi istanza ","MOONFOBL-100200");
		}
	}

	private Istanza gestioneAllegatiEDataIstanzaPerDuplica(Istanza istanzaDaDuplicare, long idIstanza, boolean duplicaConAllegati, boolean versioniDiverse, String ipAddress) {
		try {
			LOG.debug("[" + CLASS_NAME + "::gestioneAllegatiEDataIstanzaPerDuplica]  BEGIN ");
			if(duplicaConAllegati==true) {
				Map<String,String> nuovoNameFormio = allegatiService.duplicaAllegatiOfIstanza(idIstanza, istanzaDaDuplicare.getModulo().getIdModulo(), istanzaDaDuplicare.getModulo().getIdVersioneModulo(), versioniDiverse, ipAddress);
				istanzaDaDuplicare = aggiornaDataIstanzaFile(istanzaDaDuplicare,nuovoNameFormio);
			} else {
				istanzaDaDuplicare = azzeraDataIstanzaFile(istanzaDaDuplicare);
			}
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::gestioneAllegatiEDataIstanzaPerDuplica] ", be);
			throw be;
		}
		return istanzaDaDuplicare;
	}
	
	private Istanza aggiornaDataIstanzaFile(Istanza istanzaDaDuplicare, Map<String,String> nuovoNameFormio) {
		//nota:in caso di versioni diverse i campi presenti nella vecchia (e magari non presenti nella nuova) vers non vengono puliti per scelta implementativa 
		try {
			LOG.debug("[" + CLASS_NAME + "::aggiornaDataIstanzaFile] ");
			String dataIstanza = istanzaDaDuplicare.getData().toString();
			for(String oldName : nuovoNameFormio.keySet()) {
				dataIstanza=dataIstanza.replace(oldName, nuovoNameFormio.get(oldName));
			}
			istanzaDaDuplicare.setData(dataIstanza);
		} catch (Exception be) {
			LOG.error("[" + CLASS_NAME + "::aggiornaDataIstanzaFile] ", be);
			throw new BusinessException("Errore aggiornaDataIstanzaFile ","MOONFOBL-100200");
		}
		return istanzaDaDuplicare;
	}

	private Istanza azzeraDataIstanzaFile(Istanza istanzaDaDuplicare) {
		try {
			List<CampoModulo> campiFile = moonsrvDAO.getCampiModulo(istanzaDaDuplicare.getModulo().getIdModulo(),istanzaDaDuplicare.getModulo().getIdVersioneModulo(),"file");
			if (campiFile == null || campiFile.isEmpty()) {
				return istanzaDaDuplicare;
			}
			istanzaDaDuplicare.setData(new DatiIstanzaHelper().azzeraDataIstanzaFile((String)istanzaDaDuplicare.getData(), campiFile));
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::azzeraDataIstanzaFile] Errore invocazione moonsrvDAO.getCampiModulo.", daoe);
			throw new BusinessException("Errore moonsrvDAO  getCampiModulo","MOONFOBL-10020");
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::azzeraDataIstanzaFile] BusinessException ", be);
			throw be;
		}
		return istanzaDaDuplicare;
	}

	
	private Long getIdVersioneModuloPubblicato(Long idModulo) {
		Long idVers = null;
		try {
			idVers = moduloDAO.findVersionePubblicatoByIdModulo(idModulo).getIdVersioneModulo();
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::getIdVersioneModuloPubblicato] Impossibile duplicare il modulo " + idModulo +"  ", e);
			throw new BusinessException("Errore modulo non trovato o non PUBBLICATO","MOONFOBL-10022");
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getIdVersioneModuloPubblicato] Errore invocazione DAO  ", e);
			throw new BusinessException("Errore moduloDAO  findVersionePubblicatoByIdModulo","MOONFOBL-10020");
		}
		
		return idVers;
	}
	
	private void verificaDuplicazione(UserInfo user, Istanza istanzaDaDuplicare) throws BusinessException {
		
		StatoEntity statoEntity = null;
		try {
			statoEntity = statoDAO.findById( istanzaDaDuplicare.getStato().getIdStato() );			
			if ( DecodificaStatoIstanza.BOZZA.isCorrectStato(statoEntity.getIdStatoWf()) ||
					DecodificaStatoIstanza.COMPLETATA.isCorrectStato(statoEntity.getIdStatoWf()) ||
					DecodificaStatoIstanza.IN_ATTESA_INTEGRAZIONE.isCorrectStato(statoEntity.getIdStatoWf())) {
				LOG.error("[" + CLASS_NAME + "::verificaDuplicazione] Impossibile duplicare l'istanza " + istanzaDaDuplicare.getIdIstanza() +". Deve essere nello stato INVIATA ");
				throw new BusinessException("Impossibile duplicare l'istanza. Deve essere nello stato INVIATA", "MOONFOBL-10021");
			}
			
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::verificaDuplicazione] id_stato_wf non trovato! ");
			throw new BusinessException("Errore duplicazione istanza. id_stato_wf non trovato! ", "MOONFOBL-10020");
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::verificaDuplicazione] Errore invocazione DAO moon_wf_d_stato ");
			throw new BusinessException("Errore statoDAO findById", "MOONFOBL-10020");
		}	
	
	}


}
