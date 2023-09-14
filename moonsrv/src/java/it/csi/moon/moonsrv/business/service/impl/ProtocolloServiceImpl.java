/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Area;
import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ProtocolloParametro;
import it.csi.moon.commons.dto.ResponseOperazioneMassiva;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.commons.entity.AreaModuloEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzaPdfEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.ProtocolloParametroEntity;
import it.csi.moon.commons.entity.ProtocolloRichiestaEntity;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.mapper.ProtocolloParametroMapper;
import it.csi.moon.commons.util.MapProtocolloAttributi;
import it.csi.moon.commons.util.ProtocolloAttributoKeys;
import it.csi.moon.commons.util.decodifica.DecodificaTipoRepositoryFile;
import it.csi.moon.moonsrv.business.service.AreeService;
import it.csi.moon.moonsrv.business.service.EntiService;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.ProtocolloService;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ProtocolloParametroDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.TagIstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.protocollo.Protocollo;
import it.csi.moon.moonsrv.business.service.impl.protocollo.ProtocolloFactory;
import it.csi.moon.moonsrv.business.service.impl.protocollo.ProtocolloParams;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.stardas.cxfclient.MetadatiType;

/**
 * Metodi di business relativi alla protocollazione delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class ProtocolloServiceImpl implements ProtocolloService {
	
	private static final String CLASS_NAME = "ProtocolloServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	IstanzeService istanzeService;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	AreaModuloDAO areaModuloDAO;
	@Autowired
	ProtocolloParametroDAO protocolloParametroDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	TagIstanzaDAO tagIstanzaDAO;
	@Autowired
	EntiService entiService;
	@Autowired
	AreeService areeService;
	@Autowired
	AllegatoDAO allegatoDAO;
	
	// Protocollazione delle istanze
	@Override
	public void protocollaIstanza(Long idIstanza) throws BusinessException {
        long start = System.currentTimeMillis();
        String errore = "";
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaIstanza] IN idIstanza: " + idIstanza);
			// Recupero l'istanza e richiama il secondo servizo
			protocollaIstanza(istanzeService.getIstanzaById(idIstanza));
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::protocollaIstanza] ");
			errore = "BusinessException";
			throw e;
		} finally {
            long end = System.currentTimeMillis();
            float msec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::protocollaIstanza] SERVICE_ELAPSED_TIME " + msec + " milliseconds." + errore);
        }
	}
	

	@Override
	public ResponseOperazioneMassiva protocollaMassivo(Long idTag) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::protocollaMassivo] IN idTag = " + idTag);
			}
			ResponseOperazioneMassiva resp = new ResponseOperazioneMassiva();
			resp.setOperation("PROTOCOLLA");
			resp.setIdTag(idTag);
			resp.setStarted(new Date());
			//
			IstanzeFilter filter = new IstanzeFilter();
			filter.setIdTag(idTag);
			List<IstanzaEntity> istanze = istanzaDAO.find(filter, Optional.empty());
			LOG.info("[" + CLASS_NAME + "::protocollaMassivo] istanze.size()=" + istanze!=null?istanze.size():"null");
//			istanze.forEach(i -> protocollaIstanza(istanzeService.getIstanzaById(i.getIdIstanza()), prtParams));
			int total = istanze!=null?istanze.size():0;
			int ok = 0;
			int ko = 0;
			for (IstanzaEntity i : istanze) {
				try {
					protocollaIstanza(istanzeService.getIstanzaById(i.getIdIstanza()));
					ok++;
				} catch (Exception e) {
					ko++;
					tagIstanzaDAO.updateEsito(idTag, i.getIdIstanza(), "ERR");
					LOG.warn("[" + CLASS_NAME + "::protocollaMassivo] Exception for istanza=" + i.getIdIstanza() + " - " + e.getMessage());
				}
			}
			LOG.info("[" + CLASS_NAME + "::protocollaMassivo] istanze.size()=" + (istanze!=null?istanze.size():"null") 
				+ "   OK=" + ok + "   KO=" + ko);
			resp.setTotal(total);
			resp.setOk(ok);
			resp.setKo(ko);
			resp.setStatus(total==ok?"SUCCESS":"WARNING");
			resp.setEnded(new Date());
			return resp;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::protocollaMassivo] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		}
	}
	
	/**
	 * Servizio principale di protocollazione di un istanza
	 */
	@Override
	public void protocollaIstanza(Istanza istanza) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::protocollaIstanza] IN istanza: "+istanza);
				LOG.debug("[" + CLASS_NAME + "::protocollaIstanza] IN istanza.idIstanza: "+istanza.getIdIstanza());
				LOG.debug("[" + CLASS_NAME + "::protocollaIstanza] IN istanza.idModulo: "+istanza.getModulo().getIdModulo());
			}
			validaIstanzaForProtocollazione(istanza);
			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte());
			istanza.setIdArea(areaModulo.getIdArea()); // TODO Da vedere se valorizzarlo da sempre molto prima, per il momento per sicurezza si rivalorizza
			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
			MapProtocolloAttributi attributi = getMapProtocolloAttributi(areaModulo);
			if (attributi!=null) {
				if (!verifyPresenzaEdEstraiNumPrt(istanza, attributi)) {
					String codiceProtocolloSistema = attributi.getWithCorrectType(ProtocolloAttributoKeys.SISTEMA_PROTOCOLLO_ISTANZA); // STARDAS, ...
					if (StringUtils.isNotEmpty(codiceProtocolloSistema)) {
						IstanzaPdfEntity pdfE = printIstanzeService.getIstanzaPdfEntityById(istanza.getIdIstanza());
						Protocollo prt = getProtocolloManager(istanza, codiceProtocolloSistema);
						ProtocolloParams params = new ProtocolloParams();
						params.setConf(attributi);
						params.setContent(pdfE.getContenutoPdf());
						params.setResoconto(pdfE.getResoconto());
						String response = prt.protocollaIstanza(istanza, params);
						LOG.info("[" + CLASS_NAME + "::protocollaIstanza] response = " + response);
					} else {
						LOG.error("[" + CLASS_NAME + "::protocollaIstanza] COD_SISTEMA_PROTOCOLLO_ISTANZA mancate per areaModulo:" + areaModulo);
						throw new BusinessException("SISTEMA_PROTOCOLLO_ISTANZA non trovata o vuota nei attributi del modulo","MOONSRV-30602");
					}
				}
			}
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaIstanza] ERROR " + be.getMessage());
			throw be;
		}
	}


	/**
	 * Estrai il numero protocollo dall'istanza se presente attribubuto NUMERO_PROTOCOLLO_DATA_KEY
	 * @param istanza
	 * @param attributi
	 * @return true se numeroProtocollo estratto è presente nell'istanza
	 */
