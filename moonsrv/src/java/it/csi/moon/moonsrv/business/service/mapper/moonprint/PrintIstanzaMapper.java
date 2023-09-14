/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.moonprint;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.commons.entity.ModuloStrutturaEntity;

public interface PrintIstanzaMapper {

	public MoonprintDocument remap(Istanza istanza, ModuloStrutturaEntity strutturaEntity) throws Exception;
	
}
