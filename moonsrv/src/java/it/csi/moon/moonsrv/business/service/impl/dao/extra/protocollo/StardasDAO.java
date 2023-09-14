/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo;

import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.stardas.cxfclient.SmistaDocumentoRequestType;
import it.csi.stardas.cxfclient.SmistaDocumentoResponseType;

public interface StardasDAO {

	public SmistaDocumentoResponseType smistaDocumento(SmistaDocumentoRequestType smistaDocumentoRequest) throws DAOException;
	
}
