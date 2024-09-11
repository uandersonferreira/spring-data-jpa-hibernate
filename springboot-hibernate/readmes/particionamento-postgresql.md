# Particionamento de Tabelas no PostgreSQL

O particionamento de tabelas no PostgreSQL permite que os dados sejam divididos em partes menores, chamadas partições,
para facilitar a manutenção e melhorar o desempenho em grandes volumes de dados. Existem diferentes métodos de
particionamento, como por **intervalo (rango)**, **lista**, e **hash**. Abaixo, detalhamos cada um deles.

## Particionamento por Intervalo (Range)

### O que é o particionamento por intervalo?

O particionamento por intervalo divide os dados com base em um intervalo contínuo de valores em uma coluna específica.
Cada partição armazena dados cujo valor da coluna particionada se encontra em um intervalo específico.

- **Exemplo**: Se particionarmos uma tabela de funcionários pela data de nascimento (`birth_date`), cada partição
  conterá dados de funcionários cujas datas de nascimento caem dentro de um determinado intervalo de anos ou meses.

### Exemplo prático:

```sql
CREATE TABLE employees
(
    id         BIGSERIAL,
    birth_date DATE        NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id, birth_date)
) PARTITION BY RANGE (birth_date);
```

Aqui, a tabela `employees` foi particionada pela coluna `birth_date`.

### Por que usamos chaves primárias compostas?

### Por que usamos chaves primárias compostas?

Ao particionar por intervalos, a coluna usada para o particionamento (neste caso, `birth_date`) precisa fazer parte da
chave primária, pois a chave primária deve garantir unicidade entre as partições.

- **Exemplo**: O `id` pode ser único dentro de uma partição, mas não necessariamente em outra. Para garantir que cada
  registro seja único em todas as partições, a chave primária inclui tanto o `id` quanto o `birth_date`.

### Por que o intervalo de 2019 não vai até 31 de dezembro?

No PostgreSQL, a cláusula `TO` define o final de um intervalo **de forma exclusiva**. Portanto, para garantir que todas
as datas de 2019 sejam incluídas, o intervalo precisa ser definido até `2020-01-01`:

```sql
CREATE TABLE employees_2019 PARTITION OF employees
    FOR VALUES FROM
(
    '2019-01-01'
) TO
(
    '2020-01-01'
);
```

Este intervalo inclui todas as datas de `2019-01-01` até `2019-12-31`, já que o valor final (`2020-01-01`) não está
incluído.
O valor inicial é inclusivo (`FROM`), e o valor final é exclusivo (`TO`). Se fosse definido até `'2019-12-31'`, a data
de `2019-12-31` ficaria fora do intervalo e causaria
problemas.

---

### Particionamento por Lista (BY LIST)

#### O que é o particionamento por lista?

O particionamento por lista divide os dados com base em valores específicos 
de uma coluna. É ideal para situações onde você tem um conjunto fixo de categorias 
ou grupos, e cada grupo pode ser representado por uma lista de valores.

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
Neste exemplo, a tabela `employees` é particionada pela coluna country_code, onde cada partição armazena os funcionários de diferentes países.

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

O particionamento por hash distribui os dados de forma uniforme entre várias partições com base em um valor hash
calculado de uma coluna. Ele é útil para balancear a carga de dados, distribuindo-os de maneira uniforme entre as
partições.

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

### Considerações Finais:

- O **particionamento por intervalo** é ideal para dados temporais.
- O **particionamento por lista** é melhor utilizado para categorias fixas, como regiões ou tipos de produtos.
- O **particionamento por hash** distribui os dados uniformemente e é ótimo para grandes volumes, mas não é adequado
  para consultas baseadas em intervalos.

---


