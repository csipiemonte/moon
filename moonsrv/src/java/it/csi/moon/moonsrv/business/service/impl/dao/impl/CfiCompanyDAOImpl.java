/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.CfiCompanyEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.CfiCompanyDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

@Component
public class CfiCompanyDAOImpl extends JdbcTemplateDAO implements CfiCompanyDAO{

	private static final String CLASS_NAME = "CfiCompanyDAOImpl";
	
	private static final  String FIND_BY_CFI  = "SELECT company_name" +
			" FROM moon_r2u_t_cfi_companyname" +
			" WHERE cfi = :cfi";
	
	@Override
	public List<CfiCompanyEntity> findByCfi(String cfi) throws ItemNotFoundDAOException, DAOException {
		try {
			List<CfiCompanyEntity> result = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("cfi", cfi);
			result = (List<CfiCompanyEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_CFI, params, BeanPropertyRowMapper.newInstance(CfiCompanyEntity.class));
			return result;
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCfi] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCfi] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

}
