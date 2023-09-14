/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import it.csi.moon.commons.dto.CampoModulo;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.ReportVerificaAttributiModulo;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.AreaModuloEntity;
import it.csi.moon.commons.entity.DatiAzioneEntity;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.ModuloCronologiaStatiEntity;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.entity.ModuloProgressivoEntity;
import it.csi.moon.commons.entity.ModuloStrutturaEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.ModuloVersioneEntity;
import it.csi.moon.commons.entity.ProtocolloParametroEntity;
import it.csi.moon.commons.mapper.CategoriaMapper;
import it.csi.moon.commons.mapper.ModuloAttributoMapper;
import it.csi.moon.commons.mapper.ModuloMapper;
import it.csi.moon.commons.mapper.PortaleMapper;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.commons.util.MapProtocolloAttributi;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.commons.util.ProtocolloAttributoKeys;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonsrv.business.service.ModuliService;
import it.csi.moon.moonsrv.business.service.ProcessiService;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.CategoriaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO.ModuloAttributoFilter;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloStrutturaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.PortaleDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ProtocolloParametroDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonsrv.business.service.mapper.CampiModuloMapper;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi ai moduli
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class ModuliServiceImpl implements ModuliService {
	
	private static final String CLASS_NAME = "ModuliServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	ModuloStrutturaDAO strutturaDAO;
	@Autowired
	ModuloProgressivoDAO moduloProgressivoDAO;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	@Autowired
	CategoriaDAO categoriaDAO;
	@Autowired
	PortaleDAO portaleDAO;
	@Autowired
	ProcessiService processiService;
	@Autowired
	WorkflowDAO workflowDAO;
	@Autowired
	AreaModuloDAO areaModuloDAO;
	@Autowired
	ProtocolloParametroDAO protocolloParametroDAO;

	@Override
	public List<Modulo> getElencoModuli(ModuliFilter filter) throws BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getElencoModuli] BEGIN");
		}
		try {
			return moduloDAO.find(filter).stream()
				.map(ModuloMapper::buildFromModuloVersionatoEntity)
//				.map(m -> completaModuloCategoria(m))
				.map(m -> completaModuloAttributi(m, ModuloAttributoFilter.WCL))
				.collect(Collectors.toList());
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoModuli] DAOException with filter="+filter, e);
			throw new BusinessException("Errore recupero elenco moduli");
 		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getElencoModuli] END");
			}
		}
	}

	
	@Override
	public Modulo getModuloById(Long idModulo, Long idVersioneModulo, String fields) throws ItemNotFoundBusinessException, BusinessException {
		if(StrUtils.isEmpty(fields)) {
			fields = DEFAULT_FIELDS_MODULO_BY_ID;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getModuloById] BEGIN");
			LOG.debug("[" + CLASS_NAME + "::getModuloById] IN idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  fields="+fields);
		}
		try {
			ModuloVersionatoEntity entity = moduloDAO.findModuloVersionatoById(idModulo, idVersioneModulo);
			Modulo modulo = ModuloMapper.buildFromModuloVersionatoEntity(entity);
			return completeRequestFields(fields, modulo);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Risorsa nont trovata idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] DAOException idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo, e);
			throw new BusinessException("Errore ricerca modulo per id");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Exception idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo, e);
			throw new BusinessException("Errore generica in ricerca modulo per id");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuloById] END");
			}
		}
	}
	private Modulo completeRequestFields(String fields, Modulo result) {
		if (StringUtils.isEmpty(fields)) {
			return result;
		}
		List<String> listFields = Arrays.asList(fields.split(","));
		if ( listFields.contains("struttura")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] struttura");
			result = completaModuloStruttura(result);
		}
		if ( listFields.contains("portali")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] portali");
			result = completaModuloPortali(result);
		}
		if ( listFields.contains("processo")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] processo");
			result = completaModuloProcesso(result);
		}
		if ( listFields.contains("attributiWCL")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] attributiWCL");
			result = completaModuloAttributi(result, ModuloAttributoFilter.WCL);
		}
		if ( listFields.contains("attributiCOSMO")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] attributiCOSMO");
			result = completaModuloAttributi(result, ModuloAttributoFilter.COSMO);
		}
		if ( listFields.contains("attributiEMAIL")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] attributiEMAIL");
			result = completaModuloAttributi(result, ModuloAttributoFilter.EMAIL);
		}
		if ( listFields.contains("attributiTicketCRM")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] attributiTicketCRM");
			result = completaModuloAttributi(result, ModuloAttributoFilter.CRM);
		}
		if ( listFields.contains("attributiEPAY")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] attributiEPAY");
			result = completaModuloAttributi(result, ModuloAttributoFilter.EPAY);
		}
		return result;
	}
	
	
	@Override
	public Modulo getModuloByCodice(String codiceModulo, String versione) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] BEGIN");
			LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] IN codiceModulo="+codiceModulo+"  versione="+versione);
		}
		try {
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoByCodice(codiceModulo, versione);
			ModuloStrutturaEntity struttura = strutturaDAO.findByIdVersioneModulo(moduloE.getIdVersioneModulo());

			Modulo modulo = ModuloMapper.buildFromModuloVersionatoEntity(moduloE);
			modulo.setStruttura(struttura.getStruttura());
			modulo.setTipoStruttura(struttura.getTipoStruttura());
			modulo.setIdModuloStruttura(struttura.getIdModuloStruttura());
			return completaModuloAttributi(modulo, ModuloAttributoFilter.WCL);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] Risorsa nont trovata codiceModulo="+codiceModulo+"  versione="+versione);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] DAOException codiceModulo="+codiceModulo+"  versione="+versione, e);
			throw new BusinessException("Errore ricerca modulo per codice");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] END");
			}
		}
	}

	@Override
	public Modulo getModuloPubblicatoByCodice(String codiceModulo) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] BEGIN");
			LOG.debug("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] IN codiceModulo="+codiceModulo);
		}
		try {
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoPubblicatoByCodice(codiceModulo);
			ModuloStrutturaEntity struttura = strutturaDAO.findByIdVersioneModulo(moduloE.getIdVersioneModulo());

			Modulo modulo = ModuloMapper.buildFromModuloVersionatoEntity(moduloE);
			modulo.setStruttura(struttura.getStruttura());
			modulo.setTipoStruttura(struttura.getTipoStruttura());
			modulo.setIdModuloStruttura(struttura.getIdModuloStruttura());
			return completaModuloAttributi(modulo, ModuloAttributoFilter.WCL);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] ItemNotFoundDAOException for codiceModulo="+codiceModulo, notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] DAOException for codiceModulo="+codiceModulo, e);
			throw new BusinessException("Errore ricerca modulo pubblicato per codice");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] END");
			}
		}
	}
	
	/**
	 * Completa l'oggetto Modulo della struttura che servire per gli applicativi client WCL
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloStruttura(Modulo modulo) throws DAOException {
		ModuloStrutturaEntity struttura = strutturaDAO.findByIdVersioneModulo(modulo.getIdVersioneModulo());
		modulo.setStruttura(struttura.getStruttura());
		modulo.setTipoStruttura(struttura.getTipoStruttura());
		modulo.setIdModuloStruttura(struttura.getIdModuloStruttura());
		return modulo;
	}
	
	/**
	 * Completa l'oggetto Modulo dei attributi che possono servire per gli applicativi client WCL
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloAttributi(Modulo modulo, ModuloAttributoFilter filter) throws DAOException {
		modulo.setAttributi(
			moduloAttributiDAO.findByIdModuloFilter(modulo.getIdModulo(), filter).stream()
				.map(ModuloAttributoMapper::remap)
				.collect(Collectors.toList()));
		return modulo;
	}

	private Modulo completaModuloCategoria(Modulo modulo) throws DAOException {
		modulo.setCategoria(
			categoriaDAO.findByIdModulo(modulo.getIdModulo()).stream()
				.findFirst()
				.map(CategoriaMapper::buildFromCategoriaEntity)
				.orElse(null));
		return modulo;
	}

	/**
	 * Completa l'oggetto Modulo del suo processo
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloProcesso(Modulo modulo) throws DAOException {
		modulo.setProcesso(processiService.getProcessoByIdModulo(modulo.getIdModulo()));
		return modulo;
	}
	
	/**
	 * Completa l'oggetto Modulo dei portali di pubblicazione
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloPortali(Modulo modulo) {
		modulo.setPortali(
			portaleDAO.findByIdModulo(modulo.getIdModulo()).stream()
				.map(PortaleMapper::buildFromEntity)
				.collect(Collectors.toList()));
		return modulo;
	}
	
	@Override
	public String getStrutturaByIdStruttura(Long idStruttura) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getStrutturaByIdStruttura] BEGIN");
		}
		try {
			ModuloStrutturaEntity struttura = strutturaDAO.findById(idStruttura);
			return struttura.getStruttura();
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Risorsa nont trovata idStruttura="+idStruttura);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] DAOException idStruttura="+idStruttura, e);
			throw new BusinessException("Errore ricerca struttura per id");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getStrutturaByIdStruttura] END");
			}
		}
	}
	
	@Override
	@Transactional
	public Modulo insertModulo(UserInfo user, Modulo modulo) throws BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::insertModulo] BEGIN");
		}
		Date now = new Date();
		String attoreIns = user.getIdentificativoUtente();

		// MODULO
		ModuloEntity moduloEntity = new ModuloEntity();
		moduloEntity.setCodiceModulo(modulo.getCodiceModulo() );
		moduloEntity.setAttoreUpd(attoreIns);
		moduloEntity.setDataIns(now);
		moduloEntity.setDataUpd(now); // NOT NULL !
		moduloEntity.setDescrizioneModulo(modulo.getDescrizioneModulo() );
		moduloEntity.setFlagIsRiservato("N");
		moduloEntity.setFlagProtocolloIntegrato("N");
		moduloEntity.setIdTipoCodiceIstanza(modulo.getIdTipoCodiceIstanza());
		moduloEntity.setOggettoModulo(modulo.getOggettoModulo());
		Long idModulo;
		try {
			idModulo = moduloDAO.insert(moduloEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertModulo] Errore invocazione DAO moduloDAO", e);
			throw new BusinessException("Errore inserimento modulo");
		}
		modulo.setIdModulo(idModulo);

		// VERSIONE MODULO
		ModuloVersioneEntity versioneEntity = new ModuloVersioneEntity();
		versioneEntity.setIdModulo(idModulo);
		versioneEntity.setVersioneModulo(modulo.getVersioneModulo());
		versioneEntity.setDataUpd(now);
		versioneEntity.setAttoreUpd(attoreIns);
		Long idVersioneModulo;
		try {
			idVersioneModulo = moduloDAO.insertVersione(versioneEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertVersione] Errore invocazione DAO moduloDAO", e);
			throw new BusinessException("Errore inserimento Versione");
		}
		modulo.setIdVersioneModulo(idVersioneModulo);
		
		// CRONOLOGIA
		ModuloCronologiaStatiEntity cron = new ModuloCronologiaStatiEntity();
		cron.setIdVersioneModulo(idVersioneModulo);
		cron.setIdStato(DecodificaStatoModulo.IN_COSTRUZIONE.getId());
		cron.setDataInizioValidita(now);
		cron.setDataFineValidita(null);
		try {
			Long idCron = moduloDAO.insertCronologia(cron);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertModulo] Errore invocazione DAO cronologiaDAO", e);
			throw new BusinessException("Errore inserimento modulo-cronologia");
		}
		
		
		// Progressivo
		ModuloProgressivoEntity progressivo = new ModuloProgressivoEntity();
		progressivo.setIdModulo(idModulo);
		progressivo.setIdTipoCodiceIstanza(modulo.getIdTipoCodiceIstanza());
		progressivo.setProgressivo(0L);
		progressivo.setAnnoRiferimento(Calendar.getInstance().get(Calendar.YEAR));
		progressivo.setLunghezza(7);
		try {
			moduloProgressivoDAO.insert(progressivo);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertModulo] Errore invocazione DAO progressivoDAO", e);
			throw new BusinessException("Errore inserimento progressivo modulo");
		}
		
		// STRUTTURA
		ModuloStrutturaEntity struttura = new ModuloStrutturaEntity();
		struttura.setIdVersioneModulo(idVersioneModulo);
		struttura.setIdModulo(idModulo);
		struttura.setTipoStruttura(modulo.getTipoStruttura());
		struttura.setStruttura(modulo.getStruttura());
		try {
			Long idStruttura = strutturaDAO.insert(struttura);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertModulo] Errore invocazione DAO strutturaDAO", e);
			throw new BusinessException("Errore inserimento modulo-struttura");
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::insertModulo] END");
		}
		return modulo;
	}

	@Override
	@Transactional
	public Modulo updateModulo(UserInfo user, Modulo modulo) throws BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::updateModulo] BEGIN");
		}

		Date now = new Date();
		String attoreIns = user.getIdentificativoUtente();

		// MODULO
		ModuloEntity moduloEntity = new ModuloEntity();
		moduloEntity.setIdModulo(modulo.getIdModulo());
		moduloEntity.setCodiceModulo(modulo.getCodiceModulo() );
		moduloEntity.setAttoreUpd(attoreIns);
		moduloEntity.setDataIns(modulo.getDataIns());
		moduloEntity.setDataUpd(now); // NOT NULL !
		moduloEntity.setDescrizioneModulo(modulo.getDescrizioneModulo() );
		moduloEntity.setFlagIsRiservato(modulo.isFlagIsRiservato() ? "S" : "N");
		moduloEntity.setFlagProtocolloIntegrato(modulo.isFlagProtocolloIntegrato() ? "S" : "N");
		moduloEntity.setIdTipoCodiceIstanza(modulo.getIdTipoCodiceIstanza());
		moduloEntity.setOggettoModulo(modulo.getOggettoModulo());
		try {
			moduloDAO.update(moduloEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::updateModulo] Errore invocazione DAO moduloDAO",e);
			throw new BusinessException("Errore aggiornamento modulo");
		}
		
		// VERSIONE MODULO
		ModuloVersioneEntity versioneEntity = new ModuloVersioneEntity();
		versioneEntity.setIdVersioneModulo(modulo.getIdVersioneModulo());
		versioneEntity.setIdModulo(modulo.getIdModulo());
		versioneEntity.setVersioneModulo(modulo.getVersioneModulo());
		versioneEntity.setDataUpd(now);
		versioneEntity.setAttoreUpd(attoreIns);
		try {
			moduloDAO.updateVersione(versioneEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::updateVersione] Errore invocazione DAO moduloDAO", e);
			throw new BusinessException("Errore aggiornamento versione");
		}
		
		// STRUTTURA
		ModuloStrutturaEntity struttura = new ModuloStrutturaEntity();
		struttura.setIdModuloStruttura(modulo.getIdModuloStruttura());
		struttura.setIdVersioneModulo(modulo.getIdVersioneModulo());
		struttura.setIdModulo(modulo.getIdModulo());
		struttura.setTipoStruttura(modulo.getTipoStruttura());
		struttura.setStruttura(modulo.getStruttura());
		try {
			strutturaDAO.update(struttura);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::updateModulo] Errore invocazione DAO strutturaDAO", e);
			throw new BusinessException("Errore aggiornamento struttura");
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::updateModulo] END");
		}
		return modulo;
	}

	@Override
	@Transactional
	public Modulo cambiaStato(UserInfo user, Long idModulo, Long idVersioneModulo, DecodificaStatoModulo newStato) throws BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::cambiaStato] BEGIN");
			LOG.debug("[" + CLASS_NAME + "::cambiaStato] IN idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  newStato="+newStato.getCodice());
		}
		try {
			ModuloCronologiaStatiEntity currentCron = moduloDAO.findCurrentCronologia(idVersioneModulo);
			
			switch (newStato) {
				case IN_TEST:
					if (!DecodificaStatoModulo.IN_COSTRUZIONE.getId().equals(currentCron.getIdStato()) &&
						!DecodificaStatoModulo.IN_MODIFICA.getId().equals(currentCron.getIdStato())) {
						LOG.error("[" + CLASS_NAME + "::cambiaStato] Stato modulo non INIT or MOD :: impossibile metterlo in TEST idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
						throw new BusinessException("Stato modulo non INIT or MOD :: impossibile metterlo in TEST");
					}
					break;
				case PUBBLICATO:
					if (!DecodificaStatoModulo.IN_TEST.getId().equals(currentCron.getIdStato()) &&
						!DecodificaStatoModulo.SOSPESO.getId().equals(currentCron.getIdStato())) {
						LOG.error("[" + CLASS_NAME + "::cambiaStato] Stato modulo non IN TEST or SOSP :: impossibile pubblicarlo idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
						throw new BusinessException("Stato modulo non IN TEST or SOSP :: impossibile pubblicarlo");
					}
					break;
				case IN_MODIFICA:
					if (!DecodificaStatoModulo.PUBBLICATO.getId().equals(currentCron.getIdStato()) &&
						!DecodificaStatoModulo.SOSPESO.getId().equals(currentCron.getIdStato())) {
						LOG.error("[" + CLASS_NAME + "::cambiaStato] Stato modulo non PUBB or SOSP :: impossibile metterlo in MOD idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
						throw new BusinessException("Stato modulo non PUBB or SOSP :: impossibile metterlo in MOD");
					}
					break;
				case SOSPESO:
					if (!DecodificaStatoModulo.PUBBLICATO.getId().equals(currentCron.getIdStato()) ) {
						LOG.error("[" + CLASS_NAME + "::cambiaStato] Stato modulo non PUBBLICATO :: impossibile metterlo in SOSPESO idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
						throw new BusinessException("Stato modulo non PUBBLICATO :: impossibile metterlo in SOSPESO");
					}
					break;
				case RITIRATO_DA_PUBBLICAZIONE:
					if (!DecodificaStatoModulo.PUBBLICATO.getId().equals(currentCron.getIdStato()) &&
						!DecodificaStatoModulo.SOSPESO.getId().equals(currentCron.getIdStato())) {
						LOG.error("[" + CLASS_NAME + "::cambiaStato] Stato modulo non PUBB or SOSP :: impossibile metterlo in RITIRATO idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
						throw new BusinessException("Stato modulo non PUBB or SOSP :: impossibile metterlo in RITIRATO");
					}
					break;
				case ELIMINATO:
					if (!DecodificaStatoModulo.IN_TEST.getId().equals(currentCron.getIdStato()) &&
						!DecodificaStatoModulo.PUBBLICATO.getId().equals(currentCron.getIdStato()) &&
						!DecodificaStatoModulo.SOSPESO.getId().equals(currentCron.getIdStato()) &&
						!DecodificaStatoModulo.RITIRATO_DA_PUBBLICAZIONE.getId().equals(currentCron.getIdStato())) {
						LOG.error("[" + CLASS_NAME + "::cambiaStato] Stato modulo non PUBB or SOSP :: impossibile metterlo in MOD idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
						throw new BusinessException("Stato modulo non PUBB or SOSP :: impossibile metterlo in MOD");
					}
					break;
				default:
					LOG.error("[" + CLASS_NAME + "::cambiaStato] Cambio Stato modulo non previsto idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"newStatoCodice:"+newStato.getCodice());
					throw new BusinessException("Cambio Stato modulo non previsto");
			}
			
			
			Date now = new Date();
			boolean aggiornaDataFineOfCurrentCron = currentCron.getDataFineValidita()==null?true:false;
			if (currentCron.getDataFineValidita()!=null && currentCron.getDataFineValidita().after(now)) {
				if(existsOverFutureCron(currentCron)) {
					LOG.error("[" + CLASS_NAME + "::cambiaStato] Altri stati prenotati nel futuro  per idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
					throw new BusinessException("Errore Cambio Stato Modulo");
				}
				aggiornaDataFineOfCurrentCron = true;
			}
			if (aggiornaDataFineOfCurrentCron) {
				currentCron.setDataFineValidita(now);
				int r = moduloDAO.updateCronologia(currentCron);
			}
			
			ModuloCronologiaStatiEntity newCron = new ModuloCronologiaStatiEntity();
			newCron.setIdVersioneModulo(idVersioneModulo);
			newCron.setIdStato(newStato.getId());
			newCron.setDataInizioValidita(now);
			newCron.setDataFineValidita(null);					
			Long idNewCron = moduloDAO.insertCronologia(newCron);
			return getModuloById(idModulo, idVersioneModulo, DEFAULT_FIELDS_MODULO_BY_ID);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::cambiaStato] DAOException idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  newStato="+newStato.getCodice(), e);
			throw new BusinessException("Errore Cambio Stato Modulo");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::cambiaStato] END");
			}
		}
	}


	private boolean existsOverFutureCron(ModuloCronologiaStatiEntity currentCron) {
		// TODO Verificare se exists altri record di cronologie oltre la data fine di quello passato in argument
		return false;
	}



	private String toJson (List<ModuloAttributoEntity> moduloAttributi) throws BusinessException {
		try {
			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.writeStartObject();
		    g.writeObjectFieldStart("attributi");
			for (ModuloAttributoEntity modAttr : moduloAttributi) {
			    g.writeStringField(modAttr.getNomeAttributo(), modAttr.getValore());
			}
		    g.writeEndObject();
		    g.writeEndObject();
		    g.close();
		    return writer.toString();
		} catch(Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::toJson] Errore", e);
	    	throw new BusinessException("ERROR ModuliServiceImpl.toJson");
	    }
	}
	
	
	@Override
	public List<CampoModulo> getCampiModulo(Long idModulo, Long idVersioneModulo, String type) throws ItemNotFoundBusinessException, BusinessException {
		return getCampiModulo(idModulo, idVersioneModulo, "N", type);
	}
	
	/**
	 * Estrai l'elenco dei campi di un modulo via parsing del json di struttura
	 */
	@Override
	public List<CampoModulo> getCampiModulo(Long idModulo, Long idVersioneModulo, String onlyFirstLevel, String type) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getCampiModulo] BEGIN");
			LOG.debug("[" + CLASS_NAME + "::getCampiModulo] IN idModulo: "+idModulo);
			LOG.debug("[" + CLASS_NAME + "::getCampiModulo] IN idVersioneModulo: "+idVersioneModulo);
			LOG.debug("[" + CLASS_NAME + "::getCampiModulo] IN onlyFirstLevel: "+onlyFirstLevel);
			LOG.debug("[" + CLASS_NAME + "::getCampiModulo] IN type: "+type);
		}
		List<CampoModulo> campi = new ArrayList<>();
		try {
			ModuloStrutturaEntity strutturaEntity = strutturaDAO.findByIdVersioneModulo(idVersioneModulo);
			campi = new CampiModuloMapper().getCampi(strutturaEntity.getStruttura(), "S".equalsIgnoreCase(onlyFirstLevel), type);
			return campi;	
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getCampiModulo] ItemNotFoundDAOException idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  onlyFirstLevel="+onlyFirstLevel+"  type="+type);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getCampiModulo] DAOException idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  onlyFirstLevel="+onlyFirstLevel+"  type="+type, e);
			throw new BusinessException("Errore recupero elenco campi modulo");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCampiModulo] Exception idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  onlyFirstLevel="+onlyFirstLevel+"  type="+type, e);
			throw new BusinessException("Errore recupero elenco campi modulo");
 		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getCampiModulo] END");
			}
		}
	}
	
	/**
	 * Estrai l'elenco dei campi di un modulo DatiAzione via parsing del json di struttura
	 */
	@Override
	public List<CampoModulo> getCampiDatiAzione(Long idDatiAzione, String onlyFirstLevel, String type) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getCampiDatiAzione] BEGIN");
			LOG.debug("[" + CLASS_NAME + "::getCampiDatiAzione] IN idDatiAzione: "+idDatiAzione);
			LOG.debug("[" + CLASS_NAME + "::getCampiDatiAzione] IN onlyFirstLevel: "+onlyFirstLevel);
			LOG.debug("[" + CLASS_NAME + "::getCampiDatiAzione] IN type: "+type);
		}
		List<CampoModulo> campi = new ArrayList<>();
		try {
			DatiAzioneEntity datiAzioneEntity = workflowDAO.findDatiAzioneById(idDatiAzione);
			campi = new CampiModuloMapper().getCampi(datiAzioneEntity.getStruttura(), "S".equalsIgnoreCase(onlyFirstLevel), type);
			return campi;	
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getCampiDatiAzione] ItemNotFoundDAOException idDatiAzione="+idDatiAzione+"  onlyFirstLevel="+onlyFirstLevel+"  type="+type);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getCampiDatiAzione] DAOException idDatiAzione="+idDatiAzione+"  onlyFirstLevel="+onlyFirstLevel+"  type="+type, e);
			throw new BusinessException("Errore recupero elenco campi DatiAzione");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCampiDatiAzione] Exception idDatiAzione="+idDatiAzione+"  onlyFirstLevel="+onlyFirstLevel+"  type="+type, e);
			throw new BusinessException("Errore recupero elenco campi DatiAzione");
 		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getCampiDatiAzione] END");
			}
		}
	}
	
	@Override
	public Long getIdModuloByCodice(String codiceModulo) throws ItemNotFoundBusinessException, BusinessException {
		try {
			return moduloDAO.findByCodice(codiceModulo).getIdModulo();
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIdModuloByCodice] Errore invocazione DAO", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getIdModuloByCodice] Errore invocazione DAO", e);
			throw new BusinessException("Errore ricerca IdModulo per codice");
		}
	}


	@Override
	public ReportVerificaAttributiModulo validaAttributiModulo(Long idModulo, String categoriaAttributi)
			throws ItemNotFoundBusinessException, BusinessException {
		try {
			ReportVerificaAttributiModulo report = new ReportVerificaAttributiModulo(idModulo, categoriaAttributi);
			switch(categoriaAttributi) {
				case "PROTOCOLLO":
					report = validaAttributiModuloProtocollo(report);
					break;
				default:
					throw new BusinessException("getModuloAttributoFilterByCategoria NOT IMPLEMENTED for categoriaAttributi = " + categoriaAttributi);
			}
			return report;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::validaAttributiModulo] Errore invocazione DAO", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::validaAttributiModulo] Errore invocazione DAO", e);
			throw new BusinessException("Errore validaAttributiModulo");
		}
	}

	// per STARDAS
	private ReportVerificaAttributiModulo validaAttributiModuloProtocollo(ReportVerificaAttributiModulo report) {
		// ConfModulo mapModuloAttributi
		List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModuloFilter(report.getIdModulo(), ModuloAttributoFilter.PROTOCOLLO);
		LOG.debug("[" + CLASS_NAME + "::validaAttributiModuloProtocollo] attributiModuloE: " + attributiModuloE);
		MapModuloAttributi mapModuloAttributi = new MapModuloAttributi(attributiModuloE);
		Boolean psitProtocollo = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_PROTOCOLLO);
		Boolean psitProtocolloIntegrazione = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_PROTOCOLLO_INTEGRAZIONE);
		Boolean protocollaBo = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PROTOCOLLA_BO);
		Boolean protocollaBoIntegrazione = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PROTOCOLLA_INTEGRAZIONE_BO);
		// ConfProtocollo mapProtocolloAttributi
		AreaModuloEntity areaModuloE = areaModuloDAO.findByIdModulo(report.getIdModulo());
		List<ProtocolloParametroEntity> listAttributi = protocolloParametroDAO.findForModulo(areaModuloE);
		LOG.debug("[" + CLASS_NAME + "::validaAttributiModuloProtocollo] listAttributi: " + listAttributi);
		MapProtocolloAttributi mapProtocolloAttributi = new MapProtocolloAttributi(listAttributi);
		//
		if (Boolean.TRUE.equals(psitProtocollo) || Boolean.TRUE.equals(protocollaBo) || Boolean.TRUE.equals(psitProtocolloIntegrazione) || Boolean.TRUE.equals(protocollaBoIntegrazione)) {
			validaAttributiModuloProtocolloGenerico(report, mapProtocolloAttributi);
		}
		if (Boolean.TRUE.equals(psitProtocollo) || Boolean.TRUE.equals(protocollaBo)) {
			validaAttributiModuloProtocolloIstanza(report, mapProtocolloAttributi);
		}
		if (Boolean.TRUE.equals(psitProtocolloIntegrazione) || Boolean.TRUE.equals(protocollaBoIntegrazione)) {
			validaAttributiModuloProtocolloIntegrazione(report, mapProtocolloAttributi);
		}
		return report;
	}

	protected void validaAttributiModuloProtocolloGenerico(ReportVerificaAttributiModulo report,
			MapProtocolloAttributi mapProtocolloAttributi) {
		LOG.info("[" + CLASS_NAME + "::validaAttributiModuloProtocollo] PSIT_PROTOCOLLO OR PROTOCOLLA_BO ...");
		String sistemaProtocoller = mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.SISTEMA_PROTOCOLLO_ISTANZA);
		if( StringUtils.isBlank(sistemaProtocoller) ) {
			report.addErrorMessagge("SISTEMA_PROTOCOLLO_ISTANZA mancante");
		} else {
			if (!"STARDAS".equals(sistemaProtocoller) /*&& !"STARDAS".equals(sistemaProtocoller) */) {
				report.addErrorMessagge("SISTEMA_PROTOCOLLO_ISTANZA need be: STARDAS");
			}
		}
		if( StringUtils.isBlank(mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_RESPONSABILE_TRATTAMENTO)) ) {
			report.addErrorMessagge("CODICE_FISCALE_RESPONSABILE_TRATTAMENTO mancante");
		}
		if( StringUtils.isBlank(mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.CODICE_FISCALE_ENTE)) ) {
			report.addErrorMessagge("CODICE_FISCALE_ENTE mancante");
		}
		if( StringUtils.isBlank(mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.CODICE_FRUITORE)) ) {
			report.addErrorMessagge("CODICE_FRUITORE mancante");
		}
		if( StringUtils.isBlank(mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.CODICE_APPLICAZIONE)) ) {
			report.addErrorMessagge("CODICE_APPLICAZIONE mancante");
		}
		if (Boolean.TRUE.equals(mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.PROTOCOLLA_ALLEGATI)) &&
			StringUtils.isBlank(mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.CODICE_TIPO_DOCUMENTO_ALLEGATI)) ) {
				report.addErrorMessagge("CODICE_TIPO_DOCUMENTO_ALLEGATI mancante");
		}
	}

	protected void validaAttributiModuloProtocolloIstanza(ReportVerificaAttributiModulo report,
			MapProtocolloAttributi mapProtocolloAttributi) {
		LOG.info("[" + CLASS_NAME + "::validaAttributiModuloProtocollo] PSIT_PROTOCOLLO OR PROTOCOLLA_BO ...");
		if( StringUtils.isBlank(mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.CODICE_TIPO_DOCUMENTO)) ) {
			report.getMessaggi().add("CODICE_TIPO_DOCUMENTO mancante");
		}
	}

	protected void validaAttributiModuloProtocolloIntegrazione(ReportVerificaAttributiModulo report,
			MapProtocolloAttributi mapProtocolloAttributi) {
		LOG.info("[" + CLASS_NAME + "::validaAttributiModuloProtocollo] PSIT_PROTOCOLLO_INTEGRAZIONE OR PROTOCOLLA_INTEGRAZIONE_BO ...");
		if( StringUtils.isBlank(mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.CODICE_TIPO_DOC_INTEGRAZIONE)) ) {
			report.addErrorMessagge("CODICE_TIPO_DOC_INTEGRAZIONE mancante");
		}
		if (Boolean.TRUE.equals(mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.PROTOCOLLA_ALLEGATI)) &&
			StringUtils.isBlank(mapProtocolloAttributi.getWithCorrectType(ProtocolloAttributoKeys.CODICE_TIPO_DOC_INTEGRAZIONE_ALLEGATI)) ) {
				report.addErrorMessagge("CODICE_TIPO_DOC_INTEGRAZIONE_ALLEGATI mancante");
		}
	}

}
