/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Pipe, PipeTransform } from '@angular/core';
import {Modulo} from '../../../model/dto/modulo';

@Pipe({
  name: 'moduliFilter'
})
export class ModuliFilterPipe implements PipeTransform {

  transform(myobjects: Array<Modulo>, searchText: string): any {
    if (!myobjects) {
      return [];
    }
    if (!searchText) {
      return myobjects;
    }

    searchText = searchText.toLocaleLowerCase();

    return myobjects.filter(it => {
      return it.oggettoModulo.toLocaleLowerCase().includes(searchText);
    });

  }

  transformWithCodice(myobjects: Array<Modulo>, searchText: string): any {
    if (!myobjects) {
      return [];
    }
    if (!searchText) {
      return myobjects;
    }
    console.log('searchText: ' + searchText);
    const idxSeparator1 = searchText.lastIndexOf(' (');
    let searchOggetto = searchText.toLocaleLowerCase().substring(0, idxSeparator1);
    searchOggetto = (searchOggetto.trim().length === 0) ? undefined : searchOggetto.trim();
    let searchCodice = searchText.substring(idxSeparator1 + 2);
    searchCodice = searchCodice.substring(0, searchCodice.indexOf(')'));
    const idxSeparatorId = searchText.lastIndexOf(' [');
    let searchId = searchText.substring(idxSeparatorId + 2);
    searchId = searchId.substring(0, searchId.indexOf(']'));
    console.log('searchOggetto: ' + searchOggetto + ' OR searchCodice: ' + searchCodice + ' OR searchId: ' + searchId);

    if (searchId) {
      return myobjects.filter(it => {
        return searchId === it.idModulo.toString();
      });
    } else {
      if (searchCodice) {
        return myobjects.filter(it => {
          return it.codiceModulo.includes(searchCodice);
        });
      } else {
        return myobjects.filter(it => {
          return it.oggettoModulo.toLocaleLowerCase().includes(searchOggetto);
        });
      }
    }
  }

}
