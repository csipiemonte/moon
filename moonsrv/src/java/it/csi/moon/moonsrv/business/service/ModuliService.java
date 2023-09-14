/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.CampoModulo;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.ReportVerificaAttributiModulo;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi ai moduli
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ModuliService {
	
	static final String DEFAULT_FIELDS_MODULO_BY_ID = "struttura,attributiWCL";
	
	public List<Modulo> getElencoModuli(ModuliFilter filter) throws ItemNotFoundBusinessException, BusinessException;
	public Modulo getModuloById(Long idModulo, Long idVersioneModulo, String fields) throws ItemNotFoundBusinessException, BusinessException;
	public Modulo getModuloByCodice(String codiceModulo, String versione) throws ItemNotFoundBusinessException, BusinessException;
	public Modulo getModuloPubblicatoByCodice(String codiceModulo) throws ItemNotFoundBusinessException, BusinessException;
	public String getStrutturaByIdStruttura(Long idStruttura) throws ItemNotFoundBusinessException, BusinessException;
	public Modulo insertModulo(UserInfo user, Modulo modulo) throws BusinessException;
	public Modulo updateModulo(UserInfo user, Modulo modulo) throws BusinessException;
	public Modulo cambiaStato(UserInfo user, Long idModulo, Long idVersioneModulo, DecodificaStatoModulo newStato) throws BusinessException;
	public List<CampoModulo> getCampiModulo(Long idModulo, Long idVersioneModulo, String type) throws ItemNotFoundBusinessException, BusinessException;
	public List<CampoModulo> getCampiModulo(Long idModulo, Long idVersioneModulo, String onlyFirstLevel, String type) throws ItemNotFoundBusinessException, BusinessException;
	public List<CampoModulo> getCampiDatiAzione(Long idDatiAzione, String onlyFirstLevel, String type) throws ItemNotFoundBusinessException, BusinessException;
	public Long getIdModuloByCodice(String codiceModulo) throws ItemNotFoundBusinessException, BusinessException;
	public ReportVerificaAttributiModulo validaAttributiModulo(Long idModulo, String categoriaAttributi) throws ItemNotFoundBusinessException, BusinessException;
}
