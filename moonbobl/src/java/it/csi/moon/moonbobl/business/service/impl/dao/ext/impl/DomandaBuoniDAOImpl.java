/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.ext.impl;


import java.sql.Types;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.ext.DomandaBuoniDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ext.DomandaBuoniEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;


/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
 * 
 * @author Laurent Pissard
 *
 * @since 1.0.0
 */
@Component
public class DomandaBuoniDAOImpl extends JdbcTemplateDAO implements DomandaBuoniDAO {
	private final static String CLASS_NAME= "DomandaBuoniDAOImpl";
	
	private static final String SELECT_FIELDS_ = "SELECT id_ext_domanda,id_istanza,codice_istanza,codice_fiscale,tipo_documento,numero_documento,data_emissione_documento,nome ,cognome ,num_componenti, telefono ,cellulare ,email,chk_reddito_cittadinanza,chk_altri_sostegni,flag_verificatoanpr,flag_mail_inviata,flag_controlli_eseguiti,esito_controlli,data_ins_domanda,periodo,operatore_ins,pin"; 
	
	private static final  String FIND_BY_ID = SELECT_FIELDS_ +
		" FROM moon_ext_domanda_buoni " +
		" WHERE id_ext_buono=:id_ext_buono";

	private static final String INSERT = "INSERT INTO moon_ext_domanda_buoni(id_ext_domanda,id_istanza,codice_istanza,codice_fiscale,tipo_documento,numero_documento,data_emissione_documento,nome ,cognome ,num_componenti, telefono ,cellulare ,email,chk_reddito_cittadinanza,chk_altri_sostegni,flag_verificatoanpr,flag_mail_inviata,flag_controlli_eseguiti,esito_controlli,data_ins_domanda,periodo,operatore_ins,pin)" +
		" VALUES (:id_ext_domanda,:id_istanza,:codice_istanza,:codice_fiscale,:tipo_documento,:numero_documento,:data_emissione_documento,:nome,:cognome,:num_componenti,:telefono,:cellulare,:email,:chk_reddito_cittadinanza,:chk_altri_sostegni,:flag_verificatoanpr,:flag_mail_inviata,:flag_controlli_eseguiti,:esito_controlli,:data_ins_domanda,:periodo,:operatore_ins,:pin)";
		
	private static final String UPDATE = "UPDATE moon_ext_domanda_buoni" + 
		" SET codice_istanza=:codice_istanza, id_modulo=:id_modulo, codice_fiscale_dichiarante=:codice_fiscale_dichiarante, " +
		" id_stato_wf=:id_stato_wf, data_creazione=:data_creazione, attore_ins=:attore_ins, attore_upd=:attore_upd, fl_eliminata=:fl_eliminata, fl_archiviata=:fl_archiviata, importanza=:importanza, numero_protocollo=:numero_protocollo, data_protocollo=:data_protocollo, current_step=:current_step" + 
		" WHERE id_ext_buono= :id_ext_buono";
	
	private static final String SEQ_ID = "SELECT nextval('moon_ext_domanda_buoni_id_ext_domanda_seq')";
	
	
	@Override
	public DomandaBuoniEntity findById(Integer id) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ext_buono", id);
			return (DomandaBuoniEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(DomandaBuoniEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	@Override
	public Integer countByCodiceFiscali(String joinedCodiceFiscali) throws DAOException {
		Integer result = null;
		log.debug("[" + CLASS_NAME + "::countByCodiceFiscali] IN joinedCodiceFiscali: "+joinedCodiceFiscali);
		String sql = "SELECT COUNT(*) FROM moon_ext_domanda_buoni WHERE codice_fiscale IN (" + joinedCodiceFiscali + ")";
		result = getCustomJdbcTemplate().queryForInt(sql);
		return result;
	}


	@Override
	public Integer insert(DomandaBuoniEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Integer id = getCustomNamedParameterJdbcTemplateImpl().queryForInt(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdExtDomanda(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA");
		}
	}


	@Override
	public int update(DomandaBuoniEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA");
		}
	}

	
//	id_istanza, codice_istanza, id_modulo, codice_fiscale_dichiarante, id_stato_wf, data_creazione, attore_ins, attore_upd, fl_eliminata
    private MapSqlParameterSource mapEntityParameters(DomandaBuoniEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_ext_domanda", entity.getIdExtDomanda());
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("codice_istanza" , entity.getCodiceIstanza(), Types.VARCHAR);
    	params.addValue("codice_fiscale" , entity.getCodiceFiscale(), Types.VARCHAR);
    	
    	params.addValue("tipo_documento", entity.getTipoDocumento(), Types.VARCHAR);
    	params.addValue("numero_documento", entity.getNumeroDocumento(), Types.VARCHAR);
    	params.addValue("data_emissione_documento", entity.getDataEmissioneDocumento(), Types.DATE);
    	
    	params.addValue("nome", entity.getNome(), Types.VARCHAR);
    	params.addValue("cognome", entity.getCognome(), Types.VARCHAR);
    	params.addValue("num_componenti", entity.getNumComponenti());    	
    	params.addValue("telefono", entity.getTelefono(), Types.VARCHAR);
    	params.addValue("cellulare", entity.getCellulare(), Types.VARCHAR);
    	params.addValue("email", entity.getEmail(), Types.VARCHAR);
    	
    	params.addValue("chk_reddito_cittadinanza", entity.getChkRedditoCittadinanza(), Types.VARCHAR);
    	params.addValue("chk_altri_sostegni", entity.getChkAltriSostegni(), Types.VARCHAR);

    	params.addValue("flag_verificatoanpr", entity.getFlagMailInviata(), Types.VARCHAR);
    	params.addValue("flag_mail_inviata", entity.getFlagMailInviata(), Types.VARCHAR);
    	params.addValue("flag_controlli_eseguiti", entity.getFlagControlliEseguiti(), Types.VARCHAR);
    	params.addValue("esito_controlli", entity.getEsitoControlli(), Types.VARCHAR);
    	
    	params.addValue("data_ins_domanda", entity.getDataInsDomanda(), Types.TIMESTAMP);
    	params.addValue("periodo", entity.getPeriodo(), Types.VARCHAR);
    	params.addValue("operatore_ins", entity.getOperatoreIns(), Types.VARCHAR);
    	params.addValue("pin", entity.getPin(), Types.VARCHAR);
    	return params;
    }

/*
    id_ext_domanda,id_istanza,codice_istanza,codice_fiscale,
    tipo_documento,numero_documento,data_emissione_documento,
    nome ,cognome ,num_componenti, telefono ,cellulare ,email,
    chk_reddito_cittadinanza,chk_altri_sostegni,
    flag_verificatoanpr,flag_mail_inviata,flag_controlli_eseguiti,esito_controlli,
    data_ins_domanda,periodo,operatore_ins,pin
*/
 
}
