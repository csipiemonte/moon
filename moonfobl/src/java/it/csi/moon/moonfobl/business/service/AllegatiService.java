/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import java.util.List;
import java.util.Map;

import it.csi.moon.commons.dto.AllegatiSummary;
import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.dto.CampoModuloFormioFileName;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;

/*
 * Service per la gestione degli allegati
 * Utilizzato da FileApi
 */
public interface AllegatiService {
	
	public Allegato getById(Long id) throws ItemNotFoundBusinessException, BusinessException;
	public Allegato getByFormIoNameFile(String formioNameFile) throws ItemNotFoundBusinessException, BusinessException;
	public Allegato getByCodice(String codiceFile) throws ItemNotFoundBusinessException, BusinessException;
	public Allegato getByIdIstanzaCodice(Long idIstanza, String codiceFile) throws ItemNotFoundBusinessException, BusinessException;
	
	public List<Allegato> findByIdIstanza(Long idIstanza) throws BusinessException;
	public List<Allegato> findLazyByIdIstanza(Long idIstanza) throws BusinessException;
	
	public Long insert(Allegato allegato) throws BusinessException;
	public int delete(Long allegato) throws BusinessException;
	public int update(Allegato allegato) throws BusinessException;
	public int updateIdIstanza(CampoModuloFormioFileName campoFormioNomeFile, Long idIstanza) throws BusinessException;
	public int resetIdIstanza(Long idIstanza) throws BusinessException;
	public void deleteAllegatoByNameFormio(String nomeFile) throws BusinessException;
	
	public Map<String,String> duplicaAllegatiOfIstanza(long idIstanza, long idModulo, long idVersioneModulo, boolean versioniDiverse, String ipAddress) throws BusinessException;
	
	public AllegatiSummary getAllegatiSummary(Long idIstanza) throws BusinessException;
	
	public String validaBytesContentTypesFully(byte[] bytes, String[] contentTypesArr) throws BusinessException;
	public String validaBytesContentTypesContains(byte[] bytes, String[] filterTypesArr) throws BusinessException;
	public String retrieveContentType(byte[] bytes) throws BusinessException;
	
	public boolean validaBytesSigned(byte[] bytes, String signed, String contentTypeNullable) throws BusinessException;
}
