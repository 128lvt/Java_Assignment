package com.example.assignment01.system;

import com.example.assignment01.model.User;
import com.example.assignment01.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private UserService userService;

    public DBDataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

//        User user1 = new User();
//        user1.setFullname("Nguyễn Văn Mẫn");
//        user1.setPhoneNumber("0388448348");
//        user1.setAddress("123 Lê Lợi");
//        user1.setUsername("fixluck1502");
//        user1.setPassword("daewoo123");
//        user1.setEnabled(true);
//        user1.setRoles(true);
//
//        User user2 = new User();
//        user2.setFullname("Lê Văn Thoại");
//        user2.setPhoneNumber("0388448348");
//        user2.setAddress("123 Lê Lợi");
//        user2.setUsername("fuggboi");
//        user2.setPassword("123456");
//        user2.setEnabled(true);
//        user2.setRoles(false);
//
//        userService.save(user1);
//        userService.save(user2);

    }
}
