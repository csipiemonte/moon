/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModuliFilterPipe } from './pipes/moduli-filter-pipe';
import { ModuliFilterByPipe } from './pipes/moduli-filter-by.pipe';
import { CategorieSortPipe } from './pipes/categorie-sort.pipe';

@NgModule({
  declarations: [
    ModuliFilterPipe,
    ModuliFilterByPipe,
    CategorieSortPipe
  ],
  imports: [
    CommonModule
  ],
  exports: [
    ModuliFilterPipe,
    ModuliFilterByPipe,
    CategorieSortPipe
  ]
})
export class CustomPipesModule { }
