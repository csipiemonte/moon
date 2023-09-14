/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.FruitoreAttributoEntity;

/**
 * DAO per l'accesso ai attributi di un fruitore
 * 
 * @see FruitoreAttributoEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface FruitoreAttributoDAO {
	
	public enum FruitoreAttributoEnum {ALL_MODULES, ONE_USER_ENTE};
	public List<FruitoreAttributoEntity> findByIdFruitore(Integer idFruitore);
	
	public FruitoreAttributoEntity findById(Long idAttributo);
	public FruitoreAttributoEntity findByNome(Integer idFruitore, String nomeAttributo);
	public FruitoreAttributoEntity findByAttributo(Integer idFruitore, FruitoreAttributoEnum attributo);
	
	public Long insert(FruitoreAttributoEntity attributo);
	public int update(FruitoreAttributoEntity attributo);
	public int delete(Long idAttributo);
	public int deleteAllByIdFruitore(Long idFruitore);

}
