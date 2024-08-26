# DEFININDO DADOS DE TESTE QUE SERÃO EXECUTADOS AUTOMATICAMENTE PELO HIBERNATE POR ESTAR DENTRO
# DE UM ARQUIVO CHAMADO 'import.sql'

-- Inserção de registros na tabela ob_companies
INSERT INTO ob_companies(capital, year, cif, legalName) VALUES (1000000, 2005, 'CIF12345678', 'Tech Solutions Ltda');
INSERT INTO ob_companies(capital, year, cif, legalName) VALUES (5000000, 2010, 'CIF87654321', 'Innovative Corp SA');
INSERT INTO ob_companies(capital, year, cif, legalName) VALUES (750000, 2015, 'CIF11223344', 'Future Enterprises Inc');

-- Inserção de registros na tabela ob_directions
INSERT INTO ob_directions(city, country, street) VALUES ('New York', 'USA', '5th Avenue, 123');
INSERT INTO ob_directions(city, country, street) VALUES ('London', 'UK', 'Baker Street, 221B');
INSERT INTO ob_directions(city, country, street) VALUES   ('Tokyo', 'Japan', 'Shibuya, 456');

-- Inserção de registros na tabela ob_employees
INSERT INTO ob_employees(age, birth_date, married, salary, company_id, direction_pk, register_date, first_name,email, last_name, category) VALUES (28, '1996-03-14', true, 50000, 1, 1, '2024-08-25', 'John', 'john.doe@email.com', 'Doe', 'ANALYST');
INSERT INTO ob_employees(age, birth_date, married, salary, company_id, direction_pk, register_date, first_name,email, last_name, category) VALUES (35, '1989-11-22', false, 75000, 2, 2, '2024-08-26', 'Jane', 'jane.smith@email.com', 'Smith', 'JUNIOR');
INSERT INTO ob_employees(age, birth_date, married, salary, company_id, direction_pk, register_date, first_name,email, last_name, category) VALUES (42, '1982-07-05', true, 60000, 3, 3, '2024-08-27', 'Robert', 'robert.brown@email.com', 'Brown', 'SENIOR');
