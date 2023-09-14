/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

/**
 * DAO per l'accesso alla componente MOOnDB solo per funzioni di sistema
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface MoondbDAO  {

	public String pingDB() ;
}
