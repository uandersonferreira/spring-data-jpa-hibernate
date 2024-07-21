package br.com.uanderson.springdata.service;

import br.com.uanderson.springdata.orm.Aluno;
import br.com.uanderson.springdata.orm.Disciplina;
import br.com.uanderson.springdata.orm.Professor;
import br.com.uanderson.springdata.repository.AlunoRepository;
import br.com.uanderson.springdata.repository.DisciplinaRepository;
import br.com.uanderson.springdata.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class DisciplinaService {
    private final ProfessorRepository professorRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AlunoRepository alunoRepository;

    public DisciplinaService(ProfessorRepository professorRepository, DisciplinaRepository disciplinaRepository, AlunoRepository alunoRepository) {
        this.professorRepository = professorRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.alunoRepository = alunoRepository;
    }

    public void menu(Scanner scanner){
        Boolean isTrue = true;
        while (isTrue){
            System.out.println("Qual ação você que excutar ? ");
            System.out.println("0 - Voltar ao menu anterior");
            System.out.println("1 - Cadastrar nova Disciplina");
            System.out.println("2 - Atualizar Disciplina");
            System.out.println("3 - Visualizar todas");
            System.out.println("4 - Deletar");
            System.out.println("5 - Matricular Aluno");
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
                    matricularAlunos(scanner);
                    break;
                default:
                    isTrue = false;
                    break;
            }//switch
        }//while
    }//method

    private void cadastrar(Scanner scanner){
        System.out.print("Digite o nome da Disciplina: ");
        String nome = scanner.next();

        System.out.print("Digite o semestre: ");
        int semestre = scanner.nextInt();

        System.out.print("Digite o id do Professor: ");
        Long professorId = scanner.nextLong();

        Optional<Professor> optionalProfessor = professorRepository.findById(professorId);
        if (optionalProfessor.isPresent()){
            Professor professor = optionalProfessor.get();

            Set<Aluno> alunos = matricula(scanner);

            Disciplina disciplina = new Disciplina(nome, semestre, professor);
            disciplina.setAlunos(alunos);
            disciplinaRepository.save(disciplina);
            System.out.println("Disciplina salva com sucesso!!\n");
        }else {
            System.out.printf("O ID do professor informado '%d' é inválido!\n", professorId);
        }

    }

    private void atualizar (Scanner scanner){
        System.out.print("Digite o ID da disciplina a ser atualizada: ");
        Long id = scanner.nextLong();

        Optional<Disciplina> optionalDisciplina = disciplinaRepository.findById(id);
        if (optionalDisciplina.isPresent()){
            Disciplina disciplina = optionalDisciplina.get();

            System.out.println("Digite o nome da Disciplina: ");
            String nome = scanner.next();

            System.out.print("Digite o semestre: ");
            int semestre = scanner.nextInt();

            System.out.print("Digite o id do Professor: ");
            Long professorId = scanner.nextLong();

            Optional<Professor> optionalProfessor = professorRepository.findById(professorId);
            if (optionalProfessor.isPresent()){
                Professor professor = optionalProfessor.get();

                Set<Aluno> alunos = matricula(scanner);

                disciplina.setNome(nome);
                disciplina.setSemestre(semestre);
                disciplina.setProfessor(professor);
                disciplina.setAlunos(alunos);

                disciplinaRepository.save(disciplina);
                System.out.println("Disciplina Atualizada com sucesso!");
            }else {
                System.out.printf("O ID do professor informado '%d' é inválido!\n", id);
            }

        }else {
            System.out.printf("O ID da disciplina informado '%d' é inválido!\n", id);
        }
    }//method


    private void  visualizar(){
        Iterable<Disciplina> disciplinas = disciplinaRepository.findAll();
        for (Disciplina disciplina : disciplinas) {
            System.out.println(disciplina);
        }
        //Utilizando Lambdas
        //1-  professors.forEach(professor -> System.out.println(professor));
        //3-  professors.forEach(System.out::println);
        System.out.println();
    }

    private void  deletar(Scanner scanner){
        System.out.print("Digite o ID da Disciplina a ser Deletada: ");
        Long id = scanner.nextLong();

        Optional<Disciplina> optionalDisciplina = disciplinaRepository.findById(id);
        if (optionalDisciplina.isPresent()){
            disciplinaRepository.deleteById(id);
            System.out.println("Disciplina deletada!!\n");
        }else {
            System.out.printf("O ID da Disciplina informado '%d' é inválido!\n", id);
        }

    }
    private void  matricularAlunos(Scanner scanner){
        System.out.print("Digite o ID da Disciplina para matricular alunos: ");
        Long id = scanner.nextLong();

        Optional<Disciplina> optionalDisciplina = disciplinaRepository.findById(id);

        if (optionalDisciplina.isPresent()){
            Disciplina disciplina = optionalDisciplina.get();
            Set<Aluno> novoAlunos = matricula(scanner);
            disciplina.getAlunos().addAll(novoAlunos);
            disciplinaRepository.save(disciplina);
        }else {
            System.out.printf("O ID da Disciplina informado '%d' é inválido!\n", id);
        }

    }

    private Set<Aluno> matricula(Scanner scanner) {
        Boolean isTrue = true;
        Set<Aluno> alunos = new HashSet<>();

        while (isTrue){
            System.out.print("Digite o ID do Aluno a ser Matriculado (Digite 0 para sair): ");
            Long alunoId = scanner.nextLong();

            if (alunoId > 0){
                System.out.println("alunoId: "+alunoId);
                Optional<Aluno> optionalAluno = alunoRepository.findById(alunoId);

                if (optionalAluno.isPresent()){
                    alunos.add(optionalAluno.get());
                }else {
                    System.out.printf("Nenhum aluno possui id: '%d' \n", alunoId);
                }

            }else {
                isTrue = false;
            }
        }
        return alunos;
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