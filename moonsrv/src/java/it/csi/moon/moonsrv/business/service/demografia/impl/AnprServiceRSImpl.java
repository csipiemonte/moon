/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.demografia.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.apimint.demografia.v1.dto.LegameSoggettoCompleto;
import it.csi.apimint.demografia.v1.dto.Soggetto;
import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.commons.dto.extra.demografia.DocumentoRiconoscimento;
import it.csi.moon.commons.dto.extra.demografia.LuogoMOON;
import it.csi.moon.commons.dto.extra.demografia.RelazioneParentela;
import it.csi.moon.moonsrv.business.service.AuditService;
import it.csi.moon.moonsrv.business.service.demografia.AnprNazioneService;
import it.csi.moon.moonsrv.business.service.demografia.AnprService;
import it.csi.moon.moonsrv.business.service.demografia.mapper.AnprCittadinanzeMapper;
import it.csi.moon.moonsrv.business.service.demografia.mapper.AnprLuogoMoonMapper;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.AnprApimintDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Layer di logica servizi che richiama i DAO ANPR via API Manager Outer REST
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
@Qualifier("RS")
public class AnprServiceRSImpl implements AnprService {
	
	private static final String CLASS_NAME = "AnprServiceRSImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	AnprApimintDAO anprDAO;
	@Autowired
	AnprNazioneService anprNazioneService;
	@Autowired
	AuditService auditService;
	
