package com.justplay1994.github.db2es;

import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Package: com.justplay1994.github.db2es
 * @Project: db2es
 * @Creator: huangzezhou
 * @Create_Date: 2018/11/30 15:24
 * @Updater: huangzezhou
 * @Update_Date: 2018/11/30 15:24
 * @Update_Description: huangzezhou 补充
 * @Description: //TODO
 **/
public class ClassTest {



    @Test
    public void test() throws NoSuchFieldException {
        //获取类定义上的泛型类型
        A<Integer> c = new A<Integer>();
        Field dataMapField = c.getClass().getDeclaredField("dataMap");


        //获取HashMap的泛型参数
        ParameterizedType parameterizedType = (ParameterizedType) dataMapField.getGenericType();
        Type[] types = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < types.length; ++i){
            System.out.println(types[i]);
        }
        System.out.println(dataMapField);
    }

}
class A<T>{
    LinkedHashMap<String, LinkedHashMap<String, T>> dataMap = null;    //统计的数据

    T type;

    public void getType(){
        System.out.println(type.getClass().getTypeName());
    }

}
