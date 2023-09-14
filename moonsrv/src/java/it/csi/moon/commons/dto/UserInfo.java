/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

// Serializable for modulistica
public class UserInfo implements Serializable {
	  
	private static final long serialVersionUID = 1L;

	public static final UserInfo ADMIN = new UserInfo("ADMIN");
	
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private String nome = null;
  private String cognome = null;
  private String identificativoUtente = null;
  private String mail = null;
  private String codFiscDichIstanza = null;
  private String nomeDich = null;
  private String cognomeDich = null;
  private Boolean isOperatore;
  private Boolean isMultiEntePortale;
  private Ente ente = null;
  private TipoUtente tipoUtente = null;
  private List<EnteAreeRuoli> entiAreeRuoli = null;
  private List<Ruolo> ruoli = null;
  private List<String> funzioni = null;
  private List<String> gruppi = null;
  private String idIride = null;
  private String idMoonToken = null;
  private String jwt = null;
  private String gruppoOperatoreFo = null;
  private DatiAggiuntivi datiAggiuntivi = null;
  private Integer idAmbito = null;
  private String descrizioneAmbito = null;
  private Integer idFruitore = null;
  private String portalName = null;
  private Boolean isApi;

  
  public UserInfo() {
	  super();
	  this.isOperatore = Boolean.FALSE;
	  this.isMultiEntePortale = Boolean.FALSE;
	  this.isApi = Boolean.FALSE;
  }
  public UserInfo(String identificativoUtente) {
	  this.identificativoUtente = identificativoUtente;
	  this.isOperatore = Boolean.FALSE;
	  this.isMultiEntePortale = Boolean.FALSE;
	  this.isApi = Boolean.FALSE;
  }
  public UserInfo(String identificativoUtente, String cognome, String nome) {
	  this.identificativoUtente = identificativoUtente;
	  this.cognome = cognome;
	  this.nome = nome;
	  this.isOperatore = Boolean.FALSE;
	  this.isMultiEntePortale = Boolean.FALSE;
	  this.isApi = Boolean.FALSE;
  }
  
  // Per Accesso Diretto : Modulistica
  public UserInfo(String identificativoUtente, String cognome, String nome, Long idEnte) {
	  this.identificativoUtente = identificativoUtente;
	  this.cognome = cognome;
	  this.nome = nome;
	  this.ente = new Ente();
	  this.ente.setIdEnte(idEnte);
	  this.isOperatore = Boolean.FALSE;
	  this.isMultiEntePortale = Boolean.FALSE;
	  this.isApi = Boolean.FALSE;
  }

  /**
   * Clone UserInfo
   */
  public UserInfo(UserInfo userInfoToClone) {
	  super();
	  this.nome = userInfoToClone.getNome();
	  this.cognome = userInfoToClone.getCognome();
	  this.identificativoUtente = userInfoToClone.getIdentificativoUtente();
	  this.mail = userInfoToClone.getMail();
	  this.codFiscDichIstanza = userInfoToClone.getCodFiscDichIstanza();
	  this.nomeDich = userInfoToClone.getNomeDich();
	  this.cognomeDich = userInfoToClone.getCognomeDich();
	  this.isOperatore = userInfoToClone.isOperatore();
	  this.isMultiEntePortale = userInfoToClone.isMultiEntePortale();
	  this.ente = userInfoToClone.getEnte()==null?null:new Ente(userInfoToClone.getEnte());
	  this.tipoUtente = userInfoToClone.getTipoUtente()==null?null:new TipoUtente(userInfoToClone.getTipoUtente());
	  this.entiAreeRuoli = userInfoToClone.getEntiAreeRuoli()==null?null:new ArrayList<>(userInfoToClone.getEntiAreeRuoli());
	  this.ruoli = userInfoToClone.getRuoli()==null?null:new ArrayList<>(userInfoToClone.getRuoli());
	  this.funzioni = userInfoToClone.getFunzioni()==null?null:new ArrayList<>(userInfoToClone.getFunzioni());
	  this.gruppi = userInfoToClone.getGruppi()==null?null:new ArrayList<>(userInfoToClone.getGruppi());
	  this.idIride = userInfoToClone.getIdIride();
	  this.idMoonToken = userInfoToClone.getIdMoonToken();
	  this.jwt = userInfoToClone.getJwt();
	  this.gruppoOperatoreFo = userInfoToClone.getGruppoOperatoreFo();
	  this.datiAggiuntivi = userInfoToClone.getDatiAggiuntivi()==null?null:new DatiAggiuntivi(userInfoToClone.getDatiAggiuntivi());
	  this.idAmbito = userInfoToClone.getIdAmbito();
	  this.descrizioneAmbito = userInfoToClone.getDescrizioneAmbito();
	  this.idFruitore = userInfoToClone.getIdFruitore();
	  this.portalName = userInfoToClone.getPortalName();
	  this.isApi = userInfoToClone.isApi();
  }
  
