package br.com.uanderson.util;

import br.com.uanderson.EmployeeInterceptor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * A classe HibernateUtil é uma utility class que facilita a
 * criação e gerenciamento da SessionFactory do Hibernate, que
 * é essencial para interagir com o banco de dados.
 *
 */
public class HibernateUtil {
    private static StandardServiceRegistry registry;//armazena as configurações e serviços do Hibernate.
    private static SessionFactory sessionFactory;//é um objeto imutável e compartilhável, utilizado para criar sessões do Hibernate

    public static SessionFactory getSessionFactory() {//Padrão Singleton
        if (sessionFactory == null){
            try {
                // Create registry default - usando as configurações especificadas no arquivo hibernate.cfg.xml.(então ele deve está criado ok?)
                registry = new StandardServiceRegistryBuilder().configure().build();

                // Create MetadataSources - a partir do registro criado, que contém as configurações do Hibernate
                MetadataSources sources = new MetadataSources(registry);

                // Create Metadata - que o Hibernate usa para saber como mapear as classes Java para as tabelas do banco de dados
                Metadata metadata = sources.getMetadataBuilder().build();

                // Create SessionFactory - Usa os metadados para construir a SessionFactory.
                sessionFactory = metadata.getSessionFactoryBuilder()
                        .applyInterceptor(new EmployeeInterceptor()) //Adicionando um interceptor a nossa session, para poder captar as operações realizadas
                        //.applyInterceptor(new EmployeeInterceptor()) //Podemos aplicar mais de um Interceptor
                        .build();

            } catch (Exception ex) {
                ex.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                    //Destroi o registro de serviços se ele foi criado, para liberar recursos.
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown(){
        if (registry != null){
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }


}//class
