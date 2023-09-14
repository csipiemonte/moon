/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.coto.impl;

import java.sql.Types;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.coto.TplRichiedenteDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.TplRichiedenteEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

public class TplRichiedenteDAOImpl extends JdbcTemplateDAO implements TplRichiedenteDAO {
	
	private static final String CLASS_NAME = "TplRichiedenteDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_richiedente, cf_piva, nome, cognome, denominazione, codice_voucher, tipo_abbonamento, data_richiesta, data_scadenza, cf_utilizzatore, stato_utilizzo_voucher, stato_utilizzo_abbonamento";
	
	private static final String FIND_BY_ID  = FIND_SELECT_FIELDS +
			" FROM moon_ext_tpl_richiedenti" +
			" WHERE id_richiedente = :id_richiedente";
	
	private static final String FIND_BY_CF_CV  = FIND_SELECT_FIELDS +
			" FROM moon_ext_tpl_richiedenti" +
			" WHERE cf_piva = :cf_piva" +
			" AND codice_voucher = :codice_voucher";
	
	private static final String INSERT = "INSERT INTO moon_ext_tpl_richiedenti(id_richiedente, cf_piva, nome, cognome, denominazione, codice_voucher, tipo_abbonamento, data_richiesta, data_scadenza, cf_utilizzatore, stato_utilizzo_voucher, stato_utilizzo_abbonamento)" + 
			" VALUES (:id_richiedente, :cf_piva, :nome, :cognome, :denominazione, :codice_voucher, :tipo_abbonamento, :data_richiesta, :data_scadenza, :cf_utilizzatore, :stato_utilizzo_voucher, :stato_utilizzo_abbonamento)";
	
	private static final String SEQ_ID = "SELECT nextval('moon_ext_tpl_richiedenti_seq')";
	
	@Override
	public TplRichiedenteEntity findById(Integer idRichiedente) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_richiedente", idRichiedente);
			return (TplRichiedenteEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(TplRichiedenteEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public TplRichiedenteEntity findByCfCv(String cfPiva, String codiceVoucher) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("cf_piva", cfPiva);
			params.addValue("codice_voucher", codiceVoucher);
			return (TplRichiedenteEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CF_CV, params, BeanPropertyRowMapper.newInstance(TplRichiedenteEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCfCv] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCfCv] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public Integer insert(TplRichiedenteEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Integer id = getCustomNamedParameterJdbcTemplateImpl().queryForInt(SEQ_ID, new MapSqlParameterSource());
			entity.setIdRichiedente(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}
	
    private MapSqlParameterSource mapEntityParameters(TplRichiedenteEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_richiedente", entity.getIdRichiedente());
    	params.addValue("cf_piva", entity.getCfPiva(), Types.VARCHAR);
    	params.addValue("nome", entity.getNome(), Types.VARCHAR);
    	params.addValue("cognome" , entity.getCognome(), Types.VARCHAR);
    	params.addValue("denominazione" , entity.getDenominazione(), Types.VARCHAR);
    	params.addValue("codice_voucher" , entity.getCodiceVoucher(), Types.VARCHAR);
    	params.addValue("tipo_abbonamento" , entity.getTipoAbbonamento(), Types.VARCHAR);
    	params.addValue("data_richiesta", entity.getDataRichiesta(), Types.TIMESTAMP);
    	params.addValue("data_scadenza", entity.getDataScadenza(), Types.TIMESTAMP);
    	params.addValue("cf_utilizzatore" , entity.getCfUtilizzatore(), Types.VARCHAR);
    	params.addValue("stato_utilizzo_voucher" , entity.getStatoUtilizzoVoucher(), Types.VARCHAR);
    	params.addValue("stato_utilizzo_abbonamento" , entity.getStatoUtilizzoAbbonamento(), Types.VARCHAR);
    	return params;
    }
    
}
