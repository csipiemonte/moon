/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {HEADERS_MOON_IDENTITA_JWT} from './../common/costanti';
import {Injectable} from '@angular/core';
import {HttpXsrfTokenExtractor} from '@angular/common/http';
import {StorageManager} from '../util/storage-manager';
import {STORAGE_KEYS} from 'src/app/common/costanti';

export const CUSTOM_TOKEN_PLUGIN_NAME = 'custom_token_plugin';

const TOKEN_KEY = 'X-XSRF-TOKEN';
const FAKE_IDENTITY_KEY = 'Shib-Iride-IdentitaDigitale';

@Injectable()
export class CustomTokenPlugin {


  constructor(private tokenExtractor: HttpXsrfTokenExtractor) {
  }

  priority = 0;

  preRequest(requestArgs: any) {
    // tslint:disable-next-line:no-console
    console.debug('FormIo Rest request intercepted: ', requestArgs);
    if (requestArgs.method === 'upload') {
      // tslint:disable-next-line:no-console
      console.debug('FormIo Rest request ignored because file uploads are handled in a different way');
      return;
    }

    const jwt = StorageManager.get(STORAGE_KEYS.JWT_MOON);


    if (requestArgs.opts) {
      if (requestArgs.opts.header) {
        requestArgs.opts.header.set(HEADERS_MOON_IDENTITA_JWT, jwt);
      } else if (requestArgs.opts.headers) {
        requestArgs.opts.headers[HEADERS_MOON_IDENTITA_JWT] = jwt;
      } else {
        requestArgs.opts.header = new Headers();
        requestArgs.opts.header.set(HEADERS_MOON_IDENTITA_JWT, jwt);
        requestArgs.opts.headers = {};
        requestArgs.opts.headers[HEADERS_MOON_IDENTITA_JWT] = jwt;
      }
    }
    // const token = this.tokenExtractor.getToken() as string;
    // if (token && requestArgs.opts) {

    //     if (requestArgs.opts.header) {
    //         requestArgs.opts.header.set(TOKEN_KEY, token);
    //     } else if (requestArgs.opts.headers) {
    //         requestArgs.opts.headers[TOKEN_KEY] = token;
    //     } else {
    //         requestArgs.opts.header = new Headers();
    //         requestArgs.opts.header.set(TOKEN_KEY, token);
    //         requestArgs.opts.headers = {};
    //         requestArgs.opts.headers[TOKEN_KEY] = token;
    //     }
    // }

    // if (!environment.production && requestArgs.opts) {
    //     if (requestArgs.opts.header) {
    //         requestArgs.opts.header.set(FAKE_IDENTITY_KEY, environment.identitaIrideParameter);
    //     } else if (requestArgs.opts.headers) {
    //         requestArgs.opts.headers[FAKE_IDENTITY_KEY] = environment.identitaIrideParameter;
    //     } else {
    //         requestArgs.opts.header = new Headers();
    //         requestArgs.opts.header.set(FAKE_IDENTITY_KEY, environment.identitaIrideParameter);
    //         requestArgs.opts.headers = {};
    //         requestArgs.opts.headers[FAKE_IDENTITY_KEY] = environment.identitaIrideParameter;
    //     }
    // }

    // tslint:disable-next-line:no-console
    console.debug('FormIo Rest request integrated with tokens: ', requestArgs);
    return requestArgs;
  }

}
