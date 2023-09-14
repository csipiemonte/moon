/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.territorio.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.territorio.Civico;
import it.csi.moon.commons.dto.extra.territorio.Piano;
import it.csi.moon.commons.dto.extra.territorio.PianoNUI;
import it.csi.moon.commons.dto.extra.territorio.UiuCivicoIndirizzo;
import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonsrv.business.be.extra.territorio.ToponomasticaApi;
import it.csi.moon.moonsrv.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonsrv.business.service.territorio.ToponomasticaService;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class ToponomasticaApiImpl extends MoonBaseApiImpl implements ToponomasticaApi {
	
	private static final String CLASS_NAME = "ToponomasticaApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	@Qualifier("RS")
	private ToponomasticaService toponomasticaService = null;

	//
	// Entity Via
	@Override
	public Response getVie(int limit, int skip, String filter, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			boolean flagLimitSkip = validaLimitSkip(limit, skip);
			List<Via> result = toponomasticaService.getVie(limit,skip,filter);
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException("Vie non trovate.");
			}
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getVie] Errore UnprocessableEntityException");
			throw uee;
		} catch (ServiceException se) {
			LOG.warn("[" + CLASS_NAME + "::getVie] ServiceException servizio getVie" + se.getMessage());
			throw se;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getVie] Errore generico servizio getVie", ex);
			throw new ServiceException("Errore generico servizio elenco getVie");
		}
	}

	/**
	 * limit è obbligatorio nel caso di paginazione
	 * @param limit
	 * @param skip
	 * @return
	 */
	private boolean validaLimitSkip(int limit, int skip) {
		if (skip<0 || limit<0 || (skip>0 && limit<1)) {
			throw new UnprocessableEntityException("limit parameter required more than zero, if using skip parameter.");
		}
		return (limit>0)?true:false;
	}


	@Override
	public Response getViaById(String codiceVia, String fields, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Integer idVia = validaIntegerRequired(codiceVia);
			Via result = toponomasticaService.getViaById(idVia);
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException("Via "+codiceVia+" non trovata.");
			}
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getViaById] Risorsa non trovata con codiceVia: " + codiceVia);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getViaById] Errore UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getViaById] Errore generico servizio getViaById", ex);
			throw new ServiceException("Errore generico servizio getViaById");
		}
	}

	
    //
    // Integer - Numero Radici
	@Override
	public Response getNumeriRadiceByVia(String codiceVia, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Integer idVia = validaIntegerRequired(codiceVia);
			List<Integer> result = toponomasticaService.getNumeriRadiceByVia(idVia);
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException("Vie non trovate.");
			}
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getViaById] Errore generico servizio getNumeriRadiceByVia", ex);
			throw new ServiceException("Errore generico servizio elenco getNumeriRadiceByVia");
		}
	}
	
	
    //
	// Entity Civico
	@Override
	public Response getCiviciByVia(String codiceVia, String tipologieCivico, String statiQP, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Integer idVia = validaIntegerRequired(codiceVia);
			List<Integer> stati = validaListInteger(statiQP);
			List<Civico> result = toponomasticaService.getCiviciByVia(idVia, tipologieCivico, stati);
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException();
			}
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCiviciByVia] Errore generico servizio getCiviciByVia", ex);
			throw new ServiceException("Errore generico servizio elenco getCiviciByVia");
		}
	}

	@Override
	public Response getCivicoById(String codiceVia, String codiceCivico, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Integer idVia = validaIntegerRequired(codiceVia);
			Integer idCivico = validaIntegerRequired(codiceCivico);
			Civico result = toponomasticaService.getCivicoById(idVia, idCivico);
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException("Civico "+codiceCivico+" di Via "+codiceVia+" non trovato.");
			}
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] Risorsa non trovata con codiceVia: " + codiceVia + "  codiceCivico: "+codiceCivico);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] Errore UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] Errore generico servizio getCivicoById", ex);
			throw new ServiceException("Errore generico servizio getCivicoById");
		}
	}
    
	@Override
	public Response getCiviciByViaNumero(String codiceVia, String numero, String tipologieCivico, String statiQP, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Integer idVia = validaIntegerRequired(codiceVia);
			Integer num = validaIntegerRequired(numero);
			List<Integer> stati = validaListInteger(statiQP);
			List<Civico> result = toponomasticaService.getCiviciByViaNumero(idVia, num, tipologieCivico, stati);
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException();
			}
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getCiviciByViaNumero] Risorsa non trovata con codiceVia: " + codiceVia + "  numero: "+numero);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getCiviciByViaNumero] Errore UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCiviciByViaNumero] Errore generico servizio getCiviciByViaNumero", ex);
			throw new ServiceException("Errore generico servizio elenco getCiviciByViaNumero");
		}
	}
	
	
    //
	// Entity PianoNUI
	@Override
	public Response getPianiNuiByCivico(String codiceVia, String codiceCivico, String fields, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Integer idVia = validaIntegerRequired(codiceVia);
			Integer idCivico = validaIntegerRequired(codiceCivico);
			List<PianoNUI> result = toponomasticaService.getPianiNuiByCivico(idVia,idCivico);
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException();
			}
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPianiNuiByCivico] Risorsa non trovata con codiceVia: " + codiceVia + "  codiceCivico: "+codiceCivico);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getPianiNuiByCivico] Errore UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPianiNuiByCivico] Errore generico servizio getPianiNuiByCivico", ex);
			throw new ServiceException("Errore generico servizio elenco PianiNuiByCivico");
		} 
	}

	@Override
	public Response getPianoNuiById(String codiceVia, String codiceCivico, String codicePianoNui, String fields, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Integer idVia = validaIntegerRequired(codiceVia);
			Integer idCivico = validaIntegerRequired(codiceCivico);
			Integer idPianoNui = validaIntegerRequired(codicePianoNui);
			PianoNUI ris = toponomasticaService.getPianoNuiById(idVia, idCivico, idPianoNui);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException();
			}
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPianoNuiById] Risorsa non trovata con codiceVia: " + codiceVia + "  codiceCivico: "+codiceCivico + "  codicePianoNui: "+codicePianoNui);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getPianoNuiById] Errore UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPianoNuiById] Errore generico servizio getPianoNuiById", ex);
			throw new ServiceException("Errore generico servizio getPianoNuiById");
		} 
	}
	
	
    //
	// Entity Piano
	@Override
	public Response getPiani(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<Piano> result = toponomasticaService.getPiani();
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException();
			}
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPiani] Errore generico servizio getPiani", ex);
			throw new ServiceException("Errore generico servizio elenco Piani");
		}
	}

    public Response getPianoById(String codicePiano,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Piano result = toponomasticaService.getPianoById(codicePiano);
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException();
			}
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPianoById] Risorsa non trovata con codicePiano: " + codicePiano);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getPianoById] Errore UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPianoById] Errore generico servizio getPianoById", ex);
			throw new ServiceException("Errore generico servizio getPianoById");
		}
    }

    public Response getPianiByCivico( String codiceVia, String codiceCivico, String fields,
        SecurityContext securityContext, HttpHeaders httpHeaders , HttpServletRequest httpRequest) {
		try {
			Integer idVia = validaIntegerRequired(codiceVia);
			Integer idCivico = validaIntegerRequired(codiceCivico);
			List<Piano> result = toponomasticaService.getPianiByCivico(idVia,idCivico);
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException();
			}
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPianiByCivico] Errore generico servizio getPianiByCivico", ex);
			throw new ServiceException("Errore generico servizio elenco PianiByCivico");
		} 
	}

	@Override
	public Response getUiuCivicoIndirizzoByTriplettaCatastale(String foglio, String numero, String subalterno,
			String fields, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws UnprocessableEntityException, ResourceNotFoundException {
		try {
			List<UiuCivicoIndirizzo> result = toponomasticaService.getUiuCivicoIndirizzoByTriplettaCatastale(foglio, numero, subalterno);
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException();
			}
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getUiuCivicoIndirizzoByTriplettaCatastale] Risorsa non trovata con foglio: " + foglio + "  numero: "+numero + "  subalterno: "+subalterno);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getUiuCivicoIndirizzoByTriplettaCatastale] Errore UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getUiuCivicoIndirizzoByTriplettaCatastale] Errore generico servizio getUiuCivicoIndirizzoByTriplettaCatastale", ex);
			throw new ServiceException("Errore generico servizio elenco getUiuCivicoIndirizzoByTriplettaCatastale");
		}
	}

}
