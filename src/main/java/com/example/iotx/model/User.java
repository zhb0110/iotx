package com.example.iotx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.example.iotx.enums.UserSexEnum;
import lombok.Data;

import java.time.LocalDateTime;

// TODO:不写有问题吗？
// 最好写：Serializable:序列化给我们提供了一种技术，用于保存对象的变量和传输。虽然也可以使用别的一些方法实现同样的功能，但是Java给我们提供的方法使用起来是非常方便的。
@Data
//@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends Model<User> {


    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String email;
    private String userName;
    private String password;
    private String nickName;
    private LocalDateTime regTime;
    @TableField("user_sex")
    private UserSexEnum userSex;

    public User() {
        super();
    }

    public User(String email, String userName, String password, String nickName, LocalDateTime regTime, UserSexEnum userSex) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.nickName = nickName;
        this.regTime = regTime;
        this.userSex = userSex;
    }

}
