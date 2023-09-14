/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {NgxSpinnerService} from 'ngx-spinner';
import {Router} from '@angular/router';

import {StorageManager} from '../../../common/utils/storage-manager';
import {AutenticazioneService} from '../../../services/notifiers/autenticazione.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LoginResponseIF} from '../../../model/common/login-response';
import { ModalAlertComponent } from '../../modal/modal-alert/modal-alert.component';
import { SecurityService } from 'src/app/security.service';
import { SharedService } from 'src/app/services/shared.service';
import { UserInfo } from 'src/app/model/common/user-info';
import {STORAGE_KEYS} from '../../../common/costanti';

@Component({
  selector: 'app-user-password',
  templateUrl: './user-password.component.html',
  styleUrls: ['./user-password.component.css']
})
export class UserPasswordComponent implements OnInit {

  constructor(
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private securityService: SecurityService,
    private modalService: NgbModal,
    private _autenticazioneService: AutenticazioneService,
    private sharedService: SharedService
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
    await this.spinnerService.show();
    try {
      console.log('user-password.component::login: securityService.loginUserPassword');
      const loginResponse = await this.securityService.loginUserPassword(f.value.user, f.value.password).toPromise() as LoginResponseIF;
      console.log(loginResponse);
      loginResponse.authMode = 'USER-PWD';
     
      //const queryString = window.location.search;
      const user = UserInfo.createUserInfo(loginResponse);
      
      await this.spinnerService.hide();
      
      this.sharedService.UserLogged = user;
      StorageManager.add(STORAGE_KEYS.MOON_JWT_TOKEN, loginResponse.idMoonToken);
      this._autenticazioneService.sendDataUserAuhenticated(user);
      
      //this.loginService.login(user, queryString);
    } catch (e) {
      await this.spinnerService.hide();
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
