/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.moon.moonbobl.business.service.IstanzeService;
import it.csi.moon.moonbobl.business.service.ModuliService;
import it.csi.moon.moonbobl.business.service.impl.dao.AzioneDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.CodiceIstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.EpayNotificaPagamentoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaPdfDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.EpayRichiestaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaPdfEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaSaveResponse;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeSorter;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloAttributoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaBoHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.task.AssociaAllegatiAdIstanzaTask;
import it.csi.moon.moonbobl.business.service.impl.helper.task.SendEmailDichiaranteIstanzaTask;
import it.csi.moon.moonbobl.business.service.impl.istanza.IstanzaServiceDelegate;
import it.csi.moon.moonbobl.business.service.impl.istanza.IstanzaServiceDelegateFactory;
import it.csi.moon.moonbobl.business.service.mapper.IstanzaMapper;
import it.csi.moon.moonbobl.business.service.mapper.PagamentoMapper;
import it.csi.moon.moonbobl.business.service.mapper.PagamentoNotificaMapper;
import it.csi.moon.moonbobl.business.service.mapper.StatoMapper;
import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAggiuntiviHeaders;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaMoonsrv;
import it.csi.moon.moonbobl.dto.moonfobl.Pagamento;
import it.csi.moon.moonbobl.dto.moonfobl.ResponsePaginated;
import it.csi.moon.moonbobl.dto.moonfobl.Stato;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonbobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.IstanzaUtils;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.MapModuloAttributi;
import it.csi.moon.moonbobl.util.ModuloAttributoKeys;
import it.csi.moon.moonbobl.util.decodifica.DecodificaAzione;
import it.csi.moon.moonbobl.util.decodifica.DecodificaRuolo;
import it.csi.moon.moonbobl.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.moonbobl.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoModificaDati;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoUtente;

/**
 * @author Laurent Layer di logica servizi che richiama i DAO
 */
@Component
public class IstanzeServiceImpl implements IstanzeService {

	private final static String CLASS_NAME = "IstanzeServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	@Autowired
	ModuliService moduliService;
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	StatoDAO statoDAO;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	ModuloProgressivoDAO moduloProgressivoDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	@Autowired
	CodiceIstanzaDAO codiceIstanzaDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	IstanzaPdfDAO istanzaPdfDAO;
	@Autowired
	WorkflowDAO workflowDAO;
	@Autowired
	AzioneDAO azioneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	EpayRichiestaDAO epayRichiestaDAO;
	@Autowired
	EpayNotificaPagamentoDAO epayNotificaPagamentoDAO;
	
