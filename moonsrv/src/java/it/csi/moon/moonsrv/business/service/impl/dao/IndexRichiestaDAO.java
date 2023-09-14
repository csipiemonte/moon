/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import it.csi.moon.commons.entity.IndexRichiestaEntity;

public interface IndexRichiestaDAO {

	public Long insert(IndexRichiestaEntity richiestaEntity);

	public int update(IndexRichiestaEntity richiestaEntity);

}
