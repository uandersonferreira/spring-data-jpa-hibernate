### Resumo de Quando Usar Cada JOIN (com Exemplos):

> Exemlo gerado com IA, consultar documentação oficial
https://docs.jboss.org/hibernate/stable/orm/userguide/html_single/Hibernate_User_Guide.html#hql-explicit-fetch-join


1. **INNER JOIN**:
    - **Uso**: Use quando você quer registros que tenham correspondência em ambas as tabelas.
    - **Exemplo**: Buscar todos os empregados que têm um carro.
   ```hql
   SELECT e.*, c.*
   FROM Employee e
   INNER JOIN Cars c ON e.id = c.employee_id;
   ```

2. **LEFT JOIN (ou LEFT OUTER JOIN)**:
    - **Uso**: Use quando você quer todos os registros da tabela à esquerda, mesmo que não haja correspondência na
      tabela à direita.
    - **Exemplo**: Listar todos os empregados, mesmo aqueles que não têm um carro.
   ```hql
   SELECT e.*, c.*
   FROM Employee e
   LEFT JOIN Cars c ON e.id = c.employee_id;
   ```

3. **RIGHT JOIN (ou RIGHT OUTER JOIN)**:
    - **Uso**: Use quando você quer todos os registros da tabela à direita, mesmo que não haja correspondência na tabela
      à esquerda.
    - **Exemplo**: Listar todos os carros, mesmo que não tenham um empregado associado.
   ```hql
   SELECT e.*, c.*
   FROM Employee e
   RIGHT JOIN Cars c ON e.id = c.employee_id;
   ```

4. **FULL OUTER JOIN**:
    - **Uso**: Use quando você quer todos os registros de ambas as tabelas, independentemente de haver correspondência.
    - **Exemplo**: Listar todos os empregados e todos os carros, mesmo que alguns empregados não tenham carros e alguns
      carros não estejam associados a um empregado.
   ```hql
   SELECT e.*, c.*
   FROM Employee e
   FULL OUTER JOIN Cars c ON e.id = c.employee_id;
   ```

5. **CROSS JOIN**:
    - **Uso**: Use quando quiser combinar todas as linhas de uma tabela com todas as linhas de outra (produto
      cartesiano).
    - **Exemplo**: Combinar todos os empregados com todos os carros, independentemente de qualquer relacionamento.
   ```hql
   SELECT e.*, c.*
   FROM Employee e
   CROSS JOIN Cars c;
   ```

6. **NATURAL JOIN**:
    - **Uso**: Use quando as colunas de junção têm o mesmo nome e você quer simplificar o código (cuidado com o uso
      inadvertido).
    - **Exemplo**: Juntar `Employee` e `Cars`, assumindo que ambas as tabelas têm uma coluna com o mesmo nome.
   ```hql
   SELECT e.*, c.*
   FROM Employee e
   NATURAL JOIN Cars c;
   ```

7. **SELF JOIN**:
    - **Uso**: Use quando precisar juntar uma tabela a ela mesma para comparar ou relacionar registros internos.
    - **Exemplo**: Comparar empregados com seus gerentes, onde ambos os dados estão na tabela `Employee`.
   ```hql
   SELECT e1.*, e2.*
   FROM Employee e1
   INNER JOIN Employee e2 ON e1.manager_id = e2.id;
   ```

#### ============================================================================

```hql
select distinct e 
from Employee e 
join fetch e.cars 
where e.id = :pk

```

O uso do `fetch` no `JOIN` na consulta HQL (`join fetch`) tem um propósito diferente dos outros `JOINs` convencionais
mostrados anteriormente. Ele é usado para otimizar a recuperação de dados e evitar o problema de *N+1 queries*. Vou
explicar por que o `join fetch` foi usado e como ele difere dos outros tipos de `JOINs`.

### Diferença entre `JOIN` e `JOIN FETCH`:

- **`JOIN`**:  
  Usado para unir entidades relacionadas em uma consulta, mas o relacionamento associado (`e.cars`, por exemplo) **não
  será carregado imediatamente** na memória (ou seja, será carregado como *lazy* - carregamento preguiçoso - por padrão,
  dependendo da configuração da entidade).

