/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.entity.StoricoWorkflowFilter;
import it.csi.moon.commons.entity.UtenteEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso allo storicoworkflow e oggetti direttamente correlati
 *
 * @author Alberto
 *
 */
@Component
public class StoricoWorkflowDAOImpl extends JdbcTemplateDAO implements StoricoWorkflowDAO {
	
	private static final String CLASS_NAME = "StoricoWorkflowDAOImpl";
	
	private static final String SELECT_FIELDS = "SELECT id_storico_workflow, id_istanza, id_processo, id_stato_wf_partenza, nome_stato_wf_partenza, id_stato_wf_arrivo," + 
			" nome_stato_wf_arrivo, id_azione, nome_azione, dati_azione, desc_destinatario, data_inizio, data_fine, id_file_rendering, attore_upd, id_datiazione";
	private static final String SELECT_FIELDS_SW = "SELECT sw.id_storico_workflow, sw.id_istanza,sw.id_processo, sw.id_stato_wf_partenza, sw.nome_stato_wf_partenza, sw.id_stato_wf_arrivo," + 
			" sw.nome_stato_wf_arrivo, sw.id_azione, sw.nome_azione, sw.dati_azione, sw.desc_destinatario, sw.data_inizio, sw.data_fine, sw.id_file_rendering, sw.attore_upd, sw.id_datiazione";

	private static final String FIND_LAST_STORICO = SELECT_FIELDS +
		" FROM moon_wf_t_storico_workflow " +
		" WHERE id_istanza=:id_istanza " +
		" AND data_fine IS NULL ORDER BY data_inizio DESC";

	private static final String FIND_LAST_STORICO_AZIONE = SELECT_FIELDS +
		" FROM moon_wf_t_storico_workflow " +
		" WHERE id_istanza=:id_istanza " +
		" AND id_azione=:id_azione " +
		" ORDER BY data_inizio DESC LIMIT 1";
	
	private static final String FIND_BY_FILTER = SELECT_FIELDS_SW +
		" FROM moon_wf_t_storico_workflow sw, moon_wf_d_azione a " +
		" WHERE a.id_azione = sw.id_azione";
	
	private static final String FIND_LAST_STORICO_LISTAZIONI = SELECT_FIELDS +
		" FROM moon_wf_t_storico_workflow " +
		" WHERE id_istanza=:id_istanza";
	
	private static final String FIND_BY_ID_ISTANZA = SELECT_FIELDS +
		" FROM moon_wf_t_storico_workflow " +
		" WHERE id_istanza=:id_istanza ORDER BY data_inizio";
	
	private static final String FIND_BY_ID = SELECT_FIELDS +
		" FROM moon_wf_t_storico_workflow " +
		" WHERE id_storico_workflow=:id_storico_workflow";
			
	private static final String INSERT = "INSERT INTO moon_wf_t_storico_workflow" +
		" (id_storico_workflow, id_istanza, id_processo, id_stato_wf_partenza, nome_stato_wf_partenza, " +
		" id_stato_wf_arrivo, nome_stato_wf_arrivo, id_azione, nome_azione, dati_azione, " +
		" desc_destinatario, data_inizio, id_file_rendering, attore_upd, id_datiazione)" +
		" VALUES (:id_storico_workflow, :id_istanza , :id_processo, :id_stato_wf_partenza, :nome_stato_wf_partenza, " +
		":id_stato_wf_arrivo, :nome_stato_wf_arrivo, :id_azione, :nome_azione, :dati_azione, " +
		":desc_destinatario, :data_inizio, :id_file_rendering, :attore_upd, :id_datiazione)";
	
	private static final String UPDATE_DATAFINE = "UPDATE moon_wf_t_storico_workflow" + 
		" SET data_fine=:data_fine" +
		" WHERE id_istanza=:id_istanza AND data_fine IS NULL";
	
	private static final String UPDATE_DATAFINE_BY_ID = "UPDATE moon_wf_t_storico_workflow" + 
		" SET data_fine=:data_fine" +
		" WHERE id_storico_workflow=:id_storico_workflow";
	
	private static final String UPDATE_ID_FILE_RENDERED = "UPDATE moon_wf_t_storico_workflow" + 
		" SET id_file_rendering=:id_file_rendering" +
		" WHERE id_storico_workflow=:id_storico_workflow";
	
	private static final String SET_DATAFINE_NULL = "UPDATE moon_wf_t_storico_workflow" + 
		" SET data_fine=NULL " +
		" WHERE id_istanza=:id_istanza and data_inizio = "
		+ "(select MAX(data_inizio) from moon_wf_t_storico_workflow where id_istanza=:id_istanza)";
	
