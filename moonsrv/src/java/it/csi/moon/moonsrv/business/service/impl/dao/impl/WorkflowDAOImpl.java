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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.DatiAzioneEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.WorkflowEntity;
import it.csi.moon.commons.entity.WorkflowFilter;
import it.csi.moon.moonsrv.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso allo storicoworkflow e oggetti direttamente correlati
 * 
 * @see IstanzaEntity
 *
 * @author Alberto
 *
 */
@Component
public class WorkflowDAOImpl extends JdbcTemplateDAO implements WorkflowDAO {

	private static final String CLASS_NAME = "WorkflowDAOImpl";

	private static final String ID_WORKFLOW = "id_workflow";
	
	private static final String SELECT_WF_AZ = 
			"SELECT wf.id_workflow, wf.id_processo, wf.id_stato_wf_partenza, wf.id_stato_wf_arrivo," +
			" wf.campo_condizione, wf.valore_condizione, " + 
			" wf.id_azione, az.nome_azione, az.codice_azione, " + 
			" wf.email_destinatario, wf.id_utente_destinatario, wf.id_tipo_utente_destinatario, wf.id_gruppo_utenti_destinatari, " + 
			" wf.flag_automatico, wf.flag_annullabile, wf.id_datiazione, wf.flag_stato_istanza, wf.flag_api, wf.id_condition ";

	private static final String SELECT_FROM = SELECT_WF_AZ +
			" FROM moon_wf_d_workflow wf, moon_wf_d_azione az";
	
	private static final String FIND_WHERE = " WHERE az.id_azione = wf.id_azione";

	private static final String FIND_BY_ID = SELECT_FROM +
			" WHERE az.id_azione = wf.id_azione " +
			" AND wf.id_workflow = :id_workflow ";

	private static final String FIND_DATI_AZIONE_BY_ID = "SELECT id_datiazione, codice_datiazione, " +
			" versione_datiazione, struttura, descrizione_datiazione" +
			" FROM moon_wf_d_dati_azione " +
			" WHERE id_datiazione = :id_datiazione ";
	
	private static final String FIND_DATI_AZIONE_BY_CODICE = "SELECT id_datiazione, codice_datiazione, " +
			" versione_datiazione, struttura, descrizione_datiazione" +
			" FROM moon_wf_d_dati_azione " +
			" WHERE codice_datiazione = :codice_datiazione ";
	
	private static final String FIND_BY_ID_PROCESSO_PARTENZA_AZIONE = SELECT_FROM
			+ " WHERE wf.id_processo = :id_processo "
			+ " AND wf.id_azione = :id_azione"
			+ " AND wf.id_stato_wf_partenza = :id_stato_wf_partenza"
			+ " AND az.id_azione = wf.id_azione ";

	
	private static final String INSERT = "INSERT INTO moon_wf_d_workflow" +
		" (id_workflow, id_processo, id_stato_wf_partenza, id_stato_wf_arrivo, campo_condizione, valore_condizione, id_azione, email_destinatario, id_utente_destinatario, id_tipo_utente_destinatario, id_gruppo_utenti_destinatari, flag_archiviabile, flag_annullabile, id_datiazione, flag_stato_istanza, flag_api, flag_automatico, id_condition)" + 
		" VALUES (:id_workflow, :id_processo, :id_stato_wf_partenza, :id_stato_wf_arrivo, :campo_condizione, :valore_condizione, :id_azione, :email_destinatario, :id_utente_destinatario, :id_tipo_utente_destinatario, :id_gruppo_utenti_destinatari, :flag_archiviabile, :flag_annullabile, :id_datiazione, :flag_stato_istanza, :flag_api, :flag_automatico, :id_condition)";

	private static final String DELETE = "DELETE FROM moon_wf_d_workflow WHERE id_workflow = :id_workflow";

	private static final String UPDATE = "UPDATE moon_wf_d_workflow" +
		" SET id_stato_wf_partenza=:id_stato_wf_partenza, id_stato_wf_arrivo=:id_stato_wf_arrivo, campo_condizione=:campo_condizione, valore_condizione=:valore_condizione" +
		", id_azione=:id_azione, email_destinatario=:email_destinatario, id_utente_destinatario=:id_utente_destinatario" +
		", id_tipo_utente_destinatario=:id_tipo_utente_destinatario, id_gruppo_utenti_destinatari=:id_gruppo_utenti_destinatari" +
		", flag_archiviabile=:flag_archiviabile, flag_annullabile=:flag_annullabile, id_datiazione=:id_datiazione, flag_stato_istanza=:flag_stato_istanza" +
		", flag_api=:flag_api, flag_automatico=:flag_automatico, id_condition=:id_condition" +
		" WHERE id_workflow = :id_workflow";

