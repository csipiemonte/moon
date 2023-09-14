/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.dirittostudio;


import java.util.List;

import it.csi.moon.commons.dto.extra.istat.Comune;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.dto.ScuolaEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle scuole Modulo Voucher studio
 * 
 * @author Danilo
 * 
 * @since 1.0.0
 */
public interface ScuoleDAO {


	public List<ScuolaEntity> find() throws DAOException;

	public ScuolaEntity findById(String id) throws ItemNotFoundDAOException, DAOException;
	
	public List<ScuolaEntity> findByCodiceIstatIdOrdine(String codiceIstat,Integer idOrdineScuola) throws DAOException;
		
	public List<ScuolaEntity> findByCodiceIstat(String codiceIstat) throws DAOException;
	
	public List<ScuolaEntity> findByIdOrdine(Integer idOrdineScuola) throws DAOException;
	
	public List<ScuolaEntity> findByIdTipo(Integer idTipoScuola) throws DAOException;
	
	public List<Comune> findByCodiceIstatProvincia(String codiceIstat) throws DAOException;
	
	public List<ScuolaEntity> findV2() throws DAOException;
	
	public List<ScuolaEntity> findByCodiceIstatIdOrdineV2(String codiceIstat,Integer idOrdineScuola) throws DAOException;
	
	public List<ScuolaEntity> findByCodiceIstatIdOrdineAnnoScolasticoV2(String codiceIstat,Integer idOrdineScuola, String annoScolastico) throws DAOException;
	
	public List<ScuolaEntity> findByCodiceIstatInIdOrdineAnnoScolasticoV2(List<String> codiciIstat,Integer idOrdineScuola, String annoScolastico) throws DAOException;
	
	public List<ScuolaEntity> findByCodiceIstatNotInIdOrdineAnnoScolasticoV2(List<String> codiciIstat,Integer idOrdineScuola, String annoScolastico) throws DAOException;
	
	public List<ScuolaEntity> findByCodiceIstatV2(String codiceIstat) throws DAOException;
	
	public List<ScuolaEntity> findByIdOrdineV2(Integer idOrdineScuola) throws DAOException;
	
	public List<ScuolaEntity> findByIdTipoV2(Integer idTipoScuola) throws DAOException;	
	
	public List<Comune> findByCodiceIstatProvinciaV2(String codiceIstat) throws DAOException;
	


}
