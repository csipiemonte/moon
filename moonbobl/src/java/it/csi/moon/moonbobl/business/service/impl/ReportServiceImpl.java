/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.IstanzeService;
import it.csi.moon.moonbobl.business.service.ModuliService;
import it.csi.moon.moonbobl.business.service.ReportService;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ReportDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeSorter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeSorterBuilder;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaBoHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.RilevazioneServiziInfanzia;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapper;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapperFactory;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * @author fran
 * Layer di logica servizi che richiama i DAO
 */
@Component
public class ReportServiceImpl implements ReportService {
	
	private final static String CLASS_NAME = "ModuliServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ReportDAO reportDAO;

	@Autowired
	ModuliService moduliService;

	@Autowired
	IstanzeService istanzeService;
	
	@Autowired
	IstanzaDAO istanzaDAO;
	
	@Override
	public List<String> getRowsCSV(IstanzeFilter filter, String codiceModulo) throws Exception {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
		try {
			
			String sort = "";
			if (filter.getSort().isPresent()) {
				sort = filter.getSort().get();
			}
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
	
			//List<IstanzaEntity> elenco = istanzaDAO.findInLavorazione(filter, optSorter, null);
			// cerco le istanze in stato inviata = 2
			Integer stato = 2;
			List<Istanza> elenco = istanzeService.getElencoIstanze(stato, filter, optSorter, null);	
			
			List<String> rows = new ArrayList<String>();
			
			
			log.debug("[" + CLASS_NAME + "::" + "getRowsCSV" +"] elenco size : "+elenco.size());
			
			if (elenco != null && elenco.size() > 0) {
				
				ReportIstanzaMapper mapper = new ReportIstanzaMapperFactory().getReportIstanzaMapper(codiceModulo);
				
				log.debug("[" + CLASS_NAME + "::" + "remap elenco");
				rows = mapper.remap(elenco);

			}
			return rows;
			
		} catch (DAOException e1) {
			log.error("[" + CLASS_NAME + "::" + nameofCurrMethod +"] Errore invocazione servizio - ", e1);
			throw new BusinessException("Errore report " + nameofCurrMethod);
		}
	}
	
	
	@Override
	public List<Istanza> getElencoIstanze(IstanzeFilter filter) throws Exception {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
		try {
			
			String sort = "";
			if (filter.getSort().isPresent()) {
				sort = filter.getSort().get();
			}
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
	
			//List<IstanzaEntity> elenco = istanzaDAO.findInLavorazione(filter, optSorter, null);
			// cerco le istanze in stato inviata = 2
			Integer stato = 2;
			List<Istanza> elenco = istanzeService.getElencoIstanze(stato, filter, optSorter, null);	
				
			return elenco;
			
		} catch (DAOException e1) {
			log.error("[" + CLASS_NAME + "::" + nameofCurrMethod +"] Errore invocazione servizio - ", e1);
			throw new BusinessException("Errore report " + nameofCurrMethod);
		}
	}
	
	


	@Override
	public Integer getNumeroModuliInviati(UserInfo user, Long idModulo) throws BusinessException {
		try {
			if(!moduliService.verificaAbilitazioneModulo(idModulo, user)) {
				throw new BusinessException("Utente non abilitato ad operare sul modello indicato");
			}
						
			Integer count = reportDAO.countModuliInviati(idModulo);
			
			return count;

		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getNumeroModuliInviati] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getNumeroModuliInviati] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore report per id");
		}
	}


	@Override
	public Integer getNumeroComuniCompilanti(UserInfo user, Long idModulo) throws BusinessException {
		if(!moduliService.verificaAbilitazioneModulo(idModulo, user)) {
			throw new BusinessException("Utente non abilitato ad operare sul modello indicato");
		}
		IstanzeFilter filter = new IstanzeFilter();
		
		if (idModulo != null) {
			filter.setIdModuli(Arrays.asList(idModulo));
		}
		
		String sort = "";
		filter.setSort(sort);
		Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
		List<Istanza> elenco = istanzeService.getElencoIstanzeInLavorazione(filter, optSorter, null);
		int count = 0;
		Map<String, String> resultsMap = new HashMap<String, String>();
		for(Istanza istanza:elenco)
		{
			resultsMap.put((String) istanza.getMetadata(), "ok");
		}
		count = resultsMap.size();			
		return count;

	}


	@Override
	public Integer getCount(UserInfo user, Long idModulo, String tipoValore) throws BusinessException {
		if(!moduliService.verificaAbilitazioneModulo(idModulo, user)) {
			throw new BusinessException("Utente non abilitato ad operare sul modello indicato");
		}
		IstanzeFilter filter = new IstanzeFilter();
		if (idModulo != null) {
			filter.setIdModuli(Arrays.asList(idModulo));
		}
		
		String sort = "";
		filter.setSort(sort);
		Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();

		int count = 0;
		List<IstanzaEntity> elenco;
		try {
			elenco = istanzaDAO.findInLavorazione(filter, optSorter, null);
			
			if (elenco != null && elenco.size() > 0) {
				for (IstanzaEntity  entity : elenco) {
					IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());
					
					DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
					try {
						
						 int countPerIstanza = datiIstanzaBoHelper.getSommaPerIstanza(datiE.getDatiIstanza(), tipoValore);
						 count = count + countPerIstanza;
						
					} catch (Exception e) {
						log.error("[" + CLASS_NAME + "::getNumServizi02] Errore lettura comune - ",e );
					}
					
				}
			}
			return count;
			
		} catch (DAOException e1) {
			log.error("[" + CLASS_NAME + "::getNumServizi02] Errore invocazione DAO - ", e1);
			throw new BusinessException("Errore report getNumServizi02");
		}
		
	}


	//@SuppressWarnings("null")
	@Override
	public List<RilevazioneServiziInfanzia> getRowsCSV(UserInfo user, Long idModulo) throws BusinessException {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
		List<RilevazioneServiziInfanzia> listRilevazioni = new ArrayList<RilevazioneServiziInfanzia>();
		try {
			if(!moduliService.verificaAbilitazioneModulo(idModulo, user)) {
				throw new BusinessException("Utente non abilitato ad operare sul modello indicato");
			}
			IstanzeFilter filter = new IstanzeFilter();
			if (idModulo != null) {
				filter.setIdModuli(Arrays.asList(idModulo));
			}
			
			String sort = "";
			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
	
			List<IstanzaEntity> elenco;

			elenco = istanzaDAO.findInLavorazione(filter, optSorter, null);
			
			if (elenco != null && elenco.size() > 0) {
				for (IstanzaEntity  entity : elenco) {
					IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());
					
					DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
					RilevazioneServiziInfanzia rilevazione;
					try {
						rilevazione = datiIstanzaBoHelper.getRilevazione(datiE.getDatiIstanza());
					} catch (Exception e) {
						log.error("[" + CLASS_NAME + "::" + nameofCurrMethod + "] Errore invocazione datiIstanzaHelper - ", e);
						throw new BusinessException("Errore datiIstanzaHelper in " + nameofCurrMethod);
					}
					listRilevazioni.add(rilevazione);
				}
			}
			return listRilevazioni;
			
		} catch (DAOException e1) {
			log.error("[" + CLASS_NAME + "::" + nameofCurrMethod +"] Errore invocazione servizio - ", e1);
			throw new BusinessException("Errore report " + nameofCurrMethod);
		}
	}


}
