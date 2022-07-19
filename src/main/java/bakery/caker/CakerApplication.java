package bakery.caker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CakerApplication.class, args);
	}

}
