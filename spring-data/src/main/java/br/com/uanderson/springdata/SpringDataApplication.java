package br.com.uanderson.springdata;

import br.com.uanderson.springdata.service.AlunoService;
import br.com.uanderson.springdata.service.DisciplinaService;
import br.com.uanderson.springdata.service.ProfessorService;
import br.com.uanderson.springdata.service.RelatorioAlunoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class SpringDataApplication implements CommandLineRunner {
    private final ProfessorService professorService;
    private final DisciplinaService disciplinaService;
    private final AlunoService alunoService;
    private final RelatorioAlunoService relatorioAlunoService;
    public SpringDataApplication(ProfessorService professorService, DisciplinaService disciplinaService, AlunoService alunoService, RelatorioAlunoService relatorioAlunoService) {
        this.professorService = professorService;
        this.disciplinaService = disciplinaService;
        this.alunoService = alunoService;
        this.relatorioAlunoService = relatorioAlunoService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringDataApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Boolean isTrue = true;
        Scanner scanner = new Scanner(System.in);

        while (isTrue){
            System.out.println("Escolha uma das opções abaixo: ");
            System.out.println("0 - Sair");
            System.out.println("1 - Professor");
            System.out.println("2 - Disciplina");
            System.out.println("3 - Aluno");
            System.out.println("4 - Relatório");
            System.out.print("opção:  ");
            int opcao = scanner.nextInt();

            switch (opcao){
                case 1:
                    professorService.menu(scanner);
                    break;
                case 2:
                    disciplinaService.menu(scanner);
                    break;
                case 3:
                    alunoService.menu(scanner);
                    break;
                case 4:
                    relatorioAlunoService.menu(scanner);
                    break;
                default:
                    isTrue = false;
                    break;
            }//switch
        }//while
    }
}//class
