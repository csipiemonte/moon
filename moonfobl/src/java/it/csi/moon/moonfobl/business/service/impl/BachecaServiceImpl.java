/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.commons.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.moonfobl.business.service.BachecaService;
import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonfobl.dto.moonfobl.MessaggioBacheca;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class BachecaServiceImpl implements BachecaService {
	
	private static final String CLASS_NAME = "BachecaServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
    @Autowired
    IstanzaDAO istanzaDAO;

	@Override
	public List<MessaggioBacheca> getElencoMessaggiBacheca(UserInfo user) {			
		List<MessaggioBacheca> messaggiBacheca = new ArrayList<>();
		messaggiBacheca.addAll(verificaIstanzeAttesaIntegrazione(user));	
		messaggiBacheca.addAll(verificaIstanzeAttesaPagamento(user));
		return messaggiBacheca;
	}

	private List<MessaggioBacheca> verificaIstanzeAttesaIntegrazione(UserInfo user) {
		List<MessaggioBacheca> messaggiBacheca = new ArrayList<>();
		try {
			IstanzeFilter filter = new IstanzeFilter();		
			filter.setIdentificativoUtente(user.getIdentificativoUtente());			
			filter.setIdEnte(user.getEnte().getIdEnte());
            filter.setIdTabFo(1);
//			filter.setFlagArchiviata(IstanzeFilter.EnumFilterFlagArchiviata.NONARCHIVIATI);
			filter.setFlagEliminata(IstanzeFilter.EnumFilterFlagEliminata.NONELIMINATI);
			filter.setFlagTest(IstanzeFilter.EnumFilterFlagTest.NONTEST);
			filter.setStatiIstanza(Arrays.asList(DecodificaStatoIstanza.IN_ATTESA_INTEGRAZIONE.getIdStatoWf()));			
			Optional<IstanzeSorter> sorter = Optional.empty();			
			List<IstanzaEntity> istanze = istanzaDAO.find(filter,null,sorter);
			
			if(istanze != null && !istanze.isEmpty()) {			
				messaggiBacheca.add(new MessaggioBacheca("INFO","Sono presenti istanze che richiedono una integrazione"));
			}
						
		} catch (DAOException e) {
			LOG.warn("[" + CLASS_NAME + "::verificaIstanzeAttesaIntegrazione] Errore DAO ",e );
		}
		return messaggiBacheca;
	}
	
	private List<MessaggioBacheca> verificaIstanzeAttesaPagamento(UserInfo user) {
		List<MessaggioBacheca> messaggiBacheca = new ArrayList<>();
		try {
			IstanzeFilter filter = new IstanzeFilter();		
			filter.setIdentificativoUtente(user.getIdentificativoUtente());			
			filter.setIdEnte(user.getEnte().getIdEnte());
            filter.setIdTabFo(1);
//			filter.setFlagArchiviata(IstanzeFilter.EnumFilterFlagArchiviata.NONARCHIVIATI);
			filter.setFlagEliminata(IstanzeFilter.EnumFilterFlagEliminata.NONELIMINATI);
			filter.setFlagTest(IstanzeFilter.EnumFilterFlagTest.NONTEST);
			filter.setStatiIstanza(Arrays.asList(DecodificaStatoIstanza.ATTESA_PAGAMENTO.getIdStatoWf(),DecodificaStatoIstanza.IN_PAGAMENTO_ONLINE.getIdStatoWf(),DecodificaStatoIstanza.ATTESA_RICEVUTA_PAGAMENTO.getIdStatoWf(),DecodificaStatoIstanza.IN_PAGAMENTO.getIdStatoWf(),DecodificaStatoIstanza.DA_PAGARE.getIdStatoWf()));			
			Optional<IstanzeSorter> sorter = Optional.empty();			
			List<IstanzaEntity> istanze = istanzaDAO.find(filter, null, sorter);
			
			if(istanze != null && !istanze.isEmpty()) {			
				messaggiBacheca.add(new MessaggioBacheca("INFO","Sono presenti istanze in attesa di pagamento"));
			}
						
		} catch (DAOException e) {
			LOG.warn("[" + CLASS_NAME + "::verificaIstanzeAttesaPagamento] Errore DAO ",e );
		}
		return messaggiBacheca;
	}
	
}
