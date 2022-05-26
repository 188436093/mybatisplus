package com.maxy.test;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxy.mapper.UserMapper;
import com.maxy.pojo.User;
import com.maxy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author maxy
 */
@SpringBootTest
public class BaseTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01(){
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    /**
     * 插入操作
     */
    @Test
    public void test02(){
        /*可以插入一个含有相关数据的对象，无需给全部属性都赋值，MP会自动判断有值的字段进行插入，
        没有赋值的字段MP不会写进sql语句中，省去了原生mybatis需要在sql中进行非空判断的操作
        * */
        User user = User.builder().userName("张三").name("zs").age(18).build();
        //count：被影响的行数
        int count = userMapper.insert(user);
    }
    @Test
    public void test03(){
        /*MP若不指定主键，默认采用雪花算法自动生成主键
        * */
        User user = User.builder().userName("张三").name("zs").age(18).build();
        //count：被影响的行数
        int count = userMapper.insert(user);
        //MP可以自动主键回填，直接取值即可
        Long primaryId = user.getId();
        System.out.println(primaryId);
    }

    /*
     * 要求：添加名为小明的user数据
     * sql语句：insert into tb_user(xx,xx,xx,...) values (xx,xx,xx,....)
     * */
    @Test
    public void testAddUesrById() {
        User user = User.builder()
                .name("小明")
                .userName("xiaoming")
                .age(18)
                .email("this is a email")
                .password("123456")
                .build();
        userMapper.insert(user);
    }

    /*
     * 要求：删除id为 16 的用户
     * sql语句：DELETE FROM tb_user WHERE id=?
     * */
    @Test
    public void testDelUesrById() {
        userMapper.deleteById(16);
    }

    /*
     * 要求：删除id为 15、14 的用户
     * sql语句：DELETE FROM tb_user WHERE id=? or id=?
     * sql语句：DELETE FROM tb_user WHERE id IN ( ? , ? )
     * */
    @Test
    public void testDelUesrByIds() {
        userMapper.deleteBatchIds(Arrays.asList(14, 15));
    }

    /*
     * 要求：删除user_name为王八衰 age 为22 的用户
     * sql语句：DELETE FROM tb_user WHERE user_name = ? AND age = ?
     * */
    @Test
    public void testDelUesrByCondition() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_name","王八衰");
        map.put("age",22);
        userMapper.deleteByMap(map);
    }

    /*
     * 修改用户id为 4 的username修改为 李催
     * sql语句：UPDATE tb_user SET user_name=? WHERE id=?
     */
    @Test
    public void testUpdate(){
        User user = User.builder().id(4L).userName("李催").build();
        userMapper.updateById(user);
    }

    /*
     * 要求：分页查询user数据，每页5条数据,查询第一页数据
     * sql语句：SELECT * FROM tb_user LIMIT 0,5
     * sql语句：SELECT * FROM tb_user LIMIT 5 （简写形式）
     */
    @Test
    public void testPage(){
        Page<User> page = new Page<>(1,5);
        Page<User> userPage = userMapper.selectPage(page, null);
        System.out.println(userPage.getRecords());
    }


    /*
     * 要求：查询用户中姓名包含"伤"，密码为"123456",且年龄为19或者25或者29，查询结果按照年龄降序排序；
     * sql语句：SELECT * FROM tb_user WHERE (user_name LIKE ? AND password = ? AND age IN (?,?,?)) ORDER BY age DESC
     */
    @Test
    public void testConditions1Query(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> result = wrapper.like(User::getUserName, "伤")
                .eq(User::getPassword, "123456")
                .in(User::getAge, 19, 25, 29)
                .orderByDesc(User::getAge);
        System.out.println(userMapper.selectList(wrapper));

    }


    /*
     * 要求：查询用户中姓名包含"伤"，密码为"123456",且年龄为19或者25或者29，查询结果按照年龄降序排序；
     * sql语句：SELECT user_name,age FROM tb_user WHERE (user_name LIKE ? AND password = ? AND age IN (?,?,?)) ORDER BY age DESC
     */
    @Test
    public void testConditions2Query(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> result = wrapper.like(User::getUserName, "伤")
                .eq(User::getPassword, "123456")
                .in(User::getAge, 19, 25, 29)
                .orderByDesc(User::getAge).select(User::getUserName,User::getAge);
        System.out.println(userMapper.selectList(wrapper));

    }

    /*
     * 要求：查询用户密码为"123456"的数据，并且要求每页显示5条，查询第二页的数据；
     * sql语句：SELECT user_name,age FROM tb_user WHERE (user_name LIKE ? AND password = ? AND age IN (?,?,?)) ORDER BY age DESC
     */
    @Test
    public void testConditionsPageQuery(){
        Page<User> page = new Page<>(2,5);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPassword,"123456");
        Page<User> selectPage = userMapper.selectPage(page, wrapper);
        List<User> records = selectPage.getRecords();
        System.out.println(records);
    }


    /*
     * 要求：查询用户密码为"123456",或者 age 为 20、21、22，并且要求每页显示5条，查询第二页的数据；
     * sql语句：SELECT user_name,age FROM tb_user WHERE  password = ? or age IN (?,?,?)
     */
    @Test
    public void testConditionsOrQuery(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPassword,"123456").or().in(User::getAge,20,21,22);
        Page<User> page = new Page<>(2, 5);

        Page<User> selectPage = userMapper.selectPage(page, wrapper);
        System.out.println(selectPage.getRecords());
    }

    /*
    主键生成策略
    AUTO:数据库自增
    NONE:未设置主键类型，约等于input
    INPUT：用户输入ID
    插入主键内容为空时，启用以下两种策略
    ASSING_ID 雪花算法随机生成主键
    ASSING_UUID UUID随机生成主键
    * */
    @Test
    public void test04(){
        User user = User.builder().userName("赵六").build();
        userMapper.insert(user);
    }

    //LambdaUpdate
    @Test
    public void test05(){
        User user = User.builder()
                .userName("liuyan")
                .password("6669")
                .name("柳岩")
                .age(18)
                .email("liuyan@qq.com")
                .build();
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getUserName,"赵一伤");

        userMapper.update(user,wrapper);
    }

    //封装service层测试
    @Autowired
    private UserService userService;
    @Test
    public void test06(){
        User user = User.builder()
                .userName("liuyan")
                .password("6669")
                .name("王心凌")
                .age(18)
                .email("liuyan@qq.com")
                .build();
        boolean flag = userService.save(user);
        System.out.println(flag);
    }

    //自动填充测试
    @Test
    public void test07(){
        /*
        在pojo类上添加@TableField注解，fill值为枚举类型，有以下策略：
        1.DEFAULT 不进行任何处理
        2.INSERT 插入时填充字段
        3.UPDATE 更新时填充字段
        4.INSERT_UPDATE 插入和更新时填充字段，等于上面两个之和
        * */
        User user = User.builder()
                .userName("liuyan")
                .password("6669")
                .name("九点")
                .age(18)
                .build();
        userMapper.insert(user);
    }
}
