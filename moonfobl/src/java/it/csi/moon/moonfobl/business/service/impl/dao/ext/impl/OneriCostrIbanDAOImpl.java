/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.ext.impl;


import java.sql.Types;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonfobl.business.service.impl.dao.ext.OneriCostrIbanDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.OneriCostrIbanEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
@Component
public class OneriCostrIbanDAOImpl extends JdbcTemplateDAO implements OneriCostrIbanDAO {
	private static final String CLASS_NAME= "OneriCostrIbanDAOImpl";
	
	private static final String SELECT_FIELDS = "SELECT cod_istat, nome_comune, iban, data_ins, data_upd, attore_ins, attore_upd"; 
		
	private static final  String FIND_BY_CODICE_ISTAT = SELECT_FIELDS +
		" FROM moon_ext_oneri_costr_iban " +
		" WHERE cod_istat=:cod_istat";

	private static final String INSERT = "INSERT INTO moon_ext_oneri_costr_iban(cod_istat, nome_comune, iban, data_ins, data_upd, attore_ins, attore_upd)\r\n" + 
		" VALUES (:cod_istat, :nome_comune, :iban, :data_ins, :data_upd, :attore_ins, :attore_upd)";

	private static final String UPDATE = "UPDATE moon_ext_oneri_costr_iban\r\n" + 
			" SET cod_istat=:cod_istat, nome_comune=:nome_comune, iban=:iban, data_ins=:data_ins, data_upd=:data_upd, attore_ins=:attore_ins, attore_upd=:attore_upd\r\n" + 			
			" WHERE cod_istat=:cod_istat";
	
	@Override
	public OneriCostrIbanEntity findByCodiceIstat(String codiceIstat) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstat] IN codiceIstatd: "+codiceIstat);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("cod_istat", codiceIstat);
			return (OneriCostrIbanEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CODICE_ISTAT, params, BeanPropertyRowMapper.newInstance(OneriCostrIbanEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstat] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstat] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	@Override
	public int insert(OneriCostrIbanEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
		
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA");
		}
	}


	@Override
	public int update(OneriCostrIbanEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA");
		}
	}


//	cod_istat, nome_comune, iban, data_ins, data_upd, attore_ins, attore_upd
    private MapSqlParameterSource mapEntityParameters(OneriCostrIbanEntity entity) {
    	
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("cod_istat", entity.getCodIstat(),Types.VARCHAR);
    	params.addValue("nome_comune", entity.getNomeComune(),Types.VARCHAR);
    	params.addValue("iban" , entity.getIban(), Types.VARCHAR);    	     
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("attore_ins", entity.getAttoreIns(), Types.VARCHAR);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	return params;
    }


 
}
