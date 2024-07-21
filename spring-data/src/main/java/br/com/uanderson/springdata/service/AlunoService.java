package br.com.uanderson.springdata.service;

import br.com.uanderson.springdata.orm.Disciplina;
import br.com.uanderson.springdata.orm.Aluno;
import br.com.uanderson.springdata.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Scanner;

@Service
@Transactional
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
    public void menu(Scanner scanner){
        Boolean isTrue = true;
        while (isTrue){
            System.out.println("Qual ação você que executar ? ");
            System.out.println("0 - Voltar ao menu anterior");
            System.out.println("1 - Cadastrar novo Aluno");
            System.out.println("2 - Atualizar Aluno");
            System.out.println("3 - Visualizar todos");
            System.out.println("4 - Deletar um Aluno");
            System.out.println("5 - visualizar um Aluno");
            System.out.print("Opção: ");

            int opcao = scanner.nextInt();
            scanner.reset();

            switch (opcao){
                case 1:
                    cadastrar(scanner);
                    break;
                case 2:
                    atualizar(scanner);
                    break;
                case 3:
                    visualizar();
                    break;
                case 4:
                    deletar(scanner);
                    break;
                case 5:
                    visualizarUmAluno(scanner);
                    break;
                default:
                    isTrue = false;
                    break;
            }//switch
        }//while
    }//method

    private void cadastrar(Scanner scanner){
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.next();

        System.out.print("Digite a idade do aluno: ");
        Integer idade = scanner.nextInt();

        Aluno aluno = new Aluno(nome,idade);
        alunoRepository.save(aluno);
        System.out.println("Aluno salvo no Banco!!\n");
    }

    private void atualizar (Scanner scanner){
        System.out.print("Digite o ID do aluno a ser atualizado: ");
        Long id = scanner.nextLong();

        Optional<Aluno> optionalAluno = alunoRepository.findById(id);
        if (optionalAluno.isPresent()){
            System.out.println("Digite o nome do aluno: ");
            String nome = scanner.next();

            System.out.print("Digite a idade do aluno: ");
            Integer idade = scanner.nextInt();

            Aluno aluno = optionalAluno.get();
            aluno.setNome(nome);
            aluno.setIdade(idade);

            alunoRepository.save(aluno);
            System.out.println("Aluno Atualizado com sucesso!");


        }else {
            System.out.printf("O ID do aluno informado '%d' é inválido!\n", id);
        }
    }//method
    
    private void  visualizar(){
        Iterable<Aluno> alunos = alunoRepository.findAll();
        for (Aluno aluno : alunos) {
            System.out.println(aluno);
        }
        //Utilizando Lambdas
        //1-  alunos.forEach(aluno -> System.out.println(aluno));
        //3-  alunos.forEach(System.out::println);
        System.out.println();
    }

    private void  deletar(Scanner scanner){
        System.out.print("Digite o ID do aluno a ser Deletado: ");
        Long id = scanner.nextLong();

        Optional<Aluno> optionalAluno = alunoRepository.findById(id);
        if (optionalAluno.isPresent()){
            alunoRepository.deleteById(id);
            System.out.println("Aluno deletado!!\n");
        }else {
            System.out.printf("O ID do aluno informado '%d' é inválido!\n", id);
        }

    }

    private void visualizarUmAluno (Scanner scanner){
        System.out.print("Digite o ID do aluno a ser visualizado: ");
        Long id = scanner.nextLong();

        Optional<Aluno> optionalAluno = alunoRepository.findById(id);
        if (optionalAluno.isPresent()){
            Aluno aluno = optionalAluno.get();

            System.out.println("Aluno: {");
            System.out.println("ID: "+aluno.getId());
            System.out.println("Nome: "+aluno.getNome());
            System.out.println("Idade: "+aluno.getId());
            System.out.println("Disciplinas: [");

            for (Disciplina disciplina : aluno.getDisciplinas()) {
                System.out.println("\tID: "+disciplina.getId());
                System.out.println("\tNome: "+disciplina.getNome());
                System.out.println("\tSemestre: "+disciplina.getSemestre());
                System.out.println();
            }
            System.out.println("]\n}");

        }else {
            System.out.printf("O ID do aluno informado '%d' é inválido!\n", id);
        }
    }//method
}//class


/*
    %s formats strings.
    %d formats decimal integers.
    %f formats floating-point numbers.
    %t formats date/time values.
-----------------------------------------------
    %c character
    %d decimal (integer) number (base 10)
    %e exponential floating-point number
    %f floating-point number
    %i integer (base 10)
    %o octal number (base 8)
    %s String
    %u unsigned decimal (integer) number
    %x number in hexadecimal (base 16)
    %t formats date/time
    %% print a percent sign
    \% print a percent sign

 */