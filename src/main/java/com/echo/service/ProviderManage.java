package com.echo.service;

import com.alibaba.fastjson.JSON;
import com.echo.dao.ProviderMapper;
import com.echo.pojo.Provider;
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
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "provider",urlPatterns = "/jsp/provider")
public class ProviderManage extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("query".equals(method)){
            String queryProCode = req.getParameter("queryProCode");
            String queryProName = req.getParameter("queryProName");
            System.out.println(queryProCode);
            System.out.println(queryProName);
            HashMap<String,Object> map=new HashMap<>();
            if(queryProCode!=null){
                String trim = queryProCode.trim();
                if(trim.length()!=0){
                    map.put("queryProCode",queryProCode);
                }
            }
            if(queryProName!=null){
                String trim = queryProName.trim();
                if(trim.length()!=0){
                    map.put("queryProName",queryProName);
                }
            }
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            ProviderMapper mapper = sqlSession.getMapper(ProviderMapper.class);
            List<Provider> providers = mapper.getProviders(map);
            HttpSession session = req.getSession();
            session.setAttribute("providerList",providers);
            session.setAttribute("queryProCode",queryProCode);
            session.setAttribute("queryProName",queryProName);
            resp.sendRedirect("/jsp/providerlist.jsp");
            sqlSession.close();
        }else if("view".equals(method)){
            String proid = req.getParameter("proid");
            int proId=0;
            if(proid!=null){
                proId  = Integer.valueOf(proid);
            }
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            ProviderMapper mapper = sqlSession.getMapper(ProviderMapper.class);
            Provider provider = mapper.getProviderById(proId);
            HttpSession session = req.getSession();
            session.setAttribute("provider",provider);
            sqlSession.close();
            resp.sendRedirect("/jsp/providerview.jsp");

        }else if("modify".equals(method)){
            String proid = req.getParameter("proid");
            int proId=0;
            if(proid!=null){
                proId  = Integer.valueOf(proid);
            }
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            ProviderMapper mapper = sqlSession.getMapper(ProviderMapper.class);
            Provider provider = mapper.getProviderById(proId);
            HttpSession session = req.getSession();
            session.setAttribute("provider",provider);
            sqlSession.close();
            resp.sendRedirect("/jsp/providermodify.jsp");
        }else if("modifysave".equals(method)){
            String id = req.getParameter("id");
            String proCode = req.getParameter("proCode");
            String proName = req.getParameter("proName");
            String proContact = req.getParameter("proContact");
            String proPhone = req.getParameter("proPhone");
            String proAddress = req.getParameter("proAddress");
            String proFax = req.getParameter("proFax");
            String proDesc = req.getParameter("proDesc");
            Date modifyDate=new Date();
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("loginUser");
            int modifyBy=user.getId();
            HashMap<String,Object> map=new HashMap<>();
            map.put("id",id);
            map.put("proCode",proCode);
            map.put("proName",proName);
            map.put("proContact",proContact);
            map.put("proPhone",proPhone);
            map.put("proAddress",proAddress);
            map.put("proFax",proFax);
            map.put("proDesc",proDesc);
            map.put("modifyDate",modifyDate);
            map.put("modifyBy",modifyBy);
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            ProviderMapper mapper = sqlSession.getMapper(ProviderMapper.class);
            int i = mapper.updateProvider(map);
            sqlSession.commit();
            Provider providerById = mapper.getProviderById(Integer.valueOf(id));
            sqlSession.close();
            if(i>0){
//                session.setAttribute("info","true");
                req.setAttribute("info","true");
            }else{
//                session.setAttribute("info","false");
                req.setAttribute("info","false");
            }
            req.setAttribute("provider",providerById);
//            resp.sendRedirect("/jsp/provider?method=modify&proid="+id);
            req.getRequestDispatcher("/jsp/providermodify.jsp").forward(req,resp);
        }else if("delprovider".equals(method)){
            String proid = req.getParameter("proid");
            int id=0;
            if(proid!=null){
                String trim = proid.trim();
                id=Integer.valueOf(trim);
            }
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            ProviderMapper mapper = sqlSession.getMapper(ProviderMapper.class);
            Provider provider = mapper.getProviderById(id);
            Map<String,String> map=new HashMap<>();
            if(provider==null){
                map.put("delResult","notexist");
            }else{
                int i = mapper.deleteProviderById(id);
                sqlSession.commit();
                if(i>0){
                    map.put("delResult","true");
                }else{
                    map.put("delResult","false");
                }
            }
            sqlSession.close();
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSON.toJSONString(map));
            writer.flush();
            writer.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
