/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.Categoria;
import it.csi.moon.commons.entity.CategoriaEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;

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
		categoria.setCodiceAmbito(moduloVersionato.getCodiceAmbito());
		categoria.setNomeAmbito(moduloVersionato.getNomeAmbito());
		categoria.setColor(moduloVersionato.getColor());
		return categoria;
	}

	/**
	 * Categoria for API (external fruitore) so without 'idCategoria'
	 * @param moduloVersionato
	 * @return Categoria
	 */
	public static Categoria buildFromModuloVersionatoEntityForApi(ModuloVersionatoEntity moduloVersionato) {
		Categoria categoria = new Categoria();
		categoria.setDescrizione(moduloVersionato.getNomeCategoria());
		categoria.setCodiceAmbito(moduloVersionato.getCodiceAmbito());
		categoria.setNomeAmbito(moduloVersionato.getNomeAmbito());
		categoria.setColor(moduloVersionato.getColor());
		return categoria;
	}
}
