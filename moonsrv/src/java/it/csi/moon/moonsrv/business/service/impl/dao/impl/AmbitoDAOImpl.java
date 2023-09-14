/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;
/**
 * DAO per accesso alle Categorie
 * 
 */
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.AmbitoEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.AmbitoDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

@Component
public class AmbitoDAOImpl extends JdbcTemplateDAO implements AmbitoDAO {

	private static final String CLASS_NAME = "AmbitoDAOImpl";
	
	private static final String SELECT = "SELECT id_ambito, codice_ambito, nome_ambito, id_visibilita_ambito, color, data_upd, attore_upd";
	private static final String SELECT_A = "SELECT a.id_ambito, a.codice_ambito, a.nome_ambito, a.id_visibilita_ambito, a.color, a.data_upd, a.attore_upd";
	
	private static final String FIND_BY_ID = SELECT + " FROM moon_fo_d_ambito WHERE id_ambito=:id_ambito";
	private static final String FIND_BY_CODICE = SELECT + " FROM  moon_fo_d_ambito WHERE codice_ambito = :codice_ambito";
	private static final String ELENCO = SELECT + " FROM moon_fo_d_ambito";
	private static final String FIND_BY_ID_MODULO = SELECT_A
			+ " FROM moon_fo_d_ambito a, moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod"
		    + " WHERE a.id_ambito = cat.id_ambito"
			+ " AND cat.id_categoria = catmod.id_categoria"
			+ " AND catmod.id_modulo = :id_modulo";
	
	@Override
	public AmbitoEntity findById(long idAmbito) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ambito", idAmbito);
			return (AmbitoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(AmbitoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException();
		}
	}

	@Override
	public AmbitoEntity findByCodice(String codiceAmbito) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_ambito", codiceAmbito);
			return (AmbitoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CODICE, params, BeanPropertyRowMapper.newInstance(AmbitoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCodice] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodice] Errore database: "+e.getMessage(),e);
			throw new DAOException();
		}
	}
	
	@Override
	public List<AmbitoEntity> find() throws DAOException {
		return (List<AmbitoEntity>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(AmbitoEntity.class));
	}

	@Override
	public List<AmbitoEntity> findByIdModulo(long idModulo) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return (List<AmbitoEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(AmbitoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

}
