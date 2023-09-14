/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { Modulo } from '../../../../model/dto/modulo';
import { environment } from '../../../../../environments/environment';
import { SharedService } from '../../../../services/shared.service';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { UserInfo } from 'src/app/model/common/user-info';
import { ModuliFilterPipe } from 'src/app/modules/custom-pipes/pipes/moduli-filter-pipe';

@Component({
  selector: 'app-miei-moduli-tabella',
  templateUrl: './miei-moduli-tabella.component.html',
  styleUrls: ['./miei-moduli-tabella.component.css']
})
export class MieiModuliTabellaComponent implements OnInit {

  @Input() mieiModuli: Modulo[];
  // eventi rilanciati
  @Output() eventModificaModulo = new EventEmitter();
  @Output() eventCambiaStato = new EventEmitter<Modulo>();
  @Output() eventDettaglioModulo = new EventEmitter<Modulo>();
  @Output() eventDatiGenerali = new EventEmitter<Modulo>();
  @Output() eventEliminaModulo = new EventEmitter<Modulo>();
  @Output() eventNuovaVersione = new EventEmitter<Modulo>();
  @Output() eventCreaModulo = new EventEmitter();
  @ViewChild('auto') auto;

  pageSizeMieiModuli = environment.pageSize;
  currPageMieiModuli: number;
  elencofiltroModuli: string[] = [];
  filtroElencoModuli: string;
  elencoModuliFiltrato: Modulo[];

  currentUser: UserInfo = null;
  isAdmin = false;
  faPlus = faPlus;

  constructor(private sharedService: SharedService,
              private filterModuliPipe: ModuliFilterPipe) {
    this.currentUser = this.sharedService.UserLogged;
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    console.log('miei-moduli-tabella::ngOnInit()...');
    if (this.sharedService.pageMieiModuli) {
      this.currPageMieiModuli = this.sharedService.pageMieiModuli;
    } else {
      this.currPageMieiModuli = 1;
    }


    if (this.mieiModuli) {
      this.mieiModuli.forEach(el => {
        if (el.codiceModulo != null) {
          this.elencofiltroModuli.push(`${el.oggettoModulo} (${el.codiceModulo})` +
            (this.isAdmin ? ` [${el.idModulo}]` : ``));
        }
      });
      this.elencofiltroModuli.sort((a, b) =>
          a.toLocaleLowerCase().trim().localeCompare(b.toLocaleLowerCase().trim()));
      if (this.sharedService.filtroModuli) {
        // se arrivo da elenco filtrato resetto la pagina a 1
        this.currPageMieiModuli = 1;
        this.filtroElencoModuli = this.sharedService.filtroModuli;
        this.elencoModuliFiltrato = this.filterModuliPipe.transformWithCodice(this.mieiModuli, this.filtroElencoModuli);
      } else {
        this.elencoModuliFiltrato = this.mieiModuli;
      }
    } else {
      this.mieiModuli = [];
    }
  }

  selectEvent($event: any) {
    this.filtroElencoModuli = $event;
    this.currPageMieiModuli = 1;
    this.elencoModuliFiltrato = this.filterModuliPipe.transformWithCodice(this.mieiModuli, this.filtroElencoModuli);
    this.sharedService.filtroModuli = this.filtroElencoModuli;
  }

  clearFilter() {
    this.filtroElencoModuli = '';
    this.sharedService.filtroModuli = '';
    this.elencoModuliFiltrato = this.mieiModuli;
    this.currPageMieiModuli = 1;
  }

  pageChanged(currPage: number) {
    this.sharedService.pageMieiModuli = currPage;
  }

  // Eventi Gestione modulo
  modificaRiga(modulo: Modulo) {
    this.eventModificaModulo.emit(modulo);
  }

  cambiaStato($event: Modulo) {
    this.eventCambiaStato.emit($event);
  }

  dettaglioModulo(modulo: Modulo) {
    this.eventDettaglioModulo.emit(modulo);
  }

  datiGenerali(modulo: Modulo) {
    this.eventDatiGenerali.emit(modulo);
  }

  eliminaModulo(modulo: Modulo) {
    const res = this.eventEliminaModulo.emit(modulo);
    console.log('miei-moduli-tabella::eliminaModulo() res: ' + res);
    console.log('miei-moduli-tabella::eliminaModulo() this.filtroElencoModuli: ' + this.filtroElencoModuli);
    // TODO: dovrebbe scatenare syncrono e solo dopo eliminazione ok !
    if (this.elencoModuliFiltrato && this.elencoModuliFiltrato.length === 1) {
      this.clearFilter(); // non funziona bene
    }
    //tolgo il modulo cancellato dall elenco
    //this.elencofiltroModuli = this.elencofiltroModuli.filter(x => x.split(" ")[1].indexOf(modulo.codiceModulo) ==-1 );

  }

  nuovaVersione(modulo: Modulo) {
    this.eventNuovaVersione.emit(modulo);
  }

  creaModulo() {
    this.eventCreaModulo.emit();
  }
}
