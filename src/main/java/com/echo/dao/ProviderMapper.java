package com.echo.dao;

import com.echo.pojo.Provider;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ProviderMapper {
    public List<Provider> getProviders(Map<String,Object> map);
    public Provider getProviderById(int id);
    public int updateProvider(Map<String,Object> map);
    public int deleteProviderById(int id);
}
