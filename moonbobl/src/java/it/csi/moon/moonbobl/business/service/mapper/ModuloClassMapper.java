/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.ModuloClassEntity;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloClass;

public class ModuloClassMapper {

	public static ModuloClass buildFromModuloClassEntity(ModuloClassEntity moduloEntity) {
		ModuloClass modulo = new ModuloClass(moduloEntity.getIdModulo(),moduloEntity.getTipologia(),moduloEntity.getNomeClass());
		return modulo;
	}
}
