Jakarta Persistence (JPA) define um conjunto de callbacks que permitem executar código
em momentos específicos do ciclo de vida de uma entidade. Essas anotações são úteis para garantir
que as entidades estejam em um estado correto antes e depois de operações como persistência, atualização e remoção.

### **1. @PrePersist**

- **Descrição:** Executado **antes** que a operação de persistência do EntityManager seja realmente executada ou
  propagada. Este método é chamado de forma síncrona com a operação de persistência.
- **Detalhes Importantes:** O `@PrePersist` é útil para inicializar ou validar dados antes que uma nova entidade seja
  salva no banco de dados. Um ponto importante é que ele **sempre** é chamado quando você tenta persistir uma nova
  entidade, independentemente de o registro realmente ser inserido (por exemplo, se ocorrer um erro na persistência).

  **Exemplo:**
  ```java
  @PrePersist
  public void prePersist() {
      this.setRegisterDate(LocalDateTime.now());
      this.setEditDate(LocalDateTime.now());
  }
  ```

### **2. @PreRemove**

- **Descrição:** Executado **antes** que a operação de remoção do EntityManager seja realmente executada ou propagada.
  Este método é chamado de forma síncrona com a operação de remoção.
- **Detalhes Importantes:** O `@PreRemove` permite que você execute ações antes de uma entidade ser removida, como
  registrar logs de auditoria ou desassociar relações. Como o método é chamado **antes** da remoção real, você ainda
  pode cancelar a operação, por exemplo, lançando uma exceção.

  **Exemplo:**
  ```java
  @PreRemove
  public void preRemove() {
      System.out.println("preRemove");
  }
  ```

### **3. @PostPersist**

- **Descrição:** Executado **após** a operação de persistência do EntityManager ser realmente executada ou propagada.
  Este método é invocado **depois** que o `INSERT` no banco de dados é executado.
- **Detalhes Importantes:** O `@PostPersist` é útil para executar lógica que depende do fato de que a entidade foi
  efetivamente persistida no banco de dados, como o envio de notificações ou a realização de ações em sistemas externos.
  Ele é chamado **após** o registro ter sido inserido na base, garantindo que a entidade já existe no banco.

  **Exemplo:**
  ```java
  @PostPersist
  public void postPersist() {
      System.out.println("postPersist");
  }
  ```

### **4. @PostRemove**

- **Descrição:** Executado **após** a operação de remoção do EntityManager ser realmente executada ou propagada. Este
  método é chamado de forma síncrona com a operação de remoção.
- **Detalhes Importantes:** O `@PostRemove` permite que você realize ações depois que uma entidade foi removida do banco
  de dados, como limpar caches ou notificar outros sistemas. Como o método é chamado **depois** da remoção, ele garante
  que a entidade já foi excluída.

  **Exemplo:**
  ```java
  @PostRemove
  public void postRemove() {
      System.out.println("postRemove");
  }
  ```

### **5. @PreUpdate**

- **Descrição:** Executado **antes** que a operação de atualização do banco de dados seja realmente executada.
- **Detalhes Importantes:** O `@PreUpdate` é chamado **apenas** se os dados da entidade foram realmente modificados.
  Isso significa que se você tentar atualizar uma entidade sem alterar nenhum de seus campos, esse callback **não** será
  chamado. É útil para atualizar campos como `editDate` ou validar dados antes da atualização.

  **Exemplo:**
  ```java
  @PreUpdate
  public void preUpdate() {
      this.setEditDate(LocalDateTime.now());
  }
  ```

### **6. @PostUpdate**

- **Descrição:** Executado **após** que a operação de atualização do banco de dados seja realmente executada.
- **Detalhes Importantes:** O `@PostUpdate` é útil para realizar ações que precisam ocorrer após a confirmação de que a
  atualização foi bem-sucedida, como a sincronização de dados com sistemas externos ou o envio de notificações.

  **Exemplo:**
  ```java
  @PostUpdate
  public void postUpdate() {
      System.out.println("postUpdate");
  }
  ```

### **7. @PostLoad**

- **Descrição:** Executado **após** uma entidade ter sido carregada no contexto de persistência atual ou ter sido
  atualizada via `refresh`.
- **Detalhes Importantes:** O `@PostLoad` é chamado sempre que uma entidade é carregada do banco de dados, seja por uma
  consulta ou ao navegar por uma associação lazy. Esse callback é especialmente útil para inicializar campos derivados,
  carregar dados auxiliares ou transformar os dados carregados.

  **Exemplo:**
  ```java
  @PostLoad
  public void postLoad() {
      this.fullName = this.firstName + " " + this.lastName;
  }
  ```

### **Resumo das Anotações:**

- **@PrePersist:** Antes da persistência.
- **@PostPersist:** Após a persistência.
- **@PreRemove:** Antes da remoção.
- **@PostRemove:** Após a remoção.
- **@PreUpdate:** Antes da atualização, se houver mudanças.
- **@PostUpdate:** Após a atualização.
- **@PostLoad:** Após o carregamento ou atualização de uma entidade.

