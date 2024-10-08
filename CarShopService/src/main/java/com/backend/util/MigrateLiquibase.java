package com.backend.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MigrateLiquibase {

    private static final String PATH_TO_LIQUIBASE_PROPERTIES = "properties/liquibase.properties";

    public void migrate () {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PATH_TO_LIQUIBASE_PROPERTIES)) {

            properties.load(input);


            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String driver = properties.getProperty("driver");
            String changeLogFile = properties.getProperty("changeLogFile");


            Class.forName(driver);


            try (Connection connection = DriverManager.getConnection(url, username, password)) {

                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();
                Liquibase liquibase = new Liquibase(changeLogFile, resourceAccessor, database);


                liquibase.update("");

                System.out.println("Liquibase update executed successfully.");
            } catch (SQLException | LiquibaseException e) {
                e.printStackTrace();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
