package com.smartcaf;


import com.smartcaf.model.User;
import com.smartcaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartCafApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCafApplication.class, args);
    }

}
