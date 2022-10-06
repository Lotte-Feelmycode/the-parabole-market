package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
/*
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    public void changeRoleToSeller() throws Exception {
        // given
        Seller seller = new Seller(null, "test1's storeName", "1231231231");

        // when
        User user = new User("test1@naver.com", "testuser1", "testnick1", "userpw1");
        userService.changeRoleToSeller(user, seller);

        // then
        assertEquals("User ", user, seller.getUser());
        assertEquals("Seller ", seller, user.getSeller());
    }
*/

}
