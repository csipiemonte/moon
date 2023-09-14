/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ResponseOperazioneMassiva;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzaPdfEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.TicketCrmService;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.TagIstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.ticketcrm.TicketingSystem;
import it.csi.moon.moonsrv.business.service.impl.ticketcrm.TicketingSystemFactory;
import it.csi.moon.moonsrv.business.service.impl.ticketcrm.TicketingSystemParams;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi alla protocollazione delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class TicketCrmServiceImpl implements TicketCrmService {
	
	private static final String CLASS_NAME = "TicketCrmServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	IstanzeService istanzeService;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	TagIstanzaDAO tagIstanzaDAO;
	@Autowired
	ModuloAttributiDAO moduloAttrDAO;
	
	@Override
	public void creaTicketCrmIstanza(Long idIstanza) throws BusinessException {
        long start = System.currentTimeMillis();
        String errore = "";
		try {
			LOG.debug("[" + CLASS_NAME + "::creaTicketCrmIstanza] IN idIstanza: "+idIstanza);
			// Recupero l'istanza e richiama il secondo servizo
			creaTicketCrmIstanza(istanzeService.getIstanzaById(idIstanza,"attributiTicketCRM"));
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::creaTicketCrmIstanza] ");
			errore = "BusinessException";
			throw e;
		} finally {
            long end = System.currentTimeMillis();
            float msec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::creaTicketCrmIstanza] SERVICE_ELAPSED_TIME " + msec + " milliseconds." + errore);
        }
	}
	

	@Override
	public ResponseOperazioneMassiva creaTicketCrmIstanzaMassivo(Long idTag) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::creaTicketCrmIstanzaMassivo] IN idTag = " + idTag);
			}
			ResponseOperazioneMassiva resp = new ResponseOperazioneMassiva();
			resp.setOperation("TS_CRM");
			resp.setIdTag(idTag);
			resp.setStarted(new Date());
			//
			IstanzeFilter filter = new IstanzeFilter();
			filter.setIdTag(idTag);
			List<IstanzaEntity> istanze = istanzaDAO.find(filter, Optional.empty());
			LOG.info("[" + CLASS_NAME + "::creaTicketCrmIstanzaMassivo] istanze.size()=" + istanze!=null?istanze.size():"null");
//			istanze.forEach(i -> creaTicketCrmIstanza(istanzeService.getIstanzaById(i.getIdIstanza()), prtParams));
			int total = istanze!=null?istanze.size():0;
			int ok = 0;
			int ko = 0;
			for (IstanzaEntity i : istanze) {
				try {
					creaTicketCrmIstanza(istanzeService.getIstanzaById(i.getIdIstanza()));
					ok++;
				} catch (Exception e) {
					ko++;
					tagIstanzaDAO.updateEsito(idTag, i.getIdIstanza(), "ERR");
					LOG.warn("[" + CLASS_NAME + "::creaTicketCrmIstanzaMassivo] Exception for istanza=" + i.getIdIstanza() + " - " + e.getMessage());
				}
			}
			LOG.info("[" + CLASS_NAME + "::creaTicketCrmIstanzaMassivo] istanze.size()=" + (istanze!=null?istanze.size():"null") 
				+ "   OK=" + ok + "   KO=" + ko);
			resp.setTotal(total);
			resp.setOk(ok);
			resp.setKo(ko);
			resp.setStatus(total==ok?"SUCCESS":"WARNING");
			resp.setEnded(new Date());
			return resp;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::creaTicketCrmIstanzaMassivo] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		}
	}
	
	/**
	 * Servizio principale di protocollazione di un istanza
	 */
	@Override
	public void creaTicketCrmIstanza(Istanza istanza) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::creaTicketCrmIstanza] IN istanza: "+istanza);
				LOG.debug("[" + CLASS_NAME + "::creaTicketCrmIstanza] IN istanza.idIstanza: "+istanza.getIdIstanza());
				LOG.debug("[" + CLASS_NAME + "::creaTicketCrmIstanza] IN istanza.idModulo: "+istanza.getModulo().getIdModulo());
			}
			validaIstanzaForTicketing(istanza);
//			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte());
//			istanza.setIdArea(areaModulo.getIdArea()); // TODO Da vedere se valorizzarlo da sempre molto prima, per il momento per sicurezza si rivalorizza
//			// Recupera la configurazione per la protocollazione per ente, sovrascritta per aera, sovrascritta per modullo (gestito via order by e sovrascrittura nella Mappa)
//			List<ProtocolloParametroEntity> listAttributi = protocolloParametroDAO.findForModulo(areaModulo);
//			LOG.debug("[" + CLASS_NAME + "::creaTicketCrmIstanza] listAttributi (non filtrati, con possibile dupplicati) ma ordinata ! \n" + listAttributi);
//			MapProtocolloAttributi attributi = new MapProtocolloAttributi(listAttributi);
//			LOG.debug("[" + CLASS_NAME + "::creaTicketCrmIstanza] attributi (senza dupplicati) \n" + attributi);
//			if (listAttributi!=null && listAttributi.size()>=1) {
//				String codiceProtocolloIstanza = attributi.getWithCorrectType(ProtocolloAttributoKeys.SISTEMA_PROTOCOLLO_ISTANZA); // STARDAS, ...
//				if (StringUtils.isNotEmpty(codiceProtocolloIstanza)) {
			String codiceTicketingSystem = moduloAttrDAO.findByNome(istanza.getModulo().getIdModulo(), ModuloAttributoKeys.PSIT_CRM_SYSTEM.getKey()).getValore();
			
			IstanzaPdfEntity pdfE = printIstanzeService.getIstanzaPdfEntityById(istanza.getIdIstanza());
						
			TicketingSystem crm = new TicketingSystemFactory().getTicketingSystem(codiceTicketingSystem, istanza.getModulo().getCodiceModulo());
			TicketingSystemParams params = new TicketingSystemParams();
			params.setIstanzaPdf(pdfE);
			String response = crm.creaTicketIstanza(istanza, params);
			LOG.info("[" + CLASS_NAME + "::creaTicketCrmIstanza] response uuid_ticketing_system= " + response);
//				} else {
//					LOG.error("[" + CLASS_NAME + "::creaTicketCrmIstanza] COD_SISTEMA_PROTOCOLLO_ISTANZA mancate per areaModulo:" + areaModulo);
//					throw new BusinessException("SISTEMA_PROTOCOLLO_ISTANZA non trovata o vuota nei attributi del modulo","MOONSRV-30602");
//				}
//			}
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::creaTicketCrmIstanza] ERROR " + be.getMessage());
			throw be;
		}
	}

	private void validaIstanzaForTicketing(Istanza istanza) throws BusinessException {
//		if (StringUtils.isNotEmpty(istanza.getNumeroProtocollo()) || istanza.getDataProtocollo()!=null) {
//			LOG.error("[" + CLASS_NAME + "::creaTicketCrmIstanza] numeroProtocollo = "+istanza.getNumeroProtocollo()+"   dataProtocollo = "+istanza.getDataProtocollo());
//			throw new BusinessException("Istanza già con tiket aperto "+istanza.getNumeroProtocollo(),"MOONSRV-30601");
//		}
	}
	
}
