/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export enum VALUTAZIONE {
  BUONO = 5,
  CONTENTO = 4,
  MEDIO = 3,
  NONCONTENTO = 2,
  CATTIVO = 1
}


export class Valutazione {
  trovaModulo: boolean;
  compilaModulo: boolean;
  inviaIstanza: boolean;
  trovaIstanza: boolean;
  comeProcedere: boolean;
  aspettoGrafico: boolean;
  visualizzaResponsive: boolean;
  proceduraCompilaInvio: boolean;
  sezioneAiuto: boolean;
  dettaglio: string;

}

export class ValutazioneModulo {
  idIstanza: string;
  idModulo: number;
  idVersioneModulo: number;
  idValutazione: number;
  valutazione: Valutazione;
}





