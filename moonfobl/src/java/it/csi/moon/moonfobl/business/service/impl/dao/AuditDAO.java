/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import it.csi.moon.commons.entity.AuditEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;


public interface AuditDAO {

	public int insertAuditEntity(AuditEntity auditEntity) throws DAOException;
}
