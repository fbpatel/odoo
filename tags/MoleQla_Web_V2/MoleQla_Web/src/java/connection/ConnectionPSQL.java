/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Rafa
 */
public class ConnectionPSQL {

    //DATOS PARA LA CONEXION
    private static String bd = "moleqla";
    private static String user = "openuser";
    private static String password = "openerp";
    private static String url = "jdbc:postgresql://localhost:5432/" + bd;

    private ResultSet resultSet = null;
    private Statement statement = null;

    //Constructor de clase que se conecta a la base de datoS
    public ConnectionPSQL() {

    }

    public static Connection connection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conectado a la base de datos [ " + bd + "]");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }
}
