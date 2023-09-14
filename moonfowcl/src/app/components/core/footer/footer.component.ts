/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {NgEventBus} from 'ng-event-bus';
import {MetaData} from 'ng-event-bus/lib/meta-data';
import {Subscription} from 'rxjs-compat';
import {CONSUMER_TEMPLATE} from 'src/app/model/common/consumer-constants';
import {ConsumerParams} from '../../../model/common/consumer-params';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  @Input() version: string;
  @Input() year: number;

  consumerParams: ConsumerParams;
  template: string = '';
  urlCssFooter: string = '';
  htmlFooter: string = '';

  subscribers: Subscription = new Subscription();

  constructor(
    private router: Router,
    private eventBus: NgEventBus) {
  }

  ngOnInit() {
    this.setSubscribers();
  }

  setSubscribers() {
    if (this.template === '') this.template = CONSUMER_TEMPLATE.MOON;
    this.subscribers.add(
      this.eventBus.on('microfe:set').subscribe((meta: MetaData) => {
        console.log(meta.data);
        if (meta.data.consumerParams) {
          console.log(' *** FOOTER.component::setSubscribers: consumer ' + JSON.stringify(meta.data.consumerParams) + ' ***');
          this.template = meta.data.consumerParams.consumer;
          this.consumerParams = meta.data.consumerParams;
        }
      })
    );
  }

  goToAccessibilita() {
    this.eventBus.cast('active-nav-bar:disable', true);
    this.router.navigate(["accessibilita"]);
  }

  goToCookie() {
    this.eventBus.cast('active-nav-bar:disable', true);
    this.router.navigate(["cookie"]);
  }

  goToPrivacy() {
    this.eventBus.cast('active-nav-bar:disable', true);
    this.router.navigate(["privacy"]);
  }

  ngOnDestroy() {
    this.subscribers.unsubscribe();
  }
}
