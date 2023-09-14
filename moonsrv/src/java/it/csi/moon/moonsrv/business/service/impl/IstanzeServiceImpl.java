/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.AllegatiSummary;
import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.dto.Pagamento;
import it.csi.moon.commons.dto.RepositoryFile;
import it.csi.moon.commons.dto.ResponseOperazioneMassiva;
import it.csi.moon.commons.entity.EpayRichiestaEntity;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.commons.entity.IstanzeSorterBuilder;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.RepositoryFileFilter;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.commons.mapper.IstanzaMapper;
import it.csi.moon.commons.mapper.PagamentoMapper;
import it.csi.moon.commons.mapper.PagamentoNotificaMapper;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.commons.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.moonsrv.business.service.AllegatiService;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.ModuliService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.RepositoryFileService;
import it.csi.moon.moonsrv.business.service.dto.IstanzaInitCompletedParams;
import it.csi.moon.moonsrv.business.service.dto.NotificatoreDatiInit;
import it.csi.moon.moonsrv.business.service.helper.DatiIstanzaHelper;
import it.csi.moon.moonsrv.business.service.helper.task.EstraiDichiaranteTask;
import it.csi.moon.moonsrv.business.service.helper.task.SendEmailDichiaranteIstanzaTask;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayNotificaPagamentoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO.ModuloAttributoFilter;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.TagIstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.initializer.IstanzaInitializer;
import it.csi.moon.moonsrv.business.service.notificatore.NotificatoreService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi alle istanze
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
@Configurable
public class IstanzeServiceImpl implements IstanzeService {
	
	private static final String CLASS_NAME = "IstanzeServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final String DEFAULT_FIELDS_ISTANZA = null;
	
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	StatoDAO statoDAO;
	@Autowired
	ModuliService moduliService;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	ModuloProgressivoDAO moduloProgressivoDAO;
	@Autowired
	IstanzaInitializer istanzaInitializer;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	AllegatiService allegatiService;
	@Autowired
	TagIstanzaDAO tagIstanzaDAO;
	@Autowired
	EpayRichiestaDAO epayRichiestaDAO;
	@Autowired
	EpayNotificaPagamentoDAO epayNotificaPagamentoDAO;
	@Autowired
	NotificatoreService notificatoreService;
	@Autowired
	RepositoryFileService repositoryFileService;

	public IstanzeServiceImpl() {
		super();
	}

