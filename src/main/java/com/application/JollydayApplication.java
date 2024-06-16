package com.application;

import com.application.repository.UserRepository;
import com.application.service.GroupService;
import com.application.service.UserService;
import com.application.utils.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@RequiredArgsConstructor
public class JollydayApplication {

	private final SendEmailService sendService;
	private final GroupService grpService;

	private final UserService userService;

	private final UserRepository userRepository;

	public static void main(String[] args) {

		SpringApplication.run(JollydayApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void test() {
		System.out.println(userService.findUserById(253));
		System.out.println(userRepository.getGroupId(253));
		System.out.println(userRepository.findAll());
	}
	// Vrem ca in momentul organizarii unui eveniment, daca nu exista niciun group, atunci sa se creeze unul

	@EventListener(ApplicationReadyEvent.class)
	public void send() {
		sendService.sendMail(
				"bianca.andrei.f22@outlook.com",
				"Jollyday - Ati fost adaugat la un nou eveniment!",
				"Cu ocazia zilei de 20 iunie 2024, il vom sarbatori pe colegul X.\nY este persoana desemnata pentru a colecta fondurile, in valoare de 50 RON/pers.\nPentru mai multe detalii, il puteti gasi in Grupul 1.\nVa multumim!");
	}

//	@EventListener
//	public void createGroupIfThereIsNone() {
//
//	}

}