//	private boolean verifyPresenzaEdEstraiNumPrt_OLD(Istanza istanza, MapProtocolloAttributi attributi) {
//		boolean presenzaNumeroProtocollo = false;
//		String numPrtKey = attributi.getWithCorrectType(ProtocolloAttributoKeys.NUMERO_PROTOCOLLO_FROM_ISTANZA);
//		String dataPrtKey = attributi.getWithCorrectType(ProtocolloAttributoKeys.DATA_PROTOCOLLO_FROM_ISTANZA);
//		LOG.debug("[" + CLASS_NAME + "::verifyPresenzaEdEstraiNumPrt] numPrtKey:" + numPrtKey + " dataPrtKey:" + dataPrtKey);
//		if (StringUtils.isNotBlank(numPrtKey)) {
//			DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
//			datiIstanzaHelper.initDataNode(istanza);
//			String numeroProtocolloValue = datiIstanzaHelper.extractedTextValueFromDataNodeByKey(numPrtKey);
//			if (StringUtils.isNotBlank(numeroProtocolloValue)) {
//				presenzaNumeroProtocollo = true;
//			}
//			String dataProtocolloValue = null;
//			if (StringUtils.isNotBlank(dataPrtKey)) {
//				dataProtocolloValue = datiIstanzaHelper.extractedTextValueFromDataNodeByKey(dataPrtKey);
//			}
//			LOG.info("[" + CLASS_NAME + "::verifyPresenzaEdEstraiNumPrt] numeroProtocolloValue:" + numeroProtocolloValue + " dataProtocolloValue:" + dataProtocolloValue);
//			Date dataProtocollo = null;
//			istanzaDAO.updateProtocollo(istanza.getIdIstanza(), numeroProtocolloValue, dataProtocollo);
//		}
//		return presenzaNumeroProtocollo;
//	}
	private boolean verifyPresenzaEdEstraiNumPrt(Istanza istanza, MapProtocolloAttributi attributi) {
		boolean presenzaNumeroProtocollo = false;
		String numPrtKey = attributi.getWithCorrectType(ProtocolloAttributoKeys.NUMERO_PROTOCOLLO_FROM_ISTANZA);
		String dataPrtKey = attributi.getWithCorrectType(ProtocolloAttributoKeys.DATA_PROTOCOLLO_FROM_ISTANZA);
		LOG.debug("[" + CLASS_NAME + "::verifyPresenzaEdEstraiNumPrt] numPrtKey:" + numPrtKey + " dataPrtKey:" + dataPrtKey);
		if (StringUtils.isNotBlank(numPrtKey)) {
			StrReplaceHelper strReplaceHelper = new StrReplaceHelper(istanza);
			String numeroProtocolloValue = strReplaceHelper.replaceDinamici(numPrtKey, istanza);
			if (StringUtils.isNotBlank(numeroProtocolloValue)) {
				presenzaNumeroProtocollo = true;
			}
			String dataProtocolloValue = null;
			if (StringUtils.isNotBlank(dataPrtKey)) {
				dataProtocolloValue = strReplaceHelper.replaceDinamici(dataPrtKey, istanza);
			}
			LOG.info("[" + CLASS_NAME + "::verifyPresenzaEdEstraiNumPrt] numeroProtocolloValue:" + numeroProtocolloValue + " dataProtocolloValue:" + dataProtocolloValue);
			Date dataProtocollo = convertStr2Date(dataProtocolloValue);
			istanzaDAO.updateProtocollo(istanza.getIdIstanza(), numeroProtocolloValue, dataProtocollo);
		}
		return presenzaNumeroProtocollo;
	}

	private Date convertStr2Date(String strData) {
		Date result = null;
		if (StringUtils.isNotBlank(strData)) {
			if(strData.length()==10 && Pattern.matches("[0-3][0-9]/[0-1][0-9]/[1-2][0-9][0-9][0-9]",strData)) {
				LOG.info("[" + CLASS_NAME + "::convertStr2Date] matches with dd/MM/yyyy : " + strData);
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				try {
					result = formatter.parse(strData);
				} catch (Exception e) {
					LOG.error("[" + CLASS_NAME + "::convertStr2Date] ERROR parsing with dd/MM/yyyy : " + strData);
				}
			} else {
				LOG.error("[" + CLASS_NAME + "::convertStr2Date] NO matches : " + strData);
			}
			LOG.info("[" + CLASS_NAME + "::convertStr2Date] END strData: " + strData + " result: " + result);
		}
		return result;
	}


	protected Protocollo getProtocolloManager(Istanza istanza, String codiceProtocolloSistem) {
		Protocollo prt = new ClassLoaderModulo().findProtocolloByIdModuloNullable(istanza.getModulo().getIdModulo());
		if (prt==null) {
			prt = new ProtocolloFactory().getProtocollo(codiceProtocolloSistem, istanza.getModulo().getCodiceModulo());
		}
		return prt;
	}


	private MapProtocolloAttributi getMapProtocolloAttributi(AreaModuloEntity areaModulo) {
		List<ProtocolloParametroEntity> listAttributi = protocolloParametroDAO.findForModulo(areaModulo);
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getMapProtocolloAttributi] listAttributi (non filtrati, con possibile dupplicati) ma ordinata ! \n" + listAttributi);
		}
		if (listAttributi==null || listAttributi.size()==0) {
			LOG.error("[" + CLASS_NAME + "::getMapProtocolloAttributi] ERROR Impossibile effettuare la protocollazione. Nessun configurazione trovata per ente/area/modulo = " + areaModulo.getIdEnte() + "/" + areaModulo.getIdArea() + "/" + areaModulo.getIdModulo());
			throw new BusinessException("Impossibile effettuare la protocollazione. Nessun configurazione trovata per ente/area/modulo = " + areaModulo.getIdEnte() + "/" + areaModulo.getIdArea() + "/" + areaModulo.getIdModulo(), "MOONSRV-30612");
		}
		MapProtocolloAttributi attributi = new MapProtocolloAttributi(listAttributi);
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getMapProtocolloAttributi] attributi (senza dupplicati) \n" + attributi);
		}
		return attributi;
	}
	public List<ProtocolloParametro> getProtocolloParametri(Long idModulo) {
		List<ProtocolloParametro> result = new ArrayList<>();
		List<ProtocolloParametroEntity> listAttributi = protocolloParametroDAO.findAllByIdModulo(idModulo);
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getProtocolloParametri] listAttributi (non filtrati, con possibile dupplicati) ma ordinata ! \n" + listAttributi);
		}
		if (listAttributi==null || listAttributi.size()==0) {
			return result;
		}
