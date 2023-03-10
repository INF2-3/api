package com.quintor.api;

import com.quintor.api.mongoConnection.Student;
import com.quintor.api.mongoConnection.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;


@SpringBootApplication
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
public class ApiApplication {

	@Autowired
	StudentRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(StudentRepository repository) {
		return args -> {
			Student student = new Student("Franca", "Baars", "francabaars@gmail.com");

			Object[] test = repository.findAll().toArray();
			System.out.println(repository.findAll().get(0).getFirstName());

//			repository.insert(student);
		};
	}

	private List<Student> getAllFiles(StudentRepository repository) {
		List<Student> alles = repository.findAll();
		return alles;

	}


}
