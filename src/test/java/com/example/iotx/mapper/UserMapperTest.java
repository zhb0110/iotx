package com.example.iotx.mapper;

import com.example.iotx.enums.UserSexEnum;
import com.example.iotx.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

// TODO:@RunWith运行识别环境的，但是在idea中自动识别了，太方便，以至于可以不用
// 但是有可能会失效。。。
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() throws Exception {
//        userMapper.insert(new User("email", "用户1", "密码1", "昵称1", null, UserSexEnum.MAN));
//        userMapper.insert(new User("email", "用户2", "密码1", "昵称1", null, UserSexEnum.MAN));
//        userMapper.insert(new User("email", "用户3", "密码1", "昵称1", null, UserSexEnum.MAN));

        System.out.println(userMapper.getAll().size());
    }

    @Test
    public void testQuery() throws Exception {
//        List<User> users = userMapper.getAll();
//        if (users == null || users.size() == 0) {
//            System.out.println("is null");
//        } else {
//            System.out.println(users.toString());
//        }
    }


    @Test
    public void testUpdate() throws Exception {

        Long id = 1l;
        User user = userMapper.selectById(id);
        System.out.println(user.toString());
        user.setNickName("neo");
        userMapper.updateById(user);
        Assert.assertTrue(("neo".equals(userMapper.selectById(id).getNickName())));
    }
}