//		MapProtocolloAttributi attributi = new MapProtocolloAttributi(listAttributi);
		Map<String, ProtocolloParametroEntity> attributiMap = new HashMap<>();
		listAttributi.forEach( mae -> { attributiMap.put(mae.getNomeAttributo(), mae); });

		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getProtocolloParametri] attributiMap (senza dupplicati) \n" + attributiMap);
		}
		Map<Long, Ente> entiCacheMap = new HashMap<>();
		Map<Long, Area> areeCacheMap = new HashMap<>();
		result = List.copyOf(attributiMap.values()).stream()
			.map(ProtocolloParametroMapper::buildFromEntity)
			.map(pp -> completaEnte(pp, entiCacheMap))
			.map(pp -> completaArea(pp, areeCacheMap))
			.map(pp -> completaOrder(pp))
			.collect(Collectors.toList());

		return result;
	}
	private ProtocolloParametro completaEnte(ProtocolloParametro pp, Map<Long, Ente> cacheMap) throws DAOException {
		Ente ente = null;
		if (pp.getEnte()!=null && pp.getEnte().getIdEnte()!=null) {
			ente = cacheMap.get(pp.getEnte().getIdEnte());
			if (ente==null) {
				ente = entiService.getEnteById(pp.getEnte().getIdEnte());
				cacheMap.put(pp.getEnte().getIdEnte(), ente);
			}
		}
		pp.setEnte(ente);
		return pp;
	}
	private ProtocolloParametro completaArea(ProtocolloParametro pp, Map<Long, Area> cacheMap) throws DAOException {
		Area area = null;
		if (pp.getArea()!=null && pp.getArea().getIdArea()!=null) {
			area = cacheMap.get(pp.getArea().getIdArea());
			if (area==null) {
				area = areeService.getAreaById(pp.getArea().getIdArea());
				cacheMap.put(pp.getArea().getIdArea(), area);
			}
		}
		pp.setArea(area);
		return pp;
	}
	private ProtocolloParametro completaOrder(ProtocolloParametro pp) throws DAOException {
		pp.setOrder(ProtocolloAttributoKeys.byName(pp.getNomeAttributo()).getOrder());
		return pp;
	}
	
	private void validaIstanzaForProtocollazione(Istanza istanza) throws BusinessException {
		if (StringUtils.isNotEmpty(istanza.getNumeroProtocollo()) || istanza.getDataProtocollo()!=null) {
			LOG.warn("[" + CLASS_NAME + "::protocollaIstanza] numeroProtocollo = "+istanza.getNumeroProtocollo()+"   dataProtocollo = "+istanza.getDataProtocollo());
			throw new BusinessException("Istanza già protocollata "+istanza.getNumeroProtocollo(),"MOONSRV-30601");
		}
	}

	/**
	 * Servizio principale di protocollazione di un allegato from RepositoryFile
	 */
