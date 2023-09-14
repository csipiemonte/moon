/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio;

import java.util.List;

import it.csi.apimint.toponomastica.v1.dto.CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse;
import it.csi.apimint.toponomastica.v1.dto.CivicoLight;
import it.csi.apimint.toponomastica.v1.dto.Piano;
import it.csi.apimint.toponomastica.v1.dto.UiuLight;
import it.csi.apimint.toponomastica.v1.dto.ViaLight;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
* DAO Toponomastica del Comune di Torino via servizi REST 
* - via applogic
* - via API Manager Outer
* 
* @author Francesco Zucaro
* @author Laurent Pissard
* 
* @since 1.0.0
*/
public interface ToponomasticaApiRestDAO {

	/** elencaVie: /vie */
	public List<ViaLight> elencaVie() throws DAOException;
	
	/** elencaNumeriRadiceDiUnaVia /vie/{idvia}/civicilight/numeriRadice */
	public List<Integer> elencaNumeriRadiceDiUnaVia(Integer idVia) throws DAOException;
	

	/** elencaCiviciDiUnaViaLight /vie/{idvia}/civicilight */
	public List<CivicoLight> elencaCiviciDiUnaVia(Integer idVia) throws DAOException;

	/** elencaCiviciDiUnaViaLightNumero /vie/{idvia}/civicilight/{numeriRadice} */
	public List<CivicoLight> elencaCiviciDiUnaViaNumero(Integer idVia, Integer numero) throws DAOException;
	
	/** civico per id */
	public it.csi.apimint.toponomastica.v1.dto.Civico findCivicoById(Integer codiceCivico) throws DAOException;
	
	/** elencaUiuLightDiUnCivico /civico/{idcivico}/uiulight */
	public List<UiuLight> elencaUiuLightDiUnCivico(Integer idCivico) throws DAOException;

	/** elencaPiani /uiu/piani */
	public List<Piano> elencaPiani() throws DAOException;

	/** cercaIByQuadruplaCatastale /uiu/piani */
	public List<CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse> cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale(String foglio, String numero, String subalterno) throws DAOException;
}
 