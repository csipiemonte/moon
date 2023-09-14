/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloProgressivoEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class CodiceIstanzaServiceImpl {
	
	private final static String CLASS_NAME = "CodiceIstanzaServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	  
	@Autowired
	ModuloProgressivoDAO moduloProgressivoDAO;
	
	/**
	 * Gestione Singleton
	 */
	private static CodiceIstanzaServiceImpl istance=null;
	private CodiceIstanzaServiceImpl() {}
	public static synchronized CodiceIstanzaServiceImpl getInstance() {
		if(istance==null)
			istance = new CodiceIstanzaServiceImpl();
		return istance;
	}

	/**
	 * Genera il codiceIstanza univoco
	 * Usato durante l'inserimento di una nuova istanza di un modulo
	 *  - 000015
	 *  - 2020-0000123
	 *  
	 * @param modulo
	 * @return String codiceInstanza
	 * @throws BusinessException
	 */
	public String generate(ModuloEntity modulo) throws BusinessException {
		String codiceInstanza = null;
		if(modulo == null){
			return null;
		}

		try {
			// Sol1
//TODO findByIdModuloForUpdate(idModulo)  serivirebbe  fare la ricerca findByIdModuloForUpdate(idModulo, tipoProgressivo)
//     findByIdModuloForUpdate deve tornare solo 1 record, nel caso di possibile multi righe (es.ANNO) deve tornare l'ultimo anno conosciuto
			ModuloProgressivoEntity progressivoE = moduloProgressivoDAO.findByIdModuloForUpdate(modulo.getIdModulo());
			if (progressivoE.getAnnoRiferimento()!=null) {
				Integer year = Calendar.getInstance().get(Calendar.YEAR);
				if (!year.equals(progressivoE.getAnnoRiferimento())) {
					progressivoE.setProgressivo(1L);
					progressivoE.setAnnoRiferimento(year);
				} else {
					progressivoE.setProgressivo(progressivoE.getProgressivo()+1);
				}
				codiceInstanza = year + "-" + padProgressivo(progressivoE);
			} else {
				progressivoE.setProgressivo(progressivoE.getProgressivo()+1);
				codiceInstanza = padProgressivo(progressivoE);
			}
			moduloProgressivoDAO.update(progressivoE);
			
			// Sol2
//			codiceInstanza = moduloProgressivoDAO.generateCodiceIstanzaForIdModulo(modulo.getIdModulo());
			
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		}
		
		return codiceInstanza;
	}

	/**
	 * Padding di un numero con dei zero davanti
	 * @param progressivoE
	 * @return
	 */
	private String padProgressivo(ModuloProgressivoEntity progressivoE) {
		return String.format("%1$" + progressivoE.getLunghezza() + "s", progressivoE.getProgressivo()).replace(' ', '0');
	}
}
