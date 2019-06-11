package atmmachine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  private static Map<String, Object> getDefaultPropertiesMap() {
    Map<String, Object> properties = new HashMap<>();
    properties.put("server.port", "8083");
    return properties;
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    app.setDefaultProperties(getDefaultPropertiesMap());

    app.run(args);
  }

}