	private static final String FIND_OPERATORE = 
		"SELECT u.id_utente, u.identificativo_utente, u.nome, u.cognome," +
		" u.username, u.password, u.email, u.fl_attivo, u.id_tipo_utente, u.data_ins, u.data_upd, u.attore_ins, u.attore_upd" +
		" FROM moon_fo_t_utente u, moon_wf_t_storico_workflow WF" +
		" WHERE identificativo_utente = WF.attore_upd and " +
		"id_istanza=:id_istanza  AND data_fine IS NULL ";

	private static final String SEQ_ID = "SELECT nextval('moon_wf_t_storico_workflow_id_storico_workflow_seq')";
	
	@Override
	public Optional<StoricoWorkflowEntity> findLastStorico(Long idIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findLastStorico] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			StoricoWorkflowEntity lastStorico = (StoricoWorkflowEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_LAST_STORICO, params, BeanPropertyRowMapper.newInstance(StoricoWorkflowEntity.class) );
			return Optional.of(lastStorico);
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.warn("[" + CLASS_NAME + "::findLastStorico] Nessun LastStorico for idIstanza = " + idIstanza + " return Optional.empty();", emptyEx);
			return Optional.empty();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findLastStorico] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public StoricoWorkflowEntity findStoricoWorkflowById(Long idStoricoWorkflow) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findStoricoWorkflowById] idStoricoWorkflow: " + idStoricoWorkflow);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_storico_workflow", idStoricoWorkflow);
			return (StoricoWorkflowEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(StoricoWorkflowEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findStoricoWorkflowById] Elemento non trovato idStoricoWorkflow: " + idStoricoWorkflow, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findStoricoWorkflowById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<StoricoWorkflowEntity> findByIdIstanza(Long idIstanza) throws DAOException {
		try {
			List<StoricoWorkflowEntity> result = null;
			LOG.debug("[" + CLASS_NAME + "::findByIdIstanza] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			result = (List<StoricoWorkflowEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, params, BeanPropertyRowMapper.newInstance(StoricoWorkflowEntity.class) );
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanza] Errore database for idIstanza:" + idIstanza, e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public StoricoWorkflowEntity findLastStoricoListAzioni(Long idIstanza, List<String> listAzioni) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findLastStoricoListAzioni] idIstanza: " + idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			
			String QUERY_NAME = FIND_LAST_STORICO_LISTAZIONI;
			StringBuilder sb = new StringBuilder(QUERY_NAME);
			
			if (listAzioni.size() > 0)
			{
				sb.append(" AND id_azione in ( select a.id_azione from moon_wf_d_azione a where ");
				for (String codiceAzione: listAzioni) {
					String appendClause = (listAzioni.indexOf(codiceAzione) == 0) ? " a.codice_azione = '" + codiceAzione +"'" :
						" or a.codice_azione = '" + codiceAzione +"'";
					sb.append(appendClause);
				}
				sb.append(")");
			}
			sb.append(" ORDER BY data_inizio DESC LIMIT 1");
			
			return (StoricoWorkflowEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(sb.toString(), params, BeanPropertyRowMapper.newInstance(StoricoWorkflowEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findLastStoricoListAzioni] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findLastStoricoListAzioni] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public Long insert(StoricoWorkflowEntity entity) throws DAOException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			}
			Long idStoricoWorkflow = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdStoricoWorkflow(idStoricoWorkflow);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::insert] Voglio INSERIRE: "+entity);
			}
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			}
			return idStoricoWorkflow;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO STORICO WORKFLOW");
		}
	}
	
    private MapSqlParameterSource mapEntityParameters(StoricoWorkflowEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_storico_workflow", entity.getIdStoricoWorkflow());
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("id_processo", entity.getIdProcesso());
    	params.addValue("id_stato_wf_partenza", entity.getIdStatoWfPartenza());
    	params.addValue("id_stato_wf_arrivo", entity.getIdStatoWfArrivo());
    	params.addValue("id_azione", entity.getIdAzione());
    	params.addValue("nome_stato_wf_partenza", entity.getNomeStatoWfPartenza(), Types.VARCHAR);
    	params.addValue("nome_stato_wf_arrivo", entity.getNomeStatoWfArrivo(), Types.VARCHAR);
    	params.addValue("nome_azione", entity.getNomeAzione(), Types.VARCHAR);
    	params.addValue("dati_azione", entity.getDatiAzione(), Types.VARCHAR);
    	params.addValue("desc_destinatario", entity.getDescDestinatario(), Types.VARCHAR);
    	params.addValue("data_inizio", entity.getDataInizio(), Types.TIMESTAMP);
    	params.addValue("id_file_rendering", entity.getIdFileRendering());
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	params.addValue("id_datiazione", entity.getIdDatiazione());
    	return params;
    }

	@Override
	public int updateDataFine(Date data, Long idIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateDataFine] IN data fine: "+ data.toString());
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_DATAFINE, mapDatiEntityParameters(data,idIstanza));
			LOG.debug("[" + CLASS_NAME + "::updateDataFine] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateDataFine] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO DATA FINE");
		}
	}
	
	public int updateDataFineById(Date data, Long idStoricoWorkflow) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateDataFineById] IN data fine: " + data.toString());
			MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("data_fine", data, Types.TIMESTAMP);
	    	params.addValue("id_storico_workflow", idStoricoWorkflow);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_DATAFINE_BY_ID, params);
			LOG.debug("[" + CLASS_NAME + "::updateDataFineById] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateDataFineById] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO DATA FINE ID");
		}
	}
    
	@Override
	public int setDataFineNull(Long idIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::setDataFineNull] idIstanza: "+ idIstanza);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(SET_DATAFINE_NULL, mapDataFineNullParameters(idIstanza));
			LOG.debug("[" + CLASS_NAME + "::setDataFineNull] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::setDataFineNull] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO DATA FINE NULL");
		}
	}
	
	private MapSqlParameterSource mapDatiEntityParameters(Date data, Long idIstanza) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("data_fine", data, Types.TIMESTAMP);
    	params.addValue("id_istanza", idIstanza);
    	return params;
    }
	
	private MapSqlParameterSource mapDataFineNullParameters(Long idIstanza) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_istanza", idIstanza);
    	return params;
    }

	
	@Override
	public StoricoWorkflowEntity findLastStoricoAzione(Long idIstanza, Long idAzione) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findLastStoricoAzione] idIstanza: " + idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("id_azione", idAzione);
			return (StoricoWorkflowEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_LAST_STORICO_AZIONE, params, BeanPropertyRowMapper.newInstance(StoricoWorkflowEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findLastStoricoAzione] Elemento non trovato for idIstanza:" + idIstanza + " idAzione:" + idAzione, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findLastStoricoAzione] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public UtenteEntity findOperatore(Long idIstanza) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findOperatore] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			UtenteEntity operatore = new UtenteEntity();
			
			List<UtenteEntity> result = null;
			result = ( List<UtenteEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_OPERATORE, params, BeanPropertyRowMapper.newInstance(UtenteEntity.class) );
			if (result.size() >0 ) {
				operatore = result.get(0);
			} else {
				operatore.setCognome("");
				operatore.setNome("");
			}
			return operatore;
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findOperatore] Elemento Istanza non trovata: " + idIstanza);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findOperatore] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<StoricoWorkflowEntity> find(StoricoWorkflowFilter filter) throws DAOException {
		List<StoricoWorkflowEntity> result = null;
		Optional<Long> longOpt = Optional.empty();
		Optional<String> strOpt = Optional.empty();
		Optional<Integer> intOpt = Optional.empty();
		StringBuilder sb = new StringBuilder(FIND_BY_FILTER); 
		List par = new ArrayList();
		
		LOG.debug("[" + CLASS_NAME + "::find] IN filter: " + filter);
		
		if (filter != null) {
			longOpt = filter.getIdIstanza();
			if (longOpt.isPresent()) {
				sb.append(" AND sw.id_istanza = ? ");
				par.add(longOpt.get());
			}
			longOpt = filter.getIdProcesso();
			if (longOpt.isPresent()) {
				sb.append(" AND sw.id_processo = ? ");
				par.add(longOpt.get());
			}
			intOpt = filter.getIdStatoWfPartenza();
			if (intOpt.isPresent()) {
				sb.append(" AND sw.id_stato_wf_partenza = ? ");
				par.add(intOpt.get());
			}
			intOpt = filter.getIdStatoWfArrivo();
			if (intOpt.isPresent()) {
				sb.append(" AND sw.id_stato_wf_arrivo = ? ");
				par.add(intOpt.get());
			}
			strOpt = filter.getCodiceAzione();
			if (strOpt.isPresent()) {
				sb.append(" AND a.codice_azione = ? ");
				par.add(strOpt.get());
			}
		}

		String sql = new StringBuilder().append(sb).toString();
		result = (List<StoricoWorkflowEntity>) getCustomJdbcTemplate().query(sql, new PreparedStatementSetter() {

			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				int index = 1;
				for (Object param : par) {
					LOG.debug("[" + CLASS_NAME + "::find] par: " + param);
					preparedStatement.setObject(index, param);
					index++;
				}
			}
		}, BeanPropertyRowMapper.newInstance(StoricoWorkflowEntity.class));

		return result;
	}

	@Override
	public int updateIdFileRendered(Long idStoricoWorkflow, Long idFile) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateIdFileRendered] IN idStoricoWorkflow= "+ idStoricoWorkflow + " idFile= "+ idFile);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_storico_workflow", idStoricoWorkflow);
			params.addValue("id_file_rendering", idFile);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ID_FILE_RENDERED, params);
			LOG.debug("[" + CLASS_NAME + "::updateIdFileRendered] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateIdFileRendered] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO updateIdFileRendered");
		}
	}

}
