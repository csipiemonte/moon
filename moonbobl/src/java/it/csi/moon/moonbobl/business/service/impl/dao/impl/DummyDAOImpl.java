/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.DummyDAO;

@Component
public class DummyDAOImpl extends JdbcTemplateDAO  implements DummyDAO{

	@Override
	public String pingDB() {
//		return (String)getCustomJdbcTemplate().queryForObject("select msg from moon_fo_dummy where id = 1", String.class);
		return (String)getCustomJdbcTemplate().queryForObject("select 'OK'", String.class);
	}

}
