/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/*
Oggetto che contiene in sessione elenco delle istanze selezionbate
 */
import {Istanza} from './istanza';

export class SessioneIstanze {
  elementiTotali: number;
  righeIstanza: Istanza[];
}
