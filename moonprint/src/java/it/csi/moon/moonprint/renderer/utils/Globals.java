/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.csi.moon.moonprint.renderer.utils;

import it.csi.moon.moonprint.renderer.config.Config;

/**
 *
 * @author Roberto Politi
 */
public class Globals {
    public static final String APP_NAME = "MOOnPrintRenderer";
    public static final String APP_VERSION_NAME = "1.0";
    public static final String APP_VERSION_CODE = "1";

    public enum OUTPUT_FORMATS { PDF, HTML, ZIP};

    public static Config config;

}
