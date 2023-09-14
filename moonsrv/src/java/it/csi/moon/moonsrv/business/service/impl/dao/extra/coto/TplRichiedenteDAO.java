/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.coto;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.TplRichiedenteEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO di accesso ai richiedente di Voucher TPL del comune di Torino
 * 
 * @author Laurent
 * 
 * @since 3.0.0
 */
public interface TplRichiedenteDAO {

	public TplRichiedenteEntity findById(Integer idRichiedente) throws ItemNotFoundDAOException, DAOException;
	public TplRichiedenteEntity findByCfCv(String cfRichiedente, String codiceVoucher) throws ItemNotFoundDAOException, DAOException;
	public Integer insert(TplRichiedenteEntity entity) throws DAOException;
	
}
