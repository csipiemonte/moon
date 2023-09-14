/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.renderer.template.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class DataModel {

    public static class Document {
        @JsonProperty
        public String title;
    }
}
