/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.sql.Types;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.AuditDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.util.EnvProperties;



@Component	
public class AuditDAOImpl extends JdbcTemplateDAO implements AuditDAO {

	private final static String CLASS_NAME = "AuditDAOImpl";

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String INSERT_AUDIT_ROW = " INSERT INTO CSI_LOG_AUDIT " + 
													"(data_ora, id_app, ip_address, utente, operazione, ogg_oper, key_oper) "+
													"VALUES(now(), ?,  ?,  ?,  ?,  ?,  ?) ";

	
	
	@Override
	public int insertAuditEntity(AuditEntity auditEntity) throws DAOException {
		
		log.debug("[" + CLASS_NAME + "::insert] IN entity: " + auditEntity);
		try {
			
			return jdbcTemplate.update(INSERT_AUDIT_ROW,
					new SqlParameterValue(Types.VARCHAR, EnvProperties.readFromFile(EnvProperties.AUDIT_ID_APP)),
					new SqlParameterValue(Types.VARCHAR, StringUtils.left(auditEntity.getIpAddress(),40)),
					new SqlParameterValue(Types.VARCHAR, StringUtils.left(auditEntity.getUtente(),100) ),
					new SqlParameterValue(Types.VARCHAR, StringUtils.left(auditEntity.getOperazione().name(),50)),
					new SqlParameterValue(Types.VARCHAR, StringUtils.left(auditEntity.getOggettoOperazione(),500)),
					new SqlParameterValue(Types.VARCHAR, StringUtils.left(auditEntity.getKeyOperazione(),500))
						);		
		} catch (Exception ex ) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+ex.getMessage(),ex);
			throw new DAOException("ERRORE INSERIMENTO AUDIT");
		}
		
		
	}

	
	
	
	
	
	
	
}
