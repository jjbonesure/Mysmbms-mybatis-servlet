package com.echo.dao;

import com.echo.pojo.Bill;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BillMapper {
    public List<Bill> getBills(Map<String,Object> map);
    public int addBill(Map<String,Object> map);
    public Bill getBillById(int id);
    public int updateBillById(Map<String,Object> map);
    public int deleteBillById(int id);
}
