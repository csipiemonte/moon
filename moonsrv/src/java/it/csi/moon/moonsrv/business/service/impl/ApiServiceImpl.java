/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.api.FruitoreAllegato;
import it.csi.moon.commons.dto.api.FruitoreAllegatoAzione;
import it.csi.moon.commons.dto.api.FruitoreDettaglioImporto;
import it.csi.moon.commons.dto.api.FruitoreEnte;
import it.csi.moon.commons.dto.api.FruitoreIstanza;
import it.csi.moon.commons.dto.api.FruitoreIstanzaDettagliata;
import it.csi.moon.commons.dto.api.FruitoreModuloCodiceVersione;
import it.csi.moon.commons.dto.api.FruitoreModuloVersione;
import it.csi.moon.commons.dto.api.FruitorePagamento;
import it.csi.moon.commons.dto.api.FruitoreStato;
import it.csi.moon.commons.dto.api.PostAzione;
import it.csi.moon.commons.dto.api.StatoAcquisizioneRequest;
import it.csi.moon.commons.dto.extra.epay.IUVChiamanteEsternoRequest;
import it.csi.moon.commons.entity.AzioneEntity;
import it.csi.moon.commons.entity.EnteEntity;
import it.csi.moon.commons.entity.EpayComponentePagamentoEntity;
import it.csi.moon.commons.entity.EpayRichiestaEntity;
import it.csi.moon.commons.entity.FruitoreAttributoEntity;
import it.csi.moon.commons.entity.FruitoreDatiAzioneEntity;
import it.csi.moon.commons.entity.FruitoreEntity;
import it.csi.moon.commons.entity.IstanzaApiEntity;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzaPdfEntity;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.ModuloVersioneEntity;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.entity.WorkflowEntity;
import it.csi.moon.commons.entity.WorkflowFilter;
import it.csi.moon.commons.mapper.CategoriaMapper;
import it.csi.moon.commons.mapper.IstanzaMapper;
import it.csi.moon.commons.mapper.RepositoryFileMapper;
import it.csi.moon.commons.mapper.StatoModuloMapper;
import it.csi.moon.commons.util.JsonHelper;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.commons.util.decodifica.DecodificaTipoModificaDati;
import it.csi.moon.moonsrv.business.service.AllegatiService;
import it.csi.moon.moonsrv.business.service.ApiService;
import it.csi.moon.moonsrv.business.service.AuditService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.ReportService;
import it.csi.moon.moonsrv.business.service.WorkflowService;
import it.csi.moon.moonsrv.business.service.helper.JwtClientProfileUtils;
import it.csi.moon.moonsrv.business.service.impl.apipostazione.ApiPostAzioneFactory;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.AzioneDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayComponentePagamentoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.FruitoreAttributoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.FruitoreDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.FruitoreDatiAzioneDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business API per fruitori
 * 
 * @author Danilo Mosca
 *
 * @since 1.0.0
 */
@Component
public class ApiServiceImpl implements ApiService {

	private static final String CLASS_NAME = "ApiServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String DISTINTA_DI_PRESENTAZIONE = "DISTINTA_DI_PRESENTAZIONE";
	
	@Autowired
	FruitoreDAO fruitoreDAO;
	@Autowired
	FruitoreAttributoDAO fruitoreAttributoDAO;
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	AllegatiService allegatiService;
	@Autowired
	AllegatoDAO allegatoDAO;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	StatoDAO statoDAO;
	@Autowired
	JwtClientProfileUtils jwtClientProfileUtils;
	@Autowired
	WorkflowDAO workflowDAO;
	@Autowired
	FruitoreDatiAzioneDAO fruitoreDatiAzioneDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	AzioneDAO azioneDAO;
	@Autowired
	EnteDAO enteDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;	
	@Autowired
	EpayRichiestaDAO epayRichiestaDAO;
	@Autowired
	EpayComponentePagamentoDAO epayComponentePagamentoDAO;
	@Autowired
	AuditService auditService;
	@Autowired
	ReportService reportService;
	@Autowired
	WorkflowService workflowService;

