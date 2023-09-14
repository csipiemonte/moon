/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { PortaliIf } from './portali-if';
import { LogonModeIf } from './logon-mode-if';

export class PortaleModuloLogonMode {
    idModulo: number;
    portale: PortaliIf;
    logonMode: LogonModeIf;
    filtro: string;
}
