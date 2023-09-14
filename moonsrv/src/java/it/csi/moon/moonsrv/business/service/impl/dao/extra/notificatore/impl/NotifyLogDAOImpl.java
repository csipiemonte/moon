/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.NotifyLogEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotifyLogDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

@Component
public class NotifyLogDAOImpl extends JdbcTemplateDAO implements NotifyLogDAO {

	private static final String CLASS_NAME = "NotifyLogDAOImpl";
	
	private static final  String SELECT  = "SELECT id_log_notify, data_log_notify, id_ente, id_modulo, id_istanza, uuid_messaggio, uuid_payload, esito, email, sms, push, mex, io";
	
	private static final  String INSERT = "INSERT INTO moon_ntf_log (id_ente, id_modulo, id_istanza, uuid_messaggio, uuid_payload, esito, email, sms, push, mex, io)" + 
			" VALUES (:id_ente, :id_modulo, :id_istanza, :uuid_messaggio, :uuid_payload, :esito, :email, :sms, :push, :mex, :io)";
	
	private static final  String FIND_BY_ISTANZA = SELECT + " FROM moon_ntf_log WHERE id_istanza=:id_istanza";

	@Override
	public List<NotifyLogEntity> findByIdIstanza(Long idIstanza) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);

			return (List<NotifyLogEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ISTANZA, params,
				BeanPropertyRowMapper.newInstance(NotifyLogEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanza] Errore with idIstanza: " + idIstanza, e);
			throw new DAOException();
		}
	}

	@Override
	public int insert(NotifyLogEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapNotifyLogEntityParametersForInsert(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT LOG NOTIFY");
		}
	}
	
	   private MapSqlParameterSource mapNotifyLogEntityParametersForInsert(NotifyLogEntity entity) {
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	
	    	params.addValue("id_ente", entity.getIdEnte());
	    	params.addValue("id_modulo", entity.getIdModulo());
	    	params.addValue("id_istanza", entity.getIdIstanza());
	    	params.addValue("uuid_messaggio", entity.getUuidMessaggio());
	    	params.addValue("uuid_payload" , entity.getUuidPayload());
	    	params.addValue("esito", entity.getEsito());
	    	params.addValue("email", entity.getEmail());
	    	params.addValue("sms", entity.getSms());
	    	params.addValue("push", entity.getPush());
	    	params.addValue("mex", entity.getMex());
	    	params.addValue("io", entity.getIo());
	    	
	    	return params;
	    }

}
