package de.serra.ballot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App extends SpringBootServletInitializer {

	/*
	 * Startpoint for when spring boot is run as .war.
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(App.class);
	}

	/**
	 * Startpoint for when spring boot is run as standalone .jar.
	 *
	 * @param args
	 *            Optional arguments passed to spring boot.
	 */
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
