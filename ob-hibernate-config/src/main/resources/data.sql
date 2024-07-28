# PARA QUE SEJA EXECUTADO(data.sql), JUNTO COM O import.sql DEVE SER CONFIGURAR NO file hibernate.cfg.xml
# <property name="hibernate.hbm2ddl.import_files">import.sql, data.sql</property>

INSERT INTO ob_employees (age, birth_date, email, first_name, last_name, married, register_date, salary) VALUES (27, '1997-09-25', 'fiona.white@example.com', 'Fiona', 'White', FALSE, '2024-07-28 12:00:00', 60000.00);

INSERT INTO ob_employees (age, birth_date, email, first_name, last_name, married, register_date, salary) VALUES (38, '1986-04-13', 'george.clark@example.com', 'George', 'Clark', TRUE, '2024-07-28 12:00:00', 70000.00);