	@Override
	public List<String> getIstanze(String codiceModulo, String stato,  String versioneModulo, String codiceEnte, String codiceAmbito, String identificativoUtente, Date dataDa, Date dataA, boolean test, 
			String clientProfile, String xRequestId) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN stato: " + stato);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN versioneModulo: " + versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN codiceEnte: " + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN codiceAmbito: " + codiceAmbito);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN identificativoUtente: " + identificativoUtente);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN dataDa: " + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN dataA: " + dataA);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN test: " + test);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN xRequestId: " + xRequestId);
			}
			
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getIstanze] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] idFruitore by codiceFruitore : " + fruitore.getIdFruitore());
			}
			List<FruitoreAttributoEntity> attrs = fruitoreAttributoDAO.findByIdFruitore(fruitore.getIdFruitore());
			validaIstanzeRequest(fruitore, attrs, codiceModulo, stato, codiceEnte, identificativoUtente);

			// audit istanze
			String params = "CODICE MODULO = {"+StringUtils.defaultIfBlank(codiceModulo, "null") +"}"+
					        " - STATO = {"+StringUtils.defaultIfBlank(stato, "null") +"}"+
					        " - VERSIONE MODULO = {"+StringUtils.defaultIfBlank(versioneModulo, "null") +"}"+
					        " - CODICE ENTE = {"+StringUtils.defaultIfBlank(codiceEnte, "null") +"}"+
					        " - DATA DA = {"+StringUtils.defaultIfBlank(dataDa+"", "null") +"}"+
					        " - DATA A = {"+StringUtils.defaultIfBlank(dataA+"", "null") +"}"+
					        " - TEST= {"+StringUtils.defaultIfBlank(test+"", "null")+"}";
	
			auditService.insertAuditAPIIstanze(retrieveIP(), codiceFruitore, params);
			
			// Ente : Default null (tutti enti associati al fruitore)
			Long idEnte = null;
			if ( StringUtils.isNotEmpty(codiceEnte)) {
				idEnte = enteDAO.findByCodice(codiceEnte).getIdEnte();
				LOG.debug("[" + CLASS_NAME + "::getIstanze] idEnte by codiceEnte : " + idEnte);
				validaIdEnteFruitore(idEnte, fruitore.getIdFruitore());
			}

			// Modulo
			Long idModulo = null;
			if (StringUtils.isNotEmpty(codiceModulo)) {
				idModulo = retrieveModulo(codiceModulo);
				validaModuloFruitore(idModulo, fruitore.getIdFruitore());
			}
			Integer idStato = null;
			if (StringUtils.isNotEmpty(codiceModulo)) {
				idStato = retrieveStato(stato);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] id modulo: " + idModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] id stato: " + idStato);
			}
			
			// VersionModulo
			Long idVersioneModulo = null;
			if (idModulo!=null && StringUtils.isNotEmpty(versioneModulo)) {				
				ModuloVersioneEntity moduloVers = moduloDAO.findVersione(idModulo,versioneModulo);
				idVersioneModulo = moduloVers.getIdVersioneModulo();
			}
			
			List<String> result = istanzaDAO.findForApi(fruitore.getIdFruitore(), idModulo, idStato, idVersioneModulo, idEnte, identificativoUtente, dataDa, dataA, test);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] result.size() = " + result.size());
			}

			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getIstanza] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getIstanza] errore generico DAO ");
			throw new BusinessException();
		}
	}
	
	@Override
	public List<FruitoreIstanza> getIstanze(String codiceModulo, String stato, String versioneModulo, String codiceEnte,
			String codiceAmbito, String identificativoUtente, Date dataDa, Date dataA, String ordinamento, boolean test,
			String clientProfile, String xRequestId) throws UnauthorizedBusinessException, BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN stato: " + stato);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN versioneModulo: " + versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN codiceEnte: " + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN codiceAmbito: " + codiceAmbito);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN identificativoUtente: " + identificativoUtente);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN dataDa: " + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN dataA: " + dataA);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN ordinamento: " + ordinamento);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN test: " + test);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] IN xRequestId: " + xRequestId);
			}
			
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getIstanze] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] idFruitore by codiceFruitore : " + fruitore.getIdFruitore());
			}
			List<FruitoreAttributoEntity> attrs = fruitoreAttributoDAO.findByIdFruitore(fruitore.getIdFruitore());
			validaIstanzeRequest(fruitore, attrs, codiceModulo, stato, codiceEnte, identificativoUtente);

			// audit istanze
			String params = "CODICE MODULO = {"+StringUtils.defaultIfBlank(codiceModulo, "null") +"}"+
					        " - STATO = {"+StringUtils.defaultIfBlank(stato, "null") +"}"+
					        " - VERSIONE MODULO = {"+StringUtils.defaultIfBlank(versioneModulo, "null") +"}"+
					        " - CODICE ENTE = {"+StringUtils.defaultIfBlank(codiceEnte, "null") +"}"+
					        " - DATA DA = {"+StringUtils.defaultIfBlank(dataDa+"", "null") +"}"+
					        " - DATA A = {"+StringUtils.defaultIfBlank(dataA+"", "null") +"}"+
					        " - TEST= {"+StringUtils.defaultIfBlank(test+"", "null")+"}";
	
			auditService.insertAuditAPIIstanze(retrieveIP(), codiceFruitore, params);
			
			// Ente : Default null (tutti enti associati al fruitore)
			Long idEnte = null;
			if ( StringUtils.isNotEmpty(codiceEnte)) {
				idEnte = enteDAO.findByCodice(codiceEnte).getIdEnte();
				LOG.debug("[" + CLASS_NAME + "::getIstanze] idEnte by codiceEnte : " + idEnte);
				validaIdEnteFruitore(idEnte, fruitore.getIdFruitore());
			}

			// Modulo
			Long idModulo = null;
			if (StringUtils.isNotEmpty(codiceModulo)) {
				idModulo = retrieveModulo(codiceModulo);
				validaModuloFruitore(idModulo, fruitore.getIdFruitore());
			}
			Integer idStato = null;
			if (StringUtils.isNotEmpty(codiceModulo)) {
				idStato = retrieveStato(stato);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] id modulo: " + idModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanze] id stato: " + idStato);
			}
			
			// VersionModulo
			Long idVersioneModulo = null;
			if (idModulo!=null && StringUtils.isNotEmpty(versioneModulo)) {				
				ModuloVersioneEntity moduloVers = moduloDAO.findVersione(idModulo,versioneModulo);
				idVersioneModulo = moduloVers.getIdVersioneModulo();
			}
			
			List<IstanzaApiEntity> istanze = istanzaDAO.findForApi(fruitore.getIdFruitore(), idModulo, idStato, idVersioneModulo, idEnte, identificativoUtente, dataDa, dataA, ordinamento, test);
						
			List<FruitoreIstanza> result = istanze.stream()
					.map(istanza -> new FruitoreIstanza(istanza.getCodiceIstanza(),istanza.getCodiceFiscaleDichiarante(),istanza.getCognomeDichiarante(),istanza.getNomeDichiarante(),
							istanza.getDataCreazione(),new FruitoreStato(istanza.getIdStatoWfArrivo().toString(),istanza.getNomeStatoWfArrivo())))
							.collect(Collectors.toList());
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] result.size() = " + result.size());
			}

			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getIstanza] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getIstanza] errore generico DAO ");
			throw new BusinessException();
		}
	}

	
	private void validaIstanzeRequest(FruitoreEntity fruitore, List<FruitoreAttributoEntity> attrs, 
			String codiceModulo, String stato, String codiceEnte, String identificativoUtente) {
		if (!hasFruitoreAllModules(attrs)) {
			if (StringUtils.isEmpty(codiceModulo)) {
				LOG.error("[" + CLASS_NAME + "::validaIstanzeRequest] codiceModulo obbligatorio per fruitore " + fruitore.getDescFruitore());
				throw new BusinessException("codiceModulo obbligatorio per fruitore");
			}
			if (StringUtils.isEmpty(stato)) {
				LOG.error("[" + CLASS_NAME + "::validaIstanzeRequest] stato obbligatorio per fruitore " + fruitore.getDescFruitore());
				throw new BusinessException("stato obbligatorio per fruitore");
			}
		}
		if (hasFruitoreOneUserEnte(attrs)) {
			if (StringUtils.isEmpty(identificativoUtente)) {
				LOG.error("[" + CLASS_NAME + "::validaIstanzeRequest] identificativoUtente obbligatorio per fruitore " + fruitore.getDescFruitore());
				throw new BusinessException("identificativoUtente obbligatorio per fruitore");
			}
			if (StringUtils.isEmpty(codiceEnte)) {
				LOG.error("[" + CLASS_NAME + "::validaIstanzeRequest] codiceEnte obbligatorio per fruitore " + fruitore.getDescFruitore());
				throw new BusinessException("codiceEnte obbligatorio per fruitore");
			}
		}
	}


	private boolean hasFruitoreAllModules(List<FruitoreAttributoEntity> attributi) {
		if (attributi == null || attributi.isEmpty())
			return false;
		return "S".equalsIgnoreCase(
			attributi.stream()
				.filter(a -> FruitoreAttributoDAO.FruitoreAttributoEnum.ALL_MODULES.name().equals(a.getNomeAttributo()))
				.map(a -> a.getValore()).findAny().orElse(null)
			);
	}
	private boolean hasFruitoreOneUserEnte(List<FruitoreAttributoEntity> attributi) {
		if (attributi == null || attributi.isEmpty())
			return false;
		return "S".equalsIgnoreCase(
			attributi.stream()
				.filter(a -> FruitoreAttributoDAO.FruitoreAttributoEnum.ONE_USER_ENTE.name().equals(a.getNomeAttributo()))
				.map(a -> a.getValore()).findAny().orElse(null)
			);
	}

	@Override
	public Integer getCountIstanze(String codiceModulo, String stato,  String versioneModulo, String codiceEnte, String codiceAmbito, String identificativoUtente, Date dataDa, Date dataA, boolean test, 
			String clientProfile, String xRequestId) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] IN stato: " + stato);
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] IN versioneModulo: " + versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] IN codiceEnte: " + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] IN identificativoUtente: " + identificativoUtente);
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] IN dataDa: " + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] IN dataA: " + dataA);
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] IN test: " + test);
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] IN xRequestId: " + xRequestId);
			}
			
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getCountIstanze] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] idFruitore by codiceFruitore : " + fruitore.getIdFruitore());
			}

			// Ente : Default null (tutti enti associati al fruitore)
			Long idEnte = null;
			if ( StringUtils.isNotEmpty(codiceEnte)) {				
				idEnte = enteDAO.findByCodice(codiceEnte).getIdEnte();
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] idEnte by codiceEnte : " + idEnte);
				validaIdEnteFruitore(idEnte, fruitore.getIdFruitore());
			}

			// Modulo
			Long idModulo = retrieveModulo(codiceModulo);
			validaModuloFruitore(idModulo, fruitore.getIdFruitore());
			Integer idStato = retrieveStato(stato);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] id modulo: " + idModulo);
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] id stato: " + idStato);
			}			
			
			// VersionModulo
			Long idVersioneModuloFilter = null;
			if ( StringUtils.isNotEmpty(versioneModulo)) {				
				idVersioneModuloFilter = moduloDAO.findVersione(idModulo,versioneModulo).getIdVersioneModulo();
			}
			
			Integer result = istanzaDAO.countForApi(fruitore.getIdFruitore(), idModulo, idStato, idVersioneModuloFilter, idEnte, identificativoUtente, dataDa, dataA, test);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getCountIstanze] result = " + result);
			}
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getCountIstanze] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getCountIstanze] errore generico DAO ");
			throw new BusinessException();
		}
	}
	
	
	@Override
	public List<String> getIstanzePaginate(String codiceModulo, String stato, String versioneModulo, String codiceEnte, String codiceAmbito, String identificativoUtente,  Date dataDa, Date dataA, boolean test, 
			Integer offset, Integer limit,
			String clientProfile, String xRequestId) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN stato: " + stato);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN versioneModulo: " + versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN codiceEnte: " + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN dataDa: " + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN dataA: " + dataA);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN test: " + test);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN xRequestId: " + xRequestId);
			}
	
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getIstanzePaginate] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] idFruitore by codiceFruitore : " + fruitore.getIdFruitore());
			}
			List<FruitoreAttributoEntity> attrs = fruitoreAttributoDAO.findByIdFruitore(fruitore.getIdFruitore());
			validaIstanzeRequest(fruitore, attrs, codiceModulo, stato, codiceEnte, identificativoUtente);
			
			// audit istanze paginate
			String params = "CODICE MODULO = {"+StringUtils.defaultIfBlank(codiceModulo, "null") +"}"+
					        " - STATO = {"+StringUtils.defaultIfBlank(stato, "null") +"}"+
					        " - VERSIONE MODULO = {"+StringUtils.defaultIfBlank(versioneModulo, "null") +"}"+
					        " - CODICE ENTE = {"+StringUtils.defaultIfBlank(codiceEnte, "null") +"}"+
					        " - DATA DA = {"+StringUtils.defaultIfBlank(dataDa+"", "null") +"}"+
					        " - DATA A = {"+StringUtils.defaultIfBlank(dataA+"", "null") +"}"+
					        " - TEST = {"+StringUtils.defaultIfBlank(test+"", "null")+"}";
	
			auditService.insertAuditAPIIstanzePaginate(retrieveIP(), codiceFruitore, params);
			
			// Ente : Default null (tutti enti associati al fruitore)
			Long idEnte = null;
			if ( StringUtils.isNotEmpty(codiceEnte)) {				
				idEnte = enteDAO.findByCodice(codiceEnte).getIdEnte();
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] idEnte by codiceEnte : " + idEnte);
				validaIdEnteFruitore(idEnte, fruitore.getIdFruitore());
			}

			// Modulo
			Long idModulo = null;
			if (StringUtils.isNotEmpty(codiceModulo)) {
				idModulo = retrieveModulo(codiceModulo);
				validaModuloFruitore(idModulo, fruitore.getIdFruitore());
			}
			Integer idStato = null;
			if (StringUtils.isNotEmpty(codiceModulo)) {
				idStato = retrieveStato(stato);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] id modulo: " + idModulo);
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] id stato: " + idStato);
			}
			
			// VersionModulo
			Long idVersioneModuloFilter = null;
			if (StringUtils.isNotEmpty(versioneModulo)) {			
				idVersioneModuloFilter = moduloDAO.findVersione(idModulo,versioneModulo).getIdVersioneModulo();
			}			
			
			List<String> result = istanzaDAO.findForApi(fruitore.getIdFruitore(), idModulo, idStato, idVersioneModuloFilter, idEnte, identificativoUtente, dataDa, dataA, test, offset, limit);			

			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzePaginate] result.size() = " + result.size());
			}
						
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzePaginate] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getIstanzePaginate] errore generico DAO ");
			throw new BusinessException();
		}
	}
	
	@Override
	public FruitoreIstanzaDettagliata getIstanza(String codiceIstanza, String clientProfile, String xRequestId) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN codiceIstanza: " + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanza] IN xRequestId: " + xRequestId);
			}
			
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getIstanza] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			
			String params = "CODICE ISTANZA = {"+StringUtils.defaultIfBlank(codiceIstanza, "null")+"}";
			auditService.insertAuditAPIDettaglioIstanza(retrieveIP(), codiceFruitore, params);
			
			// Istanza :  
			IstanzaEntity istanza = istanzaDAO.findByCd(codiceIstanza);			
			validaModuloFruitore(istanza.getIdModulo(), fruitore.getIdFruitore());
			validaIdEnteFruitore(istanza.getIdEnte(), fruitore.getIdFruitore());

			IstanzaCronologiaStatiEntity cronologia = istanzaDAO.findLastCronologia(istanza.getIdIstanza());
			IstanzaDatiEntity istanzaDati = istanzaDAO.findDati(istanza.getIdIstanza(), cronologia.getIdCronologiaStati());
			List<FruitoreAllegato> allegati = allegatoDAO.findByIdIstanzaForApi(istanza.getIdIstanza());
			
			EnteEntity ente = enteDAO.findById(istanza.getIdEnte());
			ModuliFilter filter = new ModuliFilter();
			filter.setIdModulo(istanza.getIdModulo());
			filter.setIdVersioneModulo(istanza.getIdVersioneModulo());
			ModuloVersionatoEntity modulo = moduloDAO.find(filter).get(0);
			
		    Integer idStatoWf = istanza.getIdStatoWf();
		    StatoEntity stato = statoDAO.findById(idStatoWf);
		    
		    FruitoreStato fruitoreStato = new FruitoreStato(stato.getCodiceStatoWf(), stato.getDescStatoWf());
		    FruitorePagamento pagamento = null;
			try {
				EpayRichiestaEntity richiestaEpay =  epayRichiestaDAO.findLastValideByIdIstanza(istanza.getIdIstanza());		    
				pagamento = retrievePagamento(richiestaEpay);
			} catch (ItemNotFoundDAOException e) {
				LOG.debug("[" + CLASS_NAME + "::getIstanza] pagamento non presente");
			}
		    		    		   			
			FruitoreIstanzaDettagliata result = new FruitoreIstanzaDettagliata();
			result.setCodice(istanza.getCodiceIstanza());
			result.setCodiceFiscaleDichiarante(istanza.getCodiceFiscaleDichiarante());
			result.setDataCreazione(istanza.getDataCreazione());
			result.setJsonString(istanzaDati.getDatiIstanza());
			result.setDataProtocollo(istanza.getDataProtocollo());
			result.setNumeroProtocollo(istanza.getNumeroProtocollo());
			result.setAllegati(allegati);			
			result.setEnte(new FruitoreEnte(ente.getCodiceEnte(),ente.getNomeEnte(),null));
			result.setModulo(new FruitoreModuloCodiceVersione(modulo.getCodiceModulo(),modulo.getVersioneModulo()));
			result.setAttoreIns(istanza.getAttoreIns());
			result.setCognomeDichiarante(istanza.getCognomeDichiarante());
			result.setNomeDichiarante(istanza.getNomeDichiarante());
			result.setStato(fruitoreStato);
			result.setPagamento(pagamento);
			
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getIstanza] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getIstanza] errore generico DAO ");
			throw new BusinessException();
		}
	}

	@Override
	@Transactional
	public String updateStatoAcquisizione(String codiceIstanza, String codiceAzione, StatoAcquisizioneRequest request,
			String clientProfile, String xRequestId) {
		try {
			boolean updateIstanza = false;
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateStatoAcquisizione] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::updateStatoAcquisizione] IN codiceIstanza=" + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::updateStatoAcquisizione] IN codiceAzione=" + codiceAzione);
				LOG.debug("[" + CLASS_NAME + "::updateStatoAcquisizione] IN request=" + request);
				LOG.debug("[" + CLASS_NAME + "::updateStatoAcquisizione] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::updateStatoAcquisizione] IN xRequestId: " + xRequestId);
			}
			
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::updateStatoAcquisizione] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			
			// audit stato acquisizione
			String params = "CODICE ISTANZA = {"+StringUtils.defaultIfBlank(codiceIstanza, "null")+"} - CODICE AZIONE = {"+StringUtils.defaultIfBlank(codiceAzione,"null")+"}";
			auditService.insertAuditAPICambioStato(retrieveIP(), codiceFruitore, params);
			
			// Istanza :  
			IstanzaEntity istanzaE = istanzaDAO.findByCd(codiceIstanza);			
			validaModuloFruitore(istanzaE.getIdModulo(), fruitore.getIdFruitore());
			validaIdEnteFruitore(istanzaE.getIdEnte(), fruitore.getIdFruitore());
			
			// Azione :
			AzioneEntity azione = azioneDAO.findByCd(codiceAzione);
			StoricoWorkflowEntity lastStoricoWf = storicoWorkflowDAO.findLastStorico(istanzaE.getIdIstanza())
					.orElseThrow(ItemNotFoundBusinessException::new);
			
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdModulo(istanzaE.getIdModulo());
			filter.setIdAzione(azione.getIdAzione());
			filter.setIdStatoWfPartenza(lastStoricoWf.getIdStatoWfArrivo());
			filter.setFlagApi("S");
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			if (elencoWorkflow.isEmpty()) {
				LOG.error("[" + CLASS_NAME + "::updateStatoAcquisizione] " + codiceIstanza + " Nessun Workflow trovato per filter=" + filter);
				throw new BusinessException("Azione non eseguibile","MOONSRV-00910");
			} else if (elencoWorkflow.size() > 1) {
				LOG.error("[" + CLASS_NAME + "::updateStatoAcquisizione] " + codiceIstanza + " Troppi Workflow trovati per filter=" + filter);
				throw new BusinessException("Errore configurazione workflow", "MOONSRV-00911");
			}
			WorkflowEntity workflow = elencoWorkflow.get(0);

