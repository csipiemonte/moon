/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.dto.moonfobl.EmbeddedNavigator;

public interface EmbeddedService {

	EmbeddedNavigator gotoViewIstanza(UserInfo user, String codiceIstanza);

	EmbeddedNavigator gotoEditIstanza(UserInfo user, String codiceIstanza);

	EmbeddedNavigator gotoNewIstanza(UserInfo user, String codiceModulo);

	EmbeddedNavigator gotoIstanza(UserInfo user, String codiceIstanza);

}
