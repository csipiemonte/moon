/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Pipe, PipeTransform} from '@angular/core';
import {Categoria} from '../model/dto/categoria';

@Pipe({
  name: 'sortCategorie'
})
export class SortCategoriePipe implements PipeTransform {
  // ordinamento categorie moduli
  transform(categorie: Categoria[]): Categoria[] {
    if(categorie !== undefined && categorie !== null && categorie.length > 1) {
      categorie.sort((a, b) => (a.descrizione > b.descrizione) ? 1 :
        // se la descrizione è uguale allora confronto anche l'id, altrimenti confronto solo la descrizione
        (a.descrizione === b.descrizione) ? ((a.idCategoria > b.idCategoria) ? 1 : -1) : -1 );
    }
    return categorie;
  }

}
