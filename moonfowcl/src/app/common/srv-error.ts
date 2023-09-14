/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class SrvError {
  code: string;
  errorMessage: string;
  fields: string;
  messaggioCittadino: string;
  sessionExpired?: boolean;
}
