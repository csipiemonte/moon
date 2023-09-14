/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.ModuloClass;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;

public interface ModuloClassService {

	List<ModuloClass> getFileClassByIdModulo(Long idModulo);

	ModuloClass getFileClassByIdModuloTipologia(Long idModulo, int idTipologia) throws ItemNotFoundBusinessException, BusinessException;

	ModuloClass uploadFileClass(Long idModulo, int idTipologia,  byte[] bytes);

	void delete(Long idModulo, int idTipologia);

}
