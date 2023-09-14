/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { FakeIdentityInterceptor } from './fake-identity-interceptor';

describe('FakeIdentityInterceptor', () => {
  it('should create an instance', () => {
    expect(new FakeIdentityInterceptor()).toBeTruthy();
  });
});
