
/*
CTEs - Common Table Expressions (Expressões de Tabela Comuns)

* Resultado temporário obtido de uma instrução SQL
* É uma forma de criar tabelas temporárias para consultar dados em vez de usar subconsultas em uma cláusula FROM.
* CTEs são uma alternativa às subconsultas
* Ao contrário das subconsultas, os CTEs podem ser referenciados diversas vezes em diversas partes da mesma instrução SQL.
* Melhora a legibilidade das frases
* O ciclo de vida dos CTEs é o mesmo de uma instrução SQL
* Usado em conjunto com funções de janela

* 2 tipos: não recursivo, recursivo

Sintaxe:
WITH cte_name (column_list) AS (
	cte_query_definition
)
statement;

*/

-- EXEMPLO 1
WITH cte_numbers AS (SELECT *
                     FROM generate_series(1, 10) AS id) -- Gera uma série de números de 1 a 10
SELECT *
FROM cte_numbers; -- Seleciona todos os números gerados pela CTE

WITH cte_numbers AS (SELECT *
                     FROM generate_series(1, 10) AS id) -- Gera uma série de números de 1 a 10
SELECT *
FROM cte_numbers
WHERE id > 5; -- Seleciona apenas os números maiores que 5

-- EXEMPLO 2
-- Gerar datas com horários diferentes a cada 6h
WITH cte_dates AS (SELECT *
                   FROM generate_series(
                                '2021-01-01 00:00'::timestamp,
                                '2021-12-20 00:00',
                                '6 hours'
                        ) AS creation_date) -- Gera datas de 6 em 6 horas entre 01/01/2021 e 20/12/2021
SELECT *
FROM cte_dates; -- Seleciona todas as datas geradas pela CTE

-- EXEMPLO 3
-- Base de dados: demo, schema: ticket_flights
SELECT *
FROM ticket_flights; -- Seleciona todos os registros da tabela ticket_flights

WITH cte_flights AS (SELECT flight_id,
                            fare_conditions,
                            amount,
                            (
                                CASE
                                    WHEN amount < 10000 THEN 'CHEAP' -- BARATO
                                    WHEN amount < 30000 THEN 'MEDIUM' -- MÉDIO
                                    ELSE 'EXPENSIVE' -- CARO
                                    END
                                ) AS price_categorie -- Classifica os preços em categorias
                     FROM ticket_flights)
SELECT *
FROM cte_flights
WHERE price_categorie = 'CHEAP'; -- Seleciona apenas os voos com preços classificados como 'CHEAP'

-- EXEMPLO 4
-- Excluir dados de uma tabela original para uma tabela de histórico

-- TABELA ORIGINAL
SELECT *
FROM SEATS; -- Seleciona todos os registros da tabela SEATS

SELECT *
FROM SEATS_ARCHIVE; -- Seleciona todos os registros da tabela SEATS_ARCHIVE

DROP TABLE IF EXISTS SEATS; -- Remove a tabela SEATS se ela existir

DROP TABLE IF EXISTS SEATS_ARCHIVE; -- Remove a tabela SEATS_ARCHIVE se ela existir

-- CRIANDO A TABELA DE HISTÓRICO COM A ESTRUTURA DA ORIGINAL, MAS SEM NENHUM REGISTRO
CREATE TABLE SEATS_ARCHIVE AS
SELECT *
FROM SEATS LIMIT 0; -- Cria a tabela SEATS_ARCHIVE com a mesma estrutura da SEATS, mas sem registros

-- FAZENDO UM SELECT NA TABELA ORIGINAL E COM O RESULTADO INSERINDO NA TABELA DE HISTÓRICO
INSERT INTO SEATS_ARCHIVE
SELECT *
FROM SEATS; -- Insere todos os registros da tabela SEATS na tabela SEATS_ARCHIVE

WITH CTE_SEATS_ARCHIVE_AICRAFT AS (
DELETE
FROM SEATS
WHERE AIRCRAFT_CODE = '319' RETURNING * -- Deleta registros da tabela SEATS onde AIRCRAFT_CODE é '319' e retorna os registros deletados
)
INSERT
INTO SEATS_ARCHIVE
SELECT *
FROM CTE_SEATS_ARCHIVE_AICRAFT; -- Insere os registros deletados na tabela SEATS_ARCHIVE

