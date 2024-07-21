package br.com.uanderson.springdata.service;

import br.com.uanderson.springdata.orm.Aluno;
import br.com.uanderson.springdata.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class RelatorioAlunoService {
    private final AlunoRepository alunoRepository;

    public RelatorioAlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public void menu(Scanner scanner){
        Boolean isTrue = true;
        while (isTrue){
            System.out.println("Qual relatório você deseja ? ");
            System.out.println("0 - Voltar ao menu anterior");
            System.out.println("1 - Aluno por um dado nome");
            System.out.println("2 - Aluno por um dado nome e idade menor ou igual");
            System.out.print("Opção: ");

            int opcao = scanner.nextInt();
            scanner.reset();

            switch (opcao){
                case 1:
                    buscarAlunoPorNome(scanner);
                    break;
                case 2:
                    buscarAlunoPorNomeAndIdadeMenorOuIgual (scanner);
                    break;
                default:
                    isTrue = false;
                    break;
            }//switch
        }//while
    }//method

    public void buscarAlunoPorNome(Scanner scanner){
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.next();

        List<Aluno> alunos = alunoRepository.findByNomeStartingWith(nome);
        alunos.forEach(System.out::println);
    }
    public void buscarAlunoPorNomeAndIdadeMenorOuIgual(Scanner scanner){
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.next();
        System.out.print("Digite a idade do aluno: ");
        Integer idade = scanner.nextInt();

        List<Aluno> alunos = alunoRepository.findByNomeStartingWithAndIdadeLessThanEqual(nome, idade);
        alunos.forEach(System.out::println);
    }

}//class

//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.sample-app.finders.strategies
