/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimanWSTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.GinevraDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.territorio.ginevra.lanci.civico.cxfclient.Civico;
import it.csi.territorio.ginevra.lanci.civico.cxfclient.LanciCivico;
import it.csi.territorio.ginevra.lanci.civico.cxfclient.LanciCivicoService;
import it.csi.territorio.ginevra.lanci.sedime.cxfclient.LanciSedime;
import it.csi.territorio.ginevra.lanci.sedime.cxfclient.LanciSedimeService;
import it.csi.territorio.ginevra.lanci.sedime.cxfclient.Sedime;
import it.csi.territorio.ginevra.lanci.via.cxfclient.LanciVia;
import it.csi.territorio.ginevra.lanci.via.cxfclient.LanciViaService;
import it.csi.territorio.ginevra.lanci.via.cxfclient.ViaComunale;
import it.csi.territorio.ginevra.lanci.via.cxfclient.ViaSovracomunale;

@Component
public class GinevraDAOImpl extends ApimanWSTemplateImpl implements GinevraDAO {
	
	private static final String CLASS_NAME = "GinevraDAOImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final Integer MODO_RICERCA_ESATTA = 0;   // (esegue una ricerca esatta. Es. NOME)
	private static final Integer MODO_RICERCA_CONTIENE = 1; // (%NOME%)
	private static final Integer MODO_RICERCA_INIZIA = 2;   // (NOME%)
	private static final Integer MODO_RICERCA_FINISCE = 3;  // (%NOME)

	private static final String APIMAN_EXTRA_PATH_TERRITORIO_GINEVRA_LANCI_SEDIME_2_0 = "/territorio_ginevra_lanci_sedime/2.0";

	//
	// SEDIMI
	@Override
	public List<Sedime> getSedimi() throws DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_GINEVRA_LANCI_SEDIME_2_0);
	    	LanciSedimeService ss = new LanciSedimeService();
	    	LanciSedime wrapped = (LanciSedime) getPortService(ss.getLanciSedime(), LanciSedime.class);
	    	return wrapped.cercaTuttiISedimi();
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getSedimi] Errore generico servizio getSedimi", e);
			throw new DAOException("Errore generico servizio getSedimi");
	     }
	}
	@Override
	public Sedime getSedimeById(Long idTipoVia) throws DAOException {
		try {
	    	setPathExtra(APIMAN_EXTRA_PATH_TERRITORIO_GINEVRA_LANCI_SEDIME_2_0);
	    	LanciSedimeService ss = new LanciSedimeService();
	    	LanciSedime wrapped = (LanciSedime) getPortService(ss.getLanciSedime(), LanciSedime.class);
	    	return wrapped.cercaSedimePerIdTipoVia(idTipoVia);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getSedimeById] Errore generico servizio getSedimeById", e);
			throw new DAOException("Errore generico servizio getSedimeById");
	     }
	}
	
	//
	// VIE
	@Override
	public List<ViaComunale> getViePerNomeContiene(String nome, Long idComune) throws DAOException {
		try {
	    	setPathExtra("/territorio_ginevra_lanci_via/2.0");
	    	LanciViaService ss = new LanciViaService();
	    	LanciVia wrapped = (LanciVia) getPortService(ss.getLanciVia(), LanciVia.class);
			return wrapped.cercaVieComunaliPerNomeEIdComune(nome, MODO_RICERCA_CONTIENE, idComune);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getViePerNomeContiene] Errore generico servizio getViePerNomeContiene", e);
			throw new DAOException("Errore generico servizio getViePerNomeContiene");
	     }
	}
	
	@Override
	public ViaComunale getViaByIdIndirizzoComunale(Long idVia) throws DAOException {
		try {
	    	setPathExtra("/territorio_ginevra_lanci_via/2.0");
	    	LanciViaService ss = new LanciViaService();
	    	LanciVia wrapped = (LanciVia) getPortService(ss.getLanciVia(), LanciVia.class);
			return wrapped.cercaViaComunalePerIdIndirizzoComunale(idVia);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getViaByIdIndirizzoComunale] Errore generico servizio getVie", e);
			throw new DAOException("Errore generico servizio getViaByIdIndirizzoComunale");
	     }
	}
	
	@Override
	public ViaSovracomunale getViaByIdIndirizzoSovracomunale(Long idVia) throws DAOException {
		try {
	    	setPathExtra("/territorio_ginevra_lanci_via/2.0");
	    	LanciViaService ss = new LanciViaService();
	    	LanciVia wrapped = (LanciVia) getPortService(ss.getLanciVia(), LanciVia.class);
			return wrapped.cercaViaSovracomunalePerIdIndirizzoSovracomunale(idVia);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getViaByIdIndirizzoSovracomunale] Errore generico getViaByIdIndirizzoSovracomunale", e);
			throw new DAOException("Errore generico servizio getViaByIdIndirizzoSovracomunale");
	     }
	}
	//
	// CIVICI
	@Override
	public List<Civico> getCivici(Long idL2, Long numero) throws DAOException {
		try {
	    	setPathExtra("/territorio_ginevra_lanci_civico/2.0");
	    	LanciCivicoService ss = new LanciCivicoService();
	    	LanciCivico wrapped = (LanciCivico) getPortService(ss.getLanciCivico(), LanciCivico.class);
	    	boolean escludiInterpolati = true;
	    	return wrapped.cercaCiviciPerIdIndirizzoComunaleENumero(idL2, numero, escludiInterpolati);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCivici] Errore generico servizio getCivici", e);
			throw new DAOException("Errore generico servizio elenco getCivici");
	     }
	}
	@Override
	public Civico getCivicoById(Long idCivico) throws DAOException {
		try {
	    	setPathExtra("/territorio_ginevra_lanci_civico/2.0");
	    	LanciCivicoService ss = new LanciCivicoService();
	    	LanciCivico wrapped = (LanciCivico) getPortService(ss.getLanciCivico(), LanciCivico.class);
	    	return wrapped.cercaCivicoPerIdCivico(idCivico);
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] Errore generico servizio getCivicoById", e);
			throw new DAOException("Errore generico servizio getCivicoById");
	     }
	}
	
}
