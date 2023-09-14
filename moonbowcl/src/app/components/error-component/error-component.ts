/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, isDevMode, OnInit} from '@angular/core';
import {ErrorNotificationService} from '../../services/error-notification.service';
import {MoonboError} from '../../model/common/moonbo-error';

@Component({
  selector: 'app-error-component',
  templateUrl: './error-component.html',
  styleUrls: ['./error-component.css']
})
export class ErrorComponent implements OnInit {

  public errorMessage = '';
  public codiceErrore: number;
  public messaggioTecnico: string;
  public url: string;
  public showErr = false;
    scope: any;
    showTech: boolean;
  constructor(private errorNotificationService: ErrorNotificationService) { }

  ngOnInit(): void {
    this.errorNotificationService.notification.subscribe(
         (err: MoonboError) => {
             if (err) {
                 this.errorMessage = err.errorMsg;
                 this.codiceErrore = err.codice;
                 this.messaggioTecnico = err.techMsg;
                 this.scope = err.scope;
                 this.url = err.url;
                 this.showErr = true;
             } else {
                 this.errorMessage = '' ;
                 this.codiceErrore = 0 ;
                 this.messaggioTecnico = '';
                 this.showErr = false;
             }
    }
    );
  }

}
