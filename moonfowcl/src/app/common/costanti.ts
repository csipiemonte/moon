/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export const AUTH_MODES = {
  LOGIN_USER_PWD: 'LOGIN_PWD',
  GOOGLE: 'GOOGLE'
};

export const HEADERS_MOON_IDENTITA_JWT = 'Moon-Identita-JWT';

export const STORAGE_APP_PREFIX = 'fo-';

export enum STORAGE_KEYS {
  USER = 'fo-USER',
  JWT_MOON = 'fo-MOON_IDENTITA_JWT',
  CRITERIO_RICERCA = 'fo-CRITERIO_RICERCA',
  ISTANZE_SEARCH_RESULT = 'fo-RISULTATO_RICERCA',
  ISTANZE_RICERCA_TOTALI = 'fo-ISTANZE_TOTALI',
  ISTANZE_BOZZA = 'fo-ISTANZE_BOZZA',
};

export class AZIONI {
  public static INVIA_INTEGRAZIONE = 35;
  public static CARICA_RICEVUTA_FIRMATA = 71;
};

export const NOME_AZIONE = {
  DINIEGO: 'Diniego',
  ACCOGLIMENTO: 'Accoglimento',
  GENERA_RICEVUTA: 'Genera ricevuta'

};

export class STATI {
  public static IN_ATTESA_INTEGRAZIONE = 15;
  public static IN_ATTESA_PAGAMENTO = 54;
  public static IN_ATTESA_OSSERVAZIONI = 57;
  public static ACCOLTA = 9;
  public static NOTIFICATA = 33;
  public static RESPINTA = 34;
  public static DEFINITA = 50;
  public static INVIATA = 2;
  public static DA_INVIARE = 10;
  public static BOZZA = 1;
  public static ATTESA_RICEVUTA_PAGAMENTO = 54;
  public static IN_PAGAMENTO = 23;
  public static IN_PAGAMENTO_ONLINE = 47;
  public static ATTESA_PAGAMENTO = 48;
  public static DA_PAGARE = 22;
  public static ELIMINATA = 3;
};

export class MODULI {
  public static PAGO_PA = 'PAGO_PA';
  public static PAGO_PA_ONLINE = 'PAGO_PA_ONLINE';
};

export const CODICE_MODULO = {
  VOUCHER: 'VOUCHER',
  ESTA_RAGA_21: 'ESTA_RAGA_21'
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
  public static IN_ATTESA_DI_RICEVUTA_PAGAMENTO = 54;
  public static IN_PAGAMENTO = 23;
  public static IN_PAGAMENTO_ONLINE = 47;
  public static ATTESA_ESITO_PAGAMENTO = 48;
};