	@Override
	public Istanza getIstanzaById(UserInfo user, Long idIstanza) throws ItemNotFoundBusinessException, BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::getIstanzaById] IN idIstanza: " + idIstanza);
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);

			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
			StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
					entity.getIdVersioneModulo());

			Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE);
			istanza = completaIstanzaEpay(istanza);
			return istanza;

		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		}
	}

	private Istanza completaIstanzaEpay(Istanza istanza) {
		try {
			List<EpayRichiestaEntity> richiesteE = epayRichiestaDAO.findByIdIstanza(istanza.getIdIstanza());
			if (richiesteE != null && richiesteE.size()>0) {
				EpayRichiestaEntity richiesta = richiesteE.get(richiesteE.size()-1);
				istanza.setDataEsitoPagamento(richiesta.getDataNotificaPagamento());
				istanza.setPagamenti(richiesteE.stream()
					.map( r -> {
						Pagamento res = PagamentoMapper.buildFromEpayRichiestaEntity(r);
						return completaPagamentoNotifica(res, r.getIdNotificaPagamento());
					})
					.collect(Collectors.toList()));
			}
		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::completaIstanzaEpay] Exception but continue istanza:" + istanza.getIdIstanza(), e);
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
	public Istanza getIstanzaCompletaById(UserInfo user, Long idIstanza, String nomePortale) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::getIstanzaCompletaById] IN idIstanza: " + idIstanza);
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);

			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
			String jsonDati = datiE.getDatiIstanza();
			try {
				/*
				 * moodifico il contesto nei path delle url degli allegati per poterli scaricare
				 * da backoffice
				 */
				// jsonDati = modificaContesto(jsonDati, entity.getIdModulo(), nomePortale);

				jsonDati = IstanzaUtils.modificaContestoFoToBo(jsonDati);

			} catch (Exception e) {
				log.error("[" + CLASS_NAME + "::getIstanzaCompletaById] Errore in cambio contesto in istanza " + idIstanza);
			}
			datiE.setDatiIstanza(jsonDati);

			StoricoWorkflowEntity storico = storicoWorkflowDAO.findLastStorico(idIstanza)
					.orElseThrow(ItemNotFoundBusinessException::new);
			StatoEntity statoE = statoDAO.findById(storico.getIdStatoWfArrivo());
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
					entity.getIdVersioneModulo());

			// se l'istanza non e' in bozza cerco la data invio
			Date dataInvio = null;
			if (entity.getIdStatoWf().intValue() != 1 && entity.getIdStatoWf().intValue() != 10) {
				IstanzaCronologiaStatiEntity datiInvio = istanzaDAO.findInvio(entity.getIdIstanza());
				if (datiInvio != null) {
					dataInvio = datiInvio.getDataInizio();
				}
			}
			Boolean flagMatadati = false;
			String nomeComune = "";
			if (flagMatadati) {
				DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
				try {
					nomeComune = datiIstanzaBoHelper.getNomeComune(datiE.getDatiIstanza());
				} catch (Exception e) {
					log.error("[" + CLASS_NAME + "::getIstanzaCompletaById] Errore lettura comune - ", e);
					throw new BusinessException("Errore ricerca istanza per id");
				}
			}

			Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE, nomeComune,
					dataInvio);
			istanza = completaIstanzaEpay(istanza);
			return istanza;

		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getIstanzaCompletaById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getIstanzaCompletaById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		}
	}

	@Override
	public Istanza getIstanzaBozzaById(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::getIstanzaById] IN idIstanza: " + idIstanza);
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);

			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());

			String jsonDati = datiE.getDatiIstanza();
			jsonDati = IstanzaUtils.modificaContestoFoToBo(jsonDati);
			datiE.setDatiIstanza(jsonDati);

			StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
					entity.getIdVersioneModulo());

			Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE);

			return istanza;

		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		}
	}

	private void validateUserAccess(UserInfo user, IstanzaEntity entity) throws BusinessException {
		boolean validate = user.getIdentificativoUtente().equals(entity.getCodiceFiscaleDichiarante());
		// eliminato controllo dichiarante diverso da user
	}

	@Override
	public List<Istanza> getElencoIstanze(Integer stato, IstanzeFilter filter, Optional<IstanzeSorter> sorter,
			String filtroRicercaDati) throws BusinessException {
		List<Istanza> elencoIstanze = new ArrayList<Istanza>();
		try {
			List<IstanzaEntity> elenco = istanzaDAO.find(stato, filter, sorter, filtroRicercaDati);
			if (elenco != null && elenco.size() > 0) {
				for (IstanzaEntity entity : elenco) {

					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());

					IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(entity.getIdIstanza());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
							entity.getIdVersioneModulo());

					IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());

					// se l'istanza non e' in bozza cerco la data invio
					Date dataInvio = null;
					if (entity.getIdStatoWf().intValue() != 1 && entity.getIdStatoWf().intValue() != 10) {
						IstanzaCronologiaStatiEntity datiInvio = istanzaDAO.findInvio(entity.getIdIstanza());
						if (datiInvio != null) {
							dataInvio = datiInvio.getDataInizio();
						}
					}

					String nomeComune = "";
					if (moduloE.getCodiceModulo().startsWith("RP_RSI")) {
						DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();

						try {
							nomeComune = datiIstanzaBoHelper.getNomeComune(datiE.getDatiIstanza());
						} catch (Exception e) {
							log.error("[" + CLASS_NAME + "::getElencoIstanze] RP_RSI Comune non trovato ");
						}
					}
					// elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity,statoE,moduloE,
					// nomeComune, dataInvio));

					elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE,
							nomeComune, dataInvio));

				}
			}
			return elencoIstanze;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco istanza");
		}
	}

	@Override
	public ResponsePaginated<Istanza> getElencoIstanzePaginate(Integer stato, IstanzeFilter filter,
			Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException {
		List<Istanza> elencoIstanze = new ArrayList<Istanza>();
		try {
			Integer totalElements = istanzaDAO.count(stato, filter, filtroRicercaDati);
			List<IstanzaEntity> elenco = istanzaDAO.find(stato, filter, sorter, filtroRicercaDati);
			if (elenco != null && elenco.size() > 0) {
				for (IstanzaEntity entity : elenco) {
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
							entity.getIdVersioneModulo());
					IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());
					// se l'istanza non e' in bozza cerco la data invio
					Date dataInvio = null;
					if (entity.getIdStatoWf().intValue() != 1 && entity.getIdStatoWf().intValue() != 10) {
						IstanzaCronologiaStatiEntity datiInvio = istanzaDAO.findInvio(entity.getIdIstanza());
						if (datiInvio != null) {
							dataInvio = datiInvio.getDataInizio();
						}
					}
					Boolean showExtraColumn = true;
					String nomeComune = "";
					String nomeEnte = "";

					if (showExtraColumn) {
						DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
						try {
							nomeComune = datiIstanzaBoHelper.getStringValueByKey(datiE.getDatiIstanza(), "comune.nome");
						} catch (Exception e) {
							log.debug(
									"[" + CLASS_NAME + "::getElencoIstanzePaginate] comune non trovato: " + nomeComune);
						}
						try {
							//nomeEnte = datiIstanzaBoHelper.getNomeEnte(datiE.getDatiIstanza());
							nomeEnte = datiIstanzaBoHelper.getNomeEnte(filtroRicercaDati,datiE.getDatiIstanza());
						} catch (Exception e) {
							log.debug("[" + CLASS_NAME + "::getElencoIstanzePaginate] ente non trovato: " + nomeEnte);
						}
					}
					
					Boolean isPagato = null;
					if (filter.getFiltroEpay() != null) {							
						isPagato = getIsPagato(filter.getFiltroEpay(),entity.getIdIstanza());
					}
								
					if (!StringUtils.isEmpty(nomeEnte)) {
						elencoIstanze.add(
								IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeEnte, dataInvio, isPagato));
					} else {
						elencoIstanze.add(
								IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeComune, dataInvio, isPagato));
					}
				}
			}
			ResponsePaginated<Istanza> result = new ResponsePaginated<Istanza>();
			result.setItems(elencoIstanze);
			result.setPage(filter.getOffset() % filter.getLimit()); // 0 based
			result.setPageSize(filter.getLimit());
			result.setTotalElements(totalElements);
			result.setTotalPages(
					((Double) Math.ceil(totalElements.doubleValue() / filter.getLimit().doubleValue())).intValue());
			return result;

		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco istanza");
		}
	}

	@Override
	public ResponsePaginated<Istanza> getElencoArchivioIstanzePaginate(IstanzeFilter filter,
			Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException {
		List<Istanza> elencoIstanze = new ArrayList<Istanza>();
		try {
			Integer totalElements = istanzaDAO.countArchivio(filter, filtroRicercaDati);
			List<IstanzaEntity> elenco = istanzaDAO.findArchivio(filter, sorter, filtroRicercaDati);
			if (elenco != null && elenco.size() > 0) {
				for (IstanzaEntity entity : elenco) {
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWfArrivo());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
							entity.getIdVersioneModulo());
					IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());
					// se l'istanza non e' in bozza cerco la data invio

					String operatore = "";
					try {
						UtenteEntity user = storicoWorkflowDAO.findOperatore(entity.getIdIstanza());
						operatore = generaNomeBreveOperatore(user);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error("[" + CLASS_NAME
								+ "::getElencoIstanzeDaCompletarePaginate] Errore lettura operatore - ", e);
					}

					Date dataInvio = null;
					if (entity.getIdStatoWf().intValue() != 1 && entity.getIdStatoWf().intValue() != 10) {
						try {
							IstanzaCronologiaStatiEntity datiInvio = istanzaDAO.findInvio(entity.getIdIstanza());
							if (datiInvio != null) {
								dataInvio = datiInvio.getDataInizio();
							}
						} catch (Exception e) {
							log.error(
									"[" + CLASS_NAME
											+ "::getElencoArchivioIstanzePaginate] Errore invocazione DAO - findInvio",
									e);
						}
					}
					Boolean showExtraColumn = true;
					/*
					 * ricerco se è impostato un filtro su un campo comune o ente, in questo caso
					 * bisogna acquisire quale chiave deve essere letta e quindi estrarre il dato e
					 * mostrarlo
					 */
					// ricercaParametriModulo;

					String nomeComune = "";
					String nomeEnte = "";

					if (showExtraColumn) {
						DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
						try {
							// nomeComune = datiIstanzaBoHelper.getStringValueByKey(datiE.getDatiIstanza(),
							// "sceltaComune.comune.nome");
							nomeComune = datiIstanzaBoHelper.getStringValueByKey(datiE.getDatiIstanza(), "comune.nome");
						} catch (Exception e) {
							log.debug("[" + CLASS_NAME + "::getElencoArchivioIstanzePaginate] comune non trovato: "
									+ nomeComune);
						}
						try {
							//nomeEnte = datiIstanzaBoHelper.getNomeEnte(datiE.getDatiIstanza());
							nomeEnte = datiIstanzaBoHelper.getNomeEnte(filtroRicercaDati,datiE.getDatiIstanza());
						} catch (Exception e) {
							log.debug("[" + CLASS_NAME + "::getElencoArchivioIstanzePaginate] ente non trovato: "
									+ nomeEnte);
						}
					}
					
					Boolean isPagato = null;
					if (filter.getFiltroEpay() != null) {							
						isPagato = getIsPagato(filter.getFiltroEpay(),entity.getIdIstanza());
					}
					
					if (!StringUtils.isEmpty(nomeEnte)) {
						elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeEnte,
								dataInvio, operatore, isPagato));
					} else {
						elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeComune,
								dataInvio, operatore, isPagato));
					}

				}
			}
			ResponsePaginated<Istanza> result = new ResponsePaginated<Istanza>();
			result.setItems(elencoIstanze);
			result.setPage(filter.getOffset() % filter.getLimit()); // 0 based
			result.setPageSize(filter.getLimit());
			result.setTotalElements(totalElements);
			result.setTotalPages(
					((Double) Math.ceil(totalElements.doubleValue() / filter.getLimit().doubleValue())).intValue());
			return result;

		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoArchivioIstanzePaginate] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco istanza");
		}
	}

	@Override
	public List<Istanza> getElencoIstanzeInLavorazione(IstanzeFilter filter, Optional<IstanzeSorter> sorter,
			String filtroRicercaDati) throws BusinessException {
		List<Istanza> elencoIstanze = new ArrayList<Istanza>();
		try {
			List<IstanzaEntity> elenco = istanzaDAO.findInLavorazione(filter, sorter, filtroRicercaDati);
			if (elenco != null && elenco.size() > 0) {
				for (IstanzaEntity entity : elenco) {
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWfArrivo());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
							entity.getIdVersioneModulo());
					IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());

					// se l'istanza non e' in bozza cerco la data invio
					Date dataInvio = null;
					if (entity.getIdStatoWf().intValue() != 1 && entity.getIdStatoWf().intValue() != 10) {
						IstanzaCronologiaStatiEntity datiInvio = istanzaDAO.findInvio(entity.getIdIstanza());
						if (datiInvio != null) {
							dataInvio = datiInvio.getDataInizio();
						}
					}

					String nomeComune = "";
					String nomeEnte = "";
					DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
					if (moduloE.getCodiceModulo().startsWith("RP_RSI")) {

						try {
							// nomeComune = datiIstanzaHelper.getNomeComune(datiE.getDatiIstanza());
							nomeComune = datiIstanzaBoHelper.getStringValueByKey(datiE.getDatiIstanza(), "comune.nome");

						} catch (Exception e) {
							// TODO Auto-generated catch block
							log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore lettura comune - ", e);
						}
					} else {

						try {
							//nomeEnte = datiIstanzaBoHelper.getNomeEnte(datiE.getDatiIstanza());
							nomeEnte = datiIstanzaBoHelper.getNomeEnte(filtroRicercaDati,datiE.getDatiIstanza());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore lettura ente - ", e);
						}

					}

					if (!StringUtils.isEmpty(nomeEnte)) {
						elencoIstanze.add(
								IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeEnte, dataInvio));
					} else {
						elencoIstanze.add(
								IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeComune, dataInvio));
					}
				}
			}
			return elencoIstanze;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco istanza");
		}
	}

	@Override
	public ResponsePaginated<Istanza> getElencoIstanzeInLavorazionePaginate(IstanzeFilter filter,
			Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException {

		Integer totalElements = istanzaDAO.countInLavorazione(filter, filtroRicercaDati);

		List<Istanza> elencoIstanze = new ArrayList<Istanza>();
		try {
			List<IstanzaEntity> elenco = istanzaDAO.findInLavorazione(filter, sorter, filtroRicercaDati);
			if (elenco != null && elenco.size() > 0) {
				for (IstanzaEntity entity : elenco) {
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWfArrivo());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
							entity.getIdVersioneModulo());
					IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());

					// se l'istanza non e' in bozza cerco la data invio
					Date dataInvio = null;
					if (entity.getIdStatoWf().intValue() != 1 && entity.getIdStatoWf().intValue() != 10) {
						IstanzaCronologiaStatiEntity datiInvio = istanzaDAO.findInvio(entity.getIdIstanza());
						if (datiInvio != null) {
							dataInvio = datiInvio.getDataInizio();
						}
					}

					String operatore = "";
					try {
						UtenteEntity user = storicoWorkflowDAO.findOperatore(entity.getIdIstanza());
						operatore = generaNomeBreveOperatore(user);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore lettura operatore - ", e);
					}

					String nomeComune = "";
					String nomeEnte = "";
					DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();

					try {
						nomeComune = datiIstanzaBoHelper.getStringValueByKey(datiE.getDatiIstanza(), "comune.nome");
					} catch (Exception e) {
						log.debug("[" + CLASS_NAME + "::getElencoIstanzeInLavorazionePaginate] comune non trovato: "
								+ nomeComune);
					}
					try {
						//nomeEnte = datiIstanzaBoHelper.getNomeEnte(datiE.getDatiIstanza());
						nomeEnte = datiIstanzaBoHelper.getNomeEnte(filtroRicercaDati,datiE.getDatiIstanza());
					} catch (Exception e) {
						log.debug("[" + CLASS_NAME + "::getElencoIstanzeInLavorazionePaginate] ente non trovato: "
								+ nomeEnte);
					}
					
//					Boolean isPagato = null;
//					if (filter.getFiltroEpay() != null) {						
//						isPagato = filter.getFiltroEpay().equals(Constants.FILTRO_EPAY_PAID) ? true : false;
//					}
					
					Boolean isPagato = null;
					if (filter.getFiltroEpay() != null) {							
						isPagato = getIsPagato(filter.getFiltroEpay(),entity.getIdIstanza());
					}
										

					if (!StringUtils.isEmpty(nomeEnte)) {
						elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeEnte,
								dataInvio, operatore, isPagato));
					} else {
						elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeComune,
								dataInvio, operatore, isPagato));
					}
				}
			}

			ResponsePaginated<Istanza> result = new ResponsePaginated<Istanza>();
			result.setItems(elencoIstanze);
			result.setPage(filter.getOffset() % filter.getLimit()); // 0 based
			result.setPageSize(filter.getLimit());
			result.setTotalElements(totalElements);
			result.setTotalPages(
					((Double) Math.ceil(totalElements.doubleValue() / filter.getLimit().doubleValue())).intValue());
			return result;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco istanza");
		}
	}

	@Override
	public ResponsePaginated<Istanza> getElencoIstanzeInBozzaPaginate(IstanzeFilter filter,
			Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException {

		Integer totalElements = istanzaDAO.countInBozza(filter, filtroRicercaDati);

		List<Istanza> elencoIstanze = new ArrayList<Istanza>();
		try {
			List<IstanzaEntity> elenco = istanzaDAO.findInBozza(filter, sorter, filtroRicercaDati);
			if (elenco != null && elenco.size() > 0) {
				for (IstanzaEntity entity : elenco) {
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
							entity.getIdVersioneModulo());
					IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());

					// se l'istanza e' in bozza la data invio è nulla
					Date dataInvio = null;

					String nomeComune = "";
					String nomeEnte = "";
					DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();

					try {
						nomeComune = datiIstanzaBoHelper.getStringValueByKey(datiE.getDatiIstanza(), "comune.nome");
					} catch (Exception e) {
						log.debug("[" + CLASS_NAME + "::getElencoIstanzeInBozzaPaginate] comune non trovato: "
								+ nomeComune);
					}
					try {
						//nomeEnte = datiIstanzaBoHelper.getNomeEnte(datiE.getDatiIstanza());
						nomeEnte = datiIstanzaBoHelper.getNomeEnte(filtroRicercaDati,datiE.getDatiIstanza());
					} catch (Exception e) {
						log.debug(
								"[" + CLASS_NAME + "::getElencoIstanzeInBozzaPaginate] ente non trovato: " + nomeEnte);
					}

					if (!StringUtils.isEmpty(nomeEnte)) {
						elencoIstanze.add(
								IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeEnte, dataInvio));
					} else {
						elencoIstanze.add(
								IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeComune, dataInvio));
					}
				}
			}

			ResponsePaginated<Istanza> result = new ResponsePaginated<Istanza>();
			result.setItems(elencoIstanze);
			result.setPage(filter.getOffset() % filter.getLimit()); // 0 based
			result.setPageSize(filter.getLimit());
			result.setTotalElements(totalElements);
			result.setTotalPages(
					((Double) Math.ceil(totalElements.doubleValue() / filter.getLimit().doubleValue())).intValue());
			return result;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco istanza");
		}
	}

	@Override
	public Istanza getInitIstanza(UserInfo user, Long idModulo, Long idVersioneModulo, IstanzaInitBLParams params,
			HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::getInitIstanza] IN idModulo: " + idModulo);
			log.debug("[" + CLASS_NAME + "::getInitIstanza] IN idVersioneModulo: " + idVersioneModulo);
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(idModulo, idVersioneModulo);
			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());

			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
			IstanzaServiceDelegate istanzaServiceDelegate = new IstanzaServiceDelegateFactory()
					.getDelegate(moduloE.getCodiceModulo());
			return istanzaServiceDelegate.getInitIstanza(user, moduloE, attributi, params, httpRequest);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		}
	}

	@Override
	@Transactional
	public Istanza saveIstanza(UserInfo user, Istanza istanza) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::saveIstanza] IN istanza: " + istanza);
			validateForSaving(user, istanza);

			boolean insertMode = true;
			Date now = new Date();
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(istanza.getModulo().getIdModulo(),
					istanza.getModulo().getIdVersioneModulo());

			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
			MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);

			if (!moduliService.verificaAbilitazioneModulo(moduloE.getIdModulo(), user)) {
				throw new BusinessException("Utente non abilitato ad operare sul modulo indicato");
			}

			/*
			 * discriminazione degli stati di insertMode/submitMode/editMode attraverso lo
			 * stato di input passato: BOZZA/INVIATO
			 */

			IstanzaEntity eIstanza = null;
			Integer lastStatoWf = null;
			if (istanza.getIdIstanza() != null) {
				eIstanza = istanzaDAO.findById(istanza.getIdIstanza());
				lastStatoWf = eIstanza.getIdStatoWf(); // stato attuale dell'istanza
				insertMode = false;
				validateForModifying(user, istanza, eIstanza);
			} else {
				eIstanza = new IstanzaEntity();
			}

			Integer statoWf = eIstanza.getIdStatoWf();
			Boolean confAutModifica = attributi.getWithCorrectType(ModuloAttributoKeys.MODIFICA_ISTANZA_INVIATA);
			if (confAutModifica != null && !confAutModifica && statoWf.intValue() > 2) {
				throw new UnauthorizedBusinessException(
						"Il modulo non è autorizzato per la modifica di istanze inviate");
			}

			// identifico lo stato passato in input 1-BOZZA 2-INVIATA
			DecodificaStatoIstanza dStato = DecodificaStatoIstanza.byIdStatoWf(istanza.getStato().getIdStato());
			StatoEntity statoE = dStato.getStatoEntity();
			boolean submitMode = DecodificaStatoIstanza.INVIATA.equals(dStato);

			if (submitMode && istanza.getIdIstanza() != null) {
				validateForSubmit(user, eIstanza);
			}

			// ISTANZE
			eIstanza.setIdStatoWf(statoE.getIdStatoWf());
			if (StringUtils.isEmpty(eIstanza.getCodiceIstanza())) {
				String descTipoCodiceIstanza = codiceIstanzaDAO.findById(moduloE.getIdTipoCodiceIstanza());
				eIstanza.setCodiceIstanza(
						moduloProgressivoDAO.generateCodiceIstanzaForIdModulo(moduloE, descTipoCodiceIstanza));
			}

			// caso istanza modificata
			boolean editMode = false;
			if (!insertMode && !submitMode) {
				// DecodificaStatoIstanza dStatoAttuale =
				// DecodificaStatoIstanza.byIdStatoWf(lastStatoWf);
				// if (DecodificaStatoIstanza.INVIATO.equals(dStato)) {
				editMode = true;
				eIstanza.setIdStatoWf(lastStatoWf);
				// }
			}

			eIstanza.setFlagEliminata(istanza.getFlagEliminata() ? "S" : "N");
			eIstanza.setFlagArchiviata(istanza.getFlagArchiviata() ? "S" : "N");
			eIstanza.setFlagTest(istanza.getFlagTest() ? "S" : "N");
			eIstanza.setIdModulo(istanza.getModulo().getIdModulo());
