package com.echo.service;

import com.echo.dao.UserMapper;
import com.echo.pojo.User;
import com.echo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@WebServlet(name = "login",urlPatterns = "/login")
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map<String,Object> map=new HashMap<>();
        map.put("userCode",userCode);
        map.put("userPassword",userPassword);
        User loginUser = mapper.getLoginUser(map);
        sqlSession.close();
        if(loginUser==null){
            req.setAttribute("error","用户名或者密码错误！");
            req.getRequestDispatcher("/login.jsp").forward(req,resp);
        }else{
            HttpSession session = req.getSession();
            session.setAttribute("loginUser",loginUser);
            resp.sendRedirect("/jsp/frame.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
