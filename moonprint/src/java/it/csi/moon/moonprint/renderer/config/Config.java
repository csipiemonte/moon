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
import it.csi.moon.moonprint.renderer.utils.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author Roberto Politi
 */
public class Config {

    private static Config instance = null;

    private HashMap<String, String> cfg;

    private final String DEFAULT_CONFIG_FILE_NAME = Globals.APP_NAME + ".cfg";
    private final String DEFAULT_LOG_FILE_NAME = Globals.APP_NAME + ".log";
    //private final String DEFAULT_FILE_PATH = "." + File.separator + DEFAULT_FILE_NAME;
    private final String DEFAULT_CONFIG_FILE_PATH = DEFAULT_CONFIG_FILE_NAME;
    private String configFileName = DEFAULT_CONFIG_FILE_NAME;
    private String configFilePath = DEFAULT_CONFIG_FILE_PATH;

    // <editor-fold defaultstate="collapsed" desc="[> Allowed Params]">
    // global parameters (do not change)
    public static final String CONFIG_PARAM_CONFIG_FILE = "config";
    public static final String CONFIG_PARAM_LOG_FILE_PATH = "log_file_path";
    public static final String CONFIG_PARAM_LOG_TO_FILE_LEVEL = "log_to_file_level";
    //custom parameters
    public static final String CONFIG_PARAM_JSON_FILE = "json_file";
    //public static final String CONFIG_PARAM_JSON = "json";
    public static final String CONFIG_PARAM_OUTPUT_FORMAT = "output_format";
    public static final String CONFIG_PARAM_TEMPLATE = "template";


    public static ArrayList<String> validConfigParams = new ArrayList<String>() {
        {
            // global parameters (do not change)
            add(CONFIG_PARAM_CONFIG_FILE);
            add(CONFIG_PARAM_LOG_FILE_PATH);
            add(CONFIG_PARAM_LOG_TO_FILE_LEVEL);
            //custom parameters
            add(CONFIG_PARAM_OUTPUT_FORMAT);
            add(CONFIG_PARAM_TEMPLATE);
            add(CONFIG_PARAM_JSON_FILE);
//            add(CONFIG_PARAM_JSON);

        }
    };
// </editor-fold>

    public static HashMap<String, String> defaultConfigParams = new HashMap<String, String>() {
        {
            put(CONFIG_PARAM_CONFIG_FILE, Globals.APP_NAME + ".cfg");
            put(CONFIG_PARAM_OUTPUT_FORMAT, "pdf");
            put(CONFIG_PARAM_TEMPLATE, "default");
            put(CONFIG_PARAM_JSON_FILE, "data.json");
//            put(CONFIG_PARAM_JSON, "{}");

            // Logging
            put(CONFIG_PARAM_LOG_FILE_PATH, Globals.APP_NAME + ".log");
            put(CONFIG_PARAM_LOG_TO_FILE_LEVEL, Log.Level.ALL.name());
        }
    };

    private Config() {
        cfg = new HashMap<>();
        cfg.put(CONFIG_PARAM_CONFIG_FILE, DEFAULT_CONFIG_FILE_NAME);
    }

    public static synchronized Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

//    public int init(CommandLineArgs clArgs) {
//        return init(clArgs, DEFAULT_CONFIG_FILE_PATH);
//    }

    public int init(CommandLineArgs clArgs, String baseDirectory) {
        return init(clArgs, baseDirectory, DEFAULT_CONFIG_FILE_PATH);
    }

    public int init(CommandLineArgs clArgs, String baseDirectory, String configFileName) {
        // add command line config first
        for (String key : clArgs.cfg.keySet()) {
            cfg.put(key, clArgs.cfg.get(key));
        }

        if (baseDirectory == null)
            baseDirectory = "";
        if (baseDirectory.endsWith("/") || baseDirectory.endsWith("\\")) {
            baseDirectory = baseDirectory.substring(0,baseDirectory.length() - 1) ;
        }
        baseDirectory = baseDirectory + File.separator;

        configFilePath = baseDirectory + configFileName;
        try(BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line; //reader.readLine();
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                ConfigLine cl = new ConfigLine(line);
                if (cl.isValid()) {
                    if (cfg.containsKey(cl.getKey())) {
                        // System.err.println("CHIAVE DUPLICATA: " + cl.getKey());
                        //Log.w("CHIAVE DUPLICATA: " + cl.getKey());
                    } else {
                        cfg.put(cl.getKey(), cl.getValue());
                    }
                }
            }

            // adding default parameters (if missing or not specified in config)
            addDefaultParameters();
        } catch (IOException e) {
            // config file not found --> using default parameters
            addDefaultParameters();

            Log.d("Eccezione in initConf : " + e.getMessage() + " --> Verranno usati i parametri di default");
        }
        return 0;
    }

    private void addDefaultParameters() {
        for (String key : validConfigParams) {
            if (!cfg.containsKey(key)) {
                cfg.put(key, defaultConfigParams.get(key));
            }
        }
    }

    public static String appNameAndVersion() {
        return Globals.APP_NAME + " - Versione " + Globals.APP_VERSION_NAME;
    }

    public void print() {
        // System.out.println(appNameAndVersion() + "\n");
        // System.out.println("Configurazione attuale:");
        for (String key : cfg.keySet()) {
            System.out.println("[" + key + "] = " + cfg.get(key));
        }
        // System.out.println("-");
    }

    public String toString() {
        //String result = appNameAndVersion() + "\r\n";
        String result = "Configurazione attuale:\r\n";
        for (String key : cfg.keySet()) {
            String padding = String.join("", Collections.nCopies(18 - key.length(), " "));
            result += "\t[" + key + "] " + padding + "= " + cfg.get(key) + "\r\n";
        }
        result += "\t--\r\n";
        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="[> Config parmeters getters]">

    public String getLogToFileLevel() {
        return cfg.get(Config.CONFIG_PARAM_LOG_TO_FILE_LEVEL);
    }

    public String getLogFilePath() {
        //return cfg.get(Config.CONFIG_PARAM_LOG_FILE_PATH).replaceAll("/", File.separator);
        if ( cfg.get(Config.CONFIG_PARAM_LOG_FILE_PATH).endsWith(File.separator) || cfg.get(Config.CONFIG_PARAM_LOG_FILE_PATH).endsWith("/")) {
            return cfg.get(Config.CONFIG_PARAM_LOG_FILE_PATH) + File.separator + DEFAULT_LOG_FILE_NAME;
        }
        return cfg.get(Config.CONFIG_PARAM_LOG_FILE_PATH);
    }

    public String getTemplate() {
        return cfg.get(Config.CONFIG_PARAM_TEMPLATE);
    }

    public String getOutputFormat() {
        return cfg.get(Config.CONFIG_PARAM_OUTPUT_FORMAT);
    }

    public String getJsonFile() {
        return cfg.get(Config.CONFIG_PARAM_JSON_FILE);
    }
//    public String getJson() {
//        return cfg.get(Config.CONFIG_PARAM_JSON);
//    }

// </editor-fold>
}
