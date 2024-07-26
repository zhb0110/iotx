package com.example.iotx.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.iotx.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> getAll();

//    User getOne(Long id);

//    void insert(User user);

//    void update(User user);

//    void delete(Long id);
}
