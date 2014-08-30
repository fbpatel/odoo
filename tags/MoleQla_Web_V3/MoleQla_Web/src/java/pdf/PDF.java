/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

import connection.ConnectionPSQL;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Rafa
 */
public class PDF {

    /* metodo que guarda una imagen JPG en la base de datos
     * input: ID : identificador unico para el registro, osea primary key
     * name: nombre de la imagen para reconocerlo mas adelante
     * ruta: direccion absoluta de la imagen JPG
     */
    /*public boolean guardarpdf(String id, String name, String ruta) {
        FileInputStream fis = null;
        try {
            File file = new File(ruta);
            fis = new FileInputStream(file);
            PreparedStatement pstm = connection.prepareStatement("INSERT into "
                    + " articulo(id, nombre, archivo) " + " VALUES(?,?,?)");
            pstm.setString(1, id);
            pstm.setString(2, name);
            pstm.setBinaryStream(3, fis, (int) file.length());
            pstm.execute();
            pstm.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }*/
/*
    Consulta de varios:
    while (rs.next())
    {
       System.out.print("Column 1 returned ");
       System.out.println(rs.getString(1));
    } rs.close()
    */
    public void descargarpdf(String ruta) {
        
        try {
            Connection connection = ConnectionPSQL.connection();
            InputStream is = null;
            StringBuffer out = null;
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT archivo FROM articulo");
            byte[] imgBytes = null;
            if (rs != null) {
                while (rs.next()) {
                    imgBytes = rs.getBytes(1);
                }

                FileOutputStream os = new FileOutputStream(ruta+"\\revista.pdf");
                os.write(imgBytes);
                os.flush();
                os.close();
            }

            rs.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
