/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.component;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.ResultSetSupportingSqlParameter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class CustomJdbcTemplateImpl extends JdbcTemplate {
	protected static Logger log = LoggerAccessor.getLoggerIntegration();

	
	@Autowired
	public CustomJdbcTemplateImpl(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected void applyStatementSettings(Statement stmt) throws SQLException {
		super.applyStatementSettings(stmt);
	}

	@Override
	public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException {
		return super.batchUpdate(sql, pss);
	}

	@Override
	public <T> int[][] batchUpdate(String sql, Collection<T> batchArgs, int batchSize,
			ParameterizedPreparedStatementSetter<T> pss) {
		return super.batchUpdate(sql, batchArgs, batchSize, pss);
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) {
		return super.batchUpdate(sql, batchArgs, argTypes);
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		return super.batchUpdate(sql, batchArgs);
	}

	@Override
	public int[] batchUpdate(String[] sql) throws DataAccessException {
		return super.batchUpdate(sql);
	}

	@Override
	public Map<String, Object> call(CallableStatementCreator csc, List<SqlParameter> declaredParameters)
			throws DataAccessException {
		return super.call(csc, declaredParameters);
	}

	@Override
	protected Connection createConnectionProxy(Connection con) {
		return super.createConnectionProxy(con);
	}

	@Override
	protected Map<String, Object> createResultsMap() {
		return super.createResultsMap();
	}

	@Override
	public <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action) throws DataAccessException {
		return super.execute(csc, action);
	}

	@Override
	public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
		return super.execute(action);
	}

	@Override
	public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException {
		return super.execute(psc, action);
	}

	@Override
	public <T> T execute(StatementCallback<T> action) throws DataAccessException {
		return super.execute(action);
	}

	@Override
	public <T> T execute(String callString, CallableStatementCallback<T> action) throws DataAccessException {
		return super.execute(callString, action);
	}

	@Override
	public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
		return super.execute(sql, action);
	}

	@Override
	public void execute(String sql) throws DataAccessException {
		super.execute(sql);
	}

	@Override
	protected Map<String, Object> extractOutputParameters(CallableStatement cs, List<SqlParameter> parameters)
			throws SQLException {
		return super.extractOutputParameters(cs, parameters);
	}

	@Override
	protected Map<String, Object> extractReturnedResults(CallableStatement cs, List updateCountParameters,
			List resultSetParameters, int updateCount) throws SQLException {
		return super.extractReturnedResults(cs, updateCountParameters, resultSetParameters, updateCount);
	}

	@Override
	protected RowMapper<Map<String, Object>> getColumnMapRowMapper() {
		return super.getColumnMapRowMapper();
	}

	@Override
	public int getFetchSize() {
		return super.getFetchSize();
	}

	@Override
	public int getMaxRows() {
		return super.getMaxRows();
	}

	@Override
	public NativeJdbcExtractor getNativeJdbcExtractor() {
		return super.getNativeJdbcExtractor();
	}

	@Override
	public int getQueryTimeout() {
		return super.getQueryTimeout();
	}

	@Override
	protected <T> RowMapper<T> getSingleColumnRowMapper(Class<T> requiredType) {
		return super.getSingleColumnRowMapper(requiredType);
	}

	@Override
	protected void handleWarnings(SQLWarning warning) throws SQLWarningException {
		super.handleWarnings(warning);
	}

	@Override
	protected void handleWarnings(Statement stmt) throws SQLException {
		super.handleWarnings(stmt);
	}

	@Override
	public boolean isIgnoreWarnings() {
		return super.isIgnoreWarnings();
	}

	@Override
	public boolean isResultsMapCaseInsensitive() {
		return super.isResultsMapCaseInsensitive();
	}

	@Override
	public boolean isSkipResultsProcessing() {
		return super.isSkipResultsProcessing();
	}

	@Override
	public boolean isSkipUndeclaredResults() {
		return super.isSkipUndeclaredResults();
	}

	@Override
	protected PreparedStatementSetter newArgPreparedStatementSetter(Object[] args) {
		return super.newArgPreparedStatementSetter(args);
	}

	@Override
	protected PreparedStatementSetter newArgTypePreparedStatementSetter(Object[] args, int[] argTypes) {
		return super.newArgTypePreparedStatementSetter(args, argTypes);
	}

	@Override
	protected Map<String, Object> processResultSet(ResultSet rs, ResultSetSupportingSqlParameter param)
			throws SQLException {
		return super.processResultSet(rs, param);
	}

	@Override
	public <T> T query(PreparedStatementCreator psc, PreparedStatementSetter pss, ResultSetExtractor<T> rse)
			throws DataAccessException {
		return super.query(psc, pss, rse);
	}

	@Override
	public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) throws DataAccessException {
		return super.query(psc, rse);
	}

	@Override
	public void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws DataAccessException {
		super.query(psc, rch);
	}

	@Override
	public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws DataAccessException {
		return super.query(psc, rowMapper);
	}

	@Override
	public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse)
			throws DataAccessException {
		return super.query(sql, args, argTypes, rse);
	}

	@Override
	public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
		super.query(sql, args, argTypes, rch);
	}

	@Override
	public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
			throws DataAccessException {
		return super.query(sql, args, argTypes, rowMapper);
	}

	@Override
	public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) throws DataAccessException {
		return super.query(sql, args, rse);
	}

	@Override
	public void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException {
		super.query(sql, args, rch);
	}

	@Override
	public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
		return super.query(sql, args, rowMapper);
	}

	@Override
	public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
		return super.query(sql, pss, rse);
	}

	@Override
	public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException {
		super.query(sql, pss, rch);
	}

	@Override
	public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper)
			throws DataAccessException {
		return super.query(sql, pss, rowMapper);
	}

	@Override
	public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException {
		return super.query(sql, rse, args);
	}

	@Override
	public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
		return super.query(sql, rse);
	}

	@Override
	public void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException {
		super.query(sql, rch, args);
	}

	@Override
	public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
		super.query(sql, rch);
	}

	@Override
	public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
		return super.query(sql, rowMapper, args);
	}

	@Override
	public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		return super.query(sql, rowMapper);
	}
	
	public int queryForInt(String sql, Object... args) throws DataAccessException {
		return super.queryForObject(sql, Integer.class, args);
	}


	public int queryForInt(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return super.queryForObject(sql, Integer.class, args);
	}


	public int queryForInt(String sql) throws DataAccessException {
		return super.queryForObject(sql, Integer.class);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException {
		return super.queryForList(sql, elementType, args);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
		return super.queryForList(sql, elementType);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
		return super.queryForList(sql, args);
	}

	@Override
	public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException {
		return super.queryForList(sql, args, elementType);
	}

	@Override
	public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType)
			throws DataAccessException {
		return super.queryForList(sql, args, argTypes, elementType);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return super.queryForList(sql, args, argTypes);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
		return super.queryForList(sql);
	}


	public long queryForLong(String sql, Object... args) throws DataAccessException {
		return super.queryForObject(sql, Long.class, args);
	}

	public long queryForLong(String sql) throws DataAccessException {
		return super.queryForObject(sql, Long.class);
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
		return super.queryForMap(sql, args);
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return super.queryForMap(sql, args, argTypes);
	}

	@Override
	public Map<String, Object> queryForMap(String sql) throws DataAccessException {
		return super.queryForMap(sql);
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException {
		return super.queryForObject(sql, requiredType, args);
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
		return super.queryForObject(sql, requiredType);
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
		return super.queryForObject(sql, args, requiredType);
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType)
			throws DataAccessException {
		return super.queryForObject(sql, args, argTypes, requiredType);
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
			throws DataAccessException {
		return super.queryForObject(sql, args, argTypes, rowMapper);
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
		return super.queryForObject(sql, args, rowMapper);
	}

	@Override
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
		return super.queryForObject(sql, rowMapper, args);
	}

	@Override
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		return super.queryForObject(sql, rowMapper);
	}

	@Override
	public SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException {
		return super.queryForRowSet(sql, args);
	}

	@Override
	public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return super.queryForRowSet(sql, args, argTypes);
	}

	@Override
	public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
		return super.queryForRowSet(sql);
	}

	@Override
	public void setFetchSize(int fetchSize) {
		super.setFetchSize(fetchSize);
	}

	@Override
	public void setIgnoreWarnings(boolean ignoreWarnings) {
		super.setIgnoreWarnings(ignoreWarnings);
	}

	@Override
	public void setMaxRows(int maxRows) {
		super.setMaxRows(maxRows);
	}

	@Override
	public void setNativeJdbcExtractor(NativeJdbcExtractor extractor) {
		super.setNativeJdbcExtractor(extractor);
	}

	@Override
	public void setQueryTimeout(int queryTimeout) {
		super.setQueryTimeout(queryTimeout);
	}

	@Override
	public void setResultsMapCaseInsensitive(boolean resultsMapCaseInsensitive) {
		super.setResultsMapCaseInsensitive(resultsMapCaseInsensitive);
	}

	@Override
	public void setSkipResultsProcessing(boolean skipResultsProcessing) {
		super.setSkipResultsProcessing(skipResultsProcessing);
	}

	@Override
	public void setSkipUndeclaredResults(boolean skipUndeclaredResults) {
		super.setSkipUndeclaredResults(skipUndeclaredResults);
	}

	@Override
	public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws DataAccessException {
		return super.update(psc, generatedKeyHolder);
	}

	@Override
	protected int update(PreparedStatementCreator psc, PreparedStatementSetter pss) throws DataAccessException {
		return super.update(psc, pss);
	}

	@Override
	public int update(PreparedStatementCreator psc) throws DataAccessException {
		return super.update(psc);
	}

	@Override
	public int update(String sql, Object... args) throws DataAccessException {
		return super.update(sql, args);
	}

	@Override
	public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return super.update(sql, args, argTypes);
	}

	@Override
	public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
		return super.update(sql, pss);
	}

	@Override
	public int update(String sql) throws DataAccessException {
		return super.update(sql);
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
	}

	@Override
	public DataSource getDataSource() {
		return super.getDataSource();
	}

	@Override
	public synchronized SQLExceptionTranslator getExceptionTranslator() {
		return super.getExceptionTranslator();
	}

	@Override
	public boolean isLazyInit() {
		return super.isLazyInit();
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	public void setDatabaseProductName(String dbName) {
		super.setDatabaseProductName(dbName);
	}

	@Override
	public void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator) {
		super.setExceptionTranslator(exceptionTranslator);
	}

	@Override
	public void setLazyInit(boolean lazyInit) {
		super.setLazyInit(lazyInit);
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

	public int execute(String sql, AbstractLobCreatingPreparedStatementCallback action) {
		return super.execute(sql, action);
	}
	
}
