/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.PortaleModuloLogonModeEntity;
import it.csi.moon.moonbobl.dto.moonfobl.LogonMode;
import it.csi.moon.moonbobl.dto.moonfobl.PortaleLogonMode;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;

public interface ModulisticaService {

	public List<LogonMode> getElencoLogonMode();

	public List<PortaleLogonMode> getPortaliLogonModeByIdModulo(UserInfo user, Long idModulo);

	public PortaleLogonMode inserisciPortaleModuloLogonMode(UserInfo user, PortaleModuloLogonModeEntity pmlm);

	public void cancellaPortaleModuloLogonMode(UserInfo user, Long idPortale, Long idModulo);

}
