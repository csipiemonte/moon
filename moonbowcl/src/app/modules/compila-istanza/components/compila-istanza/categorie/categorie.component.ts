/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { Categoria } from 'src/app/model/dto/categoria';
import { Modulo } from 'src/app/model/dto/modulo';
import { HandleExceptionService } from 'src/app/services/handle-exception.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { AlertService } from 'src/app/modules/alert';

@Component({
  selector: 'app-categorie',
  templateUrl: './categorie.component.html',
  styleUrls: ['./categorie.component.scss']
})
export class CategorieComponent implements OnInit {

  moduli: Modulo[];
  categorie: Categoria[] = [];
  msg: string;

  constructor(
    private moonservice: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private handleException: HandleExceptionService,
    private sharedService: SharedService,
    private alertService: AlertService) {
  }

  ngOnInit(): void {

    this.msg = null;
    this.spinnerService.show();
    //setTimeout(() => {
      this.getElencoModuli();      
    //}, 4000);
    
  }

  alertOptions = {
    id: 'alert-compila-istanza-categorie',
    autoClose: true,
    keepAfterRouteChange: false
  };

  getCategorie(moduli: Modulo[]) {
    const mapCategorie: Map<number, Categoria> = new Map<number, Categoria>();
    moduli.forEach(function (m) {
      if (m.categoria !== null) {
        mapCategorie.set(m.categoria.idCategoria, m.categoria);
      }
    });
    this.categorie = Array.from(mapCategorie.values());
  }
 
  getElencoModuli() {      
      this.moonservice.getElencoModuliPubblicati().then(moduli => {
        this.moduli = moduli;
        if (this.moduli.length === 0) {
          this.msg = 'Al momento non sono disponibili moduli per presentare istanze!';
        }
        else {
          this.getCategorie(this.moduli);
        }
        this.spinnerService.hide();
      }
      ).catch(err => {
        console.log(err);
        this.spinnerService.hide();
        //this.msg = 'Al momento non sono disponibili moduli per presentare istanze!';
        this.alertService.warn('Al momento non sono disponibili moduli per presentare istanze!', this.alertOptions);
      }
      );
    }

}
