/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.impl;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.moon.moonfobl.util.LoggerAccessor;
import it.csi.moon.moonfobl.business.service.impl.dao.component.CustomJdbcTemplateImpl;
import it.csi.moon.moonfobl.business.service.impl.dao.component.CustomNamedParameterJdbcTemplateImpl;

/**
 * @author franc
 * Classe da cui ereditano tutti i DAO che accedono al database
 */
public abstract class JdbcTemplateDAO {
	
	protected static final String MSG_ERR_DEFAULT = "ERORE BLOCCANTE ESECUZIONE COMANDO DATABASE";
	protected static final Logger LOG = LoggerAccessor.getLoggerIntegration();
	
	private CustomNamedParameterJdbcTemplateImpl customNamedParameterJdbcTemplate;
	private CustomJdbcTemplateImpl	customJdbcTemplate;
	
	public CustomNamedParameterJdbcTemplateImpl getCustomNamedParameterJdbcTemplateImpl() {
		return customNamedParameterJdbcTemplate;
	}
	@Autowired
	public void setCustomNamedParameterJdbcTemplate(CustomNamedParameterJdbcTemplateImpl customNamedParameterJdbcTemplate) {
		this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
	}
	public CustomJdbcTemplateImpl getCustomJdbcTemplate() {
		return customJdbcTemplate;
	}
	@Autowired
	public void setCustomJdbcTemplate(CustomJdbcTemplateImpl customJdbcTemplate) {
		this.customJdbcTemplate = customJdbcTemplate;
	}
	
	public String getMsgErrDefault() {
		return this.MSG_ERR_DEFAULT;
	}
	
	// readFilter
	protected <T> void readFilterProperty(Optional<T> opt, String sqlToAppend, String paramName, StringBuilder sb, MapSqlParameterSource params) {
		if (opt.isPresent()) {
			sb.append(sqlToAppend);
			params.addValue(paramName, opt.get());
		}
	}
	protected void readFilterPropertyUC(Optional<String> opt, String sqlToAppend, String paramName, StringBuilder sb, MapSqlParameterSource params) {
		if (opt.isPresent()) {
			sb.append(sqlToAppend);
			params.addValue(paramName, opt.get().toUpperCase());
		}
	}
	protected void readFilterPropertyContains(Optional<String> strOpt, String sqlToAppend, String paramName, StringBuilder sb, MapSqlParameterSource params) {
		if (strOpt.isPresent()) {
			sb.append(sqlToAppend);
			params.addValue(paramName, "%"+strOpt.get()+"%");
		}
	}
	protected void readFilterPropertyContainsUC(Optional<String> strOpt, String sqlToAppend, String paramName, StringBuilder sb, MapSqlParameterSource params) {
		if (strOpt.isPresent()) {
			sb.append(sqlToAppend);
			params.addValue(paramName, "%"+strOpt.get().toUpperCase()+"%");
		}
	}
	//
	protected <T> void readFilterPropertyWa(Optional<T> opt, String sqlToAppend, String paramName, StringBuilder sb, MapSqlParameterSource params) {
		if (opt.isPresent()) {
			appendWhereOrAnd(sb).append(sqlToAppend);
			params.addValue(paramName, opt.get());
		}
	}
	protected void readFilterPropertyUCWa(Optional<String> opt, String sqlToAppend, String paramName, StringBuilder sb, MapSqlParameterSource params) {
		if (opt.isPresent()) {
			appendWhereOrAnd(sb).append(sqlToAppend);
			params.addValue(paramName, opt.get().toUpperCase());
		}
	}
	protected void readFilterPropertyContainsWa(Optional<String> strOpt, String sqlToAppend, String paramName, StringBuilder sb, MapSqlParameterSource params) {
		if (strOpt.isPresent()) {
			appendWhereOrAnd(sb).append(sqlToAppend);
			params.addValue(paramName, "%"+strOpt.get()+"%");
		}
	}
	protected void readFilterPropertyContainsUCWa(Optional<String> strOpt, String sqlToAppend, String paramName, StringBuilder sb, MapSqlParameterSource params) {
		if (strOpt.isPresent()) {
			appendWhereOrAnd(sb).append(sqlToAppend);
			params.addValue(paramName, "%"+strOpt.get().toUpperCase()+"%");
		}
	}
	protected StringBuilder appendWhereOrAnd(StringBuilder sb) {
		return sb.append(sb.length()==0?" WHERE ":" AND ");
	}
}
