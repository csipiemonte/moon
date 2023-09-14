/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {NgEventBus} from 'ng-event-bus';
import {HEADERS_MOON_IDENTITA_JWT, STORAGE_KEYS} from 'src/app/common/costanti';
import {NavSelection} from '../common/nav-selection';
import {ConsumerParams} from '../model/common/consumer-params';
import {EMB_SERVICE_NAME} from '../model/common/embedded-constants';
import {EmbeddedNavigator} from '../model/common/embedded-navigator';
import {User} from '../model/common/user';
import { Ente } from '../model/dto/ente';
import { FiltroModuli } from '../model/dto/filtro-moduli';
import { Modulo } from '../model/dto/modulo';
import { Nav } from '../model/dto/nav';
import { Notifica } from '../model/dto/notifica';
import { StorageManager } from '../util/storage-manager';
import { MoonfoblService } from './moonfobl.service';
import { ObserveService } from './observe.service';
import { SecurityService } from './security.service';
import { SharedService } from './shared.service';

@Injectable({
  providedIn: 'root'
})

export class LoginService {

  constructor(
    private router: Router,
    private securityService: SecurityService,
    private observeService: ObserveService,
    private sharedService: SharedService,
    private moonService: MoonfoblService,
    private eventBus: NgEventBus) {
  }

  login(userInfo: User, queryString: string) {

    const urlParams = new URLSearchParams(queryString);
    const codiceModulo = urlParams.get('codice_modulo');
    const codiceEnte = urlParams.get('codice_ente');

    if (userInfo.consumerParams) {
      console.log("*** LOGIN CONSUMER ***");
      this.loginConsumer(userInfo);
    } else if (userInfo.embeddedNavigator) {
      this.loginEmbedded(userInfo);
    } else {
      if (userInfo.multiEntePortale && !userInfo.ente) {
        if (codiceEnte) {
          this.loginAggiornaEnte(codiceEnte, codiceModulo);
        } else {
          // gestione pagina scelta ente
          if (codiceModulo) {
            // se è valorizzato idModulo visualizzo direttamente il modulo selezionato se presente
            this.loginMonoEnte(codiceModulo);
          } else {
            // selezione dell'ente
            this.eventBus.cast('multi-ente:set', userInfo);

            console.log("*** LOGIN MULTI ENTE ***");
            console.log("*** url params ***" + urlParams);

            this.loginMultiEnte();
          }
        }
      } else {
        if (userInfo.multiEntePortale && userInfo.ente) {
          if (codiceEnte) {
            this.loginAggiornaEnte(codiceEnte, codiceModulo);
          } else {
            console.log(' Ente: ' + userInfo.ente);
            this.observeService.setEventResponse(userInfo);
          }
        }
        // gestione mono ente (o dopo selezione ente nel caso multi-ente) con eventuale gestione codice_modulo parametrico
        this.loginMonoEnte(codiceModulo);
      }
    }

  }

  loginMonoEnte(codiceModulo) {
    if (codiceModulo) {
      const filtro = new FiltroModuli();
      if (isNaN(codiceModulo)) {
        filtro.codiceModulo = codiceModulo;
      } else {
        filtro.idModulo = codiceModulo;
      }
      this.eventBus.cast("active-nav-bar:enable", NavSelection.NUOVA_ISTANZA);
      this.eventBus.cast("nav-bar:anchor:disable", true);
      this.moonService.getModuloFiltrato(filtro).subscribe(
        (m: Modulo[]) => {
          if (m === undefined || m.length === 0) {
            const notifica = new Notifica('NOT_FOUND', 'Modulo non disponibile', 'Avviso', null);
            this.securityService.goToNotificationError(notifica);
          }
          this.router.navigate(['manage-form/NEW/' + m[0].idModulo + '/' + m[0].idVersioneModulo], { queryParams: { direct: true } });
        }
      );
    } else { // id Modulo non valorizzato
      this.router.navigate(['home']);
    }
  }

  loginMultiEnte() {

    console.log('*** call servizio elencoEnti ***')

    this.moonService.getElencoEnti().subscribe((enti) => {
      if (enti.length > 1) {
        this.sharedService.elencoEnti = enti;

        this.eventBus.cast('multi-ente:choose', 'scelta ente');

        this.sharedService.nav = new Nav(NavSelection.ENTE, 'home/scegli-ente');
        this.router.navigate(['home']);
      }
    });
  }

