/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { AlertService } from 'src/app/modules/alert';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UtenteEnteAbilitato } from 'src/app/model/dto/utente ente-abilitato';
import { faEdit, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { Area } from 'src/app/model/dto/area';
import { Ruolo } from 'src/app/model/common/ruolo';
import { AreaRuolo } from 'src/app/model/dto/area-ruolo';
import { EnteAreaRuolo } from 'src/app/model/dto/ente-area-ruolo';
import { Ente } from 'src/app/model/dto/ente';

@Component({
  selector: 'app-utenti-dettaglio-aree-ruolo',
  templateUrl: './aree-ruolo.component.html',
  styleUrls: ['./aree-ruolo.component.scss']
})
export class AreeRuoloComponent implements OnInit {

  isAdmin = false;

  faEdit = faEdit;
  faTrashAlt = faTrashAlt;

  @Input() utenteSelezionato: UtenteEnteAbilitato;
  @Output() eventModifica = new EventEmitter<UtenteEnteAbilitato>();

  entiAreeRuoliAbilitati: EnteAreaRuolo[];
  ente: Ente;

  areeEnte: Area[];
  areeNonAbilitate: Area[];
  ruoli: Ruolo[];

  newARidEnte: number;
  newARidArea: number;
  newARidRuolo: number;

  alertOptions = {
    id: 'alert-utenti-area-ruolo',
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(
    private moonboblService: MoonboblService,
    private sharedService: SharedService,
    protected alertService: AlertService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.ente = this.sharedService.UserLogged.ente;
    this.newARidEnte = this.ente.idEnte;
  }

  ngOnInit(): void {
    log('aree-ruolo::ngOnInit() utenteSelezionato = ' + this.utenteSelezionato);
    if (this.utenteSelezionato.entiAreeRuoli) {
      this.entiAreeRuoliAbilitati = [...this.utenteSelezionato.entiAreeRuoli];
    } else {
      this.entiAreeRuoliAbilitati = [];
    }

    this.moonboblService.getAreeByIdEnte(this.sharedService.UserLogged.ente.idEnte).subscribe(
      (res) => {
        log('aree-ruolo::ngOnInit() moonboblService.getAreeByIdEnte() res = ' + JSON.stringify(res));
        this.areeEnte = res;
        log('aree-ruolo::ngOnInit() moonboblService.getAreeByIdEnte() this.areeEnte = ' + JSON.stringify(this.areeEnte));
        // Init degli attributi Editabile per il FORM (ci vorrebbe un clone() )
        this.aggiornaAreeNonAbilitate();
      },
      (err) => {
        this.alertService.error('Errore di inizializzazione (areeEnte) ! ' + err.errorMsg, this.alertOptions);
        log('aree-ruolo::ngOnInit() Errore di inizializzazione (areeEnte) ! ' + err.errorMsg);
        //        this.spinnerService.hide();
      }
    );
    this.moonboblService.getRuoli().subscribe(
      (res) => {
        this.ruoli = res;
        log('aree-ruolo::ngOnInit() moonboblService.getRuoli() this.ruoli = ' + JSON.stringify(this.ruoli));
        if (!this.isAdmin) { // nel caso di OP_ADM o RESP ...
          this.ruoli = this.ruoli.filter(r => r.codiceRuolo !== 'ADMIN');
          log('aree-ruolo::ngOnInit() moonboblService.getRuoli() NOT ADMIN this.ruoli = ' + JSON.stringify(this.ruoli));
        } else {
          log('aree-ruolo::ngOnInit() loggedUser.isAdmin ADMIN this.ruoli = ' + JSON.stringify(this.ruoli));
          if (this.utenteSelezionato.tipoUtente.codice === 'ADM') {
            log('aree-ruolo::ngOnInit() Filtro Ruoli ===ADMIN');
            this.ruoli = this.ruoli.filter(r => r.codiceRuolo === 'ADMIN');
          } else {
            log('aree-ruolo::ngOnInit() Filtro Ruoli !==ADMIN');
            this.ruoli = this.ruoli.filter(r => r.codiceRuolo !== 'ADMIN');
          }
        }
        log('aree-ruolo::ngOnInit() OUT ruoli = ' + JSON.stringify(this.ruoli));
        if (this.ruoli) {
          this.newARidRuolo = this.ruoli[0].idRuolo;
        }
      },
      (err) => {
        this.alertService.error('Errore di inizializzazione (ruoli) ! ' + err.errorMsg, this.alertOptions);
        log('aree-ruolo::ngOnInit() Errore di inizializzazione (ruoli) ! ' + err.errorMsg);
      }
    );
  }

  aggiornaAreeNonAbilitate() {
    this.areeNonAbilitate = [];
    const currIdEnte = this.sharedService.UserLogged.ente.idEnte;
//  log('aree-ruolo::aggiornaAreeNonAbilitate() currIdEnte = ' + currIdEnte);
//  log('aree-ruolo::aggiornaAreeNonAbilitate() utenteSelezionato.entiAreeRuoli = ' + JSON.stringify(this.utenteSelezionato.entiAreeRuoli));
    if (this.entiAreeRuoliAbilitati) {
      const currEnteAR = this.entiAreeRuoliAbilitati.find(e => e.idEnte === currIdEnte);
      if (currEnteAR) {
//      log('aree-ruolo::aggiornaAreeNonAbilitate() currEnteAR = ' + JSON.stringify(currEnteAR));
//      log('aree-ruolo::aggiornaAreeNonAbilitate() currEnteAR.areeRuoli = ' + JSON.stringify(currEnteAR.areeRuoli));
        const elencoIdAreaAbilitate = currEnteAR.areeRuoli?.map(ar => ar.idArea);
        log('aree-ruolo::aggiornaAreeNonAbilitate() elencoIdAreaAbilitate = ' + elencoIdAreaAbilitate);
        if (elencoIdAreaAbilitate) {
          log('aree-ruolo::aggiornaAreeNonAbilitate() this.areeEnte = ' + this.areeEnte);
          this.areeNonAbilitate = this.areeEnte.filter(a => (elencoIdAreaAbilitate.indexOf(a.idArea) === -1));
          log('aree-ruolo::aggiornaAreeNonAbilitate() this.areeNonAbilitate = ' + this.areeNonAbilitate);
          log('aree-ruolo::aggiornaAreeNonAbilitate() this.areeNonAbilitate = ' + JSON.stringify(this.areeNonAbilitate));
        } else {
          log('aree-ruolo::aggiornaAreeNonAbilitate() ALL aree.2');
          this.areeNonAbilitate = this.areeEnte;
        }
      } else {
        log('aree-ruolo::aggiornaAreeNonAbilitate() ALL aree.');
        this.areeNonAbilitate = this.areeEnte;
      }
      if (this.areeNonAbilitate) {
        this.newARidArea = this.areeNonAbilitate[0].idArea;
      }
    }
  }

  modifica(e: EnteAreaRuolo, ar: AreaRuolo) {
    log('aree-ruolo::modifica() e=' + JSON.stringify(e) + ' ar=' + JSON.stringify(ar));
  }
  elimina(e: EnteAreaRuolo, ar: AreaRuolo) {
//    log('aree-ruolo::elimina() e=' + JSON.stringify(e) + ' ar=' + JSON.stringify(ar));
    log('aree-ruolo::elimina() e=' + e.idEnte + ' idRuolo=' + ar.idArea + ' idRuolo=' + ar.idRuolo);
    this.moonboblService.deleteUtenteAreaRuolo(this.utenteSelezionato.idUtente, e.idEnte, ar.idArea, ar.idRuolo).subscribe(
        (res) => {
          log('aree-ruolo::elimina() moonboblService.postAggiungiAreaRuolo() res = ' + JSON.stringify(res));
          if (res) {
            this.utenteSelezionato = res;
          } else {
            log('aree-ruolo::elimina() Era Ultimo AreaRuolo ? ' + JSON.stringify(this.utenteSelezionato));
            if (this.utenteSelezionato.entiAreeRuoli[0].areeRuoli.length === 1) {
              log('aree-ruolo::elimina() SI era unico AreaRuolo ');
              this.utenteSelezionato.entiAreeRuoli[0].areeRuoli = [];
            }
          }
          this.entiAreeRuoliAbilitati = [...this.utenteSelezionato.entiAreeRuoli];
          log('aree-ruolo::elimina() moonboblService.postAggiungiAreaRuolo() this.utente = '
            + JSON.stringify(this.utenteSelezionato));
          // Init degli attributi Editabile per il FORM (ci vorrebbe un clone() )
          this.aggiornaAreeNonAbilitate();
          // @Output() eventModifica
          this.eventModifica.emit(this.utenteSelezionato);
        },
        (err) => {
//          alert(err.errorMsg);
          this.alertService.error('Impossibile eliminare l\'abilitazione selezionata ! ' + err.errorMsg, this.alertOptions);
          log('aree-ruolo::elimina() Impossibile eliminare l\'abilitazione selezionata ! ' + err.errorMsg);
          //        this.spinnerService.hide();
        }
      );
  }
  aggiungiAreaRuolo() {
    log('aree-ruolo::aggiungiAreaRuolo() idEnte = ' + this.newARidEnte + ' idArea = ' +
      this.newARidArea + ' idRuolo = ' + this.newARidRuolo);
    this.moonboblService.postAggiungiAreaRuolo(this.utenteSelezionato.idUtente, this.newARidEnte,
      this.newARidArea, this.newARidRuolo).subscribe(
      (res) => {
          this.alertService.success('Salvataggio Area Ruolo effettuato con successo !', this.alertOptions);
          log('aree-ruolo::aggiungiAreaRuolo() moonboblService.postAggiungiAreaRuolo() res = ' + JSON.stringify(res));
          this.utenteSelezionato = res;
          this.entiAreeRuoliAbilitati = [...this.utenteSelezionato.entiAreeRuoli];
          log('aree-ruolo::aggiungiAreaRuolo() moonboblService.postAggiungiAreaRuolo() this.utente = '
            + JSON.stringify(this.utenteSelezionato));
        // Init degli attributi Editabile per il FORM (ci vorrebbe un clone() )
          this.aggiornaAreeNonAbilitate();
          this.eventModifica.emit(this.utenteSelezionato);
      },
      (err) => {
//        alert(err.errorMsg);
        this.alertService.error('Impossibile effettuare il salvataggio del nuovo Area Ruolo !' + err.errorMsg, this.alertOptions);
        log('aree-ruolo::aggiungiAreaRuolo() Impossibile effettuare il salvataggio del nuovo Area Ruolo ! ' + err.errorMsg);
        //        this.spinnerService.hide();
      }
    );

/*
    const currIdEnte = this.sharedService.UserLogged.ente.idEnte;
    const currEnteAR = this.entiAreeRuoliAbilitati?.find(e => e.idEnte === currIdEnte);
    log('aree-ruolo::aggiungiAreaRuolo() AREE Ricerca idArea = ' + this.newARidArea +
      '\areeEnte = ' + JSON.stringify(this.areeEnte));
    const newArea = this.areeEnte?.find(ae => ae.idArea === this.newARidArea);

    log('aree-ruolo::aggiungiAreaRuolo() RUOLO Ricerca idRuolo = ' + this.newARidRuolo +
      '\nruoli = ' + JSON.stringify(this.ruoli));
    const newRuolo = this.ruoli?.find(r => r.idRuolo === this.newARidRuolo);

    log('aree-ruolo::aggiungiAreaRuolo() FINALE idEnte = ' + this.newARidEnte + '\nnewArea = ' +
      JSON.stringify(newArea) + '\nnewRuolo = ' + JSON.stringify(newRuolo));
    if (currEnteAR && newArea && newRuolo) {
      log('aree-ruolo::aggiungiAreaRuolo() ...');
      const newAR = new AreaRuolo();
      newAR.idArea = newArea.idArea;
      newAR.codiceArea = newArea.codiceArea;
      newAR.nomeArea = newArea.nomeArea;
      newAR.idRuolo = newRuolo.idRuolo;
      newAR.codiceRuolo = newRuolo.codiceRuolo;
      newAR.nomeRuolo = newRuolo.nomeRuolo;
      newAR.dataUpdAbilitazione = new Date();
      newAR.attoreUpdAbilitazione = this.sharedService.UserLogged.codFisc;
//      currEnteAR.areeRuoli.push(newAR);
      this.entiAreeRuoliAbilitati.forEach(e => {
        if (e.idEnte === currIdEnte) {
          e.areeRuoli.push(newAR);
          log('aree-ruolo::aggiungiAreaRuolo() ... pushed');
        }
      });

    }*/
  }
}

function log(a: any) {
//  console.log(a);
}
