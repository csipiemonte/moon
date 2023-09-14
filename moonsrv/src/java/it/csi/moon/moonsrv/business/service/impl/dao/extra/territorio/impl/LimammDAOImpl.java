/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimanWSTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.LimammDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.territorio.svista.limamm.ente.cxfclient.Comune;
import it.csi.territorio.svista.limamm.ente.cxfclient.LimammEnte;
import it.csi.territorio.svista.limamm.ente.cxfclient.LimammEnteService;
import it.csi.territorio.svista.limamm.ente.cxfclient.Provincia;
import it.csi.territorio.svista.limamm.ente.cxfclient.Regione;

@Component
public class LimammDAOImpl extends ApimanWSTemplateImpl implements LimammDAO {
	
	private static final String CLASS_NAME = "LimammDAOImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0 = "/territorio_svista_limamm_ente/2.0";

	//
	// REGIONI
	@Override
	public List<Regione> getRegioni() throws DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaTutteLeRegioni();
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRegioni] Errore generico servizio getRegioni", e);
			throw new DAOException("Errore generico servizio elenco Regioni");
	     }
	}
	@Override
	public Regione getRegioneById(Long idRegione) throws ItemNotFoundDAOException, DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaRegionePerIdRegione(idRegione);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRegioneById] Errore generico servizio getRegioneById", e);
			throw new DAOException("Errore generico servizio getRegioneById");
	     }
	}
	@Override
	public Regione getRegioneByIstat(String istat) throws ItemNotFoundDAOException, DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaRegionePerCodIstat(istat);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRegioneByIstat] Errore generico servizio getRegioneByIstat", e);
			throw new DAOException("Errore generico servizio getRegioneByIstat");
	     }
	}
//	public List<Regione> getRegioniByNome(String istat) throws DAOException {
//		try {
//	    	setPathExtra("/territorio_svista_limamm_ente/2.0");
//			LimammEnteService ss = new LimammEnteService();
//			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
//			return wrapped.cercaRegioniPerNome(arg0, arg1);
//	     } catch(Exception e) {
//			LOG.error("[" + CLASS_NAME + "::getRegioniByNome] Errore generico servizio getRegioniByNome", e);
//			throw new DAOException("Errore generico servizio getRegioniByNome");
//	     }
//	}
	
	//
	// PROVINCE
	@Override
	public List<Provincia> getProvince() throws DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaTutteLeProvince();
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvince] Errore generico servizio getProvince", e);
			throw new DAOException("Errore generico servizio elenco Province");
	     }
	}
	@Override
	public Provincia getProvinciaById(Long idProvincia) throws ItemNotFoundDAOException, DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaProvinciaPerIdProvincia(idProvincia);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaById] Errore generico servizio getProvinciaById", e);
			throw new DAOException("Errore generico servizio getProvinciaById");
	     }
	}
	@Override
	public Provincia getProvinciaByIstat(String istat) throws ItemNotFoundDAOException, DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaProvinciaPerCodiceIstat(istat);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] Errore generico servizio getProvinciaByIstat", e);
			throw new DAOException("Errore generico servizio getProvinciaByIstat");
	     }
	}
	@Override
	public List<Provincia> getProvincePerIdRegione(Long idRegione) throws DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaProvincePerIdRegione(idRegione);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvincePerIdRegione] Errore generico servizio getProvincePerIdRegione", e);
			throw new DAOException("Errore generico servizio getProvincePerIdRegione");
	     }
	}
	
	//
	// COMUNI
	@Override
	public List<Comune> getComuni() throws DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaTuttiIComuni();
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuni] Errore generico servizio getComuni", e);
			throw new DAOException("Errore generico servizio elenco Comuni");
	     }
	}
	@Override
	public Comune getComuneById(Long idComune) throws ItemNotFoundDAOException, DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaComunePerIdComune(idComune);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuneById] Errore generico servizio getComuneById", e);
			throw new DAOException("Errore generico servizio getComuneById");
	     }
	}
	@Override
	public Comune getComuneByIstat(String istat) throws ItemNotFoundDAOException, DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaComunePerCodiceIstat(istat);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstat] Errore generico servizio getComuneByIstat", e);
			throw new DAOException("Errore generico servizio getComuneByIstat");
	     }
	}
	@Override
	public List<Comune> getComuniPerIdProvincia(Long idProvincia) throws DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_SVISTA_LIMAMM_ENTE_2_0);
			LimammEnteService ss = new LimammEnteService();
			LimammEnte wrapped = (LimammEnte) getPortService(ss.getLimammEnte(), LimammEnte.class);
			return wrapped.cercaComuniPerIdProvincia(idProvincia);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuni] Errore generico servizio getComuni", e);
			throw new DAOException("Errore generico servizio elenco Comuni");
	     }
	}
}
