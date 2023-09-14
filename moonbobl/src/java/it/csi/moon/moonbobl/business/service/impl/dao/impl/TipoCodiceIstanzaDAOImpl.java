/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;
/**
 * DAO per accesso alle decodifiche della codifica delle istanze di un modulo
 * 
 */
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.TipoCodiceIstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.TipoCodiceIstanzaEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

@Component
public class TipoCodiceIstanzaDAOImpl extends JdbcTemplateDAO implements TipoCodiceIstanzaDAO {

	private static final String CLASS_NAME = "TipoCodiceIstanzaDAOImpl";
	
	private static final String SELECT_FROM_ = "SELECT id_tipo_codice_istanza, desc_codice, descrizione_tipo FROM moon_io_d_tipocodiceistanza";
	private static final String FIND_BY_ID = SELECT_FROM_ + " WHERE id_tipo_codice_istanza = :id_tipo_codice_istanza";
	private static final String FIND_BY_CODICE = SELECT_FROM_ + " WHERE desc_codice = :desc_codice";
	private static final String ELENCO = SELECT_FROM_;

	@Override
	public TipoCodiceIstanzaEntity findById(long idTipoCodiceIstanza) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_tipo_codice_istanza", idTipoCodiceIstanza);
			return (TipoCodiceIstanzaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(TipoCodiceIstanzaEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public TipoCodiceIstanzaEntity findByCodice(String descCodice) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("desc_codice", descCodice);
			return (TipoCodiceIstanzaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CODICE, params, BeanPropertyRowMapper.newInstance(TipoCodiceIstanzaEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByCodice] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByCodice] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<TipoCodiceIstanzaEntity> find() throws DAOException {
		return (List<TipoCodiceIstanzaEntity>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(TipoCodiceIstanzaEntity.class));
	}

	private MapSqlParameterSource mapCategoriaModuloEntityParameters(TipoCodiceIstanzaEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_tipo_codice_istanza", entity.getIdTipoCodiceIstanza());
		params.addValue("desc_codice", entity.getDescCodice());
		params.addValue("descrizione_tipo", entity.getDescrizioneTipo());
		return params;
	}
}
