/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class AttrNotifyConf {
    serviceName: string;
    serviceToken: string;
    
    initAzione: string;
    initAzioneRequired: string;
    sendAzione: boolean;
    sendAzioneRequired: string;
    
    email: {
            subject: string;
            body: string;
            template_id: string;
            prefRequired: boolean;
            send: boolean;
    };
    sms: {
            content: string;
            prefRequired: boolean;
            send: boolean;
    };
   
    errori:{
        "MOONSRV-30414": string;
        "MOONSRV-30415": string;
        "MOONSRV-30416": string;
        "MOONSRV-30417": string;
    }
    
   
}
