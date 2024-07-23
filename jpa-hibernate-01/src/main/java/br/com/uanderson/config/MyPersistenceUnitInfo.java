package br.com.uanderson.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

import javax.sql.DataSource;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class MyPersistenceUnitInfo implements PersistenceUnitInfo {

    // Nome da unidade de persistência definido no persistence.xml
    @Override
    public String getPersistenceUnitName() {
        return "my-persistence-unit";
    }

    // Nome da classe do provedor de persistência (Hibernate)
    @Override
    public String getPersistenceProviderClassName() {
        return "org.hibernate.jpa.HibernatePersistenceProvider";
    }

    // Tipo de transação da unidade de persistência (RESOURCE_LOCAL ou JTA)
    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    // Retorna o DataSource JTA (null aqui porque estamos usando RESOURCE_LOCAL)
    @Override
    public DataSource getJtaDataSource() {
        return null;
    }

    // Configura e retorna o DataSource para conexões não-JTA (local)
    @Override
    public DataSource getNonJtaDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/db_hibernate");
        hikariDataSource.setUsername("test");
        hikariDataSource.setPassword("test123");
        hikariDataSource.setDriverClassName("org.postgresql.Driver");

        return hikariDataSource;
    }

    // Retorna a lista de nomes de arquivos de mapeamento (não utilizado aqui)
    @Override
    public List<String> getMappingFileNames() {
        return null;
    }

    // Retorna a lista de URLs de arquivos JAR que contêm entidades (não utilizado aqui)
    @Override
    public List<URL> getJarFileUrls() {
        return null;
    }

    // Retorna a URL raiz da unidade de persistência (não utilizado aqui)
    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    // Retorna a lista de nomes de classes gerenciadas pela unidade de persistência
    @Override
    public List<String> getManagedClassNames() {
        return List.of(
                "br.com.uanderson.entities.Student",
                "br.com.uanderson.entities.City"
        );
    }

    // Define se classes não listadas devem ser excluídas (false para incluir todas as classes anotadas)
    @Override
    public boolean excludeUnlistedClasses() {
        return false;
    }

    // Retorna o modo de cache compartilhado (não configurado aqui)
    @Override
    public SharedCacheMode getSharedCacheMode() {
        return null;
    }

    // Retorna o modo de validação (não configurado aqui)
    @Override
    public ValidationMode getValidationMode() {
        return null;
    }

    // Retorna as propriedades específicas da unidade de persistência (não configuradas aqui)
    @Override
    public Properties getProperties() {
        return null;
    }

    // Retorna a versão do esquema XML de persistência (não configurada aqui)
    @Override
    public String getPersistenceXMLSchemaVersion() {
        return null;
    }

    // Retorna o ClassLoader a ser utilizado (não configurado aqui)
    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    // Adiciona um transformador de classe (não utilizado aqui)
    @Override
    public void addTransformer(ClassTransformer transformer) {
        // No implementation needed
    }

    // Retorna um novo ClassLoader temporário (não configurado aqui)
    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }
}
