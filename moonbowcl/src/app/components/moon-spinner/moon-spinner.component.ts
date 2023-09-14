/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-moon-spinner',
  templateUrl: './moon-spinner.component.html',
  styleUrls: ['./moon-spinner.component.scss']
})
export class MoonSpinnerComponent implements OnInit {
  @Input('messaggio') messaggio = 'Attendere prego...';
  msg: string;
  constructor() { }

  ngOnInit() {
  }

}
