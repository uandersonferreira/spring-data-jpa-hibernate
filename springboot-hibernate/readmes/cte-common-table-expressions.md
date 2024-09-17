# Common Table Expressions (CTEs) no PostgreSQL

#### Conceito Geral
Common Table Expressions (CTEs) são um recurso que permite criar subconsultas nomeadas dentro de uma consulta maior. Elas tornam o código SQL mais legível e gerenciável, especialmente para consultas complexas, porque permitem a criação de "tabelas temporárias" que podem ser referenciadas na consulta principal.

#### Sintaxe Básica:
A sintaxe de uma CTE é simples, começando com a palavra-chave `WITH`, seguida pelo nome da CTE e a subconsulta entre parênteses:

```sql
WITH cte_name AS (
    SELECT ...
)
SELECT * FROM cte_name;
```

#### Aplicabilidade:
- **Modularidade e Reutilização:** Dividem consultas complexas em blocos menores e reutilizáveis, facilitando a manutenção.
- **Recursividade:** Permitem consultas recursivas, úteis para navegar em hierarquias ou grafos.

#### Exemplo de CTE:
Calcular a média de preços de produtos e usar essa média em uma consulta principal:

```sql
WITH avg_price AS (
    SELECT group_name, AVG(price) AS avg_price
    FROM products
    INNER JOIN product_groups USING (group_id)
    GROUP BY group_name
)
SELECT product_name, price, avg_price
FROM products
INNER JOIN product_groups USING (group_id)
INNER JOIN avg_price USING (group_name);
```

### Funções de Janela no PostgreSQL

#### Conceito Geral:
Funções de janela permitem realizar cálculos sobre um conjunto de linhas que são relacionadas à linha atual, sem alterar o número de linhas retornadas. Diferente das funções agregadas, que reduzem o número de linhas, funções de janela operam sobre "janelas" definidas para cada linha.

#### Sintaxe:
A sintaxe básica de uma função de janela envolve a palavra-chave `OVER`, podendo incluir as cláusulas `PARTITION BY` e `ORDER BY`.

```sql
window_function(arg1, arg2, ...) OVER (
   [PARTITION BY partition_expression]
   [ORDER BY sort_expression]
)
```

- **`PARTITION BY`**: Divide o conjunto de resultados em partições, dentro das quais a função será aplicada.
- **`ORDER BY`**: Ordena as linhas dentro de cada partição antes de aplicar a função de janela.

#### Exemplo de Função de Janela com `AVG`:

Aqui, calculamos o preço médio dos produtos dentro de cada grupo, sem reduzir o número de linhas:

```sql
SELECT 
    product_name, 
    price, 
    group_name, 
    AVG(price) OVER (PARTITION BY group_name) AS avg_price_per_group
FROM 
    products
INNER JOIN product_groups USING (group_id);
```

Neste exemplo, a função `AVG` calcula a média do preço dentro das partições definidas por `group_name`.

### Exemplos de Funções de Janela

### 1. **CUME_DIST**
- **Descrição:** Retorna a distribuição acumulada de uma linha dentro de uma partição, calculando a proporção de linhas com valores menores ou iguais ao valor atual.
- **Exemplo:**
```sql
SELECT product_name, price,
  CUME_DIST() OVER (PARTITION BY group_name ORDER BY price) AS cume_dist
FROM products
INNER JOIN product_groups USING (group_id);
```
Isso retorna a posição relativa do preço atual em comparação com os outros produtos do mesmo grupo.

### 2. **DENSE_RANK**
- **Descrição:** Atribui uma classificação às linhas dentro de uma partição sem pular números de classificação em caso de empate (diferente de `RANK`).
- **Exemplo:**
```sql
SELECT product_name, price,
  DENSE_RANK() OVER (PARTITION BY group_name ORDER BY price) AS dense_rank
FROM products
INNER JOIN product_groups USING (group_id);
```
Isso classifica os produtos pelo preço dentro de cada grupo de produtos, sem pular classificações em caso de empate.

### 3. **FIRST_VALUE**
- **Descrição:** Retorna o valor da primeira linha dentro da partição, de acordo com a ordem especificada.
- **Exemplo:**
```sql
SELECT product_name, price,
  FIRST_VALUE(price) OVER (PARTITION BY group_name ORDER BY price) AS first_price
FROM products
INNER JOIN product_groups USING (group_id);
```
Aqui, ele retorna o preço mais baixo em cada grupo de produtos.

### 4. **LAG**
- **Descrição:** Acessa o valor da linha anterior à linha atual na partição. Se a linha anterior não existir, pode retornar `NULL` ou um valor especificado.
- **Exemplo:**
```sql
SELECT product_name, price,
  LAG(price, 1) OVER (PARTITION BY group_name ORDER BY price) AS previous_price
FROM products
INNER JOIN product_groups USING (group_id);
```
Retorna o preço do produto anterior dentro de cada grupo de produtos.

