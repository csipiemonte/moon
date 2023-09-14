/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonfobl.business.service.impl.dao.ConsumerDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.ConsumerParameterEntity;
import it.csi.moon.moonfobl.business.service.impl.dto.ImageConsumerParameterEntity;
import it.csi.moon.moonfobl.business.service.impl.dto.LinkConsumerParameterEntity;
import it.csi.moon.moonfobl.business.service.impl.dto.TextConsumerParameterEntity;
import it.csi.moon.moonfobl.dto.moonfobl.ConsumerTypeEnum;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai parametri Consumer
 * <br>
 * <br>Tabella principale : moon_fo_t_consumer_parameter
 * 
 * @see ConsumerParameterEntity
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
@Component
public class ConsumerDAOImpl extends JdbcTemplateDAO implements ConsumerDAO {
	
	private static final String CLASS_NAME = "ConsumerDAOImpl";
	
	private static final String FIND_SELECT_TEXT_FIELDS = "select code_consumer, codice_ente, id, text from moon_fo_t_text_consumer_parameter where id = :id";
	private static final String FIND_SELECT_LINK_FIELDS = "select code_consumer, codice_ente, id, label, url from moon_fo_t_link_consumer_parameter where id = :id";
	private static final String FIND_SELECT_IMAGE_FIELDS = "select code_consumer, codice_ente, id, alt_text,image_data from moon_fo_t_image_consumer_parameter where id = :id";
	private static final String FIND_ALL_SELECT_TEXT_FIELDS = "select code_consumer, codice_ente, id, text from moon_fo_t_text_consumer_parameter where code_consumer = :code_consumer and codice_ente = :codice_ente";
	private static final String FIND_ALL_SELECT_LINK_FIELDS = "select code_consumer, codice_ente, id, label, url from moon_fo_t_link_consumer_parameter where code_consumer = :code_consumer and codice_ente = :codice_ente";
	private static final String FIND_ALL_SELECT_IMAGE_FIELDS = "select code_consumer, codice_ente, id, alt_text,image_data from moon_fo_t_image_consumer_parameter where code_consumer = :code_consumer and codice_ente = :codice_ente";

	
	@Override
	public <P> List<P> getParameters(String consumer, String codiceEnte)
			throws ItemNotFoundDAOException, DAOException {
		
		List<P> list = new ArrayList<>();
		
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getParameters] IN consumer: "+consumer);
				LOG.debug("[" + CLASS_NAME + "::getParameters] IN codiceEnte: "+codiceEnte);
			}
	
        list.addAll(this.getParameters(consumer, codiceEnte, ConsumerTypeEnum.TEXT.name())); 
        list.addAll(this.getParameters(consumer, codiceEnte,ConsumerTypeEnum.LINK.name()));  
        list.addAll(this.getParameters(consumer, codiceEnte,ConsumerTypeEnum.IMAGE.name()));  
			
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::getParameter] Elemento non trovato: " + consumer + "/" + codiceEnte);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getParameter] Elemento non trovato: " + consumer + "/" + codiceEnte, e);
			throw new DAOException(getMsgErrDefault());
		}
		
		return list;
	}

	@Override
	public <P> P getParameter(String consumer, String codiceEnte,String idParameter, String type)
			throws DAOException {

		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getParameter] IN consumer: " + consumer);
				LOG.debug("[" + CLASS_NAME + "::getParameter] IN codiceEnte: " + codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getParameter] IN idParameter: " + idParameter);
				LOG.debug("[" + CLASS_NAME + "::getParameter] IN type: " + type);
			}

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("code_consumer", consumer);
			params.addValue("codice_ente", codiceEnte);
			params.addValue("id", idParameter);

			ConsumerParameterEntity parameter = null;

			switch (ConsumerTypeEnum.byName(type)) {
			case TEXT:
				parameter = (TextConsumerParameterEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
						FIND_SELECT_TEXT_FIELDS, params,
						BeanPropertyRowMapper.newInstance(TextConsumerParameterEntity.class));
				break;
			case LINK:
				parameter = (LinkConsumerParameterEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
						FIND_SELECT_LINK_FIELDS, params,
						BeanPropertyRowMapper.newInstance(LinkConsumerParameterEntity.class));				
				break;
			case IMAGE:
				parameter = (ImageConsumerParameterEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
						FIND_SELECT_IMAGE_FIELDS, params,
						BeanPropertyRowMapper.newInstance(ImageConsumerParameterEntity.class));
				break;
			default:
				LOG.debug("[" + CLASS_NAME + "::getParameter] no type found");
			}

			return (P) parameter;

		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.info("[" + CLASS_NAME + "::getParameter] Elemento non trovato: " + consumer + "/" + codiceEnte);
			return null;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getParameter] Elemento non trovato: " + consumer + "/" + codiceEnte, e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	private <P> List<P> getParameters(String consumer, String codiceEnte, String type)
			throws ItemNotFoundDAOException, DAOException {
		
		List<P> list = new ArrayList<>();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("code_consumer", consumer);
		params.addValue("codice_ente", codiceEnte);
		
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getParameters] IN consumer: "+consumer);
				LOG.debug("[" + CLASS_NAME + "::getParameters] IN codiceEnte: "+codiceEnte);
				LOG.debug("[" + CLASS_NAME + "::getParameters] IN type: "+type);
			}
			
			switch (ConsumerTypeEnum.byName(type)) {
			case TEXT:
				list = (List<P>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_ALL_SELECT_TEXT_FIELDS, params, BeanPropertyRowMapper.newInstance(TextConsumerParameterEntity.class));
				break;
			case LINK:
				list = (List<P>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_ALL_SELECT_LINK_FIELDS, params, BeanPropertyRowMapper.newInstance(LinkConsumerParameterEntity.class));
				break;
			case IMAGE:
				list = (List<P>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_ALL_SELECT_IMAGE_FIELDS, params, BeanPropertyRowMapper.newInstance(ImageConsumerParameterEntity.class));
				break;
			default:
				LOG.debug("[" + CLASS_NAME + "::getParameters] no type found");
			}
			
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.info("[" + CLASS_NAME + "::getParameters] Elemento non trovato: " + consumer + "/" + codiceEnte+ "/" + type);
			return new ArrayList<>() ;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getParameters] Elemento non trovato: " + consumer + "/" + codiceEnte+ "/" + type, e);
			throw new DAOException(getMsgErrDefault());
		}
		
		return list;
	}
	
	
}
