/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Pipe, PipeTransform } from '@angular/core';
import { Modulo } from 'src/app/model/dto/modulo';

@Pipe({
  name: 'moduliFilterBy'
})
export class ModuliFilterByPipe implements PipeTransform {

  transform(moduli: Modulo[], ...args: any[]): Modulo[] {

    const moduliFiltered: Modulo[] = [];
    // args[0] primo filtro per categoria
    if ( args[0] != null ) {
      moduli.forEach( function(m) {
        if (m.categoria !== null) {
          console.log(m.categoria.idCategoria);
          console.log('Filtro: ' + args[0].idCategoria);
          if (m.categoria.idCategoria === args[0].idCategoria) {
            console.log('checked');
            moduliFiltered.push(m);
          }
        }
      });
    }
    console.log('Filtrati ' + moduliFiltered.length);
    return moduliFiltered;
  }

}
