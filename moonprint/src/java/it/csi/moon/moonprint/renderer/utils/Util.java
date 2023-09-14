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

import java.time.Instant;

/**
 *
 * @author Carola Grossi
 */
public class Util {

    public static long dateStrToLong(String datetime) {
        Instant instant = Instant.parse(datetime);
        return instant.getEpochSecond();
    }

    public static String normalizePath(String path) {
        if (path.startsWith("/") || path.startsWith("\\")) {
            path = path.substring(1, path.length());
        }
        if (path.endsWith("/") || path.endsWith("\\")) {
            path = path.substring(0, path.length()-1);
        }
        if (!path.startsWith("file:/")) {
            path = "file:/" + path;
        }
        return path;
    }

}
