/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEnteAbilitatoFlatDTO;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.dto.extra.istat.CodiceNome;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
 * 
 * @see IstanzaEntity
 * @see IstanzaCronologiaStatiEntity
 * @see IstanzaDatiEntity
 * 
 * @author Laurent
 *
 */
public interface UtenteDAO {
	public UtenteEntity findById(Long idUtente) throws ItemNotFoundDAOException, DAOException;
	public UtenteEntity findByIdentificativoUtente(String identificativoUtente) throws ItemNotFoundDAOException, DAOException;
	public List<UtenteEntity> find() throws DAOException;
	public Long insert(UtenteEntity entity) throws DAOException;
	public int delete(UtenteEntity entity) throws DAOException;
	public int delete(Long idUtente) throws DAOException;
	public int update(UtenteEntity entity) throws DAOException;
	public List<CodiceNome> findComuniAbilitatiUtente(String codice_fiscale) throws DAOException;
	public List<CodiceNome> findEntiAbilitatiUtente(String codice_fiscale) throws DAOException;
	public List<UtenteEnteAbilitatoFlatDTO> findUtentiEnteAbilitatoByIdEnte(Long idEnte, Long idUtente) throws DAOException;
	public List<UtenteEnteAbilitatoFlatDTO> findUtentiEnteNoAbilitatoByIdEnte(Long idEnte/*, Long idUtente*/) throws DAOException;
	public UtenteEntity findByUsrPwd(String user,String pwd) throws ItemNotFoundDAOException, DAOException;
	public List<UtenteEntity> utentiAreaByCf(String codiceFiscale) throws DAOException;
}
