/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.AllegatiSummary;
import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.dto.CampoModulo;
import it.csi.moon.commons.dto.CampoModuloFormioFileName;
import it.csi.moon.commons.dto.ReportVerificaFirma;
import it.csi.moon.commons.entity.AllegatoEntity;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.commons.mapper.AllegatoMapper;
import it.csi.moon.moonfobl.business.service.AllegatiService;
import it.csi.moon.moonfobl.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.util.LoggerAccessor;
/**
 * Metodi di business relativi agli allegati
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class AllegatiServiceImpl implements AllegatiService {
	
	private static final String CLASS_NAME = "AllegatiServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	AllegatoDAO allegatoDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	@Override
	public Allegato getById(Long idAllegato) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getById] BEGIN IN idAllegato="+idAllegato);
		}
		Allegato result = null;
		try {
			AllegatoEntity entity = allegatoDAO.findById(idAllegato);
			if(entity.getContenuto() == null && entity.getUuidIndex() != null) {
				entity.setContenuto(moonsrvDAO.getContenutoIndexByUid(entity.getUuidIndex()));
			}
			result = AllegatoMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getById] idAllegato="+idAllegato);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca allegato per id");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getById] END result="+result);
			}
		}
	}

	@Override
	public Allegato getByFormIoNameFile(String formioNameFile) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getByFormIoNameFile] BEGIN IN formioNameFile="+formioNameFile);
		}
		Allegato result = null;
		try {
			AllegatoEntity entity = allegatoDAO.findByFormIoNameFile(formioNameFile);
			if(entity.getContenuto() == null && entity.getUuidIndex() != null) {
				entity.setContenuto(moonsrvDAO.getContenutoIndexByUid(entity.getUuidIndex()));
			}
			result = AllegatoMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getByFormIoNameFile] formioNameFile="+formioNameFile);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getByFormIoNameFile] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca allegato per FormIoNameFile");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getByFormIoNameFile] END result="+result);
			}
		}
	}

	@Override
	public Allegato getByCodice(String codiceFile) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getByCodice] BEGIN IN codiceFile="+codiceFile);
		}
		Allegato result = null;
		try {
			AllegatoEntity entity = allegatoDAO.findByCodice(codiceFile);
			if(entity.getContenuto() == null && entity.getUuidIndex() != null) {
				entity.setContenuto(moonsrvDAO.getContenutoIndexByUid(entity.getUuidIndex()));
			}
			result = AllegatoMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getByCodice] codiceFile="+codiceFile);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getByCodice] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca allegato per Codice");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getByCodice] END result="+result);
			}
		}
	}

	@Override
	public Allegato getByIdIstanzaCodice(Long idIstanza, String codiceFile) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getByIdIstanzaCodice] BEGIN IN idIstanza="+idIstanza+"  codiceFile="+codiceFile);
		}
		Allegato result = null;
		try {
			AllegatoEntity entity = allegatoDAO.findByIdIstanzaCodice(idIstanza,codiceFile);
			result = AllegatoMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getByIdIstanzaCodice] idIstanza="+idIstanza+"  codiceFile="+codiceFile);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getByIdIstanzaCodice] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca allegato per IdIstanzaCodice");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getByIdIstanzaCodice] END result="+result);
			}
		}
	}

	/**
	 * Deprecated, usare findLazyByIdIstanza che non torna il contenuto.
	 */
	@Deprecated
	@Override
	public List<Allegato> findByIdIstanza(Long idIstanza) throws BusinessException {
		try {
			List<Allegato> result = allegatoDAO.findByIdIstanza(idIstanza).stream()
					.map(AllegatoMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanza] Errore generico servizio findByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco Allegati");
		} 
	}

	@Override
	public List<Allegato> findLazyByIdIstanza(Long idIstanza) throws BusinessException {
		try {
			List<Allegato> result = allegatoDAO.findLazyByIdIstanza(idIstanza).stream()
					.map(AllegatoMapper::buildFromLazyEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::findLazyByIdIstanza] Errore generico servizio findLazyByIdIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco AllegatiLazy");
		} 
	}

	@Override
	public Long insert(Allegato allegato) throws BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::insert] BEGIN IN allegato="+allegato);
		}
		Long idAllegato = null;
		try {
			AllegatoEntity entity = AllegatoMapper.buildFromObj(allegato);
			
			entity.setCodiceFile(UUID.randomUUID().toString()); // CodiceFile MOOn UUID usato per comunicazione a sistemi esterni (Protocolo)
			entity.setEstensione(extractEstensione(allegato));
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(allegato.getContenuto());
	        entity.setHashFile(Base64.getEncoder().encodeToString(hash));
	        entity.setLunghezza(allegato.getContenuto().length);
	        entity.setUuidIndex(null);
	        entity.setDataCreazione(new Date());
	        entity.setFlFirmato(allegato.isFlagFirmato()?"S":"N");
	        idAllegato = allegatoDAO.insert(entity);
	        return idAllegato;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore inserimento allegato");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore invocazione (MessageDigest) ", e);
			throw new BusinessException("Errore inserimento allegato");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::insert] END result="+idAllegato);
			}
		}
	}

	private String extractEstensione(Allegato allegato) {
		String result = FilenameUtils.getExtension(allegato.getNomeFile());
		return result!=null?result.toLowerCase():null;
	}
	
	@Override
	public int delete(Long idAllegato) throws BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::delete] BEGIN IN idAllegato="+idAllegato);
		}
		Allegato result = null;
		try {
			return allegatoDAO.delete(idAllegato);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore delete allegato");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::delete] END result="+result);
			}
		}
	}

	@Override
	public int update(Allegato allegato) throws BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::update] BEGIN IN allegato="+allegato);
		}
		Allegato result = null;
		try {
			return allegatoDAO.update(AllegatoMapper.buildFromObj(allegato));
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore inserimento allegato");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::update] END result="+result);
			}
		}
	}

	@Override
	public int updateIdIstanza(CampoModuloFormioFileName campoFormioNomeFile, Long idIstanza) throws BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::updateIdIstanza] BEGIN IN campoFormioNomeFile="+campoFormioNomeFile+" idIstanza="+idIstanza);
		}
		Allegato result = null;
		try {
			return allegatoDAO.updateIdIstanza(campoFormioNomeFile,idIstanza);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::updateIdIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore aggiornamento idIstanza");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateIdIstanza] END result="+result);
			}
		}
	}

	@Override
	public int resetIdIstanza(Long idIstanza) throws BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::resetIdIstanza] BEGIN IN idIstanza="+idIstanza);
		}
		int result = 0;
		try {
			result = allegatoDAO.resetIdIstanza(idIstanza);
			return result;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::resetIdIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore resetIdIstanza allegato");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::resetIdIstanza] END result="+result);
			}
		}
	}

	@Override
	public void deleteAllegatoByNameFormio(String nomeFile) {
		try {
			allegatoDAO.deleteAllegatoByNameFormio(nomeFile);
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::deleteAllegatoByNameFormio] File allegato non trovato!", e);
			throw new BusinessException("Errore deleteAllegatoByNameFormio ");
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::deleteAllegatoByNameFormio] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore deleteAllegatoByNameFormio");
		}
	}

	@Override
	public Map<String, String> duplicaAllegatiOfIstanza(long idIstanza, long idModulo, long idVersioneModulo, boolean versioniDiverse,
			String ipAddress) throws BusinessException {
		List<AllegatoLazyEntity> allegatiDaDuplicare = retrieveAllegatiDaDuplicare(idIstanza, idModulo, idVersioneModulo, versioniDiverse);
		Map<String,String> mapResultNuoviNameFormio = inserisciAllegatiConCampiAggiornati(allegatiDaDuplicare, ipAddress);
		return mapResultNuoviNameFormio;
	}

	private List<AllegatoLazyEntity> retrieveAllegatiDaDuplicare(long idIstanza, long idModulo, long idVersioneModulo, boolean versioniDiverse) {
		List<AllegatoLazyEntity> allegati = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::retrieveAllegatiDaDuplicare] ");
			if(versioniDiverse == true) {
				allegati = allegatoDAO.findLazyByIdIstanza(idIstanza);
				List<CampoModulo> campiFile = moonsrvDAO.getCampiModulo(idModulo, idVersioneModulo, "file");
				allegati = new DatiIstanzaHelper().verificaMatchFullKeyAllegati(allegati,campiFile);
			} else {
				allegati = allegatoDAO.findLazyByIdIstanza(idIstanza);
			}
			return allegati;
		} catch (DAOException d) {
			LOG.error("[" + CLASS_NAME + "::retrieveAllegatiDaDuplicare] moonsrvDAO.getCampiModulo  ", d);
			throw new BusinessException("Errore duplica istanza ","MOONFOBL-100200");
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::retrieveAllegatiDaDuplicare] BusinessException ", be);
			throw be;
		}
	}

	private Map<String,String> inserisciAllegatiConCampiAggiornati(List<AllegatoLazyEntity> allegati, String ipAddress) {
		String newName = null;
		Map<String,String> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::inserisciAllegatiConCampiAggiornati] ");
			result = new HashMap<>();
			for (AllegatoLazyEntity a : allegati) {
				newName = generaNuovoFormioFileName(a);
				result.put(a.getFormioNameFile(), newName);
				AllegatoLazyEntity newA = new AllegatoLazyEntity(a);
				newA.setUuidIndex(null);
				newA.setIdAllegato(null);
				newA.setIdIstanza(null);
				newA.setCodiceFile(UUID.randomUUID().toString());
				newA.setDataCreazione(new Date());
				newA.setIpAddress(ipAddress);
				newA.setFormioNameFile(newName);
		        Long newIdAllegato = allegatoDAO.insertWithCopyContent(newA, a.getIdAllegato());
		        newA.setIdAllegato(newIdAllegato);
				if (a.getUuidIndex()!=null) {
					byte[] bytes = moonsrvDAO.getContenutoIndexByUid(a.getUuidIndex());
					allegatoDAO.updateContenuto(newIdAllegato, bytes);
				}
			}
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::inserisciAllegatiConCampiAggiornati] BusinessException ", be);
			throw be;
		}
		return result;
	}

	private String generaNuovoFormioFileName(AllegatoLazyEntity a) {
		try {
			LOG.debug("[" + CLASS_NAME + "::generaNuovoFormioFileName] ");
			String name = a.getNomeFile();
			String est = a.getEstensione();
			
			if (name.contains(".")) {
				 int sepPos = name.lastIndexOf(".");
				 return name.substring(0,sepPos)+"-"+UUID.randomUUID().toString()+"."+est;
			}	
		}catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::generaNuovoFormioFileName]  ", e);
			throw new BusinessException("Errore generaNuovoFormioFileName ","MOONFOBL-100200");
		}
		return UUID.randomUUID().toString();
	}

	@Override
	public AllegatiSummary getAllegatiSummary(Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getAllegatiSummary] IN idIstanza: " + idIstanza);
			return allegatoDAO.selectSummary(idIstanza);
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getAllegatiSummary] DAOException ", daoe);
			throw new BusinessException(daoe);
		}
	}

	//
	// ContentTypes
	//
	@Override
	public String validaBytesContentTypesFully(byte[] bytes, String[] contentTypesArr) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::validaBytesContentTypesFully] IN contentTypesArr: " + String.join(", ", contentTypesArr));
			if (contentTypesArr==null || contentTypesArr.length==0) {
				return null;
			}
			String contentType = retrieveContentType(bytes);
			LOG.info("[" + CLASS_NAME + "::validaBytesContentTypesFully] contentType: " + contentType);
			boolean validateContentType = Arrays.asList(contentTypesArr).contains(contentType);
			if (!validateContentType) {
				LOG.error("[" + CLASS_NAME + "::validaBytesContentTypesFully] " + String.join(", ", contentTypesArr) + " NOT contains: " + contentType);
				throw new BusinessException("ContentType non valido, richiesto contentTypes: " + String.join(", ", contentTypesArr), "MOONFOBL-10032");
			}
			LOG.debug("[" + CLASS_NAME + "::validaBytesContentTypesFully] " + contentType + " find in " + String.join(", ", contentTypesArr));
			return contentType;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::validaBytesContentTypesFully] " + be);
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::validaBytesContentTypesFully] Exception ", e);
			throw new BusinessException("ContentType non valido", "MOONFOBL-10030");
		}
	}
	
	@Override
	public String validaBytesContentTypesContains(byte[] bytes, String[] filterTypesArr) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::validaBytesContentTypesContains] IN filterTypesArr: " + String.join(", ", filterTypesArr));
			if (bytes==null || bytes.length==0) {
				LOG.error("[" + CLASS_NAME + "::validaBytesContentTypesContains] Contenuto bytes null");
	            throw new BusinessException("Contenuto bytes null");
	        }
			if (filterTypesArr==null || filterTypesArr.length==0) {
				return null;
			}
			String contentType = retrieveContentType(bytes);
			LOG.info("[" + CLASS_NAME + "::validaBytesContentTypesContains] contentType: " + contentType);
			boolean validateContentType = stringContainsItemFromList(contentType, filterTypesArr);
			if (!validateContentType) {
				LOG.error("[" + CLASS_NAME + "::validaBytesContentTypesContains] " + String.join(", ", filterTypesArr) + " NOT found in " + contentType);
				throw new BusinessException("ContentType non valido, richiesto types: " + String.join(", ", filterTypesArr), "MOONFOBL-10033");
			}
			LOG.debug("[" + CLASS_NAME + "::validaBytesContentTypesContains] " + String.join(", ", filterTypesArr) + " found in " + contentType);
			return contentType;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::validaBytesContentTypesContains] " + be);
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::validaBytesContentTypesContains] Exception ", e);
			throw new BusinessException("ContentType non valido", "MOONFOBL-10030");
		}
	}

	public static boolean stringContainsItemFromList(String inputStr, String[] items) {
	    return Arrays.stream(items).anyMatch(inputStr::contains);
	}
	
	public String retrieveContentType(byte[] bytes) throws BusinessException {
	    try {
			if (bytes==null || bytes.length==0) {
	            throw new BusinessException("bytes null");
	        }
		    BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(bytes));
		    Metadata md = new Metadata();
			return new Tika().detect(bis, md);
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::retreiveContentType] IOException ", e);
			throw new BusinessException("ContentType non rilevato", "MOONFOBL-10031");
		}
	}
	
	//
	// Signed
	//
	private enum SignedFilter { SIGNED, NOT_SIGNED, ALL, PDF_SIGNED, PDF_NOT_SIGNED, PDF_ALL };
	@Override
	public boolean validaBytesSigned(byte[] bytes, String signed, String contentTypeNullable) throws BusinessException {
		boolean resultFlagFirmato = false;
		ReportVerificaFirma reportFirma = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::validaBytesSigned] IN signed: " + signed + " contentTypeNullable: " + contentTypeNullable);
			if (bytes==null || bytes.length==0) {
				LOG.error("[" + CLASS_NAME + "::validaBytesSigned] Contenuto bytes null");
	            throw new BusinessException("Contenuto bytes null", "MOONFOBL-10036");
	        }
			if (StringUtils.isEmpty(signed)) {
				return false;
			}
			SignedFilter signedFilter = retrieveSignedFilter(signed);
			if (SignedFilter.PDF_SIGNED.equals(signedFilter) || SignedFilter.PDF_NOT_SIGNED.equals(signedFilter) || SignedFilter.PDF_ALL.equals(signedFilter)) {
				if (StringUtils.isEmpty(contentTypeNullable) || !contentTypeNullable.contains("pdf")) {
					return false;
				}
			}
			reportFirma = moonsrvDAO.verificaFirmaByContenuto(bytes);
			if (reportFirma==null) {
				LOG.warn("[" + CLASS_NAME + "::validaBytesSigned] signed:" + signed + " - Impossibile verificare la presenza/assenza di firma.");
			}
			LOG.info("[" + CLASS_NAME + "::validaBytesSigned] reportFirma: " + reportFirma);
			resultFlagFirmato = reportFirma!=null?reportFirma.isFirmato():false;
			switch (signedFilter) {
				case SIGNED:
				case PDF_SIGNED:
					if (reportFirma!=null && !resultFlagFirmato) {
						LOG.error("[" + CLASS_NAME + "::validaBytesSigned] signed:" + signed + " reportFirma:" + reportFirma);
						throw new BusinessException("Documento non firmato, DEVE essere firmato", "MOONFOBL-10037");
					}
					break;
				case NOT_SIGNED:
				case PDF_NOT_SIGNED:
					if (reportFirma!=null && resultFlagFirmato) {
						LOG.error("[" + CLASS_NAME + "::validaBytesSigned] signed:" + signed + " reportFirma:" + reportFirma);
						throw new BusinessException("Documento Firmato, DEVE essere NON firmato", "MOONFOBL-10038");
					}
					break;
				default: // ALL || PDF_ALL
			}
			LOG.debug("[" + CLASS_NAME + "::validaBytesSigned] signed:" + signed + " reportFirma:" + reportFirma);
			LOG.info("[" + CLASS_NAME + "::validaBytesSigned] resultFlagFirmato:" + resultFlagFirmato);
			return resultFlagFirmato;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::validaBytesSigned] " + be);
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::validaBytesSigned] Exception ", e);
			throw new BusinessException("Errore di verifica signature", "MOONFOBL-10035");
		}
	}

	private SignedFilter retrieveSignedFilter(String signed) {
		if ("true".equalsIgnoreCase(signed) || "s".equalsIgnoreCase(signed) || "y".equalsIgnoreCase(signed)) {
			return SignedFilter.SIGNED;
		}
		if ("false".equalsIgnoreCase(signed) || "n".equalsIgnoreCase(signed)) {
			return SignedFilter.NOT_SIGNED;
		}
		if ("pdf_true".equalsIgnoreCase(signed)) {
			return SignedFilter.PDF_SIGNED;
		}
		if ("pdf_false".equalsIgnoreCase(signed)) {
			return SignedFilter.PDF_NOT_SIGNED;
		}
		if ("pdf_all".equalsIgnoreCase(signed)) {
			return SignedFilter.PDF_ALL;
		}
		return SignedFilter.ALL;
	}

}