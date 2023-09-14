/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Pipe, PipeTransform} from '@angular/core';
import {Modulo} from '../model/dto/modulo';
import {Categoria} from '../model/dto/categoria';

@Pipe({
  name: 'filterModules'
})
export class FilterModulesPipe implements PipeTransform {

  transform(moduli: Modulo[], ...args: any[]): Modulo[] {

    const moduliFiltered: Modulo[] = [];
    // args[0] primo filtro per categoria
    if ( args[0] != null ) {
      moduli.forEach( function(m) {
        if (m.categoria !== null) {
          if (m.categoria.idCategoria === args[0].idCategoria) {
            moduliFiltered.push(m);
          }
        }
      });
    }
    return moduliFiltered;
  }

}
