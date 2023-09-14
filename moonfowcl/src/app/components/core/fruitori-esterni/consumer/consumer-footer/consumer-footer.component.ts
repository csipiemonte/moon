/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Inject, Input, OnInit} from '@angular/core';
import {DOCUMENT} from '@angular/common';

@Component({
  selector: 'consumer-footer',
  templateUrl: './consumer-footer.component.html',
  styleUrls: ['./consumer-footer.component.scss']
})
export class ConsumerFooterComponent implements OnInit {

  @Input('urlCssFooter') urlCssFooter: string;
  @Input('htmlFooter') htmlFooter: string;

  constructor(@Inject(DOCUMENT) private document: Document) {}

  ngOnInit(): void {
    this.loadStyle(this.urlCssFooter);
  }


  loadStyle(styleName: string) {
    const head = this.document.getElementsByTagName('head')[0];

    let themeLink = this.document.getElementById(
      'client-theme'
    ) as HTMLLinkElement;
    if (themeLink) {
      themeLink.href = styleName;
    } else {
      const style = this.document.createElement('link');
      style.id = 'client-theme';
      style.rel = 'stylesheet';
      style.type = 'text/css';
      style.href = `${styleName}`;

      head.appendChild(style);
    }
  }

}
