/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {NgEventBus} from 'ng-event-bus';
import {STATI, STORAGE_KEYS} from 'src/app/common/costanti';
import {NavSelection} from 'src/app/common/nav-selection';
import {CriterioRicercaIstanze} from 'src/app/model/dto/criterio-ricerca-istanze';
import {Nav} from 'src/app/model/dto/nav';
import {Costanti} from 'src/app/common/costanti';
import {Istanza} from '../../../model/dto/istanza';
import {MoonfoblService} from '../../../services/moonfobl.service';
import {SharedService} from '../../../services/shared.service';
import { StorageManager } from 'src/app/util/storage-manager';


@Component({
  selector: 'app-istanze',
  templateUrl: './istanze.component.html',
  styleUrls: ['./istanze.component.scss']
})
export class IstanzeComponent implements OnInit, AfterViewInit {

  @ViewChild('divRicerca') divElemetRicerca!: ElementRef;
  divRicerca: HTMLElement;

  active: number;
  righeIstanzaRichiestaIntegrazione: Istanza[] = [];
  righeIstanzaAttesaPagamento: Istanza[] = [];
  isRicercaAvanzata: boolean = false;
  formRicercaAperto = false;


  constructor(
    private router: Router,
    private sharedService: SharedService,
    private moonfoblService: MoonfoblService,
    private eventBus: NgEventBus
  ) {
    this.verificaIstanzeRichiestaIntegrazione();
    this.verificaIstanzeAttesaPagamento();
  }


  ngOnInit() {
    const state = this.router.getCurrentNavigation()?.extras.state;
    if (state && Number(state.caller) === NavSelection.NUOVA_ISTANZA) {
      const urlIstanza = this.sharedService.nav.route;
      this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'home/istanze');
      this.active = this.sharedService.activeTab;
      this.router.navigate([urlIstanza]);
    } else {
      this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'home/istanze');
      this.active = this.sharedService.activeTab;

    }
    this.eventBus.cast('active-nav-bar:enable', NavSelection.ISTANZE);
  }

  public checkOpenAccordionRicerca(check: boolean) {
    this.isRicercaAvanzata = check;
    if (check) {
      this.divRicerca?.classList.add('show');
      this.formRicercaAperto = true;
    }
  }

  onActiveIdChange($event) {
    console.log($event);
    // resetto la pagina
    this.sharedService.page = 1;
  }

  // async
  verificaIstanzeRichiestaIntegrazione() {
    this.moonfoblService.getElencoIstanzeFiltratoEOrdinato(Costanti.ISTANZA_STATO_RICHIESTA_INTEGRAZIONE, null,null, null, '-dataCreazione').subscribe((righe) => {
      this.righeIstanzaRichiestaIntegrazione = righe;
    });
  }

  verificaIstanzeAttesaPagamento() {
    const istanzeInAttesaPagamento = [STATI.IN_PAGAMENTO_ONLINE.toString(), STATI.ATTESA_RICEVUTA_PAGAMENTO.toString(),STATI.IN_PAGAMENTO.toString(), STATI.ATTESA_PAGAMENTO.toString()];
    this.moonfoblService.getElencoIstanzeFiltratoEOrdinato(null,istanzeInAttesaPagamento,null, null, '-dataCreazione').subscribe((righe) => {
      this.righeIstanzaAttesaPagamento = righe;
    });
  }


  toggleIstanze() {

    this.formRicercaAperto = !this.formRicercaAperto;
    // FZ-NAV this.isRicercaAvanzata = this.isRicercaAvanzata ? false:true;
    // this.isRicercaAvanzata = !this.isRicercaAvanzata;
    // form ricerca chiuso resetto la ricerca
    if (!this.isRicercaAvanzata) {
      this.sharedService.fromElencoRicerca = false;
      this.sharedService.page = 1;
    } else {
      this.isRicercaAvanzata = false;
    }

    if (this.isRicercaAttiva() && this.formRicercaAperto){
      this.isRicercaAvanzata = true;
    }

  }

  risultatoRicerca($event: boolean) {
    this.isRicercaAvanzata = $event;
    if (this.isRicercaAvanzata) {
      this.sharedService.fromElencoRicerca = true;
    }
  }

  ngAfterViewInit(): void {
    this.divRicerca = this.divElemetRicerca.nativeElement;
    this.checkOpenAccordionRicerca(this.sharedService.fromElencoRicerca);
  }


  isRicercaAttiva() {
    const obj: CriterioRicercaIstanze = StorageManager.get(STORAGE_KEYS.CRITERIO_RICERCA);
    return obj == null ? false : true;
  }

  isDivRicercaOpen() {
    return this.divRicerca.classList.contains('show');
  }
}
