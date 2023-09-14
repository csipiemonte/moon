/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.renderer;

public abstract class BaseRenderer {
    public abstract byte[] toPdf(String _jsonString, String _templateName);
}
