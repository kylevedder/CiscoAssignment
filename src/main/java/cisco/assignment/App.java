package cisco.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//Kyle C Vedder
//kvedder@umass.edu
//Resume: http://people.umass.edu/kvedder/resume

/**
 * Main app, launches the application and points Spring to the appropriate
 * packages to scan.
 * 
 * @author kyle
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "cisco.assignment")
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
