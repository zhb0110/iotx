package com.example.iotx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.iotx.enums.UserSexEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

// TODO:不写有问题吗？
// 最好写：Serializable:序列化给我们提供了一种技术，用于保存对象的变量和传输。虽然也可以使用别的一些方法实现同样的功能，但是Java给我们提供的方法使用起来是非常方便的。
@Data
@TableName("user")
public class User implements Serializable {


    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String email;
    private String userName;
    private String password;
    private String nickName;
    private LocalDateTime regTime;
    private UserSexEnum userSex;

}
