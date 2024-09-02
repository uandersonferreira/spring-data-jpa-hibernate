# Interceptor do Hibernate

O Hibernate oferece a interface `org.hibernate.Interceptor`, que permite à aplicação interceptar e manipular eventos durante o ciclo de vida de uma entidade. Através dos métodos de callback fornecidos por essa interface, é possível inspecionar ou alterar as propriedades de um objeto persistente antes de ele ser salvo, atualizado, deletado ou carregado.

#### Tipos de Interceptores

Existem dois tipos principais de interceptores no Hibernate:

1. **Session-scoped Interceptor**:
    - Associado a uma instância específica de `Session`.
    - É especificado no momento da abertura da `Session`.
    - Ideal para interceptações que precisam ser aplicadas a uma única sessão de interação com o banco de dados.

   Exemplo:
   ```java
   SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
   Session session = sessionFactory
       .withOptions()
       .interceptor(new LoggingInterceptor())
       .openSession();

   session.getTransaction().begin();

   Customer customer = session.get(Customer.class, customerId);
   customer.setName("Mr. John Doe");

   session.getTransaction().commit();
   ```

2. **SessionFactory-scoped Interceptor**:
    - Registrado no nível do `SessionFactory`.
    - Aplicado automaticamente a todas as sessões abertas a partir daquele `SessionFactory`, exceto se uma sessão for aberta explicitamente com um interceptor diferente.
    - Deve ser thread-safe, já que pode ser usado por múltiplas sessões simultaneamente.
    - Útil para aplicar interceptações de forma abrangente em toda a aplicação.

   Exemplo:
   ```java
   SessionFactory sessionFactory = new MetadataSources(new StandardServiceRegistryBuilder().build())
       .addAnnotatedClass(Customer.class)
       .getMetadataBuilder()
       .build()
       .getSessionFactoryBuilder()
       .applyInterceptor(new LoggingInterceptor())
       .build();
   ```

#### Implementação do Interceptor

Um exemplo comum de uso do interceptor é para fins de auditoria, como registrar automaticamente as mudanças em entidades. A seguir, é mostrado um exemplo de implementação de um `Interceptor` que loga as mudanças realizadas em uma entidade:

```java
import org.hibernate.Interceptor;
import org.hibernate.type.Type;
import java.util.Arrays;

public class LoggingInterceptor implements Interceptor {

    @Override
    public boolean onFlushDirty(
        Object entity,
        Object id,
        Object[] currentState,
        Object[] previousState,
        String[] propertyNames,
        Type[] types) {
        
        // Loga as mudanças realizadas na entidade
        System.out.println(String.format(
            "Entity %s#%s changed from %s to %s",
            entity.getClass().getSimpleName(),
            id,
            Arrays.toString(previousState),
            Arrays.toString(currentState)
        ));

        return false; // Retorna false indicando que o estado não foi modificado
    }

    // Outros métodos de callback podem ser implementados aqui conforme a necessidade
}
```

#### Considerações Finais

- **Session-scoped**: É ideal para tarefas específicas de uma sessão, como adicionar um comportamento temporário durante uma transação.

- **SessionFactory-scoped**: Deve ser usado quando a interceptação precisa ser aplicada globalmente em todas as sessões criadas a partir de um `SessionFactory`.

O uso do `Interceptor` permite uma grande flexibilidade e controle sobre como as entidades são gerenciadas e persistidas, facilitando a implementação de funcionalidades como auditoria, controle de acesso, ou qualquer outro comportamento personalizado que precise ser integrado ao ciclo de vida das entidades no Hibernate.

---

