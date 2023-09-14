/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.service.impl.dao.MoondbDAO;

/**
 * DAO per l'accesso alla componente MOOnDB solo per funzioni di sistema
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class MoondbDAOImpl extends JdbcTemplateDAO  implements MoondbDAO {

	@Override
	public String pingDB() {
//		return (String)getCustomJdbcTemplate().queryForObject("select msg from moon_fo_dummy where id = 1", String.class);
		return (String)getCustomJdbcTemplate().queryForObject("select 'OK'", String.class);
	}

}
