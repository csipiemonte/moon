/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import it.csi.moon.commons.dto.AllegatiSummary;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.dto.ResponseOperazioneMassiva;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Metodi di business relativi alle istanze
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface IstanzeService {
	public List<Istanza> getElencoIstanze(IstanzeFilter filter, Optional<IstanzeSorter> sorter) throws BusinessException;	
	public Istanza getIstanzaById(Long idIstanza) throws BusinessException;
	public Istanza getIstanzaById(Long idIstanza, String fields) throws BusinessException;
	public Istanza getIstanzaByCd(String codiceIstanza) throws BusinessException;
	public Istanza getIstanzaByCd(String codiceIstanza, String fields) throws BusinessException;
	public String getIstanzaDataById(Long idIstanza) throws BusinessException;
//	public Istanza getInitIstanzaByIdModulo(UserInfo user, Long idModulo) throws BusinessException;
	public Istanza getInitIstanza(IstanzaInitParams initParams) throws BusinessException;
//	public Istanza saveIstanza(UserInfo user, Istanza istanza) throws BusinessException;
//	public Istanza saveIstanza(UserInfo user, Long idIstanza, Istanza body) throws BusinessException;
//	public Istanza deleteIstanza(UserInfo user, Long idIstanza) throws BusinessException;
//	public Istanza patchIstanza(UserInfo user, Long idIstanza, Istanza partialIstanza) throws BusinessException;
	public void inviaEmail(Long idIstanza) throws BusinessException;
	public void rinviaEmail(Long idIstanza, String dest);
	public void rinviaEmails(Long idTag, String dest);
	public void estraiDichiaranteUpdate(Long idIstanza);
	
	public String downloadAllFileIstanzeByIdModulo(Long idModulo, Date dataDal, Date dataAl) throws IOException;
	public String downloadRepositoryFileByIdModulo(Long idModulo, Date dataDal, Date dataAl);
	public String downloadRepositoryFileByIdIstanza(Long idIstanza);
	
	public ResponseOperazioneMassiva generaHashUnivocitaMassivo(Long idTag);
	
	public Istanza completaIstanzaEpay(Istanza istanza);
	
	public AllegatiSummary getAllegatiSummary(Long idIstanza) throws BusinessException;
}
