/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util;

public class IrideIdentitaDigitale {
    private String nome;
    private String cognome;
    private String codFiscale;
    private String idProvider;
    private String timestamp;
    private int livelloAutenticazione;
    private String mac;
    public static final int AUTENTICAZIONE_USERNAME_PASSWORD_UNVERIFIED = 1;
    public static final int AUTENTICAZIONE_USERNAME_PASSWORD = 2;
    public static final int AUTENTICAZIONE_USERNAME_PASSWORD_PIN = 4;
    public static final int AUTENTICAZIONE_CERTIFICATO = 8;
    public static final int AUTENTICAZIONE_CERTIFICATO_FORTE = 16;
    
    public IrideIdentitaDigitale(final String codFiscale, final String nome, final String cognome, final String idProvider, final String timestamp, final int livelloAutenticazione) {
        this.codFiscale = codFiscale;
        this.nome = nome;
        this.cognome = cognome;
        this.idProvider = idProvider;
        this.timestamp = timestamp;
        this.livelloAutenticazione = livelloAutenticazione;
    }
    
    public IrideIdentitaDigitale() {
        this.codFiscale = null;
        this.nome = null;
        this.cognome = null;
        this.idProvider = null;
        this.timestamp = null;
        this.livelloAutenticazione = 0;
    }
    
    public IrideIdentitaDigitale(final String token) throws Exception {
        final int slash1Index = token.indexOf(47);
        if (slash1Index == -1) {
            throw new Exception(token);
        }
        this.codFiscale = token.substring(0, slash1Index);
        final int slash2Index = token.indexOf(47, slash1Index + 1);
        if (slash2Index == -1) {
            throw new Exception(token);
        }
        this.nome = token.substring(slash1Index + 1, slash2Index);
        final int slash3Index = token.indexOf(47, slash2Index + 1);
        if (slash3Index == -1) {
            throw new Exception(token);
        }
        this.cognome = token.substring(slash2Index + 1, slash3Index);
        final int slash4Index = token.indexOf(47, slash3Index + 1);
        if (slash4Index == -1) {
            throw new Exception(token);
        }
        this.idProvider = token.substring(slash3Index + 1, slash4Index);
        final int slash5Index = token.indexOf(47, slash4Index + 1);
        if (slash5Index == -1) {
            throw new Exception(token);
        }
        this.timestamp = token.substring(slash4Index + 1, slash5Index);
        final int slash6Index = token.indexOf(47, slash5Index + 1);
        if (slash6Index == -1) {
            throw new Exception(token);
        }
        this.livelloAutenticazione = Integer.parseInt(token.substring(slash5Index + 1, slash6Index));
        this.mac = token.substring(slash6Index + 1);
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public String getCognome() {
        return this.cognome;
    }
    
    public String getCodFiscale() {
        return this.codFiscale;
    }
    
    public String getIdProvider() {
        return this.idProvider;
    }
    
    public String getTimestamp() {
        return this.timestamp;
    }
    
    public int getLivelloAutenticazione() {
        return this.livelloAutenticazione;
    }
    
    public String getMac() {
        return this.mac;
    }
    
    public void setMac(final String mac) {
        this.mac = mac;
    }
    
    public boolean equals(final Object obj) {
        if (obj != null && this.getClass().equals(obj.getClass())) {
            final IrideIdentitaDigitale that = (IrideIdentitaDigitale)obj;
            return ((this.nome == null && that.nome == null) || (this.nome != null && this.nome.equals(that.nome))) && ((this.cognome == null && that.cognome == null) || (this.cognome != null && this.cognome.equals(that.cognome))) && ((this.codFiscale == null && that.codFiscale == null) || (this.codFiscale != null && this.codFiscale.equals(that.codFiscale)));
        }
        return false;
    }
    
    public int hashCode() {
        return String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.codFiscale))).append(this.nome).append(this.cognome).append(this.mac))).hashCode();
    }
    
    public String toString() {
        return String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.codFiscale))).append("/").append(this.nome).append("/").append(this.cognome).append("/").append(this.idProvider).append("/").append(this.timestamp).append("/").append(this.livelloAutenticazione).append("/").append(this.mac)));
    }
    
    public String getRappresentazioneInterna() {
        return String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.codFiscale))).append("/").append(this.nome).append("/").append(this.cognome).append("/").append(this.idProvider).append("/").append(this.timestamp).append("/").append(this.livelloAutenticazione)));
    }
    
    public void setNome(final String nome) {
        this.nome = nome;
    }
    
    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setLivelloAutenticazione(final int livelloAutenticazione) {
        this.livelloAutenticazione = livelloAutenticazione;
    }
    
    public void setIdProvider(final String idProvider) {
        this.idProvider = idProvider;
    }
    
    public void setCognome(final String cognome) {
        this.cognome = cognome;
    }
    
    public void setCodFiscale(final String codFiscale) {
        this.codFiscale = codFiscale;
    }
}
