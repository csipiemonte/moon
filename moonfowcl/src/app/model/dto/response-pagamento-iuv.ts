/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/*
Interfaccia di risposta del servizio pagamentoIUV
 */

export interface ResponsePagamentoIUV {
  identificativoPagamento: string;
  codiceEsito: string;
  descrizioneEsito: string;
  urlWisp: string;
}
