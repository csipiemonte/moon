/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MoonSpinnerComponent } from './moon-spinner/moon-spinner.component';
import { NgxSpinnerModule } from 'ngx-spinner';



@NgModule({
  declarations: [MoonSpinnerComponent],
  imports: [
    CommonModule,
    NgxSpinnerModule
  ],
  exports: [MoonSpinnerComponent],
})
export class MoonSpinnerModule { }
