package br.com.uanderson;

import br.com.uanderson.entities.BillingInfo;
import br.com.uanderson.entities.Task;
import br.com.uanderson.entities.User;
import br.com.uanderson.repository.BillingInfoRepository;
import br.com.uanderson.repository.TaskRepository;
import br.com.uanderson.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class ObHibernateProjetoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ObHibernateProjetoApplication.class, args);

		// BillingInfo and User Associate ==========================================
		BillingInfoRepository billingInfoRepository = context.getBean(BillingInfoRepository.class);

		UserRepository userRepository = context.getBean(UserRepository.class);

		BillingInfo billingInfo = new BillingInfo(null, "ELM street", "54432", "Pesadilla", "Transilvania", "ES54325454321", null);
		BillingInfo billingInfo2 = new BillingInfo(null, "Dom Pedro street", "785478", "Lisboa", "Portugal", "P84258765232", null);

		User user1 = new User(null, "Jack", "Dorsey", "858755874R", true, LocalDate.of(1970, 12, 1));
		user1.setBillingInfo(billingInfo);
		userRepository.save(user1);

		// Task and User Associate ==========================================
		User user2 = new User(null, "Mike", "Dorsey", "456123789E", false, LocalDate.of(2000, 10, 12));
		user2.setBillingInfo(billingInfo2);
		userRepository.save(user2);

		TaskRepository taskRepository = context.getBean(TaskRepository.class);

		Task task1= new Task(null, "Tarefa 1", "Lorem ipsum", false, LocalDate.of(2024, 10, 13), user1);
		Task task2 = new Task(null, "Tarefa 2", "Lorem ipsum", false, LocalDate.of(2024, 11, 13), user1);
		Task task3 = new Task(null, "Tarefa 3", "Lorem ipsum", false, LocalDate.of(2024, 12, 13), user2);
		Task task4 = new Task(null, "Tarefa 4", "Lorem ipsum", false, LocalDate.of(2024, 12, 13), user2);

		taskRepository.saveAll(List.of(task1, task2, task3, task4));

		System.out.printf("fin");



	}//main
}//class
