/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

// Interfaccia relativa alla response dei servizi di autenticazione


import {Ente} from '../dto/ente';
import { Ruolo } from './ruolo';
import { TipoUtenteIF } from './tipo-utente-if';

export interface LoginResponseIF {
  nome: string;
  cognome: string;
  identificativoUtente: string;
  ente: Ente;
  //ruoli: string[];
  ruoli: Ruolo[];
  idIride: string;
  funzioni: string[];
  gruppi: string[];
  //codFiscDichIstanza: string;
  //nomeDich: string;
  //cognomeDich: string;
  mail: string;
  //tipoUtente: number;
  tipoUtente: TipoUtenteIF;
  entiAreeRuoli: any;
  idMoonToken: string;
  jwt: string;
  modulo: any;
  operatore: string;
  multiEntePortale: boolean;
  urlLogout: string;
  photoUrl: string;
  authMode: string;
  //descrizioneAmbito: string;
}
