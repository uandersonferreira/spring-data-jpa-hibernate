-- Consulta sem índice, retornando todos os dados da tabela "employee".
SELECT * FROM employee;

-- Comando EXPLAIN ANALYZE para analisar o desempenho da consulta anterior.
-- Como não há filtro, um "Seq Scan" (varredura sequencial) será feito, já que a consulta retorna todos os registros.
EXPLAIN ANALYZE SELECT * FROM employee;

-- A coluna "id" é a chave primária (PK) da tabela. Por padrão, o PostgreSQL cria um índice para chaves primárias.
-- Portanto, a consulta é otimizada usando esse índice para buscar o registro onde "id = 78999".
SELECT * FROM employee WHERE id = 78999;

-- Analisando o desempenho da consulta acima. O PostgreSQL utiliza o índice na coluna "id", fazendo uma busca eficiente.
EXPLAIN ANALYZE SELECT * FROM employee WHERE id = 78999;

-- Buscando um funcionário pelo nome.
SELECT * FROM employee WHERE name = 'Employee 78999';

-- Analisando a consulta por nome. Se a tabela for grande e não houver um índice na coluna "name",
-- a consulta pode ser lenta, pois o PostgreSQL fará uma varredura sequencial.
EXPLAIN ANALYZE SELECT * FROM employee WHERE name = 'Employee 78999';

-- Para otimizar consultas que buscam pelo nome, é criado um índice na coluna "name".
CREATE INDEX idx_employee_name ON employee(name);

-- Consulta para filtrar funcionários com o campo "active" como verdadeiro (true).
SELECT * FROM employee WHERE active IS true;

-- Consulta para filtrar funcionários com o campo "active" como falso (false).
SELECT * FROM employee WHERE active IS false;

-- Analisando a consulta com "active = true". Se não houver um índice, o PostgreSQL realizará uma varredura sequencial.
EXPLAIN ANALYZE SELECT * FROM employee WHERE active IS true;

-- Analisando a consulta com "active = false". A consulta pode ser otimizada com o uso de um índice parcial (ver abaixo).
EXPLAIN ANALYZE SELECT * FROM employee WHERE active IS false;

-- O índice na coluna "active" otimiza as consultas. Entretanto, em colunas booleanas com poucos valores distintos (como true/false),
-- o índice pode ser menos eficaz, pois o PostgreSQL pode considerar que a varredura sequencial seria mais eficiente para certos casos.
-- O índice padrão não é limitado a "false", ele otimiza ambas as consultas, mas em cenários específicos, um índice parcial pode ser melhor.
CREATE INDEX idx_employee_active ON employee(active);

-- Criando um índice parcial para otimizar consultas somente onde "active = false".
-- Isso é útil se "false" for o valor menos comum, permitindo uma busca mais rápida para esse subconjunto de dados.
CREATE INDEX IF NOT EXISTS idx_employee_active_false ON employee(active) WHERE active IS false;

-- Analisando a performance de uma consulta que busca funcionários com idade entre 1 e 29 anos.
EXPLAIN ANALYZE SELECT * FROM employee WHERE age BETWEEN 1 AND 29;

-- Criando um índice parcial para otimizar consultas onde a idade é menor que 30.
-- Isso reduz o tamanho do índice e melhora a performance de consultas que se encaixam nesse intervalo.
CREATE INDEX IF NOT EXISTS idx_employee_age_lt_30 ON employee(age) WHERE age < 30;

-- Caso execute uma consulta com um valor fora do intervalo do índice parcial (por exemplo, idade entre 1 e 50),
-- o PostgreSQL fará uma varredura sequencial, pois o índice parcial cobre apenas "age < 30".
EXPLAIN ANALYZE SELECT * FROM employee WHERE age BETWEEN 1 AND 50;

-- Quanto mais específico for o índice, melhor a otimização da consulta. Aqui, é criado um índice específico para idades entre 18 e 22.
CREATE INDEX IF NOT EXISTS idx_employee_age_lt_22 ON employee(age) WHERE age >= 18 AND age <= 22;

-- Analisando a performance de uma consulta que busca funcionários com idade igual a 20.
EXPLAIN ANALYZE SELECT * FROM employee WHERE age = 20;

-- Analisando a performance de uma consulta que busca funcionários registrados em 2020.
EXPLAIN ANALYZE SELECT * FROM employee WHERE EXTRACT(year FROM register_date) = 2020;

-- Criando um índice com base em uma expressão, neste caso extraindo o ano da coluna "register_date".
-- Isso otimiza consultas que filtram por ano, como registros feitos em 2020.
CREATE INDEX IF NOT EXISTS idx_employee_register_date_2020
    ON employee(register_date)
    WHERE EXTRACT(year FROM register_date) = 2020;
