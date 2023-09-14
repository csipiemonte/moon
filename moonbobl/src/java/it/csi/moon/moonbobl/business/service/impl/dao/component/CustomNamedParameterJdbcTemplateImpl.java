/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.component;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.SqlQueriesUtil;
	
@Component
public class CustomNamedParameterJdbcTemplateImpl extends NamedParameterJdbcTemplate {

	protected static Logger log = LoggerAccessor.getLoggerIntegration();
	
	
	@Autowired
	public CustomNamedParameterJdbcTemplateImpl(DataSource dataSource) {
		super(dataSource);
		
	}


	@Override
	public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
		SqlQueriesUtil.debugSQL(sql, batchValues);
		return super.batchUpdate(sql, batchValues);
	}


	@Override
	public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
		SqlQueriesUtil.debugSQL(sql, batchArgs);
		return super.batchUpdate(sql, batchArgs);
	}


	@Override
	public <T> T execute(String sql, Map<String, ?> paramMap, PreparedStatementCallback<T> action)
			throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("[CustomNamedParameterJdbcTemplateImpl::execute]-Esecuzione sql: " + sql);
		}
		return super.execute(sql, paramMap, action);
	}


	@Override
	public <T> T execute(String sql, SqlParameterSource paramSource, PreparedStatementCallback<T> action)
			throws DataAccessException {
		return super.execute(sql, paramSource, action);
	}


	@Override
	public int getCacheLimit() {
		return super.getCacheLimit();
	}


	@Override
	public JdbcOperations getJdbcOperations() {
		return super.getJdbcOperations();
	}


	@Override
	protected ParsedSql getParsedSql(String sql) {
		return super.getParsedSql(sql);
	}


	@Override
	protected PreparedStatementCreator getPreparedStatementCreator(String sql, SqlParameterSource paramSource) {
		return super.getPreparedStatementCreator(sql, paramSource);
	}


	@Override
	public <T> T query(String sql, Map<String, ?> paramMap, ResultSetExtractor<T> rse) throws DataAccessException {
		return super.query(sql, paramMap, rse);
	}


	@Override
	public void query(String sql, Map<String, ?> paramMap, RowCallbackHandler rch) throws DataAccessException {
		super.query(sql, paramMap, rch);
	}


	@Override
	public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
		return super.query(sql, paramMap, rowMapper);
	}


	@Override
	public <T> T query(String sql, SqlParameterSource paramSource, ResultSetExtractor<T> rse)
			throws DataAccessException {
		return super.query(sql, paramSource, rse);
	}


	@Override
	public void query(String sql, SqlParameterSource paramSource, RowCallbackHandler rch) throws DataAccessException {
		super.query(sql, paramSource, rch);
	}


	@Override
	public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper)
			throws DataAccessException {
		SqlQueriesUtil.debugSQL(sql, paramSource);
		return super.query(sql, paramSource, rowMapper);
	}


	public int queryForInt(String sql, Map<String, ?> paramMap) throws DataAccessException {
		return super.queryForObject(sql, paramMap,Integer.class);
	}


	public int queryForInt(String sql, SqlParameterSource paramSource) throws DataAccessException {
		return super.queryForObject(sql, paramSource,Integer.class);
	}


	@Override
	public <T> List<T> queryForList(String sql, Map<String, ?> paramMap, Class<T> elementType)
			throws DataAccessException {
		return super.queryForList(sql, paramMap, elementType);
	}


	@Override
	public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap) throws DataAccessException {
		return super.queryForList(sql, paramMap);
	}


	@Override
	public <T> List<T> queryForList(String sql, SqlParameterSource paramSource, Class<T> elementType)
			throws DataAccessException {
		return super.queryForList(sql, paramSource, elementType);
	}


	@Override
	public List<Map<String, Object>> queryForList(String sql, SqlParameterSource paramSource)
			throws DataAccessException {
		return super.queryForList(sql, paramSource);
	}


	public long queryForLong(String sql, Map<String, ?> paramMap) throws DataAccessException {
		return super.queryForObject(sql, paramMap, Long.class);
	}


	public long queryForLong(String sql, SqlParameterSource paramSource) throws DataAccessException {
		return super.queryForObject(sql, paramSource, Long.class);
	}


	@Override
	public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) throws DataAccessException {
		// TODO Auto-generated method stub
		return super.queryForMap(sql, paramMap);
	}


	@Override
	public Map<String, Object> queryForMap(String sql, SqlParameterSource paramSource) throws DataAccessException {
		return super.queryForMap(sql, paramSource);
	}


	@Override
	public <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws DataAccessException {
		return super.queryForObject(sql, paramMap, requiredType);
	}


	@Override
	public <T> T queryForObject(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
			throws DataAccessException {
		return super.queryForObject(sql, paramMap, rowMapper);
	}


	@Override
	public <T> T queryForObject(String sql, SqlParameterSource paramSource, Class<T> requiredType)
			throws DataAccessException {
		return super.queryForObject(sql, paramSource, requiredType);
	}


	@Override
	public <T> T queryForObject(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper)
			throws DataAccessException {
		SqlQueriesUtil.debugSQL(sql, paramSource);
		return super.queryForObject(sql, paramSource, rowMapper);
	}


	@Override
	public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap) throws DataAccessException {
		return super.queryForRowSet(sql, paramMap);
	}


	@Override
	public SqlRowSet queryForRowSet(String sql, SqlParameterSource paramSource) throws DataAccessException {
		return super.queryForRowSet(sql, paramSource);
	}


	@Override
	public void setCacheLimit(int cacheLimit) {
		super.setCacheLimit(cacheLimit);
	}


	@Override
	public int update(String sql, Map<String, ?> paramMap) throws DataAccessException {
		return super.update(sql, paramMap);
	}


	@Override
	public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder, String[] keyColumnNames)
			throws DataAccessException {
		return super.update(sql, paramSource, generatedKeyHolder, keyColumnNames);
	}


	@Override
	public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder)
			throws DataAccessException {
		return super.update(sql, paramSource, generatedKeyHolder);
	}


	@Override
	public int update(String sql, SqlParameterSource paramSource) throws DataAccessException {
		SqlQueriesUtil.debugSQL(sql, paramSource);
		return super.update(sql, paramSource);
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}


	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}


	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}


	@Override
	public int hashCode() {
		return super.hashCode();
	}


	@Override
	public String toString() {
		return super.toString();
	}

	
	private void logMapParameters(Map<String,?>parametri) {
		
	}
	
	
	
	
}
