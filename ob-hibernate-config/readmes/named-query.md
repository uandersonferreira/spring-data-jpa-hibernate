### **Conceito de `@NamedQuery` em Hibernate**

`@NamedQuery` é uma anotação utilizada no Hibernate para definir consultas
HQL (Hibernate Query Language) de forma estática e associá-las a uma entidade específica.
Ela permite que as consultas sejam nomeadas e reutilizadas em diferentes partes da aplicação,
promovendo a reutilização de código e facilitando a manutenção.

### **Motivação para o Uso de `@NamedQuery`**

A motivação para um desenvolvedor utilizar `@NamedQuery` em uma classe Java ao trabalhar com Hibernate
está relacionada a vários fatores, incluindo reutilização de consultas, manutenção de código e eficiência
de desempenho. Aqui estão as razões principais:

1. **Reutilização de Consultas**: Ao definir uma `@NamedQuery`, o desenvolvedor cria uma consulta que pode
   ser reutilizada em diferentes partes da aplicação. Isso evita a duplicação de código e facilita a manutenção,
   já que qualquer alteração na consulta precisa ser feita apenas em um único lugar.

2. **Melhoria na Manutenção**: Se a lógica da consulta precisar ser modificada no futuro, a mudança pode ser
   feita diretamente na definição da `@NamedQuery`. Isso simplifica a manutenção, especialmente em aplicações grandes,
   onde a mesma consulta pode ser utilizada em múltiplos lugares.

3. **Separação da Lógica de Consultas**: Usar `@NamedQuery` permite que as consultas sejam definidas de forma
   declarativa fora dos métodos Java, promovendo uma separação clara entre a lógica de negócios e a lógica de
   persistência.
   Isso pode melhorar a clareza do código.

4. **Melhorias de Desempenho**: Algumas implementações do Hibernate podem realizar otimizações em `NamedQueries`,
   como pré-compilar as consultas ou otimizá-las para execução. Embora isso dependa da configuração e da versão
   específica
   do Hibernate, pode haver benefícios de desempenho ao usar `NamedQuery`.

5. **Convenção e Organização**: `@NamedQuery` promove a organização das consultas, especialmente em
   classes de entidade que possuem várias operações relacionadas. Ao utilizar uma `@NamedQuery`, você
   segue uma convenção que facilita a leitura e o entendimento do código, tanto para você quanto para
   outros desenvolvedores que possam trabalhar no projeto.

No exemplo fornecido, a `@NamedQuery` com o nome `"Employee.mostPaid"` e a consulta
`from Employee e where e.salary > 5000` foi criada para buscar todos os empregados que têm um
salário superior a 5000. Ao usar a consulta nomeada no método `findMostPaid()`, o desenvolvedor
evita a necessidade de reescrever a mesma lógica de consulta diretamente no código do método, tornando
o código mais limpo e reutilizável.

A chamada para a `NamedQuery` no método `findMostPaid()` busca a consulta já definida e a executa,
retornando a lista de empregados que atendem ao critério especificado.

### **Sintaxe**

A anotação `@NamedQuery` é colocada diretamente acima da classe de entidade ou no arquivo de
mapeamento XML da entidade. Sua sintaxe básica é:

```java

@Entity
@NamedQuery(
        name = "nome_da_consulta",
        query = "HQL_query"
)
public class EntityName {
    // campos, construtores, métodos, etc.
}
```

**Componentes da Sintaxe:**

1. **`name`**: Define o nome único da consulta. Este nome será usado para referenciar a consulta em outros pontos do
   código.
2. **`query`**: Contém a consulta HQL que será executada. Esta consulta é definida de forma estática e pode incluir
   parâmetros.

### **Nomenclaturas Recomendadas**

Ao nomear uma `@NamedQuery`, é importante seguir convenções que tornam o código mais legível e intuitivo. As
recomendações incluem:

1. **Prefixo com o Nome da Entidade**: É comum prefixar o nome da consulta com o nome da entidade à qual ela
   se refere, por exemplo, `Employee.findAll` ou `Employee.findBySalaryGreaterThan`.

2. **Uso de Verbos Indicativos**: Use verbos que indicam claramente a ação da consulta,
   como `find`, `get`, `count`, `delete`, etc. Isso ajuda a entender a intenção da consulta apenas pelo nome.

3. **Descreva a Ação Claramente**: O nome da consulta deve refletir claramente o critério ou objetivo da consulta.
   Por exemplo, `Employee.findMostPaid` é mais descritivo do que algo genérico como `Employee.find`.

### **Boas Práticas**

1. **Reutilização e Centralização**: Utilize `@NamedQuery` para centralizar consultas que serão usadas em múltiplas
   partes do código, evitando a duplicação e facilitando a manutenção.

2. **Documentação**: Documente as `@NamedQuery` com comentários explicativos sobre o propósito da consulta e os
   parâmetros utilizados, especialmente em projetos grandes onde as consultas podem se tornar complexas.

3. **Testes e Validação**: Teste as consultas definidas por `@NamedQuery` de forma independente para garantir
   que estejam corretas e otimizadas antes de serem amplamente usadas na aplicação.

4. **Limitação de Complexidade**: Mantenha as consultas simples e evite incluir lógica de negócios complexa
   diretamente na consulta. Se necessário, considere dividir consultas complexas em várias `@NamedQuery` ou
   refatorar a lógica para métodos de serviço.

5. **Uso de Parâmetros Nominados**: Prefira o uso de parâmetros nominados (`:paramName`) em vez de
   parâmetros posicionais (`?1`, `?2`, etc.) para melhorar a clareza e evitar erros ao passar os valores.

---