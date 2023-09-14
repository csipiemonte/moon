/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.extra.demografia;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ComponenteFamiglia (Fonte: Demografia ANPR - getFamiglia)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ComponenteFamiglia   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private String nome = null;
  private String cognome = null;
  private String codiceFiscale = null;
  private String dataNascita = null;
  private Nazione nazioneNascita = null;
//  private Regione regioneNascita = null;
//  private Provincia provinciaNascita = null;
//  private Comune comuneNascita = null;
//  private String comuneEsteroNascita = null;
  private String luogoNascita = null;
  private String sesso = null;
  private Cittadinanza cittadinanza = null;
  private RelazioneParentela relazioneParentela = null;
  private String flagPossessoPatente = null;
  private String flagPossessoVeicolo = null;
  private DocumentoRiconoscimento cartaIdentita = null;
  private DocumentoRiconoscimento documentoSoggiorno = null;

	public ComponenteFamiglia() {
		super();
	}
	public ComponenteFamiglia(String codiceFiscale) {
		super();
		this.codiceFiscale = codiceFiscale;
	}
	public ComponenteFamiglia(String codiceFiscale, String cognome, String nome) {
		super();
		this.codiceFiscale = codiceFiscale;
		this.cognome = cognome;
		this.nome = nome;
	}
	
  /**
   * nome del componente
   **/
  
  @ApiModelProperty(value = "nome del componente")
  @JsonProperty("nome") 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * cognome del componente
   **/
  
  @ApiModelProperty(value = "cognome del componente")
  @JsonProperty("cognome") 
  public String getCognome() {
    return cognome;
  }
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  /**
   * codice fiscale del componente
   **/
  @ApiModelProperty(value = "codice fiscale del componente")
  @JsonProperty("codiceFiscale") 
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  /**
   * data nascita del componente
   **/
  @ApiModelProperty(value = "data nascita del componente")
  @JsonProperty("dataNascita") 
  public String getDataNascita() {
    return dataNascita;
  }
  public void setDataNascita(String dataNascita) {
    this.dataNascita = dataNascita;
  }
  
  /**
   * nazione di Nascita del componente
   **/
  @ApiModelProperty(value = "nazioneNascita del componente")
  @JsonProperty("nazioneNascita") 
  public Nazione getNazioneNascita() {
    return nazioneNascita;
  }
  public void setNazioneNascita(Nazione nazioneNascita) {
    this.nazioneNascita = nazioneNascita;
  }
  
  /**
   * regione di Nascita del componente
   **/
//  @ApiModelProperty(value = "regioneNascita del componente")
//  @JsonProperty("regioneNascita") 
//  public Regione getRegioneNascita() {
//    return regioneNascita;
//  }
//  public void setRegioneNascita(Regione regioneNascita) {
//    this.regioneNascita = regioneNascita;
//  }
  
  /**
   * provincia di Nascita del componente
   **/
//  @ApiModelProperty(value = "provinciaNascita del componente")
//  @JsonProperty("provinciaNascita") 
//  public Provincia getProvinciaNascita() {
//    return provinciaNascita;
//  }
//  public void setProvinciaNascita(Provincia provinciaNascita) {
//    this.provinciaNascita = provinciaNascita;
//  }
  
  /**
   * comune di Nascita del componente
   **/
//  @ApiModelProperty(value = "comune italiano Nascita del componente")
//  @JsonProperty("comuneNascita") 
//  public Comune getComuneNascita() {
//    return comuneNascita;
//  }
//  public void setComuneNascita(Comune comuneNascita) {
//    this.comuneNascita = comuneNascita;
//  }
  
  
  /**
   * comune estero di Nascita del componente
   **/
