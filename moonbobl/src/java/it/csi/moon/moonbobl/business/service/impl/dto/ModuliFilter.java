/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Optional;

import it.csi.moon.moonbobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonbobl.util.decodifica.DecodificaStatoModulo;

/**
 * Filter DTO usato dal ModuloDAO per le ricerche dei moduli
 * 
 * @see ModuloDAO
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuliFilter {

	public static final Integer VISIBILITA_AMBITO_PUBLIC = 1;
	
	private Long idModulo;
	private Long idVersioneModulo;
	private String codiceModulo;
	private String versioneModulo;
	private String oggettoModulo;
	private String descrizioneModulo;
	private String flagIsRiservato;
	private Integer idTipoCodiceIstanza;
	private String flagProtocolloIntegrato;
	private DecodificaStatoModulo statoModulo;
	private boolean findAllCronologie; // default false only current cronologia
	private Boolean conPresenzaIstanze;
	private String conPresenzaIstanzeUser;
	private String utenteAbilitato;
	private String nomePortale;
	private boolean onlyLastVersione;
	private Long idEnte;
	private Long idArea;
	private Integer idAmbito;
	private Integer idVisibilitaAmbito;
	private String attributoPresente;
	
	public ModuliFilter() {}
	
	public Optional<Long> getIdModulo() {
		return Optional.ofNullable(idModulo);
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Optional<Long> getIdVersioneModulo() {
		return Optional.ofNullable(idVersioneModulo);
	}
	public void setIdVersioneModulo(Long idVersioneModulo) {
		this.idVersioneModulo = idVersioneModulo;
	}
	public Optional<String> getCodiceModulo() {
		return Optional.ofNullable(codiceModulo);
	}
	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}
	public Optional<String> getVersioneModulo() {
		return Optional.ofNullable(versioneModulo);
	}
	public void setVersioneModulo(String versioneModulo) {
		this.versioneModulo = versioneModulo;
	}
	public Optional<String> getOggettoModulo() {
		return Optional.ofNullable(oggettoModulo);
	}
	public void setOggettoModulo(String oggettoModulo) {
		this.oggettoModulo = oggettoModulo;
	}
	public Optional<String> getDescrizioneModulo() {
		return Optional.ofNullable(descrizioneModulo);
	}
	public void setDescrizioneModulo(String descrizioneModulo) {
		this.descrizioneModulo = descrizioneModulo;
	}
	public Optional<String> getFlagIsRiservato() {
		return Optional.ofNullable(flagIsRiservato);
	}
	public void setFlagIsRiservato(String flagIsRiservato) {
		this.flagIsRiservato = flagIsRiservato;
	}
	public Optional<Integer> getIdTipoCodiceIstanza() {
		return Optional.ofNullable(idTipoCodiceIstanza);
	}
	public void setIdTipoCodiceIstanza(Integer idTipoCodiceIstanza) {
		this.idTipoCodiceIstanza = idTipoCodiceIstanza;
	}
	public Optional<String> getFlagProtocolloIntegrato() {
		return Optional.ofNullable(flagProtocolloIntegrato);
	}
	public void setFlagProtocolloIntegrato(String flagProtocolloIntegrato) {
		this.flagProtocolloIntegrato = flagProtocolloIntegrato;
	}
	public Optional<DecodificaStatoModulo> getStatoModulo() {
		return Optional.ofNullable(statoModulo);
	}
	public void setStatoModulo(DecodificaStatoModulo statoModulo) {
		this.statoModulo = statoModulo;
	}
	public boolean isFindAllCronologie() {
		return findAllCronologie;
	}
	public void setFindAllCronologie(boolean findAllCronologie) {
		this.findAllCronologie = findAllCronologie;
	}
	public Optional<Boolean> getConPresenzaIstanze() {
		return Optional.ofNullable(conPresenzaIstanze);
	}
	public void setConPresenzaIstanze(Boolean conPresenzaIstanze) {
		this.conPresenzaIstanze = conPresenzaIstanze;
	}
	public Optional<String> getConPresenzaIstanzeUser() {
		return Optional.ofNullable(conPresenzaIstanzeUser);
	}
	public void setConPresenzaIstanzeUser(String conPresenzaIstanzeUser) {
		this.conPresenzaIstanzeUser = conPresenzaIstanzeUser;
	}
	public Optional<String> getUtenteAbilitato() {
		return Optional.ofNullable(utenteAbilitato);
	}
	public void setUtenteAbilitato(String utenteAbilitato) {
		this.utenteAbilitato = utenteAbilitato;
	}
	public Optional<String> getNomePortale() {
		return Optional.ofNullable(nomePortale);
	}
	public void setNomePortale(String nomePortale) {
		this.nomePortale = nomePortale;
	}
	public boolean isOnlyLastVersione() {
		return onlyLastVersione;
	}
	public void setOnlyLastVersione(boolean onlyLastVersione) {
		this.onlyLastVersione = onlyLastVersione;
	}
	public Optional<Long> getIdEnte() {
		return Optional.ofNullable(idEnte);
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public Optional<Long> getIdArea() {
		return Optional.ofNullable(idArea);
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Optional<Integer> getIdAmbito() {
		return Optional.ofNullable(idAmbito);
	}
	public void setIdAmbito(Integer idAmbito) {
		this.idAmbito = idAmbito;
	}
	public Optional<Integer> getIdVisibilitaAmbito() {
		return Optional.ofNullable(idVisibilitaAmbito);
	}
	public void setIdVisibilitaAmbito(Integer idVisibilitaAmbito) {
		this.idVisibilitaAmbito = idVisibilitaAmbito;
	}
	public Optional<String> getAttributoPresente() {
		return Optional.ofNullable(attributoPresente);
	}
	public void setAttributoPresente(String attributoPresente) {
		this.attributoPresente = attributoPresente;
	}
	
	//
	// INNER BUILDER
	public static class Builder {

		private Long _idModulo;
		private Long _idVersioneModulo;
		private String _codiceModulo;
		private String _versioneModulo;
		private String _oggettoModulo;
		private String _descrizioneModulo;
		private String _flagIsRiservato;
		private Integer _idTipoCodiceIstanza;
		private String _flagProtocolloIntegrato;
		private DecodificaStatoModulo _statoModulo;
		private boolean _findAllCronologie; // default false only current cronologia
		private Boolean _conPresenzaIstanze;
		private String _conPresenzaIstanzeUser;
		private String _utenteAbilitato;
		private String _nomePortale;
		private boolean _onlyLastVersione;
		private Long _idEnte;
		private Long _idArea;
		private Integer _idAmbito;
		private Integer _idVisibilitaAmbito;
		
		public Builder idModulo(Long idModulo) {
			this._idModulo = idModulo;
			return this;
		}
		public Builder idVersioneModulo(Long idVersioneModulo) {
			this._idVersioneModulo = idVersioneModulo;
			return this;
		}
		public Builder codiceModulo(String codiceModulo) {
			this._codiceModulo = codiceModulo;
			return this;
		}
		public Builder versioneModulo(String versioneModulo) {
			this._versioneModulo = versioneModulo;
			return this;
		}
		public Builder oggettoModulo(String oggettoModulo) {
			this._oggettoModulo = oggettoModulo;
			return this;
		}
		public Builder descrizioneModulo(String descrizioneModulo) {
			this._descrizioneModulo = descrizioneModulo;
			return this;
		}
		public Builder flagIsRiservato(String flagIsRiservato) {
			this._flagIsRiservato = flagIsRiservato;
			return this;
		}
		public Builder idTipoCodiceIstanza(Integer idTipoCodiceIstanza) {
			this._idTipoCodiceIstanza = idTipoCodiceIstanza;
			return this;
		}
		public Builder flagProtocolloIntegrato(String flagProtocolloIntegrato) {
			this._flagProtocolloIntegrato = flagProtocolloIntegrato;
			return this;
		}
		public Builder statoModulo(DecodificaStatoModulo statoModulo) {
			this._statoModulo = statoModulo;
			return this;
		}
		public Builder findAllCronologie(boolean findAllCronologie) {
			this._findAllCronologie = findAllCronologie;
			return this;
		}
		public Builder conPresenzaIstanze(Boolean conPresenzaIstanze) {
			this._conPresenzaIstanze = conPresenzaIstanze;
			return this;
		}
		public Builder conPresenzaIstanzeUser(String conPresenzaIstanzeUser) {
			this._conPresenzaIstanzeUser = conPresenzaIstanzeUser;
			return this;
		}
		public Builder utenteAbilitato(String utenteAbilitato) {
			this._utenteAbilitato = utenteAbilitato;
			return this;
		}
		public Builder nomePortale(String nomePortale) {
			this._nomePortale = nomePortale;
			return this;
		}
		public Builder onlyLastVersione(boolean onlyLastVersione) {
			this._onlyLastVersione = onlyLastVersione;
			return this;
		}
		public Builder idEnte(Long idEnte) {
			this._idEnte = idEnte;
			return this;
		}
		public Builder idArea(Long idArea) {
			this._idArea = idArea;
			return this;
		}
		public Builder idAmbito(Integer idAmbito) {
			this._idAmbito = idAmbito;
			return this;
		}
		public Builder idVisibilitaAmbito(Integer idVisibilitaAmbito) {
			this._idVisibilitaAmbito = idVisibilitaAmbito;
			return this;
		}
		
		public ModuliFilter build() {
			return new ModuliFilter(this);
		}
	}
	
	public ModuliFilter(Builder builder) {
		 setIdModulo(builder._idModulo);
		 setIdVersioneModulo(builder._idVersioneModulo);
		 setCodiceModulo(builder._codiceModulo);
		 setVersioneModulo(builder._versioneModulo);
		 setOggettoModulo(builder._oggettoModulo);
		 setDescrizioneModulo(builder._descrizioneModulo);
		 setFlagIsRiservato(builder._flagIsRiservato);
		 setIdTipoCodiceIstanza(builder._idTipoCodiceIstanza);
		 setFlagProtocolloIntegrato(builder._flagProtocolloIntegrato);
		 setStatoModulo(builder._statoModulo);
		 setFindAllCronologie(builder._findAllCronologie);
		 setConPresenzaIstanze(builder._conPresenzaIstanze);
		 setConPresenzaIstanzeUser(builder._conPresenzaIstanzeUser);
		 setUtenteAbilitato(builder._utenteAbilitato);
		 setNomePortale(builder._nomePortale);
		 setOnlyLastVersione(builder._onlyLastVersione);
		 setIdEnte(builder._idEnte);
		 setIdArea(builder._idArea);
		 setIdAmbito(builder._idAmbito);
		 setIdVisibilitaAmbito(builder._idVisibilitaAmbito);
	}

	@Override
	public String toString() {
		return "ModuliFilter [idModulo=" + idModulo + ", idVersioneModulo=" + idVersioneModulo + ", codiceModulo="
				+ codiceModulo + ", versioneModulo=" + versioneModulo + ", oggettoModulo=" + oggettoModulo
				+ ", descrizioneModulo=" + descrizioneModulo + ", flagIsRiservato=" + flagIsRiservato
				+ ", idTipoCodiceIstanza=" + idTipoCodiceIstanza + ", flagProtocolloIntegrato="
				+ flagProtocolloIntegrato + ", statoModulo=" + statoModulo + ", findAllCronologie=" + findAllCronologie
				+ ", conPresenzaIstanze=" + conPresenzaIstanze + ", conPresenzaIstanzeUser=" + conPresenzaIstanzeUser
				+ ", utenteAbilitato=" + utenteAbilitato + ", nomePortale=" + nomePortale + ", onlyLastVersione="
				+ onlyLastVersione + ", idEnte=" + idEnte + ", idArea=" + idArea + ", idAmbito=" + idAmbito
				+ ", idVisibilitaAmbito=" + idVisibilitaAmbito 
				+ ", attributoPresente=" + attributoPresente + "]";
	}
	
}
