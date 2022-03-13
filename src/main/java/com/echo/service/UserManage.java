package com.echo.service;

import com.alibaba.fastjson.JSON;
import com.echo.dao.RoleMapper;
import com.echo.dao.UserMapper;
import com.echo.pojo.Role;
import com.echo.pojo.User;
import com.echo.utils.MyBatisUtils;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "user",urlPatterns = "/jsp/user")
public class UserManage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("query".equals(method)){
            String queryname = req.getParameter("queryname");
            String queryUserRole = req.getParameter("queryUserRole");
            String pageIndex = req.getParameter("pageIndex");
            HashMap<String,Object> map=new HashMap<>();
            if(queryname!=null){
                String trim = queryname.trim();
                if(trim.length()>0){
                    map.put("queryname",queryname);
                }
            }
            if(queryUserRole!=null){
                String trim = queryUserRole.trim();
                if(trim.length()>0){
                    if(Integer.valueOf(trim)>0){
                        map.put("queryUserRole",queryUserRole);
                    }
                }
            }
            int currentPage=1;
            int pageNums=3;
            map.put("pageNums",pageNums);
            if(pageIndex!=null){
                String trim = pageIndex.trim();
                if(trim.length()>0){
                    map.put("pageStart",(Integer.valueOf(pageIndex)-1)*pageNums);
                    currentPage=Integer.valueOf(pageIndex);
                }else{
                    map.put("pageStart",0);
                    currentPage=1;
                }
            }else{
                map.put("pageStart",0);
                currentPage=1;
            }
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> users = mapper.getUsers(map);
            System.out.println(users);
            int totalCount = mapper.getCountUsers();
            int pageCount=(int)Math.ceil(totalCount/3.0);
            HttpSession session = req.getSession();
            session.setAttribute("userList",users);
            RoleMapper mapper1 = sqlSession.getMapper(RoleMapper.class);
            List<Role> allRoles = mapper1.getAllRoles();
            session.setAttribute("roleList",allRoles);
            session.setAttribute("queryUserName",queryname);
            session.setAttribute("queryUserRole",queryUserRole);
            session.setAttribute("totalPageCount",pageCount);
            session.setAttribute("totalCount",totalCount);
            session.setAttribute("currentPageNo",currentPage);
            sqlSession.close();
            resp.sendRedirect("/jsp/userlist.jsp");
        }else if("view".equals(method)){
            String uid = req.getParameter("uid");
            int id=0;
            if(uid!=null){
                id=Integer.valueOf(uid);
            }
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserById(id);
            HttpSession session = req.getSession();
            session.setAttribute("user",user);
            sqlSession.close();
            resp.sendRedirect("/jsp/userview.jsp");
        }else if("modify".equals(method)){
            String uid = req.getParameter("uid");
            int id=0;
            if(uid!=null){
                id=Integer.valueOf(uid);
            }
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserById(id);
            HttpSession session = req.getSession();
            session.setAttribute("user",user);
            sqlSession.close();
            resp.sendRedirect("/jsp/usermodify.jsp");
        }else if("deluser".equals(method)){
            String uid = req.getParameter("uid");
            int id=0;
            if(uid!=null){
                id=Integer.valueOf(uid);
            }
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserById(id);
            HashMap<String, Object> map=new HashMap<>();
            if(user==null){
                map.put("delResult","notexist");
            }else{
                int i = mapper.deleteUserById(id);
                sqlSession.commit();
                sqlSession.close();
                if(i>0){
                    map.put("delResult","true");
                }else{
                    map.put("delResult","false");
                }
            }
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSON.toJSONString(map));
            writer.flush();
            writer.close();
        }else if("modifysave".equals(method)){
            String id = req.getParameter("uid");
            String userName = req.getParameter("userName");
            String gender = req.getParameter("gender");
            String birthday = req.getParameter("birthday");
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            Date parse=null;
            try {
                parse= format.parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            System.out.println(birthday);
            String phone = req.getParameter("phone");
            String address = req.getParameter("address");
            String userRole = req.getParameter("userRole");
            Date modifyDate=new Date();
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("loginUser");
            int modifyBy=user.getId();
            Map<String,Object> map=new HashMap<>();
            map.put("id",id);
            map.put("userName",userName);
            map.put("gender",gender);
            map.put("birthday",parse);
            map.put("phone",phone);
            map.put("address",address);
            map.put("userRole",userRole);
            map.put("modifyDate",modifyDate);
            map.put("modifyBy",modifyBy);
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            int i = mapper.updateUser(map);
            sqlSession.commit();
            int uid=Integer.valueOf(id);
            User userById = mapper.getUserById(uid);
            sqlSession.close();
            if(i>0){
//                session.setAttribute("info","true");
                req.setAttribute("info","true");
            }else{
//                session.setAttribute("info","false");
                req.setAttribute("info","false");
            }
            req.setAttribute("user",userById);
//            resp.sendRedirect("/jsp/user?method=modify&uid="+id);
            req.getRequestDispatcher("/jsp/usermodify.jsp").forward(req,resp);
        }else if("getrolelist".equals(method)){
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);
            List<Role> allRoles = mapper.getAllRoles();
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSON.toJSONString(allRoles));
            writer.flush();
            writer.close();
            sqlSession.close();
        }else if("add".equals(method)){
            String userPassword = req.getParameter("userPassword");
            String ruserPassword = req.getParameter("ruserPassword");
            if(!userPassword.equals(ruserPassword)){
                req.setAttribute("notice","fail");
                req.getRequestDispatcher("/jsp/useradd.jsp").forward(req,resp);
            }else{
                String userCode = req.getParameter("userCode");
                String userName = req.getParameter("userName");
                String gender = req.getParameter("gender");
                String birthday = req.getParameter("birthday");
                String phone = req.getParameter("phone");
                String address = req.getParameter("address");
                String userRole = req.getParameter("userRole");
                HttpSession session = req.getSession();
                User user = (User) session.getAttribute("loginUser");
                int createdBy=user.getId();
                Date creationDate=new Date();
                HashMap<String,Object> map=new HashMap<>();
                map.put("userCode",userCode);
                map.put("userName",userName);
                map.put("userPassword",userPassword);
                map.put("gender",gender);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parse=null;
                try {
                    parse = simpleDateFormat.parse(birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                map.put("birthday",parse);
                map.put("phone",phone);
                map.put("address",address);
                map.put("userRole",userRole);
                map.put("createdBy",createdBy);
                map.put("creationDate",creationDate);
                SqlSession sqlSession = MyBatisUtils.getSqlSession();
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                int i = mapper.insertUser(map);
                sqlSession.commit();
                sqlSession.close();
                if(i>0){
                    req.setAttribute("notice","true");
                }else{
                    req.setAttribute("notice","false");
                }
            }
            req.getRequestDispatcher("/jsp/useradd.jsp").forward(req,resp);
        }else if("ucexist".equals(method)){
            String userCode = req.getParameter("userCode");
            HashMap<String,Object> map=new HashMap<>();
            User user=null;
            if(userCode!=null){
                String trim = userCode.trim();
                SqlSession sqlSession = MyBatisUtils.getSqlSession();
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                user= mapper.getUserByUserCode(trim);
                sqlSession.close();
            }
            if(user!=null){
                map.put("userCode","exist");
            }else{
                map.put("userCode","NotExist");
            }
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSON.toJSONString(map));
            writer.flush();
            writer.close();
        }else if("pwdmodify".equals(method)){
            String oldpassword = req.getParameter("oldpassword");
            HashMap<String,Object> map=new HashMap<>();
            if(oldpassword==null){
                map.put("result","error");
            }else{
                SqlSession sqlSession = MyBatisUtils.getSqlSession();
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                HttpSession session = req.getSession();
                User loginUser = (User) session.getAttribute("loginUser");
                if(loginUser==null){
                    map.put("result","sessionerror");
                }else{
                    int id=loginUser.getId();
                    String userPasswordById = mapper.getUserPasswordById(id);
                    sqlSession.close();
                    if(userPasswordById==null){
                        map.put("result","sessionerror");
                    }else{
                        if(userPasswordById.equals(oldpassword)){
                            map.put("result","true");
                        }else{
                            map.put("result","false");
                        }
                    }
                }
            }
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSON.toJSONString(map));
            writer.flush();
            writer.close();
        }else if("savepwd".equals(method)){
            String newpassword = req.getParameter("newpassword");
            if(newpassword==null){
                req.setAttribute("message","密码为空！");
            }else{
                SqlSession sqlSession = MyBatisUtils.getSqlSession();
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);
                Map<String,Object> map=new HashMap<>();
                HttpSession session = req.getSession();
                User loginUser = (User) session.getAttribute("loginUser");
                map.put("id",loginUser.getId());
                map.put("userPassword",newpassword);
                int i = mapper.updateUserPassword(map);
                sqlSession.commit();
                sqlSession.close();
                if(i>0){
                    req.setAttribute("message","修改成功！");
                    session.removeAttribute("loginUser");
                }else{
                    req.setAttribute("message","修改失败！");
                }
            }
            req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req,resp);
        }
    }
}
