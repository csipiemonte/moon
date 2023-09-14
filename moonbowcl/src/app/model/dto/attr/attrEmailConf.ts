/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class AttrEmailConf {
    to: string;
    to_istanza_data_key: string;
    cc: string;
    cc_istanza_data_key: string;
    bcc: string;
    bcc_istanza_data_key: string;
    subject: string;
    text: string;
    html: string;
    attach_istanza: boolean;
    attach_allegati: boolean;
    protocollo_to: string;
    subject_rinvio: string;
    text_rinvio: string;
    html_rinvio: string;
}
