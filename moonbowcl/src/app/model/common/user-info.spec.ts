/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { inject, waitForAsync } from '@angular/core/testing';
import {UserInfo} from './user-info';
import {Ruolo} from './ruolo';

describe ('Testing UserInfo', () => {
    let currentUser: UserInfo;

    beforeEach(() => {
     let user = {
         "nome": "ALBERTO",
         "cognome": "DEIRO",
         "codFisc": "DRELRT69T20E379V",
         "idEnte": 14,
         "ente": "COMUNE DI Alba",
         "tipoUtente": {
             "codice": "ADM",
             "descrizione": "utente admin"
         },
         "entiAreeRuoli": [
             {
                 "idEnte": 15,
                 "codiceEnte": "004003",
                 "nomeEnte": "Alba",
                 "descrizioneEnte": "COMUNE DI Alba",
                 "idTipoEnte": 2,
                 "areeRuoli": [
                     {
                         "idArea": 16,
                         "codiceArea": "EDIL_PRIV",
                         "nomeArea": "Ufficio comunale Edilizia privata",
                         "idRuolo": 4,
                         "codiceRuolo": "OP_SIMP",
                         "nomeRuolo": "Operatore semplice"
                     }
                 ]
             },
             {
                 "idEnte": 14,
                 "codiceEnte": "004003",
                 "nomeEnte": "Alba",
                 "descrizioneEnte": "COMUNE DI Alba",
                 "idTipoEnte": 2,
                 "areeRuoli": [
                     {
                         "idArea": 16,
                         "codiceArea": "14-EDIL_PRIV",
                         "nomeArea": "Ufficio comunale Edilizia privata",
                         "idRuolo": 4,
                         "codiceRuolo": "OP_SIMP",
                         "nomeRuolo": "Operatore semplice"
                     },
                     {
                         "idArea": 16,
                         "codiceArea": "EDIL_PRIV",
                         "nomeArea": "Ufficio comunale Edilizia privata",
                         "idRuolo": 4,
                         "codiceRuolo": "OP_BLDR",
                         "nomeRuolo": "Operatore Builder"
                     }
                 ]
             }
         ],
         "ruoli": null,
         "funzioni": null,
         "idIride": "DRELRT69T20E379V/ALBERTO/DEIRO/SP//1//",
         "idMoonToken": null,
         "jwt": null,
         "isAuthenticated": true
     };
        currentUser = new UserInfo();
        currentUser.codFisc = user.codFisc;
        currentUser.cognome = user.cognome;
        currentUser.nome = user.nome;
        currentUser.ente = user.ente;
        currentUser.funzioni = user.funzioni;
        currentUser.idEnte = user.idEnte;
        currentUser.idIride = user.idIride;
        currentUser.jwt = user.jwt;
        currentUser.tipoUtente = user.tipoUtente;
        currentUser.isAuthenticated = true;
        // impostare ruoli in base ad utente
        const enteUtente = currentUser.idEnte;
        if (user['entiAreeRuoli'] != null && user['entiAreeRuoli'].length > 0 ) {
            user['entiAreeRuoli'].forEach(enteAreeRuoli => {
                if (enteAreeRuoli['idEnte'] === enteUtente) {
                    const areeRuoli: any[] = enteAreeRuoli['areeRuoli'];
                    areeRuoli.forEach(el => {
                        const r = new Ruolo(el['idRuolo'], el['codiceRuolo'], el['nomeRuolo']);
                        currentUser.ruoli.push(r);
                    });
                }
            });


            // prendo primo elemento

        }
        console.log(JSON.stringify(currentUser));
        // this.sharedService.UserLogged = this.currentUser;
    });


    afterEach(() => {
        currentUser = null;
    });

    it('Controllo se tipologia Utente è ADMIN', () => {
      expect(currentUser.isTipoADMIN()).toBe(true);
    });

});

