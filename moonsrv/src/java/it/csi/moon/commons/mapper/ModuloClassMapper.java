/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.ModuloClass;
import it.csi.moon.commons.entity.ModuloClassEntity;


public class ModuloClassMapper {

	public static ModuloClass buildFromModuloClassEntity(ModuloClassEntity moduloEntity) {
		ModuloClass modulo = new ModuloClass(moduloEntity.getIdModulo(),moduloEntity.getTipologia(),moduloEntity.getNomeClass());
		return modulo;
	}
}
