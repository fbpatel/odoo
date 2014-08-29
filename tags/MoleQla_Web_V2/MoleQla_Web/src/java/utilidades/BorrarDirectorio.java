/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilidades;

import java.io.File;

/**
 * @file BorrarDirectorio.java
 * @author Rafael Rastrero Prieto
 * @date 21-Agosto-2014
 * @description Borrar un directorio y todos sus archivos
 */
public class BorrarDirectorio {

    /**
     * Método que elimina todos los archivos de una carpeta
     *
     * @param directorio ruta de la carpeta
     */
    public static void borrarDirectorio(File directorio) {

        File[] ficheros = directorio.listFiles();

        if (ficheros != null) {

            for (File fichero : ficheros) {
                if (fichero.isDirectory()) {
                    borrarDirectorio(fichero);
                }
                fichero.delete();
            }

        }

    }

}