/*
Funções agregadas
* Funções agregadas (COUNT, AVG, SUM...) agregam dados de um conjunto
de linhas em uma única linha (1 resultado) realizando um cálculo.

Funções de janela (window functions)
* As funções da janela permitem realizar cálculos em um conjunto de linhas relacionadas à linha atual.
* Eles não agrupam os dados em um único resultado
* Permitem realizar cálculos sem perder detalhes ou reduzir o número de resultados como
  acontece com funções agregadas.
* As agregações são criadas nas próprias linhas, sem reduzir o número de resultados

Sintaxe:

OVER ()
PARTITION BY()
ORDER BY()

ROW_NUMBER()
RANK()
DENSE_RANK()
FIRST_VALUE()
LAST_VALUE()
LAG()
LEAD()

*/

SELECT *
FROM products; -- Seleciona todos os registros da tabela products
SELECT *
FROM ORDERS; -- Seleciona todos os registros da tabela ORDERS

SELECT x
FROM generate_series(1, 10) AS x; -- Gera uma série de números de 1 a 10, um por linha/tupla
SELECT array_agg(x)
FROM generate_series(1, 10) AS x; -- Gera uma série de números de 1 a 10 e os agrega em um array
SELECT sum(x)
FROM generate_series(1, 10) AS x; -- Soma todos os números gerados

-- Será retornado duas colunas:
-- 1. x == resultado da função generate_series(1,7) AS x
-- 2. frame == resultado da função de janela array_agg(x) OVER () AS frame
-- OVER ( [MANIPULAR O 1° RESULTADO])
SELECT *, array_agg(x) OVER () AS frame
FROM generate_series(1, 7) AS x; -- Gera uma série de números de 1 a 7 e agrega todos os números em um array na coluna frame

SELECT *, array_agg(x) OVER (ORDER BY x) AS frame
FROM generate_series(1, 7) AS x; -- Gera uma série de números de 1 a 7 e agrega os números em um array na coluna frame, ordenados por x

-- O mesmo que SELECT *, array_agg(x) OVER (ORDER BY x) AS frame FROM generate_series(1,7) AS x;
-- O que é executado por debaixo dos panos
SELECT *,
       array_agg(x) OVER (
	ORDER BY x ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
)
AS frame
FROM generate_series(1, 7) AS x; -- Gera uma série de números de 1 a 7 e agrega os números em um array na coluna frame, considerando todas as linhas desde o início até a linha atual

-- Trecho que permite alteração, para manipulação do resultado [UNBOUNDED PRECEDING AND CURRENT ROW]
-- Invertendo os resultados
SELECT *,
       array_agg(x) OVER (
	ORDER BY x ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING
)
AS frame
FROM generate_series(1, 7) AS x; -- Gera uma série de números de 1 a 7 e agrega os números em um array na coluna frame, considerando todas as linhas desde a linha atual até o final

SELECT *,
       array_agg(x) OVER (
	ORDER BY x ROWS BETWEEN UNBOUNDED PRECEDING AND 0 FOLLOWING
)
AS frame
FROM generate_series(1, 7) AS x; -- Gera uma série de números de 1 a 7 e agrega os números em um array na coluna frame, considerando todas as linhas desde o início até a linha atual

SELECT *,
       array_agg(x) OVER (
	ORDER BY x ROWS BETWEEN UNBOUNDED PRECEDING AND 1 FOLLOWING
)
AS frame
FROM generate_series(1, 7) AS x; -- Gera uma série de números de 1 a 7 e agrega os números em um array na coluna frame, considerando todas as linhas desde o início até a linha seguinte

SELECT *
FROM products; -- Seleciona todos os registros da tabela products

SELECT PRODUCT_ID, PRODUCT_NAME, CATEGORY_ID, CATEGORY_NAME
FROM PRODUCTS
         INNER JOIN CATEGORIES USING (CATEGORY_ID); -- Realiza um INNER JOIN entre as tabelas PRODUCTS e CATEGORIES usando CATEGORY_ID

-- Calcular e mostrar a coluna com o preço médio dos produtos de uma mesma categoria
SELECT PRODUCT_ID,
       PRODUCT_NAME,
       CATEGORY_ID,
       CATEGORY_NAME,
       UNIT_PRICE,
       AVG(UNIT_PRICE) OVER (PARTITION BY CATEGORY_ID), -- Média dos preços por categoria
       ROW_NUMBER() OVER (PARTITION BY CATEGORY_ID) -- Posição do registro dentro da categoria
FROM PRODUCTS
         INNER JOIN CATEGORIES USING (CATEGORY_ID); -- Realiza um INNER JOIN entre as tabelas PRODUCTS e CATEGORIES usando CATEGORY_ID