//			if (insertMode) {
//				eIstanza.setDataCreazione(now);
//				eIstanza.setCodiceFiscaleDichiarante(user.getCodFisc());
//				eIstanza.setAttoreIns(user.getCodFisc());
//				Long idIstanza = istanzaDAO.insert(eIstanza);
//				eIstanza.setIdIstanza(idIstanza);
//			} else {
//				eIstanza.setAttoreUpd(user.getCodFisc());
//				istanzaDAO.update(eIstanza);
//			}

			// CRON
			IstanzaCronologiaStatiEntity eIstanzaCronologia = null;
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = null;
			if (!insertMode) {
				lastIstanzaCronologia = istanzaDAO.findLastCronologia(eIstanza.getIdIstanza());
			}
			if (insertMode || (lastIstanzaCronologia != null && submitMode)) {
				if (lastIstanzaCronologia != null) {
					lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
					lastIstanzaCronologia.setDataFine(now);
					istanzaDAO.updateCronologia(lastIstanzaCronologia);
				}
				eIstanzaCronologia = new IstanzaCronologiaStatiEntity();
				eIstanzaCronologia.setIdIstanza(eIstanza.getIdIstanza());
				eIstanzaCronologia.setIdStatoWf(lastStatoWf);
				eIstanzaCronologia.setAttoreIns(user.getIdentificativoUtente());
				eIstanzaCronologia.setDataInizio(now);
				if (lastStatoWf == null) {
					// primo salvataggio in bozza
					eIstanzaCronologia.setIdStatoWf(1);
					eIstanzaCronologia.setIdAzioneSvolta(DecodificaAzione.SALVA_BOZZA.getIdAzione());
				} else {
					// modifica dopo il primo salvataggio in bozza
					eIstanzaCronologia.setIdAzioneSvolta(DecodificaAzione.MODIFICA.getIdAzione());
				}
				Long idCronologiaStati = istanzaDAO.insertCronologia(eIstanzaCronologia);
				eIstanzaCronologia.setIdCronologiaStati(idCronologiaStati);
			} else if (editMode) {
				/*
				 * viene creata una nuova cronologia con lo stesso stato precedente
				 */
				if (lastIstanzaCronologia != null) {
					lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
					lastIstanzaCronologia.setDataFine(now);
					istanzaDAO.updateCronologia(lastIstanzaCronologia);
				}
				eIstanzaCronologia = new IstanzaCronologiaStatiEntity();
				eIstanzaCronologia.setIdIstanza(eIstanza.getIdIstanza());
				eIstanzaCronologia.setIdStatoWf(lastStatoWf);
				eIstanzaCronologia.setAttoreIns(user.getIdentificativoUtente());
				eIstanzaCronologia.setDataInizio(now);
				eIstanzaCronologia.setIdAzioneSvolta(DecodificaAzione.MODIFICA.getIdAzione());
				Long idCronologiaStati = istanzaDAO.insertCronologia(eIstanzaCronologia);
				eIstanzaCronologia.setIdCronologiaStati(idCronologiaStati);
			} else {
				if (lastIstanzaCronologia != null) {
					eIstanzaCronologia = lastIstanzaCronologia;
					eIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
					istanzaDAO.updateCronologia(eIstanzaCronologia);
				}
			}

			// DATI
			IstanzaDatiEntity eIstanzaDati = null;
			IstanzaDatiEntity lastIstanzaDati = null;
			if (!insertMode) {
				lastIstanzaDati = istanzaDAO.findDati(eIstanza.getIdIstanza(),
						lastIstanzaCronologia.getIdCronologiaStati());
			}
			if (insertMode || (lastIstanzaDati != null && submitMode)) {
//				if (lastIstanzaDati!=null) {
//					// NESSUN AZIONE
//					istanzaDAO.updateDati(lastIstanzaDati);
//				}
				eIstanzaDati = new IstanzaDatiEntity();
				eIstanzaDati.setIdIstanza(eIstanza.getIdIstanza());
				eIstanzaDati.setAttoreUpd(user.getIdentificativoUtente());
				eIstanzaDati.setDataUpd(now);
				eIstanzaDati.setDatiIstanza(IstanzaUtils.modificaContestoBoToFo(String.valueOf(istanza.getData())));
				eIstanzaDati.setIdCronologiaStati(eIstanzaCronologia.getIdCronologiaStati());
				eIstanzaDati.setIdStepCompilazione(null);
				eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.INI.getIdTipoModifica());
				Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
				eIstanzaDati.setIdDatiIstanza(idIstanzaDati);
			} else if (editMode) {
				/*
				 * viene creato un nuovo record dati
				 */
				eIstanzaDati = new IstanzaDatiEntity();
				eIstanzaDati.setIdIstanza(eIstanza.getIdIstanza());
				eIstanzaDati.setAttoreUpd(user.getIdentificativoUtente());
				eIstanzaDati.setDataUpd(now);
				eIstanzaDati.setDatiIstanza(IstanzaUtils.modificaContestoBoToFo(String.valueOf(istanza.getData())));
				eIstanzaDati.setIdCronologiaStati(eIstanzaCronologia.getIdCronologiaStati());
				eIstanzaDati.setIdStepCompilazione(null);
				eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.UPD.getIdTipoModifica());
				Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
				eIstanzaDati.setIdDatiIstanza(idIstanzaDati);
			} else {
				if (lastIstanzaDati != null) {
					eIstanzaDati = lastIstanzaDati;
					eIstanzaDati.setDataUpd(now);
					eIstanzaDati.setDatiIstanza(IstanzaUtils.modificaContestoBoToFo(String.valueOf(istanza.getData())));
					istanzaDAO.updateDati(eIstanzaDati);
				}
			}

			// STORICO WORKFLOW
			Long idProcesso = null;
			if (editMode || submitMode) {
				String descDestinatario = "";
				idProcesso = moduloDAO.findIdProcesso(istanza.getModulo().getIdModulo());
				if (lastStatoWf != 1 && lastStatoWf != 10) {
					log.info("[" + CLASS_NAME + "] istanza non in bozza: update e inserimento in storico wf "
							+ eIstanza.getIdIstanza());
					StoricoWorkflowEntity lastStorico = storicoWorkflowDAO.findLastStorico(eIstanza.getIdIstanza())
							.orElseThrow(ItemNotFoundBusinessException::new);
					StoricoWorkflowEntity eStoricoWf = new StoricoWorkflowEntity(null, eIstanza.getIdIstanza(),
							idProcesso, lastStorico.getIdStatoWfArrivo(), lastStorico.getIdStatoWfArrivo(),
							DecodificaAzione.MODIFICA.getIdAzione(), lastStorico.getNomeStatoWfArrivo(),
							lastStorico.getNomeStatoWfArrivo(), DecodificaAzione.MODIFICA.getNomeAzione(),
							descDestinatario, now, user.getIdentificativoUtente());

					// caso di salvataggio motivazione modifica
					if (istanza.getMetadata() != null) {
						eStoricoWf.setDatiAzione(istanza.getMetadata().toString());
					}
					
					storicoWorkflowDAO.updateDataFine(now, eIstanza.getIdIstanza());
					Long idStoricoWf = storicoWorkflowDAO.insert(eStoricoWf);
					eStoricoWf.setIdStoricoWorkflow(idStoricoWf);
				} else {
					log.info("[" + CLASS_NAME + "] istanza in bozza:" + eIstanza.getIdIstanza());
				}
			}

			Istanza istanzaSaved = IstanzaMapper.buildFromIstanzaEntity(eIstanza, eIstanzaCronologia, eIstanzaDati,
					statoE, moduloE);

			IstanzaSaveResponse result = new IstanzaSaveResponse();
			result.setIstanza(istanzaSaved);
			result.setCodice("SUCCESS");
			result.setDescrizione(null);

			// AssociaAllegatiAdIstanza
			log.info("[" + CLASS_NAME + "::run] new AssociaAllegatiAdIstanzaTask().call() ...");
			String associaAllegatiAdIstanza = new AssociaAllegatiAdIstanzaTask(user, istanzaSaved, moduloE).call();
			log.info("[" + CLASS_NAME + "::run] AssociaAllegatiAdIstanzaTask() Done : " + associaAllegatiAdIstanza);

			// Rigenerazione pdf
			if (lastStatoWf != null && (lastStatoWf != 1 && lastStatoWf != 10)) {
				log.info("[" + CLASS_NAME + "] Storico stato istanza: " + lastStatoWf);
				log.info("[" + CLASS_NAME + "] Rigenera e salva pdf per id istanza " + eIstanza.getIdIstanza());

				IstanzaMoonsrv istanzaMoonsrv = IstanzaMapper.buildIstanzaMoonsrvFromIstanzaEntity(eIstanza,
						eIstanzaCronologia, eIstanzaDati, statoE, moduloE);
				this.rigeneraSalvaPdf(istanzaMoonsrv);
			}

			return istanzaSaved;

		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::saveIstanza] ", e);
			throw e;
		}
	}

	private void validateForSaving(UserInfo user, Istanza istanza) throws BusinessException {
		if (istanza == null || istanza.getStato() == null || istanza.getStato().getIdStato() == null) {
			log.error("[" + CLASS_NAME + "::validateForSaving] istanza.getStato().getIdStato() NULL user:"
					+ user.getIdentificativoUtente() + "  istanza:" + istanza);
			throw new BusinessException("Dato Mancante : istanza.stato.idStato");
		}
		if (istanza == null || istanza.getModulo() == null || istanza.getModulo().getIdModulo() == null) {
			log.error("[" + CLASS_NAME + "::validateForSaving] istanza.getModulo().getIdModulo() NULL  user:"
					+ user.getIdentificativoUtente() + "  istanza:" + istanza);
			throw new BusinessException("Dato Mancante : istanza.modulo.idModulo");
		}
		if (istanza == null || istanza.getData() == null || String.valueOf(istanza.getData()).isEmpty()) {
			log.error("[" + CLASS_NAME + "::validateForSaving] istanza.getData() NULL  user:" + user.getIdentificativoUtente()
					+ "  istanza:" + istanza);
			throw new BusinessException("Dato Mancante : istanza.data");
		}
	}

	private void validateForModifying(UserInfo user, Istanza istanza, IstanzaEntity eIstanza) throws BusinessException {
		if (!eIstanza.getIdModulo().equals(istanza.getModulo().getIdModulo())) {
			log.error("[" + CLASS_NAME + "::validateForModifying] idModuloDB:" + eIstanza.getIdModulo() + "  idModulo:"
					+ istanza.getModulo().getIdModulo());
			throw new BusinessException("Istanze Validation : idModulo");
		}
		Boolean isUtenteAbilitato = false;
//		if (user.hasRuolo(DecodificaRuolo.OP_ADV.getId()) || user.hasRuolo(DecodificaRuolo.ADMIN.getId())
//				|| DecodificaTipoUtente.ADM.isCorrectType(user) || user.hasRuolo(DecodificaRuolo.OP_COMP.getId())) {
//			isUtenteAbilitato = true;
//		}
		
		if (user.hasRuoloByCode(DecodificaRuolo.OP_ADV.getCodice()) || user.hasRuoloByCode(DecodificaRuolo.ADMIN.getCodice())
				|| DecodificaTipoUtente.ADM.isCorrectType(user) || user.hasRuoloByCode(DecodificaRuolo.OP_COMP.getCodice()) ||
				 user.hasRuoloByCode(DecodificaRuolo.OP_SIMPMOD.getCodice())) {
			isUtenteAbilitato = true;
		}

		if (!isUtenteAbilitato) {
			log.error("[" + CLASS_NAME + "::validateForModifying] IdStatoWfDB:" + eIstanza.getIdStatoWf()
					+ "  USER NON ABILITATO");
			throw new BusinessException("Istanze Validation : idStatoWf");
		}
	}

	private void validateForSubmit(UserInfo user, IstanzaEntity eIstanza) throws BusinessException {
//		if (!user.getCodFisc().equals(eIstanza.getCodiceFiscaleDichiarante())) {
//			log.error("[" + CLASS_NAME + "::validateForModifying] user:" + user.getCodFisc() + "  eIstanza:"
//					+ eIstanza.getCodiceFiscaleDichiarante());
//			throw new BusinessException("Istanze Validation : l'utente non corrisponde al codiceFiscaleDichiaranteDB");
//		}

		Boolean isUtenteAbilitato = false;
//		if (user.hasRuolo(DecodificaRuolo.OP_ADV.getId()) || user.hasRuolo(DecodificaRuolo.ADMIN.getId())
//				|| DecodificaTipoUtente.ADM.isCorrectType(user) || user.hasRuolo(DecodificaRuolo.OP_COMP.getId())) {
//			isUtenteAbilitato = true;
//		}
		
		if (user.hasRuoloByCode(DecodificaRuolo.OP_ADV.getCodice()) || user.hasRuoloByCode(DecodificaRuolo.ADMIN.getCodice())
				|| DecodificaTipoUtente.ADM.isCorrectType(user) || user.hasRuoloByCode(DecodificaRuolo.OP_COMP.getCodice()) ||
				 user.hasRuoloByCode(DecodificaRuolo.OP_SIMPMOD.getCodice())) {
			isUtenteAbilitato = true;
		}

		if (!isUtenteAbilitato) {
			log.error("[" + CLASS_NAME + "::validateForSubmit] IdStatoWfDB:" + eIstanza.getIdStatoWf()
					+ "  USER NON ABILITATO");
			throw new BusinessException("Istanze Validation : idStatoWf");
		}

	}

	@Override
	public Istanza saveIstanza(UserInfo user, Long idIstanza, Istanza istanza) throws BusinessException {
		log.debug("[" + CLASS_NAME + "::saveIstanza] IN idIstanza: " + idIstanza);
		log.debug("[" + CLASS_NAME + "::saveIstanza] IN istanza: " + istanza);
		if (idIstanza == null || !idIstanza.equals(istanza.getIdIstanza())) {
			log.error("[" + CLASS_NAME + "::validatePUTsaveIstanza] idIstanza: " + idIstanza
					+ "  istanza.getIdIstanza():" + istanza.getIdIstanza());
			throw new BusinessException("Istanze Validation : idIstanza non corrispondente");
		}
		Istanza istanzaSaved = saveIstanza(user, istanza);
		return istanzaSaved;
	}

	@Override
	public Istanza deleteIstanza(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::deleteIstanza] IN idIstanza: " + idIstanza);
			Date now = new Date();

			// Recupero l'istanza anche per verificare l'esistenza e l'accessibilita
			IstanzaEntity eIstanza = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, eIstanza);

			// ISTANZA
			eIstanza.setAttoreUpd(user.getIdentificativoUtente());
			eIstanza.setDataCreazione(now);
			eIstanza.setFlagEliminata("S");
			istanzaDAO.update(eIstanza);

			// CRON
			// LAST CRON
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(idIstanza);
			if (lastIstanzaCronologia != null) {
				lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
				lastIstanzaCronologia.setDataFine(now);
				istanzaDAO.updateCronologia(lastIstanzaCronologia);
			}
			// NEW CRON
			IstanzaCronologiaStatiEntity newIstanzaCronologia = null;
			newIstanzaCronologia = new IstanzaCronologiaStatiEntity();
			newIstanzaCronologia.setIdIstanza(idIstanza);
			newIstanzaCronologia.setIdStatoWf(DecodificaStatoIstanza.CANCELLATO.getIdStatoWf());
			newIstanzaCronologia.setAttoreIns(user.getIdentificativoUtente());
			newIstanzaCronologia.setDataInizio(now);
			newIstanzaCronologia.setIdAzioneSvolta(DecodificaAzione.ELIMINA.getIdAzione());
			Long idCronologiaStati = istanzaDAO.insertCronologia(newIstanzaCronologia);
			newIstanzaCronologia.setIdCronologiaStati(idCronologiaStati);

			// DATI (essendo un cambio stato ricopio i dati precedente con la nuova
			// cronologia)
			IstanzaDatiEntity eIstanzaDati = istanzaDAO.findDati(eIstanza.getIdIstanza(),
					lastIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setAttoreUpd(user.getIdentificativoUtente());
			eIstanzaDati.setDataUpd(now);
			eIstanzaDati.setIdCronologiaStati(newIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.INI.getIdTipoModifica());
			Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
			eIstanzaDati.setIdDatiIstanza(idIstanzaDati);

			// RETURN
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, newIstanzaCronologia.getIdCronologiaStati());
			StatoEntity statoE = DecodificaStatoIstanza.CANCELLATO.getStatoEntity();
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(eIstanza.getIdModulo(),
					eIstanza.getIdVersioneModulo());

			Istanza istanzaDeleted = IstanzaMapper.buildFromIstanzaEntity(eIstanza, newIstanzaCronologia, eIstanzaDati,
					statoE, moduloE);

			return istanzaDeleted;

		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::deleteIstanza] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::deleteIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::deleteIstanza] ", e);
			throw e;
		}
	}

	/**
	 * Permette l'update partial di un intanza su alcuni campi : - istanza
	 */
	@Override
	public Istanza patchIstanza(UserInfo user, Long idIstanza, Istanza partialIstanza) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::patchIstanza] IN idIstanza: " + idIstanza);
			log.debug("[" + CLASS_NAME + "::patchIstanza] IN partialIstanza: " + partialIstanza);
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, entity);

