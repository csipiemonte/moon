/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.coto;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.TplBeneficiarioEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO di accesso ai beneficiari del Voucher TPL del comune di Torino
 * 
 * @author Laurent
 * 
 * @since 3.0.0
 */
public interface TplBeneficiarioDAO {

	public TplBeneficiarioEntity findById(String cfBeneficiario) throws ItemNotFoundDAOException, DAOException;
	public TplBeneficiarioEntity findByCfCv(String cfBeneficiario, String codiceVoucher) throws ItemNotFoundDAOException, DAOException;
	public Integer insert(TplBeneficiarioEntity entity) throws DAOException;
	
}
