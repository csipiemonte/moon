/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper.report.coto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.ProvinciaDAO;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapper;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapperDefault;
import it.csi.moon.moonbobl.dto.extra.istat.Provincia;
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
public class ReportIstanzaMapper_ELET_RICH_ACCREDITO extends ReportIstanzaMapperDefault implements ReportIstanzaMapper {
	
	private final static String CLASS_NAME = "PrintIstanzaMapper_ELET_RICH_ACCREDITO";
	private final static Logger log = LoggerAccessor.getLoggerBusiness();
	private static final char CSV_SEPARATOR = ';';
	
	
	@Autowired
	@Qualifier("moon")
	ProvinciaDAO provinciaDAO;
	
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
		final String DEFAULT_VALUE = "";
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
		ArrayList<String> rows = new ArrayList<String>();
		
		/*
		 * export dati presenti sul form IBAN nel formato richiesto da Maggioli 
		 * con prima colonna Numero sequence crescente, seconda colonna Importo vuota, 
		 * terza colonna CodEsecPagamento valore = 53 per tutte le righe, 
		 * poi tutti i campi della form come indicati 
		 * e in fondo i campi aggiuntivi non indicati nel formato .csv (es. mail, telefono)
		 */
		// riga di intestazione
		
		
		//log.debug("[" + CLASS_NAME + ":: begin remap]");
		
		StringBuilder sb = new StringBuilder();
		sb.append("Numero").append(CSV_SEPARATOR);
		sb.append("Importo").append(CSV_SEPARATOR);
		sb.append("CodEsecPagamento").append(CSV_SEPARATOR);  // valore 53
		sb.append("BeneficiarioCognome").append(CSV_SEPARATOR);
		sb.append("BeneficiarioNome").append(CSV_SEPARATOR);
		sb.append("BeneficiarioCodiceFiscale").append(CSV_SEPARATOR);
		sb.append("BeneficiarioIndirizzo").append(CSV_SEPARATOR);
		sb.append("BeneficiarioNumeroCivico").append(CSV_SEPARATOR);
		sb.append("BeneficiarioCap").append(CSV_SEPARATOR);
		sb.append("BeneficiarioLocalita").append(CSV_SEPARATOR);
		sb.append("BeneficiarioCitta").append(CSV_SEPARATOR);
		sb.append("BeneficiarioProvincia").append(CSV_SEPARATOR);
		sb.append("BeneficiarioIban").append(CSV_SEPARATOR);
		
		sb.append("BeneficiarioDataNascita").append(CSV_SEPARATOR);
		sb.append("BeneficiarioLuogoNascita").append(CSV_SEPARATOR);
		sb.append("BeneficiarioProvinciaNascita").append(CSV_SEPARATOR);
		
		sb.append("QuietanzanteCognome").append(CSV_SEPARATOR);
		sb.append("QuietanzanteNome").append(CSV_SEPARATOR);
		sb.append("QuietanzanteCodicefiscale").append(CSV_SEPARATOR);
		sb.append("QuietanzantePartitaIva").append(CSV_SEPARATOR);
		sb.append("QuietanzanteIndirizzo").append(CSV_SEPARATOR);
		sb.append("QuietanzanteNumeroCivico").append(CSV_SEPARATOR);
		sb.append("QuietanzanteCap").append(CSV_SEPARATOR);
		sb.append("QuietanzanteLocalita").append(CSV_SEPARATOR);
		sb.append("QuietanzanteCitta").append(CSV_SEPARATOR);
		sb.append("QuietanzanteProvincia").append(CSV_SEPARATOR);
		sb.append("QuietanzanteIban").append(CSV_SEPARATOR);
		sb.append("QuietanzanteDataNascita").append(CSV_SEPARATOR);
		sb.append("QuietanzanteLuogoNascita").append(CSV_SEPARATOR);
		sb.append("QuietanzanteProvinciaNascita").append(CSV_SEPARATOR);
		sb.append("BIC-SWIFT Beneficiario").append(CSV_SEPARATOR);
		sb.append("BIC-SWIFTQuietanzante").append(CSV_SEPARATOR);
		sb.append("Ruolo svolto").append(CSV_SEPARATOR);
		sb.append("Sezione").append(CSV_SEPARATOR);
		sb.append("Email").append(CSV_SEPARATOR);
		sb.append("Cellulare").append(CSV_SEPARATOR);
		sb.append("Telefono").append(CSV_SEPARATOR);
		sb.append("Codice istanza").append(CSV_SEPARATOR);
		sb.append("Data invio").append(CSV_SEPARATOR);

