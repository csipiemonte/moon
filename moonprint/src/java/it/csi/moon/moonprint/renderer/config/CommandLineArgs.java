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

import it.csi.moon.moonprint.renderer.utils.Globals;
import java.util.HashMap;

/**
 *
 * @author Roberto Politi
 */
public class CommandLineArgs {

    private static CommandLineArgs instance = null;
    public HashMap<String, String> cfg;

    private CommandLineArgs() {
        cfg = new HashMap<>();
    }

    public static synchronized CommandLineArgs getInstance() {
        if (instance == null) {
            instance = new CommandLineArgs();
        }
        return instance;
    }

    public void init(String[] _args) {

        for (String _arg : _args) {
            //System.out.println(cfg[i]);

            CommandLineParam clp = new CommandLineParam(_arg);
            if (clp.isValid()) {
                if (cfg.containsKey(clp.getKey())) {
                    // System.out.println("PARAMETRO DUPLICATO: " + clp.getKey());
                }
                cfg.put(clp.getKey(), clp.getValue());
            }
        }
    }

    public void print() {
        // System.out.println(Globals.APP_NAME + " - Versione " + Globals.APP_VERSION_NAME + "\n");
        // System.out.println("Parametri da linea di comando:");
//        for (String key : cfg.keySet()) {
//            System.out.println("[" + key + "] = " + cfg.get(key));
//        }
        cfg.keySet().forEach((key) -> System.out.println("[" + key + "] = " + cfg.get(key)));
        // System.out.println("-");
    }

    public String toString() {
        String result = "Parametri da linea di comando:\r\n";
        for (String key : cfg.keySet()) {
            result += "\t[" + key + "] = " + cfg.get(key) + "\r\n";
        }
        result += "\t--\r\n";
        return result;
    }

}
