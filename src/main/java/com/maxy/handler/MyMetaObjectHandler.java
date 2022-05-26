package com.maxy.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        Object email = getFieldValByName("email", metaObject);
        if(null == email){
            //字段为空，可以进行填充
            setFieldValByName("email", "123456", metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}