//			Date now = new Date();

//	        if (partialIstanza.getImportanza() != null) {
//	        	entity.setImportanza(partialIstanza.getImportanza());
//	        }

			// Salva
			istanzaDAO.update(entity);

			// Rilegge l istanza
			Istanza istanza = getIstanzaById(user, idIstanza);
			return istanza;
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::patchIstanzaById] Errore servizio patchIstanzaById", e);
			throw new BusinessException("Errore servizio aggiorna partial Istanza");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::patchIstanzaById] Errore generico servizio patchIstanzaById", ex);
			throw new BusinessException("Errore generico aggiorna partial Istanza");
		}
	}

	@Override
	public byte[] getPdfIstanza(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			return moonsrvDAO.getPdfByIdIstanza(user, idIstanza);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getPdfIstanza] Errore invocazione DAO per moonsrv ", e);
			throw new BusinessException("Errore generazione pdf per istanza=" + idIstanza);
		}
	}

//	@Override
//	public String generaSalvaPdf(Long idIstanza) throws BusinessException {
//		try {
//			return moonsrvDAO.generaSalvaPdf(idIstanza);
//		} catch (DAOException e) {
//			log.error("[" + CLASS_NAME + "::getPdfIstanza] Errore invocazione DAO per moonsrv ", e);
//			throw new BusinessException("Errore generazione pdf per istanza=" + idIstanza);
//		}
//	}	

	private void rigeneraSalvaPdf(IstanzaMoonsrv istanza) throws BusinessException {
		try {
			// call locale service printPdf
			byte[] bytes = moonsrvDAO.generaPdf(istanza);
			
			// salva
			Optional<IstanzaPdfEntity> pdfOpt = istanzaPdfDAO.findByIdIstanza(istanza.getIdIstanza());
			if (!pdfOpt.isEmpty()) {
				IstanzaPdfEntity pdfEntity = pdfOpt.get();
				pdfEntity.setContenutoPdf(bytes);
				pdfEntity.setDataUpd(istanza.getModified());
				istanzaPdfDAO.update(pdfEntity);
			}
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getPdfIstanza] Errore invocazione DAO per moonsrv ", e);
			throw new BusinessException("Errore generazione pdf per istanza=" + istanza.getIdIstanza());
		}
	}

	public String modificaContesto(String testo, Long idModulo, String nomePortale) throws Exception {

		Pattern p = Pattern.compile("moonfobl", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

		String result = p.matcher(testo).replaceAll("moonbobl");

		Pattern pm = Pattern.compile("modulistica", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

		result = pm.matcher(result).replaceAll("moonbobl");
		/*
		 * "url":"/modulistica/restfacade/be/file String REGEX =
		 * "[a-zA-Z0-9\\-]+\\.patrim\\.csi\\.it"; Pattern pvh = Pattern.compile(REGEX,
		 * Pattern.DOTALL | Pattern.CASE_INSENSITIVE); result =
		 * pvh.matcher(result).replaceAll(nomePortale);
		 */

		String REGEX = "https://[a-zA-Z0-9\\-]+\\.patrim\\.csi\\.it/";
		Pattern pvh = Pattern.compile(REGEX, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		result = pvh.matcher(result).replaceAll("/");

		if (nomePortale.equals("localhost")) {
			Pattern plocalvh = Pattern.compile("https://localhost\\.patrim\\.csi\\.it",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			result = plocalvh.matcher(result).replaceAll("http://localhost:4200");
		}

		String urlDownload = "url\":\"https://" + nomePortale
				+ "/moonbobl/restfacade/be/file?baseUrl=https%3A%2F%2Fapi.form.io&project=&form=";
		if (nomePortale.equals("localhost")) {
			urlDownload = "url\":\"http://" + nomePortale
					+ ":4200/moonbobl/restfacade/be/file?baseUrl=https%3A%2F%2Fapi.form.io&project=&form=";
		}
		Pattern pUndef = Pattern.compile("url\":\"undefined", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		result = pUndef.matcher(result).replaceAll(urlDownload);

		/*
		 * Pattern pvhtest = Pattern.compile("tst-moon-internet", Pattern.DOTALL |
		 * Pattern.CASE_INSENSITIVE);
		 * 
		 * result = pvhtest.matcher(result).replaceAll("tst-moon-ru");
		 * 
		 * if (idModulo.intValue() == 20 || idModulo.intValue() == 21 ||
		 * idModulo.intValue() == 28 || idModulo.intValue() == 14 || idModulo.intValue()
		 * == 35 ) { Pattern pvhtor = Pattern.compile("moon-torinofacile\\.patrim",
		 * Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		 * 
		 * result = pvhtor.matcher(result).replaceAll("moon-bo\\.patrim"); }
		 */
		return result;
	}

	// modifica contesto per salvataggio e gestione lato fo degli allegati
//	public String modificaContestoSalvaBozza(String testo) {
//
//		log.debug("[" + CLASS_NAME + "::modificaContestoSalva] IN string json data: " + testo);
//		try {
//			Pattern p = Pattern.compile("moonbobl", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
//			String result = p.matcher(testo).replaceAll("moonfobl");		
//			return result;
//		} catch (Exception e) {			
//			log.debug("[" + CLASS_NAME + "::modificaContestoSalva] Errore in sostituzione contesto");
//			return testo;
//		}
//	}

	/**
	 * Get Last File from repositoryFileDAO
	 */
	@Override
	public byte[] getRicevutaPdfByIstanza(UserInfo user, Long idIstanza) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::getRicevutaPdfByIstanza] IN idIstanza: " + idIstanza);
			List<RepositoryFileEntity> list = repositoryFileDAO.findByIdIstanza(idIstanza);
			if (list != null && !list.isEmpty()) {
				return list.get(list.size() - 1).getContenuto();
			}
			return null;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getRicevutaPdfByIstanza] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero ricevuta pdf per istanza=" + idIstanza);
		}
	}

	/**
	 * Elenco Stati BO (da sotrico_workflow) su elencoIstanze
	 */
	@Override
	public List<Stato> getElencoStatiBoSuElencoIstanze(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati)
			throws BusinessException {
		List<Stato> elencoStatiBo = new ArrayList<Stato>();
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getElencoStatiBoSuElencoIstanze] IN filter: " + filter);
				log.debug("[" + CLASS_NAME + "::getElencoStatiBoSuElencoIstanze] IN sorter: " + sorter);
				log.debug("[" + CLASS_NAME + "::getElencoStatiBoSuElencoIstanze] IN filtroRicercaDati: " + filtroRicercaDati);
			}
			List<Integer> idStatiBoPossibili = istanzaDAO.findDistinctIdStatiBoPossibili(filter, filtroRicercaDati);
			if (idStatiBoPossibili != null && idStatiBoPossibili.size() > 0) {
				for (Integer idStato : idStatiBoPossibili) {
					StatoEntity statoE = statoDAO.findById(idStato);
					elencoStatiBo.add(StatoMapper.buildFromEntity(statoE));
				}
			}
			return elencoStatiBo;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoStatiBoSuElencoIstanze] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco stato BO di elenco Istanze");
		}
	}

	@Override
	public List<Istanza> getElencoIstanze(IstanzeFilter filter, Optional<IstanzeSorter> sorter,
			String filtroRicercaDati, String fields) throws BusinessException {
		List<Istanza> elencoIstanze = new ArrayList<Istanza>();
		try {
			List<IstanzaEntity> elenco = istanzaDAO.find(filter, sorter, filtroRicercaDati);
			if (elenco != null && elenco.size() > 0) {
				for (IstanzaEntity entity : elenco) {
					IstanzaCronologiaStatiEntity cronE = null;
					IstanzaDatiEntity datiE = null;
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWfArrivo());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
							entity.getIdVersioneModulo());
					if (fields != null && (fields.contains("data") || moduloE.getCodiceModulo().startsWith("RP_RSI"))) {
						cronE = istanzaDAO.findLastCronologia(entity.getIdIstanza());
						datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());
					}
					// se l'istanza non e' in bozza cerco la data invio
					Date dataInvio = null;
					if (entity.getIdStatoWf().intValue() != 1 && entity.getIdStatoWf().intValue() != 10) {
						try {
							IstanzaCronologiaStatiEntity datiInvio = istanzaDAO.findInvio(entity.getIdIstanza());
							if (datiInvio != null) {
								dataInvio = datiInvio.getDataInizio();
							}
						} catch (Exception e) {
							log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore invocazione DAO - findInvio", e);
						}
					}

					String nomeComune = "";
					if (moduloE.getCodiceModulo().startsWith("RP_RSI")) {
						DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
						try {
							nomeComune = datiIstanzaBoHelper.getNomeComune(datiE.getDatiIstanza());
						} catch (Exception e) {
							log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore lettura comune", e);
						}
					}
					if (fields != null && fields.contains("data")) {
						elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE,
								nomeComune, dataInvio));
					} else {
						elencoIstanze.add(
								IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeComune, dataInvio));
					}
				}
			}
			return elencoIstanze;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco istanza");
		}
	}

	@Override
	public void protocolla(UserInfo user, Long idIstanza) {
		try {
			log.debug("[" + CLASS_NAME + "::protocolla] IN idIstanza = " + idIstanza);
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);
			if (!StringUtils.isEmpty(entity.getNumeroProtocollo())) {
				log.error("[" + CLASS_NAME + "::protocolla] Protocollo " + entity.getNumeroProtocollo()
						+ " già presente sull'istanza " + entity.getIdIstanza() + " - " + entity.getCodiceIstanza());
				throw new BusinessException("Protocollo " + entity.getNumeroProtocollo() + " già presente sull'istanza "
						+ entity.getIdIstanza() + " - " + entity.getCodiceIstanza(), "MOONBOBL-00010");
			}
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
					entity.getIdVersioneModulo());
			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
			MapModuloAttributi mapModuloAttributi = new MapModuloAttributi(attributiModuloE);
			Boolean protocollo = mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_PROTOCOLLO);
			if (Boolean.TRUE.equals(protocollo)) {
				String response = moonsrvDAO.protocolla(idIstanza);
				log.debug("[" + CLASS_NAME + "::protocolla] response = " + response);
			} else {
				log.error("[" + CLASS_NAME + "::protocolla] Protocollo non previsto per questo modulo "
						+ moduloE.getIdModulo() + " - " + moduloE.getCodiceModulo());
				throw new BusinessException("Protocollo non previsto per il modulo " + moduloE.getCodiceModulo(),
						"MOONBOBL-00011");
			}
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::protocolla] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		}
	}

	/**
	 * dest: user/protocollo/user,protocollo/postCallbackPrt
	 */
	@Override
	public void rinviaEmail(UserInfo user, Long idIstanza, String dest) throws BusinessException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::rinviaEmail] IN idIstanza = " + idIstanza);
				log.debug("[" + CLASS_NAME + "::rinviaEmail] IN dest = " + dest);
			}
			IstanzaEntity entity = istanzaDAO.findById(idIstanza);

			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
			StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
					entity.getIdVersioneModulo());
			List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(moduloE.getIdModulo());
			MapModuloAttributi mapModuloAttributi = new MapModuloAttributi(attributiModuloE);

			Istanza istanza = IstanzaMapper.buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE);

			boolean isPostCallbackProtocollo = "postCallbackPrt".equalsIgnoreCase(dest);
			
			Boolean email = isPostCallbackProtocollo?
					mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PCPT_IN_EMAIL):
					mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_EMAIL);
			if (Boolean.TRUE.equals(email)) {
				String confSendEmail = isPostCallbackProtocollo?
						mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PCPT_IN_EMAIL_CONF):
						mapModuloAttributi.getWithCorrectType(ModuloAttributoKeys.PSIT_EMAIL_CONF);
				if (StringUtils.isNotEmpty(confSendEmail)) {
					IstanzaSaveResponse istanzaSaved = new IstanzaSaveResponse();
					istanzaSaved.setIstanza(istanza);
					istanzaSaved.setCodice("SUCCESS");

					log.info("[" + CLASS_NAME + "::rinviaEmail] new SendEmailDichiaranteIstanzaTask().call() ...");

					String emailDichiaranteIstanza = null;
					if (isPostCallbackProtocollo) {
						emailDichiaranteIstanza = new SendEmailDichiaranteIstanzaTask(user, istanzaSaved,
								confSendEmail, "postCallbackPrt").call();	
					} else {
						emailDichiaranteIstanza = new SendEmailDichiaranteIstanzaTask(user, istanzaSaved,
							confSendEmail, dest, true).call();
					}
				} else {
					log.error("[" + CLASS_NAME + "::run] attributo PSIT_EMAIL_CONF mancante per il modulo "
							+ moduloE.getIdModulo() + " " + moduloE.getCodiceModulo());
				}
				String response = null;

				log.debug("[" + CLASS_NAME + "::rinviaEmail] NOT IMPLEMENTED response = " + response);
			} else {
				log.error("[" + CLASS_NAME + "::rinviaEmail] InvioEmail non previsto per questo modulo "
						+ moduloE.getIdModulo() + " - " + moduloE.getCodiceModulo());
				throw new BusinessException("InvioEmail non previsto per il modulo " + moduloE.getCodiceModulo(),
						"MOONBOBL-00012");
			}
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::rinviaEmail] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		}
	}

	@Override
	public void rinviaEmails(Long idTag, String dest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::rinviaEmails] IN idTag = " + idTag);
				log.debug("[" + CLASS_NAME + "::rinviaEmails] IN dest = " + dest);
			}
			IstanzeFilter filter = new IstanzeFilter();
			filter.setIdTag(idTag);
			List<IstanzaEntity> istanze = istanzaDAO.find(filter, Optional.empty(), null);
			log.info("[" + CLASS_NAME + "::rinviaEmails] istanze.size() = " + istanze != null ? istanze.size()
					: " null ");