//			StoricoWorkflowEntity storicoToUpdate = storicoWorkflowDAO.findLastStorico(istanzaE.getIdIstanza())
//					.orElseThrow(ItemNotFoundBusinessException::new);
			
//			if (!storicoToUpdate.getIdStatoWfArrivo().equals(workflow.getIdStatoWfPartenza())) {
//				LOG.error("[" + CLASS_NAME + "::updateStatoAcquisizione] " + codiceIstanza + " Stato istanza non coerente - attuale ="
//						+ istanzaE.getIdStatoWf() + " atteso=" + workflow.getIdStatoWfPartenza());
//				throw new BusinessException("Stato Istanza non coerente", "MOONSRV-00912");
//			}

			StatoEntity statoPartenza = statoDAO.findById(workflow.getIdStatoWfPartenza());
			StatoEntity statoArrivo = statoDAO.findById(workflow.getIdStatoWfArrivo());

			// 3. Preparazione in base a dati request
			String descDestinatario = null;
			Date now = new Date();
			// update data fine

			storicoWorkflowDAO.updateDataFine(now, lastStoricoWf.getIdIstanza());

			// 4. Update stato
			StoricoWorkflowEntity storicoWf = new StoricoWorkflowEntity(null, istanzaE.getIdIstanza(),
					workflow.getIdProcesso(), workflow.getIdStatoWfPartenza(), workflow.getIdStatoWfArrivo(),
					azione.getIdAzione(), statoPartenza.getNomeStatoWf(), statoArrivo.getNomeStatoWf(),
					azione.getNomeAzione(), descDestinatario, now, "FRUITORE:"+fruitore.getIdFruitore().toString());
