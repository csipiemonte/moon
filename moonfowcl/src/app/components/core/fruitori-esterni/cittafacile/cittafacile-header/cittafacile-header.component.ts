/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {UserInfo} from 'src/app/model/common/user-info';
import {ConsumerParams} from '../../../../../model/common/consumer-params';

@Component({
  selector: 'cittafacile-header',
  templateUrl: './cittafacile-header.component.html',
  styleUrls: ['./cittafacile-header.component.scss']
})
export class CittafacileHeaderComponent implements OnInit {

  @Input() currentUser: UserInfo;
  @Input() consumerParams: ConsumerParams;

  topbarBgColor : string;
  headerBgColor : string;

  constructor() { }

  ngOnInit(): void {

    this.topbarBgColor = this.consumerParams.parameters?.topbar_bg_color['text'];
    this.headerBgColor = this.consumerParams.parameters?.header_bg_color['text'];

  }

}
