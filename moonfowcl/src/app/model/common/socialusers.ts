/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


export class Socialusers {


  provider: string;
  id: string;
  email: string;
  name: string;

  token?: string;
  idToken?: string;


  photoUrl: string;
  firstName: string;
  lastName: string;
  authToken: string;

  authorizationCode: string;
  response: any;
}