//			Long idStoricoWf = istanzaDAO.insertStoricoWorkflow(storicoWf);
			Long idStoricoWf = storicoWorkflowDAO.insert(storicoWf);
			storicoWf.setIdStoricoWorkflow(idStoricoWf);
									
			// aggioronamento dell'istanza e della cronologia in base a flag di stato
			// configurato in workflow
			
			IstanzaCronologiaStatiEntity istanzaECronologia = null;
			IstanzaDatiEntity istanzaEDati = null;
			if ("S".equals(workflow.getFlagStatoIstanza())) {

				// Aggiornamento dell'entity Istanza che verra aggiornata alla fine della function
				istanzaE.setIdStatoWf(workflow.getIdStatoWfArrivo());
				updateIstanza = true;

				// aggiornare lastCronologia e inserire la nuova cronologia

				// CRON
				String codFisc = StringUtils.isBlank(request.getCodiceFiscaleOperatore())
						? "fruitore_" + fruitore.getIdFruitore()
						: request.getCodiceFiscaleOperatore();

				// CRON
				// last CRON
				IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(istanzaE.getIdIstanza());
				lastIstanzaCronologia.setAttoreUpd(codFisc);
				lastIstanzaCronologia.setDataFine(now);
				istanzaDAO.updateCronologia(lastIstanzaCronologia);
				// new CRON
				istanzaECronologia = new IstanzaCronologiaStatiEntity();
				istanzaECronologia.setIdIstanza(istanzaE.getIdIstanza());
				istanzaECronologia.setIdStatoWf(workflow.getIdStatoWfArrivo());
				istanzaECronologia.setAttoreIns(codFisc);
				istanzaECronologia.setDataInizio(now);
				istanzaECronologia.setIdAzioneSvolta(workflow.getIdAzione());
				Long idCronologiaStati = istanzaDAO.insertCronologia(istanzaECronologia);
				istanzaECronologia.setIdCronologiaStati(idCronologiaStati);

				// DATI
				istanzaEDati = istanzaDAO.findDati(istanzaE.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
				istanzaEDati.setDataUpd(now);
				istanzaEDati.setDatiIstanza(istanzaEDati.getDatiIstanza());
				istanzaEDati.setIdCronologiaStati(istanzaECronologia.getIdCronologiaStati());
				istanzaEDati.setIdStepCompilazione(null);
				istanzaEDati.setIdTipoModifica(DecodificaTipoModificaDati.NON.getIdTipoModifica());
				Long idIstanzaDati = istanzaDAO.insertDati(istanzaEDati);
				istanzaEDati.setIdDatiIstanza(idIstanzaDati);
			}

			// 5. salvataggio della request
			FruitoreDatiAzioneEntity datiFruitore = new FruitoreDatiAzioneEntity();
			datiFruitore.setCodice(StringUtils.left(request.getCodice(), 30));
			datiFruitore.setDescrizione(StringUtils.left(request.getDescrizione(), 255));
			datiFruitore.setIdentificativo(StringUtils.left(request.getIdentificativo(), 50));
			datiFruitore.setData(request.getData());
			datiFruitore.setNumeroProtocollo(StringUtils.left(request.getNumeroProtocollo(), 50));
			datiFruitore.setDataProtocollo(request.getDataProtocollo());
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateStatoAcquisizione] data entity: " + datiFruitore.getData());
			}
			datiFruitore.setIdIstanza(istanzaE.getIdIstanza());
			datiFruitore.setIdStoricoWorkflow(idStoricoWf);
            datiFruitore.setDatiAzione(request.getDatiAzione());                  
            datiFruitore.setPostAzioni(JsonHelper.getJsonFromList(request.getPostAzioni()));
            datiFruitore.setAllegatiAzione(JsonHelper.getJsonFromList(request.getAllegatiAzione()));
            
			fruitoreDatiAzioneDAO.insert(datiFruitore);
			
			// inserimento allegati legati ad idStoricoWf
			boolean firstIdFileInserted = true;
			List<FruitoreAllegatoAzione> allegatiAzione = request.getAllegatiAzione();
			if (!allegatiAzione.isEmpty()) {
				for (FruitoreAllegatoAzione allegatoAzione : allegatiAzione) {
					RepositoryFileEntity entity = RepositoryFileMapper.buildFromObj(allegatoAzione,
							istanzaE.getIdIstanza(), storicoWf.getIdStoricoWorkflow(), null);
					Long idFile = repositoryFileDAO.insert(entity);
					if (firstIdFileInserted) {
						// Patch valorizza id_file_rendering per DEM Robot Maggioli
						storicoWorkflowDAO.updateIdFileRendered(storicoWf.getIdStoricoWorkflow(), idFile);
						firstIdFileInserted = false;
					}
				}
			}
			
			if (StringUtils.isNotBlank(request.getNumeroProtocollo()) || request.getDataProtocollo()!=null) {
				istanzaE.setNumeroProtocollo(request.getNumeroProtocollo());
				istanzaE.setDataProtocollo(request.getDataProtocollo());
				updateIstanza = true;
			}
			if (updateIstanza) {
				istanzaDAO.update(istanzaE);
			}
	
			if (istanzaECronologia == null) {
				istanzaECronologia =  istanzaDAO.findLastCronologia(istanzaE.getIdIstanza());
			}
	        if (istanzaEDati == null) {
	        	istanzaEDati = istanzaDAO.findDati(istanzaE.getIdIstanza(), istanzaECronologia.getIdCronologiaStati());
			}
	        
	        ModuloVersionatoEntity modEntity = moduloDAO.findModuloVersionatoById(istanzaE.getIdModulo(), istanzaE.getIdVersioneModulo());
	        Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(istanzaE,istanzaECronologia, istanzaEDati, statoArrivo, modEntity/*, null*/);
	        
			List<PostAzione> postAzioni = request.getPostAzioni();
			if (!postAzioni.isEmpty()) {
				LOG.info("[" + CLASS_NAME + "::updateStatoAcquisizione] - invio email postAzioni");
				new ApiPostAzioneFactory().getApiPostAzione(codiceAzione, istanza, storicoWf.getIdStoricoWorkflow())
						.execute(postAzioni);
			} else {
				sendEmailIfConfigured(codiceAzione, storicoWf, modEntity, istanza);
			}
			
			workflowService.compieAzioneAutomaticaIfPresente(istanzaE.getIdIstanza());

			return "OK";
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::updateStatoAcquisizione] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::updateStatoAcquisizione] errore generico DAO ");
			throw new BusinessException("Errore generico di salvataggio","MOONSRV-00903");
		}
	}


	private void sendEmailIfConfigured(String codiceAzione, StoricoWorkflowEntity storicoWf,
			ModuloVersionatoEntity modEntity, Istanza istanza) {
		// invio email di default
		List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO
				.findByIdModulo(modEntity.getIdModulo());
		MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
		String conf = attributi.getWithCorrectType(ModuloAttributoKeys.APA_EMAIL_CONF);
		LOG.info("[" + CLASS_NAME + "::sendEmailIfConfigured] Invio email default conf: " + conf);
		if (StringUtils.isNotEmpty(conf)) {
			new ApiPostAzioneFactory().getApiPostAzione(codiceAzione, istanza, storicoWf.getIdStoricoWorkflow())
					.sendEmail(null);
		}
	}
	
	
	@Override
	@Transactional
	public String getNotifica(String codiceIstanza, StatoAcquisizioneRequest request,
			String clientProfile, String xRequestId) {
		try {
			boolean updateIstanza = false;
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getNotifica] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getNotifica] IN codiceIstanza=" + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::getNotifica] IN request=" + request);
				LOG.debug("[" + CLASS_NAME + "::getNotifica] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getNotifica] IN xRequestId: " + xRequestId);
			}

			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getNotifica] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			
			String params = "CODICE ISTANZA = {"+StringUtils.defaultIfBlank(codiceIstanza, "null")+"} - CODICE AZIONE = {"+DecodificaAzione.NOTIFICA_FRUITORE.getCodice()+"}";
			auditService.insertAuditAPICambioStato(retrieveIP(), codiceFruitore, params);
			
			// Istanza :  
			IstanzaEntity istanzaE = istanzaDAO.findByCd(codiceIstanza);			
			validaModuloFruitore(istanzaE.getIdModulo(), fruitore.getIdFruitore());
			validaIdEnteFruitore(istanzaE.getIdEnte(), fruitore.getIdFruitore());
			
			IstanzaCronologiaStatiEntity istanzaECronologia = null;
			IstanzaDatiEntity istanzaEDati = null;
			
			// update data fine		
			Date now = new Date();
			
			// descrizione destinatario
			String descDestinatario = null;
			
			String codFisc = StringUtils.isBlank(request.getCodiceFiscaleOperatore())
				? "fruitore_" + fruitore.getIdFruitore()
				: request.getCodiceFiscaleOperatore();

			// Aggiornamento storico azione notifica fruitore senza cambio di stato
			
			StoricoWorkflowEntity lastStorico = storicoWorkflowDAO.findLastStorico(istanzaE.getIdIstanza())
					.orElseThrow(ItemNotFoundBusinessException::new);
			storicoWorkflowDAO.updateDataFine(now,istanzaE.getIdIstanza());

			// 4. Update stato
			StoricoWorkflowEntity storicoWf = new StoricoWorkflowEntity(null, istanzaE.getIdIstanza(),
					lastStorico.getIdProcesso(), lastStorico.getIdStatoWfArrivo(), lastStorico.getIdStatoWfArrivo(),
					DecodificaAzione.NOTIFICA_FRUITORE.getIdAzione(), lastStorico.getNomeStatoWfArrivo(), lastStorico.getNomeStatoWfArrivo(),
					DecodificaAzione.NOTIFICA_FRUITORE.getNomeAzione(), descDestinatario, now, "FRUITORE:"+fruitore.getIdFruitore().toString());
			Long idStoricoWf = storicoWorkflowDAO.insert(storicoWf);
			storicoWf.setIdStoricoWorkflow(idStoricoWf);

			// 5. salvataggio della request
			FruitoreDatiAzioneEntity datiFruitore = new FruitoreDatiAzioneEntity();
			datiFruitore.setCodice(StringUtils.left(request.getCodice(), 30));
			datiFruitore.setDescrizione(StringUtils.left(request.getDescrizione(), 255));
			datiFruitore.setIdentificativo(StringUtils.left(request.getIdentificativo(), 50));
			datiFruitore.setData(request.getData());
			datiFruitore.setNumeroProtocollo(StringUtils.left(request.getNumeroProtocollo(), 50));
			datiFruitore.setDataProtocollo(request.getDataProtocollo());
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getNotifica] data entity: " + datiFruitore.getData());
			}
			datiFruitore.setIdIstanza(istanzaE.getIdIstanza());
			datiFruitore.setIdStoricoWorkflow(idStoricoWf);
            datiFruitore.setDatiAzione(request.getDatiAzione());                  
            datiFruitore.setPostAzioni(JsonHelper.getJsonFromList(request.getPostAzioni()));
            datiFruitore.setAllegatiAzione(JsonHelper.getJsonFromList(request.getAllegatiAzione()));
            
			fruitoreDatiAzioneDAO.insert(datiFruitore);
			
			// inserimento allegati legati ad idStoricoWf
			List<FruitoreAllegatoAzione> allegatiAzione = request.getAllegatiAzione();
			if (!allegatiAzione.isEmpty()) {
				for (FruitoreAllegatoAzione allegatoAzione : allegatiAzione) {
					RepositoryFileEntity entity = RepositoryFileMapper.buildFromObj(allegatoAzione,
							istanzaE.getIdIstanza(), storicoWf.getIdStoricoWorkflow(), DecodificaAzione.NOTIFICA_FRUITORE.getCodice());
					repositoryFileDAO.insert(entity);
				}
			}			
			
			if (StringUtils.isNotBlank(request.getNumeroProtocollo()) || request.getDataProtocollo()!=null) {
				istanzaE.setNumeroProtocollo(request.getNumeroProtocollo());
				istanzaE.setDataProtocollo(request.getDataProtocollo());
				updateIstanza = true;
			}
			if (updateIstanza) {
				istanzaDAO.update(istanzaE);
			}
	
			if (istanzaECronologia == null) {
				istanzaECronologia =  istanzaDAO.findLastCronologia(istanzaE.getIdIstanza());
			}
	        if (istanzaEDati == null) {
	        	istanzaEDati = istanzaDAO.findDati(istanzaE.getIdIstanza(), istanzaECronologia.getIdCronologiaStati());
			}
	        
	        StatoEntity statoArrivo = statoDAO.findById(storicoWf.getIdStatoWfArrivo());
	        
	        ModuloVersionatoEntity modEntity = moduloDAO.findModuloVersionatoById(istanzaE.getIdModulo(), istanzaE.getIdVersioneModulo());
	        Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(istanzaE,istanzaECronologia, istanzaEDati, statoArrivo, modEntity/*, null*/);
	        
			List<PostAzione> postAzioni = request.getPostAzioni();
			if (!postAzioni.isEmpty()) {
				new ApiPostAzioneFactory().getApiPostAzione(DecodificaAzione.NOTIFICA_FRUITORE.getCodice(),istanza, storicoWf.getIdStoricoWorkflow()).execute(postAzioni);
			}else {
				sendEmailIfConfigured(DecodificaAzione.NOTIFICA_FRUITORE.getCodice(), storicoWf, modEntity, istanza);
			}
			
			return "OK";
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getNotifica] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getNotifica] errore generico DAO ");
			throw new BusinessException();
		}
	}

	@Override
	public Allegato getAllegato(String codiceIstanza, String codiceFile, String formIoNameFile, 
			String clientProfile, String xRequestId) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getAllegato] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getAllegato] IN codiceIstanza=" + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::getAllegato] IN codiceFile=" + codiceFile);
				LOG.debug("[" + CLASS_NAME + "::getAllegato] IN formIoNameFile=" + formIoNameFile);
				LOG.debug("[" + CLASS_NAME + "::getAllegato] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getAllegato] IN xRequestId: " + xRequestId);
			}

			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getAllegato] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			
			String params = "CODICE ISTANZA = {"+StringUtils.defaultIfBlank(codiceIstanza, "null") +"}"+
			        " - CODICE FILE = {"+StringUtils.defaultIfBlank(codiceFile, "null") +"}"+
			        " - FORTMIO NAME FILE = {"+StringUtils.defaultIfBlank(formIoNameFile, "null") +"}";
			auditService.insertAuditAPICercaAllegato(retrieveIP(), codiceFruitore, params);
			
			// Istanza :  
			IstanzaEntity istanza = istanzaDAO.findByCd(codiceIstanza);			
			validaModuloFruitore(istanza.getIdModulo(), fruitore.getIdFruitore());
			validaIdEnteFruitore(istanza.getIdEnte(), fruitore.getIdFruitore());
			
			Allegato result = null;
			if (StringUtils.isNotBlank(codiceFile)) {
				result = (DISTINTA_DI_PRESENTAZIONE.equals(codiceFile)) ?
					getXmlResocontoLikeAllegato(istanza) :
					allegatiService.getByIdIstanzaCodice(istanza.getIdIstanza(), codiceFile);
			} else if (StringUtils.isNotBlank(formIoNameFile)) {
				result = allegatiService.getByFormIoNameFile(formIoNameFile);
			}
			
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getAllegato] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getAllegato] errore generico DAO ");
			throw new BusinessException();
		}
	}

	private Allegato getXmlResocontoLikeAllegato(IstanzaEntity istanzaE) {
		Allegato result = null;
		IstanzaPdfEntity istanzaPdf = printIstanzeService.getIstanzaPdfEntityById(istanzaE.getIdIstanza());
		if (istanzaPdf!=null && istanzaPdf.getResoconto()!=null) {
			result = new Allegato();
			result.setContenuto(istanzaPdf.getResoconto().getBytes());
			result.setNomeFile("DISTINTA_" + istanzaE.getCodiceIstanza() + ".xml");
			result.setContentType("application/xml");
			result.setEstensione("xml");
		}
		return result;
	}


	@Override
	public byte[] getIstanzaPdf(String codiceIstanza, String clientProfile, String xRequestId) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN codiceIstanza=" + codiceIstanza);
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] IN xRequestId: " + xRequestId);
			}
			
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getIstanzaPdf] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			
			String params = "CODICE ISTANZA = {"+StringUtils.defaultIfBlank(codiceIstanza, "null")+"}";
			auditService.insertAuditAPIDettaglioIstanzaPdf(retrieveIP(), codiceFruitore, params);
						
			// Istanza :  
			IstanzaEntity istanza = istanzaDAO.findByCd(codiceIstanza);			
			validaModuloFruitore(istanza.getIdModulo(), fruitore.getIdFruitore());
			validaIdEnteFruitore(istanza.getIdEnte(), fruitore.getIdFruitore());
			
			byte[] result = null;
			IstanzaPdfEntity istanzaPdf = printIstanzeService.getIstanzaPdfEntityById(istanza.getIdIstanza());
			if (istanzaPdf!=null && istanzaPdf.getContenutoPdf()!=null) {
				result = istanzaPdf.getContenutoPdf();
				LOG.debug("[" + CLASS_NAME + "::getIstanzaPdf] bytes.length=" + result.length);
			}
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getIstanzaPdf] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getIstanzaPdf] errore generico DAO ");
			throw new BusinessException();
		}
	}
	
	
	private void validaIdEnteFruitore(Long idEnte, Integer idFruitore) {
		try {
			Integer num = fruitoreDAO.countByIdEnte(idEnte, idFruitore);
			if (num < 1) {
				LOG.error("[" + CLASS_NAME + "::validaIdEnteFruitore] IN idFruitore: " + idFruitore + " idEnte: " + idEnte);
				throw new ItemNotFoundBusinessException("Fruitore non associato ad ente");
			}
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::validaIdEnteFruitore] ItemNotFoundDAOException IN idFruitore: " + idFruitore + " idEnte: " + idEnte);
			throw new ItemNotFoundBusinessException("Fruitore non associato ad ente");
		}
	}
	private void validaCodiceEnteFruitore(String codiceEnte, Integer idFruitore) {
		try {
			Integer num = fruitoreDAO.countByCodiceEnte(codiceEnte, idFruitore);
			if (num < 1) {
				LOG.error("[" + CLASS_NAME + "::validaCodiceEnteFruitore] IN idFruitore: " + idFruitore + " codiceEnte: " + codiceEnte);
				throw new ItemNotFoundBusinessException("Fruitore non associato ad ente");
			}
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::validaCodiceEnteFruitore] ItemNotFoundDAOException IN idFruitore: " + idFruitore + " codiceEnte: " + codiceEnte);
			throw new ItemNotFoundBusinessException("Fruitore non associato ad ente");
		}
	}
	private void validaCodiceAreaFruitore(String codiceEnte, String codiceArea, Integer idFruitore) {
		try {
			Integer num = fruitoreDAO.countByCodiceEnteArea(codiceEnte, codiceArea, idFruitore);
			if (num < 1) {
				LOG.error("[" + CLASS_NAME + "::validaCodiceAreaFruitore] IN idFruitore: " + idFruitore + " codiceEnte: " + codiceEnte + " codiceArea: " + codiceArea);
				throw new ItemNotFoundBusinessException("Fruitore non associato all area");
			}
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::validaCodiceAreaFruitore] ItemNotFoundDAOException IN idFruitore: " + idFruitore + " codiceEnte: " + codiceEnte + " codiceArea: " + codiceArea);
			throw new ItemNotFoundBusinessException("Fruitore non associato all area");
		}
	}

	private void validaModuloFruitore(Long idModulo, Integer idFruitore) {
		try {
			Integer num = fruitoreDAO.countByIdModulo(idModulo, idFruitore);
			if (num < 1) {
				LOG.error("[" + CLASS_NAME + "::validaModuloFruitore] IN idFruitore: " + idFruitore + " idModulo: " + idModulo);
				throw new ItemNotFoundBusinessException("Fruitore non associato al modulo");
			}
		} catch (ItemNotFoundDAOException nfdaoe) {
			LOG.error("[" + CLASS_NAME + "::validaModuloFruitore] ItemNotFoundDAOException IN idFruitore: " + idFruitore + " idModulo: " + idModulo);
			throw new ItemNotFoundBusinessException("Fruitore non associato al modulo");
		}
	}

	private Integer retrieveStato(String stato) {
		try {
			LOG.debug("[" + CLASS_NAME + "::retrieveStato] IN stato: " + stato);
			return statoDAO.findByCd(stato).getIdStatoWf();
		} catch (ItemNotFoundDAOException nfdaoe) {
			LOG.error("[" + CLASS_NAME + "::retrieveStato] Errore not found - ", nfdaoe);
			throw new ItemNotFoundBusinessException(nfdaoe);
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::retrieveStato] errore validazione stato");
			throw new BusinessException(daoe);
		}
	}

	private Long retrieveModulo(String codiceModulo) {
		try {
			LOG.debug("[" + CLASS_NAME + "::retrieveModulo] IN codice modulo: " + codiceModulo);
			return moduloDAO.findByCodice(codiceModulo).getIdModulo();
		} catch (ItemNotFoundDAOException nfdaoe) {
			LOG.error("[" + CLASS_NAME + "::retrieveModulo] Modulo not found for " + codiceModulo);
			throw new ItemNotFoundBusinessException(nfdaoe);
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::retrieveModulo] errore validazione modulo");
			throw new BusinessException(daoe);
		}
	}
	
	
	private FruitorePagamento retrievePagamento(EpayRichiestaEntity epayRichiesta) {
		FruitorePagamento pagamento = buildPagamentoFromEpayRichiestaEntity(epayRichiesta);
		if (pagamento==null) {
			LOG.warn("[" + CLASS_NAME + "::buildPagamentoFromEpayRichiestaEntity] IUVChiamanteEsternoRequest request NULL, not retrieved. ");
			return null;
		}
		List<EpayComponentePagamentoEntity> importiPagamento = null;
		try {
			importiPagamento = epayComponentePagamentoDAO
					.findByIdModulo(epayRichiesta.getIdModulo());
			
			List<FruitoreDettaglioImporto> dettaglioImporti = importiPagamento.stream()
					.map(epayCompPag -> new FruitoreDettaglioImporto(epayCompPag.getNumeroAccertamento(),
							epayCompPag.getCodiceTipoVersamento(), epayCompPag.getImporto()))
					.collect(Collectors.toList());
			pagamento.setDettaglioImporti(dettaglioImporti);
			
		} catch (EmptyResultDataAccessException e) {
			LOG.debug("[" + CLASS_NAME + "::retrievePagamento] pagamenti non presenti: " + e.getMessage(), e);
		}	
		return pagamento;
	}
	
	
	private FruitorePagamento buildPagamentoFromEpayRichiestaEntity(EpayRichiestaEntity entity) {

		IUVChiamanteEsternoRequest request = retrieveIUVChiamanteEsternoRequest(entity);
		if (request==null) {
			LOG.error("[" + CLASS_NAME + "::buildPagamentoFromEpayRichiestaEntity] IUVChiamanteEsternoRequest request NULL, not retrieved. ");
			return null;
		}
		
		FruitorePagamento obj = new FruitorePagamento();

		obj.setIuv(entity.getIuv());
		obj.setCodiceAvviso(entity.getCodiceAvviso());
		obj.setDataRicevutaPagamento(entity.getDataNotificaPagamento());
		obj.setCodiceFiscaleEnte(request.getCodiceFiscaleEnte());
		obj.setImporto(request.getImporto());
		obj.setCausale(request.getCausale());
		obj.setCodiceVersamento(request.getTipoPagamento());
		obj.setCodiceFiscalePartitaIVAPagatore(request.getCodiceFiscalePartitaIVAPagatore());
		obj.setNome(request.getNome());
		obj.setCognome(request.getCognome());
		obj.setEmail(request.getEmail());
		obj.setRagioneSociale(request.getRagioneSociale());

		return obj;
	}

	private IUVChiamanteEsternoRequest retrieveIUVChiamanteEsternoRequest(EpayRichiestaEntity entity) {
		try {
			return getMapper().readValue(entity.getRichiesta(), IUVChiamanteEsternoRequest.class);
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::retrieveIUVChiamanteEsternoRequest] IOException ", e);
			return null;
		}
	}
	
	private ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper()
				.setSerializationInclusion(Include.NON_EMPTY)
				.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//		SerializationConfig config = mapper.getSerializationConfig().withSerializationInclusion(Inclusion.NON_EMPTY)
