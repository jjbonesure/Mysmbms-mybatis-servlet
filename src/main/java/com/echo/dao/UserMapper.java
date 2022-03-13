package com.echo.dao;

import com.echo.pojo.User;
import java.util.Map;
import java.util.List;

public interface UserMapper {
    public User getLoginUser(Map map);
    public List<User> getUsers(Map<String,Object> map);
    public User getUserById(int id);
    public int updateUser(Map<String,Object> map);
    public int deleteUserById(int id);
    public User getUserByUserCode(String userCode);
    public int insertUser(Map<String,Object> map);
    public String getUserPasswordById(int id);
    public int updateUserPassword(Map<String,Object> map);
    public int getCountUsers();
}