### 5. **LAST_VALUE**
- **Descrição:** Retorna o valor da última linha dentro da partição, conforme a ordem especificada. Normalmente, é necessário usar uma cláusula de janela específica para definir o intervalo da janela.
- **Exemplo:**
```sql
SELECT product_name, price,
  LAST_VALUE(price) OVER (
    PARTITION BY group_name 
    ORDER BY price 
    RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
  ) AS last_price
FROM products
INNER JOIN product_groups USING (group_id);
```
Este exemplo retorna o preço mais alto de cada grupo de produtos.

### 6. **LEAD**
- **Descrição:** Acessa o valor da linha seguinte à linha atual na partição. Se a linha seguinte não existir, pode retornar `NULL` ou um valor especificado.
- **Exemplo:**
```sql
SELECT product_name, price,
  LEAD(price, 1) OVER (PARTITION BY group_name ORDER BY price) AS next_price
FROM products
INNER JOIN product_groups USING (group_id);
```
Retorna o preço do próximo produto dentro de cada grupo.

### 7. **NTILE**
- **Descrição:** Divide as linhas em `n` partes aproximadamente iguais e atribui um número inteiro a cada linha, indicando o grupo em que a linha se encontra.
- **Exemplo:**
```sql
SELECT product_name, price,
  NTILE(4) OVER (PARTITION BY group_name ORDER BY price) AS quartile
FROM products
INNER JOIN product_groups USING (group_id);
```
Divide os produtos em quatro grupos (quartis) dentro de cada grupo de produtos.

### 8. **NTH_VALUE**
- **Descrição:** Retorna o valor da enésima linha dentro de uma partição, com base na ordem especificada.
- **Nota sobre "enésima":** A palavra "enésima" refere-se a uma posição específica em uma sequência. Por exemplo, a "enésima" linha pode ser a terceira, a décima, a centésima, etc. Nesse contexto, "enésima" é usada para se referir à posição "n" na sequência de dados.
- **Exemplo:**
```sql
SELECT product_name, price,
  NTH_VALUE(price, 3) OVER (PARTITION BY group_name ORDER BY price) AS third_price
FROM products
INNER JOIN product_groups USING (group_id);
```
A função `NTH_VALUE(price, 3)` retorna o valor da terceira posição (ou "terceiro menor preço") dentro de cada partição de `group_name`, ordenado por `price`.

### 9. **PERCENT_RANK**
- **Descrição:** Calcula a classificação percentual de uma linha dentro de sua partição, com base na ordenação especificada.
- **Exemplo:**
```sql
SELECT product_name, price,
  PERCENT_RANK() OVER (PARTITION BY group_name ORDER BY price) AS percent_rank
FROM products
INNER JOIN product_groups USING (group_id);
```
Retorna a classificação percentual de cada produto dentro do seu grupo.

### 10. **RANK**
- **Descrição:** Atribui uma classificação às linhas dentro de sua partição, com lacunas nos números de classificação em caso de empate.
- **Exemplo:**
```sql
SELECT product_name, price,
  RANK() OVER (PARTITION BY group_name ORDER BY price) AS rank
FROM products
INNER JOIN product_groups USING (group_id);
```
A diferença para `DENSE_RANK` é que `RANK` pula números no caso de produtos com o mesmo preço.

### 11. **ROW_NUMBER**
- **Descrição:** Atribui um número sequencial único a cada linha dentro de sua partição, começando de 1.
- **Exemplo:**
```sql
SELECT product_name, price,
  ROW_NUMBER() OVER (PARTITION BY group_name ORDER BY price) AS row_num
FROM products
INNER JOIN product_groups USING (group_id);
```
Aqui, ele retorna um número sequencial para cada produto dentro de cada grupo.

### Cenários de Uso:
- **Cálculos estatísticos:** Para classificações e percentuais, como `RANK` ou `PERCENT_RANK`, úteis em análises financeiras e de dados.
- **Comparações entre linhas:** Funções como `LAG` e `LEAD` são perfeitas para calcular a diferença entre linhas consecutivas, como em séries temporais.
- **Particionamento e agrupamento:** `PARTITION BY` permite operar em subconjuntos de dados dentro de uma tabela, como calcular somas ou médias por grupo, mantendo todas as linhas.
- **Análises de séries temporais:** Comparar valores consecutivos de períodos de tempo.
- **Rankings e classificações:** Como calcular a posição de um jogador em um torneio ou classificar vendas de produtos.
- **Recuperar valores de linhas adjacentes:** Usar `LAG` e `LEAD` para calcular a diferença de valores entre linhas consecutivas.

