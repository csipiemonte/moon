/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {Modulo} from '../../../model/dto/modulo';
import {MoonfoblService} from '../../../services/moonfobl.service';
import {SharedService} from '../../../services/shared.service';
import {Categoria} from '../../../model/dto/categoria';
import {Nav} from 'src/app/model/dto/nav';
import {NavSelection} from 'src/app/common/nav-selection';
import {Router} from '@angular/router';

@Component({
  selector: 'app-categorie-moduli',
  templateUrl: './categorie.component.html',
  styleUrls: ['./categorie.component.scss']
})
export class CategorieComponent implements OnInit {
  moduli: Modulo[];
  categorie: Categoria[] = [];

  msg: string;

  constructor(private moonservice: MoonfoblService,
              private router: Router,
              private sharedService: SharedService) { }

  ngOnInit() {
    this.msg = null;
    this.getElencoModuli();
    this.sharedService.nav = new Nav(NavSelection.NUOVA_ISTANZA, 'home/categorie');
  }

  private getElencoModuli(): void {
    this.moonservice.getElencoModuli().subscribe( (moduli) => {
        this.moduli = moduli;
        if (this.moduli.length === 0) {
          this.msg = 'Al momento non sono disponibili moduli per presentare istanze!';
        } else {
          // valorizzo le categorie
          const mapCategorie: Map<number, Categoria> = new Map<number, Categoria>();
          moduli.forEach(function (m) {
            if (m.categoria !== null) {
              mapCategorie.set(m.categoria.idCategoria, m.categoria);
            }
          });
          this.categorie = Array.from(mapCategorie.values());
          // route se ho una sola categoria visualizzo elenco-moduli direttamente
          if (this.categorie.length === 1) {
            this.sharedService.elencoModuliNuovaIstanza = this.moduli;
            this.router.navigate(['/home/elenco-moduli' , {backCategorie:false}]);
          }
        }
      }
    );
  }


}
