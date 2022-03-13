package com.echo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.echo.dao.BillMapper;
import com.echo.dao.ProviderMapper;
import com.echo.pojo.Bill;
import com.echo.pojo.Provider;
import com.echo.pojo.User;
import com.echo.utils.MyBatisUtils;
import com.mysql.cj.xdevapi.JsonArray;
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

@WebServlet(name = "billmanage",urlPatterns = "/jsp/bill")
public class BillManage extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("query".equals(method)){
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            BillMapper mapper = sqlSession.getMapper(BillMapper.class);
            Map<String,Object> map=new HashMap<>();
            HttpSession session = req.getSession();
            String queryProductName = req.getParameter("queryProductName");
            String queryProviderId = req.getParameter("queryProviderId");
            String queryIsPayment = req.getParameter("queryIsPayment");
            System.out.println("queryProductName::"+queryProductName);
            if("".equals(queryProductName)){
                queryProductName=null;
            }else{
                session.setAttribute("queryProductName",queryProductName);
            }
            System.out.println("queryIsPayment::"+queryIsPayment);
            if("0".equals(queryIsPayment)){
                queryIsPayment=null;
            }else{
                session.setAttribute("queryIsPayment",queryIsPayment);
            }
            System.out.println("queryProviderId"+queryProviderId);
            if("0".equals(queryProviderId)){
                queryProviderId=null;
            }else{
                session.setAttribute("queryProviderId",queryProviderId);
            }
            map.put("queryIsPayment",queryIsPayment);
            map.put("queryProductName",queryProductName);
            map.put("queryProviderId",queryProviderId);
            List<Bill> bills = mapper.getBills(map);
            session.setAttribute("billList",bills);
            ProviderMapper mapper1 = sqlSession.getMapper(ProviderMapper.class);
            Map<String,Object> map1=new HashMap<>();
            List<Provider> providers = mapper1.getProviders(map1);
            session.setAttribute("providerList",providers);
            sqlSession.close();
            resp.sendRedirect("/jsp/billlist.jsp");
        }else if("add".equals(method)){
            String billCode = req.getParameter("billCode");
            String productName = req.getParameter("productName");
            String productDesc = req.getParameter("productDesc");
            String productUnit = req.getParameter("productUnit");
            String productCount = req.getParameter("productCount");
            String totalPrice = req.getParameter("totalPrice");
            String providerId = req.getParameter("providerId");
            String isPayment = req.getParameter("isPayment");
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("loginUser");
            int createdBy= user.getId();
            Date creationDate=new Date();
            HashMap<String,Object> map=new HashMap<>();
            map.put("billCode",billCode);
            map.put("productName",productName);
            map.put("productDesc",productDesc);
            map.put("productUnit",productUnit);
            map.put("productCount",productCount);
            map.put("totalPrice",totalPrice);
            map.put("isPayment",isPayment);
            map.put("createdBy",createdBy);
            map.put("creationDate",creationDate);
            map.put("providerId",providerId);
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            BillMapper mapper = sqlSession.getMapper(BillMapper.class);
            int i = mapper.addBill(map);
            if(i>0){
                req.setAttribute("info","success");
            }else{
                req.setAttribute("info","fail");
            }
            sqlSession.commit();
            sqlSession.close();
            req.getRequestDispatcher("/jsp/billadd.jsp").forward(req,resp);

        }else if("delbill".equals(method)){
            String billid = req.getParameter("billid");
            int bid=0;
            if(billid!=null){
                bid=Integer.valueOf(billid);
            }
            resp.setContentType("application/json");
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            BillMapper mapper = sqlSession.getMapper(BillMapper.class);
            HashMap<String,String> map=new HashMap<>();
            Bill bill = mapper.getBillById(bid);
            if(bill==null){
                map.put("delResult","notexist");
            }else{
                int i = mapper.deleteBillById(bid);
                sqlSession.commit();
                sqlSession.close();
                if(i==1 ){
                    map.put("delResult","true");
                }else{
                    map.put("delResult","false");
                }
            }
            String s = JSON.toJSONString(map);
            PrintWriter writer = resp.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        }else if("getproviderlist".equals(method)){
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            ProviderMapper mapper = sqlSession.getMapper(ProviderMapper.class);
            HashMap<String,Object> map=new HashMap<>();
            List<Provider> providers = mapper.getProviders(map);
            sqlSession.close();
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            String s = JSON.toJSONString(providers);
            writer.write(s);
            writer.flush();
            writer.close();
            sqlSession.close();
        }else if("view".equals(method)){
            String billid = req.getParameter("billid");
            int bid=0;
            if(billid!=null){
                bid=Integer.valueOf(billid);
            }
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            BillMapper mapper = sqlSession.getMapper(BillMapper.class);
            Bill bill = mapper.getBillById(bid);
            HttpSession session = req.getSession();
            session.setAttribute("bill",bill);
            sqlSession.close();
            resp.sendRedirect("/jsp/billview.jsp");
        }else if("modifysave".equals(method)){
            String id = req.getParameter("id");
            int bid=0;
            if(id!=null||"".equals(id)){
                bid=Integer.valueOf(id);
            }
            String billCode = req.getParameter("billCode");
            String productName = req.getParameter("productName");
            String productDesc = req.getParameter("productDesc");
            String productUnit = req.getParameter("productUnit");
            String productCount = req.getParameter("productCount");
            String totalPrice = req.getParameter("totalPrice");
            String providerId = req.getParameter("providerId");
            String isPayment = req.getParameter("isPayment");
            HttpSession session = req.getSession();
            User  user = (User) session.getAttribute("loginUser");
            int modifyBy=user.getId();
            Date modifyDate=new Date();
            HashMap<String,Object> map=new HashMap<>();
            map.put("id",bid);
            map.put("billCode",billCode);
            map.put("productDesc",productDesc);
            map.put("productName",productName);
            map.put("productUnit",productUnit);
            map.put("productCount",productCount);
            map.put("totalPrice",totalPrice);
            map.put("providerId",providerId);
            map.put("isPayment",isPayment);
            map.put("modifyBy",modifyBy);
            map.put("modifyDate",modifyDate);
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            BillMapper mapper = sqlSession.getMapper(BillMapper.class);
            int i = mapper.updateBillById(map);
            sqlSession.commit();
            Bill billById = mapper.getBillById(bid);
            sqlSession.close();
            if(i>0){
//                session.setAttribute("info","true");
                req.setAttribute("info","true");
            }else{
//                session.setAttribute("info","false");
                req.setAttribute("info","false");
            }
            req.setAttribute("bill",billById);
//            resp.sendRedirect("/jsp/bill?method=modify&billid="+bid);
            req.getRequestDispatcher("/jsp/billmodify.jsp").forward(req,resp);
        }else if("modify".equals(method)){
            String billid = req.getParameter("billid");
            int bid=0;
            if(billid!=null){
                bid=Integer.valueOf(billid);
            }
            SqlSession sqlSession = MyBatisUtils.getSqlSession();
            BillMapper mapper = sqlSession.getMapper(BillMapper.class);
            Bill bill = mapper.getBillById(bid);
            HttpSession session = req.getSession();
            session.setAttribute("bill",bill);
            sqlSession.close();
            resp.sendRedirect("/jsp/billmodify.jsp");
        }
    }
}
