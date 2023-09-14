/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {Categoria} from '../../../../model/dto/categoria';
import {Modulo} from '../../../../model/dto/modulo';
import {SharedService} from '../../../../services/shared.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-categoria',
  templateUrl: './categoria.component.html',
  styleUrls: ['./categoria.component.scss']
})
export class CategoriaComponent implements OnInit {

  @Input() categoria: Categoria;
  @Input() moduli: Modulo[];

  isCollapsed = true;

  constructor(
    private router: Router,
    private sharedService: SharedService
  ) {

  }

  ngOnInit() {

  }


  viewModuliList() {
    this.sharedService.elencoModuliNuovaIstanza = this.moduli;
    this.router.navigate(['/home/elenco-moduli']);
  }

  collapsed() {
    this.isCollapsed = !this.isCollapsed;
  }
}
