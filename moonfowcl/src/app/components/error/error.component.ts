/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {SecurityService} from '../../services/security.service';
import {ConfigService} from '../../services/config.service';
import {environment} from 'src/environments/environment';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {
  intervalId = 0;
  seconds = 10;
  message: string;

  constructor(private router: Router,
              private securityService: SecurityService,
              private configService: ConfigService) { }

  ngOnInit() {
    if (environment.production) {
      this.launchTimeout();
    }

    const param = this.router.parseUrl(this.router.url);
    console.log('LOG init: ' + param);
    this.message = param.queryParams.message;
  }

  private launchTimeout() {

    this.intervalId = window.setInterval(() => {
      this.seconds -= 1;
      if (this.seconds === 0) {
        clearInterval(this.intervalId);
        // effettuo logout per sicurezza
        this.securityService.localLogout();
        window.location.href = this.configService.getOnAppExitURL();

        // capire se mandare in logout
        // if (!this.configService.isDisabledErrorKickOut())
        //   this.userService.logOut();
      }
    }, this.seconds);

  }
}
