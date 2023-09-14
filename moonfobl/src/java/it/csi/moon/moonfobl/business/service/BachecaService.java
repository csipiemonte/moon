/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import java.util.List;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.dto.moonfobl.MessaggioBacheca;

public interface BachecaService {
	
	public List<MessaggioBacheca> getElencoMessaggiBacheca(UserInfo user);
}
