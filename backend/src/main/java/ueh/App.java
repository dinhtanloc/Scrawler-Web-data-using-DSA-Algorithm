package ueh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        // System.out.println("MONGODB_URI: " + dotenv.get("MONGODB_URI"));
        // System.out.println("DATABASE_NAME: " + dotenv.get("DATABASE_NAME"));
        // System.out.println("DATABASE_NAME: " + dotenv.get("DATABASE_NAME"));
        System.out.println("MONGODB_URI: " + System.getenv("MONGODB_URI"));
        SpringApplication.run(App.class, args);
    }
}
