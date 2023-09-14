/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.dosign.cxfclient.SignedBuffer;
import it.csi.dosign.cxfclient.VerifyReport;
import it.csi.moon.commons.dto.ReportVerificaFirma;
import it.csi.moon.commons.entity.AllegatoEntity;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.util.decodifica.DecodificaFormatoFirma;
import it.csi.moon.commons.util.decodifica.DecodificaTipoFirma;
import it.csi.moon.moonsrv.business.service.AllegatiService;
import it.csi.moon.moonsrv.business.service.FirmaDigitaleService;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dosign.DoSignDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class FirmaDigitaleServiceImpl implements FirmaDigitaleService {

	private static final String CLASS_NAME = "FirmaDigitaleServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Qualifier("apimint")
	@Autowired
	DoSignDAO doSignDAO;
	@Autowired
	AllegatoDAO allegatoDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	AllegatiService allegatiService;
	
	
	@Override
	public ReportVerificaFirma checkDocumentSignatureByIdAllegato(Long idAllegato) throws BusinessException {
		ReportVerificaFirma reportVerifica = null;
		try {
			AllegatoEntity allegato = allegatoDAO.findById(idAllegato);
			reportVerifica =  checkDocumentSignatureByBytes(allegato.getContenuto());
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::checkDocumentSignatureByIdAllegato] Elemento non trovato: " + idAllegato, e);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::checkDocumentSignatureByIdAllegato] DAOException " + daoe.getMessage());
			throw new BusinessException(daoe);
		}
		return reportVerifica;
	}
	
	@Override
	public ReportVerificaFirma checkDocumentSignatureByIdFile(Long idFile) throws BusinessException {
		ReportVerificaFirma reportVerifica = null;
		try {
			RepositoryFileEntity repositoryFile = repositoryFileDAO.findById(idFile);
			reportVerifica =  checkDocumentSignatureByBytes(repositoryFile.getContenuto());
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::checkDocumentSignatureByIdFile] Elemento non trovato: " + idFile, e);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::checkDocumentSignatureByIdFile] DAOException " + daoe.getMessage());
			throw new BusinessException(daoe);
		}
		return reportVerifica;
	}
	
	@Override
	public ReportVerificaFirma checkDocumentSignatureByFile(MultipartFormDataInput file) throws BusinessException {
		ReportVerificaFirma reportVerifica = null;
		byte[] contenuto = null;
		try {
			contenuto = getBytesFrom(file);
			if (contenuto != null) {
				reportVerifica =  checkDocumentSignatureByBytes(contenuto);
			}
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::checkDocumentSignatureByFile] Elemento non trovato: "+e.getMessage(), e);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::checkDocumentSignatureByFile] DAOException " + daoe.getMessage());
			throw new BusinessException(daoe);
		}
		return reportVerifica;
	}
	
	@Override
	public boolean testResources() throws BusinessException {
		try {
			return doSignDAO.testResources();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::testResources] Errore invocazione DAO - ", e);
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public ReportVerificaFirma checkDocumentSignatureByBytes(byte[] contenuto) throws BusinessException {
		ReportVerificaFirma reportVerifica = new ReportVerificaFirma();
		try {
			SignedBuffer signedBuffer = new SignedBuffer();
			signedBuffer.setBuffer(contenuto);
			VerifyReport reportVerificaDoSign = doSignDAO.verifyDocument(signedBuffer);
			if (reportVerificaDoSign != null) {
				if (reportVerificaDoSign.getTipoFirma() == 0) { // file non firmato
					reportVerifica.setFirmato(false);
					reportVerifica.setTipoFirma( DecodificaTipoFirma.byId(reportVerificaDoSign.getTipoFirma()));
				} else { // file firmato
					reportVerifica.setFirmato(true);
					reportVerifica.setTipoFirma( DecodificaTipoFirma.byId(reportVerificaDoSign.getTipoFirma()));
					reportVerifica.setFormatoFirma( DecodificaFormatoFirma.byId(reportVerificaDoSign.getFormatoFirma()));
					LOG.debug("[" + CLASS_NAME + "::doSignVerifyDocument] Formato Firma: " + reportVerificaDoSign.getFormatoFirma());
					LOG.debug("[" + CLASS_NAME + "::doSignVerifyDocument] Tipo Firma: " + reportVerificaDoSign.getTipoFirma());
				}
			}
			return reportVerifica ;
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::doSignVerifyDocument] Elemento non trovato: "+e.getMessage(),e);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::doSignVerifyDocument] DAOException " + daoe.getMessage());
			throw new BusinessException(daoe);
		}
	}
	
	private byte[] getBytesFrom(MultipartFormDataInput file) throws ServiceException {
		byte[] bytes = null;
		try {
			List<InputPart> inputsParts = file.getParts();
			if (inputsParts.size() == 1) {
				InputPart inputPart = inputsParts.get(0);
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				bytes = IOUtils.toByteArray(inputStream);
			}
			return bytes;
		} catch (IOException e) {
			LOG.debug("[" + CLASS_NAME + "::getBytesFrom] Errore gestione stream byte ", e);
			throw new BusinessException("Errore recupero contenuto");
		}
	}
	
 	public String retrieveContentType(MultipartFormDataInput file) throws BusinessException {
		String result = null;
		byte[] contenuto = null;
		try {
			contenuto = getBytesFrom(file);
			if (contenuto != null) {
				result =  allegatiService.retrieveContentType(contenuto);
			}
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::retrieveContentType] Elemento non trovato: "+e.getMessage(), e);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::retrieveContentType] DAOException " + daoe.getMessage());
			throw new BusinessException(daoe);
		}
		return result;	
 	}
	
}
