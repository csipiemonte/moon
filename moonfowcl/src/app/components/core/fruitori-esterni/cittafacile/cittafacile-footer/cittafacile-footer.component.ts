/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'cittafacile-footer',
  templateUrl: './cittafacile-footer.component.html',
  styleUrls: ['./cittafacile-footer.component.scss']
})
export class CittafacileFooterComponent implements OnInit {


  @Input() consumerParams: any;
  footerBgColor: string;

  constructor() {
  }

  ngOnInit(): void {
    this.footerBgColor = this.consumerParams.parameters?.footer_bg_color['text'];
  }

}