	@Override
	public List<ComponenteFamiglia> getFamigliaANPR(String cfRicercato, String userJwt, String ipAdress, String utente) throws BusinessException {
		// CASO standard da FOBL cittadino con Auth Shib, ci presentiamo come MOON
		try {
			return _getFamigliaANPR(cfRicercato, userJwt, EnvProperties.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO);
	    } finally {
	    	try {
				auditService.insertAuditANPRGetFamigliaByCF(utente, ipAdress, cfRicercato);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getFamigliaANPR] Errore servizio Audit ", e );
			}
	    }
	}

	@Override
	public List<ComponenteFamiglia> getFamigliaANPR(String cfRicercato, String userJwt, String clientProfileKey, String ipAdress, String utente) throws BusinessException {
		// CASO da FOBL o BOBL
		if (StringUtils.isEmpty(cfRicercato)) {
			throw new BusinessException("cfRicercato required is empty.");
		}
		try {
			userJwt = StringUtils.isBlank(userJwt)?utente:userJwt; // traccio il CF operatore fino al sistema di demografia (solo CF, in sost al jwtSPID operatore mancante)
			return _getFamigliaANPR(cfRicercato, userJwt, clientProfileKey);
	    } finally {
	    	try {
				auditService.insertAuditANPRGetFamigliaByCF(utente, ipAdress, cfRicercato);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getFamigliaANPR] Errore servizio Audit ", e );
			}
	    }
	}
	
	private List<ComponenteFamiglia> _getFamigliaANPR(String cfRicercato, String userJwt, String clientProfileKey) throws BusinessException {
		List<ComponenteFamiglia> result = new ArrayList<>();
		try {
			List<Soggetto> soggetti = anprDAO.getFamigliaANPR(cfRicercato, userJwt, clientProfileKey);
			for (Soggetto s : soggetti) {
	    		ComponenteFamiglia c = buildComponenteFamiglia(s);
	    		result.add(c);
	    	}
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getFamigliaANPR] DAOException " + daoe.getMessage());
	    	throw new BusinessException("FamigliaANPR non trovata");
	    } catch(Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getFamigliaANPR] Exception " + e.getMessage());
	    	throw new BusinessException("FamigliaANPR non trovata generico");
	    }
		return result;
	}

	protected ComponenteFamiglia buildComponenteFamiglia(Soggetto s) {
		AnprCittadinanzeMapper cittadinanzeMapper = new AnprCittadinanzeMapper(anprNazioneService);
		AnprLuogoMoonMapper luogoMoonMapper = new AnprLuogoMoonMapper(anprNazioneService);
		//
		ComponenteFamiglia c = new ComponenteFamiglia();
		c.setCodiceFiscale(s.getGeneralita().getCodiceFiscale().getCodFiscale());
		c.setCognome(s.getGeneralita().getCognome());
		c.setNome(s.getGeneralita().getNome());
		c.setDataNascita(translateData(s.getGeneralita().getDataNascita()));
		LuogoMOON luogoNascita = luogoMoonMapper.remapFromLuogoEventoANPR(s.getGeneralita().getLuogoNascita());
		if (luogoNascita!=null) {
			c.setNazioneNascita(luogoNascita.getNazione());
//	    			c.setRegioneNascita(luogoNascita.getRegione());
//	    		 	c.setProvinciaNascita(luogoNascita.getProvincia());
//	    		 	c.setComuneNascita(luogoNascita.getComune());
//	    		 	c.setComuneEsteroNascita(luogoNascita.getComuneEstero());
			c.setLuogoNascita(luogoNascita.getDescrizioneLuogo());
		}
		
		c.setCittadinanza(cittadinanzeMapper.remapFromCittadinanzaANPR(s.getCittadinanza()));
		c.setSesso(s.getGeneralita().getSesso());
		c.setRelazioneParentela(translateRelazioneParentela(s.getLegameSoggetto()));
		c.setFlagPossessoPatente(null);
		c.setFlagPossessoVeicolo(null);
		completaDocRicCartaIdentita(s, c);
		completaDocRicPermessoSoggiorno(s, c);
		return c;
	}

	protected void completaDocRicCartaIdentita(Soggetto s, ComponenteFamiglia c) {
		if (s.getCartaIdentita()!=null) {
			DocumentoRiconoscimento ci = new DocumentoRiconoscimento();
			ci.setNumero(s.getCartaIdentita().getNumero());
			ci.setDataRilascio(s.getCartaIdentita().getDataRilascio());
			ci.setDataScadenza(s.getCartaIdentita().getDataScadenza());
			StringBuilder strB = new StringBuilder();
			if (s.getCartaIdentita().getComuneRilascio()!=null) {
				strB.append(s.getCartaIdentita().getComuneRilascio().getNomeComune());
				if (StringUtils.isNotEmpty(s.getCartaIdentita().getComuneRilascio().getSiglaProvinciaIstat())) {
					strB.append(" (").append(s.getCartaIdentita().getComuneRilascio().getSiglaProvinciaIstat()).append(")");
				}
				ci.setEnteRilascio(strB.toString());
			} else if (s.getCartaIdentita().getConsolatoRilascio()!=null) {
				ci.setEnteRilascio(s.getCartaIdentita().getConsolatoRilascio().getDescrizioneConsolato());
			}
			c.setCartaIdentita(ci);
		}
	}

	protected void completaDocRicPermessoSoggiorno(Soggetto s, ComponenteFamiglia c) {
		if (s.getPermessoSoggiorno()!=null) {
			DocumentoRiconoscimento permSogg = new DocumentoRiconoscimento();
			permSogg.setNumero(s.getPermessoSoggiorno().getNumeroSoggiorno());
			permSogg.setDataRilascio(s.getPermessoSoggiorno().getDataRilascio());
			permSogg.setDataScadenza(s.getPermessoSoggiorno().getDataScadenza());
			StringBuilder strB = new StringBuilder();
			if (s.getPermessoSoggiorno().getComune()!=null) {
				strB.append(s.getPermessoSoggiorno().getComune().getNomeComune());
				if (StringUtils.isNotEmpty(s.getPermessoSoggiorno().getComune().getSiglaProvinciaIstat())) {
					strB.append(" (").append(s.getPermessoSoggiorno().getComune().getSiglaProvinciaIstat()).append(")");
				}
				permSogg.setEnteRilascio(strB.toString());
			} else if (s.getPermessoSoggiorno().getQuesturaRilascioSoggiorno()!=null) {
				permSogg.setEnteRilascio(s.getPermessoSoggiorno().getQuesturaRilascioSoggiorno());
			}
			c.setDocumentoSoggiorno(permSogg);
		}
	}
	
	private RelazioneParentela translateRelazioneParentela(LegameSoggettoCompleto legameSoggettoCompleto) {
		return null;
	}
	private String translateData(Date date) {
		return sdf.format(date);
	}

	//
	// getSoggettiFamigliaANPR
	//
	private List<Soggetto> _getSoggettiFamigliaANPR(String codiceFiscale, String userJwt, String clientProfileKey) throws BusinessException {
		List<Soggetto> result = new ArrayList<>();
		try {
			result = anprDAO.getFamigliaANPR(codiceFiscale, userJwt, clientProfileKey);
	    } catch(DAOException daoe) {
	    	LOG.error("[" + CLASS_NAME + "::getSoggettiFamigliaANPR] DAOException " + daoe.getMessage());
	    	throw new BusinessException("SoggettiFamigliaANPR non trovati");
	    } catch(Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getSoggettiFamigliaANPR] Exception "+e.getMessage());
	    	throw new BusinessException("SoggettiFamigliaANPR non trovati generico");
	    }
		return result;
	}

	@Override
	public List<Soggetto> getSoggettiFamigliaANPR(String cfRicercato, String userJwt, String ipAdress, String utente) throws BusinessException {
		// CASO standard da FOBL cittadino con Auth Shib, ci presentiamo come MOON
		try {
			return _getSoggettiFamigliaANPR(cfRicercato, userJwt, EnvProperties.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO);
	    } finally {
	    	try {
				auditService.insertAuditANPRGetFamigliaByCF(utente, ipAdress, cfRicercato);
			} catch (Exception e) {
				LOG.warn("[" + CLASS_NAME + "::getSoggettiFamigliaANPR] Errore servizio Audit ", e );
			}
	    }
	}
	@Override
	public List<Soggetto> getSoggettiFamigliaANPR(String cfRicercato, String userJwt, String clientProfileKey, String ipAdress, String utente) throws BusinessException {
		try {
			return _getSoggettiFamigliaANPR(cfRicercato, userJwt, clientProfileKey);
	    } finally {
	    	try {
				auditService.insertAuditANPRGetFamigliaByCF(utente, ipAdress, cfRicercato);
			} catch (Exception e) {
				LOG.warn("[" + CLASS_NAME + "::getSoggettiFamigliaANPR] Errore servizio Audit ", e );
			}
	    }
	}
	public List<Soggetto> getSoggettiFamigliaANPRForInit(String cfRicercato, IstanzaInitParams initParams, String clientProfileKey) throws BusinessException {
		try {
			return _getSoggettiFamigliaANPR(cfRicercato, initParams.getJwt(), clientProfileKey/*, null*/);
	    } finally {
	    	try {
	    		auditService.insertAuditANPRGetFamigliaByCF(initParams, cfRicercato);
			} catch (Exception e) {
				LOG.warn("[" + CLASS_NAME + "::getSoggettiFamigliaANPRForInit] Errore servizio Audit ", e );
			}
	    }
	}
	
	//
	// getSoggettoANPR
	//
	private Soggetto _getSoggettoANPR(String cfRicercato, String userJwt, String clientProfileKey) throws BusinessException {
		Soggetto result=null;
		try {
			if (StringUtils.isEmpty(userJwt) && StringUtils.isEmpty(clientProfileKey)) {
				clientProfileKey = EnvProperties.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_OPERATORE;			
			}
			
			List<Soggetto> soggetti = anprDAO.getFamigliaANPR(cfRicercato, userJwt, clientProfileKey);
			for (Soggetto s : soggetti) {
				if (cfRicercato.equals(s.getGeneralita().getCodiceFiscale().getCodFiscale())) {
					return s;
				}
			}
	    } catch(DAOException daoe) {
	    	LOG.error("[" + CLASS_NAME + "::getSoggettoANPR] DAOException " + daoe.getMessage());
	    	throw new BusinessException("getSoggettoANPR non trovati");
	    } catch(Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getSoggettoANPR] Exception "+e.getMessage());
	    	throw new BusinessException("getSoggettoANPR non trovati generico");
	    }
		return result;
	}
	
	@Override
	public Soggetto getSoggettoANPR(String cfRicercato, String userJwt, String ipAdress, String utente) throws BusinessException {
		// CASO standard da FOBL cittadino con Auth Shib, ci presentiamo come MOON
		try {
			return _getSoggettoANPR(cfRicercato, userJwt, EnvProperties.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO/*, null*/);
	    } finally {
	    	
	    	try {
	    		auditService.insertAuditANPRGetFamigliaByCF(utente, ipAdress, cfRicercato);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getSoggettoANPR] Errore servizio Audit ", e );
			}
	    }
	}
	@Override
	public Soggetto getSoggettoANPR(String cfRicercato, String userJwt, String clientProfileKey, String ipAdress, String utente) throws BusinessException {
		try {
			return _getSoggettoANPR(cfRicercato, userJwt, clientProfileKey);
	    } finally {
	    	try {
				auditService.insertAuditANPRGetFamigliaByCF(utente, ipAdress, cfRicercato);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getSoggettoANPR] Errore servizio Audit ", e );
			}	    }
	}
	@Override
	public Soggetto getSoggettoANPRForInit(String cfRicercato, IstanzaInitParams initParams, String clientProfileKey) throws BusinessException {
		try {
			return _getSoggettoANPR(cfRicercato, initParams.getJwt(), clientProfileKey);
	    } finally {
	    	
	    	try {
	    		auditService.insertAuditANPRGetFamigliaByCF(initParams, cfRicercato);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::getSoggettoANPRForInit   ] Errore servizio Audit ", e );
			}
	    }
	}
	
}
