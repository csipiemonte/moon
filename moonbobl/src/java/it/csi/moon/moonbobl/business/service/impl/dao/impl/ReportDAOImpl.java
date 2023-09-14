/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.ReportDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ReportEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per il conteggio dei report
 * 
 * @see ModuloEntity
 * 
 * @author Alberto
 *
 */
@Component
public class ReportDAOImpl extends JdbcTemplateDAO implements ReportDAO {
	private final static String CLASS_NAME = "ModuloDAOImpl";
	
	private static final  String COUNT_MODULI_INVIATI  = "SELECT count(*) as conteggio FROM moon_fo_t_istanza "
			+ "WHERE id_modulo=:id_modulo and id_stato_wf not in (1,3,4);";
	

	@Override
	public int countModuliInviati(Long idModulo) throws DAOException {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
		try {
			log.debug("[" + CLASS_NAME + "::"+ nameofCurrMethod +"] IN id: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			
			ReportEntity row = (ReportEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(COUNT_MODULI_INVIATI, params,	BeanPropertyRowMapper.newInstance(ReportEntity.class)  );
			int result = row.getConteggio();
			return result;
			
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::"+ nameofCurrMethod +"] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::"+ nameofCurrMethod +"] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


}