//			istanze.forEach(i -> log.info("[" + CLASS_NAME + "::rinviaEmails] rinviaEmail for idIstanza: " + i.getIdIstanza() + "  dest: " + dest));
			istanze.forEach(i -> rinviaEmail(i.getIdIstanza(), dest));
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::rinviaEmails] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		}
	}

	private void rinviaEmail(Long idIstanza, String dest) {
		rinviaEmail(null, idIstanza, dest);
	}

	@Override
	@Transactional
	public Istanza riportaInBozza(UserInfo user, Long idIstanza) {
		try {
			log.debug("[" + CLASS_NAME + "::riportaInBozza] IN idIstanza: " + idIstanza);
			Date now = new Date();
			IstanzaEntity eIstanza = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, eIstanza);
			// Valida se esiste workflowE dell'azione richiesta per il processo / istanza
			Long idProcesso = moduloDAO.findIdProcesso(eIstanza.getIdModulo());
			WorkflowEntity workflow = workflowDAO.findByProcessoAzione(idProcesso, eIstanza.getIdStatoWf(),
					DecodificaAzione.RIPORTA_IN_BOZZA.getIdAzione());

			// 1-BOZZA
			Integer idStatoWfPartenza = eIstanza.getIdStatoWf();
			Integer idStatoWfBozza = DecodificaStatoIstanza.BOZZA.getIdStatoWf();

			// ISTANZE
			eIstanza.setAttoreUpd(user.getIdentificativoUtente());
			eIstanza.setIdStatoWf(idStatoWfBozza);
			eIstanza.setCurrentStep(0);

			// gestione caso di riporta in bozza
			eIstanza.setHashUnivocita(null);

			istanzaDAO.update(eIstanza);

			// CRON
			// last CRON
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(eIstanza.getIdIstanza());
			lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
			lastIstanzaCronologia.setDataFine(now);
			istanzaDAO.updateCronologia(lastIstanzaCronologia);
			// new CRON
			IstanzaCronologiaStatiEntity eIstanzaCronologia = new IstanzaCronologiaStatiEntity();
			eIstanzaCronologia.setIdIstanza(eIstanza.getIdIstanza());
			eIstanzaCronologia.setIdStatoWf(idStatoWfBozza);
			eIstanzaCronologia.setAttoreIns(user.getIdentificativoUtente());
			eIstanzaCronologia.setDataInizio(now);
			azioneDAO.initCache();
			eIstanzaCronologia.setIdAzioneSvolta(azioneDAO.findIdByCd(DecodificaAzione.RIPORTA_IN_BOZZA.getCodice()));
			Long idCronologiaStati = istanzaDAO.insertCronologia(eIstanzaCronologia);
			eIstanzaCronologia.setIdCronologiaStati(idCronologiaStati);

			// DATI
			IstanzaDatiEntity eIstanzaDati = null;
			eIstanzaDati = istanzaDAO.findDati(eIstanza.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setDataUpd(now);
			DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
			eIstanzaDati.setDatiIstanza(datiIstanzaHelper.updateSubmitFalse(eIstanzaDati.getDatiIstanza())); // RIPORTA
																												// IN
																												// BAZZA
			eIstanzaDati.setIdCronologiaStati(eIstanzaCronologia.getIdCronologiaStati());
			eIstanzaDati.setIdStepCompilazione(null);
			eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.INI.getIdTipoModifica());
			Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
			eIstanzaDati.setIdDatiIstanza(idIstanzaDati);

			// STORICO WORKFLOW
			String descDestinatario = "compilatore";
			StoricoWorkflowEntity eStoricoWf = new StoricoWorkflowEntity(null, eIstanza.getIdIstanza(), idProcesso,
					DecodificaStatoIstanza.byIdStatoWf(idStatoWfPartenza), DecodificaStatoIstanza.BOZZA,
					DecodificaAzione.RIPORTA_IN_BOZZA, descDestinatario, now, user.getIdentificativoUtente());
			storicoWorkflowDAO.updateDataFine(now, eIstanza.getIdIstanza());
			Long idStoricoWf = storicoWorkflowDAO.insert(eStoricoWf);
			eStoricoWf.setIdStoricoWorkflow(idStoricoWf);

			// Elimino PDF se presente
			istanzaPdfDAO.deleteByIdIstanza(idIstanza);

			// Rilegge l istanza
			Istanza istanza = getIstanzaById(user, idIstanza);
			return istanza;
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::riportaInBozza] Errore servizio riportaInBozza", e);
			throw new BusinessException("Errore servizio riportaInBozza");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::riportaInBozza] Errore generico servizio riportaInBozza", ex);
			throw new BusinessException("Errore generico riportaInBozza");
		}
	}

	@Override
	@Transactional
	public IstanzaSaveResponse invia(UserInfo user, Long idIstanza, DatiAggiuntiviHeaders daHeaders, String ipAddress) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::invia] IN user: " + user);
				log.debug("[" + CLASS_NAME + "::invia] IN idIstanza: " + idIstanza);
				log.debug("[" + CLASS_NAME + "::invia] IN daHeaders: " + daHeaders);
			}
			IstanzaEntity istanzaE = istanzaDAO.findById(idIstanza);
			validateUserAccess(user, istanzaE);
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(istanzaE.getIdModulo(),
					istanzaE.getIdVersioneModulo());
			validaModuloVersionatoPerCompilazione(moduloE);
			//
			IstanzaServiceDelegate istanzaServiceDelegate = new IstanzaServiceDelegateFactory()
					.getDelegate(moduloE.getCodiceModulo());
			return istanzaServiceDelegate.invia(user, istanzaE, moduloE, daHeaders, ipAddress);
			// Result
