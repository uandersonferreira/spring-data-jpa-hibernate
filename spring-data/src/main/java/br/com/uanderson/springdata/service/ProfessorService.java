package br.com.uanderson.springdata.service;

import br.com.uanderson.springdata.orm.Disciplina;
import br.com.uanderson.springdata.orm.Professor;
import br.com.uanderson.springdata.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Scanner;

@Service
@Transactional
public class ProfessorService {
    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }
    public void menu(Scanner scanner){
        Boolean isTrue = true;
        while (isTrue){
            System.out.println("Qual ação você que excutar ? ");
            System.out.println("0 - Voltar ao menu anterior");
            System.out.println("1 - Cadastrar novo Professor");
            System.out.println("2 - Atualizar Professor");
            System.out.println("3 - Visualizar todos");
            System.out.println("4 - Deletar um professor");
            System.out.println("5 - visualizar um Professor");
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
                    visualizarUmProfessor(scanner);
                    break;
                default:
                    isTrue = false;
                    break;
            }//switch
        }//while
    }//method

    private void cadastrar(Scanner scanner){
        System.out.print("Digite o nome do professor: ");
        String nome = scanner.next();

        System.out.print("Digite o prontuário do professor: ");
        String prontuario = scanner.next();

        Professor professor = new Professor(nome, prontuario);
        professorRepository.save(professor);
        System.out.println("Professor salvo no Banco!!\n");
    }

    private void atualizar (Scanner scanner){
        System.out.print("Digite o ID do professor a ser atualizado: ");
        Long id = scanner.nextLong();

        Optional<Professor> optionalProfessor = professorRepository.findById(id);
        if (optionalProfessor.isPresent()){
            System.out.println("Digite o nome do professor: ");
            String nome = scanner.next();

            System.out.println("Digite o prontuaário do professor: ");
            String prontuario = scanner.next();

            Professor professor = optionalProfessor.get();
            professor.setName(nome);
            professor.setProntuario(prontuario);

            professorRepository.save(professor);
            System.out.println("Professor Atualizado com sucesso!");


        }else {
            System.out.printf("O ID do professor informado '%d' é inválido!\n", id);
        }
    }//method

    private void atualizarSemFindById (Scanner scanner){
        System.out.print("Digite o ID do professor a ser atualizado: ");
        Long id = scanner.nextLong();

        System.out.println("Digite o nome do professor: ");
        String nome = scanner.next();

        System.out.println("Digite o prontuaário do professor: ");
        String prontuario = scanner.next();

        Professor professor = new Professor();
        professor.setId(id);
        professor.setName(nome);
        professor.setProntuario(prontuario);

        professorRepository.save(professor);
        System.out.println("Professor Atualizado com sucesso!");

        /*
        Irá salavar, caso o id não exista na base de dados, seguindo a ordem
        de geração definida do id/chave primária.
         */

    }//method

    private void  visualizar(){
        Iterable<Professor> professors = professorRepository.findAll();
        for (Professor professor : professors) {
            System.out.println(professor);
        }
        //Utilizando Lambdas
        //1-  professors.forEach(professor -> System.out.println(professor));
        //3-  professors.forEach(System.out::println);
        System.out.println();
    }

    private void  deletar(Scanner scanner){
        System.out.print("Digite o ID do professor a ser Deletado: ");
        Long id = scanner.nextLong();

        Optional<Professor> optionalProfessor = professorRepository.findById(id);
        if (optionalProfessor.isPresent()){
            professorRepository.deleteById(id);
            System.out.println("Professor deletado!!\n");
        }else {
            System.out.printf("O ID do professor informado '%d' é inválido!\n", id);
        }

    }

    private void visualizarUmProfessor (Scanner scanner){
        System.out.print("Digite o ID do professor a ser visualizado: ");
        Long id = scanner.nextLong();

        Optional<Professor> optionalProfessor = professorRepository.findById(id);
        if (optionalProfessor.isPresent()){
            Professor professor = optionalProfessor.get();

            System.out.println("Professor: {");
            System.out.println("ID: "+professor.getId());
            System.out.println("Nome: "+professor.getName());
            System.out.println("Prontuário: "+professor.getProntuario());
            System.out.println("Disciplinas: [");
            for (Disciplina disciplina : professor.getDisciplinas()) {
                System.out.println("\tID: "+disciplina.getId());
                System.out.println("\tNome: "+disciplina.getNome());
                System.out.println("\tSemestre: "+disciplina.getSemestre());
                System.out.println();
            }
            System.out.println("]\n}");

        }else {
            System.out.printf("O ID do professor informado '%d' é inválido!\n", id);
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