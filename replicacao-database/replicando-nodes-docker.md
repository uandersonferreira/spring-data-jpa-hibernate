Para replicar a configuração do `node1` para `node2` e `node3` sem ter que refazer cada passo manualmente, você pode
usar uma abordagem baseada em cópias do contêiner do `node1` ou com o uso de imagens Docker customizadas. Vamos seguir
por um caminho que usa o Docker para clonar o ambiente, ajustando somente o que for necessário (como os IPs).

### Passos para replicar as configurações usando Docker:

1. **Criar uma imagem do `node1` com todas as configurações aplicadas**  
   Primeiro, configure o `node1` totalmente, como já fez, incluindo todas as instalações (PostgreSQL, Python, Patroni,
   etc.). Depois, transforme o contêiner `node1` em uma imagem para reutilização.

```bash
# No final da configuração completa do node1, crie uma nova imagem baseada no container
docker commit node1 node1_custom_image
```

Isso cria uma imagem chamada `node1_custom_image` com todas as configurações feitas no contêiner `node1`.

Como você já criou os contêineres `node2` e `node3`, será necessário removê-los antes de criar novos contêineres a
partir da imagem personalizada que vamos gerar do `node1`.
Para remover os contêineres existentes, siga os passos abaixo.

### Remover os contêineres `node2` e `node3`

1. **Parar os contêineres `node2` e `node3`:**

```bash
docker stop node2 node3
```

2. **Remover os contêineres `node2` e `node3`:**

```bash
docker rm node2 node3
```

Com os contêineres removidos, agora você pode seguir os passos para criar novos contêineres baseados na imagem do
`node1`.

2. **Criar os contêineres `node2` e `node3` a partir dessa imagem**  
   Agora, você pode usar essa nova imagem para criar os outros contêineres. Para isso, basta alterar os IPs e os
   detalhes no arquivo `config.yml` do Patroni.

```bash
# Criação dos novos contêineres node2 e node3 a partir da imagem customizada do node1
docker run -itd --name=node2 --net clusternet --ip 172.20.0.11 node1_custom_image
docker run -itd --name=node3 --net clusternet --ip 172.20.0.12 node1_custom_image
```

3. **Editar o arquivo de configuração do Patroni para `node2` e `node3`**  
   Entre nos novos contêineres e edite o arquivo `config.yml` de acordo com os IPs e nomes corretos para `node2` e
   `node3`:

```bash
# Acessar o node2 e editar config.yml
docker exec -it node2 bash

nano /etc/patroni/config.yml
```

Altere as linhas relevantes no `config.yml` para `node2`:

```yaml
name: node2

restapi:
  listen: 172.20.0.11:8008
  connect_address: 172.20.0.11:8008

postgresql:
  listen: 172.20.0.11:5432
  connect_address: 172.20.0.11:5432
```

Faça o mesmo para o `node3`, alterando o IP para `172.20.0.12`.

4. Verificar se o PostgreSQL está ativo

Dentro de cada node (node1, node2, node3), execute o seguinte comando para verificar o status do serviço PostgreSQL:

```bash
# Entrar no contêiner do node
docker exec -it node1 bash  # ou node2, node3

# Verificar o status do PostgreSQL
service postgresql status

```

### Conclusão

Usando o Docker para criar uma imagem do `node1`, você evita repetir os comandos de instalação manualmente em cada
contêiner. Essa imagem já contém todas as configurações necessárias, e a única tarefa manual será ajustar os IPs no
arquivo de configuração do Patroni para os novos contêineres `node2` e `node3`.