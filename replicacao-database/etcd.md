# Etcd

O **etcd** é um banco de dados chave-valor distribuído e consistente, projetado para armazenar dados críticos para a
configuração de sistemas distribuídos. Ele é comumente usado para coordenar e gerenciar serviços de alta
disponibilidade, garantir consistência de dados e fornecer uma maneira segura de armazenar informações sensíveis em um
cluster de servidores.

### Principais características do etcd:

1. **Consistência forte**: O etcd usa um protocolo de consenso chamado **Raft**, que garante que, mesmo em um cluster
   distribuído, todos os nós tenham uma visão consistente do estado dos dados. Isso é essencial para sistemas que
   precisam de alta disponibilidade e resiliência a falhas.

2. **Alta disponibilidade**: O etcd é projetado para funcionar em um cluster de servidores. Se um nó falhar, outros nós
   podem assumir sua função sem perda de dados, garantindo que o sistema continue funcionando.

3. **Distribuído e tolerante a falhas**: Por ser distribuído, o etcd pode ser replicado entre vários servidores. Isso o
   torna resiliente a falhas de hardware ou software, garantindo que os dados fiquem sempre disponíveis.

4. **Chave-valor**: Os dados no etcd são armazenados em pares chave-valor, o que significa que você pode armazenar
   qualquer tipo de informação (desde strings simples até objetos complexos) em um formato eficiente.

5. **Baixa latência**: É otimizado para leitura e escrita de dados rápidas, tornando-o ideal para armazenar
   configurações dinâmicas ou dados que precisam ser acessados em tempo real.

6. **Segurança**: Ele suporta autenticação baseada em usuários e comunicação criptografada via TLS, garantindo que os
   dados estejam protegidos em trânsito e em repouso.

### Onde o etcd é utilizado?

O **etcd** é amplamente utilizado em sistemas de orquestração de containers, como o **Kubernetes**, onde é responsável
por armazenar o estado do cluster, a configuração dos serviços e informações sobre os containers em execução. Ele também
é utilizado em outras plataformas de infraestrutura como serviços de monitoramento, sistemas de armazenamento
distribuído e serviços de descoberta de microserviços.

Por exemplo:

- **Kubernetes**: O etcd armazena o estado completo do cluster. Toda vez que um nó é adicionado, removido ou atualizado,
  essa informação é armazenada no etcd.
- **Coordenação de serviços**: O etcd pode ser usado para coordenar serviços em um sistema distribuído, garantindo que
  as instâncias de um serviço saibam o estado umas das outras.

### Por que usar o etcd?

- **Garantia de Consistência**: Em sistemas distribuídos, é crucial garantir que todas as partes do sistema tenham a
  mesma visão dos dados, mesmo em cenários de falha. O etcd garante essa consistência.
- **Simplicidade e eficiência**: A interface chave-valor é simples de usar e fácil de integrar em diferentes sistemas.
- **Resiliência e recuperação de falhas**: O etcd permite que os sistemas continuem operando mesmo quando ocorrem falhas
  de componentes, graças à sua capacidade de replicação e recuperação.

### Como o etcd funciona?

Imagine que você tem um cluster de três servidores. Todos os dados armazenados no etcd são replicados entre esses três
servidores. Se um servidor falha, os outros dois continuam funcionando normalmente e garantindo que os dados estejam
disponíveis. Assim que o servidor falho é restaurado, ele se reconecta ao cluster e recebe uma cópia dos dados
atualizados.

Esses mecanismos de replicação e recuperação de falhas são controlados pelo protocolo **Raft**, que garante que todos os
servidores tenham uma cópia consistente dos dados.

### Resumo:

Em essência, o **etcd** é uma peça fundamental para manter a confiabilidade e a consistência de dados em sistemas
distribuídos, sendo muito utilizado para armazenar dados de configuração, coordenar a operação de serviços distribuídos
e garantir a alta disponibilidade de sistemas complexos.

___

Aqui está sendo configurado e executado um container Docker com a imagem do **etcd** para um novo nó (`node4`) em um
cluster distribuído. O `etcd` é um banco de dados chave-valor distribuído, frequentemente usado em sistemas de alta
disponibilidade para armazenar dados de configuração, coordenar serviços, ou como backend para sistemas como Kubernetes.

