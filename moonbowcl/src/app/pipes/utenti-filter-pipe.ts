/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Pipe, PipeTransform } from '@angular/core';
import { UtenteEnteAbilitato } from '../model/dto/utente ente-abilitato';

@Pipe({
  name: 'utentiFilter'
})
export class UtentiFilterPipe implements PipeTransform {

  transform(myobjects: Array<UtenteEnteAbilitato>, searchText: string): any {
    if (!myobjects) {
      return [];
    }
    if (!searchText) {
      return myobjects;
    }

    searchText = searchText.toLocaleLowerCase();

    return myobjects.filter(item => {
      return item.identificativoUtente.toLocaleLowerCase().includes(searchText);
    });
  }

  transformWithSuggestFormat(myobjects: Array<UtenteEnteAbilitato>, searchText: string): any {
    if (!myobjects) {
      return [];
    }
    if (!searchText) {
      return myobjects;
    }

    const idxSeparator1 = searchText.indexOf(' (');
    let searchCognomeNome = searchText.toLocaleLowerCase().substring(0, idxSeparator1);
    searchCognomeNome = (searchCognomeNome.trim().length === 0) ? undefined : searchCognomeNome.trim();
    let searchIdentificativoUtente = searchText.substring(idxSeparator1 + 2);
    searchIdentificativoUtente = searchIdentificativoUtente.substring(0, searchIdentificativoUtente.indexOf(')'));
    console.log('searchCognomeNome: ' + searchCognomeNome + ' OR searchIdentificativoUtente: ' + searchIdentificativoUtente);

    if (searchIdentificativoUtente) {
      return myobjects.filter(item => {
        return item.identificativoUtente.includes(searchIdentificativoUtente);
      });
    } else {
      return myobjects.filter(item => {
        return item.identificativoUtente.toLocaleLowerCase().includes(searchCognomeNome);
      });
    }
  }

}