//	@Override
//	public void protocollaRepositoryFile(Long idFile, IstanzaPrtParams prtParams) throws BusinessException {
//		try {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("[" + CLASS_NAME + "::protocollaRepositoryFile] IN idFile: "+idFile);
//				LOG.debug("[" + CLASS_NAME + "::protocollaRepositoryFile] IN prtParams: "+prtParams);
//			}
//			RepositoryFileEntity file = repositoryFileDAO.findById(idFile);
//			Istanza istanza = istanzeService.getIstanzaById(file.getIdIstanza());
//			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte()); // Un modulo DEVE essere solo una volta in un ente.
//			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
//			List<ProtocolloParametroEntity> listAttributi = protocolloParametroDAO.findForModulo(areaModulo);
//			LOG.debug("[" + CLASS_NAME + "::protocollaRepositoryFile] listAttributi (non filtrati, con possibile dupplicati) ma ordinata ! \n" + listAttributi);
//			MapProtocolloAttributi attributi = new MapProtocolloAttributi(listAttributi);
//			LOG.debug("[" + CLASS_NAME + "::protocollaRepositoryFile] attributi (senza dupplicati) \n" + attributi);
//			if (listAttributi!=null && listAttributi.size()>=1) {
//				String codiceProtocolloSistema = attributi.getWithCorrectType(ProtocolloAttributoKeys.SISTEMA_PROTOCOLLO_ISTANZA); // STARDAS, ...
//				if (StringUtils.isNotEmpty(codiceProtocolloSistema)) {
//					Protocollo prt = new ProtocolloFactory().getProtocollo(codiceProtocolloSistema, istanza.getModulo().getCodiceModulo());
//					ProtocolloParams params = new ProtocolloParams();
//					params.setConf(attributi);
//					params.setContent(null); // contentPrincipale
//					params.setPrtParams(prtParams);
//					params.setRepositoryFile(file);
//					String response = prt.protocollaRepositoryFile(istanza, params);
//					LOG.info("[" + CLASS_NAME + "::protocollaRepositoryFile] response = " + response);
//				} else {
//					LOG.error("[" + CLASS_NAME + "::protocollaRepositoryFile] COD_SISTEMA_PROTOCOLLO_ISTANZA mancate per areaModulo:" + areaModulo);
//					throw new BusinessException("ConfProtocolloException");
//				}
//			}
//		} catch (BusinessException e) {
//			LOG.error("[" + CLASS_NAME + "::protocollaRepositoryFile] ERROR " + e.getMessage());
//			throw e;
//		}
//	}

	@Override
	public void protocollaIntegrazione(Long idIstanza, Long idStoricoWorkflow) throws BusinessException {
        long start = System.currentTimeMillis();
        String errore = "";
		try {
			LOG.debug("[" + CLASS_NAME + "::protocollaIntegrazione] IN idIstanza: "+idIstanza + "  idStoricoWorkflow: "+idStoricoWorkflow);
			// Genero PDF dell'integrazione
			RepositoryFileEntity pdfE = printIstanzeService.getRenderedFileEntityByIdSw(idIstanza, idStoricoWorkflow, DecodificaTipoRepositoryFile.FO_RISPOSTA_INTEGRAZIONE);
			LOG.debug("[" + CLASS_NAME + "::protocollaIntegrazione] contentIntegrazione.length=" + pdfE.getContenuto().length);
			//
			Istanza istanza = istanzeService.getIstanzaById(idIstanza);
			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte()); // Un modulo DEVE essere solo una volta in un ente.
			istanza.setIdArea(areaModulo.getIdArea()); // TODO Da vedere se valorizzarlo da sempre molto prima, per il momento per sicurezza si rivalorizza
			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
			MapProtocolloAttributi attributi = getMapProtocolloAttributi(areaModulo);
			if (attributi!=null) {
				String codiceProtocolloSistema = attributi.getWithCorrectType(ProtocolloAttributoKeys.SISTEMA_PROTOCOLLO_ISTANZA); // STARDAS, ...
				if (StringUtils.isNotEmpty(codiceProtocolloSistema)) {
					Protocollo prt = getProtocolloManager(istanza, codiceProtocolloSistema);
					ProtocolloParams params = new ProtocolloParams();
					params.setConf(attributi);
					params.setContent(pdfE.getContenuto());
					String response = prt.protocollaIntegrazione(istanza, idStoricoWorkflow, params);
					LOG.info("[" + CLASS_NAME + "::postProtocollaIntegrazione] response = " + response);
				} else {
					LOG.error("[" + CLASS_NAME + "::postProtocollaIntegrazione] COD_SISTEMA_PROTOCOLLO_ISTANZA mancate per areaModulo:" + areaModulo);
					throw new BusinessException("ConfProtocolloException");
				}
			}
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::protocollaIntegrazione] ");
			errore = "BusinessException";
			throw e;
		} finally {
            long end = System.currentTimeMillis();
            float msec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::protocollaIntegrazione] SERVICE_ELAPSED_TIME " + msec + " milliseconds." + errore);
        }
	}


	@Override
	public void protocollaFile(Long idFile) {
        long start = System.currentTimeMillis();
        String errore = "";
        try {
			LOG.debug("[" + CLASS_NAME + "::protocollaFile] IN idFile: " + idFile);
			RepositoryFileEntity repositoryFile = repositoryFileDAO.findById(idFile);
			LOG.debug("[" + CLASS_NAME + "::protocollaFile] repositoryFile.getContenuto().length=" + repositoryFile.getContenuto().length);
			//
			Istanza istanza = istanzeService.getIstanzaById(repositoryFile.getIdIstanza());
			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte()); // Un modulo DEVE essere solo una volta in un ente.
			istanza.setIdArea(areaModulo.getIdArea()); // TODO Da vedere se valorizzarlo da sempre molto prima, per il momento per sicurezza si rivalorizza
			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
//			List<ProtocolloParametroEntity> listAttributi = protocolloParametroDAO.findForModulo(areaModulo);
//			LOG.debug("[" + CLASS_NAME + "::protocollaFile] listAttributi (non filtrati, con possibile dupplicati) ma ordinata ! \n" + listAttributi);
//			MapProtocolloAttributi attributi = new MapProtocolloAttributi(listAttributi);
//			LOG.debug("[" + CLASS_NAME + "::protocollaFile] attributi (senza dupplicati) \n" + attributi);
			MapProtocolloAttributi attributi = getMapProtocolloAttributi(areaModulo);
			if (attributi!=null) {
				String codiceProtocolloSistema = attributi.getWithCorrectType(ProtocolloAttributoKeys.SISTEMA_PROTOCOLLO_ISTANZA); // STARDAS, ...
				if (StringUtils.isNotEmpty(codiceProtocolloSistema)) {
					Protocollo prt = getProtocolloManager(istanza, codiceProtocolloSistema);
					ProtocolloParams params = new ProtocolloParams();
					params.setConf(attributi);
					params.setContent(repositoryFile.getContenuto());
					params.setRepositoryFile(repositoryFile);
					String response = prt.protocollaFile(istanza, params);
					LOG.info("[" + CLASS_NAME + "::protocollaFile] response = " + response);
				} else {
					LOG.error("[" + CLASS_NAME + "::protocollaFile] COD_SISTEMA_PROTOCOLLO_ISTANZA mancate per areaModulo:" + areaModulo);
					throw new BusinessException("ConfProtocolloException");
				}
			}
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::protocollaFile] ");
			errore = "BusinessException";
			throw be;
		} finally {
            long end = System.currentTimeMillis();
            float msec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::protocollaFile] SERVICE_ELAPSED_TIME " + msec + " milliseconds." + errore);
        }
	}

	//xTest
	@Override
	public MetadatiType retrieveMetadatiIstanza(Long idIstanza) throws BusinessException {
		try {
			MetadatiType result = null;
			LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiIstanza] IN idIstanza: "+idIstanza);
			// Recupero l'istanza e richiama il secondo servizo
			Istanza istanza = istanzeService.getIstanzaById(idIstanza);
			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte());
			istanza.setIdArea(areaModulo.getIdArea()); // TODO Da vedere se valorizzarlo da sempre molto prima, per il momento per sicurezza si rivalorizza
			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
			MapProtocolloAttributi attributi = getMapProtocolloAttributi(areaModulo);
			if (attributi!=null) {
				String codiceProtocolloSistema = attributi.getWithCorrectType(ProtocolloAttributoKeys.SISTEMA_PROTOCOLLO_ISTANZA); // STARDAS, ...
				if (StringUtils.isNotEmpty(codiceProtocolloSistema)) {
					IstanzaPdfEntity pdfE = printIstanzeService.getIstanzaPdfEntityById(istanza.getIdIstanza());
					Protocollo prt = getProtocolloManager(istanza, codiceProtocolloSistema);
					ProtocolloParams params = new ProtocolloParams();
					params.setConf(attributi);
					params.setContent(pdfE.getContenutoPdf());
					params.setResoconto(pdfE.getResoconto());
					boolean isDocFirmato = false;
					result = prt._retrieveMetadati(istanza, params, ProtocolloRichiestaEntity.TipoDoc.ISTANZA, isDocFirmato);
					LOG.info("[" + CLASS_NAME + "::retrieveMetadatiIstanza] result = " + result);
				} else {
					LOG.error("[" + CLASS_NAME + "::retrieveMetadatiIstanza] COD_SISTEMA_PROTOCOLLO_ISTANZA mancate per areaModulo:" + areaModulo);
					throw new BusinessException("SISTEMA_PROTOCOLLO_ISTANZA non trovata o vuota nei attributi del modulo","MOONSRV-30602");
				}
			}
			return result;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiIstanza] ERROR " + be.getMessage());
			throw be;
		}
	}
	
	@Override
	public MetadatiType retrieveMetadatiAllegato(Long idIstanza, Long idAllegato) throws BusinessException {
		try {
			MetadatiType result = null;
			LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiAllegato] IN idIstanza: "+idIstanza);
			// Recupero l'istanza e richiama il secondo servizo
			Istanza istanza = istanzeService.getIstanzaById(idIstanza);
			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte());
			istanza.setIdArea(areaModulo.getIdArea()); // TODO Da vedere se valorizzarlo da sempre molto prima, per il momento per sicurezza si rivalorizza
			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
			MapProtocolloAttributi attributi = getMapProtocolloAttributi(areaModulo);
			if (attributi!=null) {
				String codiceProtocolloSistema = attributi.getWithCorrectType(ProtocolloAttributoKeys.SISTEMA_PROTOCOLLO_ISTANZA); // STARDAS, ...
				if (StringUtils.isNotEmpty(codiceProtocolloSistema)) {
					Protocollo prt = getProtocolloManager(istanza, codiceProtocolloSistema);
					ProtocolloParams params = new ProtocolloParams();
					params.setConf(attributi);
					params.setContent(null);
					params.setResoconto(null);
					boolean isDocFirmato = false;
					AllegatoLazyEntity allegato = allegatoDAO.findLazyById(idAllegato);
					if (!idIstanza.equals(allegato.getIdIstanza())) {
						LOG.error("[" + CLASS_NAME + "::retrieveMetadatiAllegato] idAllegato:" + idAllegato + " e dell'istanza " + allegato.getIdIstanza() + " e non dell'istanza " + idIstanza);
						throw new ItemNotFoundBusinessException();
					}
					result = prt._retrieveMetadatiAllegato(istanza, params, ProtocolloRichiestaEntity.TipoDoc.ISTANZA_ALLEGATO, isDocFirmato, allegato);
					LOG.info("[" + CLASS_NAME + "::retrieveMetadatiAllegato] result = " + result);
				} else {
					LOG.error("[" + CLASS_NAME + "::retrieveMetadatiAllegato] COD_SISTEMA_PROTOCOLLO_ISTANZA mancate per areaModulo:" + areaModulo);
					throw new BusinessException("SISTEMA_PROTOCOLLO_ISTANZA ALLEGATO non trovata o vuota nei attributi del modulo","MOONSRV-30604");
				}
			}
			return result;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::retrieveMetadatiAllegato] ERROR " + be.getMessage());
			throw be;
		}
	}
	
	@Override
	public String getProtocolloManagerName(String codiceProtocolloSistema, String codiceModulo) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getProtocolloManagerName] IN codiceProtocolloSistema: " + codiceProtocolloSistema + " codiceModulo: " + codiceModulo);
			}
			Protocollo prt = new ProtocolloFactory().getProtocollo(codiceProtocolloSistema, codiceModulo);
