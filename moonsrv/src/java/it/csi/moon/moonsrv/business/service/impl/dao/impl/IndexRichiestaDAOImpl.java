/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.IndexRichiestaEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.IndexRichiestaDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

@Component
public class IndexRichiestaDAOImpl extends JdbcTemplateDAO implements IndexRichiestaDAO{

	private static final String CLASS_NAME = "IndexRichiestaDAOImpl";

	private static final String SEQ_ID = "SELECT nextval('moon_idx_t_richiesta_id_richiesta_seq')";
	private static final String INSERT = "INSERT INTO moon_idx_t_richiesta (" + 
			" id_richiesta,data_inizio,data_fine,id_modulo,data_filter,istanze_proc )" + 
			" VALUES (?,?,?,?,?,?)";
	private static final String UPDATE = "UPDATE moon_idx_t_richiesta" + 
			" SET data_inizio=?, data_fine=?, id_modulo=?, data_filter=?, istanze_proc=?" +
			" WHERE id_richiesta=?";

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public Long insert(IndexRichiestaEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: " + entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdRichiesta(id);
			jdbcTemplate.update(INSERT,
					new SqlParameterValue(Types.BIGINT, entity.getIdRichiesta()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataInizio()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataFine()),
					new SqlParameterValue(Types.INTEGER, entity.getIdModulo()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataFilter() ),
					new SqlParameterValue(Types.INTEGER, entity.getIstanzeProc())
					);
			return entity.getIdRichiesta();
			
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO INDEXRICHIESTA ");
		}	
	}

	@Override
	public int update(IndexRichiestaEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: " + entity);
			int numRecord = jdbcTemplate.update(UPDATE,
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataInizio()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataFine()),
					new SqlParameterValue(Types.INTEGER, entity.getIdModulo()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataFilter() ),
					new SqlParameterValue(Types.INTEGER, entity.getIstanzeProc()),
					new SqlParameterValue(Types.BIGINT, entity.getIdRichiesta())
					);
			
			return numRecord;		
		}catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE INDEXRICHIESTA ");
		}
	}

}
