/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class StorageManager {

  public static get(key: string): any {
    return JSON.parse(localStorage.getItem(key));
  }

  public static add(key: string, obj: any) {
    localStorage.setItem(key, JSON.stringify(obj));
  }

  public static remove(key: string) {
    localStorage.removeItem(key);
  }

  public static clear() {
    const keys: string[] = this.getKeys();
    for (let i = 0; i < keys.length; i++) {
      if(keys[i].startsWith("bo-")){
        this.remove(keys[i]);
      }
    }
  }

  public static getKeys(): string[] {
    const keys: string[] = [];
    for (let i = 0; i < localStorage.length; i++) {
      const s = localStorage.key(i) as string;
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