//				.without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
//		mapper.setSerializationConfig(config);
		return mapper;
	}
	
	private String retrieveIP() {
		String result = "127.0.0.1";
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			result = inetAddress.getHostAddress();
		} catch (UnknownHostException e) {
			LOG.error("[" + CLASS_NAME + "::retrieveIP] UnknownHostException ", e);
		}
		return result;
	}


	@Override
	public List<FruitoreModuloVersione> getElencoModuli(ModuliFilter filter, 
			String clientProfile, String xRequestId) throws UnauthorizedBusinessException, BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getElencoModuli] IN filter: " + filter);
				LOG.debug("[" + CLASS_NAME + "::getElencoModuli] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getElencoModuli] IN xRequestId: " + xRequestId);
			}
			
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getElencoModuli] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			filter.setIdFruitore(fruitore.getIdFruitore());
			validaElencoModuliRequest(filter, fruitore.getIdFruitore());
//			String params = "CODICE MODULO = {" + StringUtils.defaultIfBlank(codiceModulo, "null") + "}" +
//			        " - VERSIONE = {" + StringUtils.defaultIfBlank(versioneModulo, "null") + "}" +
//			        " - FIELDS = {" + StringUtils.defaultIfBlank(fields, "null") + "}";
//			auditService.insertAuditAPIModuloVersione(retrieveIP(), codiceFruitore, params);
			List<ModuloVersionatoEntity> moduloE = moduloDAO.find(filter);
			
			List<FruitoreModuloVersione> result = moduloE.stream().map(ApiServiceImpl::remapModuloVersione).collect(Collectors.toList());
		
			return result;
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getElencoModuli] errore generico DAO ");
			throw new BusinessException();
		}
	}

	
	private void validaElencoModuliRequest(ModuliFilter filter, Integer idFruitore) {
		String codiceEnte = null;
		Optional<String> codiceEnteOpt = filter.getCodiceEnte();
		if (codiceEnteOpt.isPresent()) {
			codiceEnte = codiceEnteOpt.get();
			validaCodiceEnteFruitore(codiceEnte, idFruitore);
		}
		Optional<String> codiceAreaOpt = filter.getCodiceArea();
		if (codiceEnteOpt.isEmpty() && codiceAreaOpt.isPresent()) {
			String codiceArea = codiceAreaOpt.get();
			LOG.error("[" + CLASS_NAME + "::validaElencoModuliRequest] IN idFruitore: " + idFruitore + " codiceArea: " + codiceArea + " without codiceEnte.");
			throw new BusinessException("codiceArea senza codiceEnte !");
		}
		if (codiceAreaOpt.isPresent()) {
			validaCodiceAreaFruitore(codiceEnte, codiceAreaOpt.get(), idFruitore);
		}
	}

	@Override
	public FruitoreModuloVersione getModuloVersione(String codiceModulo, String versioneModulo, String fields,
			String clientProfile, String xRequestId)
			throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuloVersione] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuloVersione] IN versioneModulo: " + versioneModulo);
				LOG.debug("[" + CLASS_NAME + "::getModuloVersione] IN fields: " + fields);
				LOG.debug("[" + CLASS_NAME + "::getModuloVersione] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getModuloVersione] IN xRequestId: " + xRequestId);
			}
			
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getModuloVersione] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			
			String params = "CODICE MODULO = {" + StringUtils.defaultIfBlank(codiceModulo, "null") + "}" +
			        " - VERSIONE = {" + StringUtils.defaultIfBlank(versioneModulo, "null") + "}" +
			        " - FIELDS = {" + StringUtils.defaultIfBlank(fields, "null") + "}";
			auditService.insertAuditAPIModuloVersione(retrieveIP(), codiceFruitore, params);
			
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoByCodice(codiceModulo, versioneModulo);
			validaModuloFruitore(moduloE.getIdModulo(),fruitore.getIdFruitore());
			
			FruitoreModuloVersione result = remapModuloVersione(moduloE);
		
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getModuloVersione] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getModuloVersione] errore generico DAO ");
			throw new BusinessException();
		}
	}

	protected static FruitoreModuloVersione remapModuloVersione(ModuloVersionatoEntity moduloE) {
		FruitoreModuloVersione result = new FruitoreModuloVersione();
		result.setCodice(moduloE.getCodiceModulo());
		result.setVersione(moduloE.getVersioneModulo());
		result.setOggetto(moduloE.getOggettoModulo());
		result.setDescrizione(moduloE.getDescrizioneModulo());
		result.setDataIns(moduloE.getDataIns());
		result.setDataUpd(moduloE.getDataIns());
		result.setCategoria(CategoriaMapper.buildFromModuloVersionatoEntityForApi(moduloE));
		result.setStatoAttuale(StatoModuloMapper.buildFromIdStato(moduloE.getIdStato()));
		return result;
	}

	
	@Override
	public byte[] getReport(String codiceModulo, String codice, Date dataDa, Date dataA, String clientProfile, String xRequestId) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getReport] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getReport] IN codiceModulo=" + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN codice=" + codice);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN dataDa=" + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN dataA=" + dataA);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN xRequestId: " + xRequestId);
			}
			
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getReport] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			
			String params = "CODICE MODULO = {"+StringUtils.defaultIfBlank(codiceModulo, "null")+"}"+
					 " - CODICE = {" + StringUtils.defaultIfBlank(codice, "null") + "}";
			auditService.insertAuditAPIReport(retrieveIP(), codiceFruitore, params);
						
			// Modulo :  
			ModuloEntity modulo = moduloDAO.findByCodice(codiceModulo);	
			
