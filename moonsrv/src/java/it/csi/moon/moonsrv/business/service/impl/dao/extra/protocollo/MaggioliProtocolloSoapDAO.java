/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo;

import com.maggioli.prt.cxfclient.ProtocolloOut;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.dto.maggioli.soap.InserisciDocumentoRequest;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

public interface MaggioliProtocolloSoapDAO {
	
	@Deprecated
	public String inserisciDocumentoEAnagraficheString(InserisciDocumentoRequest req) throws DAOException;
	public ProtocolloOut inserisciProtocolloEAnagraficheString(InserisciDocumentoRequest req) throws DAOException;

}