  /**
   * nome dell&#39;utente preso dall'identificazione
   **/
  @ApiModelProperty(value = "nome dell'utente preso dall'identificazione")
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * cognome dell&#39;utente preso dall'identificazione
   **/
  @ApiModelProperty(value = "cognome dell'utente preso dall'identificazione")
  public String getCognome() {
    return cognome;
  }
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  /**
   * identificativo dell&#39;utente
   **/
  @ApiModelProperty(value = "identificativo dell'utente")
  public String getIdentificativoUtente() {
    return identificativoUtente;
  }
  public void setIdentificativoUtente(String identificativoUtente) {
    this.identificativoUtente = identificativoUtente;
  }

  /**
   * mail dell&#39;utente preso dall'identificazione
   **/
  @ApiModelProperty(value = "mail dell'utente preso dall'identificazione")
  public String getMail() {
    return mail;
  }
  public void setMail(String mail) {
    this.mail = mail;
  }

  /**
   * codice fiscale dell&#39;utente dichiarante
   * Nel cal di ContoTerzi viene valorizzato con EnteArea es. "e:2;a:15"
   **/
  @ApiModelProperty(value = "codice fiscale dell'utente dichiarante")
  public String getCodFiscDichIstanza() {
    return (codFiscDichIstanza!=null&&codFiscDichIstanza.length()>0)?codFiscDichIstanza:identificativoUtente;
  }
  public void setCodFiscDichIstanza(String codFiscDichIstanza) {
    this.codFiscDichIstanza = codFiscDichIstanza;
  }
  
  /**
   * nome dell&#39;dichiarante preso dall'identificazione se cittadino o area
   **/
  @ApiModelProperty(value = "nome dell'utente preso dall'identificazione se cittadino o area")
  public String getNomeDich() {
	return (nomeDich!=null)?nomeDich:nome;
  }
  /**
   * se valorizzato a null, il get tornera nome dell'utente collegato
   * se valorizzato a vuoto, il get tornera vuoto
   * @param nomeDich
   */
  public void setNomeDich(String nomeDich) {
    this.nomeDich = nomeDich;
  }

  /**
   * se l'utente non è un cittadino opera per un operatore (Ente/Area)
   **/
  @ApiModelProperty(value = "se l'utente non è un cittadino opera per un operatore (Ente/Area)")
  public String getCognomeDich() {
	return (cognomeDich!=null)?cognomeDich:cognome;
  }
  /**
   * se valorizzato a null, il get tornera cognome dell'utente collegato
   * se valorizzato a vuoto, il get tornera vuoto
   * @param cognomeDich
   */
  public void setCognomeDich(String cognomeDich) {
    this.cognomeDich = cognomeDich;
  }

  /**
   * flag se utente is Operatore
   **/
  @ApiModelProperty(value = "flag se utente is Operatore")
  public Boolean isOperatore() {
	return isOperatore;
  }
  public void setOperatore(Boolean isOperatore) {
	this.isOperatore = isOperatore;
  }
  
  /**
   * flag se utente is logged on multiEnte Portale
   **/
  @ApiModelProperty(value = "flag se utente is logged on multiEnte Portale")
  public Boolean isMultiEntePortale() {
	return isMultiEntePortale;
  }
  public void setMultiEntePortale(Boolean isMultiEntePortale) {
	this.isMultiEntePortale = isMultiEntePortale;
  }
  
