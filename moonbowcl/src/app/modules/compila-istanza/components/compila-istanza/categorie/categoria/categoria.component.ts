/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Categoria } from 'src/app/model/dto/categoria';
import { Modulo } from 'src/app/model/dto/modulo';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-categoria',
  templateUrl: './categoria.component.html',
  styleUrls: ['./categoria.component.scss']
})
export class CategoriaComponent implements OnInit {
  @Input() categoria: Categoria;
  @Input() moduli: Modulo[];
   constructor(
     private router: Router,
     private sharedService: SharedService,
     private spinnerService: NgxSpinnerService
   ) {
 
   }
 
   ngOnInit() {
 
   }
 
   viewModuliList() {
     this.sharedService.elencoModuliNuovaIstanza = this.moduli;
     this.router.navigate(['nuova-istanza']);
   }
 
}
