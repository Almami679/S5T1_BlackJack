package S5T1BlackJack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "S5T1BlackJack")
@EnableReactiveMongoRepositories(basePackages = "S5T1BlackJack.repository")
@EntityScan(basePackages = "S5T1BlackJack.entities.sql")

public class S5T1BlackJackApplication {

	public static void main(String[] args) {
		SpringApplication.run(S5T1BlackJackApplication.class, args);
	}

}


///https://www.youtube.com/watch?v=zWWrgimOSpI&ab_channel=ProgramandoenJAVA REACTIVO