package com.application;

import com.application.exceptions.TooFewParticipants;
import com.application.exceptions.UserNotFoundException;
import com.application.exceptions.UserNotInGroup;
import com.application.model.Event;
import com.application.model.User;
import com.application.repository.EventRepository;
import com.application.service.EventService;
import com.application.service.GroupService;
import com.application.service.UserService;
import com.application.utils.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
@RequiredArgsConstructor
public class JollydayApplication {

	private final SendEmailService sendEmailService;
	private final GroupService grpService;
	private final EventService eventService;
	private final EventRepository eventRepository;
	private final UserService userService;

	private List<String> emails = Arrays.asList("bianca.andrei.f22@gmail.com", "bianca-filofteia.andrei@s.unibuc.ro", "bianca.andrei.f22@outloook.com", "biancaandrei_2009@yahoo.com");


	public static void main(String[] args) {

		SpringApplication.run(JollydayApplication.class, args);

	}

	@EventListener(ApplicationReadyEvent.class)
	public void checkEvents() throws UserNotFoundException, TooFewParticipants, UserNotInGroup {
		List<User> users = userService.findAllUsers();
		for (User user : users) {
			List<Event> existingEvents = eventService.findAllEventsByCelebratedId(user.getId());
			boolean ok = true;
			Integer eventId;
			for (Event event : existingEvents) {
				if (event.getCreationDate().isAfter(LocalDate.now())) ok = false;
				System.out.println(event.getId());
			}
			if (ok) {
				var bday = eventService.createDate(user.getBirthday());
				var daysUntilBday = ChronoUnit.DAYS.between(LocalDate.now(), bday);
				if (daysUntilBday <= 14  && daysUntilBday >= 0 && bday.isAfter(LocalDate.now())) {
					eventId = eventService.addEvent(user.getId(), 50.0);
					if (eventId != null) {
						System.out.println("id eveniment: " + eventId.toString());
						var part = eventRepository.getAllParticipantIds(user.getId());
						for (Integer id : part) {
							if (!Objects.equals(id, user.getId()))
								userService.addJoinedEventToUser(id, eventId);
							var participant = userService.findUserById(id).orElse(null);
							if (participant != null) {
								if(emails.contains(participant.getEmail())) {
									Event event = eventService.findById(eventId).orElse(null);
									if(event != null) {
										String group = event.getCollectingPlace() == null ? "Contactati colectorul in privat" : event.getCollectingPlaceName();
										sendEmailService.sendMail(participant.getEmail(), "Jollyday - o noua zi de nastere", "Collector: " + event.getCollectorUser().getUsername() + "\nLocatie strangere fonduri: " + group + "\nSarbatorit: " + event.getCelebratedUser().getUsername() + "\nSuma: " + event.getCollectedAmount() + "\nData: " + event.getCreationDate().toString());
									}
							}
							}
						}
					}
				}
			}
		}
	}



}
