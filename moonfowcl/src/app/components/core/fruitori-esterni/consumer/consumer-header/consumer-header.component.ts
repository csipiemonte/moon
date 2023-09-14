/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'consumer-header',
  templateUrl: './consumer-header.component.html',
  styleUrls: ['./consumer-header.component.scss']
})
export class ConsumerHeaderComponent implements OnInit {

  @Input('urlCssHeader') urlCssHeader: string;
  @Input('htmlHeader') htmlHeader: string;

  constructor() { }

  ngOnInit(): void {
  }

}
