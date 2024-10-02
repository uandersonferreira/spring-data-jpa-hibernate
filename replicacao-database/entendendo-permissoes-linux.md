### Entendendo as Permissões no Linux com `chmod 700 /data/patroni`

Imagine que você está criando um cofre onde só **você** tem a chave, e ninguém mais pode sequer chegar perto. No mundo
Linux, o comando `chmod 700 /data/patroni` faz algo bem parecido para o diretório `/data/patroni`: ele tranca o
diretório, dando acesso total apenas para o usuário proprietário (neste caso, o `postgres`), e deixando todos os outros
usuários e grupos sem permissão.

Vamos quebrar isso em partes para ficar mais claro.

### O que são permissões?

As permissões no Linux controlam quem pode fazer o quê com arquivos e diretórios. Elas funcionam como regras que dizem
se um usuário pode ler um arquivo, alterar seu conteúdo ou executar um programa. Essas permissões são divididas em três
categorias:

1. **Proprietário:** O usuário que criou ou é dono do arquivo ou diretório.
2. **Grupo:** Um conjunto de usuários que compartilham permissões sobre arquivos.
3. **Outros:** Todos os outros usuários no sistema que não são nem o proprietário nem parte do grupo.

Agora, vamos ao exemplo do comando `chmod 700 /data/patroni` e como ele afeta cada uma dessas categorias:

- **Proprietário (postgres):** Tem permissão total para ler, escrever e executar (`7` significa ler, escrever e
  executar).
    - O proprietário pode entrar no diretório (`x` - executar), ver os arquivos que estão dentro (`r` - ler) e modificar
      ou criar novos arquivos (`w` - escrever).
- **Grupo (postgres):** Não tem permissão nenhuma (`0`).
    - Mesmo que outros usuários façam parte do grupo `postgres`, eles não podem ler, escrever ou executar nada nesse
      diretório.
- **Outros:** Também não têm nenhuma permissão (`0`).
    - Isso significa que qualquer usuário que não seja o `postgres` ou parte do grupo `postgres` não pode acessar o
      diretório.

Essa configuração é como se você fosse o único que tem a chave para abrir esse "cofre". Nem amigos (grupo) nem
estranhos (outros) podem sequer tocar.

### Tipos de permissões

Agora que você já entende as três categorias (proprietário, grupo, outros), vamos falar sobre os três tipos de
permissões que você pode definir:

1. **Ler (r):** Permite ver o conteúdo do arquivo ou listar o conteúdo do diretório.
2. **Escrever (w):** Permite modificar o arquivo ou adicionar/excluir arquivos dentro de um diretório.
3. **Executar (x):** Permite executar um arquivo (se for um programa) ou entrar no diretório.

### O que o número `700` representa?

O Linux usa uma notação numérica chamada **notação octal** para simplificar a definição dessas permissões. Cada
categoria (proprietário, grupo e outros) recebe um número entre 0 e 7 que define o que eles podem fazer. Aqui está o que
esses números significam:

| Valor | Permissões               | O que significa                    |
|-------|--------------------------|------------------------------------|
| 0     | Nenhuma permissão        | Não pode ler, escrever ou executar |
| 1     | Executar                 | Pode executar o arquivo            |
| 2     | Escrever                 | Pode modificar ou criar arquivos   |
| 3     | Escrever e executar      | Pode modificar e executar          |
| 4     | Ler                      | Pode ver o conteúdo                |
| 5     | Ler e executar           | Pode ver e executar                |
| 6     | Ler e escrever           | Pode ver e modificar               |
| 7     | Ler, escrever e executar | Pode ver, modificar e executar     |

Quando você vê `700`, significa:

- **7** para o proprietário: O dono do diretório pode ler, escrever e executar.
- **0** para o grupo: O grupo não tem permissão.
- **0** para outros: Outros usuários não têm permissão.

### Um exemplo visual: Cofre pessoal

Imagine que você está em uma casa com três tipos de pessoas: você (o proprietário), sua família (grupo) e visitantes (
outros). Agora, você decide trancar um quarto (o diretório `/data/patroni`), mas só você tem a chave. Sua família pode
estar na casa, mas não pode abrir a porta, e os visitantes nem podem se aproximar.

Com `chmod 700`, é como se você colocasse um cadeado na porta e guardasse a chave só para você. Ninguém mais pode abrir,
olhar dentro ou sequer mexer nas coisas lá dentro.

### Conclusão

As permissões no Linux podem parecer complicadas no início, mas uma vez que você entende a lógica por trás delas, é como
configurar um sistema de segurança altamente personalizável para seus arquivos e diretórios. No caso do comando
`chmod 700`, você está criando um ambiente onde apenas o usuário dono do diretório tem total controle, garantindo que
nenhum outro usuário (nem mesmo aqueles do grupo) possa acessar o conteúdo.

