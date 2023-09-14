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
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.ModuloExportInfo;
import it.csi.moon.commons.dto.ModuloExported;
import it.csi.moon.commons.dto.ModuloImportParams;
import it.csi.moon.commons.entity.CategoriaEntity;
import it.csi.moon.commons.entity.CategoriaModuloEntity;
import it.csi.moon.commons.entity.ModuloCronologiaStatiEntity;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.entity.ModuloProgressivoEntity;
import it.csi.moon.commons.entity.ModuloStrutturaEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.ModuloVersioneEntity;
import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonsrv.business.service.ModuliImportService;
import it.csi.moon.moonsrv.business.service.impl.dao.CategoriaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloStrutturaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.PortaleDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi all'import di moduli
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class ModuliImportServiceImpl implements ModuliImportService {
	
	private static final String CLASS_NAME = "ModuliImportServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final String MODULO_EXPORT_VERSION = "1.0.0";
	private String jndiName;
	private static final String ATTORE = "ADMIN";
	
	private ModuloExported moduloExported;
	private ModuloImportParams params;
	private Date now;
	
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
	ModuloStrutturaDAO moduloStrutturaDAO;
	
	public ModuloExported getModuloExported() {
		return moduloExported;
	}
	public void setModuloExported(ModuloExported modulo) {
		this.moduloExported = modulo;
	}
	public ModuloImportParams getParams() {
		return params;
	}
	public void setParams(ModuloImportParams params) {
		this.params = params;
	}

	@Override
	public String importModulo(ModuloExported modulo, ModuloImportParams params) throws BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::importModulo] BEGIN");
			LOG.debug("[" + CLASS_NAME + "::importModulo] IN moduloExported="+modulo);
			LOG.debug("[" + CLASS_NAME + "::importModulo] IN params="+params);
		}
		try {
			String result = "OK";
			setModuloExported(modulo);
			setParams(params);
			switch (params.getModalita()) {
				case IMPORT:
					result = importAll();
					break;
				case IMPORT_ONLY_STRUTTURE:
					result = importStrutture();
					break;
				case DIFF:
					result = diff();
					break;
				default:
			}
			
			return result;

		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::importModulo] END");
			}
		}
	}

	private String importAll() {
		LOG.debug("[" + CLASS_NAME + "::importAll] BEGIN");
		try {
			ModuloEntity entity = moduloDAO.findByCodice(getCodiceModuloTarget());
			if (entity!=null) {
				return "CODICE_MODULO_ALREADY_EXISTS";
			}
			Long idModulo = inserisciModulo(); // Modulo, Cronologia, Progressivo, Struttura
			insertCategoria(idModulo);
			
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.debug("[" + CLASS_NAME + "::importAll] OK : CodiceModule Not Already exists.");
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::importAll] DAOException ", e);
			throw new BusinessException("Errore ricerca modulo per codice");
		}
		
		// IMPORT
