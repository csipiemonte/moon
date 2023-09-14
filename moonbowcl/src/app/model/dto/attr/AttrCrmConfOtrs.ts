/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class AttrCrmConfOtrs{
    titolo: string;
    queue: string;
    type: string;
    state: string;
    priority: string;

    customerID: string;
    customerUser: string;

    communicationChannel: string;
    senderType: string;
    from: string;
    subject: string;
    body: string;
    contentType: string;

    includeAllegati: boolean;

    campiDinamici :any[] = [{}];
}