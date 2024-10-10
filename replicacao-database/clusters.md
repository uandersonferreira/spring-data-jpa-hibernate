# Clusters no PostgreSQL

**Requisitos:** ter o Docker instalado.  
**Recomendação:** sistema operacional Linux.

Instalação do Docker no
Linux: [https://docs.docker.com/engine/install/ubuntu/](https://docs.docker.com/engine/install/ubuntu/)

## Criar contêineres Docker

```bash
#Crianod uma red para os nodes/containers
docker network create --subnet=172.20.0.0/16 clusternet

docker run -itd --name=node1 --net clusternet --ip 172.20.0.10 ubuntu:20.04

docker run -itd --name=node2 --net clusternet --ip 172.20.0.11 ubuntu:20.04

docker run -itd --name=node3 --net clusternet --ip 172.20.0.12 ubuntu:20.04
```

| Característica                  | Valor          | Descrição                                                                             |
|---------------------------------|----------------|---------------------------------------------------------------------------------------|
| Endereço de Rede                | 172.20.0.0     | O primeiro endereço da rede, não atribuído a nenhum host.                             |
| Máscara de Sub-rede             | 255.255.0.0    | Define a parte da rede e a parte do host em um endereço IP.                           |
| Notação CIDR                    | /16            | Indica que os primeiros 16 bits do endereço IP representam a rede.                    |
| Número de Bits de Rede          | 16             | Quantidade de bits utilizados para identificar a rede.                                |
| Número de Bits de Host          | 16             | Quantidade de bits utilizados para identificar os hosts em uma rede.                  |
| Número de Sub-redes             | 1              | A rede não está subdividida em sub-redes menores.                                     |
| Endereço de Broadcast           | 172.20.255.255 | O último endereço da rede, utilizado para enviar pacotes para todos os hosts da rede. |
| Primeiro Endereço Utilizável    | 172.20.0.1     | O primeiro endereço que pode ser atribuído a um host.                                 |
| Último Endereço Utilizável      | 172.20.255.254 | O último endereço que pode ser atribuído a um host.                                   |
| Número Total de Endereços       | 65.536         | Número total de endereços IP na rede.                                                 |
| Número de Endereços Utilizáveis | 65.534         | Número de endereços IP disponíveis para atribuição a hosts.                           |

## Configuração node1, node2, node3 (repetir os passos nos 3 contêineres)
> Caso queira clonar a imagem do node1 para os outros node e depois so trocar o IP
> Leia o readme [Replicando nodes com docker](replicando-nodes-docker.md).

```bash
docker exec -it node1 bash 

apt update

apt upgrade
```

### Instalação do PostgreSQL

```bash
apt-get install lsb-release wget nano postgresql -y

service postgresql stop

ln -s /usr/lib/postgresql/12/bin/* /usr/sbin/

nano /etc/postgresql/12/main/pg_hba.conf
```

Adicionar a linha:

```
host    all             all             172.20.0.0/16            md5
```


Reiniciar o serviço:

```bash
service postgresql status

service postgresql start

service postgresql restart

```

### Instalação do Python

```bash
apt-get install python3-pip python3-dev libpq-dev -y

pip3 install --upgrade pip
```

### Instalação do Patroni

```bash
pip install patroni
pip install python-etcd
pip install psycopg2
```

## Configuração do Patroni

> Leia o readme [sobre Patroni](patroni-conf-disponibilizade.md) para entender as configurações feitas.

```bash
mkdir /etc/patroni

cd /etc/patroni/

nano patroni.yml
```

Adicionar o seguinte conteúdo ao arquivo `patroni.yml`:

```yaml
scope: postgres
namespace: /db/
name: node1

restapi:
  listen: 172.20.0.10:8008
  connect_address: 172.20.0.10:8008

etcd:
  host: 172.20.0.13:2379 #IP do NODE ETCD1

bootstrap:
  dcs:
    ttl: 30
    loop_wait: 10
    retry_timeout: 10
    maximum_lag_on_failover: 1048576
    postgresql:
      use_pg_rewind: true
  initdb:
    - encoding: UTF8
    - data-checksums

  pg_hba:  # Add following lines to pg_hba.conf after running 'initdb'
    - host replication replicator 127.0.0.1/32 md5
    - host replication replicator 172.20.0.10/0 md5
    - host replication replicator 172.20.0.11/0 md5
    - host replication replicator 172.20.0.12/0 md5
    - host all all 0.0.0.0/0 md5
    - host all all 172.20.0.0/16 md5

  users:
    admin:
      password: admin
      options:
        - createrole
        - createdb

postgresql:
  listen: 172.20.0.10:5432
  connect_address: 172.20.0.10:5432
  data_dir: /data/patroni
  pgpass: /tmp/pgpass0
  authentication:
    replication:
      username: replicator
      password: rep-pass
    superuser:
      username: postgres
      password: zalando
    rewind:  # Has no effect on postgres 10 and lower
      username: rewind_user
      password: rewind_password
  parameters:
    unix_socket_directories: '.'

tags:
  nofailover: false
  noloadbalance: false
  clonefrom: false
  nosync: false
```


**Nota:** para os contêineres `node2` e `node3`, é necessário atualizar o nome e o IP no código acima.

Criar o diretório e ajustar permissões:

```bash
mkdir -p /data/patroni

chown postgres:postgres /data/patroni

chmod 700 /data/patroni
```

- O flag `-p` garante que todos os diretórios intermediários (se não existirem) sejam criados automaticamente.
- `chown postgres:postgres` altera o proprietário do diretório /data/patroni para o usuário e grupo **postgres**
- **`chmod 700 /data/patroni`**
    * **Proprietário (postgres):** Tem permissão total (ler, escrever e executar). Isso significa que o usuário postgres
      pode fazer qualquer coisa dentro desse diretório.
    * **Grupo (postgres):** Não tem nenhuma permissão. Nem mesmo membros do grupo postgres podem acessar esse diretório.
    * **Outros:** Também não tem nenhuma permissão. Nenhum outro usuário do sistema pode acessar esse diretório.

> Leia o readme [sobre permissões no linux](entendendo-permissoes-linux.md) para saber mais.

### Create a Service Patroni 

````bash
nano /etc/systemd/system/patroni.service

````

```bash 
[Unit]
Description=High availability PostgreSQL Cluster
After=syslog.target network.target

[Service]
Type=simple

User=postgres
Group=postgres

ExecStart=/usr/local/bin/patroni /etc/patroni/patroni.yml
KillMode=process
TimeoutSec=30
Restart=no

[Install]
WantedBy=multi-user.target
```

apt install systemctl

systemctl daemon-reload

systemctl enable patroni

systemctl status patroni

systemctl start patroni
systemctl restart patroni


Repetir todo o processo para `node2` e `node3`.

## Configuração node4 com `etcd`

Aqui está sendo configurado e executado um container Docker com a imagem do **etcd** para um novo nó (`node4`) em um
cluster distribuído. O `etcd` é um banco de dados chave-valor distribuído, frequentemente usado em sistemas de alta
disponibilidade para armazenar dados de configuração, coordenar serviços, ou como backend para sistemas como Kubernetes.

> Leia o readme [sobre etcd](etcd.md) para saber mais.


docker run -itd --name=node4 --net clusternet --ip 172.20.0.13 ubuntu:20.04

docker exec -it node4 bash

apt update

apt upgrade

apt install etcd -y

apt install nano -y

nano /etc/default/etcd

```bash
ETCD_VERSION=v3
ETCD_LISTEN_PEER_URLS="http://172.20.0.13:2380"
ETCD_LISTEN_CLIENT_URLS="http://localhost:2379,http://127.0.0.1:2379,http://172.20.0.13:2379"
ETCD_INITIAL_ADVERTISE_PEER_URLS="http://172.20.0.13:2380"
ETCD_INITIAL_CLUSTER="default=http://172.20.0.13:2380"
ETCD_ADVERTISE_CLIENT_URLS="http://172.20.0.13:2379"
ETCD_INITIAL_CLUSTER_TOKEN="node4"
ETCD_INITIAL_CLUSTER_STATE="new"
```

apt install systemctl -y

systemctl enable etcd
systemctl start etcd
systemctl restart etcd
systemctl status etcd


## Configuração node5

```bash
docker run -itd --name=node5 --net clusternet --ip 172.20.0.14 ubuntu:20.04

docker exec -it node5 bash

apt update 

apt upgrade

apt install haproxy nano -y

```

Configuração do HAProxy:

```bash
nano /etc/haproxy/haproxy.cfg
```

Adicionar o seguinte conteúdo:

```
global
    maxconn 100

defaults
    log global
    mode tcp
    retries 2
    timeout client 30m
    timeout connect 4s
    timeout server 30m
    timeout check 5s

listen stats
    mode http
    bind *:7000
    stats enable
    stats uri /
    
    
listen postgres
    bind *:5000
    option httpchk
    http-check expect status 200
    default-server inter 3s fall 3 rise 2 on-marked-down shutdown-sessions
    server postgresql_172.20.0.10_5432 172.20.0.10:5432 maxconn 100 check port 8008
    server postgresql_172.20.0.11_5432 172.20.0.11:5432 maxconn 100 check port 8008
    server postgresql_172.20.0.12_5432 172.20.0.12:5432 maxconn 100 check port 8008

```

> Leia o readme [sobre o HAProxy](haproxy-load-balance.md) para saber mais.

Reiniciar o HAProxy:

```bash
service haproxy restart
```

## Executar o Patroni

Entrar em cada um dos três contêineres `node1`, `node2` e `node3` e executar o comando:

```bash
docker exec -it node1 bash


/usr/local/bin/patroni /etc/patroni/config.yml
```

## Verificação da instalação

Acessar pelo navegador e verificar se os três nós aparecem:

```plaintext
http://172.20.0.14:7000
```

## Exemplos de ajuda

- [Snapshooter: Cluster PostgreSQL com Patroni](https://snapshooter.com/learn/postgresql/postgresql-cluster-patroni)
- [Ask Vikram: Configuração de cluster altamente disponível no PostgreSQL usando Patroni e HAProxy](https://www.askvikram.com/set-up-a-highly-available-postgresql-cluster-using-patroni-and-haproxy/)

___

O **Patroni** e o **HAProxy** são frequentemente usados juntos em cenários de alta disponibilidade para bancos de dados,
mas eles desempenham papéis diferentes. Vamos explorar as funções de cada um e como eles se complementam.

### 1. O que é o Patroni?

**Patroni** é uma ferramenta de **gerenciamento de alta disponibilidade** especificamente criada para clusters de bancos
de dados PostgreSQL. Ele cuida da parte de replicação, failover e gerenciamento automático de nós no cluster. O Patroni
monitora o estado do líder (servidor primário) e dos seguidores (servidores secundários) e, em caso de falha no líder,
promove automaticamente um dos seguidores a líder, garantindo a continuidade do serviço.

**Funções principais do Patroni:**

- **Gerenciamento de failover:** Se o líder falhar, o Patroni promove automaticamente outro servidor.
- **Replicação automática:** Configura e mantém a replicação entre os servidores PostgreSQL no cluster.
- **Monitoramento de nós:** Mantém o status de cada servidor no cluster e se certifica de que eles estejam em sincronia.

Patroni pode usar o etcd, Consul, ou ZooKeeper como sistema de controle distribuído para coordenar essa promoção
automática e garantir que apenas um nó seja o líder a qualquer momento.

### 2. O que é o HAProxy?

Por outro lado, o **HAProxy** é um **balanceador de carga**. Ele não se preocupa com a replicação ou promoção de nós,
mas sim em **distribuir as conexões** de forma eficiente entre os servidores que estão disponíveis. Ele faz verificações
periódicas (health checks) para garantir que está redirecionando o tráfego para os servidores saudáveis.

**Funções principais do HAProxy:**

- **Balanceamento de carga:** Distribui o tráfego de clientes entre vários servidores disponíveis.
- **Verificação de saúde (health checks):** Certifica-se de que os servidores estão funcionando corretamente e, caso um
  deles falhe, o HAProxy deixa de redirecionar conexões para ele.
- **Proxy reverso:** Atua como intermediário entre os clientes e os servidores de banco de dados.

### Diferenças e Relação entre Patroni e HAProxy

- **Gerenciamento de Alta Disponibilidade vs. Balanceamento de Carga:**
    - **Patroni** cuida do **gerenciamento da alta disponibilidade** do PostgreSQL, garantindo que sempre haja um nó
      líder disponível. Ele lida com a promoção de um servidor secundário quando o primário falha.
    - **HAProxy** cuida do **balanceamento de carga** e atua como um intermediário que distribui as conexões de forma
      inteligente entre os servidores no cluster.

- **Cooperação entre Patroni e HAProxy:**
    - **Patroni** gerencia o estado do cluster PostgreSQL e faz com que um servidor sempre seja o líder.
    - **HAProxy** pode ser configurado para redirecionar as conexões apenas para o líder. Ele usa as verificações de
      saúde para garantir que o tráfego vá para o servidor PostgreSQL correto, evitando redirecionar para seguidores que
      só podem ser usados para leitura (dependendo do cenário).

Em resumo, **Patroni** é a peça que mantém o cluster de PostgreSQL sempre ativo e gerencia as promoções automáticas em
caso de falhas, enquanto **HAProxy** distribui o tráfego entre os servidores e garante que as conexões cheguem ao
servidor correto.

### Exemplo de uso em conjunto:

- O **Patroni** está monitorando seu cluster PostgreSQL, garantindo que haja sempre um servidor líder.
- O **HAProxy** distribui as conexões dos clientes entre os servidores, redirecionando-as para o líder, que aceita
  leituras e gravações, ou para os seguidores, que podem ser usados para leituras.

Assim, enquanto o **Patroni** garante que o cluster continue funcionando corretamente, o **HAProxy** distribui o tráfego
de maneira eficiente, fornecendo uma solução robusta de alta disponibilidade.