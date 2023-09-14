/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {NgEventBus} from 'ng-event-bus';
import {NavSelection} from 'src/app/common/nav-selection';
import {HEADERS_MOON_IDENTITA_JWT, STORAGE_KEYS} from 'src/app/common/costanti';
import {Ente} from 'src/app/model/dto/ente';
import {Nav} from 'src/app/model/dto/nav';
import {MoonfoblService} from 'src/app/services/moonfobl.service';
import {SharedService} from 'src/app/services/shared.service';
import { StorageManager } from '../../../util/storage-manager';

@Component({
  selector: 'app-scegli-ente',
  templateUrl: './scegli-ente.component.html',
  styleUrls: ['./scegli-ente.component.scss']
})
export class ScegliEnteComponent implements OnInit {

  idEnte: number = null;
  enteSelezionato: Ente;
  elencoEnti: Ente[];
  msg: string;

  constructor(private moonservice: MoonfoblService,
              private sharedService: SharedService,
              private router: Router,
              private eventBus: NgEventBus) {
  }

  ngOnInit() {

    if (this.sharedService.elencoEnti && this.sharedService.elencoEnti.length > 0) {
      this.elencoEnti = this.sharedService.elencoEnti;
    } else {
      this.moonservice.getElencoEnti().subscribe((enti) => {
        this.elencoEnti = enti;
        if (this.elencoEnti.length === 0) {
          this.msg = 'Non sono presenti enti ';
        } else {
          console.log('Enti: ' + this.elencoEnti);
        }
      });
    }
  }


  selezionaEnte(id) {
    let ente: Ente = new Ente();
    ente.idEnte = id;
    this.enteSelezionato = ente;
  }

  annullaSelezione() {
    this.elencoEnti = null;
    this.ngOnInit();
  }

  aggiornaEnte() {

    setTimeout(() => {
    }, 1000);

    this.moonservice.aggiornaEnte(this.enteSelezionato).subscribe(response => {
      console.log(response);
      console.log('set observable for header update - response of patch imposta ente');

      StorageManager.add(STORAGE_KEYS.JWT_MOON, response.headers.get(HEADERS_MOON_IDENTITA_JWT));
      this.eventBus.cast('multi-ente:set', response.body);
      this.sharedService.nav = new Nav(NavSelection.BACHECA, 'home/bacheca');

      this.router.navigate(['home']);
    });

  }

}
