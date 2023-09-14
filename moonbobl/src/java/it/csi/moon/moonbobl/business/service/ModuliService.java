/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service;

import java.util.Date;
import java.util.List;


import it.csi.moon.moonbobl.dto.moonfobl.InitModulo;
import it.csi.moon.moonbobl.dto.moonfobl.InitModuloCambiaStato;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloAttributo;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloVersioneStato;
import it.csi.moon.moonbobl.dto.moonfobl.NuovaVersioneModuloRequest;
import it.csi.moon.moonbobl.dto.moonfobl.StatoModulo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.UtenteModuloAbilitato;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.TooManyItemFoundBusinessException;
import it.csi.moon.moonbobl.util.decodifica.DecodificaStatoModulo;

/**
 * @author franc
 * Metodi di business relativi ai moduli
 */
public interface ModuliService {
	public List<Modulo> getElencoModuliPubblicati(UserInfo user) throws BusinessException;
	public List<Modulo> getElencoModuliAbilitatiCurrentUserEnte(UserInfo user) throws BusinessException;
	public List<Modulo> getElencoModuliAbilitatiCurrentUserEnte(UserInfo user, String onlyLastVersione) throws BusinessException;
	public List<Modulo> getElencoModuliAbilitati(UserInfo user) throws BusinessException;
	public List<Modulo> getElencoModuliAbilitati(UserInfo user, Long idModulo, String onlyLastVersione, String pubblicatoBO) throws BusinessException;
	public List<Modulo> getElencoModuliAbilitati(UserInfo user, Long idModulo, String onlyLastVersione, String otherIdentificativoUtente, String pubblicatoBO) throws BusinessException;
	public Modulo getModuloById(UserInfo user, Long idModulo, Long idVersioneModulo, String fields) throws ItemNotFoundBusinessException, BusinessException;
	public Modulo getModuloByCodice(String codiceModulo, String versione) throws ItemNotFoundBusinessException, BusinessException;
	public String getStrutturaByIdStruttura(UserInfo user, Long idStruttura) throws ItemNotFoundBusinessException, BusinessException;
	public Modulo insertModulo(UserInfo user, Modulo modulo) throws BusinessException;
	public Modulo updateModulo(UserInfo user, Modulo modulo) throws BusinessException;
	public Modulo cambiaStato(UserInfo user, Long idModulo, Long idVersioneModulo, DecodificaStatoModulo newStato) throws BusinessException;
	public Modulo cambiaStato(UserInfo user, Long idModulo, Long idVersioneModulo, DecodificaStatoModulo newStato, Date inDataOra) throws BusinessException;
//	public Modulo pubblicaModulo(UserInfo user, Long idModulo) throws BusinessException;
	public boolean verificaAbilitazioneModulo(Long idModulo, UserInfo user) throws BusinessException;
	public Modulo updateStruttura(UserInfo user, Long idModulo, Long idVersioneModulo, String newStruttua) throws BusinessException;
	public List<ModuloAttributo> getAttributiModulo(Long idModulo) throws BusinessException;
	public String getObjAttributiModuloBO(Long idModulo) throws BusinessException;
	public List<StatoModulo> getElencoStatiModulo(String codiceProvenienza, String codiceDestinazione) throws BusinessException;
	public InitModulo getInitNuovaVersioneModuloById(UserInfo user, Long idModulo) throws ItemNotFoundBusinessException, BusinessException;
	public void deleteModulo(UserInfo user, Long idModulo, Long idVersioneModulo) throws ItemNotFoundBusinessException, BusinessException;
	public void savePortaliModulo(UserInfo user, Long idModulo, Long idVersioneModulo, List<Long> nuoviPortali) throws ItemNotFoundBusinessException, BusinessException;
	public Modulo nuovaVersione(UserInfo user, Long idModulo, NuovaVersioneModuloRequest body) throws ItemNotFoundBusinessException, BusinessException;
	public InitModuloCambiaStato getInitCambiaStatoById(UserInfo user, Long idModulo, Long idVersioneModulo) throws ItemNotFoundBusinessException, BusinessException;
	public List<ModuloVersioneStato> getVersioniModuloById(Long idModulo, String fields);
	public List<ModuloAttributo> aggiornaInserisciAttributiGenerali(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user);
	public List<ModuloAttributo> aggiornaInserisciAttributiEmail(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user);
	public List<ModuloAttributo> aggiornaInserisciAttributiNotificatore(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user);
	public List<ModuloAttributo> aggiornaInserisciAttributiProtocollo(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user);
	public List<ModuloAttributo> aggiornaInserisciAttributiCosmo(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user);
	public List<ModuloAttributo> aggiornaInserisciAttributiAzione(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user);
	public List<ModuloAttributo> aggiornaInserisciAttributiEstraiDichiarante(Long idModulo, ModuloAttributo[] attributi, UserInfo user);
	public List<ModuloAttributo> aggiornaInserisciAttributiCrm(Long idModulo, ModuloAttributo[] attributi, UserInfo user);
	public List<ModuloAttributo> aggiornaInserisciAttributiEpay(Long idModulo, ModuloAttributo[] attributi, UserInfo user);
	public List<UtenteModuloAbilitato> getUtentiAbilitati(Long idModulo, String fields);
	public boolean hasProtocolloParameters(Long idModulo);
	
	public Modulo getModuloPubblicatoByCodice(String codiceModulo) throws ItemNotFoundBusinessException, TooManyItemFoundBusinessException, BusinessException;
	public boolean hasProtocolloBo(Long idModulo) throws BusinessException;
}