  /**
   * ente corrente dell&#39;ente legato ai moduli/istanze
   **/
  @ApiModelProperty(value = "ente corrente legato ai moduli/istanze")
  public Ente getEnte() {
    return ente;
  }
  public void setEnte(Ente ente) {
    this.ente = ente;
  }
  
  /**
   * tipologia dell&#39;utente (ADM,PA,..)
   **/
  @ApiModelProperty(value = "tipologia dell'utente (ADM,PA,..)")
  public TipoUtente getTipoUtente() {
    return tipoUtente;
  }
  public void setTipoUtente(TipoUtente tipoUtente) {
    this.tipoUtente = tipoUtente;
  }
  
  /**
   * nome del ruolo dell&#39;utente
   **/
  @ApiModelProperty(value = "list entiAreeRuoli dell'utente")
  public List<EnteAreeRuoli> getEntiAreeRuoli() {
    return entiAreeRuoli;
  }
  public void setEntiAreeRuoli(List<EnteAreeRuoli> entiAreeRuoli) {
    this.entiAreeRuoli = entiAreeRuoli;
  }
  
  /**
   * nome del ruolo dell&#39;utente
   **/
  @ApiModelProperty(value = "list degli ruoli dell'utente")
  public List<Ruolo> getRuoli() {
    return ruoli;
  }
  public void setRuoli(List<Ruolo> ruoli) {
    this.ruoli = ruoli;
  }

  /**
   * list delle funzioni accessibile all&#39;utente
   **/
  @ApiModelProperty(value = "list delle funzioni accessibile all'utente")
  public List<String> getFunzioni() {
    return funzioni;
  }
  public void setFunzioni(List<String> funzioni) {
    this.funzioni = funzioni;
  }

  /**
   * ID iride dell&#39;utente
   **/
  @ApiModelProperty(value = "ID iride dell'utente")
  public String getIdIride() {
    return idIride;
  }
  public void setIdIride(String idIride) {
    this.idIride = idIride;
  }

  /**
   * ID Moon Token
   **/
  @ApiModelProperty(value = "ID MOOn Token")
  public String getIdMoonToken() {
    return idMoonToken;
  }
  public void setIdMoonToken(String idMoonToken) {
    this.idMoonToken = idMoonToken;
  }
  
  /**
   * JWT dell&#39;utente
   **/
  @ApiModelProperty(value = "JWT dell'utente")
  public String getJwt() {
    return jwt;
  }
  public void setJwt(String jwt) {
    this.jwt = jwt;
  }

  /**
   * gruppo dell'operatore FO
   **/
  public String getGruppoOperatoreFo() {
	return gruppoOperatoreFo;
  }
  public void setGruppoOperatoreFo(String gruppoOperatoreFo) {
	this.gruppoOperatoreFo = gruppoOperatoreFo;
  }

  	public List<String> getGruppi() {
		return gruppi;
	}
	public void setGruppi(List<String> gruppi) {
		this.gruppi = gruppi;
	}
	public DatiAggiuntivi getDatiAggiuntivi() {
		return datiAggiuntivi;
	}
	public void setDatiAggiuntivi(DatiAggiuntivi datiAggiuntivi) {
		this.datiAggiuntivi = datiAggiuntivi;
	}
	
	public Integer getIdAmbito() {
		return idAmbito;
	}
	public void setIdAmbito(Integer idAmbito) {
		this.idAmbito = idAmbito;
	}
	public String getDescrizioneAmbito() {
		return descrizioneAmbito;
	}
	public void setDescrizioneAmbito(String descrizioneAmbito) {
		this.descrizioneAmbito = descrizioneAmbito;
	}
	
	public Integer getIdFruitore() {
		return idFruitore;
	}
	public void setIdFruitore(Integer idFruitore) {
		this.idFruitore = idFruitore;
	}
	public String getPortalName() {
		return portalName;
	}
	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}
	
	public Boolean isApi() {
		return isApi;
	}
	public void setApi(Boolean isApi) {
		this.isApi = isApi;
	}
	
@Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfo userInfo = (UserInfo) o;
    return Objects.equals(nome, userInfo.nome) &&
        Objects.equals(cognome, userInfo.cognome) &&
        Objects.equals(identificativoUtente, userInfo.identificativoUtente) &&
        Objects.equals(mail, userInfo.mail) &&
        Objects.equals(codFiscDichIstanza, userInfo.codFiscDichIstanza) &&
        Objects.equals(nomeDich, userInfo.nomeDich) &&
        Objects.equals(cognomeDich, userInfo.cognomeDich) &&
        Objects.equals(isOperatore, userInfo.isOperatore) &&
        Objects.equals(isMultiEntePortale, userInfo.isMultiEntePortale) &&
        Objects.equals(ente, userInfo.ente) &&
        Objects.equals(tipoUtente, userInfo.tipoUtente) &&
        Objects.equals(entiAreeRuoli, userInfo.entiAreeRuoli) &&
        Objects.equals(ruoli, userInfo.ruoli) &&
        Objects.equals(funzioni, userInfo.funzioni) &&
        Objects.equals(gruppi, userInfo.gruppi) &&
        Objects.equals(idIride, userInfo.idIride) &&
        Objects.equals(idMoonToken, userInfo.idMoonToken) &&
        Objects.equals(jwt, userInfo.jwt) &&
        Objects.equals(gruppoOperatoreFo, userInfo.gruppoOperatoreFo) &&
        Objects.equals(idAmbito, userInfo.idAmbito) &&
        Objects.equals(descrizioneAmbito, userInfo.descrizioneAmbito) &&
        Objects.equals(idFruitore, userInfo.idFruitore) &&
        Objects.equals(portalName, userInfo.portalName) &&
        Objects.equals(isApi, userInfo.isApi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, cognome, identificativoUtente, mail, codFiscDichIstanza, nomeDich, cognomeDich, isOperatore, isMultiEntePortale, ente, tipoUtente, entiAreeRuoli, ruoli, funzioni, gruppi, idIride, idMoonToken, jwt, gruppoOperatoreFo, idAmbito, descrizioneAmbito, idFruitore, portalName, isApi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    sb.append("class UserInfo {\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    identificativoUtente: ").append(toIndentedString(identificativoUtente)).append("\n");
    sb.append("    mail: ").append(toIndentedString(mail)).append("\n");
    sb.append("    codFiscDichIstanza: ").append(toIndentedString(codFiscDichIstanza)).append("\n");
    sb.append("    nomeDich: ").append(toIndentedString(nomeDich)).append("\n");
    sb.append("    cognomeDich: ").append(toIndentedString(cognomeDich)).append("\n");
    sb.append("    isOperatore: ").append(toIndentedString(isOperatore)).append("\n");
    sb.append("    isMultiEntePortale: ").append(toIndentedString(isMultiEntePortale)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    tipoUtente: ").append(toIndentedString(tipoUtente)).append("\n");
    sb.append("    entiAreeRuoli: ").append(toIndentedString(entiAreeRuoli)).append("\n");
    sb.append("    ruoli: ").append(toIndentedString(ruoli)).append("\n");
    sb.append("    funzioni: ").append(toIndentedString(funzioni)).append("\n");
    sb.append("    gruppi: ").append(toIndentedString(gruppi)).append("\n");
    sb.append("    idIride: ").append(toIndentedString(idIride)).append("\n");
    sb.append("    idMoonToken: ").append(toIndentedString(idMoonToken)).append("\n");
    sb.append("    jwt: ").append(toIndentedString(jwt)).append("\n");
    sb.append("    gruppoOperatoreFo: ").append(toIndentedString(gruppoOperatoreFo)).append("\n");
    sb.append("    datiAggiuntivi: ").append(datiAggiuntivi==null?null:datiAggiuntivi).append("\n");
    sb.append("    idAmbito: ").append(toIndentedString(idAmbito)).append("\n");
    sb.append("    desrizioneAmbito: ").append(toIndentedString(descrizioneAmbito)).append("\n");
    sb.append("    idFruitore: ").append(toIndentedString(idFruitore)).append("\n");
    sb.append("    portalName: ").append(toIndentedString(portalName)).append("\n");
    sb.append("    isApi: ").append(toIndentedString(isApi)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   * protected, exists extended class like LoginResponse in moonfobl and modulistica components
   */
  protected String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

