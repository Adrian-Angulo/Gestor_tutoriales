/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADRIAN CASTILLO
 */
public class Categoria {

    private int id_C;
    private String nombre;

    public Categoria(String nombre) {

        this.nombre = nombre;
    }

    public Categoria() {
    }

    public int getId_C() {
        return id_C;
    }

    public void setId_C(int id_C) {
        this.id_C = id_C;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public static boolean agregarCategoria(String nombre) {
        Conexion db_connect = new Conexion();
        boolean agregado = false;
        try (Connection conexion = db_connect.get_connection()) {
            PreparedStatement ps = null;
            try {
                String query = "INSERT INTO categoria (nombre) VALUES (?)";
                ps = conexion.prepareStatement(query);
                ps.setString(1, nombre);

                ps.executeUpdate();
                System.out.println("Categoria registrada");
                agregado = true;
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println(" \nError: el numero que desea resgistrar ya se encuentra registrado " + e);
            } catch (SQLException ex) {
                System.out.println(ex + "la categoria no se puedo agregar");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return agregado;
    }

    public static List<Categoria> listarCategoria() {
        List<Categoria> listaCategoria = new ArrayList<>();

        Conexion db_connect = new Conexion();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection conexion = db_connect.get_connection()) {
            String query = "SELECT * FROM categoria";
            ps = conexion.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Categoria c = new Categoria(rs.getString("nombre"));
                c.setId_C(rs.getInt("idCategoria"));

                listaCategoria.add(c);

            }
        } catch (SQLException ex) {

            System.out.println("no se pudo traer la informacion " + ex);
        }
        return listaCategoria;
    }

    public static boolean editarCategoria(int id, String nombre) {
        Conexion db_connect = new Conexion();
        boolean actualizado = false;

        try (Connection conexion = db_connect.get_connection()) {
            PreparedStatement ps = null;
            try {
                String query = "UPDATE categoria SET nombre = ? WHERE idCategoria = ?";
                ps = conexion.prepareStatement(query);
                ps.setString(1, nombre);
                ps.setInt(2, id);

                int filaActualizada = ps.executeUpdate();
                if (filaActualizada > 0) {
                    System.out.println("Tutorial actualizado correctamente.");
                    actualizado = true;
                } else {
                    System.out.println("No se encontró el tutorial con el ID especificado.");
                }
            } catch (SQLException ex) {
                System.out.println("Error al editar el tutorial: " + ex);
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e);
        }

        return actualizado;
    }

    public static boolean eliminarCategoria(int id_C) {
        Conexion db_connect = new Conexion();
        boolean eliminado=false;
        try (Connection conexion = db_connect.get_connection()) {
            PreparedStatement ps = null;
            try {
                String query = "DELETE FROM categoria WHERE idCategoria= ?";
                ps = conexion.prepareStatement(query);
                ps.setInt(1, id_C);
                ps.executeUpdate();
                eliminado = true;
                System.out.println("La categoria ha sido borrada");
            } catch (Exception e) {
                System.out.println("No se pudo eliminar la categoria "+ e);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        
        return eliminado;
    }

    public static int darIdCategoria(String nombreC) {
        for (Categoria c : listarCategoria()) {
            if (c.getNombre().equalsIgnoreCase(nombreC)) {
                return c.getId_C();
            }
        }
        return 0;
    }

    public static String darNombreCategoria(int id) {
        for (Categoria c : listarCategoria()) {
            if (c.getId_C() == id) {
                return c.getNombre();
            }
        }
        return null;
    }
    
    public static boolean verExistencia(String nombre){
        boolean existencia = false;
        for (Categoria categoria : listarCategoria()) {
            if(categoria.nombre.equalsIgnoreCase(nombre)){
                existencia = true;
            }
        }
        
        return existencia;
    }
}
