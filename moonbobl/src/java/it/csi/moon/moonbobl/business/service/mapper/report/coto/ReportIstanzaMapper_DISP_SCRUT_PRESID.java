/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper.report.coto;

import java.util.List;

import org.apache.log4j.Logger;

import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapper;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapperDefault;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaEstratta;
import it.csi.moon.moonbobl.util.LoggerAccessor;



/**
 * Contruttore di oggetto JSON per MOOnPrint
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class ReportIstanzaMapper_DISP_SCRUT_PRESID extends ReportIstanzaMapperDefault implements ReportIstanzaMapper {
	
	private final static String CLASS_NAME = "PrintIstanzaMapper_DISP_SCRUT_PRESID";
	private final static Logger log = LoggerAccessor.getLoggerBusiness();
	private static final char CSV_SEPARATOR = ';';
	
    /**
     * Remap l'istanza con il suo modulo  in un oggetto per MOOnPrint
     * 
     * @param istanza
     * @param strutturaEntity
     * 
     * @return MoonprintDocument che contiene Module.Document di MOOnPrint
     * 
     * @throws Exception 
     */
	public List<String> remap(List<Istanza> elenco) throws Exception {
		/*
		final String DEFAULT_VALUE = "";

		StringBuilder sb = new StringBuilder();
		
		// riga di intestazione
		sb.append("CODICE PROV").append(CSV_SEPARATOR);
		sb.append("PROVINCIA").append(CSV_SEPARATOR);
		sb.append("CODICE COMUNE").append(CSV_SEPARATOR);
		sb.append("COMUNE").append(CSV_SEPARATOR);
		sb.append("STRUTTURE PRESENTI").append(CSV_SEPARATOR);
		sb.append("DENOMINAZIONE 0-2").append(CSV_SEPARATOR);
		sb.append("TIPOLOGIA 0-2").append(CSV_SEPARATOR);
		sb.append("CAPACITA 0-2").append(CSV_SEPARATOR);
		sb.append("FREQUENTANTI 0-2").append(CSV_SEPARATOR);
		sb.append("AUTORIZZAZIONE 0-2").append(CSV_SEPARATOR);
		sb.append("DENOMINAZIONE 3-6").append(CSV_SEPARATOR);
		sb.append("TIPOLOGIA 3-6").append(CSV_SEPARATOR);
		sb.append("SEZIONI 3-6").append(CSV_SEPARATOR);
		sb.append("FREQUENTANTI 3-6").append(CSV_SEPARATOR);
		// Metadata
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		localWriter.setMetadataDataPresentazione(df.format(istanza.getCreated()));
		localWriter.setMetadataNumeroIstanza(istanza.getCodiceIstanza());
		localWriter.setMetadataQrContent(istanza.getCodiceIstanza());
		localWriter.setMetadataHeader(istanza.getModulo().getOggettoModulo(), "", "Inviata il: %%dataPresentazione%%");
		localWriter.setMetadataFooter("Istanza n.: %%numeroIstanza%%", null, null);
		
		// Lettura delle due strutture variabile
		JsonNode istanzaJsonNode = readIstanzaData(istanza);
		JsonNode data = istanzaJsonNode.get("data");
		

		// Richiedente		
		if (data.has("richiedente")) {
			
			localWriter.addSection("Richiedente", "");
			
			JsonNode richiedente = data.get("richiedente");
			
			localWriter.addSubSection("Dati anagrafici", "");			
			localWriter.addItem("Nome", richiedente.has("nome") ? richiedente.get("nome").getTextValue() : DEFAULT_VALUE);
			localWriter.addItem("Cognome", richiedente.has("cognome") ? richiedente.get("cognome").getTextValue() : DEFAULT_VALUE);
			localWriter.addItem("Codice fiscale", richiedente.has("codiceFiscale") ? richiedente.get("codiceFiscale").getTextValue() : DEFAULT_VALUE );			
			
			localWriter.addSubSection("Residenza", "");

			if (richiedente.has("provinciaResidenza")) {
				localWriter.addItem("Provincia",richiedente.get("provinciaResidenza").get("nome").getTextValue());
			}
			
			if (richiedente.has("comuneResidenza")) {
				localWriter.addItem("Comune",richiedente.get("comuneResidenza").get("nome").getTextValue());
			}
						
			localWriter.addItem("Indirizzo", richiedente.has("indirizzoResidenza") ? richiedente.get("indirizzoResidenza").getTextValue() : DEFAULT_VALUE );
			localWriter.addItem("Civico", richiedente.has("civico") ? richiedente.get("civico").getTextValue() : DEFAULT_VALUE );
			localWriter.addItem("Circoscrizione", richiedente.has("circoscrizione") ? richiedente.get("circoscrizione").getTextValue() : DEFAULT_VALUE );
									
			localWriter.addSubSection("Contatti", "");
			
			localWriter.addItem("Telefono", richiedente.has("telefono") ? richiedente.get("telefono").getTextValue() : DEFAULT_VALUE );
			localWriter.addItem("Cellulare", richiedente.has("cellulare") ? richiedente.get("cellulare").getTextValue() : DEFAULT_VALUE );
			
		}
		
		if (data.has("email")) {
			localWriter.addItem("Email", data.get("email").getTextValue() );
		}
		
		// Dichiarazioni		
		if (data.has("dichiarazioni")) {
			
			localWriter.addSection("Dichiarazioni", "");			
			JsonNode dichiarazioni = data.get("dichiarazioni");	
			
			
			
			if (dichiarazioni.has("flagDPR361") && dichiarazioni.get("flagDPR361") != null && dichiarazioni.get("flagDPR361").asBoolean()) {
				localWriter.addSubSection("Dichiara di non trovarsi in alcuna delle incompatibilità previste dall'art. 38 del D.P.R. 361/1957", "");
				// localWriter.addItem("di non trovarsi in alcuna delle incompatibilità previste dall'art. 38 del D.P.R. 361/1957", "");		
			}
						
			
			if (dichiarazioni.has("giaSvoltoFunzione") && dichiarazioni.get("giaSvoltoFunzione") != null)
			{		
				JsonNode giaSvoltoFunzione = dichiarazioni.get("giaSvoltoFunzione");
				if (! giaSvoltoFunzione.get("nessunaEsperienzaPrecedente").getBooleanValue())
				{
					localWriter.addSubSection("Dichiara di avere svolto in precedenti consultazioni elettorali le funzioni di", "");
					// localWriter.addItem("di avere svolto in precedenti consultazioni elettorali le funzioni di", "");				
									
					if (giaSvoltoFunzione.get("presidenteDiSeggio").getBooleanValue())
					{
						localWriter.addItem("", "Presidente di seggio");
					}
					if (giaSvoltoFunzione.get("scrutatoreDiSeggio").getBooleanValue())
					{
						localWriter.addItem("", "Scrutatore di seggio");
					}
	
					if (giaSvoltoFunzione.get("segretarioDiSeggio").getBooleanValue())
					{
						localWriter.addItem("", "Segretario di seggio");
					}
				}
			}
			
			localWriter.addSubSection("Dichiara di dare la propria disponibilità per l'incarico, in sostituzione della persona formalmente nominata", "");
			
			// localWriter.addItem("di dare la propria disponibilità per l'incarico, in sostituzione della persona formalmente nominata", "");
			if (dichiarazioni.has("ruoloDisponibilita") && dichiarazioni.get("ruoloDisponibilita") != null ) {
				String valore = dichiarazioni.get("ruoloDisponibilita").getTextValue();
				if (valore.equals("scrutatorePresidente")) {
					valore = "scrutatore o presidente";
				}
				localWriter.addItem("", valore );
			}
			

			if (dichiarazioni.has("dichiarazioneDiploma") && dichiarazioni.get("dichiarazioneDiploma") != null && dichiarazioni.get("dichiarazioneDiploma").asBoolean()) {
				localWriter.addSubSection("Dichiara di essere in possesso di un titolo di studio pari ad almeno il diploma di scuola media superiore", "");		
			}
			
			if (dichiarazioni.has("dichiarazioneLicenzaMedia") && dichiarazioni.get("dichiarazioneLicenzaMedia") != null && dichiarazioni.get("dichiarazioneLicenzaMedia").asBoolean()) {
				localWriter.addSubSection("Dichiara di essere in possesso di un titolo di studio pari ad almeno la licenza media", "");		
			}
			
			if ( dichiarazioni.has("categoriaProf") ) {
			   localWriter.addSubSection("Dichiara di appartenere alla categoria professionale: "+ dichiarazioni.get("categoriaProf").getTextValue(), "" );
			}
			
		}		
						
		*/
						
		return null;
	}

	@Override
	public String remapIstanza(IstanzaEstratta istanza) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHeader() {
		// TODO Auto-generated method stub
		return null;
	}



}
