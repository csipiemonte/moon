/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit } from '@angular/core';
import { ProtocolloParametro } from 'src/app/model/dto/protocollo-parametro';
import { SharedService } from 'src/app/services/shared.service';
import { faCheck } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'tr[app-miei-moduli-dettaglio-attr-protocollo-parametri-riga]',
  templateUrl: './prt-parametri-riga.component.html',
  styleUrls: ['./prt-parametri-riga.component.scss']
})
export class PrtParametriRigaComponent implements OnInit {

  @Input() p: ProtocolloParametro;
  @Input() nomeOperatore: string;

  isAdmin = false;

  faCheck = faCheck;

  constructor(private sharedService: SharedService) {
    // cambia stato e nuova versione riservati a profilo ADMIN
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
  }

}
