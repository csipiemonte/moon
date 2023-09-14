/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Istanza} from './istanza';

export interface IstanzaSaveResponse {
  istanza: Istanza;
  codice: string;
  titolo: string;
  descrizione: string;
  includeDescrizioneInEmail: boolean;
  urlRedirect: string;
}
