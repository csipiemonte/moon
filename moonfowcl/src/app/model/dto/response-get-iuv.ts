/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/*
  Interfaccia risposta del servizio getIUVChiamanteEsterno
 */

export interface ResponseGetIUV {
  identificativoPagamento: string;
  iuv: string;
  codiceAvviso: string;
  codiceEsito: string;
  descrizioneEsito: string;

}
