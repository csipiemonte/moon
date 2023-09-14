/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.impl;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonfobl.business.service.impl.dao.LogonModeDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.LogonModeEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;


/**
 * DAO per l'identificazione del logon mode
 * <br>
 * <br>Tabella : moon_ml_d_logon_mode
 * 
 * @see LogonModeEntity
 * 
 * @author Danilo
 *
 */
@Component
public class LogonModeDAOImpl extends JdbcTemplateDAO implements LogonModeDAO {
	
	private static final String CLASS_NAME = "LogonModeDAOImpl";
	
	private static final  String FIND_LOGON_MODE  = "SELECT m.codice_modulo, p.nome_portale, mdm.codice_logon_mode as logon_mode, mlm.filtro" +
			" from moon_ml_r_portale_modulo_logon_mode mlm, moon_ml_d_logon_mode mdm, moon_io_d_modulo m, moon_fo_d_portale p" + 
			" where mlm.id_modulo = m.id_modulo" + 
			" and mlm.id_portale = p.id_portale" + 
			" and mlm.id_logon_mode = mdm.id_logon_mode" +
			" and m.codice_modulo = :codice_modulo" +
			" and p.nome_portale = :nome_portale";	

	@Override
	public LogonModeEntity findLogonMode(String codiceModulo, String nomePortale) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_modulo", codiceModulo);
			params.addValue("nome_portale", nomePortale);
			return (LogonModeEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_LOGON_MODE, params, BeanPropertyRowMapper.newInstance(LogonModeEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findLogonMode] Elemento non trovato: "+codiceModulo+ "/"+nomePortale);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findLogonMode] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
		
    private MapSqlParameterSource mapLogonModeEntityParameters(LogonModeEntity entity) {
    	
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("codice_modulo", entity.getCodiceModulo());
    	params.addValue("nome_portale" , entity.getNomePortale());
 
    	return params;
    }



}