//			return getIstanzaById(user, idIstanza);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::invia] Errore invocazione DAO", e);
			throw new BusinessException("Errore Invia istanza");
		}
	}

//	@Override
//	public byte[] getJsonIstanza(UserInfo user, Long idIstanza) throws BusinessException {
//
//
//		
//		
//	}

	private void validaModuloVersionatoPerCompilazione(ModuloVersionatoEntity moduloE) throws BusinessException {
		if (!(DecodificaStatoModulo.byId(moduloE.getIdStato())).isCompilabile()) {
			log.error("[" + CLASS_NAME + "::validaModuloVersionatoForInit] Errore Modulo non piu compilabile "
					+ moduloE.getIdModulo() + "/" + moduloE.getIdVersioneModulo());
			throw new BusinessException("Modulo non piu compilabile.", "MOONFOBL-10010");
		}
	}

	@Override
	@Transactional
	public IstanzaSaveResponse saveCompilaIstanza(UserInfo user, Istanza istanza) {
		try {
			log.debug("[" + CLASS_NAME + "::saveIstanza] IN istanza: " + istanza);
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(istanza.getModulo().getIdModulo(),
					istanza.getModulo().getIdVersioneModulo());
			validaModuloVersionatoPerCompilazione(moduloE);
			//
			log.debug("[" + CLASS_NAME + "::saveIstanza] IN istanza: " + istanza);
			IstanzaServiceDelegate istanzaServiceDelegate = new IstanzaServiceDelegateFactory()
					.getDelegate(moduloE.getCodiceModulo());
			return istanzaServiceDelegate.saveIstanza(user, istanza);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore Init istanza per id");
		}
	}

	@Override
	public ResponsePaginated<Istanza> getElencoIstanzeDaCompletarePaginate(IstanzeFilter filter,
			Optional<IstanzeSorter> sorter, String filtroRicercaDati) {
		Integer totalElements = istanzaDAO.countDaCompletare(filter, filtroRicercaDati);

		List<Istanza> elencoIstanze = new ArrayList<Istanza>();
		try {
			List<IstanzaEntity> elenco = istanzaDAO.findDaCompletare(filter, sorter, filtroRicercaDati);
			if (elenco != null && elenco.size() > 0) {
				for (IstanzaEntity entity : elenco) {
					StatoEntity statoE = statoDAO.findById(entity.getIdStatoWf());
					ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoById(entity.getIdModulo(),
							entity.getIdVersioneModulo());
					IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(entity.getIdIstanza());

					String operatore = "";
					UtenteEntity user;
					try {
						if (entity.getIdStatoWf() == 1) {
							user = utenteDAO.findByIdentificativoUtente(datiE.getAttoreUpd());
						} else {
							user = storicoWorkflowDAO.findOperatore(entity.getIdIstanza());
						}
						operatore = generaNomeBreveOperatore(user);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error("[" + CLASS_NAME
								+ "::getElencoIstanzeDaCompletarePaginate] Errore lettura operatore - ", e);
					}

					// se l'istanza e' in bozza la data invio è nulla
					Date dataInvio = null;

					String nomeComune = "";
					String nomeEnte = "";
					DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();

					try {
						nomeComune = datiIstanzaBoHelper.getStringValueByKey(datiE.getDatiIstanza(), "comune.nome");
					} catch (Exception e) {
						log.debug("[" + CLASS_NAME + "::getElencoIstanzeDaCompletarePaginate] comune non trovato: "
								+ nomeComune);
					}
					try {
						//nomeEnte = datiIstanzaBoHelper.getNomeEnte(datiE.getDatiIstanza());
						nomeEnte = datiIstanzaBoHelper.getNomeEnte(filtroRicercaDati,datiE.getDatiIstanza());
					} catch (Exception e) {
						log.debug("[" + CLASS_NAME + "::getElencoIstanzeDaCompletarePaginate] ente non trovato: "
								+ nomeEnte);
					}

					if (!StringUtils.isEmpty(nomeEnte)) {
						elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeEnte,
								dataInvio, operatore));
					} else {
						elencoIstanze.add(IstanzaMapper.buildFromIstanzaEntity(entity, statoE, moduloE, nomeComune,
								dataInvio, operatore));
					}
				}
			}

			ResponsePaginated<Istanza> result = new ResponsePaginated<Istanza>();
			result.setItems(elencoIstanze);
			result.setPage(filter.getOffset() % filter.getLimit()); // 0 based
			result.setPageSize(filter.getLimit());
			result.setTotalElements(totalElements);
			result.setTotalPages(
					((Double) Math.ceil(totalElements.doubleValue() / filter.getLimit().doubleValue())).intValue());
			return result;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoIstanze] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore recupero elenco istanza");
		}
	}
	
	private boolean getIsPagato(String filtroEpay, Long idIstanza) {
		boolean isPagato = false;
		try {
			switch (filtroEpay) {
				case Constants.FILTRO_EPAY_PAID:
					isPagato = true;
					break;
				case Constants.FILTRO_EPAY_UNPAID:
					isPagato = false;
					break;
			default:
//				EpayRichiestaEntity epayRicEntity = epayRichiestaDAO.findLastValideByIdIstanza(idIstanza);
				List<EpayRichiestaEntity> richiesteE = epayRichiestaDAO.findByIdIstanza(idIstanza);
				EpayRichiestaEntity epayRicEntity = richiesteE.stream().filter(e -> e.getIdNotificaPagamento() != null).findAny()
						.orElse(null);
				if (epayRicEntity != null && epayRicEntity.getIdNotificaPagamento() != null) {
					isPagato = true;
				}
			}

		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::getIsPagato] Exception but continue istanza:" + idIstanza, e);
		}
		return isPagato;
	}

	@Override
	public byte[] getCustomJsonIstanzeByModulo(UserInfo user, Long idModulo, String codiceEstrazione, Date createdStart,
			Date createdEnd) throws BusinessException {
		try {
			ModuloEntity moduloE = moduloDAO.findById(idModulo);
			return moonsrvDAO.getReport(moduloE.getCodiceModulo(), codiceEstrazione, createdStart, createdEnd);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getCustomJsonIstanzeByModulo] Errore invocazione DAO per moonsrv ", e);
			throw new BusinessException("Errore generazione pdf per idModulo=" + idModulo);
		}
	}

	private String generaNomeBreveOperatore (UtenteEntity user) {
		String nomeBreve = (user.getNome().equals("") && user.getCognome().equals("")) ? ""
				: user.getNome().toUpperCase().substring(0, 1) + "." + user.getCognome().toUpperCase()
						.substring(0, Math.min(user.getCognome().length(), 5));
		return nomeBreve;
		
	}
}
