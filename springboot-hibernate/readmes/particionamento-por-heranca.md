### Tutorial: Particionamento mediante Herança no PostgreSQL com Detecção Automática de Ano

Olá, meu nome é Uanderson, desenvolvedor Backend Java em formação, e hoje vou te guiar no entendimento sobre *
*Particionamento mediante Herança no PostgreSQL**, uma técnica poderosa para otimizar consultas e organizar dados de
maneira eficiente em grandes bases. Vamos abordar como particionar tabelas, como automatizar a criação de tabelas filhas
baseadas no ano de um registro, e também entender como essa abordagem se alinha com as propriedades ACID de um banco de
dados.

---

### O que é Particionamento mediante Herança?

No PostgreSQL, o particionamento permite dividir uma grande tabela em várias menores, chamadas de "tabelas filhas". Isso
ajuda a melhorar o desempenho, especialmente em grandes volumes de dados, facilitando operações como inserções e
consultas. O particionamento mediante herança usa o mecanismo de **herança** do PostgreSQL, onde uma tabela filha "
herda" a estrutura de uma tabela pai, com algumas restrições adicionais, como a definição de intervalos de datas.

---

### Cenário do Mundo Real: Medições Meteorológicas

Imagine que você está criando um sistema para armazenar medições diárias de temperatura e vendas em diferentes cidades.
Sabemos que os dados podem crescer exponencialmente com o tempo, e você quer garantir que as operações no banco de dados
continuem rápidas. Vamos criar uma tabela particionada com base no ano das medições.

### Passo 1: Criando a Tabela Pai

A tabela **measurement** é a tabela "mestre" que contém as medições de temperatura e vendas. A ideia é dividir esses
dados por ano, criando tabelas filhas específicas para cada ano.

```sql
CREATE TABLE measurement
(
    city_id    INT  NOT NULL,
    log_date   DATE NOT NULL,
    peak_temp  INT,
    unit_sales INT
);
```

Essa tabela contém os seguintes campos:

- `city_id`: ID da cidade.
- `log_date`: Data da medição.
- `peak_temp`: Temperatura máxima do dia.
- `unit_sales`: Unidades vendidas no dia.

### Passo 2: Criando as Tabelas Filhas

Agora, vamos criar duas tabelas filhas para os anos de 2006 e 2007. Elas herdam a estrutura da tabela **measurement**,
mas impõem restrições específicas para que cada tabela contenha apenas os dados do respectivo ano.

```sql
CREATE TABLE measurement_2006
(
    CHECK (log_date >= DATE '2006-01-01' AND log_date < DATE '2007-01-01')
) INHERITS (measurement);

CREATE TABLE measurement_2007
(
    CHECK (log_date >= DATE '2007-01-01' AND log_date < DATE '2008-01-01')
) INHERITS (measurement);
```

Essas tabelas agora contêm dados separados por ano, o que facilita a organização e a recuperação dos dados.

### Passo 3: Direcionando as Inserções com Triggers

Agora precisamos garantir que, ao inserir dados na tabela pai, eles sejam automaticamente redirecionados para a tabela
filha correta com base na data da medição. Para isso, usamos uma função **trigger**.

```sql
CREATE
OR REPLACE FUNCTION measurement_insert_trigger()
RETURNS TRIGGER AS $$
BEGIN
    IF
(NEW.log_date >= '2007-01-01' AND NEW.log_date < '2008-01-01') THEN
        INSERT INTO measurement_2007 VALUES (NEW.*);
    ELSIF
(NEW.log_date >= '2006-01-01' AND NEW.log_date < '2007-01-01') THEN
        INSERT INTO measurement_2006 VALUES (NEW.*);
ELSE
        RAISE EXCEPTION 'Data fora do intervalo particionado';
END IF;
RETURN NULL;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER insert_measurement_trigger
    BEFORE INSERT
    ON measurement
    FOR EACH ROW EXECUTE FUNCTION measurement_insert_trigger();
```

Essa função verifica a data da medição e insere o registro na tabela correta. Se a data não corresponder a nenhum
intervalo, uma exceção é lançada.

### Testando a Inserção

Vamos testar inserindo alguns registros:

```sql
INSERT INTO measurement
VALUES (1, '2006-05-12', 30, 100);
INSERT INTO measurement
VALUES (2, '2007-08-19', 28, 150);
```

Agora, podemos verificar se os dados foram inseridos nas tabelas corretas:

```sql
SELECT *
FROM measurement_2006;
SELECT *
FROM measurement_2007;
```

---

### Passo 4: Criando uma Função para Automatizar Tabelas Filhas

Agora, vamos automatizar a criação de tabelas filhas com base no ano do registro. Isso é útil quando não queremos criar
manualmente uma nova tabela filha para cada ano.

```sql
CREATE
OR REPLACE FUNCTION create_partition_for_year(year INTEGER) RETURNS VOID AS $$
BEGIN
EXECUTE format('CREATE TABLE IF NOT EXISTS measurement_%s (
        CHECK (log_date >= DATE ''%s-01-01'' AND log_date < DATE ''%s-01-01'')
    ) INHERITS (measurement);', year, year, year + 1);
END;
$$
LANGUAGE plpgsql;
```

Essa função cria dinamicamente uma nova tabela filha para o ano especificado, se ela ainda não existir.

Agora, podemos adaptar a trigger para utilizar essa função:

