/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.PortaleModuloLogonModeDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.PortaleModuloLogonModeEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

/**
 * DAO per l'accesso via Modulistica
 * <br>
 * <br>Tabella moon_ml_r_portale_modulo_logon_mode
 * 
 * @see PortaleModuloLogonModeEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class PortaleModuloLogonModeDAOImpl extends JdbcTemplateDAO implements PortaleModuloLogonModeDAO {
	
	private final static String CLASS_NAME = "PortaleModuloLogonModeDAOImpl";
	
	private static final String SELECT_FIELDS_ = "SELECT id_portale, id_modulo, id_logon_mode, filtro";

	private static final  String FIND_BY_ID_MODULO  = SELECT_FIELDS_ +
		" FROM moon_ml_r_portale_modulo_logon_mode" +
		" WHERE id_modulo = :id_modulo";

	private static final String INSERT = "INSERT INTO moon_ml_r_portale_modulo_logon_mode (id_portale, id_modulo, id_logon_mode, filtro)" + 
			" VALUES (:id_portale, :id_modulo, :id_logon_mode, :filtro)";

	private static final String DELETE = "DELETE FROM moon_ml_r_portale_modulo_logon_mode WHERE id_modulo = :id_modulo AND id_portale = :id_portale";
	
	@Override
	public List<PortaleModuloLogonModeEntity> findByIdModulo(Long idModulo) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return (List<PortaleModuloLogonModeEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(PortaleModuloLogonModeEntity.class)  );
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public int insert(PortaleModuloLogonModeEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapPortaleModuloLogonModeParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}

	@Override
	public int delete(Long idPortale, Long idModulo) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN idModulo: " + idModulo + "  idPortale: " + idPortale);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("id_portale", idPortale);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}
	
	private MapSqlParameterSource mapPortaleModuloLogonModeParameters(PortaleModuloLogonModeEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_portale", entity.getIdPortale());
		params.addValue("id_modulo", entity.getIdModulo());
		params.addValue("id_logon_mode", entity.getIdLogonMode());
		params.addValue("filtro", entity.getFiltro(), Types.VARCHAR);
		return params;
	}


}
