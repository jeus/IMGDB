package nl.lunatech.movie.imgdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableNeo4jRepositories(basePackages = "nl.lunatech.movie.imgdb.core.repository")
@EnableTransactionManagement
@EnableCaching
public class ImgdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImgdbApplication.class, args);
	}

}
