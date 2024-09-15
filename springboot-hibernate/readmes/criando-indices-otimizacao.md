### Criação de Índices no PostgreSQL

> FONTE: <https://www.postgresqltutorial.com/postgresql-indexes/postgresql-create-index/>

#### 1. **Conceito de Índice**

Índices são estruturas de dados adicionais que facilitam a recuperação de dados de uma tabela, acelerando consultas ao
custo de espaço em disco e de um maior tempo para operações de inserção, atualização e exclusão.
Sem um índice, o banco de dados realiza uma **varredura sequencial** (ou seja, lê cada linha da tabela)
para encontrar os dados solicitados, o que pode ser ineficiente em grandes volumes de dados.
Ao criar um índice O PostgreSQL cria por padrão um índice do tipo **B-tree**, adequado para a maioria das operações de busca.

> VERIFICAR OS READMES PARA CONHECER OS TIPOS DE ÍNDICES

#### 2. **Criação de Índices: Sintaxe Básica**

```sql
CREATE INDEX [IF NOT EXISTS] nome_do_indice
    ON nome_da_tabela (coluna1, coluna2,...);
```

- **IF NOT EXISTS**: Evita erro se o índice já existir.
- **nome_da_tabela**: Nome da tabela onde o índice será criado.
- **coluna1, coluna2, ...**: Colunas usadas para o índice. Pode incluir uma ou mais colunas.

#### 3. **Índices Exclusivos (UNIQUE)**

Garantem que os valores em uma ou mais colunas sejam únicos (integridade).

- **Vantagens**: Evita duplicidade, ideal para colunas que devem conter valores únicos, como emails, CPF, etc.
- **Desvantagens**: Pode aumentar o custo de escrita, pois o sistema precisa garantir a unicidade a cada inserção ou
  atualização.

**Sintaxe**:

```sql
CREATE UNIQUE INDEX nome_do_indice
    ON nome_da_tabela (coluna1, coluna2, . . .);
```

**Exemplo**:

```sql
CREATE UNIQUE INDEX idx_unique_email
    ON users (email);
```

#### 4. **Índices Parciais**

São criados para indexar apenas um subconjunto de dados que atende a uma condição específica, otimizando o desempenho em
consultas frequentes para esse subconjunto.

- **Vantagens**: Menos espaço em disco e melhores tempos de atualização para tabelas grandes.
- **Desvantagens**: Uso limitado, pois só melhora as consultas que se beneficiam da condição do índice.

**Sintaxe**:

```sql
CREATE INDEX nome_do_indice
    ON nome_da_tabela (coluna) WHERE condição;
```

**Exemplo**:

```sql
CREATE INDEX idx_employee_active_false
    ON employee (active) WHERE active IS false;
```

#### 5. **Índices em Expressões**

Permitem criar índices baseados em expressões em vez de colunas diretas. São úteis quando consultas utilizam funções ou
transformações em colunas.

- **Vantagens**: Melhoram a performance de consultas com expressões frequentemente usadas.
- **Desvantagens**: Aumento do custo de manutenção, já que o índice deve ser recalculado sempre que os valores forem
  atualizados.

**Sintaxe**:

```sql
CREATE INDEX nome_do_indice
    ON nome_da_tabela (função(coluna));
```

**Exemplo**:

```sql
CREATE INDEX idx_employee_register_year
    ON employee (EXTRACT(year FROM register_date));
```

#### 6. **Índices Multicolunas**

Permitem criar índices em mais de uma coluna, sendo vantajosos para consultas que filtram por múltiplos critérios.

- **Vantagens**: Melhoram a performance de consultas que utilizam várias colunas no WHERE.
- **Desvantagens**: Menos eficiente se a consulta filtrar por colunas que não sejam a primeira ou segunda coluna do
  índice.

**Sintaxe**:

```sql
CREATE INDEX nome_do_indice
    ON nome_da_tabela (coluna1, coluna2, . . .);
```

**Exemplo**:

```sql
CREATE INDEX idx_employee_name_active
    ON employee (name, active);
```

### 7. **Boas Práticas na Criação de Índices**

- **Nomes de Índices**: Siga um padrão de nomeação que inclua a tabela e as colunas do índice, como `idx_tabela_coluna`.

- **Evitar Índices Desnecessários**: Índices aumentam o custo de escrita e consomem espaço. Crie índices apenas para
  consultas frequentes.

- **Índices Multicolunas**: Coloque as colunas mais filtradas no início.

- **Consultas Frequentes**: Analise as consultas mais comuns usando `EXPLAIN ANALYZE` para entender quais colunas ou
  expressões podem ser indexadas.

- **Identifique Consultas Frequentes**: Crie índices para colunas que são frequentemente usadas em WHERE, JOIN ou ORDER
  BY.

- **Evite Índices em Colunas de Baixa Cardinalidade**: Colunas com poucos valores distintos (como true/false ou
  ativo/inativo) geralmente não se beneficiam de índices tradicionais. Nestes casos, considere usar **índices parciais
  **.

### 8. **Vantagens e Desvantagens dos Índices**

- **Vantagens**:
    - Aceleram consultas de leitura.
    - Facilitam a ordenação de dados.
    - Reduzem o tempo de busca em grandes volumes de dados.

- **Desvantagens**:
    - Custos adicionais de armazenamento.
    - Aumentam o tempo de inserção, atualização e exclusão.
    - Podem ser menos eficazes se a consulta não usar as colunas do índice.

#### 9. **Exemplos Aplicados em Consultas**

- Índice em coluna simples:
  ```sql
  CREATE INDEX idx_employee_name ON employee(name);
  ```

- Índice parcial para valores booleanos:
  ```sql
  CREATE INDEX idx_employee_active_false ON employee(active) WHERE active IS false;
  ```

- Índice para melhorar consultas em faixas de idade:
  ```sql
  CREATE INDEX idx_employee_age_lt_30 ON employee(age) WHERE age < 30;
  ```

#### Conclusão

Os índices são essenciais para otimizar consultas em bancos de dados, especialmente em tabelas com grandes volumes de
dados. No entanto, devem ser usados com cuidado, considerando as características das consultas, o impacto nos custos de
escrita e o espaço adicional consumido.