/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

// Interfaccia relativa alla response dei servizi di autenticazione


import {Ente} from '../dto/ente';
import {ConsumerParams} from './consumer-params';
import {EmbeddedNavigator} from './embedded-navigator';

export interface UserIF {
  nome: string;
  cognome: string;
  identificativoUtente: string;
  ente: Ente;
  ruoli: string[];
  idIride: string;
  funzioni: string[];
  gruppi: string[];
  codFiscDichIstanza: string;
  nomeDich: string;
  cognomeDich: string;
  mail: string;
  tipoUtente: number;
  entiAreeRuoli: any;
  idMoonToken: string;
  jwt: string;
  modulo: any;
  operatore: string;
  multiEntePortale: boolean;
  urlLogout: string;
  photoUrl: string;
  authMode: string;
  descrizioneAmbito: string;
  embeddedNavigator: EmbeddedNavigator;
  idFruitore: string;
  consumerParams: ConsumerParams;
  gruppoOpertoreFo: string;
}
