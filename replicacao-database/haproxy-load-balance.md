Olá! Eu sou o Uanderson, um desenvolvedor Backend Java em formação, e hoje vamos explorar juntos o que é o **HAProxy** e
como configurá-lo. Se você é um desenvolvedor júnior e está começando a entender conceitos de balanceamento de carga e
alta disponibilidade, fique tranquilo! Vamos explicar de uma maneira simples, com exemplos práticos, e no final você vai
ter uma boa noção de como configurar o HAProxy para gerenciar conexões com o PostgreSQL.

### O que é o HAProxy?

Imagine que você tem um restaurante super movimentado e várias mesas disponíveis. Agora, para garantir que o atendimento
seja rápido e eficiente, você coloca um recepcionista na entrada, que direciona cada cliente para a mesa disponível mais
rapidamente. No mundo dos servidores, o **HAProxy** é como esse recepcionista.

O **HAProxy (High Availability Proxy)** é um software utilizado para realizar o **balanceamento de carga** e garantir a
**alta disponibilidade** de serviços, distribuindo o tráfego entre vários servidores. Ele funciona como um intermediário
entre os clientes (quem acessa seu serviço) e os servidores (onde sua aplicação ou banco de dados está rodando),
garantindo que o sistema possa lidar com mais solicitações de forma eficiente.

### Por que usar o HAProxy?

Quando você tem vários servidores para uma aplicação (por exemplo, um cluster de bancos de dados PostgreSQL), o HAProxy
distribui as conexões de maneira inteligente. Isso significa que, se um dos servidores falhar ou estiver sobrecarregado,
ele redireciona o tráfego para os outros que estão saudáveis. É como um gerente de tráfego, evitando gargalos e
garantindo que sua aplicação continue funcionando, mesmo se algo der errado.

### Explicando a Configuração do HAProxy

Agora vamos mergulhar na configuração que você forneceu. Não se assuste com o formato, pois vamos passar por cada parte,
explicando o que cada linha faz. Vamos começar com a edição do arquivo de configuração:

```bash
nano /etc/haproxy/haproxy.cfg
```

Esse comando abre o arquivo de configuração do HAProxy para edição. Dentro dele, adicionamos o seguinte:

#### Seção Global

```
global
    maxconn 100
```

Aqui está a configuração global do HAProxy. A opção `maxconn 100` define o número máximo de conexões simultâneas que o
HAProxy pode gerenciar. Basicamente, é o limite de clientes que ele pode atender ao mesmo tempo. Se você pensar no
restaurante de novo, é como definir que o recepcionista só pode lidar com 100 clientes ao mesmo tempo.

#### Seção Defaults

```
defaults
    log global
    mode tcp
    retries 2
    timeout client 30m
    timeout connect 4s
    timeout server 30m
    timeout check 5s
```

Esta seção configura os padrões que serão aplicados a todas as "regras" que criarmos mais tarde. Vamos entender cada
linha:

- **log global**: Isso indica que os logs vão seguir as regras definidas na seção global.
- **mode tcp**: O HAProxy vai operar no modo TCP. Isso significa que ele está lidando com conexões em nível de
  transporte, ideal para protocolos como o do PostgreSQL.
- **retries 2**: Se o HAProxy falhar ao se conectar a um servidor, ele vai tentar novamente duas vezes antes de
  considerar a conexão como perdida.
- **timeout client 30m** e **timeout server 30m**: Esses valores indicam que tanto o cliente quanto o servidor podem
  ficar conectados por até 30 minutos sem atividade antes de serem desconectados.
- **timeout connect 4s**: Define que o HAProxy vai esperar até 4 segundos para estabelecer uma conexão com o servidor.
- **timeout check 5s**: O tempo máximo que o HAProxy vai esperar para verificar se um servidor está ativo.

#### Seção Listen - Stats

```
listen stats
    mode http
    bind *:7000
    stats enable
    stats uri /
```

Essa seção configura uma página de estatísticas no HAProxy. Vamos ver o que isso significa:

- **listen stats**: Isso cria um bloco de configuração chamado "stats" (estatísticas).
- **mode http**: O HAProxy vai usar o protocolo HTTP para essa seção.
- **bind *:7000**: O HAProxy vai escutar (ou "bindar") no endereço IP de qualquer interface (`*`) na porta 7000.
- **stats enable**: Ativa a página de estatísticas.
- **stats uri /**: Define que você pode acessar as estatísticas do HAProxy através da URL raiz ("/").

Ou seja, você poderá acessar as estatísticas do HAProxy digitando o endereço IP seguido da porta 7000 no navegador.

#### Seção Listen - PostgreSQL

```
listen postgres
    bind *:5000
    option httpchk
    http-check expect status 200
    default-server inter 3s fall 3 rise 2 on-marked-down shutdown-sessions
    server postgresql_172.20.0.10_5432 172.20.0.10:5432 maxconn 100 check port 8008
    server postgresql_172.20.0.11_5432 172.20.0.11:5432 maxconn 100 check port 8008
    server postgresql_172.20.0.12_5432 172.20.0.12:5432 maxconn 100 check port 8008
```

Agora estamos configurando o balanceamento de carga para o PostgreSQL. Vamos quebrar isso:

- **listen postgres**: Este bloco configura a seção para lidar com conexões para o PostgreSQL.
- **bind *:5000**: O HAProxy vai escutar na porta 5000 para redirecionar conexões de PostgreSQL.
- **option httpchk**: Ativa a verificação de saúde via HTTP, para garantir que os servidores estejam respondendo.
- **http-check expect status 200**: O HAProxy espera um status 200 (OK) dos servidores como sinal de que estão
  saudáveis.
- **default-server inter 3s fall 3 rise 2**: Esta linha configura o comportamento padrão para os servidores. O HAProxy
  vai verificar a saúde dos servidores a cada 3 segundos (`inter 3s`). Se o servidor falhar 3 vezes consecutivas, ele
  será considerado inativo (`fall 3`), e se ele passar em 2 verificações consecutivas, será considerado ativo
  novamente (`rise 2`).
- **on-marked-down shutdown-sessions**: Se o HAProxy marcar um servidor como inativo, ele vai encerrar todas as sessões
  abertas naquele servidor.

Agora, temos três servidores PostgreSQL configurados:

- **server postgresql_172.20.0.10_5432 172.20.0.10:5432 maxconn 100 check port 8008**
- **server postgresql_172.20.0.11_5432 172.20.0.11:5432 maxconn 100 check port 8008**
- **server postgresql_172.20.0.12_5432 172.20.0.12:5432 maxconn 100 check port 8008**

Cada um deles pode aceitar até 100 conexões simultâneas (`maxconn 100`) e será verificado na porta 8008 para garantir
que está funcionando corretamente.

### Conclusão

Neste tutorial, você aprendeu como o **HAProxy** pode ser utilizado para balancear a carga entre vários servidores de
PostgreSQL, garantindo alta disponibilidade e desempenho. Com essas configurações, você pode monitorar o estado dos seus
servidores e distribuir as conexões de forma inteligente. Se um servidor falhar, o HAProxy redireciona automaticamente
as conexões para os servidores saudáveis, garantindo que seu sistema continue operando sem interrupções.

