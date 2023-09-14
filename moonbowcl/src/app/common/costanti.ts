/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export const  STORAGE_KEYS = {
  USER_KEY:  'bo-user',
  MOON_JWT_TOKEN: 'bo-jwt-moon',
  MODATTR:'bo-modattr',
  MODULO_SELEZIONATO:'bo-modulo-selezionato',
  MODULO_SELEZ_REPORT:'bo-modulo-selez-report',
  KEY_CRITERIO_RICERCA: 'bo-criterio-ricerca',
  KEY_ISTANZE_RISULTATO_RICERCA: 'bo-istanze-risultato-ricerca',
  KEY_ISTANZE_RICERCA_TOTALI: 'bo-istanze-ricerca-totali',
  KEY_ISTANZE_BOZZA: 'bo-istanze-bozza',
};

export class Costanti {
  public static ID_TAB_BOZZE = 1;
  public static ID_TAB_INVIATE = 2;
  //
  public static ISTANZA_STATO_BOZZA = 1;
  public static ISTANZA_STATO_INVIATA = 2;
  public static ISTANZA_STATO_RICHIESTA_INTEGRAZIONE = 15;
  public static ISTANZA_NO_IMPORTANTE = 0;
  public static ISTANZA_IMPORTANTE_STAR = 1;
  public static TIPO_STRUTTURA_FORM = "FRM";
  public static TIPO_STRUTTURA_WIZARD = "WIZ";

  public static DATE_FORMAT = 'DD/MM/YYYY';
  public static DATE_TIME_FORMAT = 'DD/MM/YYYY HH:mm:ss';
};

// export class  CODICE_MODULO {
//  public static VOUCHER:  'VOUCHER'
// };

export const  CODICE_MODULO  = {
  VOUCHER:  'VOUCHER',
  ESTA_RAGA_21: 'ESTA_RAGA_21'
 };

 export const NOME_AZIONE = {
  DINIEGO: 'Diniego',
  ACCOGLIMENTO: 'Accoglimento',
  GENERA_RICEVUTA: 'Genera ricevuta'

 };

 export const MODULO  = {
    ID_MODULO: 'ID_MODULO',
    ID_VERSIONE_MODULO: 'ID_VERSIONE_MODULO'
 }

 export const EPAY  = {
  ALL: 'all',
  PAID: 'paid',
  UNPAID: 'unpaid'
}

export const AUTH_MODES = {
  LOGIN_USER_PWD: 'LOGIN_PWD',
  GOOGLE: 'GOOGLE'
};

export const LIST_CRM_SYSTEM = ['NEXTCRM','R2U','OTRS'];

export const SYSTEM_TICKET = {
  NEXTCRM : 1,
  R2U : 2,
  OTRS : 3
}