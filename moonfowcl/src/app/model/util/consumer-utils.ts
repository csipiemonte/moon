/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {CONSUMER_TEMPLATE} from '../common/consumer-constants';
import {ConsumerParams} from '../common/consumer-params';
import {EMB_SERVICE_NAME} from '../common/embedded-constants';

export class ConsumerUtils {

  private consumer: ConsumerParams;
  private INSTANCE_SERVICES = [EMB_SERVICE_NAME.VIEW_ISTANZA, EMB_SERVICE_NAME.EDIT_ISTANZA, EMB_SERVICE_NAME.ISTANZA];
  private MODULE_SERVICES = [EMB_SERVICE_NAME.NEW_ISTANZA];

  constructor(consumer) {
    this.consumer = consumer;
  }

  isValidParameterSet() {
    let isValidParameterSet = false;
    if (this.consumer) {
      const service = this.consumer.service.toUpperCase();
      if (this.INSTANCE_SERVICES.includes(service) && ((this.consumer.codiceIstanza && this.consumer.codiceIstanza.length > 1) && !(this.consumer.codiceModulo && this.consumer.codiceModulo.length > 1))) {
        isValidParameterSet = true;
      }
      if (this.MODULE_SERVICES.includes(service) && (this.consumer.codiceModulo && this.consumer.codiceModulo.length > 1) ) {
        isValidParameterSet = true;
      }
    }
    return isValidParameterSet;
  }

  isValidConsumer() {
    let isValidConsumer = false;
    switch (this.consumer.consumer) {
      case CONSUMER_TEMPLATE.MOON:
        isValidConsumer = true;
        break;
      case CONSUMER_TEMPLATE.CITTA_FACILE:
        isValidConsumer = true;
        break;
      case 'generic':
        isValidConsumer = true;
        break;
      default:
        isValidConsumer = false;
    }
    return isValidConsumer;
  }

  // isValidParameterSet() {
  //   return true;
  // }

  // isValidConsumer() {
  //   return true;
  // }

}
