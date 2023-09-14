/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


/**
 * DTO relativo al ComplexType UserInfo.
 * @generated
 */

//fixme import { exit } from 'process';
// commento effettuato nel porting verso Angular 12
import { AreaRuolo } from '../dto/area-ruolo';
import { Ente } from '../dto/ente';
import { EnteAreaRuolo } from '../dto/ente-area-ruolo';
import {Ruolo} from './ruolo';
import {TipoUtenteIF} from './tipo-utente-if';
import { LoginResponseIF } from './login-response';

enum TIPOLOGIE_UTENTE {
  ADMIN = 'ADM'
}

enum RUOLI {
  OP_ADM = 'OP_ADM',
  OP_ADV = 'OP_ADV',
  OP_SIMP = 'OP_SIMP',
  OP_CON = 'OP_CON',
  RESP = 'RESP',
  ADMIN = 'ADMIN',
  OP_COMP = 'OP_COMP',
  OP_SIMPMOD = 'OP_SIMPMOD',
}

export class UserInfo  {
  nome: string = null;
  cognome: string = null;
  codFisc: string = null;
  identificativoUtente: string = null;
  ente: Ente = null;
  idEnte: number;
  tipoUtente: TipoUtenteIF;
  jwt: string;
  ruoli: Ruolo[];
  idIride: string = null;
  funzioni: string[];
  isAuthenticated  = false;
  multiEntePortale = false;
  entiAreeRuoli: EnteAreaRuolo[];
  urlLogout = null;
  photoUrl = null;
  authMode = 'IRIDE';

  constructor() {
    this.ruoli = [];
  }


  static createUserInfo(loginResponse: LoginResponseIF) : UserInfo {
    var result = new UserInfo();
    result.nome = loginResponse.nome;
    result.cognome = loginResponse.cognome;
    result.codFisc = loginResponse.identificativoUtente;
    result.identificativoUtente = loginResponse.identificativoUtente;
    result.ente = loginResponse.ente;
    //this.descrizioneAmbito = loginResponse.descrizioneAmbito;
    result.ruoli = loginResponse.ruoli;
    result.idIride = loginResponse.idIride;
    result.funzioni = loginResponse.funzioni;
    //this.gruppi = loginResponse.gruppi;
    result.multiEntePortale = loginResponse.multiEntePortale;
    result.urlLogout = loginResponse.urlLogout;
    result.photoUrl = loginResponse.photoUrl;
    result.urlLogout = loginResponse.urlLogout;
    result.authMode = loginResponse.authMode;
    result.tipoUtente = loginResponse.tipoUtente;
    result.entiAreeRuoli = loginResponse.entiAreeRuoli;

    return result;
  }

  isTipoADMIN() {
    return this.tipoUtente.codice === TIPOLOGIE_UTENTE.ADMIN;
  }

  hasRuoloOperatorADM() {
    return this.hasRuolo(RUOLI.OP_ADM);
  }
  hasRuoloOperatorADV() {
    return this.hasRuolo(RUOLI.OP_ADV);
  }
  hasRuoloOperatorSIMP() {
    return this.hasRuolo(RUOLI.OP_SIMP);
  }
  hasRuoloOperatorSIMPMOD() {
    return this.hasRuolo(RUOLI.OP_SIMPMOD);
  }
  hasRuoloOperatorCON() {
      return this.hasRuolo(RUOLI.OP_CON);
  }
  hasRuoloRESP() {
    return this.hasRuolo(RUOLI.RESP);
  }
  hasRuoloOperatorCOMP() {
    return this.hasRuolo(RUOLI.OP_COMP);
  }

  // hasRuoloOperatoreMin :: solo su hierarchia voluta ::  ADM > OP_ADM > OP_ADV > OP_SIMP > OP_CON
  hasRuoloOperatorMinCON() {
    return this.hasRuolo(RUOLI.OP_ADM) || this.hasRuolo(RUOLI.OP_ADV) || this.hasRuolo(RUOLI.OP_SIMP) || this.hasRuolo(RUOLI.OP_CON) || this.hasRuolo(RUOLI.OP_SIMPMOD)
      || this.hasRuolo(RUOLI.ADMIN) || this.isTipoADMIN();
  }
  hasRuoloOperatorMinSIMP() {
    return this.hasRuolo(RUOLI.OP_ADM) || this.hasRuolo(RUOLI.OP_ADV) || this.hasRuolo(RUOLI.OP_SIMP) || this.hasRuolo(RUOLI.OP_SIMPMOD)
      || this.hasRuolo(RUOLI.ADMIN) || this.isTipoADMIN();
  }
  hasRuoloOperatorMinADV() {
    return this.hasRuolo(RUOLI.OP_ADM) || this.hasRuolo(RUOLI.OP_ADV)
      || this.hasRuolo(RUOLI.ADMIN) || this.isTipoADMIN();
  }
  hasRuoloOperatorMinADM() {
    return this.hasRuolo(RUOLI.OP_ADM)
      || this.hasRuolo(RUOLI.ADMIN) || this.isTipoADMIN();
  }

  private hasRuolo(ruolo: string): boolean {
    if (this.entiAreeRuoli != null && this.entiAreeRuoli.length > 0 ) {
      for (const enteAreeRuoli of this.entiAreeRuoli) {
          const areeRuoli: AreaRuolo[] = enteAreeRuoli['areeRuoli'];
          for (const areaRuoli of areeRuoli) {
               if (areaRuoli.codiceRuolo === ruolo) {
                 return true;
               }
          }
      }
    }
    return false;
  }
}