//			String responseCanonical = prt.getClass().getCanonicalName();
//			LOG.info("[" + CLASS_NAME + "::getProtocolloManagerName] responseCanonical = " + responseCanonical);
			String response = prt.getClass().getName();
			LOG.info("[" + CLASS_NAME + "::getProtocolloManagerName] response = " + response);
			return response;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getProtocolloManagerName] ERROR " + be.getMessage());
			throw be;
		}
	}
	
	@Override
	public String getProtocolloManagerName(String codiceModulo) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getProtocolloManagerName] IN codiceModulo: "+codiceModulo);
			}
			AreaModuloEntity areaModulo = areaModuloDAO.findByCdModulo(codiceModulo);
			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
			MapProtocolloAttributi attributi = getMapProtocolloAttributi(areaModulo);
			String codiceProtocolloSistema = attributi.getWithCorrectType(ProtocolloAttributoKeys.SISTEMA_PROTOCOLLO_ISTANZA); // STARDAS, ...
			if (StringUtils.isEmpty(codiceProtocolloSistema)) {
				LOG.error("[" + CLASS_NAME + "::getProtocolloManagerName] COD_SISTEMA_PROTOCOLLO_ISTANZA mancate per areaModulo:" + areaModulo);
				throw new BusinessException("SISTEMA_PROTOCOLLO_ISTANZA non trovata o vuota nei attributi del modulo","MOONSRV-30602");
			}
			Protocollo prt = new ProtocolloFactory().getProtocollo(codiceProtocolloSistema, codiceModulo);
//			String responseCanonical = prt.getClass().getCanonicalName();
//			LOG.info("[" + CLASS_NAME + "::getProtocolloManagerName] responseCanonical = " + responseCanonical);
			String response = prt.getClass().getName();
			LOG.info("[" + CLASS_NAME + "::getProtocolloManagerName] response = " + response);
			return response;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getProtocolloManagerName] ERROR " + be.getMessage());
			throw be;
		}
	}
	
}