  loginAggiornaEnte(codiceEnte, idModulo) {
    this.moonService.getEnte(codiceEnte).subscribe(
      (ente: Ente) => {
        console.log('ente by codice ente: ' + JSON.stringify(ente));
        this.moonService.aggiornaEnte(ente).subscribe(response => {

            StorageManager.add(STORAGE_KEYS.JWT_MOON, response.headers.get(HEADERS_MOON_IDENTITA_JWT));

            console.log(response);
            console.log('set observable for header update - response of patch imposta ente');
            const userInfo = response.body;
            this.eventBus.cast('multi-ente:set', userInfo);
            this.sharedService.nav = new Nav(NavSelection.BACHECA, 'home/bacheca');

            if (idModulo) {
              this.loginMonoEnte(idModulo);
            } else {
              this.router.navigate(['home']);
            }
          },
          error => {
            console.log('errore aggiornamento ente');
            // gestione pagina scelta ente
            this.loginMultiEnte();
          });
      },
      err => {
        console.log('acquisizione parametrica ente: errore nel recupero ente');
        // gestione pagina scelta ente
        this.loginMultiEnte();
      }
    );
  }

  loginEmbedded(user: User) {

    const embeddedNav: EmbeddedNavigator = user.embeddedNavigator;

    if (embeddedNav) {
      switch (embeddedNav.service) {
        case EMB_SERVICE_NAME.VIEW_ISTANZA:
          console.log('EMBEDDED VIEW ISTANZA');
          this.router.navigate(['/emb/manage-form/VIEW/' + embeddedNav.params.idIstanza]);
          break;
        case EMB_SERVICE_NAME.EDIT_ISTANZA:
          console.log('EMBEDDED EDIT ISTANZA');
          this.router.navigate(['/emb/manage-form/UPDATE/' + embeddedNav.params.idIstanza]);
          break;
        case EMB_SERVICE_NAME.NEW_ISTANZA:
          console.log('EMBEDDED NUOVA ISTANZA');
          this.router.navigate(['/emb/manage-form/NEW/' + embeddedNav.params.idModulo + '/' + embeddedNav.params.idVersioneModulo]);
          break;
        case EMB_SERVICE_NAME.ISTANZA:
          console.log('EMBEDDED WORKFLOW ISTANZA');
          this.router.navigate(['/emb/istanza/' + embeddedNav.params.idIstanza]);
          break;
        case EMB_SERVICE_NAME.ISTANZE:
          console.log('EMBEDDED LISTA ISTANZE');
          this.router.navigate(['/emb/istanze']);
          break;
        case EMB_SERVICE_NAME.ISTANZE_INVIATE:
          //@TODO
          console.log('EMBEDDED LISTA ISTANZE INVIATE');
          break;
        case EMB_SERVICE_NAME.ISTANZE_IN_INTEGRAZIONE:
          //@TODO
          console.log('EMBEDDED LISTA ISTANZE IN INTEGRAZIONE');
          break;
        case EMB_SERVICE_NAME.ISTANZE_IN_LAVORAZIONE:
          //@TODO
          console.log('EMBEDDED LISTA ISTANZE IN LAVORAZIONE');
          break;
        default:
          console.log('emb.service non valorizzato');
      }
    }
  }

  loginConsumer(user: User) {

    const consumerParams: ConsumerParams = user.consumerParams;
    const codiceIstanza = consumerParams.codiceIstanza;
    const codiceModulo = consumerParams.codiceModulo;

    if (consumerParams) {
      const service = consumerParams.service?.toUpperCase();

      console.log("consumer: " + consumerParams.consumer);
      console.log("service: " + service);
      console.log("codice istanza: " + codiceIstanza);
      console.log("codice modulo: " + codiceModulo);

      if (codiceIstanza && !codiceModulo) {
        this.moonService.getIstanzaByCodice(codiceIstanza)
          .subscribe(istanza => {
              switch (service) {
                case EMB_SERVICE_NAME.VIEW_ISTANZA:
                  console.log('EMBEDDED VIEW ISTANZA');
                  this.router.navigate(['/emb/manage-form/VIEW/' + istanza.idIstanza]);
                  break;
                case EMB_SERVICE_NAME.EDIT_ISTANZA:
                  console.log('EMBEDDED EDIT ISTANZA');
                  this.router.navigate(['/emb/manage-form/UPDATE/' + istanza.idIstanza]);
                  break;
                case EMB_SERVICE_NAME.ISTANZA:
                  console.log('EMBEDDED WORKFLOW ISTANZA');
                  this.router.navigate(['/emb/istanza/' + istanza.idIstanza]);
                  break;
                default:
                  console.log('emb.service non valorizzato');
              }
            }
          );
      } else if (codiceModulo) {
        switch (service) {
          case EMB_SERVICE_NAME.NEW_ISTANZA:
            console.log('EMBEDDED NUOVA ISTANZA');

            const filtro = new FiltroModuli();
            filtro.codiceModulo = codiceModulo;
            this.moonService.getModuloFiltrato(filtro).subscribe(
              (m: Modulo[]) => {
                if (m === undefined || m.length === 0) {
                  const notifica = new Notifica('NOT_FOUND', 'Modulo non disponibile', 'Avviso', null);
                  this.securityService.goToNotificationError(notifica);
                }
                this.router.navigate(['manage-form/NEW/' + m[0].idModulo + '/' + m[0].idVersioneModulo]);
              }
            );
            break;
          default:
            console.log('service non valorizzato');
        }
      }
    }
  }

}


