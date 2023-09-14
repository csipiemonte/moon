/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class Nav {
  public active: number;
  public route: string;

  constructor(
    public a: number,
    public r: string
  ) {
    this.active = a;
    this.route = r;
  }
}
