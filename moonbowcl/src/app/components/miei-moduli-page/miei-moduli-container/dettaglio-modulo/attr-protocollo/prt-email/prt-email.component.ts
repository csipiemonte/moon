/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit } from '@angular/core';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-protocollo-email',
  templateUrl: './prt-email.component.html',
  styleUrls: ['./prt-email.component.scss']
})
export class PrtEmailComponent implements OnInit {

  @Input() maPrtInEmail: ModuloAttributo;
  @Input() attrPrtInEmailConf: object;
  @Input() titleUpd: Map<string, string>;

  constructor() { }

  ngOnInit(): void {
  }

}
