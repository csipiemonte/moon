/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.Optional;

import it.csi.moon.commons.entity.IstanzaPdfEntity;

public interface IstanzaPdfDAO {
	
	public IstanzaPdfEntity findById(Long id);
	public Optional<IstanzaPdfEntity> findByIdIstanza(Long idIstanza);
	
	public Long insert(IstanzaPdfEntity entity);
	public int update(IstanzaPdfEntity entity);
	public int delete(Long id);
	public int deleteByIdIstanza(Long idIstanza);
	public int updateUuidIndex(IstanzaPdfEntity entityPdf);
	
}
