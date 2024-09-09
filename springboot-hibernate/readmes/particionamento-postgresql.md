# Particionamento de tabelas no PostgreSQL

O particionamento por **rango** (ou intervalo) é um tipo de particionamento em que os dados são divididos com base em um
intervalo contínuo de valores em uma coluna específica. Cada partição contém um subconjunto de dados cujo valor na
coluna de particionamento se encontra dentro de um determinado intervalo.

### O que é o particionamento por rango?

No exemplo da aula, a tabela `employees` está particionada pela coluna `birth_date` utilizando o tipo de
particionamento por intervalo de datas (range). Assim, você pode criar várias partições da tabela principal, onde cada
partição armazenará apenas os dados cujas datas de nascimento se enquadrem em um intervalo específico.

- **Exemplo**: Para o ano de 2019, criamos uma partição que conterá todos os registros de funcionários cujo `birth_date`
  seja entre 1º de janeiro de 2019 e 31 de dezembro de 2019.

### Por que usamos chaves primárias compostas?

Ao criar uma tabela particionada, como a `employees`, onde a coluna usada para particionamento faz parte da chave
primária, a chave primária precisa incluir essa coluna. Isso é necessário para garantir a unicidade dos dados em todas
as partições, já que cada partição representa um subconjunto dos dados da tabela principal.

- **Exemplo**: Na tabela `employees`, o campo `id` pode ser único dentro de uma partição, mas pode haver registros em
  outras partições com o mesmo `id`. Por isso, para garantir unicidade em todas as partições, a chave primária é
  composta por `id` e `birth_date`.

### Por que o intervalo de 2019 não vai até 31 de dezembro de 2019?

No PostgreSQL, o particionamento por range define o intervalo de dados de forma que o valor final do intervalo (neste
caso, `2020-01-01`) **não está incluído** na partição. Portanto, para garantir que todas as datas de 2019 estejam na
partição, o intervalo deve ser definido como:

```sql
FOR VALUES FROM ('2019-01-01') TO ('2020-01-01');
```

Isso inclui as datas de `2019-01-01` a `2019-12-31`, pois o valor inicial é inclusivo (`FROM`), e o valor final é
exclusivo (`TO`). Se fosse definido até `'2019-12-31'`, a data de `2019-12-31` ficaria fora do intervalo e causaria
problemas.

---

### Particionamento por Lista (BY LIST)

#### O que é o particionamento por lista?

O particionamento por lista divide os dados com base em valores específicos que são definidos como uma lista discreta. É
ideal para situações onde você tem um conjunto fixo de categorias ou grupos, e cada grupo pode ser representado por uma
lista de valores.

**Como funciona:**

- **Definição**: Cada partição é configurada para armazenar linhas que têm valores específicos ou um conjunto de valores
  na coluna de particionamento.
- **Uso**: Ideal para dados categóricos fixos, como códigos de país, tipos de produtos ou categorias de clientes.

**Exemplo de criação e uso:**

```sql
DROP TABLE IF EXISTS employees;

CREATE TABLE employees
(
    id            BIGSERIAL,
    register_date DATE        NOT NULL,
    first_name    VARCHAR(20) NOT NULL,
    country_code  VARCHAR(2)  NOT NULL,
    PRIMARY KEY (id, country_code)
) PARTITION BY LIST (country_code);

CREATE TABLE employees_north PARTITION OF employees
    FOR VALUES IN
(
    'EE',
    'FI'
);

CREATE TABLE employees_south PARTITION OF employees
    FOR VALUES IN
(
    'ES',
    'FR',
    'DE'
);

INSERT INTO employees (register_date, first_name, country_code)
VALUES ('2019-03-01', 'Employee 1', 'EE'),
       ('2019-04-01', 'Employee 2', 'FI'),
       ('2019-04-01', 'Employee 3', 'ES'),
       ('2019-04-01', 'Employee 4', 'FR'),
       ('2019-04-01', 'Employee 5', 'DE');
```

**Consultas e EXPLAIN:**

- **Selecionar dados**: `SELECT * FROM employees WHERE country_code = 'ES';`
- **Analisar plano de execução**: `EXPLAIN ANALYZE SELECT * FROM employees WHERE country_code = 'ES';`

**Notas:**

- Cada partição é definida para armazenar valores específicos da coluna de particionamento.
- Consultas para valores não listados em nenhuma partição resultarão em erro, indicando que a partição correspondente
  não existe.

---

### Particionamento por Hash (BY HASH)

#### O que é o particionamento por hash?

O particionamento por hash distribui os dados entre várias partições com base no valor de hash de uma coluna. Esse
método é útil para balancear a carga e otimizar o desempenho quando você deseja uma distribuição uniforme dos dados.

**Como funciona:**

- **Definição**: O PostgreSQL calcula um hash do valor da coluna de particionamento e usa o módulo (MODULUS) e o resto (
  REMAINDER) para distribuir os dados entre as partições.
- **Uso**: Ideal para grandes volumes de dados onde a distribuição uniforme ajuda a evitar hotspots e melhorar o
  desempenho.

#### Conceitos de MODULUS e REMAINDER

1. **Módulo (MODULUS)**: O número total de partições em que os dados serão distribuídos. Define o número de buckets ou
   grupos. Por exemplo, se há 4 partições, o módulo é 4.

2. **Resto (REMAINDER)**: O índice da partição específica para uma linha, calculado como `hash_value % MODULUS`, onde
   `hash_value` é o valor hash da coluna de particionamento. O resto determina em qual partição os dados serão
   armazenados.

**Exemplo de criação e uso:**

```sql
DROP TABLE IF EXISTS dept;

CREATE TABLE dept
(
    id INT PRIMARY KEY
) PARTITION BY HASH (id);

CREATE TABLE dept_hash1 PARTITION OF dept
    FOR VALUES WITH (MODULUS 4, REMAINDER 0);

CREATE TABLE dept_hash2 PARTITION OF dept
    FOR VALUES WITH (MODULUS 4, REMAINDER 1);

CREATE TABLE dept_hash3 PARTITION OF dept
    FOR VALUES WITH (MODULUS 4, REMAINDER 2);

CREATE TABLE dept_hash4 PARTITION OF dept
    FOR VALUES WITH (MODULUS 4, REMAINDER 3);

INSERT INTO dept (SELECT generate_series(0, 200000));
```

**Consultas e EXPLAIN:**

- **Selecionar dados**: `SELECT * FROM dept WHERE id = 47;`
- **Analisar plano de execução**: `EXPLAIN ANALYZE SELECT * FROM dept WHERE id = 47;`

**Notas:**

- O particionamento por hash não é adequado para consultas que se baseiam em intervalos de valores.
- Ele é eficaz para garantir uma distribuição uniforme dos dados, evitando que uma partição receba um volume
  desproporcional de dados.

---


