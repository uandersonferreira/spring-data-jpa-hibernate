### **Coluna SERIAL ou IDENTITY do PostgreSQL vs. Gerador de IDENTITY do Hibernate**

> fonte: https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/

**1. SERIAL e BIGSERIAL (PostgreSQL):**

- **Descrição:** São colunas autoincrementadas no PostgreSQL, semelhantes ao `AUTO_INCREMENT` no MySQL.
- **Funcionamento:** Quando você define uma coluna como `SERIAL` ou `BIGSERIAL`, o PostgreSQL cria uma sequência interna
  para gerar valores automaticamente para essa coluna.
- **Problema com JPA/Hibernate:** Quando você usa `GenerationType.IDENTITY` em Hibernate, que é a estratégia
  correspondente para colunas `SERIAL`, o Hibernate precisa executar a instrução `INSERT` imediatamente para obter o ID
  gerado. Isso impede que o Hibernate agrupe as operações de inserção em lote, o que pode impactar negativamente a
  performance, especialmente em cenários de alta carga.

**2. IDENTITY (PostgreSQL 10+):**

- **Descrição:** Introduzido no PostgreSQL 10, o tipo `IDENTITY` substitui `SERIAL` e se comporta de maneira similar,
  mas está mais alinhado com os padrões SQL.
- **Funcionamento:** Funciona de forma semelhante ao `SERIAL`, utilizando uma sequência para gerar IDs automaticamente.
- **Problema com JPA/Hibernate:** Assim como `SERIAL`, o uso de `IDENTITY` com `GenerationType.IDENTITY` em Hibernate
  sofre dos mesmos problemas de desabilitação de processamento em lote.

**3. Gerador de SEQUENCE (Hibernate):**

- **Descrição:** O `SEQUENCE` é uma estratégia mais eficiente para gerar IDs únicos, que pode ser usada com Hibernate.
- **Funcionamento:** O Hibernate pode gerar IDs antecipadamente chamando uma sequência de banco de dados antes de
  realizar as operações `INSERT`. Isso permite que o Hibernate agrupe operações de inserção em lote, melhorando a
  performance.
- **Vantagens:** A principal vantagem do uso de `SEQUENCE` é a capacidade de agrupar inserções em lote, o que é crucial
  para a performance em aplicações que realizam muitas operações de inserção.

### **Conclusão:**

Embora `SERIAL`, `BIGSERIAL`, e `IDENTITY` sejam convenientes para auto-incremento, eles não são a melhor escolha ao
usar JPA/Hibernate devido à sua incompatibilidade com processamento em lote. A estratégia `SEQUENCE`, por outro lado,
permite que o Hibernate gere identificadores antes da execução das inserções, facilitando o agrupamento em lote e,
consequentemente, melhorando a performance. Por isso, ao trabalhar com JPA/Hibernate, é recomendado usar o gerador
`SEQUENCE` ao invés de `SERIAL`, `BIGSERIAL` ou `IDENTITY`.