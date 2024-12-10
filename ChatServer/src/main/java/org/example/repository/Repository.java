package org.example.repository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;

import static org.hibernate.cfg.AvailableSettings.*;

public abstract class Repository<T, K> {
    protected EntityManagerFactory emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(archiverPersistenceUnitInfo(), config());

    private static final String PERSISTENCE_UNIT_NAME = "org.example";

    protected abstract T save(T obj);

    protected abstract Optional<T> findById(K key);

    protected abstract void delete(T obj);

    public void close() {
        emf.close();
    }

    private Map<String, Object> config() {
        Map<String, Object> map = new HashMap<>();
        map.put(JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        map.put(JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:5432/chatService");
        map.put(DEFAULT_SCHEMA, "public");
        map.put(JAKARTA_JDBC_USER, "root");
        map.put(JAKARTA_JDBC_PASSWORD, "root");
        map.put(JAKARTA_HBM2DDL_DATABASE_ACTION, "drop-and-create");
        map.put(SHOW_SQL, "true");
        map.put(QUERY_STARTUP_CHECKING, "false");
        map.put(GENERATE_STATISTICS, "false");
        map.put(USE_SECOND_LEVEL_CACHE, "false");
        map.put(USE_QUERY_CACHE, "false");
        map.put(USE_STRUCTURED_CACHE, "false");
        map.put(STATEMENT_BATCH_SIZE, "20");
        map.put(AUTOCOMMIT, "false");
        map.put("hibernate.hikari.minimumIdle", "5");
        map.put("hibernate.hikari.maximumPoolSize", "15");
        map.put("hibernate.hikari.idleTimeout", "30000");
        return map;
    }

    private static PersistenceUnitInfo archiverPersistenceUnitInfo() {
        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return PERSISTENCE_UNIT_NAME;
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "com.zaxxer.hikari.hibernate.HikariConnectionProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return null;
            }

            @Override
            public List<String> getMappingFileNames() {
                return Collections.emptyList();
            }

            @Override
            public List<java.net.URL> getJarFileUrls() {
                try {
                    return Collections.list(this.getClass()
                            .getClassLoader()
                            .getResources(""));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return null;
            }

            @Override
            public List<String> getManagedClassNames() {
                return Collections.emptyList();
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return new Properties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) { }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };
    }
}