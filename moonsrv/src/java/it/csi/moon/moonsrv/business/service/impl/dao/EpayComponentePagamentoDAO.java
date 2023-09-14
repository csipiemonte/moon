/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.EpayComponentePagamentoEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tabella moon_ep_t_componente_pagamento EPAY
 * <br>
 * <br>Tabella moon_ep_t_componente_pagamento
 * <br>PK: idComponentePagamento
 * <br>
 * <br>Tabella : moon_ep_t_componente_pagamento
 * 
 * @see EpayComponentePagamentoEntity
 * 
 * @author Danilo
 * 
 */
public interface EpayComponentePagamentoDAO {

	public List<EpayComponentePagamentoEntity> findByIdModulo(Long idModulo) throws ItemNotFoundDAOException, DAOException;
	public Long insert(EpayComponentePagamentoEntity entity);
	public int update(EpayComponentePagamentoEntity entity);
	
}