		rows.add(sb.toString());
		int i = 1;
		if (elenco != null && elenco.size() > 0) {
			for (Istanza  entity : elenco) 
			{
				
				// Lettura del json
				
				log.debug("[" + CLASS_NAME + "::remap id istanza]  - "+ entity.getIdIstanza());
				String datastr = entity.getData().toString();
				
				//JsonNode istanzaJsonNode = readIstanzaData(entity.getData().toString());
				//log.debug("[" + CLASS_NAME + "::remap lunghezza stringa ]  - "+ datastr.length());
				
				try {
					
					JsonNode istanzaJsonNode = readIstanzaData(datastr);
					
					//log.debug("[" + CLASS_NAME + "::parse data ] ");
					JsonNode data = istanzaJsonNode.get("data");
					
					String Numero = String.valueOf(i);
					String Importo = "";
					String CodEsecPagamento = "53";
					String BeneficiarioCognome = "";
					String BeneficiarioNome = "";
					String BeneficiarioCodiceFiscale = "";
					String BeneficiarioIndirizzo = "";
					String BeneficiarioNumeroCivico = "";
					String BeneficiarioCap = "";
					String BeneficiarioLocalita = "";
					String BeneficiarioCitta = "";
					String BeneficiarioProvincia = "";
					String BeneficiarioIban = "";
					
					String BeneficiarioDataNascita =  "";
					String BeneficiarioLuogoNascita = "";			
					String BeneficiarioProvinciaNascita = "";
					
					String Telefono= "";
					String Cellulare = "";
					String Email = "";
					
					String QuietanzanteCognome = "";
					String QuietanzanteNome= "";
					String QuietanzanteCodicefiscale= "";
					String QuietanzantePartitaIva= "";
					String QuietanzanteIndirizzo= "";
					String QuietanzanteNumeroCivico= "";
					String QuietanzanteCap= "";
					String QuietanzanteLocalita= "";
					String QuietanzanteCitta= "";
					String QuietanzanteProvincia= "";
					String QuietanzanteIban= "";
					String QuietanzanteDataNascita= "";
					String QuietanzanteLuogoNascita= "";
					String QuietanzanteProvinciaNascita= "";
					String BICSWIFTBeneficiario= "";
					String BICSWIFTQuietanzante= "";
					String Ruolosvolto= "";
					String Sezione= "";
					Boolean intestatarioContoFlag = true;
					Boolean ibanItaliano = true;
					
					String iban = "";
					String bic = "";
					
					if (data.has("iban")) {
						JsonNode accreditoCompenso = data.get("iban");	
						
						if (accreditoCompenso.has("iban")) {
							iban = accreditoCompenso.get("iban").getTextValue();
						}
						if (accreditoCompenso.has("tipologia") && accreditoCompenso.get("tipologia").getTextValue().equals("no")) {
							ibanItaliano = false;
						}
						if (!ibanItaliano) {
							if (accreditoCompenso.has("bic")) {
								bic = accreditoCompenso.get("bic").getTextValue();
							}
						}
					}
					
					// Richiedente		
					if (data.has("richiedente")) {			
						JsonNode richiedente = data.get("richiedente");
						
						BeneficiarioCognome = richiedente.has("cognome") ? richiedente.get("cognome").getTextValue() : DEFAULT_VALUE;
						BeneficiarioNome = richiedente.has("nome") ? richiedente.get("nome").getTextValue() : DEFAULT_VALUE;
						BeneficiarioCodiceFiscale = richiedente.has("codiceFiscale") ? richiedente.get("codiceFiscale").getTextValue() : DEFAULT_VALUE;
						BeneficiarioIndirizzo = richiedente.has("residenzaIndirizzo") ? richiedente.get("residenzaIndirizzo").getTextValue() : DEFAULT_VALUE;
						BeneficiarioNumeroCivico = richiedente.has("residenzaCivico") ? richiedente.get("residenzaCivico").getTextValue() : DEFAULT_VALUE;
						BeneficiarioCap = richiedente.has("residenzaCap") ? richiedente.get("residenzaCap").getTextValue() : DEFAULT_VALUE;
						BeneficiarioLocalita = richiedente.has("residenzaLocalita") ? richiedente.get("residenzaLocalita").getTextValue() : DEFAULT_VALUE;
						
						BeneficiarioCitta = (richiedente.has("residenzaComune") && richiedente.get("residenzaComune").has("nome") ) ? richiedente.get("residenzaComune").get("nome").getTextValue() : DEFAULT_VALUE;
						if (richiedente.has("residenzaProvincia") && richiedente.get("residenzaProvincia").get("nome") != null)
						{ 
							if (richiedente.get("residenzaProvincia").get("sigla") != null && 
									!richiedente.get("residenzaProvincia").get("sigla").getTextValue().equals(""))
							{
								BeneficiarioProvincia = richiedente.get("residenzaProvincia").get("sigla").getTextValue();
							}
							else {
								Provincia provincia = provinciaDAO.findByPK(richiedente.get("residenzaProvincia").get("codice").getIntValue());
								BeneficiarioProvincia = provincia.getSigla();
							}
						}
						BeneficiarioDataNascita =  richiedente.has("dataNascita") ? richiedente.get("dataNascita").getTextValue() : DEFAULT_VALUE;
						BeneficiarioLuogoNascita = "";
						String BeneficiarioStatoNascita = (richiedente.has("statoNascita") && richiedente.get("statoNascita").get("nome") != null) ? 
								richiedente.get("statoNascita").get("nome").getTextValue() : DEFAULT_VALUE;
						if (richiedente.has("cittaEsteraNascita") && !richiedente.get("cittaEsteraNascita").getTextValue().equals("")) {
							BeneficiarioLuogoNascita = richiedente.get("cittaEsteraNascita").getTextValue() + " (" +BeneficiarioStatoNascita+ ")";
						}
						else {
							if (richiedente.has("comuneNascita")) {
								BeneficiarioLuogoNascita = richiedente.get("comuneNascita").get("nome").getTextValue();
							}
						}
						if (richiedente.has("provinciaNascita") && richiedente.get("provinciaNascita").get("nome") != null)
						{
							if (richiedente.get("provinciaNascita").get("sigla") != null && 
									!richiedente.get("provinciaNascita").get("sigla").getTextValue().equals(""))
							{
								BeneficiarioProvinciaNascita = richiedente.get("provinciaNascita").get("sigla").getTextValue();
							}
							else {
								Provincia provinciaN = provinciaDAO.findByPK(richiedente.get("provinciaNascita").get("codice").getIntValue());
								BeneficiarioProvinciaNascita = provinciaN.getSigla();
							}
							
						}
											
						Telefono= richiedente.has("provinciaNascita") ? richiedente.get("contattiTelefono").getTextValue(): DEFAULT_VALUE;
						Cellulare = richiedente.has("contattiCellulare") ? richiedente.get("contattiCellulare").getTextValue() : DEFAULT_VALUE;
						Cellulare = modificaStringa(Cellulare);
						Email = data.has("email") ? data.get("email").getTextValue() : DEFAULT_VALUE;
					}
					if (data.has("dichiarazioni")) {			
						JsonNode dichiarazioni = data.get("dichiarazioni");		
						Ruolosvolto = dichiarazioni.has("ruoloGiaSvolto") ? dichiarazioni.get("ruoloGiaSvolto").getTextValue() : DEFAULT_VALUE;
						Sezione = dichiarazioni.has("sezioneElettoraleGiaSvolto") ? dichiarazioni.get("sezioneElettoraleGiaSvolto").getTextValue() : DEFAULT_VALUE;	
						
						if( dichiarazioni.has("intestatarioConto") && dichiarazioni.get("intestatarioConto") != null ) {
							String inteContoSN = dichiarazioni.get("intestatarioConto").getTextValue();
							switch (inteContoSN) {
							case "si":
								intestatarioContoFlag = true;
								break;
							case "no":
								intestatarioContoFlag = false;
								break;
							} 
						}
					}
					if (intestatarioContoFlag) {
						BeneficiarioIban = iban;
						BICSWIFTBeneficiario= bic;
						
					}
					else {
						// caso in cui il beneficiario non risulta intestario del conto
						QuietanzanteIban = iban;
						BICSWIFTQuietanzante= bic;
						
						if (data.has("intestatarioConto")) {
							JsonNode intestatarioConto = data.get("intestatarioConto");	
							
							QuietanzanteCognome= intestatarioConto.has("cognome") ? intestatarioConto.get("cognome").getTextValue() : DEFAULT_VALUE;
							QuietanzanteNome= intestatarioConto.has("nome") ? intestatarioConto.get("nome").getTextValue() : DEFAULT_VALUE;
							QuietanzanteCodicefiscale= intestatarioConto.has("codiceFiscale") ? intestatarioConto.get("codiceFiscale").getTextValue() : DEFAULT_VALUE;

							QuietanzantePartitaIva= "";

							QuietanzanteDataNascita= intestatarioConto.has("dataNascita") ? intestatarioConto.get("dataNascita").getTextValue() : DEFAULT_VALUE;
							
							if (intestatarioConto.has("provinciaNascita") && intestatarioConto.get("provinciaNascita").get("nome") != null)
							{
								if (intestatarioConto.get("provinciaNascita").get("sigla") != null && 
										!intestatarioConto.get("provinciaNascita").get("sigla").getTextValue().equals(""))
								{
									QuietanzanteProvinciaNascita = intestatarioConto.get("provinciaNascita").get("sigla").getTextValue();
								}
								else {
									Provincia provinciaN = provinciaDAO.findByPK(intestatarioConto.get("provinciaNascita").get("codice").getIntValue());
									QuietanzanteProvinciaNascita = provinciaN.getSigla();
								}
								
							}
							//QuietanzanteProvinciaNascita= intestatarioConto.has("provinciaNascita") ? intestatarioConto.get("provinciaNascita").get("nome").getTextValue() : DEFAULT_VALUE;
							
							
							String QuietanzanteStatoNascita = intestatarioConto.has("statoNascita") ? intestatarioConto.get("statoNascita").get("nome").getTextValue() : DEFAULT_VALUE;
							if (intestatarioConto.has("cittaEstera") && !intestatarioConto.get("cittaEstera").getTextValue().equals("")) {
								QuietanzanteLuogoNascita = intestatarioConto.get("cittaEstera").getTextValue() + " (" +QuietanzanteStatoNascita+ ")";
							}
							else {
								if (intestatarioConto.has("comuneNascita") && intestatarioConto.get("comuneNascita").has("nome")) {
									QuietanzanteLuogoNascita = intestatarioConto.get("comuneNascita").get("nome").getTextValue();
								}
							}
							if( intestatarioConto.has("residenza") ) {
								
								JsonNode residenza = intestatarioConto.get("residenza");
								QuietanzanteIndirizzo= residenza.has("indirizzo") ? residenza.get("indirizzo").getTextValue() : DEFAULT_VALUE;
								QuietanzanteNumeroCivico= residenza.has("civico") ? residenza.get("civico").getTextValue() : DEFAULT_VALUE ;
								QuietanzanteCap= residenza.has("cap") ? residenza.get("cap").getTextValue() : DEFAULT_VALUE;
								QuietanzanteLocalita= residenza.has("localita") ? residenza.get("localita").getTextValue() : DEFAULT_VALUE;
								QuietanzanteCitta= (residenza.has("comune") && residenza.get("comune").has("nome") ) ? residenza.get("comune").get("nome").getTextValue() : DEFAULT_VALUE;
								
								if (residenza.has("provincia") && residenza.get("provincia").get("nome") != null)
								{
									if (residenza.get("provincia").get("sigla") != null && 
											!residenza.get("provincia").get("sigla").getTextValue().equals(""))
									{
										QuietanzanteProvincia = residenza.get("provincia").get("sigla").getTextValue();
									}
									else {
										Provincia provinciaRQ = provinciaDAO.findByPK(residenza.get("provincia").get("codice").getIntValue());
										QuietanzanteProvincia = provinciaRQ.getSigla();
									}							
									
								}
								
							}
						}
						
					}
					
					
					StringBuilder row = new StringBuilder();
					row.append(Numero).append(CSV_SEPARATOR);
					row.append(Importo).append(CSV_SEPARATOR);
					row.append(CodEsecPagamento).append(CSV_SEPARATOR);  // valore 53
					row.append(BeneficiarioCognome).append(CSV_SEPARATOR);
					row.append(BeneficiarioNome).append(CSV_SEPARATOR);
					row.append(BeneficiarioCodiceFiscale).append(CSV_SEPARATOR);
					row.append(BeneficiarioIndirizzo).append(CSV_SEPARATOR);
					row.append(BeneficiarioNumeroCivico).append(CSV_SEPARATOR);
					row.append(BeneficiarioCap).append(CSV_SEPARATOR);
					row.append(BeneficiarioLocalita).append(CSV_SEPARATOR);
					row.append(BeneficiarioCitta).append(CSV_SEPARATOR);
					row.append(BeneficiarioProvincia).append(CSV_SEPARATOR);
					row.append(BeneficiarioIban).append(CSV_SEPARATOR);
					
					row.append(BeneficiarioDataNascita).append(CSV_SEPARATOR);
					row.append(BeneficiarioLuogoNascita).append(CSV_SEPARATOR);
					row.append(BeneficiarioProvinciaNascita).append(CSV_SEPARATOR);
					
					row.append(QuietanzanteCognome).append(CSV_SEPARATOR);
					row.append(QuietanzanteNome).append(CSV_SEPARATOR);
					row.append(QuietanzanteCodicefiscale).append(CSV_SEPARATOR);
					row.append(QuietanzantePartitaIva).append(CSV_SEPARATOR);
					row.append(QuietanzanteIndirizzo).append(CSV_SEPARATOR);
					row.append(QuietanzanteNumeroCivico).append(CSV_SEPARATOR);
					row.append(QuietanzanteCap).append(CSV_SEPARATOR);
					row.append(QuietanzanteLocalita).append(CSV_SEPARATOR);
					row.append(QuietanzanteCitta).append(CSV_SEPARATOR);
					row.append(QuietanzanteProvincia).append(CSV_SEPARATOR);
					row.append(QuietanzanteIban).append(CSV_SEPARATOR);
					row.append(QuietanzanteDataNascita).append(CSV_SEPARATOR);
					row.append(QuietanzanteLuogoNascita).append(CSV_SEPARATOR);
					row.append(QuietanzanteProvinciaNascita).append(CSV_SEPARATOR);
					row.append(BICSWIFTBeneficiario).append(CSV_SEPARATOR);
					row.append(BICSWIFTQuietanzante).append(CSV_SEPARATOR);
					row.append(Ruolosvolto).append(CSV_SEPARATOR);
					row.append(Sezione).append(CSV_SEPARATOR);
					row.append(Email).append(CSV_SEPARATOR);
					row.append(Cellulare).append(CSV_SEPARATOR);
					row.append(Telefono).append(CSV_SEPARATOR);
					
					row.append(entity.getCodiceIstanza()).append(CSV_SEPARATOR);
					row.append(df.format(entity.getCreated())).append(CSV_SEPARATOR);
					
					rows.add(row.toString());
					
				
				} catch (Exception e) {	
					
					log.debug("[" + CLASS_NAME + "::" + "ERRORE PARSING JSON" +"]  - id istanza: "+entity.getIdIstanza(), e);
				}
				
				i++;
			
			} // end for
			
		} // end if
						
		
		return rows;
	}

	public String modificaStringa(String testo) throws Exception
	{

	    Pattern p1 = Pattern.compile("\\(", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	    
	    String result = p1.matcher(testo).replaceAll("");
	    
	    Pattern p2 = Pattern.compile("\\)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	    result = p2.matcher(result).replaceAll("");

	    Pattern p3 = Pattern.compile(" ", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	    result = p3.matcher(result).replaceAll("");
	    
	    Pattern p4 = Pattern.compile("-", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	    result = p4.matcher(result).replaceAll("");
	        	    
	    return result;
	}
	
	
	public String getHeader() {

		StringBuilder sb = new StringBuilder();
		sb.append("Numero").append(CSV_SEPARATOR);
		sb.append("Importo").append(CSV_SEPARATOR);
		sb.append("CodEsecPagamento").append(CSV_SEPARATOR); // valore 53
		sb.append("BeneficiarioCognome").append(CSV_SEPARATOR);
		sb.append("BeneficiarioNome").append(CSV_SEPARATOR);
		sb.append("BeneficiarioCodiceFiscale").append(CSV_SEPARATOR);
		sb.append("BeneficiarioIndirizzo").append(CSV_SEPARATOR);
		sb.append("BeneficiarioNumeroCivico").append(CSV_SEPARATOR);
		sb.append("BeneficiarioCap").append(CSV_SEPARATOR);
		sb.append("BeneficiarioLocalita").append(CSV_SEPARATOR);
		sb.append("BeneficiarioCitta").append(CSV_SEPARATOR);
		sb.append("BeneficiarioProvincia").append(CSV_SEPARATOR);
		sb.append("BeneficiarioIban").append(CSV_SEPARATOR);

		sb.append("BeneficiarioDataNascita").append(CSV_SEPARATOR);
		sb.append("BeneficiarioLuogoNascita").append(CSV_SEPARATOR);
		sb.append("BeneficiarioProvinciaNascita").append(CSV_SEPARATOR);

		sb.append("QuietanzanteCognome").append(CSV_SEPARATOR);
		sb.append("QuietanzanteNome").append(CSV_SEPARATOR);
		sb.append("QuietanzanteCodicefiscale").append(CSV_SEPARATOR);
		sb.append("QuietanzantePartitaIva").append(CSV_SEPARATOR);
		sb.append("QuietanzanteIndirizzo").append(CSV_SEPARATOR);
		sb.append("QuietanzanteNumeroCivico").append(CSV_SEPARATOR);
		sb.append("QuietanzanteCap").append(CSV_SEPARATOR);
		sb.append("QuietanzanteLocalita").append(CSV_SEPARATOR);
		sb.append("QuietanzanteCitta").append(CSV_SEPARATOR);
		sb.append("QuietanzanteProvincia").append(CSV_SEPARATOR);
		sb.append("QuietanzanteIban").append(CSV_SEPARATOR);
		sb.append("QuietanzanteDataNascita").append(CSV_SEPARATOR);
		sb.append("QuietanzanteLuogoNascita").append(CSV_SEPARATOR);
		sb.append("QuietanzanteProvinciaNascita").append(CSV_SEPARATOR);
		sb.append("BIC-SWIFT Beneficiario").append(CSV_SEPARATOR);
		sb.append("BIC-SWIFTQuietanzante").append(CSV_SEPARATOR);
		sb.append("Ruolo svolto").append(CSV_SEPARATOR);
		sb.append("Sezione").append(CSV_SEPARATOR);
		sb.append("Email").append(CSV_SEPARATOR);
		sb.append("Cellulare").append(CSV_SEPARATOR);
		sb.append("Telefono").append(CSV_SEPARATOR);
		sb.append("Codice istanza").append(CSV_SEPARATOR);
		sb.append("Data invio").append(CSV_SEPARATOR);
		sb.append("Timestamp").append(CSV_SEPARATOR);
		
		return sb.toString();

	}
	
	public String remapIstanza(IstanzaEstratta istanza) throws Exception {
		final String DEFAULT_VALUE = "";
		final DateFormat fDataOra = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		final DateFormat fTimeStamp = new SimpleDateFormat("yyyyMMddhhmmss");

		// Lettura del json

		log.debug("[" + CLASS_NAME + "::remap id istanza]  - " + istanza.getIdIstanza());
		String datastr = istanza.getData().toString();

		// JsonNode istanzaJsonNode = readIstanzaData(entity.getData().toString());
		// log.debug("[" + CLASS_NAME + "::remap lunghezza stringa ] - "+
		// datastr.length());

		JsonNode istanzaJsonNode = readIstanzaData(datastr);

		// log.debug("[" + CLASS_NAME + "::parse data ] ");
		JsonNode data = istanzaJsonNode.get("data");

		String Numero = String.valueOf(istanza.getRowNumber());
		String Importo = "";
		String CodEsecPagamento = "53";
		String BeneficiarioCognome = "";
		String BeneficiarioNome = "";
		String BeneficiarioCodiceFiscale = "";
		String BeneficiarioIndirizzo = "";
		String BeneficiarioNumeroCivico = "";
		String BeneficiarioCap = "";
		String BeneficiarioLocalita = "";
		String BeneficiarioCitta = "";
		String BeneficiarioProvincia = "";
		String BeneficiarioIban = "";

		String BeneficiarioDataNascita = "";
		String BeneficiarioLuogoNascita = "";
		String BeneficiarioProvinciaNascita = "";

		String Telefono = "";
		String Cellulare = "";
		String Email = "";

		String QuietanzanteCognome = "";
		String QuietanzanteNome = "";
		String QuietanzanteCodicefiscale = "";
		String QuietanzantePartitaIva = "";
		String QuietanzanteIndirizzo = "";
		String QuietanzanteNumeroCivico = "";
		String QuietanzanteCap = "";
		String QuietanzanteLocalita = "";
		String QuietanzanteCitta = "";
		String QuietanzanteProvincia = "";
		String QuietanzanteIban = "";
		String QuietanzanteDataNascita = "";
		String QuietanzanteLuogoNascita = "";
		String QuietanzanteProvinciaNascita = "";
		String BICSWIFTBeneficiario = "";
		String BICSWIFTQuietanzante = "";
		String Ruolosvolto = "";
		String Sezione = "";
		Boolean intestatarioContoFlag = true;
		Boolean ibanItaliano = true;

		String iban = "";
		String bic = "";

		if (data.has("iban")) {
			JsonNode accreditoCompenso = data.get("iban");

			if (accreditoCompenso.has("iban")) {
				iban = accreditoCompenso.get("iban").getTextValue();
			}
			if (accreditoCompenso.has("tipologia") && accreditoCompenso.get("tipologia").getTextValue().equals("no")) {
				ibanItaliano = false;
			}
			if (!ibanItaliano) {
				if (accreditoCompenso.has("bic")) {
					bic = accreditoCompenso.get("bic").getTextValue();
				}
			}
		}

		// Richiedente
		if (data.has("richiedente")) {
			JsonNode richiedente = data.get("richiedente");

			BeneficiarioCognome = richiedente.has("cognome") ? richiedente.get("cognome").getTextValue()
					: DEFAULT_VALUE;
			BeneficiarioNome = richiedente.has("nome") ? richiedente.get("nome").getTextValue() : DEFAULT_VALUE;
			BeneficiarioCodiceFiscale = richiedente.has("codiceFiscale")
					? richiedente.get("codiceFiscale").getTextValue()
					: DEFAULT_VALUE;
			BeneficiarioIndirizzo = richiedente.has("residenzaIndirizzo")
					? richiedente.get("residenzaIndirizzo").getTextValue()
					: DEFAULT_VALUE;
			BeneficiarioNumeroCivico = richiedente.has("residenzaCivico")
					? richiedente.get("residenzaCivico").getTextValue()
					: DEFAULT_VALUE;
			BeneficiarioCap = richiedente.has("residenzaCap") ? richiedente.get("residenzaCap").getTextValue()
					: DEFAULT_VALUE;
			BeneficiarioLocalita = richiedente.has("residenzaLocalita")
					? richiedente.get("residenzaLocalita").getTextValue()
					: DEFAULT_VALUE;

			BeneficiarioCitta = (richiedente.has("residenzaComune") && richiedente.get("residenzaComune").has("nome"))
					? richiedente.get("residenzaComune").get("nome").getTextValue()
					: DEFAULT_VALUE;
			if (richiedente.has("residenzaProvincia") && richiedente.get("residenzaProvincia").get("nome") != null) {
				if (richiedente.get("residenzaProvincia").get("sigla") != null
						&& !richiedente.get("residenzaProvincia").get("sigla").getTextValue().equals("")) {
					BeneficiarioProvincia = richiedente.get("residenzaProvincia").get("sigla").getTextValue();
				} else {
					Provincia provincia = provinciaDAO
							.findByPK(richiedente.get("residenzaProvincia").get("codice").getIntValue());
					BeneficiarioProvincia = provincia.getSigla();
				}
			}
			BeneficiarioDataNascita = richiedente.has("dataNascita") ? richiedente.get("dataNascita").getTextValue()
					: DEFAULT_VALUE;
			BeneficiarioLuogoNascita = "";
			String BeneficiarioStatoNascita = (richiedente.has("statoNascita")
					&& richiedente.get("statoNascita").get("nome") != null)
							? richiedente.get("statoNascita").get("nome").getTextValue()
							: DEFAULT_VALUE;
			if (richiedente.has("cittaEsteraNascita")
					&& !richiedente.get("cittaEsteraNascita").getTextValue().equals("")) {
				BeneficiarioLuogoNascita = richiedente.get("cittaEsteraNascita").getTextValue() + " ("
						+ BeneficiarioStatoNascita + ")";
			} else {
				if (richiedente.has("comuneNascita")) {
					BeneficiarioLuogoNascita = richiedente.get("comuneNascita").get("nome").getTextValue();
				}
			}
			if (richiedente.has("provinciaNascita") && richiedente.get("provinciaNascita").get("nome") != null) {
				if (richiedente.get("provinciaNascita").get("sigla") != null
						&& !richiedente.get("provinciaNascita").get("sigla").getTextValue().equals("")) {
					BeneficiarioProvinciaNascita = richiedente.get("provinciaNascita").get("sigla").getTextValue();
				} else {
					Provincia provinciaN = provinciaDAO
							.findByPK(richiedente.get("provinciaNascita").get("codice").getIntValue());
					BeneficiarioProvinciaNascita = provinciaN.getSigla();
				}

			}

			Telefono = richiedente.has("provinciaNascita") ? richiedente.get("contattiTelefono").getTextValue()
					: DEFAULT_VALUE;
			Cellulare = richiedente.has("contattiCellulare") ? richiedente.get("contattiCellulare").getTextValue()
					: DEFAULT_VALUE;
			Cellulare = modificaStringa(Cellulare);
			Email = data.has("email") ? data.get("email").getTextValue() : DEFAULT_VALUE;
		}
		if (data.has("dichiarazioni")) {
			JsonNode dichiarazioni = data.get("dichiarazioni");
			Ruolosvolto = dichiarazioni.has("ruoloGiaSvolto") ? dichiarazioni.get("ruoloGiaSvolto").getTextValue()
					: DEFAULT_VALUE;
			Sezione = dichiarazioni.has("sezioneElettoraleGiaSvolto")
					? dichiarazioni.get("sezioneElettoraleGiaSvolto").getTextValue()
					: DEFAULT_VALUE;

			if (dichiarazioni.has("intestatarioConto") && dichiarazioni.get("intestatarioConto") != null) {
				String inteContoSN = dichiarazioni.get("intestatarioConto").getTextValue();
				switch (inteContoSN) {
				case "si":
					intestatarioContoFlag = true;
					break;
				case "no":
					intestatarioContoFlag = false;
					break;
				}
			}
		}
		if (intestatarioContoFlag) {
			BeneficiarioIban = iban;
			BICSWIFTBeneficiario = bic;

		} else {
			// caso in cui il beneficiario non risulta intestario del conto
			QuietanzanteIban = iban;
			BICSWIFTQuietanzante = bic;

			if (data.has("intestatarioConto")) {
				JsonNode intestatarioConto = data.get("intestatarioConto");

				QuietanzanteCognome = intestatarioConto.has("cognome") ? intestatarioConto.get("cognome").getTextValue()
						: DEFAULT_VALUE;
				QuietanzanteNome = intestatarioConto.has("nome") ? intestatarioConto.get("nome").getTextValue()
						: DEFAULT_VALUE;
				QuietanzanteCodicefiscale = intestatarioConto.has("codiceFiscale")
						? intestatarioConto.get("codiceFiscale").getTextValue()
						: DEFAULT_VALUE;

				QuietanzantePartitaIva = "";

				QuietanzanteDataNascita = intestatarioConto.has("dataNascita")
						? intestatarioConto.get("dataNascita").getTextValue()
						: DEFAULT_VALUE;

				if (intestatarioConto.has("provinciaNascita")
						&& intestatarioConto.get("provinciaNascita").get("nome") != null) {
					if (intestatarioConto.get("provinciaNascita").get("sigla") != null
							&& !intestatarioConto.get("provinciaNascita").get("sigla").getTextValue().equals("")) {
						QuietanzanteProvinciaNascita = intestatarioConto.get("provinciaNascita").get("sigla")
								.getTextValue();
					} else {
						Provincia provinciaN = provinciaDAO
								.findByPK(intestatarioConto.get("provinciaNascita").get("codice").getIntValue());
						QuietanzanteProvinciaNascita = provinciaN.getSigla();
					}

				}
				// QuietanzanteProvinciaNascita= intestatarioConto.has("provinciaNascita") ?
				// intestatarioConto.get("provinciaNascita").get("nome").getTextValue() :
				// DEFAULT_VALUE;

				String QuietanzanteStatoNascita = intestatarioConto.has("statoNascita")
						? intestatarioConto.get("statoNascita").get("nome").getTextValue()
						: DEFAULT_VALUE;
				if (intestatarioConto.has("cittaEstera")
						&& !intestatarioConto.get("cittaEstera").getTextValue().equals("")) {
					QuietanzanteLuogoNascita = intestatarioConto.get("cittaEstera").getTextValue() + " ("
							+ QuietanzanteStatoNascita + ")";
				} else {
					if (intestatarioConto.has("comuneNascita") && intestatarioConto.get("comuneNascita").has("nome")) {
						QuietanzanteLuogoNascita = intestatarioConto.get("comuneNascita").get("nome").getTextValue();
					}
				}
				if (intestatarioConto.has("residenza")) {

					JsonNode residenza = intestatarioConto.get("residenza");
					QuietanzanteIndirizzo = residenza.has("indirizzo") ? residenza.get("indirizzo").getTextValue()
							: DEFAULT_VALUE;
					QuietanzanteNumeroCivico = residenza.has("civico") ? residenza.get("civico").getTextValue()
							: DEFAULT_VALUE;
					QuietanzanteCap = residenza.has("cap") ? residenza.get("cap").getTextValue() : DEFAULT_VALUE;
					QuietanzanteLocalita = residenza.has("localita") ? residenza.get("localita").getTextValue()
							: DEFAULT_VALUE;
					QuietanzanteCitta = (residenza.has("comune") && residenza.get("comune").has("nome"))
							? residenza.get("comune").get("nome").getTextValue()
							: DEFAULT_VALUE;

					if (residenza.has("provincia") && residenza.get("provincia").get("nome") != null) {
						if (residenza.get("provincia").get("sigla") != null
								&& !residenza.get("provincia").get("sigla").getTextValue().equals("")) {
							QuietanzanteProvincia = residenza.get("provincia").get("sigla").getTextValue();
						} else {
							Provincia provinciaRQ = provinciaDAO
									.findByPK(residenza.get("provincia").get("codice").getIntValue());
							QuietanzanteProvincia = provinciaRQ.getSigla();
						}

					}

				}
			}

		}

		StringBuilder row = new StringBuilder();
		row.append(Numero).append(CSV_SEPARATOR);
		row.append(Importo).append(CSV_SEPARATOR);
		row.append(CodEsecPagamento).append(CSV_SEPARATOR); // valore 53
		row.append(BeneficiarioCognome).append(CSV_SEPARATOR);
		row.append(BeneficiarioNome).append(CSV_SEPARATOR);
		row.append(BeneficiarioCodiceFiscale).append(CSV_SEPARATOR);
		row.append(BeneficiarioIndirizzo).append(CSV_SEPARATOR);
		row.append(BeneficiarioNumeroCivico).append(CSV_SEPARATOR);
		row.append(BeneficiarioCap).append(CSV_SEPARATOR);
		row.append(BeneficiarioLocalita).append(CSV_SEPARATOR);
		row.append(BeneficiarioCitta).append(CSV_SEPARATOR);
		row.append(BeneficiarioProvincia).append(CSV_SEPARATOR);
		row.append(BeneficiarioIban).append(CSV_SEPARATOR);

		row.append(BeneficiarioDataNascita).append(CSV_SEPARATOR);
		row.append(BeneficiarioLuogoNascita).append(CSV_SEPARATOR);
		row.append(BeneficiarioProvinciaNascita).append(CSV_SEPARATOR);

		row.append(QuietanzanteCognome).append(CSV_SEPARATOR);
		row.append(QuietanzanteNome).append(CSV_SEPARATOR);
		row.append(QuietanzanteCodicefiscale).append(CSV_SEPARATOR);
		row.append(QuietanzantePartitaIva).append(CSV_SEPARATOR);
		row.append(QuietanzanteIndirizzo).append(CSV_SEPARATOR);
		row.append(QuietanzanteNumeroCivico).append(CSV_SEPARATOR);
		row.append(QuietanzanteCap).append(CSV_SEPARATOR);
		row.append(QuietanzanteLocalita).append(CSV_SEPARATOR);
		row.append(QuietanzanteCitta).append(CSV_SEPARATOR);
		row.append(QuietanzanteProvincia).append(CSV_SEPARATOR);
		row.append(QuietanzanteIban).append(CSV_SEPARATOR);
		row.append(QuietanzanteDataNascita).append(CSV_SEPARATOR);
		row.append(QuietanzanteLuogoNascita).append(CSV_SEPARATOR);
		row.append(QuietanzanteProvinciaNascita).append(CSV_SEPARATOR);
		row.append(BICSWIFTBeneficiario).append(CSV_SEPARATOR);
		row.append(BICSWIFTQuietanzante).append(CSV_SEPARATOR);
		row.append(Ruolosvolto).append(CSV_SEPARATOR);
		row.append(Sezione).append(CSV_SEPARATOR);
		row.append(Email).append(CSV_SEPARATOR);
		row.append(Cellulare).append(CSV_SEPARATOR);
		row.append(Telefono).append(CSV_SEPARATOR);

		row.append(istanza.getCodiceIstanza()).append(CSV_SEPARATOR);
		row.append(fDataOra.format(istanza.getCreated())).append(CSV_SEPARATOR);
		row.append(fTimeStamp.format(istanza.getCreated())).append(CSV_SEPARATOR);

		return row.toString();

	}
	
	

}
