/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.EnteAreaModulo;
import it.csi.moon.commons.dto.ModuloExportInfo;
import it.csi.moon.commons.dto.ModuloExported;
import it.csi.moon.commons.entity.AreaEntity;
import it.csi.moon.commons.entity.AreaModuloFilter;
import it.csi.moon.commons.entity.CategoriaEntity;
import it.csi.moon.commons.entity.EnteEntity;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.ModuloCronologiaStatiEntity;
import it.csi.moon.commons.entity.ModuloProgressivoEntity;
import it.csi.moon.commons.entity.ModuloStrutturaEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.PortaleEntity;
import it.csi.moon.commons.entity.ProtocolloParametroEntity;
import it.csi.moon.commons.mapper.EnteAreaModuloMapper;
import it.csi.moon.commons.mapper.StatoModuloMapper;
import it.csi.moon.moonsrv.business.service.ModuliExportService;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.CategoriaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloStrutturaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.PortaleDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ProtocolloParametroDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi all'export di moduli
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class ModuliExportServiceImpl implements ModuliExportService {
	
	private static final String CLASS_NAME = "ModuliExportServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final String MODULO_EXPORT_VERSION = "1.0.0";
	private String jndiName;
	
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	ModuloStrutturaDAO strutturaDAO;
	@Autowired
	ModuloProgressivoDAO moduloProgressivoDAO;
	@Autowired
	CategoriaDAO categoriaDAO;
	@Autowired
	PortaleDAO portaleDAO;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	@Autowired
	AreaModuloDAO areaModuloDAO;
	@Autowired
	EnteDAO enteDAO;
	@Autowired
	AreaDAO areaDAO;
	@Autowired
	ProtocolloParametroDAO protocolloParametroDAO;
	
	@Override
	public ModuloExported exportModuloByCd(String codiceModulo, String versione) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::exportModuloByCd] BEGIN");
			LOG.debug("[" + CLASS_NAME + "::exportModuloByCd] IN codiceModulo="+codiceModulo+"  versione="+versione);
		}
		try {
			ModuliFilter filter = new ModuliFilter();
			filter.setCodiceModulo(codiceModulo);
			filter.setVersioneModulo(versione);
			List<ModuloVersionatoEntity> moduli = moduloDAO.find(filter);
			if (moduli==null || moduli.size()==0) {
				throw new ItemNotFoundBusinessException();
			}
			if (moduli.size()>1) {
				throw new BusinessException("TOO_MANY_MODULES");
			}
			ModuloVersionatoEntity entity = moduli.get(0);
			ModuloStrutturaEntity struttura = strutturaDAO.findByIdVersioneModulo(entity.getIdVersioneModulo());
			ModuloCronologiaStatiEntity lastCron = moduloDAO.findCurrentCronologia(entity.getIdVersioneModulo());
			ModuloProgressivoEntity progressivo = moduloProgressivoDAO.findByIdModulo(entity.getIdModulo(), entity.getIdTipoCodiceIstanza());
			//
			List<CategoriaEntity> categorie = categoriaDAO.findByIdModulo(entity.getIdModulo());
			CategoriaEntity categoria = (categorie!=null&&!categorie.isEmpty())?categorie.get(0):null;
			//
			List<PortaleEntity> portali = portaleDAO.findByIdModulo(entity.getIdModulo());
			// Attributi
			List<ModuloAttributoEntity> attributi = moduloAttributiDAO.findByIdModulo(entity.getIdModulo());
			// EntiAree
			AreaModuloFilter areaFilter = new AreaModuloFilter();
			areaFilter.setIdModulo(entity.getIdModulo());
			List<EnteAreaModulo> entiAree = areaModuloDAO.find(areaFilter).stream()
					.map( am -> EnteAreaModuloMapper.buildFromEntities(retreivEnte(am.getIdEnte()), retreivArea(am.getIdArea()), am) )
					.collect(Collectors.toList());
			// Protocollo
			List<ProtocolloParametroEntity> protocolloParametri = protocolloParametroDAO.findAllByIdModulo(entity.getIdModulo());
			
			// ExportInfo & control Warning
			ModuloExportInfo exportInfo = getExportInfo();
			if (struttura.getStruttura().contains("\"url\":\"https://")) {
				exportInfo.addWarning("Strutture contains Absolute URL https.");
			}
			if (struttura.getStruttura().contains("\"url\":\"http://")) {
				exportInfo.addWarning("Strutture contains Absolute URL http.");
			}
			
			// result
			ModuloExported moduloExported = new ModuloExported();
			moduloExported.setExportInformation(exportInfo);
			moduloExported.setModulo(entity);
			moduloExported.setStato(StatoModuloMapper.buildFromIdStato(lastCron.getIdStato()));
			moduloExported.setStruttura(struttura);
			moduloExported.setProgressivo(progressivo);
			moduloExported.setCategoria(categoria);
			moduloExported.setPortali(portali);
			moduloExported.setEntiAree(entiAree);
			moduloExported.setProtocolloParametri(protocolloParametri);
			
			return moduloExported;

		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::exportModuloByCd] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::exportModuloByCd] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca modulo per codice");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::exportModuloByCd] END");
			}
		}
	}

	private EnteEntity retreivEnte(Long idEnte) {
		return enteDAO.findById(idEnte);
	}

	private AreaEntity retreivArea(Long idArea) {
		return areaDAO.findById(idArea);
	}


	private ModuloExportInfo getExportInfo() {
		ModuloExportInfo result = new ModuloExportInfo();
		result.setVersion(MODULO_EXPORT_VERSION);
		result.setFrom(getHostname());
		result.setDatabaseJndiName(getJndiName());
		result.setDatabaseURL(getDatabaseURL());
		result.setAt(new Date());
		return result;
	}
	
	private String getDatabaseURL() {
		String result = null;
		try {
			final JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			DataSource ds = dataSourceLookup.getDataSource(getJndiName());
			try (Connection con = ds.getConnection()) {
				result = con.getMetaData().getURL();
			}
			return result;
		} catch (DataSourceLookupFailureException e) {
			LOG.warn("[" + CLASS_NAME + "::getDatabaseURL] DataSourceLookupFailureException ", e);
		} catch (SQLException e) {
			LOG.warn("[" + CLASS_NAME + "::getDatabaseURL] SQLException ", e);
		}
		return result;
	}

	private String getHostname() {
		String result = null;
		try {
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    result = addr.getHostName();
		    LOG.debug("[" + CLASS_NAME + "::getHostname] addr.getHostName()="+addr.getHostName()+"   addr.getHostAddress()="+addr.getHostAddress());
		} catch (UnknownHostException ex) {
		    LOG.error("[" + CLASS_NAME + "::getHostname] Hostname can not be resolved");
		}
		return result;
	}

	public String getJndiName() {
		return jndiName;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

}
