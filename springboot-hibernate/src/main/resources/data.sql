-- INSERT INTO employee (id, name, age, email) VALUES (3,'Uanderson', 22, 'uandersonteste@company.com');

/*
-- INSERIR DADOS EM LOTE

WITH RECURSIVE generate_series_100 AS (
  SELECT 1 AS id
  UNION ALL
  SELECT id + 1
  FROM generate_series_100
  WHERE id < 100
)
INSERT INTO employee (id, name, age, email)
SELECT
  id,
  concat(
    (SELECT name FROM (VALUES ('João'), ('Maria'), ('Pedro'), ('Ana')) AS names(name) ORDER BY random() LIMIT 1),
    ' ',
    (SELECT name FROM (VALUES ('Silva'), ('Santos'), ('Oliveira'), ('Souza')) AS names(name) ORDER BY random() LIMIT 1)
  ),
  (random() * 45 + 18)::int,
  lower(concat(
    (SELECT name FROM (VALUES ('João'), ('Maria'), ('Pedro'), ('Ana')) AS names(name) ORDER BY random() LIMIT 1),
    '_',
    (SELECT name FROM (VALUES ('Silva'), ('Santos'), ('Oliveira'), ('Souza')) AS names(name) ORDER BY random() LIMIT 1),
    '@company.com'
  ))
FROM generate_series_100;


 */