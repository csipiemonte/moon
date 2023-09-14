/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {SecurityService} from '../../../services/security.service';
import {StorageManager} from '../../../util/storage-manager';
import {SharedService} from '../../../services/shared.service';
import {MoonfoblService} from 'src/app/services/moonfobl.service';
import {User} from 'src/app/model/common/user';
import {LoginService} from 'src/app/services/login.service';
import {CookieService} from 'ngx-cookie-service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ModalAlertComponent} from '../../modal-alert/modal-alert.component';
import {UserIF} from '../../../model/common/user-if';
import { NgEventBus } from 'ng-event-bus';


@Component({
  selector: 'app-user-password',
  templateUrl: './user-password.component.html',
  styleUrls: ['./user-password.component.scss']
})
export class UserPasswordComponent implements OnInit {

  constructor(
    private router: Router,
    private securityService: SecurityService,
    private sharedService: SharedService,
    private acRoute: ActivatedRoute,
    private moonService: MoonfoblService,
    private loginService: LoginService,
    private cookieService: CookieService,
    private modalService: NgbModal,
    private eventBus: NgEventBus
  ) {
  }

  ngOnInit() {
    StorageManager.clear();
    console.log('user-password.component::ngOnInit: StorageManager cleared');
  }

  async login(f: NgForm) {
    if (!f.valid) {
      return false;
    }

    try {
      console.log('user-password.component::login: securityService.loginUserPassword');
      const loginResponse = await this.securityService.loginUserPassword(f.value.user, f.value.password).toPromise() as UserIF;
      console.log(loginResponse);
      loginResponse.authMode = 'USER-PWD';
      this.eventBus.cast('authenticazione:set-current-user', loginResponse);

      const queryString = window.location.search;
      const user = new User(loginResponse);

      this.loginService.login(user, queryString);
    } catch (e) {

      switch (e.status) {
        case 401:
          const mdRef = this.modalService.open(ModalAlertComponent);
          mdRef.componentInstance.modal_titolo = 'Errore';
          mdRef.componentInstance.modal_contenuto = 'Utente non autorizzato. User/password non corretti!';
          mdRef.result.then((result) => {
            console.log('Closed with: ${result}' + result);
            this.router.navigate(['/auth/user-pwd/']);

          }, (reason) => {
            console.log(reason);
          });
          break;
        case 404:
          alert('Utente non trovato');
          break;
        case 403:
          alert('Login errato. Controllare i dati inseriti e riprovare');
          break;
        default:
          const mdRef2 = this.modalService.open(ModalAlertComponent);
          mdRef2.componentInstance.modal_titolo = e.error.title;
          mdRef2.componentInstance.modal_contenuto = 'Errore nel servizio di autenticazione!';

          mdRef2.result.then((result) => {
            console.log('Closed with: ${result}' + result);
            this.router.navigate(['/auth/user-pwd/']);

          }, (reason) => {
            console.log(reason);
          });
      }
    }
  }
}
