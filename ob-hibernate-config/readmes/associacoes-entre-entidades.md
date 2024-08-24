
## **Associações entre Entidades**

---

#### **1. One To One - Employee and Direction**

- **Conceito:** Um `Employee` está associado a exatamente uma `Direction` e vice-versa.

    - **@OneToOne**: Define a relação de um-para-um.
    - **@JoinColumn**: Especifica a coluna na tabela da entidade "dona" (Employee) que armazena a chave estrangeira para a entidade associada (Direction).
    - **@JoinTable**: Define uma tabela de junção para o relacionamento, geralmente usada quando se opta por uma abordagem de tabela intermediária.
    - **@PrimaryKeyJoinColumn**: Indica que a chave primária da entidade "dona" também serve como chave estrangeira na entidade associada.
    - **@MapsId**: Mapeia a chave primária da entidade associada diretamente para a chave primária da entidade "dona".

  **Esquema:**
    ```
    Employee ----- 1:1 ----- Direction
        |
        | @OneToOne
        | @JoinColumn
        | @JoinTable
        | @PrimaryKeyJoinColumn
        | @MapsId
    ```

---

#### **2. One To Many - Employee and Car**

- **Unidirecional:**

    - **@OneToMany**: Define a relação de um-para-muitos na classe "dona" (Employee).
    - **@JoinColumn**: (Opcional) Usado na classe associada (Car) para especificar a coluna de chave estrangeira. Normalmente não é necessário em um relacionamento unidirecional.

  **Esquema:**
    ```
    Employee ----- 1:N ----- Car
        |
        | @OneToMany
    ```

- **Bidirecional:**

    - **@OneToMany**: Define a relação de um-para-muitos na classe "dona" (Employee).
    - **@ManyToOne**: Define a relação de muitos-para-um na classe associada (Car), referenciando a entidade "dona" (Employee).
    - **mappedBy**: Usado na anotação `@OneToMany` para indicar que a propriedade de mapeamento é gerenciada pela classe associada.

  **Esquema:**
    ```
    Employee ----- 1:N ----- Car
        |                     |
        | @OneToMany          | @ManyToOne
        | mappedBy            |
    ```

---

#### **3. Many To One - Employee and Company**

- **Unidirecional:**

    - **@ManyToOne**: Define a relação de muitos-para-um na classe "dona" (Employee), referenciando a entidade associada (Company).

  **Esquema:**
    ```
    Employee ----- N:1 ----- Company
        |
        | @ManyToOne
    ```

- **Bidirecional:**

    - **@ManyToOne**: Define a relação de muitos-para-um na classe "dona" (Employee).
    - **@OneToMany**: Define a relação de um-para-muitos na classe associada (Company), mantendo uma coleção de instâncias da entidade "dona" (Employee).
    - **mappedBy**: Usado na anotação `@OneToMany` para indicar que a propriedade de mapeamento é gerenciada pela classe associada.

  **Esquema:**
    ```
    Employee ----- N:1 ----- Company
        |                    |
        | @ManyToOne         | @OneToMany
        |                    | mappedBy
    ```

---

#### **4. Many To Many - Employee and Project**

- **Unidirecional:**

    - **@ManyToMany**: Define a relação de muitos-para-muitos na classe "dona" (Employee).
    - **@JoinTable**: Especifica a tabela de junção que armazena as chaves estrangeiras para as entidades associadas.

  **Esquema:**
    ```
    Employee ----- N:N ----- Project
        |
        | @ManyToMany
        | @JoinTable
    ```

- **Bidirecional:**

    - **@ManyToMany**: Define a relação de muitos-para-muitos na classe "dona" (Employee).
    - **@ManyToMany**: Define a relação de muitos-para-muitos na classe associada (Project), referenciando a entidade "dona" (Employee).
    - **mappedBy**: Usado na anotação `@ManyToMany` para indicar que a propriedade de mapeamento é gerenciada pela classe "dona".

  **Esquema:**
    ```
    Employee ----- N:N ----- Project
        |                    |
        | @ManyToMany        | @ManyToMany
        |                    | mappedBy
        | @JoinTable
    ```

---
