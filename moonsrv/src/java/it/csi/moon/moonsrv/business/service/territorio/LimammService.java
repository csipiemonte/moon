/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio;

import java.util.List;

import it.csi.moon.commons.dto.extra.territorio.regp.ComuneLA;
import it.csi.moon.commons.dto.extra.territorio.regp.ProvinciaLA;
import it.csi.moon.commons.dto.extra.territorio.regp.RegioneLA;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;


public interface LimammService {

	//
	// REGIONI
    public List<RegioneLA> getRegioni() throws BusinessException;
    public RegioneLA getRegioneById(Long idRegione) throws ItemNotFoundBusinessException, BusinessException;
    public RegioneLA getRegioneByIstat(String istat) throws ItemNotFoundBusinessException, BusinessException;

	//
	// PROVINCE
	public List<ProvinciaLA> getProvince() throws BusinessException;
	public ProvinciaLA getProvinciaById(Long idProvincia) throws ItemNotFoundBusinessException, BusinessException;
	public ProvinciaLA getProvinciaByIstat(String istat) throws ItemNotFoundBusinessException, BusinessException;
	public List<ProvinciaLA> getProvinceByRegione(Long idRegione) throws BusinessException;
	public List<ProvinciaLA> getProvinceByIstatRegione(String istatRegione) throws BusinessException;
	public ProvinciaLA getProvinciaByRegione(Long idRegione, Long idProvincia) throws ItemNotFoundBusinessException, BusinessException;
	public ProvinciaLA getProvinciaByIstatRegione(String istatRegione, String istatProvincia) throws ItemNotFoundBusinessException, BusinessException;
	
	//
	// COMUNI
	public List<ComuneLA> getComuni() throws BusinessException;
	public ComuneLA getComuneById(Long idComune) throws ItemNotFoundBusinessException, BusinessException;
	public ComuneLA getComuneByIstat(String istat) throws ItemNotFoundBusinessException, BusinessException;
	public List<ComuneLA> getComuniByProvincia(Long idProvincia) throws BusinessException;
	public List<ComuneLA> getComuniByIstatProvincia(String istatProvincia) throws ItemNotFoundBusinessException, BusinessException;
	public List<ComuneLA> getComuniByRegioneProvincia(Long idRegione, Long idProvincia) throws ItemNotFoundBusinessException, BusinessException;
	public List<ComuneLA> getComuniByIstatRegioneProvincia(String istatRegione, String istatProvincia) throws ItemNotFoundBusinessException, BusinessException;
	public ComuneLA getComuneByRegioneProvincia(Long idRegione, Long idProvincia, Long idComune) throws ItemNotFoundBusinessException, BusinessException;
	public ComuneLA getComuneByIstatRegioneProvincia(String istatRegione, String istatProvincia, String istatComune) throws ItemNotFoundBusinessException, BusinessException;
	
}
