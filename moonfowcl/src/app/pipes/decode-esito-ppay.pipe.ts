/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/*
  Pipe per decodificare esito del pagamento da Piemonte Pay
  000 (SUCCESSO), 100 (FALLITO o ANNULLATO
 */

import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'decodeEsitoPpay'
})
export class DecodeEsitoPpayPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    switch (value) {
      case '000':
        return 'SUCCESSO';
        break;
      case '100':
        return 'FALLITO o ANNULLATO';
        break;
      default:
        return 'NON DETERMINATO';
        break;
    }
  }

}
