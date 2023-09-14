/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;
/**
 * DAO per accesso alle decodifiche della codifica delle istanze di un modulo
 * 
 */
import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.TipoCodiceIstanzaEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

public interface TipoCodiceIstanzaDAO {
	
	public TipoCodiceIstanzaEntity findById(long idTipoCodiceIstanza) throws DAOException;
	public TipoCodiceIstanzaEntity findByCodice(String descCodice) throws DAOException;
	public List<TipoCodiceIstanzaEntity> find() throws DAOException;
	
}
