/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.dosign;

import it.csi.dosign.cxfclient.CertBuffer;
import it.csi.dosign.cxfclient.Document;
import it.csi.dosign.cxfclient.EnvelopedBuffer;
import it.csi.dosign.cxfclient.SignedBuffer;
import it.csi.dosign.cxfclient.VerifyCertificateReport;
import it.csi.dosign.cxfclient.VerifyParameter;
import it.csi.dosign.cxfclient.VerifyReport;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

public interface DoSignDAO {
	
	public Document extractDocumentFromEnvelope(EnvelopedBuffer eb) throws DAOException;
	
	public int getSignaturesNumber(SignedBuffer inContent) throws DAOException;
	
	public boolean testResources() throws DAOException ;
	
	public VerifyCertificateReport verifyCertificate(CertBuffer buffer, VerifyParameter params) throws DAOException;
	
	public VerifyReport verifyDocument(SignedBuffer sb) throws DAOException;

}
