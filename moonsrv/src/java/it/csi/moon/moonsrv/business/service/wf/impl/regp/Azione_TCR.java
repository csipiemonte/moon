/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.wf.impl.regp;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.moonsrv.business.service.wf.AzioneService;
import it.csi.moon.moonsrv.business.service.wf.Azione_Default;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;


/**
 * 
 * 
 *
 */
public class Azione_TCR extends Azione_Default implements AzioneService {
	
//	private static final String CLASS_NAME = "Azione_TCR";
	
//	@Autowired
//	RepositoryFileDAO repositoryFileDAO;
//	@Autowired
//	StoricoWorkflowDAO storicoWorkflowDAO;
//	@Autowired
//	NotificaDAO notificaDAO;
//	@Autowired
//	AzioneDAO azioneDAO;
	
	public void salvaProtocollo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		/* i dati di protocollo in uscita vengono salvati in storicoEntity al passo precedente
		 */

	}



}
