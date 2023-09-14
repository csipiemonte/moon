/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.csi.moon.moonprint.renderer.config;

/**
 *
 * @author Roberto Politi
 */
public class CommandLineParam {

    private static final String VALID_PREFIX = "--";
    private String param;
    private String key;
    private String value;

    public CommandLineParam(String _param) {
        param = _param;

        if (isValid()) {
            key = param.split("=")[0].trim().replaceFirst("--", "");
            value = param.split("=")[1].trim();
        }
    }

    public boolean isValid() {
        boolean wellFormed = false;
        if (param.startsWith(VALID_PREFIX)) {
            wellFormed = true;
        }

        if (wellFormed) {
            return param.split("=").length == 2
                    && Config.validConfigParams.contains(param.split("=")[0].trim().replaceFirst("--", ""));
        }
        return false;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

}