//			validaModuloFruitore(modulo.getIdModulo(), fruitore.getIdFruitore());
			
//			validaEnteFruitore(modulo., fruitore.getIdFruitore());
			
			byte[] result = null;
//			IstanzaPdfEntity istanzaPdf = printIstanzeService.getIstanzaPdfEntityById(istanza.getIdIstanza());
//			if (istanzaPdf!=null && istanzaPdf.getContenutoPdf()!=null) {
//				result = istanzaPdf.getContenutoPdf();
//				LOG.debug("[" + CLASS_NAME + "::getReport] bytes.length=" + result.length);
//			}
			
			result  = reportService.getReportByCodiceModulo(codiceModulo);
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getReport] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getReport] errore generico DAO ");
			throw new BusinessException();
		}
	}

	@Override
	public StreamingOutput getStreamReport(String codiceModulo, String codiceEstrazione, Date dataDa, Date dataA,
			String clientProfile, String xRequestId)
			throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getStreamReport] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getStreamReport] IN codiceModulo=" + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getStreamReport] IN codiceEstrazione=" + codiceEstrazione);
				LOG.debug("[" + CLASS_NAME + "::getStreamReport] IN dataDa=" + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getStreamReport] IN dataA=" + dataA);
				LOG.debug("[" + CLASS_NAME + "::getStreamReport] IN clientProfile: " + clientProfile);
				LOG.debug("[" + CLASS_NAME + "::getStreamReport] IN xRequestId: " + xRequestId);
			}
			
			if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
				LOG.error("[" + CLASS_NAME + "::getStreamReport] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
				throw new UnauthorizedBusinessException();
			}
			
			// Fruitore :  
			String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
			FruitoreEntity fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
			
			String params = "CODICE MODULO = {"+StringUtils.defaultIfBlank(codiceModulo, "null")+"}"+
					 " - CODICE = {" + StringUtils.defaultIfBlank(codiceEstrazione, "null") + "}";
			auditService.insertAuditAPIReport(retrieveIP(), codiceFruitore, params);
						
			// Modulo :  
			ModuloEntity modulo = moduloDAO.findByCodice(codiceModulo);				
			validaModuloFruitore(modulo.getIdModulo(), fruitore.getIdFruitore());			
						
			StreamingOutput result  = reportService.getStreamReportByCodiceModuloCodiceReport(codiceModulo,codiceEstrazione,dataDa,dataA);
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getStreamReport] risorse non trovata");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {			
			LOG.warn("[" + CLASS_NAME + "::getStreamReport] errore generico DAO ");
			throw new BusinessException();
		}
	}




}
