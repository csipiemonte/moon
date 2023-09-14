/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.dirittostudio;


import java.util.List;

import it.csi.moon.moonfobl.business.service.impl.dao.extra.dto.PraticaEdiliziaEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle scuole Modulo Voucher studio
 * 
 * @author Alberto
 * 
 * @since 1.0.0
 */
public interface PraticheDAO {

	public PraticaEdiliziaEntity findById(Integer id) throws ItemNotFoundDAOException, DAOException;
	
	public List<PraticaEdiliziaEntity> findByAnnoRegProg(String anno, Integer Registro, String progressivo) throws DAOException;
	
	public List<String> findProgressiviPerRegistroAnno(String anno, Integer registro) throws DAOException;
	
	public PraticaEdiliziaEntity findPraticaByAnnoRegProg(String anno, Integer registro, String progressivo) throws DAOException;

}
