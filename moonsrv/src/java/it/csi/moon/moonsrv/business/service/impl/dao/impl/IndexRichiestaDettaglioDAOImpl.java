/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.IndexRichiestaDettaglioEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.IndexRichiestaDettaglioDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

@Component
public class IndexRichiestaDettaglioDAOImpl extends JdbcTemplateDAO implements IndexRichiestaDettaglioDAO {

	private static final String CLASS_NAME = "IndexRichiestaDettaglioDAOImpl";
	
	private static final String INSERT = "INSERT INTO moon_idx_t_richiesta_dettaglio (" + 
			" id_richiesta,tipo_file,id_istanza,id_allegato,id_file,stato,desc_stato )" + 
			" VALUES (?,?,?,?,?,?,?)";

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int insert(IndexRichiestaDettaglioEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: " + entity);
			
			jdbcTemplate.update(INSERT,
					new SqlParameterValue(Types.BIGINT, entity.getIdRichiesta()),
					new SqlParameterValue(Types.INTEGER, entity.getTipoFile()),
					new SqlParameterValue(Types.BIGINT, entity.getIdIstanza()),
					new SqlParameterValue(Types.BIGINT, entity.getIdAllegato()),
					new SqlParameterValue(Types.BIGINT, entity.getIdFile() ),
					new SqlParameterValue(Types.VARCHAR, entity.getStato()),
					new SqlParameterValue(Types.VARCHAR, entity.getDesc_stato())
					);
			return 1;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO INDEXRICHIESTA DETTAGLIO ");
		}
	}

}
