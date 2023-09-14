/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.CategoriaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Categoria;

/**
 * Contruttore di oggetto JSON Categoria per i moduli 
 *  da CategoriaEntity {@code entity}
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */

public class CategoriaMapper {
	
	public static Categoria buildFromCategoriaEntity (CategoriaEntity entity) {
		
		Categoria categoria = new Categoria();
		categoria.setIdCategoria(entity.getIdCategoria());
		categoria.setDescrizione(entity.getNomeCategoria());
		
		return categoria;	
	}

	public static Categoria buildFromModuloVersionatoEntity(ModuloVersionatoEntity moduloVersionato) {
		Categoria categoria = new Categoria();
		categoria.setIdCategoria(moduloVersionato.getIdCategoria());
		categoria.setDescrizione(moduloVersionato.getNomeCategoria());
		return categoria;
	}
}
