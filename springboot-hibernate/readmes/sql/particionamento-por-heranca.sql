-- PARTIÇÃO POR HERANÇA NO POSTGRESQL

-- Criação da tabela "PAI"
-- Esta tabela servirá como base para as tabelas particionadas
CREATE TABLE measurement (
                             city_id INT NOT NULL,    -- ID da cidade, obrigatório
                             log_date DATE NOT NULL,  -- Data do registro, obrigatório
                             peak_temp INT,           -- Temperatura máxima registrada (opcional)
                             unit_sales INT           -- Vendas em unidades (opcional)
);

-- Exibindo os registros da tabela "measurement"
SELECT * FROM measurement;

-- Criação da tabela "FILHA" para o ano de 2006
-- Esta tabela armazena dados somente de 2006, e herda a estrutura da tabela "measurement"
CREATE TABLE measurement_2006 (
                                  CHECK (log_date >= DATE '2006-01-01' AND log_date < DATE '2007-01-01')  -- Restrição para garantir que a data está no ano de 2006
) INHERITS (measurement);  -- A tabela filha herda todas as colunas da tabela "measurement"

-- Criação da tabela "FILHA" para o ano de 2007
-- Esta tabela armazena dados somente de 2007, e também herda a estrutura da tabela "measurement"
CREATE TABLE measurement_2007 (
                                  CHECK (log_date >= DATE '2007-01-01' AND log_date < DATE '2008-01-01')  -- Restrição para garantir que a data está no ano de 2007
) INHERITS (measurement);  -- Herda a estrutura da tabela "measurement"

-- CRIAÇÃO DA FUNCTION PARA REDIRECIONAR INSERÇÕES
-- A função abaixo é utilizada como uma trigger para determinar qual tabela filha deve receber os dados
-- Essa função insere dados automaticamente na tabela correta (measurement_2007 neste caso)
CREATE OR REPLACE FUNCTION measurement_insert_trigger()
RETURNS TRIGGER AS $$
BEGIN
    -- Neste exemplo simples, estamos inserindo diretamente na tabela measurement_2007
    -- Caso o registro esteja dentro do ano de 2007, será inserido na tabela correta
INSERT INTO measurement_2007 VALUES (NEW.*);  -- NEW.* indica todos os valores do novo registro
RETURN NULL;  -- Retorna NULL porque o dado já foi inserido diretamente na tabela filha
END;
$$ LANGUAGE plpgsql;

-- CRIAÇÃO DO TRIGGER QUE APLICA A FUNÇÃO
-- Este trigger será disparado toda vez que um registro for inserido na tabela "measurement" (tabela pai)
CREATE TRIGGER insert_measurement_trigger
    BEFORE INSERT ON measurement  -- Trigger será acionado antes da inserção na tabela "measurement"
    FOR EACH ROW EXECUTE PROCEDURE measurement_insert_trigger();  -- Executa a função de particionamento

-- INSERINDO DADOS
-- Neste exemplo, estamos inserindo dois registros na tabela pai "measurement"
-- A trigger redirecionará automaticamente esses dados para a tabela filha correta (measurement_2007)
INSERT INTO measurement VALUES
                            (1, '2007-06-04', 5, 6),  -- Inserindo uma data válida para o ano de 2007
                            (1, '2007-06-04', 5, 6);  -- Outro registro para 2007

-- Exibindo todos os registros na tabela "measurement"
SELECT * FROM measurement;

-- Exibindo os registros da tabela "FILHA" para 2006 (não haverá dados porque inserimos datas de 2007)
SELECT * FROM measurement_2006;

-- Exibindo os registros da tabela "FILHA" para 2007 (onde nossos dados foram armazenados)
SELECT * FROM measurement_2007;

-- EXEMPLO COM DETECÇÃO AUTOMÁTICA DE ANO

-- Agora, vamos criar uma função mais inteligente que detecta o ano automaticamente e insere o dado na tabela filha correta.
CREATE OR REPLACE FUNCTION measurement_insert_trigger()
RETURNS TRIGGER AS $$
BEGIN
    -- Se a data do novo registro estiver no ano de 2007, insere na tabela measurement_2007
    IF (NEW.log_date >= '2007-01-01' AND NEW.log_date < '2008-01-01') THEN
        INSERT INTO measurement_2007 VALUES (NEW.*);

    -- Se a data do novo registro estiver no ano de 2006, insere na tabela measurement_2006
    ELSIF (NEW.log_date >= '2006-01-01' AND NEW.log_date < '2007-01-01') THEN
        INSERT INTO measurement_2006 VALUES (NEW.*);

    -- Caso a data não pertença a nenhum dos anos particionados, gera uma exceção
ELSE
        RAISE EXCEPTION 'Data fora do intervalo particionado';  -- Levanta um erro se a data não for de 2006 ou 2007
END IF;
RETURN NULL;  -- Retorna NULL porque os dados já foram inseridos
END;
$$ LANGUAGE plpgsql;

-- CRIANDO UM NOVO TRIGGER QUE UTILIZA A FUNÇÃO ACIMA
-- O trigger agora consegue determinar automaticamente em qual tabela filha o registro deve ser inserido
CREATE TRIGGER insert_measurement_trigger
    BEFORE INSERT ON measurement
    FOR EACH ROW EXECUTE FUNCTION measurement_insert_trigger();  -- Usa a função para redirecionar a inserção

-- INSERINDO DADOS COM A DETECÇÃO AUTOMÁTICA DO ANO
INSERT INTO measurement VALUES
                            (1, '2006-05-15', 15, 10),  -- Este registro será inserido na tabela measurement_2006
                            (1, '2007-03-12', 20, 12),  -- Este registro será inserido na tabela measurement_2007
                            (1, '2005-12-25', 25, 8);   -- Este registro vai gerar um erro porque a data está fora do intervalo particionado

-- CONSULTAS PARA VERIFICAR OS DADOS NAS TABELAS FILHAS
SELECT * FROM measurement_2006;  -- Verifica os registros de 2006
SELECT * FROM measurement_2007;  -- Verifica os registros de 2007
