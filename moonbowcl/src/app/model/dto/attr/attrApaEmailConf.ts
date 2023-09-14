/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class AttrApaEmailConf {
    INVIO_EMAIL_DEFAULT: string;
    PROTOCOLLA: {
        to: string,
        subject: string,
        text: string,
        attachments: {
            istanza: boolean,
            allegati: boolean,
            allegatiAzione: boolean
        }
    };
    NOTIFICA_FRUITORE: {
        to: string,
        subject: string,
        text: string,
        attachments: {
            istanza: boolean,
            allegati: boolean,
            allegatiAzione: boolean
        }
    };
    DEFAULT: {
        to: string,
        subject: string,
        text: string,
        attachments: {
            istanza: boolean,
            allegati: boolean,
            allegatiAzione: boolean
        }
    };
}