	private static final String SEQ_ID = "SELECT nextval('moon_wf_d_workflow_id_workflow_seq')";


	@SuppressWarnings("rawtypes")
	@Override
	public List<WorkflowEntity> find(WorkflowFilter filter) throws DAOException {
		List<WorkflowEntity> result = null;
		List par = new ArrayList();
		StringBuilder sb = new StringBuilder().append(SELECT_FROM)
				.append(FIND_WHERE)
				.append(readFilter(filter, par));
		
		result = (List<WorkflowEntity>) getCustomJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				int index = 1;
				for (Object param : par) {
					LOG.debug("[" + CLASS_NAME + "::find] par: " + param);
					preparedStatement.setObject(index, param);
					index++;
				}
			}
		}, BeanPropertyRowMapper.newInstance(WorkflowEntity.class));

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private StringBuilder readFilter(WorkflowFilter filter, List par) {
		StringBuilder sb = new StringBuilder();
		LOG.debug("[" + CLASS_NAME + "::find] IN filter: " + filter);
		if (filter != null) {
			Optional<Long> longOpt = Optional.empty();
			Optional<String> strOpt = Optional.empty();
			Optional<Integer> intOpt = Optional.empty();
			longOpt = filter.getIdProcesso();
			if (longOpt.isPresent()) {
				par.add(longOpt.get());
				sb.append(" AND wf.id_processo = ? ");
			}
			intOpt = filter.getIdStatoWfPartenza();
			if (intOpt.isPresent()) {
				par.add(intOpt.get());
				sb.append(" AND wf.id_stato_wf_partenza = ? ");
			}
			intOpt = filter.getIdStatoWfArrivo();
			if (intOpt.isPresent()) {
				par.add(intOpt.get());
				sb.append(" AND wf.id_stato_wf_arrivo = ? ");
			}
			longOpt = filter.getIdAzione();
			if (longOpt.isPresent()) {
				par.add(longOpt.get());
				sb.append(" AND wf.id_azione = ? ");
			}
			longOpt = filter.getIdModulo();
			if (longOpt.isPresent()) {
				par.add(longOpt.get());
				sb.append(" AND wf.id_processo = (SELECT mp.id_processo FROM moon_wf_r_modulo_processo mp WHERE mp.id_modulo = ?)");
			}
			strOpt = filter.getFlagApi();
			if (strOpt.isPresent()) {
				par.add(strOpt.get());
				sb.append(" AND wf.flag_api = ? ");
			}
			if (filter.isEscludiAzioniUtenteCompilante()) {
				sb.append(" AND (wf.id_tipo_utente_destinatario is null OR wf.id_tipo_utente_destinatario <> 4) ");
			}
//			TEST per API cambio stato
//			else {
//				sb.append(" AND (wf.id_tipo_utente_destinatario = 4 OR wf.id_gruppo_utenti_destinatari=4) ");
//			}
			if (filter.isEscludiAzioniDiSistema()) {
				// esclude sempre le azioni che devono essere eseguite dal sistema come salva_protocollo
				sb.append(" AND (wf.id_tipo_utente_destinatario is null OR wf.id_tipo_utente_destinatario <> 5) ");
			}
			Optional<Boolean> boolOpt = filter.isAutomatica();
			if (boolOpt.isPresent()) {
				if (Boolean.TRUE.equals(boolOpt.get())) {
					sb.append(" AND wf.flag_automatico = 'S' ");
				}
				if (Boolean.FALSE.equals(boolOpt.get())) {
					sb.append(" AND wf.flag_automatico = 'N' ");
				}
			}
		}
		return sb;
	}
	
	@Override
	public WorkflowEntity findById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findById] IN id: " + id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_workflow", id);
			return (WorkflowEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params,
					BeanPropertyRowMapper.newInstance(WorkflowEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public WorkflowEntity findByProcessoAzione(Long idProcesso, Integer idStatoPartenza, Long idAzione) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByProcessoAzione] IN idProcesso=" + idProcesso + "  idStatoPartenza=" + idStatoPartenza + "  idAzione=" + idAzione);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_processo", idProcesso);
			params.addValue("id_stato_wf_partenza", idStatoPartenza);
			params.addValue("id_azione", idAzione);
			return (WorkflowEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_PROCESSO_PARTENZA_AZIONE,
					params, BeanPropertyRowMapper.newInstance(WorkflowEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByProcessoAzione] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByProcessoAzione] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public DatiAzioneEntity findDatiAzioneById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findDatiAzioneById] IN id: " + id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_datiazione", id);
			return (DatiAzioneEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_DATI_AZIONE_BY_ID,
					params, BeanPropertyRowMapper.newInstance(DatiAzioneEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findDatiAzioneById] Elemento non trovato: id_datiazione=" + id + "  " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findDatiAzioneById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public DatiAzioneEntity findDatiAzioneByCodice(String codice) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findDatiAzioneByCodice] IN codice: "+codice);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_datiazione", codice);
			return (DatiAzioneEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_DATI_AZIONE_BY_CODICE,
					params, BeanPropertyRowMapper.newInstance(DatiAzioneEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findDatiAzioneByCodice] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findDatiAzioneByCodice] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	/**
	 * Inserisce una riga di workflow (dettaglio di processo) nel sistema
	 * 
	 * @param {@code entity} la riga di workflow (dettaglio di processo) da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idWorkflow} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Long insert(WorkflowEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idWorkflow = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdWorkflow(idWorkflow);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapWorkflowEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idWorkflow;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO WORKFLOW");
		}

	}

	/**
	 * Aggiorna una riga di workflow (dettaglio di processo) nel sistema sulla base della sua chiave primaria {@code idWorkflow} 
	 * 
	 * @param {@code entity} riga di workflow (dettaglio di processo) da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun ente aggiornato, 1 se wokflow aggiornato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(WorkflowEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: " + entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapWorkflowEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO WORKFLOW");
		}
	}

	/**
	 * Cancella una riga di workflow (dettaglio di processo) dal sistema per chiave primaria {@code idWorkflow}.
	 * Per essere attuabile, non devono essere presenti referenze della riga di workflow in cancellazione nelle entity collegate
	 * 
	 * @param {@code idWorkflow} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ente cancellato, 1 se workflow cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Long idWorkflow) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idWorkflow: " + idWorkflow);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_WORKFLOW, idWorkflow);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE WORKFLOW");
		}
	}

	/**
	 * Cancella una riga di workflow (dettaglio di processo) dal sistema per entita Processo {@code entity}. L'entita deve avere valorizzata almeno la sua chiave primaria {@code idWorkflow}.
	 * Per essere attuabile, non devono essere presenti referenze dell'ente in cancellazione nelle entity collegate
	 * 
	 * @param {@code entity} la riga di workflow (dettaglio di processo) da cancellare. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ente cancellato, 1 se workflow cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(WorkflowEntity entity) throws DAOException {
		return delete(entity.getIdWorkflow());
	}
	
	
	// id_workflow, id_processo, id_stato_wf_partenza, id_stato_wf_arrivo, campo_condizione, valore_condizione, id_azione,
	// email_destinatario, id_utente_destinatario, id_tipo_utente_destinatario, id_gruppo_utenti_destinatari, 
	// flag_archiviabile, flag_annullabile, id_datiazione, flag_stato_istanza, flag_api, flag_automatico, id_condition
    private MapSqlParameterSource mapWorkflowEntityParameters(WorkflowEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue(ID_WORKFLOW, entity.getIdWorkflow());
    	params.addValue("id_processo", entity.getIdProcesso());
    	params.addValue("id_stato_wf_partenza", entity.getIdStatoWfPartenza());
    	params.addValue("id_stato_wf_arrivo" , entity.getIdStatoWfArrivo());
    	params.addValue("campo_condizione", entity.getCampoCondizione());
    	params.addValue("valore_condizione", entity.getValoreCondizione());
    	params.addValue("id_azione" , entity.getIdAzione());
    	
    	params.addValue("email_destinatario", entity.getEmailDestinatario());
    	params.addValue("id_utente_destinatario", entity.getIdUtenteDestinatario());
    	params.addValue("id_tipo_utente_destinatario" , entity.getIdTipoUtenteDestinatario());
    	params.addValue("id_gruppo_utenti_destinatari", entity.getIdGruppoUtentiDestinatari());
    	
    	params.addValue("flag_archiviabile", entity.getFlagArchiviabile());
    	params.addValue("flag_annullabile" , entity.getFlagAnnullabile());
    	params.addValue("id_datiazione", entity.getIdDatiazione());
    	params.addValue("flag_stato_istanza", entity.getFlagStatoIstanza());
    	params.addValue("flag_api" , entity.getFlagApi());
    	params.addValue("flag_automatico", entity.getFlagAutomatico());
    	params.addValue("id_condition", entity.getIdCondition());
    	
    	return params;
    }

}
