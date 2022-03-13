import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.echo.dao.BillMapper;
import com.echo.dao.ProviderMapper;
import com.echo.dao.UserMapper;
import com.echo.pojo.Bill;
import com.echo.pojo.Provider;
import com.echo.pojo.User;
import com.echo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTest {
    @Test
    public void test01(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map<String,Object> map=new HashMap<>();
        map.put("userCode","admin");
        map.put("userPassword","123456");
        User loginUser = mapper.getLoginUser(map);
        System.out.println(loginUser);
        sqlSession.close();
    }

    @Test
    public void test02(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        BillMapper mapper = sqlSession.getMapper(BillMapper.class);
        Map<String,Object> map=new HashMap<>();
        map.put("queryProductName","大豆油");
        map.put("queryProviderId",null);
        map.put("queryIsPayment",null);
        List<Bill> bills = mapper.getBills(map);
        bills.forEach(bill -> {
            System.out.println(bill);
        });
    }

    @Test
    public void test03(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        ProviderMapper mapper = sqlSession.getMapper(ProviderMapper.class);
        HashMap<String,Object> map=new HashMap<>();
        List<Provider> providers = mapper.getProviders(map);
        providers.forEach(provider -> {
//            System.out.println(provider);
            String s = JSON.toJSONString(provider);
            System.out.println(s);
        });
    }

    @Test
    public void test04() throws ParseException {

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String datetime="2020-10-22";
        Date parse = format.parse(datetime);
        System.out.println(parse);
    }

    @Test
    public void test05(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        String userPasswordById = mapper.getUserPasswordById(1);
        System.out.println(userPasswordById);
        sqlSession.close();
    }

}
