/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {faArrowLeft, faHome} from '@fortawesome/free-solid-svg-icons';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {NavSelection} from 'src/app/common/nav-selection';
import {Nav} from 'src/app/model/dto/nav';
import {Modulo} from '../../../model/dto/modulo';
import {MoonfoblService} from '../../../services/moonfobl.service';
import {SharedService} from '../../../services/shared.service';
import { ContoTerziFormComponent } from '../conto-terzi-form/conto-terzi-form.component';

@Component({
  selector: 'app-nuova-istanza',
  templateUrl: './nuova-istanza.component.html',
  styleUrls: ['./nuova-istanza.component.scss']
})
export class NuovaIstanzaComponent implements OnInit {
  moduli: Modulo[];
  msg: string;
  categoriaModuli: string = null;
  modal: NgbModalRef;
  faHome = faHome;
  faArrowLeft = faArrowLeft;
  backCategorie = 'true';

  constructor(
    private route: ActivatedRoute,
    private moonservice: MoonfoblService,
    private router: Router,
    private sharedService: SharedService,
    private modalService: NgbModal) {
  }

  ngOnInit(): void {
    if(this.route.snapshot.paramMap.get('backCategorie') != null){
      this.backCategorie = this.route.snapshot.paramMap.get('backCategorie');
    }
    window. scroll(0, 0);
    this.msg = null;
    if (this.sharedService.elencoModuliNuovaIstanza) {
      this.moduli = this.sharedService.elencoModuliNuovaIstanza;
      this.sharedService.elencoModuliNuovaIstanza = undefined;
      this.categoriaModuli = this.moduli[0].categoria.descrizione;
    } else {
      this.getElencoModuli();
    }
    this.sharedService.nav = new Nav(NavSelection.NUOVA_ISTANZA, 'home/categorie');
  }

  private getElencoModuli(): void {

    this.moonservice.getElencoModuli().subscribe((moduli) => {
        this.moduli = moduli;
        if (!this.moduli || this.moduli.length === 0) {
          this.msg = 'Al momento non sono disponibili moduli per presentare istanze!';
        } else {
          this.categoriaModuli = this.moduli[0].categoria.descrizione;
        }
      }
    );
  }


  isModuloCompilabileContoTerzi(m: Modulo): boolean {
    if (m) {
      const v = this.moonservice.getModuloAttributo(m, 'CONTO_TERZI');
      if (v) {
        return true;
      } else {
        return false;
      }
    }
  }

  initModuloContoTerzi(m: Modulo, isConcessionario: boolean) {
    if (m) {
      let f = this.moonservice.getModuloAttributo(m, 'CONTO_TERZI');
      if (isConcessionario) {
        f += '-IN_PROPRIO';
      }
      // console.log(f);
      // tipo di modulo da mostrare
      const modalRef = this.modalService.open(ContoTerziFormComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.form = f;
      modalRef.result.then(
        (result) => {
          console.log(result);
          //  let cf = result.value.cf;
          const cf_pIva = result.value.cf_pIva;
          this.sharedService.datiContoTerzi.ct = true;
          this.sharedService.datiContoTerzi.dati = {cf_pIva, isConcessionario};
          this.router.navigate(['/manage-form/NEW/' + m.idModulo + '/' + m.idVersioneModulo]);
        },
        (reason) => {
          // console.log(reason);
        }
      );
      // console.log(f);
    }
  }

  checkPartitaIVA(pIva) {
    return (/^[0-9]{11}$/.test(pIva));
  }

  checkCF(cf) {
    return (/^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$/.test(cf));
  }

  back() {
    this.router.navigate(['home']);
  }
}
