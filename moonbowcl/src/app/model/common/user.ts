/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {UserInfo} from './user-info';
import {LoginResponseIF} from './login-response';
import { TipoUtenteIF } from './tipo-utente-if';

export class User extends UserInfo {
  nome: string = null;
  cognome: string = null;
  mail: string;
  tipoUtente: TipoUtenteIF;
  entiAreeRuoli: any;
  jwt: string;
  modulo: any;
  operatore: string;
 // dichiaranteIstanza: DichiaranteIstanza;

  constructor(loginResponse: LoginResponseIF) {
    super();
    //super(loginResponse);
    //this.dichiaranteIstanza = new DichiaranteIstanza(loginResponse.nomeDich, loginResponse.cognomeDich, loginResponse.codFiscDichIstanza);
    this.nome = loginResponse.nome;
    this.cognome = loginResponse.cognome;
    this.mail = loginResponse.mail;
    this.tipoUtente = loginResponse.tipoUtente;
    this.entiAreeRuoli = loginResponse.entiAreeRuoli;
    this.jwt = loginResponse.jwt;
    this.modulo = loginResponse.modulo;
    this.operatore = loginResponse.operatore;
  }

}
