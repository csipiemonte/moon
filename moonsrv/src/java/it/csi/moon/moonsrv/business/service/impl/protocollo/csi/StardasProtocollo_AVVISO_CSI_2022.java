/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.protocollo.csi;

import java.text.SimpleDateFormat;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.commons.entity.ProtocolloRichiestaEntity;
import it.csi.moon.moonsrv.business.service.impl.protocollo.ProtocolloParams;
import it.csi.moon.moonsrv.business.service.impl.protocollo.StardasProtocollo;
import it.csi.moon.moonsrv.business.service.impl.protocollo.util.StardasMetadatiTypeBuilder;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.Constants;
import it.csi.stardas.cxfclient.MetadatiType;

public class StardasProtocollo_AVVISO_CSI_2022 extends StardasProtocollo {

	private static final String CLASS_NAME = "StardasProtocollo_AVVISO_M01_2022";
	
	protected final SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
	
	@Override
	protected MetadatiType retrieveMetadati(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato) {
		
		// Recupero Info dall'istanza
		String dataPresentazione = sdf.format(istanza.getCreated());
		String nomeCognome = getNomeCognomeDichiarante(istanza);
		String anno = sdf_yyyy.format(istanza.getCreated());
		String codiceModulo = istanza.getModulo().getCodiceModulo();
		String codiceFascicolo = "";
		String nomeAvviso = "";
		String autoreFisicoDoc = nomeCognome;
		if(Constants.CODICE_MODULO_AVVISO_03_2023.equalsIgnoreCase(codiceModulo) 
				|| Constants.CODICE_MODULO_AVVISO_02_2023.equalsIgnoreCase(codiceModulo) 
				|| Constants.CODICE_MODULO_AVVISO_04_2023.equalsIgnoreCase(codiceModulo)) {
			autoreFisicoDoc = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey("anagrafica.candidato.cognome")
					+ " " + getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey("anagrafica.candidato.nome")
					+ " " + getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey("anagrafica.candidato.codiceFiscale");;
		}
		switch (codiceModulo) {
		case "AVVISO_M01_2022":
			codiceFascicolo = "3/2022A";
			nomeAvviso = "Candidatura per Avviso n. M01/2022 - Cloud developer";
			break;
		case "AVVISO_02_2022":
			codiceFascicolo = "4/2022A";
			nomeAvviso = "Candidatura per Avviso 02/2022 - Esperti di Cloud, Cybersecurity e Infrastrutture";
			break;
		case "AVVISO_03_2022":
			codiceFascicolo = "6/2022A";
			nomeAvviso = "Candidatura per Avviso 03/2022 - Esperti in Architecture Product, Program e Service Management";
			break;
		case "AVVISO_04_2022":
			codiceFascicolo = "7/2022A";
			nomeAvviso = "Candidatura per Avviso 04/2022 - Esperti in Project e Service Management";
			break;
		case "AVVISO_UT01_2022":
			codiceFascicolo = "5/2022A";
			nomeAvviso = "Candidatura per Avviso UT 01/2022 - Specialista Ufficio Tecnico";
			break;
		case "AVVISO_M02_2022":
			codiceFascicolo = "10/2022A";
			nomeAvviso = "Candidatura per Avviso n. M02/2022 - Neo laureati apprendisti";
			break;
		case "AVVISO_06_2022":
			codiceFascicolo = "11/2022A";
			nomeAvviso = "Candidatura per Avviso 06/2022 - n. 3 Service Specialist";
			break;
		case "AVVISO_05_2022":
			codiceFascicolo = "12/2022A";
			nomeAvviso = "Candidatura per Avviso 05/2022 - n. 6 esperti in Architecture, Design e Software development";
			break;
		case "AVVISO_07_2022":
			codiceFascicolo = "13/2022A";
			nomeAvviso = "Candidatura per Avviso 07/2022 - n.2 Data Base Administrator";
			break;
		case "AVVISO_08_2022":
			codiceFascicolo = "15/2022A";
			nomeAvviso = "Candidatura per Avviso 08/2022 -  n. 1 esperto in Digital Product & Service Management";
			break;
		case "AVVISO_09_2022":
			codiceFascicolo = "18/2022A";
			nomeAvviso = "Candidatura per Avviso 09/2022 - Analisti ed esperti di materia/servizio";
			break;
		case "AVVISO_10_2022":
			codiceFascicolo = "19/2022A";
			nomeAvviso = "Candidatura per Avviso 10/2022 - Esperti in ambito Infrastructure Cloud e Cybersecurity";
			break;
		case "AVVISO_11_2022":
			codiceFascicolo = "20/2022A";
			nomeAvviso = "Candidatura per Avviso 11/2022 - Esperti in Project e Service Management";
			break;
		case "AVVISO_12_2022":
			codiceFascicolo = "21/2022A";
			nomeAvviso = "Candidatura per Avviso 12/2022 - Esperti in Architecture, Design e Product Development";
			break;
		case "CSI_CONF_INC":
			codiceFascicolo = "";
			nomeAvviso = "Candidatura per CSI_CONF_INC - Conferimento di incarico professionale esterno";
			break;
		case "CSI_ODV_1_2023_AP":
			codiceFascicolo = "5/2023A";
			nomeAvviso = "Avviso 1/2023 Procedura comparativa per il conferimento di n. 2 incarichi di componente esterno dell'Organismo di Vigilanza ex, D. Lgs.231/2001 del CSI- Piemonte_ Avvocato Penalista";
			break;
		case "CSI_ODV_1_2023_DC":
			codiceFascicolo = "6/2023A";
			nomeAvviso = "Avviso 1/2023 Procedura comparativa per il conferimento di n.2 incarichi di componente esterno dell'Organismo di Vigilanza ex D.Lgs 231/2001 del CSI-Piemonte_ Dottore Commercialista";
			break;
			
		case "AVVISO_01_2023":
			codiceFascicolo = "7/2023A";
			nomeAvviso = "Avviso 01_2023 - n.1 Apprendista in ambito Amministrazione Finanza e fiscalità";
			break;
		case "AVVISO_02_2023":
			codiceFascicolo = "4/2023A";
			nomeAvviso = "Avviso 02_2023 - n.1 HR Business Partner";
			break;
		case "AVVISO_03_2023":
			codiceFascicolo = "8/2023A";
			nomeAvviso = "Avviso 03_2023 - n.1 Specialista Procurement";
			break;
		case "AVVISO_04_2023":
			codiceFascicolo = "9/2023A";
			nomeAvviso = "Avviso 04_2023 - n.1 Specialista Ufficio Tecnico";
			break;
		default:
			throw new BusinessException();
		}

		MetadatiType result = new StardasMetadatiTypeBuilder()
				.oggettoDocumento(nomeCognome + " " + nomeAvviso)
				.paroleChiaveDocumento(istanza.getCodiceIstanza() + " " + nomeCognome)
				.dataCronicaDocumento(dataPresentazione)
				.autoreFisicoDocumento(autoreFisicoDoc)
				.mittenteDenominazione(nomeCognome + "-" + istanza.getCodiceFiscaleDichiarante())
				.codiceFascicolo(codiceFascicolo)
				.descrizioneVolSerieFascicoli(anno)
				.annotazioneFormale2("Istanza MOOn " +istanza.getCodiceIstanza())
				.build();
		
		LOG.debug("[" + CLASS_NAME + "::retrieveMetadati] OUT result=" + result);
		return result;
	}

	@Override
	protected MetadatiType retrieveMetadatiAllegato(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato, AllegatoLazyEntity allegato) {

		MetadatiType result = new MetadatiType();
	
		String dataPresentazione = sdf.format(istanza.getModified());
		String nomeCognome = getNomeCognomeDichiarante(istanza);
		String nomeFile = getFileName(allegato.getNomeFile());
		result.getMetadato().add(makeNewMetadato("OGGETTO_DOCUMENTO", nomeFile)); 
		
		String parolaChiave = istanza.getCodiceIstanza() + " " + nomeFile;
		if (nomeFile.equals("DISTINTA_DI_PRESENTAZIONE")) {
			parolaChiave = istanza.getCodiceIstanza() + " " + nomeCognome;
		}
		result.getMetadato().add(makeNewMetadato("PAROLE_CHIAVE_DOCUMENTO", parolaChiave)); 
		result.getMetadato().add(makeNewMetadato("AUTORE_FISICO_DOCUMENTO", nomeCognome ));
		result.getMetadato().add(makeNewMetadato("DATA_CRONICA_DOCUMENTO", dataPresentazione));
		
		LOG.debug("[" + CLASS_NAME + "::retrieveMetadatiAllegato] OUT result=" + result);
		return result;
	}
	
}
