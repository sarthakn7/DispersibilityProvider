package edu.umass.cs.dispersibility.provider;


import edu.umass.cs.dispersibility.provider.storage.StorageProperties;
import edu.umass.cs.dispersibility.provider.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * @author Sarthak Nandi on 30/4/18.
 */
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  CommandLineRunner init(StorageService storageService) {
    return (args) -> {
      storageService.deleteAll();
      storageService.init();
    };
  }
}