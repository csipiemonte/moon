/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.LogEmailEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso ai log email
 * <br>
 * <br>Tabella principale : moon_fo_t_log_email
 * 
 * @see LogEmailEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 10/06/2020 - versione iniziale
 */
@Component
public class LogEmailDAOImpl extends JdbcTemplateDAO implements LogEmailDAO {
	
	private static final String CLASS_NAME = "LogEmailDAOImpl";
	
	private static final  String SELECT  = "SELECT id_log_email, data_log_email, id_tipologia, id_ente, id_modulo, id_istanza, email_destinatario, tipo_email, esito";
	
	private static final String ELENCO = SELECT +
		" FROM moon_fo_t_log_email";
	
	private static final String ELENCO_BY_ISTANZA = ELENCO +
		" WHERE id_istanza=:id_istanza";
	
	private static final  String INSERT = "INSERT INTO moon_fo_t_log_email (id_tipologia, id_ente, id_modulo, id_istanza, email_destinatario, tipo_email, esito, id_storico_workflow)" + 
		" VALUES (:id_tipologia, :id_ente, :id_modulo, :id_istanza, :email_destinatario, :tipo_email, :esito, :id_storico_workflow)";

	@Override
	public List<LogEmailEntity> findByIdIstanza(Long idIstanza) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);

			return (List<LogEmailEntity>) getCustomNamedParameterJdbcTemplateImpl().query(ELENCO_BY_ISTANZA, params,
				BeanPropertyRowMapper.newInstance(LogEmailEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanza] Errore with idIstanza: " + idIstanza, e);
			throw new DAOException();
		}
	}
	
	@Override
	public int insert(LogEmailEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapLogEmailEntityParametersForInsert(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT LOG EMAIL");
		}
	}
	
    private MapSqlParameterSource mapLogEmailEntityParametersForInsert(LogEmailEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_tipologia", entity.getIdTipologia());
    	params.addValue("id_ente", entity.getIdEnte());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("email_destinatario", entity.getEmailDestinatario());
    	params.addValue("tipo_email" , entity.getTipoEmail());
    	params.addValue("esito", entity.getEsito());
    	params.addValue("id_storico_workflow", entity.getIdStoricoWorkflow());
    	
    	return params;
    }

}
