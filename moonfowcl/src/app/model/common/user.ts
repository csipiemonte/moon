/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {UserInfo} from './user-info';
import {DichiaranteIstanza} from './dichiarante-istanza';
import {UserIF} from './user-if';
import {EmbeddedNavigator} from './embedded-navigator';
import {ConsumerParams} from './consumer-params';

export class User extends UserInfo {
  mail: string;
  tipoUtente: number;
  entiAreeRuoli: any;
  idMoonToken: string;
  jwt: string;
  modulo: any;
  operatore: string;
  dichiaranteIstanza: DichiaranteIstanza;
  embeddedNavigator: EmbeddedNavigator;
  idFruitore: string;
  consumerParams: ConsumerParams;

  constructor(userIF: UserIF) {
    super(userIF);
    this.dichiaranteIstanza = new DichiaranteIstanza(userIF.nomeDich, userIF.cognomeDich, userIF.codFiscDichIstanza);
    this.mail = userIF.mail;
    this.tipoUtente = userIF.tipoUtente;
    this.entiAreeRuoli = userIF.entiAreeRuoli;
    this.idMoonToken = userIF.idMoonToken;
    this.jwt = userIF.jwt;
    this.modulo = userIF.modulo;
    this.operatore = userIF.operatore;
    this.embeddedNavigator = userIF.embeddedNavigator;
    this.idFruitore= userIF.idFruitore;
    this.consumerParams = userIF.consumerParams;
  }

}
