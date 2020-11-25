package ee4216.miniproject.imdbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ImdbApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImdbApiApplication.class, args);
	}

}
