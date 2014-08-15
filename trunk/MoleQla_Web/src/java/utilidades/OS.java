package utilidades;
/**
 * @file OS.java
 * @author Juan Humanes Ferrer
 * @date 04-Marzo-2014
 * @description Clase utilizada para saber el sistema operativo
 */

public class OS {
    /**
     * Método que retorna el sistema operativo
     * @return OS
     */
    public static String getNameOS(){
        return System.getProperty("os.name");
    }
    
    /**
     * Método que retorna el separador dependiendo del SO
     * @return 
     */
    public static String getDirectorySeparator(){
        String separator="\\";
        if(getNameOS().indexOf("Windows")<0)//No es Windows
            separator="//";
        
        return separator;
            
    }
    
}
