/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.dto.ValutazioneModuloSintesi;
import it.csi.moon.commons.entity.ValutazioneModuloEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso alle valutazioni utente dei moduli
 * 
 * @see ValutazioneModuloEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ValutazioneModuloDAO {
	public List<ValutazioneModuloEntity> findByIdModulo(Long idModulo) throws DAOException;
	public void insert(ValutazioneModuloEntity entity) throws DAOException;
	
	public List<ValutazioneModuloSintesi> findSintesiByIdModulo(Long idModulo) throws DAOException;
}

