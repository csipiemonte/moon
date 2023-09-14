/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.ModuloClass;


public interface ModuloClassService {

	List<ModuloClass> getFileClassByIdModulo(Long idModulo);

	ModuloClass getFileClassByIdModuloTipologia(Long idModulo, int idTipologia);

	ModuloClass uploadModuloClass(Long idModulo, int idTipologia,  byte[] bytes);

	void delete(Long idModulo, int idTipologia);

}