//  @ApiModelProperty(value = "comune estero Nascita del componente")
//  @JsonProperty("comuneEsteroNascita") 
//  public String getComuneEsteroNascita() {
//    return comuneEsteroNascita;
//  }
//  public void setComuneEsteroNascita(String comuneEsteroNascita) {
//    this.comuneEsteroNascita = comuneEsteroNascita;
//  }
  
  
  /**
   * comune estero di Nascita del componente
   **/
  @ApiModelProperty(value = "luogo Nascita del componente")
  @JsonProperty("luogoNascita") 
  public String getLuogoNascita() {
    return luogoNascita;
  }
  public void setLuogoNascita(String luogoNascita) {
    this.luogoNascita = luogoNascita;
  }
  
  
  /**
   * sesso del componente
   **/
  @ApiModelProperty(value = "sesso del componente")
  @JsonProperty("sesso") 
  public String getSesso() {
    return sesso;
  }
  public void setSesso(String sesso) {
    this.sesso = sesso;
  }

  /**
   * cittadinanza del componente
   **/
  @ApiModelProperty(value = "cittadinanza del componente")
  @JsonProperty("cittadinanza") 
  public Cittadinanza getCittadinanza() {
    return cittadinanza;
  }
  public void setCittadinanza(Cittadinanza cittadinanza) {
    this.cittadinanza = cittadinanza;
  }

  /**
   * Relazione Parentela del componente
   **/
  @ApiModelProperty(value = "relazione Parenteladel componente")
  @JsonProperty("relazioneParentela") 
  public RelazioneParentela getRelazioneParentela() {
    return relazioneParentela;
  }
  public void setRelazioneParentela(RelazioneParentela relazioneParentela) {
    this.relazioneParentela = relazioneParentela;
  }

  /**
   * flag di possesso di patente del componente
   **/
  @ApiModelProperty(value = "flag di possesso di patente del componente")
  @JsonProperty("flagPossessoPatente") 
  public String getFlagPossessoPatente() {
    return flagPossessoPatente;
  }
  public void setFlagPossessoPatente(String flagPossessoPatente) {
    this.flagPossessoPatente = flagPossessoPatente;
  }
  
  /**
   * flag di possesso di veicolo del componente
   **/
  @ApiModelProperty(value = "flag di possesso di veicolo del componente")
  @JsonProperty("flagPossessoVeicolo") 
  public String getFlagPossessoVeicolo() {
    return flagPossessoVeicolo;
  }
  public void setFlagPossessoVeicolo(String flagPossessoVeicolo) {
    this.flagPossessoVeicolo = flagPossessoVeicolo;
  }


  /**
   * cartaIdentita
   **/
  @ApiModelProperty(value = "cartaIdentita")
  @JsonProperty("cartaIdentita") 
	public DocumentoRiconoscimento getCartaIdentita() {
		return cartaIdentita;
	}
	public void setCartaIdentita(DocumentoRiconoscimento cartaIdentita) {
		this.cartaIdentita = cartaIdentita;
	}

	  /**
	   * documentoSoggorno
	   **/
	  @ApiModelProperty(value = "documentoSoggiorno")
	  @JsonProperty("documentoSoggiorno") 
	public DocumentoRiconoscimento getDocumentoSoggiorno() {
		return documentoSoggiorno;
	}
	public void setDocumentoSoggiorno(DocumentoRiconoscimento documentoSoggiorno) {
		this.documentoSoggiorno = documentoSoggiorno;
	}


@Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComponenteFamiglia componente = (ComponenteFamiglia) o;
    return Objects.equals(nome, componente.nome) &&
        Objects.equals(cognome, componente.cognome) &&
        Objects.equals(codiceFiscale, componente.codiceFiscale) &&
        Objects.equals(dataNascita, componente.dataNascita) &&
        Objects.equals(nazioneNascita, componente.nazioneNascita) &&
//        Objects.equals(regioneNascita, componente.regioneNascita) &&
//        Objects.equals(provinciaNascita, componente.provinciaNascita) &&
//        Objects.equals(comuneNascita, componente.comuneNascita) &&
//        Objects.equals(comuneEsteroNascita, componente.comuneEsteroNascita) &&
        Objects.equals(luogoNascita, componente.luogoNascita) &&
        Objects.equals(sesso, componente.sesso) &&
        Objects.equals(cittadinanza, componente.cittadinanza) &&
        Objects.equals(relazioneParentela, componente.relazioneParentela) &&
        Objects.equals(flagPossessoPatente, componente.flagPossessoPatente) &&
        Objects.equals(flagPossessoVeicolo, componente.flagPossessoVeicolo) &&
        Objects.equals(cartaIdentita, componente.cartaIdentita) &&
        Objects.equals(documentoSoggiorno, componente.documentoSoggiorno);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, cognome, codiceFiscale, dataNascita, nazioneNascita, luogoNascita, sesso, cittadinanza, relazioneParentela, flagPossessoPatente, flagPossessoVeicolo, cartaIdentita, documentoSoggiorno);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComponenteFamiglia {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    dataNascita: ").append(toIndentedString(dataNascita)).append("\n");
    sb.append("    nazioneNascita: ").append(toIndentedString(nazioneNascita)).append("\n");
//    sb.append("    regioneNascita: ").append(toIndentedString(regioneNascita)).append("\n");
//    sb.append("    provinciaNascita: ").append(toIndentedString(provinciaNascita)).append("\n");
//    sb.append("    comuneNascita: ").append(toIndentedString(comuneNascita)).append("\n");
//    sb.append("    comuneEsteroNascita: ").append(toIndentedString(comuneEsteroNascita)).append("\n");
    sb.append("    luogoNascita: ").append(toIndentedString(luogoNascita)).append("\n");
    sb.append("    sesso: ").append(toIndentedString(sesso)).append("\n");
    sb.append("    cittadinanza: ").append(toIndentedString(cittadinanza)).append("\n");
    sb.append("    relazioneParentela: ").append(toIndentedString(relazioneParentela)).append("\n");
    sb.append("    flagPossessoPatente: ").append(toIndentedString(flagPossessoPatente)).append("\n");
    sb.append("    flagPossessoVeicolo: ").append(toIndentedString(flagPossessoVeicolo)).append("\n");
    sb.append("    cartaIdentita: ").append(toIndentedString(cartaIdentita)).append("\n");
    sb.append("    documentoSoggiorno: ").append(toIndentedString(documentoSoggiorno)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

