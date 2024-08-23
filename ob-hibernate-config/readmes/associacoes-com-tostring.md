## Aviso sobre o uso de associações no método toString():
Não é recomendável imprimir o atributo de uma associação no método `toString()` devido a vários motivos, especialmente quando se trata de associações bidirecionais e coleções de entidades. Vamos analisar os principais motivos:

### 1. Ciclos Infinitos

Quando você tem associações bidirecionais, como um relacionamento `OneToOne` ou `OneToMany` entre duas entidades, incluir o atributo da associação no método `toString()` pode causar ciclos infinitos.

Por exemplo:

```java
public class Employee {
    @OneToOne
    private Direction direction;

    @Override
    public String toString() {
        return "Employee{" +
                "direction=" + direction +
                '}';
    }
}

public class Direction {
    @OneToOne(mappedBy = "direction")
    private Employee employee;

    @Override
    public String toString() {
        return "Direction{" +
                "employee=" + employee +
                '}';
    }
}
```

Aqui, chamar `toString()` em `Employee` chamará `toString()` em `Direction`, que por sua vez chamará `toString()` em `Employee` novamente, criando um ciclo infinito que resultará em uma `StackOverflowError`.

### 2. Performance

As associações podem trazer muitas entidades relacionadas, o que pode resultar em um grande volume de dados sendo carregado e impresso. Isso pode causar problemas de desempenho, especialmente se as associações envolvem coleções grandes.

### 3. Lazy Loading

Muitas associações em JPA são configuradas para carregamento tardio (lazy loading). Incluir essas associações no método `toString()` pode resultar em consultas desnecessárias ao banco de dados quando o método `toString()` é chamado, o que pode impactar negativamente o desempenho da aplicação.

### Recomendações

Para evitar esses problemas, você pode adotar as seguintes práticas:

1. **Não incluir associações no `toString()`**: Apenas inclua os atributos básicos e identificadores únicos que não causam ciclos.

2. **Usar `@ToString.Exclude` (Lombok)**: Se você estiver usando Lombok, pode excluir associações do método `toString()` gerado.

3. **Métodos de debug separados**: Crie métodos de debug separados que lidam cuidadosamente com associações e evite ciclos infinitos.

### Exemplo Corrigido (com uso do Lombok)

Aqui está um exemplo corrigido que evita ciclos infinitos:

```java
import lombok.ToString;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToOne
    @ToString.Exclude // Exclui a associação do método toString gerado pelo Lombok
    private Direction direction;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

@Entity
public class Direction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;

    @OneToOne(mappedBy = "direction")
    @ToString.Exclude // Exclui a associação do método toString gerado pelo Lombok
    private Employee employee;

    @Override
    public String toString() {
        return "Direction{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
```

Neste exemplo, as associações `direction` e `employee` são excluídas do método `toString()` para evitar ciclos e problemas de desempenho.


### Observação 2 :

Se você chamar um atributo de uma entidade relacionada dentro do método `toString()`, ainda pode enfrentar alguns problemas, principalmente relacionados ao desempenho e ao carregamento de dados. Embora isso não cause um ciclo infinito por si só, pode levar a consultas adicionais ao banco de dados se o atributo for carregado tardiamente (lazy loading).

### Exemplo e Problemas Potenciais

Suponha que você tenha a seguinte estrutura:

```java
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    private Direction direction;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", directionCity='" + (direction != null ? direction.getCity() : "N/A") + '\'' +
                '}';
    }
}

@Entity
public class Direction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    @OneToOne(mappedBy = "direction")
    private Employee employee;

    @Override
    public String toString() {
        return "Direction{" +
                "id=" + id +
                ", city='" + city + '\'' +
                '}';
    }
}
```

### Problemas Potenciais

1. **Lazy Loading**: Se `direction` for carregado tardiamente, acessar `direction.getCity()` no método `toString()` de `Employee` resultará em uma consulta ao banco de dados para carregar a entidade `Direction`. Isso pode ser inesperado e pode impactar o desempenho.

2. **Desempenho**: Chamadas repetidas ao método `toString()` que acessam entidades relacionadas podem resultar em muitas consultas ao banco de dados, especialmente em listas ou coleções de entidades.

### Recomendações

1. **Evitar Acessos a Entidades Relacionadas no `toString()`**: É melhor evitar acessar atributos de entidades relacionadas diretamente no método `toString()`. Isso evita carregamentos indesejados e problemas de desempenho.

2. **Carregar Dados Antecipadamente**: Se precisar dos dados relacionados, considere carregá-los antecipadamente em uma consulta separada, garantindo que estejam disponíveis sem causar carregamentos tardios.