```sql
CREATE
OR REPLACE FUNCTION measurement_insert_dynamic_trigger()
RETURNS TRIGGER AS $$
BEGIN
    PERFORM
create_partition_for_year(EXTRACT(YEAR FROM NEW.log_date));
EXECUTE format('INSERT INTO measurement_%s VALUES ($1.*)', EXTRACT(YEAR FROM NEW.log_date)) USING NEW;
RETURN NULL;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER insert_measurement_dynamic_trigger
    BEFORE INSERT
    ON measurement
    FOR EACH ROW EXECUTE FUNCTION measurement_insert_dynamic_trigger();
```

Essa nova trigger detecta automaticamente o ano do registro e cria a tabela filha correspondente, se ainda não existir.

---

### Particionamento e ACID

No PostgreSQL, a técnica de particionamento mediante herança é **totalmente compatível com ACID** (Atomicidade,
Consistência, Isolamento, Durabilidade). Isso significa que, ao usar particionamento, o banco de dados continua
garantindo:

- **Atomicidade**: As operações são tratadas como indivisíveis.
- **Consistência**: As transações mantêm a consistência dos dados.
- **Isolamento**: As transações são isoladas umas das outras.
- **Durabilidade**: As mudanças feitas pelas transações são permanentes.

Ao particionar dados, estamos apenas distribuindo a carga, mas as propriedades fundamentais do banco de dados permanecem
intactas.

---

### Explicação mais detalhada do o que é ACID?

ACID é um conjunto de propriedades que os bancos de dados garantem para que as transações ocorram de forma confiável:

1. **Atomicidade (A)**: Todas as operações em uma transação são executadas completamente ou nenhuma delas é. Ou seja, se
   qualquer parte de uma transação falhar, tudo é desfeito, como se a transação nunca tivesse acontecido.

2. **Consistência (C)**: As transações mantêm a integridade do banco de dados, sempre deixando o banco em um estado
   válido. Se você tem regras de integridade ou restrições de dados, elas serão respeitadas.

3. **Isolamento (I)**: Cada transação é executada de forma independente, sem interferir nas outras. Mesmo que várias
   transações ocorram simultaneamente, o resultado final será como se elas fossem executadas uma após a outra.

4. **Durabilidade (D)**: Uma vez que uma transação é concluída, suas mudanças são permanentes, mesmo em caso de falha do
   sistema.

Agora, vamos ver como o **particionamento mediante herança** no PostgreSQL respeita essas propriedades.

---

### ACID no Contexto de Particionamento

Quando usamos particionamento por herança, estamos basicamente dividindo os dados em várias tabelas filhas. Isso otimiza
o desempenho do banco, mas não altera as garantias que ele oferece em relação às transações.

#### **Atomicidade** no Particionamento

Mesmo com dados particionados em várias tabelas, a atomicidade das transações é preservada. Quando você insere um
registro em uma tabela particionada (como a tabela pai), o PostgreSQL decide automaticamente qual tabela filha deve
armazenar os dados. Se algo der errado durante a inserção, a transação falha completamente, e nada é comprometido.

**Exemplo**: Se você inserir um registro e houver um erro (por exemplo, falta de conexão ou violação de restrição),
nenhum dado será inserido em nenhuma tabela. Ou seja, ou a transação acontece de forma completa, ou nada é alterado.

#### **Consistência** no Particionamento

O particionamento respeita as mesmas restrições e regras de integridade que você definir no banco de dados. Isso
significa que qualquer restrição de chave estrangeira, índices únicos ou verificações de integridade (como o `CHECK`)
também se aplicam nas tabelas filhas.

**Exemplo**: Se você definir que uma data deve estar dentro de um intervalo específico para cada tabela filha, essa
restrição será verificada antes de permitir qualquer inserção. Se a data não estiver no intervalo permitido, o
PostgreSQL bloqueará a inserção, mantendo a consistência dos dados.

#### **Isolamento** no Particionamento

Mesmo que você tenha múltiplas transações simultâneas operando em diferentes partes do sistema, o particionamento não
afeta o isolamento entre elas. As transações podem ocorrer em diferentes tabelas filhas (ou até na mesma tabela), e o
PostgreSQL ainda garantirá que as transações sejam isoladas umas das outras.

**Exemplo**: Se dois usuários estiverem inserindo dados em tabelas filhas diferentes ao mesmo tempo, suas transações não
vão interferir uma na outra. O PostgreSQL gerencia isso para que o resultado final seja o mesmo que se cada transação
tivesse ocorrido de forma sequencial.

#### **Durabilidade** no Particionamento

Depois que uma transação é confirmada (commit), as alterações são permanentes, independentemente de onde os dados foram
armazenados (na tabela pai ou nas tabelas filhas). Se houver uma falha no sistema (como uma queda de energia), o banco
de dados garantirá que as mudanças feitas durante a transação estejam seguras.

**Exemplo**: Se você fizer uma inserção em uma tabela filha de um dado particionado, mesmo que o sistema falhe logo em
seguida, o dado ainda estará lá após o sistema voltar a funcionar.

---

### Conclusão

Esse tutorial abordou o particionamento mediante herança no PostgreSQL, uma técnica que otimiza o desempenho e facilita
a manutenção de dados. Ao criar automaticamente tabelas filhas com base no ano, você pode gerenciar grandes volumes de
dados com mais eficiência. Além disso, essa técnica é compatível com os princípios ACID, garantindo que suas transações
permaneçam seguras e confiáveis.

