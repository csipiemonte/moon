/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.AllegatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.AllegatoLazyEntity;
import it.csi.moon.moonbobl.dto.moonfobl.AllegatiSummary;
import it.csi.moon.moonbobl.dto.moonfobl.CampoModuloFormioFileName;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

public interface AllegatoDAO {
	
	public AllegatoEntity findById(Long id) throws ItemNotFoundDAOException, DAOException;
	public AllegatoEntity findByFormIoNameFile(String formioNameFile) throws ItemNotFoundDAOException, DAOException;
	public AllegatoEntity findByCodice(String codiceFile) throws ItemNotFoundDAOException, DAOException;
	public AllegatoEntity findByIdIstanzaCodice(Long idIstanza, String codiceFile) throws ItemNotFoundDAOException, DAOException;
	
	public AllegatiSummary selectSummary(Long idIstanza) throws DAOException;
	
	public List<AllegatoEntity> findByIdIstanza(Long idIstanza) throws DAOException;
	public List<AllegatoLazyEntity> findLazyByIdIstanza(Long idIstanza) throws DAOException;
	
	public Long insert(AllegatoEntity allegato) throws DAOException;
	public int delete(Long allegato) throws DAOException;
	public int update(AllegatoEntity allegato) throws DAOException;
	public int updateIdIstanza(CampoModuloFormioFileName campoFormioNomeFile, Long idIstanza) throws DAOException;
	public int updateFlFirmato(AllegatoLazyEntity entity) throws DAOException;
	public int updateFlEliminato(AllegatoEntity entity) throws DAOException;
	public int resetIdIstanza(Long idIstanza) throws DAOException;
	public int deleteAllegatoByNameFormio(String nomeFile) throws DAOException;
	
	public Long insert(AllegatoLazyEntity newAllegato, Long idAllegatoOfContenuto) throws DAOException;
	public int updateContenuto(Long newIdAllegato, byte[] bytes) throws DAOException;
	
}
