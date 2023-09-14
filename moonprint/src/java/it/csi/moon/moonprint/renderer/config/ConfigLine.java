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
public class ConfigLine {

    private static final String[] COMMENT_PREFIX = {"#", "//"};
    private String line;
    private String key;
    private String value;

    public ConfigLine(String _line) {
        line = _line;

        if (isValid()) {
            key = line.split("=")[0].trim();
            value = line.split("=")[1].trim();
        }
    }

    public boolean isEmpty() {
        return line.isEmpty();
    }

    public boolean isComment() {
        for (String aCOMMENT_PREFIX : COMMENT_PREFIX) {
            if (line.startsWith(aCOMMENT_PREFIX)) {
                return true;
            }
        }
        return false;
        //return line.startsWith(COMMENT_PREFIX);
    }

    public boolean isValid() {
        return !isEmpty()
                && !isComment()
                && line.split("=").length == 2
                && Config.validConfigParams.contains(line.split("=")[0].trim());
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