	@Override
	public Istanza getIstanzaById(Long idIstanza) throws BusinessException {
		return getIstanzaById(idIstanza, "nonVuotoPerModuloRetroCompatibile");
	}
	@Override
	public Istanza getIstanzaById(Long idIstanza, String fields) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaById] IN idIstanza: "+idIstanza+"  fields="+fields);
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);
			return getIstanzaByEntity(entity, fields);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaById] Errore invocazione DAO - ", daoe);
			throw new BusinessException(daoe);
		}
	}
	@Override
	public Istanza getIstanzaByCd(String codiceIstanza) throws BusinessException {
		return getIstanzaByCd(codiceIstanza, "nonVuotoPerModuloRetroCompatibile");
	}
	@Override
	public Istanza getIstanzaByCd(String codiceIstanza, String fields) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaByCd] IN codiceIstanza: "+codiceIstanza+"  fields="+fields);
			IstanzaEntity entity = istanzaDAO.findByCd(codiceIstanza);
			return getIstanzaByEntity(entity, fields);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaByCd] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaByCd] Errore invocazione DAO - ", daoe);
			throw new BusinessException(daoe);
		}
	}
	private Istanza getIstanzaByEntity(IstanzaEntity entity, String fields) throws BusinessException {
		try {
			if(StrUtils.isEmpty(fields)) {
				fields = DEFAULT_FIELDS_ISTANZA;
			}
			LOG.debug("[" + CLASS_NAME + "::getIstanzaByEntity] IN entity: "+entity+"  fields="+fields);
			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(entity.getIdIstanza());
			IstanzaDatiEntity datiE = istanzaDAO.findDati(entity.getIdIstanza(), cronE.getIdCronologiaStati());
			StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
			Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE); // modulo minimo con solo Ids
			istanza = completeRequestFields(fields, istanza, entity);
			return istanza;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaByEntity] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaByEntity] Errore invocazione DAO - ", daoe);
			throw new BusinessException(daoe);
		}
	}
	private Istanza completeRequestFields(String fields, Istanza result, IstanzaEntity entity) {
		if (StringUtils.isEmpty(fields)) {
			return result;
		}
		List<String> listFields = Arrays.asList(fields.split(","));
		if (listFields.contains("attributiTicketCRM")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] modulo");
			result = completaIstanzaModulo(result, entity, "attributiTicketCRM");
		} else {
			if (listFields.contains("attributiCOSMO")) {
				LOG.debug("[" + CLASS_NAME + "::completeRequestFields] modulo");
				result = completaIstanzaModulo(result, entity, "attributiCOSMO");
			} else {
				if (listFields.contains("attributiMAIL")) {
					LOG.debug("[" + CLASS_NAME + "::completeRequestFields] modulo");
					result = completaIstanzaModulo(result, entity, "attributiMAIL");
				} else {
					if (listFields.contains("attributiEPAY")) {
						LOG.debug("[" + CLASS_NAME + "::completeRequestFields] modulo");
						result = completaIstanzaModulo(result, entity, "attributiEPAY");
					} else {
						// completiamo un minimo il modulo con gli atributi di primo livello
						LOG.debug("[" + CLASS_NAME + "::completeRequestFields] modulo semplice");
						result = completaIstanzaModulo(result, entity, "niente");
					}
				}
			}
		}
		if (listFields.contains("completaEPAY")) {
			result = completaIstanzaEpay(result);
		}
		return result;
	}
	
	private Istanza completaIstanzaModulo(Istanza istanza, IstanzaEntity entity, String filter) {
		istanza.setModulo(moduliService.getModuloById(entity.getIdModulo(), entity.getIdVersioneModulo(), filter));
		return istanza;
	}
	
	public Istanza completaIstanzaEpay(Istanza istanza) {
		try {
			List<EpayRichiestaEntity> richiesteE = epayRichiestaDAO.findByIdIstanza(istanza.getIdIstanza());
			if (richiesteE != null && richiesteE.size()>0) {
				EpayRichiestaEntity richiesta = richiesteE.get(richiesteE.size()-1);
				istanza.setCodiceAvviso(richiesta.getCodiceAvviso());
				istanza.setIuv(richiesta.getIuv());
				istanza.setDataEsitoPagamento(richiesta.getDataNotificaPagamento());
				istanza.setPagamenti(richiesteE.stream()
					.map( r -> {
						Pagamento res = PagamentoMapper.buildFromEpayRichiestaEntity(r);
						return completaPagamentoNotifica(res, r.getIdNotificaPagamento());
					})
					.collect(Collectors.toList()));
			}
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::completaIstanzaEpay] Exception but continue istanza:" + istanza.getIdIstanza(), e);
		}
		return istanza;
	}

	Pagamento completaPagamentoNotifica(Pagamento pagamento, Long idNotifica) {
		if (idNotifica == null)
			return pagamento;
		pagamento.setNotifica(PagamentoNotificaMapper.buildFromEntity(epayNotificaPagamentoDAO.findById(idNotifica)));
		return pagamento;
	}

	@Override
	public String getIstanzaDataById(Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaDataById] IN idIstanza: "+idIstanza);
			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
			return datiE.getDatiIstanza();
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaDataById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaDataById] Errore invocazione DAO - ", daoe);
			throw new BusinessException(daoe);
		}
	}
	
	@Override
	public List<Istanza> getElencoIstanze(IstanzeFilter filter, Optional<IstanzeSorter> sorter) throws BusinessException {
		List<Istanza> elencoIstanze = new ArrayList<>();
		try {
			List<IstanzaEntity> elenco = istanzaDAO.find(filter, sorter);
			if (elenco != null && elenco.size() > 0) {
				statoDAO.initCache();
				moduloDAO.initCache();
				for (IstanzaEntity  entity : elenco) {
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),entity.getIdVersioneModulo());
					List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
					MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
					elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity,statoE,moduloE/*,attributi*/));
				}
			}
			return elencoIstanze;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoIstanze] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero elenco istanza");
 		}
	}

	
	@Override
	public Istanza getInitIstanza(IstanzaInitParams initParams) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getInitIstanza] IN initParams: "+initParams);
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(initParams.getIdModulo(), initParams.getIdVersioneModulo()); 
			IstanzaInitCompletedParams completedParams = new IstanzaInitCompletedParams(initParams);
			Istanza result = null;
			List<ModuloAttributoEntity> listAttributi = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
			MapModuloAttributi attributi = new MapModuloAttributi(listAttributi);
			if (listAttributi!=null && listAttributi.size()>=1) {
				String initDataNomeClass = attributi.getWithCorrectType(ModuloAttributoKeys.INIT_NOME_CLASS); // socioassistenziale.SostegnoEconomicoCovi19Initializer ; demografia.CambioIndirizzoInitializer
				Boolean initObbligatorio = attributi.getWithCorrectType(ModuloAttributoKeys.INIT_OBBLIGATORIA); // null ; true
				initParams.setFlagAnprSpento(attributi.getWithCorrectType(ModuloAttributoKeys.ANPR_SPENTO)); // false ; null
				completedParams.setModuloAttributi(attributi);
				completedParams = completaNotificatore(completedParams);
				istanzaInitializer.initialize(completedParams, moduloE);
				LOG.debug("[" + CLASS_NAME + "::getInitIstanza] initDataNomeClass = " + initDataNomeClass);

				String istanzaData = istanzaInitializer.getDatiIstanza(initDataNomeClass,initParams.getFlagCompilaBo());
				LOG.debug("[" + CLASS_NAME + "::getInitIstanza] istanzaData = " + istanzaData);
				LOG.debug("[" + CLASS_NAME + "::getInitIstanza] Controllo obbligatorieta istanzaData ... initObbligatorio=" + initObbligatorio);
				if (Boolean.TRUE.equals(initObbligatorio) && StringUtils.isEmpty(istanzaData) ) {
					throw new BusinessException("Non è stato possibile acquisire i dati necessari per iniziare la compilazione","MOONSRV-00210");
				}
				result = IstanzaMapper.buildFromIstanzaEntity(istanzaInitializer.getInitIstanza(), istanzaData, DecodificaStatoIstanza.BOZZA.getStatoEntity(), moduloE/*, attributi*/);
			} else {
				LOG.warn("[" + CLASS_NAME + "::getInitIstanza] moduloAttributi mancanti per idModulo="+moduloE.getIdModulo());
				istanzaInitializer.initialize(completedParams, moduloE);
				result = IstanzaMapper.buildFromIstanzaEntity(istanzaInitializer.getInitIstanza(), "", DecodificaStatoIstanza.BOZZA.getStatoEntity(), moduloE/*, attributi*/);
			}			
			return result;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getInitIstanza] Errore invocazione DAO");
			throw new BusinessException(daoe);
		} catch (ItemNotFoundBusinessException e) {
			// Rilanciato dalla init di cambio indirizzo			
				throw  e;				
			
		}
	}
	
	private IstanzaInitCompletedParams completaNotificatore(IstanzaInitCompletedParams completedParams) {
		Optional<NotificatoreDatiInit> notificatoreDatiInitOpt = notificatoreService.getDatiInit(completedParams);
		if (notificatoreDatiInitOpt.isPresent()) {
			completedParams.setNotificatoreDatiInit(notificatoreDatiInitOpt.get());
		}
		return completedParams;
	}

	private void inviaEmail(Long idIstanza, String dest, boolean rinvio) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::inviaEmail] IN idIstanza = " + idIstanza);
				LOG.debug("[" + CLASS_NAME + "::inviaEmail] IN dest = " + dest);
				LOG.debug("[" + CLASS_NAME + "::inviaEmail] IN rinvio = " + rinvio);
			}
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);
			
			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
			StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(), entity.getIdVersioneModulo());
			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModuloFilter(moduloE.getIdModulo(), ModuloAttributoFilter.EMAIL);
			MapModuloAttributi mapModuloAttributi = new MapModuloAttributi(attributiModuloE);
			Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE/*, mapModuloAttributi*/);

			String confSendEmail = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_EMAIL_CONF);
			if (StringUtils.isNotEmpty(confSendEmail)) {
				LOG.info("[" + CLASS_NAME + "::inviaEmail] new SendEmailDichiaranteIstanzaTask().call() ...");
				new SendEmailDichiaranteIstanzaTask(istanza, confSendEmail, dest, rinvio).call();
			} else {
				LOG.error("[" + CLASS_NAME + "::inviaEmail] attributo PSIT_EMAIL_CONF mancante per il modulo " + moduloE.getIdModulo() + " " + moduloE.getCodiceModulo());
			}
			String response = null;

			LOG.debug("[" + CLASS_NAME + "::inviaEmail] NOT IMPLEMENTED response = " + response);

		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::inviaEmail] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		}
	}

	@Override
	public void inviaEmail(Long idIstanza) throws BusinessException {
		inviaEmail(idIstanza, "", false);
	}
	
	@Override
	public void rinviaEmail(Long idIstanza, String dest) throws BusinessException {
		inviaEmail(idIstanza, dest, true);
	}

	@Override
	public void rinviaEmails(Long idTag, String dest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::rinviaEmails] IN idTag = " + idTag);
				LOG.debug("[" + CLASS_NAME + "::rinviaEmails] IN dest = " + dest);
			}
			IstanzeFilter filter = new IstanzeFilter();
			filter.setIdTag(idTag);
			List<IstanzaEntity> istanze = istanzaDAO.find(filter, Optional.empty());
			LOG.info("[" + CLASS_NAME + "::rinviaEmails] istanze.size()=" + ((istanze!=null)?istanze.size():"null"));
			if (istanze!=null) {
				istanze.forEach(i -> rinviaEmail(i.getIdIstanza(), dest));
			}
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::rinviaEmails] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		}
	}


	@Override
	public void estraiDichiaranteUpdate(Long idIstanza) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::estraiDichiaranteUpdate] IN idIstanza = " + idIstanza);
			}
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);
			if (DecodificaStatoIstanza.BOZZA.isCorrectStato(entity) || DecodificaStatoIstanza.COMPLETATA.isCorrectStato(entity)) {
				LOG.error("[" + CLASS_NAME + "::estraiDichiaranteUpdate] Funzione applicabile solo per istanza INVIATA. adesso idStato: " + entity.getIdStatoWf());
				throw new BusinessException("Funzione estraiDichiaranteUpdate applicabile solo per istanza INVIATA","MOONSRV-10013");
			}
			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
			StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(), entity.getIdVersioneModulo());
			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModuloFilter(moduloE.getIdModulo(), ModuloAttributoFilter.EXTRAI_DICHIARANTE);
			MapModuloAttributi mapModuloAttributi = new MapModuloAttributi(attributiModuloE);
			Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE/*, mapModuloAttributi*/);

        	// Estrai Dichiarante
			String confEstraiDichiarante = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_ESTRAI_DICHIARANTE);
			if (StringUtils.isNotEmpty(confEstraiDichiarante)) {
				LOG.info("[" + CLASS_NAME + "::estraiDichiaranteUpdate] new EstraiDichiaranteTask().call() ...");
				String extractDichiaranteResponse = new EstraiDichiaranteTask(istanza, confEstraiDichiarante).call();
				LOG.info("[" + CLASS_NAME + "::estraiDichiaranteUpdate] new EstraiDichiaranteTask().call() : " + extractDichiaranteResponse);
			} else {
				LOG.error("[" + CLASS_NAME + "::estraiDichiaranteUpdate] attributo PSIT_EXTRAI_DICHIARANTE mancante per il modulo " + moduloE.getIdModulo() + " " + moduloE.getCodiceModulo());
			}
			String response = null;

			LOG.debug("[" + CLASS_NAME + "::estraiDichiaranteUpdate] NOT IMPLEMENTED response = " + response);
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::estraiDichiaranteUpdate] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		}
	}

	@Override
	public String downloadAllFileIstanzeByIdModulo(Long idModulo, Date dataDal, Date dataAl) throws BusinessException {
        int nIstanzeWrite = 0;
        String logFile = "";
        String eccezioniIstanze="";
		try {
			LOG.info("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] BEGIN");
			
			IstanzeFilter filter = new IstanzeFilter();
			filter.setNotStatiFo(List.of(DecodificaStatoIstanza.BOZZA.getIdStatoWf(),
					DecodificaStatoIstanza.ELIMINATA.getIdStatoWf(),DecodificaStatoIstanza.COMPLETATA.getIdStatoWf()));
			filter.setIdModulo(idModulo);
			filter.setCreatedStart(dataDal);
			filter.setCreatedEnd(dataAl);
			IstanzeSorter sorter = null;
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder("").build();
			List<Istanza> elenco = getElencoIstanze(filter, optSorter);
			
			String codiceModulo = moduloDAO.findById(idModulo).getCodiceModulo();									
			String path = EnvProperties.readFromFile(EnvProperties.TMP_PATH_FS);
			String archivio = path+File.separator+"moon_archivioIstanze_"+codiceModulo+".zip";

	        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(archivio)) ) {
		        byte[] bytes = null;
		        for (Istanza i : elenco) {
		        	//scrivo cartella e pdf istanza
		        	try {
		            	bytes = printIstanzeService.getPdfById(i.getIdIstanza());
		            } catch(BusinessException b) {
		            	eccezioniIstanze += i.getCodiceIstanza()+"\n";
		            	continue;
		            }
		        	String fileName = i.getCodiceIstanza()+File.separator+i.getCodiceIstanza()+".pdf";
		        	File fileWithRelativePath = new File(fileName);
		        	ZipEntry zipEntry = new ZipEntry(fileName);
		            zipOut.putNextEntry(zipEntry);
		            if(bytes != null) {
		            	zipOut.write(bytes);
		            	nIstanzeWrite++;
		            }
		           	bytes = null;
		           	//scrivo allegati istanza
		           	List<Allegato> allegati = allegatiService.findLazyByIdIstanza(i.getIdIstanza());
		           	for(Allegato a : allegati) {
		           		String filenameAllegato = i.getCodiceIstanza()+File.separator+i.getCodiceIstanza()+"_"+prune(i.getCodiceFiscaleDichiarante())+"_"+a.getFormioNameFile();
		           		fileWithRelativePath = new File(filenameAllegato);
			        	zipEntry = new ZipEntry(filenameAllegato);
			            zipOut.putNextEntry(zipEntry);
			            zipOut.write(allegatiService.getById(a.getIdAllegato()).getContenuto());
		           	}
		        }
		        //scrivo file di log
		        logFile = archivio+"\nNumero istanze totali: "+elenco.size()+"\nNumero istanze scaricate: "+nIstanzeWrite+"\nCodici istanze in errore: "+eccezioniIstanze;
		        zipOut.putNextEntry(new ZipEntry("log.txt"));
	            zipOut.write(logFile.getBytes());
		    	zipOut.closeEntry();
		        zipOut.close();
	        }
			LOG.debug("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] END");
	        return "Archive Generated: " + logFile;
		} catch (FileNotFoundException fn) {
			LOG.error("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] Errore FileNotFoundException"+ fn);
			throw new BusinessException("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] Errore FileNotFoundException");
		} catch (IOException io) {
			LOG.error("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] Errore IOException"+ io);
			throw new BusinessException("[" + CLASS_NAME + "::downloadAllFileIstanzeByIdModulo] Errore IOException");
		}
	}

	@Override
	public String downloadRepositoryFileByIdModulo(Long idModulo, Date dataDal, Date dataAl) {
	        String logFile = "";
	        String eccezioniIstanze="";
			try {
				LOG.info("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] BEGIN");
				
				IstanzeFilter filter = new IstanzeFilter();
				filter.setNotStatiFo(List.of(DecodificaStatoIstanza.BOZZA.getIdStatoWf(),
						DecodificaStatoIstanza.ELIMINATA.getIdStatoWf(),DecodificaStatoIstanza.COMPLETATA.getIdStatoWf()));
				filter.setIdModulo(idModulo);
				filter.setCreatedStart(dataDal);
				filter.setCreatedEnd(dataAl);
				IstanzeSorter sorter = null;
				Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder("").build();
				List<Istanza> elenco = getElencoIstanze(filter, optSorter);
				
				String codiceModulo = moduloDAO.findById(idModulo).getCodiceModulo();									
				String path = EnvProperties.readFromFile(EnvProperties.TMP_PATH_FS);
				String archivio = path+File.separator+"moon_archivioRepositoryFile_"+codiceModulo+".zip";

		        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(archivio)) ) {
			        byte[] bytes = null;
			        for (Istanza i : elenco) {
			        	RepositoryFileFilter repoFilter = new RepositoryFileFilter();
						repoFilter.setIdIstanza(i.getIdIstanza());
			        	List<RepositoryFile> repoFiles = repositoryFileService.getElencoRepositoryFile(repoFilter,null);

			        	//scrivo cartella e repo file per istanza
			        	for(RepositoryFile rf : repoFiles) {
			        		try{
			        			bytes = null;
			        			bytes = repositoryFileService.getContenutoRepositoryFile(rf.getIdFile());
			        			String fileName = i.getCodiceIstanza()+File.separator+rf.getFormioNameFile();
					        	File fileWithRelativePath = new File(fileName);
					        	ZipEntry zipEntry = new ZipEntry(fileName);
					            zipOut.putNextEntry(zipEntry);
					            if(bytes != null) {
					            	zipOut.write(bytes);
					            }
			        		} catch(BusinessException b) {
				            	eccezioniIstanze += i.getCodiceIstanza()+" idFile="+rf.getIdFile()+"\n";
				            	continue;
				            }
			        	}		        	
			        }
			        //scrivo file di log
			        logFile = archivio+"\nIstanze in errore: "+eccezioniIstanze;
			        zipOut.putNextEntry(new ZipEntry("log.txt"));
		            zipOut.write(logFile.getBytes());
			    	zipOut.closeEntry();
			        zipOut.close();
		        }
				LOG.debug("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] END");
		        return "Archive Generated: " + logFile;
			} catch (FileNotFoundException fn) {
				LOG.error("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] Errore FileNotFoundException"+ fn);
				throw new BusinessException("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] Errore FileNotFoundException");
			} catch (IOException io) {
				LOG.error("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] Errore IOException"+ io);
				throw new BusinessException("[" + CLASS_NAME + "::downloadRepositoryFileByIdModulo] Errore IOException");
			}
	}

	@Override
	public String downloadRepositoryFileByIdIstanza(Long idIstanza) {
		int nFileWrite = 0;
        String logFile = "";
        String eccezioniFile="";
        List<RepositoryFile> repoFiles = new ArrayList<RepositoryFile>();
		try {
			LOG.info("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] BEGIN");
			
			IstanzeFilter filter = new IstanzeFilter();
			filter.setNotStatiFo(List.of(DecodificaStatoIstanza.BOZZA.getIdStatoWf(),
					DecodificaStatoIstanza.ELIMINATA.getIdStatoWf(),DecodificaStatoIstanza.COMPLETATA.getIdStatoWf()));
			filter.setIdIstanza(idIstanza);
			IstanzeSorter sorter = null;
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder("").build();
			List<Istanza> elenco = getElencoIstanze(filter, optSorter);
			String codIstanza = "";
			if(!elenco.isEmpty()) {
				codIstanza = elenco.get(0).getCodiceIstanza();
			}									
			String path = EnvProperties.readFromFile(EnvProperties.TMP_PATH_FS);
			String archivio = path+File.separator+"moon_repositoryFile_istanza_"+codIstanza+".zip";

	        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(archivio)) ) {
		        byte[] bytes = null;
		        for (Istanza i : elenco) {
		        	RepositoryFileFilter repoFilter = new RepositoryFileFilter();
					repoFilter.setIdIstanza(i.getIdIstanza());
		        	repoFiles = repositoryFileService.getElencoRepositoryFile(repoFilter,null);

		        	//scrivo cartella e repo file per istanza
		        	for(RepositoryFile rf : repoFiles) {
		        		try{
		        			bytes = null;
		        			bytes = repositoryFileService.getContenutoRepositoryFile(rf.getIdFile());
		        			String fileName = i.getCodiceIstanza()+File.separator+rf.getFormioNameFile();
				        	File fileWithRelativePath = new File(fileName);
				        	ZipEntry zipEntry = new ZipEntry(fileName);
				            zipOut.putNextEntry(zipEntry);
				            if(bytes != null) {
				            	zipOut.write(bytes);
				            	nFileWrite++;
				            }
		        		} catch(BusinessException b) {
			            	eccezioniFile += " idFile="+rf.getIdFile()+"\n";
			            	continue;
			            }
		        	}		        	
		        }
		        //scrivo file di log
		        logFile = archivio+"\nNumero file totali: "+repoFiles.size()+"\nNumero file scaricati: "+nFileWrite+"\nFile in errore: "+eccezioniFile;
		        zipOut.putNextEntry(new ZipEntry("log.txt"));
	            zipOut.write(logFile.getBytes());
		    	zipOut.closeEntry();
		        zipOut.close();
	        }
			LOG.debug("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] END");
	        return "Archive Generated: " + logFile;
		} catch (FileNotFoundException fn) {
			LOG.error("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] Errore FileNotFoundException"+ fn);
			throw new BusinessException("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] Errore FileNotFoundException");
		} catch (IOException io) {
			LOG.error("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] Errore IOException"+ io);
			throw new BusinessException("[" + CLASS_NAME + "::downloadRepositoryFileByIdIstanza] Errore IOException");
		}
	}
	
	private String prune(String codiceFiscaleDichiarante) {
		if(codiceFiscaleDichiarante == null) return "";
		codiceFiscaleDichiarante = codiceFiscaleDichiarante.replaceAll("[^a-zA-Z0-9]","");
		return codiceFiscaleDichiarante;
	}

	@Override
	public ResponseOperazioneMassiva generaHashUnivocitaMassivo(Long idTag) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::generaHashUnivocitaMassivo] IN idTag = " + idTag);
			}
			ResponseOperazioneMassiva resp = new ResponseOperazioneMassiva();
			resp.setOperation("GENERA_HASH_UNIVOCITA");
			resp.setIdTag(idTag);
			resp.setStarted(new Date());
			//
			IstanzeFilter filter = new IstanzeFilter();
			filter.setIdTag(idTag);
			List<IstanzaEntity> istanze = istanzaDAO.find(filter, Optional.empty());
			LOG.info("[" + CLASS_NAME + "::generaHashUnivocitaMassivo] istanze.size()=" + istanze!=null?istanze.size():"null");
			int total = istanze!=null?istanze.size():0;
			int ok = 0;
			int ko = 0;
			for (IstanzaEntity i : istanze) {
				try {
					generaHashUnivocita(getIstanzaById(i.getIdIstanza()));
					ok++;
				} catch (Exception e) {
					ko++;
					tagIstanzaDAO.updateEsito(idTag, i.getIdIstanza(), "ERR");
					LOG.warn("[" + CLASS_NAME + "::generaHashUnivocitaMassivo] Exception for istanza=" + i.getIdIstanza() + " - " + e.getMessage());
				}
			}
			LOG.info("[" + CLASS_NAME + "::generaHashUnivocitaMassivo] istanze.size()=" + (istanze!=null?istanze.size():"null") 
				+ "   OK=" + ok + "   KO=" + ko);
			resp.setTotal(total);
			resp.setOk(ok);
			resp.setKo(ko);
			resp.setStatus(total==ok?"SUCCESS":"WARNING");
			resp.setEnded(new Date());
			return resp;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::generaHashUnivocitaMassivo] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		}
	}
	
	private void generaHashUnivocita(Istanza istanza) throws BusinessException {
		try {
			String hash = calcHash(getAK(istanza));
			int numRecord = istanzaDAO.updateHashUnivocita(istanza.getIdIstanza(), hash);
			LOG.debug("[" + CLASS_NAME + "::generaHashUnivocita] IN idIstanza = " + istanza.getIdIstanza() + " - record aggiornati = "+ numRecord);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::generaHashUnivocita] ERROR " + be.getMessage());
			throw be;
		}
	}

	private String calcHash(String ak) throws BusinessException {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] sha256 = digest.digest(ak.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(sha256);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("[" + CLASS_NAME + "::calcHash] NoSuchAlgorithmException SHA-256");
			throw new BusinessException("calcHash NoSuchAlgorithmException");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::calcHash] Exception " + e.getMessage(), e);
			throw new BusinessException("calcHash Exception");
		}
	}
	
	private String getAK(Istanza istanza) throws BusinessException {
		
		List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(istanza.getModulo().getIdModulo());
		MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
		String hashUnivocitaFields = attributi.getWithCorrectType(ModuloAttributoKeys.HASH_UNIVOCITA_FIELDS);
		String[] fields;
		if (hashUnivocitaFields.contains("|"))
			fields = hashUnivocitaFields.split("\\|");
		else
			fields = new String[]{ hashUnivocitaFields };
		List<String> results = new ArrayList<>();
		DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
		datiIstanzaHelper.initDataNode(istanza);
		for (int i = 0 ; i < fields.length ; i++) {
			String field = fields[i].trim();
    		switch(field) {
    			case "" : 
    				results.add("*");
    				break;
	    		case "@@CODICE_FISCALE@@" : 
	    			results.add(istanza.getCodiceFiscaleDichiarante());
    				break;
	    		default :
	    			results.add(datiIstanzaHelper.extractedTextValueFromDataNodeByKey(field));
    		}
		}
		String result = String.join("|", results);
		return result;
	}

	@Override
	public AllegatiSummary getAllegatiSummary(Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getAllegatiSummary] IN idIstanza: " + idIstanza);
			return allegatiService.getAllegatiSummary(idIstanza);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getAllegatiSummary] BusinessException ");
			throw be;
		}
	}


}