- **`JOIN FETCH`**:  
  Usado para unir entidades relacionadas **e garantir que as coleções ou entidades associadas sejam carregadas
  imediatamente** (eager fetching). Em outras palavras, quando você usa `join fetch`, você está dizendo ao Hibernate
  para buscar os dados da entidade `Employee` e ao mesmo tempo carregar todas as entidades relacionadas `cars` de uma só
  vez, numa única consulta SQL.

### Por que `join fetch` foi usado no exemplo:

```hql
select distinct e from Employee e 
join fetch e.cars 
where e.id = :pk
```

1. **Evitar N+1 queries**:  
   Se você usasse um `JOIN` normal, como `INNER JOIN` ou `LEFT JOIN`, o Hibernate poderia carregar a entidade `Employee`
   primeiro, e então fazer outra consulta SQL para buscar as entidades `Cars` relacionadas para cada `Employee`. Esse
   comportamento é conhecido como problema de *N+1 queries*, onde você acaba fazendo uma consulta para a entidade
   principal (a primeira), e depois N consultas adicionais para carregar as entidades associadas. Usar `fetch` força o
   carregamento de todas as associações em uma única consulta, evitando múltiplas chamadas ao banco de dados.

2. **Carregamento imediato das coleções associadas**:  
   O `join fetch` é especialmente útil quando você sabe que vai precisar dos dados relacionados de forma imediata. Neste
   caso, você precisa dos carros (`cars`) associados ao empregado (`Employee`) com o `id` especificado (`e.id = :pk`).
   O `fetch` instrui o Hibernate a carregar os carros associados em uma única query SQL, o que melhora o desempenho
   quando o carregamento é necessário.

3. **Consulta otimizada**:  
   Ao usar `fetch`, o Hibernate faz um `JOIN` no banco de dados, combinando as tabelas de `Employee` e `Cars`,
   retornando os resultados de ambas as entidades numa única operação. Isso elimina a necessidade de carregar as
   coleções `cars` em um momento posterior, otimizando a execução da consulta.

### Como seria o comportamento sem o `fetch`:

- **Sem `fetch`**:
   ```hql
   select e from Employee e 
   inner join e.cars 
   where e.id = :pk
   ```
  Nesse caso, o Hibernate faria duas operações:
    1. Carregaria os dados do `Employee`.
    2. Posteriormente, caso você acesse `e.cars`, ele executaria uma segunda consulta ao banco de dados para buscar os
       carros, a menos que a associação fosse configurada como *eager* diretamente no mapeamento.

### Quando usar `join fetch`:

- Use `join fetch` quando você quer garantir que as entidades associadas sejam carregadas imediatamente em conjunto com
  a entidade principal, e você sabe que vai precisar desses dados no momento da consulta.
- Ele é particularmente útil em cenários onde o carregamento preguiçoso (*lazy loading*) pode gerar múltiplas consultas
  ao banco de dados, levando a problemas de desempenho.

### Exemplo Real:

Se você tem um relacionamento `OneToMany` entre `Employee` e `Cars`, e cada `Employee` pode ter vários `Cars`, usar
o `join fetch` permite que você recupere o `Employee` e todos os `Cars` de uma só vez, evitando o carregamento
preguiçoso que, de outra forma, geraria várias consultas adicionais:

```hql
select distinct e from Employee e 
join fetch e.cars 
where e.id = :pk
```

```hql
    select
        distinct e1_0.id,
        e1_0.age,
        e1_0.birth_date,
        c1_0.employee_id,
        c1_1.id,
        c1_1.cc,
        c1_1.manufacturer,
        c1_1.release_year,
        e1_0.category,
        e1_0.direction_pk,
        e1_0.email,
        e1_0.first_name,
        e1_0.last_name,
        e1_0.married,
        e1_0.register_date,
        e1_0.salary 
    from
        ob_employees e1_0 
    join
        ob_employee_cars c1_0 
            on e1_0.id=c1_0.employee_id 
    join
        ob_cars c1_1 
            on c1_1.id=c1_0.car_id 
    where
        e1_0.id=?
```

Isso retorna o `Employee` e todos os seus `Cars` numa única query, o que é muito mais eficiente em termos de tempo de
resposta.