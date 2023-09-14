/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


/**
 * DTO relativo al ComplexType UserInfo.
 * @generated
 */

import {Ente} from '../dto/ente';
import {UserIF} from './user-if';

export class UserInfo {
  nome: string = null;
  cognome: string = null;
  codFisc: string = null;
  ente: Ente;
  descrizioneAmbito: string;
  ruoli: string[];
  idIride: string = null;
  funzioni: string[];
  gruppi: string[];
  isAuthenticated = false;
  multiEntePortale = false;
  urlLogout = null;
  photoUrl = null;
  authMode = 'IRIDE';
  gruppoOperatoreFo: string = null;


  constructor(userIF: UserIF) {
    this.nome = userIF.nome;
    this.cognome = userIF.cognome;
    this.codFisc = userIF.identificativoUtente;
    this.ente = userIF.ente;
    this.descrizioneAmbito = userIF.descrizioneAmbito;
    this.ruoli = userIF.ruoli;
    this.idIride = userIF.idIride;
    this.funzioni = userIF.funzioni;
    this.gruppi = userIF.gruppi;
    this.multiEntePortale = userIF.multiEntePortale;
    this.urlLogout = userIF.urlLogout;
    this.photoUrl = userIF.photoUrl;
    this.urlLogout = userIF.urlLogout;
    this.authMode = userIF.authMode;
    this.gruppoOperatoreFo = userIF.gruppoOpertoreFo;
  }
}
