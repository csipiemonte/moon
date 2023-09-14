/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-processi-page',
  templateUrl: './processi-page.component.html',
  styleUrls: ['./processi-page.component.scss']
})
export class ProcessiPageComponent implements OnInit {

  constructor() {
    log('[app-processi-page::constructor]');
  }

  ngOnInit(): void {
  }

}

function log(a: any) {
  console.log(a);
}