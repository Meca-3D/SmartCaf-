package com.smartcaf.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionChecker implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionChecker.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static boolean databaseConnected = false;

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.execute("SELECT 1");
            databaseConnected = true;
            logger.info("Connexion à la base de données réussie.");
        } catch (Exception e) {
            databaseConnected = false;
            logger.error("Échec de la connexion à la base de données : " + e.getMessage());
        }
    }
}
