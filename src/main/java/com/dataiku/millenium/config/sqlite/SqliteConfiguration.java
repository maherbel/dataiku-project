package com.dataiku.millenium.config.sqlite;

import com.dataiku.millenium.exceptions.DataSourceFormatNotSupported;
import com.dataiku.millenium.pojos.MilleniumFalconModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * Configuration class for setting up a SQLite database for the application.
 * This class is responsible for creating a {@link DataSource} bean that is used to create the
 * {@link LocalContainerEntityManagerFactoryBean} bean for JPA. It also handles the creation of a temporary
 * file if the routes database file is located within a JAR file.
 */
@Configuration
@EntityScan(basePackages = "com.dataiku.millenium.entities")
@EnableJpaRepositories(basePackages = "com.dataiku.millenium.repositories")
public class SqliteConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SqliteConfiguration.class);

    private static final String JDBC_SQLITE = "jdbc:sqlite:";
    final
    Environment env;

    private File tempFile;


    final
    MilleniumFalconModel milleniumFalconModel;

    public SqliteConfiguration(Environment env, MilleniumFalconModel milleniumFalconModel) {
        this.env = env;
        this.milleniumFalconModel = milleniumFalconModel;
    }

    /**
     * Bean definition for the {@link DataSource} object for the SQLite database.
     * This method sets up the driver class name, URL, username, and password for the data source.
     * If the routes database file is located within a JAR file, it creates a temporary file and sets the
     * URL to point to the temporary file.
     *
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @Bean
    @DependsOn("milleniumFalconModelBean")
    public DataSource dataSource() throws IOException, URISyntaxException, DataSourceFormatNotSupported {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("driverClassName")));
        String routesDatabasePath = milleniumFalconModel.getRoutesDB();
        // the database file's URL
        URL routesDBFileUrl;
        if (Paths.get(routesDatabasePath).isAbsolute()) {
            File dbFile = new File(routesDatabasePath);
            routesDBFileUrl = dbFile.toURI().toURL();
            logger.info("Absolute path to routes db: {}", routesDBFileUrl);
        } else {
            Resource resource = new ClassPathResource(routesDatabasePath);
            routesDBFileUrl = resource.getURL();
            logger.info("Relative path to routes db: {}", routesDBFileUrl);
        }

        if (Objects.equals(routesDBFileUrl.getProtocol(), "file")) {
            String jdbcUrl = JDBC_SQLITE + routesDBFileUrl.toURI();
            dataSource.setUrl(jdbcUrl);
            logger.info("JDBC URL: {}", jdbcUrl);
        } else if (Objects.equals(routesDBFileUrl.getProtocol(), "jar")) {
            computeDataSourceUrl();
            String jdbcUrl = JDBC_SQLITE + this.tempFile.getAbsolutePath();
            dataSource.setUrl(jdbcUrl);
            logger.info("JDBC URL: {}", jdbcUrl);
        } else {
            throw new DataSourceFormatNotSupported("The protocol " + routesDBFileUrl.getProtocol() +
                    " is not supported for the file at URL " + routesDBFileUrl);
        }
        dataSource.setUsername(env.getProperty("username"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }

    /**
     * Helper method that creates a temporary file and copies the contents of the routes database file
     * located within the JAR file to the temporary file. This is done because SQLite does not support
     * accessing databases stored within JAR files.
     *
     * @throws IOException
     */
    private void computeDataSourceUrl() throws IOException {
        cleanupTempFile();
        this.tempFile = File.createTempFile("tempDB", ".db");
        logger.info("Temp file {} has been created.", this.tempFile.getName());
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(milleniumFalconModel.getRoutesDB());
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }

    /**
     * Helper method that deletes the temporary file created by the {@link #computeDataSourceUrl()} method
     * if it exists. This method is called during bean destruction to clean up any temporary files created
     * by the application.
     */
    private void cleanupTempFile() {
        if (this.tempFile != null && this.tempFile.exists()) {
            String tempFileName = this.tempFile.getName();
            this.tempFile.delete();
            logger.info("Temp file {} deleted: ", tempFile.getAbsolutePath());
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws IOException, URISyntaxException, DataSourceFormatNotSupported {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.dataiku.millenium.entities");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        if (env.getProperty("hibernate.hbm2ddl.auto") != null) {
            hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        }
        if (env.getProperty("hibernate.dialect") != null) {
            hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        }
        return hibernateProperties;
    }

    @Configuration
    @PropertySource("classpath:persistence-sqlite.properties")
    static class SqliteConfig {
    }

    @PreDestroy
    public void onDestroy() {
        cleanupTempFile();
    }
}
