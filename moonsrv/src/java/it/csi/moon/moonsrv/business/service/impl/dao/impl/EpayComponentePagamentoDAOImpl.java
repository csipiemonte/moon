/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.EpayComponentePagamentoEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayComponentePagamentoDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tabella moon_ep_t_componente_pagamento EPAY
 * <br>
 * <br>Tabella moon_ep_t_componente_pagamento
 * <br>PK: idComponentePagamento
 * <br>
 * <br>Tabella : moon_ep_t_componente_pagamento
 * 
 * @see EpayComponentePagamentoEntity
 * 
 * @author Danilo
 * 
 */
@Component
public class EpayComponentePagamentoDAOImpl extends JdbcTemplateDAO implements EpayComponentePagamentoDAO {
	
	private static final String CLASS_NAME = "EpayComponentePagamentoDAOImpl";
	
	
	private static final String SELECT_FIELDS = "SELECT id_componente_pagamento, id_modulo, anno_accertamento, numero_accertamento, causale, importo, dati_specifici_riscossione, ordine, codice_tipo_versamento";

	private static final String FIND_BY_ID_MODULO  = SELECT_FIELDS +
		" FROM moon_ep_t_componente_pagamento" +
		" WHERE id_modulo = :id_modulo"+
		" ORDER BY ordine ASC";
	
	private static final String INSERT = "INSERT INTO id_componente_pagamento(id_componente_pagamento, id_modulo, anno_accertamento, numero_accertamento, causale, importo, dati_specifici_riscossione, ordine, codice_tipo_versamento)" + 
			" VALUES (:id_componente_pagamento, :id_modulo, :anno_accertamento, :numero_accertamento, :causale, :importo, :dati_specifici_riscossione, :ordine, :codice_tipo_versamento)";
		
	private static final String UPDATE = "UPDATE id_componente_pagamento" +
			" SET anno_accertamento=:anno_accertamento, numero_accertamento=:numero_accertamento, causale=:causale, importo=:importo, dati_specifici_riscossione=:dati_specifici_riscossione, ordine=:ordine, codice_tipo_versamento=:codice_tipo_versamento " +			
			" WHERE id_modulo = :id_modulo";
	
	private static final String SEQ_ID = "SELECT nextval('moon_ep_t_componente_pagamento_id_componente_pagamento_seq')";
	

	@Override
	public List<EpayComponentePagamentoEntity> findByIdModulo(Long idModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);			
			return (List<EpayComponentePagamentoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(EpayComponentePagamentoEntity.class));			
			
			
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE FIND COMPONENTE PAGAMENTO BY ID MODULO", "MOONSRV-30800");
//			throw new DAOException("");
		}
	}

	
	
    private MapSqlParameterSource mapEntityParameters(EpayComponentePagamentoEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_componente_pagamento", entity.getIdComponentePagamento());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("anno_accertamento", entity.getAnnoAccertamento());
    	params.addValue("numero_accertamento", entity.getNumeroAccertamento());
    	params.addValue("causale", entity.getCausale());
    	params.addValue("importo", entity.getImporto());
    	params.addValue("dati_specifici_riscossione", entity.getDatiSpecificiRiscossione());    	
    	params.addValue("ordine", entity.getOrdine());    	
    	params.addValue("codice_tipo_versamento", entity.getCodiceTipoVersamento());

    	return params;
    }

    
	@Override
	public Long insert(EpayComponentePagamentoEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdComponentePagamento(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT COMPONENTE PAGAMENTO ", "MOONSRV-30800");
		}
	}
	
	@Override
	public int update(EpayComponentePagamentoEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE UPDATE COMPONENTE PAGAMENTO ", "MOONSRV-30800");
		}
	}




}
