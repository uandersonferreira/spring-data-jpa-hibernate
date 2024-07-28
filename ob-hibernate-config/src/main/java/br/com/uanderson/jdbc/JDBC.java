package br.com.uanderson.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistência:
 * <p>
 * 1. Java DataBase Connectivity (JDBC)
 * Conexão e manipulação direta do banco de dados usando SQL.
 * <p>
 * 2. JPA (Java Persistence API) Especificação
 * Implementações:
 * - Hibernate (ORM Object Relational Mapping)
 * - EclipseLink
 * Facilita o mapeamento objeto-relacional usando anotações.
 * <p>
 * 3. Spring Data JPA
 * Integração do Spring com JPA, simplificando as operações de persistência.
 * <p>
 * CRUD - Create Retrieve Update Delete
 * Operações básicas de criação, leitura, atualização e exclusão.
 */
public class JDBC {

    // Query SQL para selecionar todas as pessoas
    private static final String SQL_QUERY = "SELECT * FROM taller.persona";
    private static final String URL_TALLER = ""; // URL do banco de dados
    private static final String USER = ""; // Usuário do banco de dados
    private static final String PASSWORD = ""; // Senha do banco de dados

    public static void main(String[] args) {

        Connection conexion = null; // Conexão com o banco de dados
        Statement statement = null; // Declaração SQL
        ResultSet resultSet = null; // Resultado da consulta SQL

        try {

            // 1. Criar conexão
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/db-ob-hibernate", // URL do banco de dados
                    "root", "root"); // Usuário e senha

            // 2. Criar declaração SQL
            statement = conexion.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT * FROM coches;"); // Executa a consulta SQL

            // 3. Processar resultados
            List<Coche> cars = new ArrayList<>();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String modelo = resultSet.getString("modelo");
                String fabricante = resultSet.getString("fabricante");
                Integer numCilindros = resultSet.getInt("num_cilindros");
                Double numCV = resultSet.getDouble("num_cv");
                Coche coche = new Coche(
                        id, modelo, fabricante, numCilindros, numCV);
                cars.add(coche);
            }

            System.out.println(cars);

        } catch (SQLException exception) {
            exception.printStackTrace(); // Tratamento de exceção
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (statement != null) statement.close();
            } catch (Exception e) {
            }
            try {
                if (conexion != null) conexion.close();
            } catch (Exception e) {
            }
        }

    }
}
/*

-- Criação do DATABASE
CREATE DATABASE `db-ob-hibernate`;

-- Criação da tabela Coche
CREATE TABLE Coche
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    modelo        VARCHAR(255) NOT NULL,
    fabricante    VARCHAR(255) NOT NULL,
    num_cilindros INT          NOT NULL,
    num_cv        DOUBLE       NOT NULL
);

-- Inserção de 5 registros na tabela Coche

INSERT INTO coches (modelo, fabricante, num_cilindros, num_cv)
VALUES ('Modelo A', 'Fabricante X', 4, 150.0),
       ('Modelo B', 'Fabricante Y', 6, 250.0),
       ('Modelo C', 'Fabricante Z', 8, 350.0),
       ('Modelo D', 'Fabricante W', 4, 120.0),
       ('Modelo E', 'Fabricante V', 6, 200.0);


 */