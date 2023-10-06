package se.harbil.policeslackplugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class PoliceSlackPluginApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoliceSlackPluginApplication.class, args);
    }
}
