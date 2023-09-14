/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {STORAGE_APP_PREFIX, STORAGE_KEYS} from '../common/costanti';

export class StorageManager {

  public static get(key: STORAGE_KEYS): any {
    return JSON.parse(sessionStorage.getItem(key));
  }

  public static add(key: STORAGE_KEYS, obj: any) {
    sessionStorage.setItem(key, JSON.stringify(obj));
  }

  public static remove(key: STORAGE_KEYS) {
    sessionStorage.removeItem(key);
  }

  // Elimina tutte le chiave che iniziano con il prefisso
  public static clear() {
    const keys: string[] = this.getKeys();
    for (let i = 0; i < keys.length; i++) {
      if (keys[i].startsWith(STORAGE_APP_PREFIX)) {
        sessionStorage.removeItem(keys[i]);
      }
    }
  }

  public static getKeys(): string[] {
    const keys: string[] = [];
    for (let i = 0; i < sessionStorage.length; i++) {
      const s = sessionStorage.key(i) as string;
      keys.push(s);
    }
    return keys;
  }

  // effettua decode in base 64 e escape dei caratteri
  // serve per gestire il cookie di autenticazione con caratteri particolari
  public static fromBinary(encoded): any {
    const binary = atob(encoded);
    return decodeURIComponent(escape(binary));
  }

}
