package com.castores.inventario;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class InventarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventarioApplication.class, args);
	}

    @Bean
    CommandLineRunner testConnection(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("Conexión exitosa a la base de datos!");
                System.out.println("Base de datos: " + connection.getCatalog());
            } catch (Exception e) {
                System.err.println("Error al conectar: " + e.getMessage());
            }
        };
    }


}
