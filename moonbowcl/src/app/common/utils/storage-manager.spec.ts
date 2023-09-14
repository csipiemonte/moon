/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {StorageManager} from './storage-manager';

describe('StorageManager', () => {
  it('should create an instance', () => {
    expect(new StorageManager()).toBeTruthy();
  });
});
