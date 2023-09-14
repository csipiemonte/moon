/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.impl;

import java.sql.Types;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.FruitoreDatiAzioneEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.FruitoreDatiAzioneDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;


@Component
public class FruitoreDatiAzioneDAOImpl extends JdbcTemplateDAO implements FruitoreDatiAzioneDAO {
	
	private static final String CLASS_NAME = "FruitoreDatiAzioneDAOImpl";
	
	private static final  String FIND_BY_ID  = "SELECT id_fruitore_dati_azione, codice, descrizione, identificativo, data, numero_protocollo, data_protocollo, id_istanza, id_storico_workflow, dati_azione, post_azioni, allegati_azione" +
			" FROM moon_wf_t_fruitore_dati_azione" +
			" WHERE id_fruitore_dati_azione = :id_fruitore_dati_azione";
	
	private static final  String FIND_BY_ID_STORICO_WORFLOW  = "SELECT id_fruitore_dati_azione, codice, descrizione, identificativo, data, numero_protocollo, data_protocollo, id_istanza, id_storico_workflow, dati_azione, post_azioni, allegati_azione" +
			" FROM moon_wf_t_fruitore_dati_azione" +
			" WHERE id_storico_workflow = :id_storico_workflow";
	
	private static final String INSERT = "INSERT INTO moon_wf_t_fruitore_dati_azione(" + 
			" id_fruitore_dati_azione, codice, descrizione, identificativo, data, numero_protocollo, data_protocollo, id_istanza, id_storico_workflow, dati_azione, post_azioni, allegati_azione)" + 
			" VALUES (:id_fruitore_dati_azione, :codice, :descrizione, :identificativo, :data, :numero_protocollo, :data_protocollo, :id_istanza, :id_storico_workflow, :dati_azione, :post_azioni, :allegati_azione)";   
	

	
	private static final String SEQ_ID = "SELECT nextval('moon_wf_t_fruitore_dati_azione_id_fruitore_dati_azione_seq')";
	
	@Override
	public FruitoreDatiAzioneEntity findById(long idFruitoreDatiAzione) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_fruitore_dati_azione", idFruitoreDatiAzione);
			return (FruitoreDatiAzioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(FruitoreDatiAzioneEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public FruitoreDatiAzioneEntity findByIdStoricoWorkflow(long idStoricoWorkflow) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_storico_workflow", idStoricoWorkflow);
			return (FruitoreDatiAzioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_STORICO_WORFLOW, params, BeanPropertyRowMapper.newInstance(FruitoreDatiAzioneEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdStoricoWorkflow] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdStoricoWorkflow] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	@Override
	public Integer insert(FruitoreDatiAzioneEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Integer idFruitoreDatiAzione = getCustomNamedParameterJdbcTemplateImpl().queryForInt(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdFruitoreDatiAzione(idFruitoreDatiAzione);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapFruitoreDatiAzioneEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idFruitoreDatiAzione;
			
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
		
	}


	private MapSqlParameterSource mapFruitoreDatiAzioneEntityParameters(FruitoreDatiAzioneEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_fruitore_dati_azione", entity.getIdFruitoreDatiAzione());
    	params.addValue("codice", entity.getCodice());
    	params.addValue("descrizione", entity.getDescrizione());
    	params.addValue("identificativo", entity.getIdentificativo());
    	params.addValue("data", entity.getData(),Types.TIMESTAMP);
    	params.addValue("numero_protocollo", entity.getNumeroProtocollo());
    	params.addValue("data_protocollo", entity.getDataProtocollo(),Types.TIMESTAMP);
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("id_storico_workflow", entity.getIdStoricoWorkflow());
    	params.addValue("dati_azione", entity.getDatiAzione());
    	params.addValue("post_azioni", entity.getPostAzioni());
    	params.addValue("allegati_azione", entity.getAllegatiAzione());
    	return params;
    	}
}


