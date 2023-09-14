/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.dosign.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.dosign.cxfclient.CertBuffer;
import it.csi.dosign.cxfclient.Document;
import it.csi.dosign.cxfclient.DosignBusinessManager;
import it.csi.dosign.cxfclient.DosignBusinessManager_Service;
import it.csi.dosign.cxfclient.DosignException_Exception;
import it.csi.dosign.cxfclient.EnvelopedBuffer;
import it.csi.dosign.cxfclient.SignedBuffer;
import it.csi.dosign.cxfclient.VerifyCertificateReport;
import it.csi.dosign.cxfclient.VerifyParameter;
import it.csi.dosign.cxfclient.VerifyReport;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimanWSTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dosign.DoSignDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Qualifier("apimint")
@Component
public class DoSignDAOImpl extends ApimanWSTemplateImpl implements DoSignDAO  {

	private static final String CLASS_NAME = "DoSignDAOImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();

	private static final String DOC_DOSIGN_BUSINESS_MANAGER_1_0_PATH = "/DOC_DOSIGN_BusinessManager/1.0";
	
	@Override
	public Document extractDocumentFromEnvelope(EnvelopedBuffer eb) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSignaturesNumber(SignedBuffer inContent) throws DAOException {
        long start = System.currentTimeMillis();
        String errore = "";
		try {
			setPathExtra(DOC_DOSIGN_BUSINESS_MANAGER_1_0_PATH);					
			DosignBusinessManager_Service doSignService = new DosignBusinessManager_Service();
			DosignBusinessManager wrapped = (DosignBusinessManager) getPortService(doSignService.getDosignBusinessManagerBeanPort(), DosignBusinessManager.class);
			return wrapped.getSignaturesNumber(inContent);
		} catch (DosignException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::getSignaturesNumber] Errore servizio Dosign", e);
			errore = "DosignException_Exception";
			throw new DAOException(e.getMessage());
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::getSignaturesNumber] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
        }
	}

	@Override
	public boolean testResources() throws DAOException {
		try {
			setPathExtra(DOC_DOSIGN_BUSINESS_MANAGER_1_0_PATH);
			DosignBusinessManager_Service doSignService = new DosignBusinessManager_Service();
			DosignBusinessManager wrapped = (DosignBusinessManager) getPortService(doSignService.getDosignBusinessManagerBeanPort(), DosignBusinessManager.class);
			return wrapped.testResources();
		} catch (DosignException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::testResources] Errore servizio Dosign", e);
			throw new DAOException(e.getMessage());
		}
	}

	@Override
	public VerifyCertificateReport verifyCertificate(CertBuffer buffer, VerifyParameter params) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VerifyReport verifyDocument(SignedBuffer sb) throws DAOException {
        long start = System.currentTimeMillis();
        String errore = "";
		try {
			setPathExtra(DOC_DOSIGN_BUSINESS_MANAGER_1_0_PATH);
			DosignBusinessManager_Service doSignService = new DosignBusinessManager_Service();
			DosignBusinessManager wrapped = (DosignBusinessManager) getPortService(doSignService.getDosignBusinessManagerBeanPort(), DosignBusinessManager.class);
			return wrapped.verifyDocument(sb);
		} catch (DosignException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::verifyDocument] Errore servizio Dosign", e);
			errore = "DosignException_Exception";
			throw new DAOException(e.getMessage());
		} catch (Throwable e) {
			LOG.error("[" + CLASS_NAME + "::verifyDocument] Eccezione Throwable", e);
			errore = "Throwable";
			throw new DAOException(e.getMessage());
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::verifyDocument] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
        }
	}

}