### Vamos detalhar cada parte:

1. **Definição da variável `NODE4`:**
   ```bash
   export NODE4=172.20.0.13
   ```
    - Aqui, você está definindo uma variável de ambiente chamada `NODE4`, que contém o endereço IP do nó (neste caso,
      `172.20.0.13`).
    - Essa variável será usada mais adiante no comando para referenciar o IP do nó, o que facilita a manutenção do
      script.

2. **Execução do container Docker:**
   ```bash
   docker run -itd --net clusternet --ip 172.20.0.13 --name node4 quay.io/coreos/etcd:latest
   ```
    - **`docker run -itd`**: Inicia um container Docker em modo interativo (`-i`), com pseudo-TTY (`-t`), rodando em
      segundo plano (`-d`).
    - **`--net clusternet`**: Coloca o container na rede Docker chamada `clusternet`, que provavelmente foi criada
      anteriormente para permitir a comunicação entre os nós do cluster.
    - **`--ip 172.20.0.13`**: Define o IP do container como `172.20.0.13`, que foi atribuído a este nó.
    - **`--name node4`**: Nomeia o container como `node4` para fácil identificação.
    - **`quay.io/coreos/etcd:latest`**: Esta é a imagem do `etcd` sendo utilizada, vinda do repositório `quay.io`. A
      versão mais recente da imagem (`latest`) está sendo usada.

3. **Configurações específicas do etcd dentro do container:**
   ```bash
   /usr/local/bin/etcd --data-dir=/etcd-data --name node4
   ```
    - **`/usr/local/bin/etcd`**: Comando que executa o `etcd` dentro do container.
    - **`--data-dir=/etcd-data`**: Define o diretório onde o `etcd` armazenará seus dados, neste caso, `/etcd-data`.
    - **`--name node4`**: Nomeia este membro do cluster como `node4`.

4. **URLs de comunicação entre os nós do cluster:**
   ```bash
   --initial-advertise-peer-urls http://${NODE4}:2380 --listen-peer-urls http://0.0.0.0:2380
   ```
    - **`--initial-advertise-peer-urls http://${NODE4}:2380`**: Informa a outros nós do cluster como eles podem se
      conectar a este nó. Aqui, ele está anunciando que o `node4` está acessível pelo IP `172.20.0.13` (armazenado na
      variável `NODE4`) na porta `2380`, que é usada para comunicação entre pares (nós do cluster).
    - **`--listen-peer-urls http://0.0.0.0:2380`**: Informa ao `etcd` para escutar na porta `2380` em todas as
      interfaces de rede (`0.0.0.0`) para conexões de outros nós.

5. **URLs de comunicação com clientes:**
   ```bash
   --advertise-client-urls http://${NODE4}:2379 --listen-client-urls http://0.0.0.0:2379
   ```
    - **`--advertise-client-urls http://${NODE4}:2379`**: Informa os clientes (por exemplo, outros serviços ou
      aplicações) sobre como eles podem se conectar a este nó para fazer operações de leitura e escrita. Ele está
      anunciando que o nó está acessível no IP `172.20.0.13` na porta `2379`, que é a porta padrão para clientes `etcd`.
    - **`--listen-client-urls http://0.0.0.0:2379`**: Configura o `etcd` para escutar na porta `2379` em todas as
      interfaces de rede (`0.0.0.0`) para conexões de clientes.

6. **Configuração do cluster inicial:**
   ```bash
   --initial-cluster node4=http://${NODE4}:2380
   ```
    - **`--initial-cluster node4=http://${NODE4}:2380`**: Define a configuração inicial do cluster. Aqui, o cluster está
      sendo iniciado com apenas um nó, `node4`, e ele pode ser acessado via `http://${NODE4}:2380`.

### Resumo:

Este comando está configurando o **`etcd`** para ser executado como um container Docker em um nó chamado `node4` com o
IP `172.20.0.13`. O nó está configurado para ser parte de um cluster distribuído e está ouvindo na porta `2380` para
comunicação entre os nós do cluster e na porta `2379` para comunicação com clientes externos.