/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio;

import java.util.List;

import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;


public interface GinevraService {

	//
	// SEDIMI
    public List<Via> getSedimi() throws BusinessException;
    public Via getSedimeById(Long idTipoVia) throws BusinessException;
    
	//
	// VIE
	public List<Via> getVie(String nome, Long idComune) throws BusinessException;
	public Via getViaById(Long idComune, Long idVia) throws BusinessException;
	
	//
	// CIVICI
	public List<Via> getCivici(Long idVia, Long numero) throws BusinessException;
	public Via getCivicoById(Long idCivico) throws BusinessException;
	
}