//		ModuloStrutturaEntity struttura = strutturaDAO.findByIdModulo(entity.getIdModulo());
//		ModuloCronologiaStatiEntity lastCron = moduloDAO.findLastCronologia(entity.getIdModulo());
//		ModuloProgressivoEntity progressivo = moduloProgressivoDAO.findByIdModulo(entity.getIdModulo(), entity.getIdTipoCodiceIstanza());
//		//
//		List<CategoriaEntity> categorie = categoriaDAO.findByIdModulo(entity.getIdModulo());
//		CategoriaEntity categoria = (categorie!=null&&!categorie.isEmpty())?categorie.get(0):null;
//		//
//		List<PortaleEntity> portali = portaleDAO.findByIdModulo(entity.getIdModulo());
//		
//		// result
//		ModuloExported moduloExported = new ModuloExported();
//		moduloExported.setExportInformation(getExportInfo());
//		moduloExported.setModulo(entity);
//		moduloExported.setStato(StatoModuloMapper.buildFromCodiceStato(lastCron.getCodiceStato()));
//		moduloExported.setStruttura(struttura);
//		moduloExported.setProgressivo(progressivo);
//		moduloExported.setCategoria(categoria);
//		moduloExported.setPortali(portali);
		
		return "OK";
	}
	
	private Long inserisciModulo() {
		now = new Date();
		Long idModulo = moduloDAO.insert(buildModuloEntity());
		Long idVersioneModulo = moduloDAO.insertVersione(buildModuloVersioneEntity(idModulo));
		Long idCronologia = moduloDAO.insertCronologia(buildCronologiaModuloLAV(idVersioneModulo));
		moduloProgressivoDAO.insert(buildModuloProgressivoEntity(idModulo));
		Long idStruttura = moduloStrutturaDAO.insert(buildModuloStrutturaEntity(idModulo, idVersioneModulo));
		return idModulo;
	}

	private ModuloEntity buildModuloEntity() {
		ModuloEntity moduloEntity = new ModuloEntity();
		moduloEntity.setCodiceModulo( getCodiceModuloTarget() );
		moduloEntity.setAttoreUpd(ATTORE);
		moduloEntity.setDataIns(now);
		moduloEntity.setDataUpd(now); // NOT NULL !
		moduloEntity.setDescrizioneModulo(getModuloExported().getModulo().getDescrizioneModulo() );
		moduloEntity.setFlagIsRiservato(getModuloExported().getModulo().getFlagIsRiservato());
		moduloEntity.setFlagProtocolloIntegrato(getModuloExported().getModulo().getFlagProtocolloIntegrato());
		moduloEntity.setIdTipoCodiceIstanza(getModuloExported().getModulo().getIdTipoCodiceIstanza()); // VERIFY ID dovrebbe essere equali
		moduloEntity.setOggettoModulo(getModuloExported().getModulo().getOggettoModulo());
//		moduloEntity.setVersioneModulo(getModuloExported().getModulo().getVersioneModulo());
		return moduloEntity;
	}
	private ModuloVersioneEntity buildModuloVersioneEntity(Long idModulo) {
		ModuloVersioneEntity moduloVersioneEntity = new ModuloVersioneEntity();
		moduloVersioneEntity.setIdModulo(idModulo);
		moduloVersioneEntity.setVersioneModulo(getModuloExported().getModulo().getVersioneModulo());
		moduloVersioneEntity.setAttoreUpd(ATTORE);
		moduloVersioneEntity.setDataUpd(now); // NOT NULL !
		return moduloVersioneEntity;
	}
	private ModuloCronologiaStatiEntity buildCronologiaModuloLAV(Long idVersioneModulo) {
		ModuloCronologiaStatiEntity cron = new ModuloCronologiaStatiEntity();
		cron.setIdVersioneModulo(idVersioneModulo);
		cron.setIdStato(DecodificaStatoModulo.IN_COSTRUZIONE.getId());
		cron.setDataInizioValidita(now);
		cron.setDataFineValidita(null);
		return cron;
	}
	private ModuloProgressivoEntity buildModuloProgressivoEntity(Long idModulo) {
		ModuloProgressivoEntity progressivo = new ModuloProgressivoEntity();
		progressivo.setIdModulo(idModulo);
		progressivo.setProgressivo(0L); // Inizializzato a 0
		progressivo.setAnnoRiferimento(Calendar.getInstance().get(Calendar.YEAR)); // Initializzato ad anno corrente
		progressivo.setLunghezza(getModuloExported().getProgressivo().getLunghezza());
		return progressivo;
	}
	private ModuloStrutturaEntity buildModuloStrutturaEntity(Long idModulo, Long idVersioneModulo) {
		ModuloStrutturaEntity struttura = new ModuloStrutturaEntity();
		struttura.setIdVersioneModulo(idVersioneModulo);
		struttura.setIdModulo(idModulo);
		struttura.setTipoStruttura(getModuloExported().getStruttura().getTipoStruttura()); // FRM / WIZ
		struttura.setStruttura(getModuloExported().getStruttura().getStruttura());
		return struttura;
	}
	//
	private void insertCategoria(Long idModulo) {
		try {
			CategoriaEntity categoria = categoriaDAO.findByNome(getModuloExported().getCategoria().getNomeCategoria());
			categoriaDAO.insertCategoriaModulo(new CategoriaModuloEntity(categoria.getIdCategoria(), idModulo, new Date(), ATTORE));
		} catch (ItemNotFoundDAOException nfe) {
			LOG.warn("[" + CLASS_NAME + "::insertCategoria] ItemNotFoundDAOException importModulo con categoria non presente "+getModuloExported().getCategoria().getNomeCategoria());
		} catch (DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::insertCategoria] DAOException "+getModuloExported().getCategoria().getNomeCategoria());
		}
	}

	
	private String importStrutture() {
		LOG.debug("[" + CLASS_NAME + "::importStrutture] BEGIN");
		try {
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoPubblicatoByCodice(getCodiceModuloTarget());
			if (moduloE==null) {
				return "CODICE_MODULO_NOT_EXISTS";
			}
			
			// STRUTTURA
			ModuloStrutturaEntity struttura = strutturaDAO.findByIdVersioneModulo(moduloE.getIdVersioneModulo());
			struttura.setTipoStruttura(getModuloExported().getStruttura().getTipoStruttura());
			struttura.setStruttura(getModuloExported().getStruttura().getStruttura());
			strutturaDAO.update(struttura);
			
			// MODULO
			moduloE.setDataUpd(new Date());
			moduloE.setAttoreUpd(getAttore());
			moduloDAO.update(moduloE);
			
			// Warnings
			String warning = "";
			if (struttura.getStruttura().contains("\"url\":\"https://")) {
				warning += "\nWarning: Strutture contains Absolute URL https.";
			}
			if (struttura.getStruttura().contains("\"url\":\"http://")) {
				warning += "\nWarning: Strutture contains Absolute URL http.";
			}
// TODO MEV_VERSION
//			if (!getModuloExported().getModulo().getVersioneModulo().equals(moduloE.getVersioneModulo())) {
//				warning += "\nWarning: version source module "+getModuloExported().getModulo().getVersioneModulo()+" different with destination module "+moduloE.getVersioneModulo();
//			}
			
			return "Strutture of module "+getCodiceModuloTarget()+" updated."+warning;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::importStrutture] ItemNotFoundDAOException (moduloDAO.findByCodice OR strutturaDAO.findByIdModulo) " + notFoundEx.getMessage());
			return "CODICE_MODULO_NOT_EXISTS";
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::importStrutture] DAOException " + e.getMessage());
			throw new BusinessException("Errore ricerca modulo per codice");
		}

	}
	private String getAttore() {
		return "ADMIN-IMPORT";
	}
	private String diff() {
		LOG.debug("[" + CLASS_NAME + "::diff] BEGIN");
		return "OK";
	}
	
	private String getCodiceModuloTarget() {
		return params.getCodiceModuloTarget()!=null?params.getCodiceModuloTarget():getModuloExported().getModulo().getCodiceModulo();
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
