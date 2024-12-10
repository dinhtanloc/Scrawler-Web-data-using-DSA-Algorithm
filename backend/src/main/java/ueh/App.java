package ueh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.out.println("MONGODB_URI: " + dotenv.get("MONGODB_URI"));
        System.out.println("DATABASE_NAME: " + dotenv.get("DATABASE_NAME"));
        System.out.println("DATABASE_NAME: " + dotenv.get("DATABASE_NAME"));
        SpringApplication.run(App.class, args);
    }
}
