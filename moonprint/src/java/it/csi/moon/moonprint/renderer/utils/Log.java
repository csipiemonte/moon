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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

/**
 * @author Roberto Politi
 */
public class Log {

    public enum Level {
        ALL,        // Report all log messages. Highest possible severity.
        TRACE,      // Verbose debugging.
        DEBUG,      // Fine-grained debugging.
        INFO,       // Coarse-grained application flow.
        WARN,       // Potentially harmful situation.
        ERROR,      // Potentially recoverable error.
        FATAL,      // Unrecoverable error.
        NONE,       // Report no log messages. Lowest possible severity.
    }

    public static void t(String message) {
        log(Level.TRACE, message);
    }

    public static void d(String message) {
        log(Level.DEBUG, message);
    }

    public static void i(String message) {
        log(Level.INFO, message);
    }

    public static void w(String message) {
        log(Level.WARN, message);
    }

    public static void e(String message) {
        log(Level.ERROR, message);
    }

    public static void f(String message) {
        log(Level.FATAL, message);
    }

    public static void log(Level loglevel, String message) {
        Date now = new Date();
        //SimpleDateFormat sdf = new SimpleDateFormat("");
        String logMessage = "[" + now.toString() + "][" + loglevel.name() + "] " + message;
        // System.err.println(logMessage); // ROBY --> NON usare stdout altrimenti va in conflitto con l'output vero e proprio
        logToFile(loglevel, logMessage);
    }

//    public static Level fromString(String levelString) {
//        return Level.valueOf(levelString);
//    }

    private static void logToFile(Level logLevel, String logMessage) {
        logMessage += "\r\n";
//        System.out.println("[logToFile] currentLogLevel=" + logLevel.ordinal() + "(" + logLevel.name() + ")");
        if (!Globals.config.getLogToFileLevel().equals(Level.NONE.name()) && Level.valueOf(Globals.config.getLogToFileLevel()).ordinal() <= logLevel.ordinal()) {
//            System.out.println("[logToFile] currentLogLevel=" + logLevel.ordinal() + "(" + logLevel.name() + ") ==> SALVO messsaggio su file (" + Globals.config.getLogFilePath() + ") ==> " + logMessage);
            try {
                if (Globals.config.getLogFilePath().contains(File.separator)) {
                    String path = Globals.config.getLogFilePath().substring(0, Globals.config.getLogFilePath().lastIndexOf(File.separator));
                    if (!Files.exists(Paths.get(path)))
                        Files.createDirectories(Paths.get(path));
                }
                if (!Files.exists(Paths.get(Globals.config.getLogFilePath()))) {
//                    // Crea i permessi del file.
//                    Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-rw----");
//                    FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                    Files.createFile(Paths.get(Globals.config.getLogFilePath()));
                }
                Files.write(Paths.get(Globals.config.getLogFilePath()), logMessage.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                // System.err.println("ERRORE: Impossibile salvare il file di log. " + e.getMessage());
                // System.err.println("Verificare la configurazione e i permessi del file di log e del suo percorso!");
                // e.printStackTrace();
            }
  //      } else {
//            System.out.println("[logToFile] currentLogLevel=" + logLevel.ordinal() + "(" + logLevel.name() + ") ==> NON salvo su file");
        }
    }
}
