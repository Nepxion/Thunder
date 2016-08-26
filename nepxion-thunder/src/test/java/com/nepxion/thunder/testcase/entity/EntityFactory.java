package com.nepxion.thunder.testcase.entity;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Date;

public class EntityFactory {
    public static User createUser1() {
        User user = new User();
        user.setId("10000");
        user.setName("Zhangsan");
        user.setAge(30);
        user.setBirthday(new Date());
        user.setPhone("021-12345678");
        user.setEmail("Zhangsan@qq.com");
        user.setAddress("Shanghai");
        
        return user;
    }
    
    public static User createUser2() {
        User user = new User();
        user.setId("20000");
        user.setName("Lisi");
        user.setAge(40);
        user.setBirthday(new Date());
        user.setPhone("010-87654321");
        user.setEmail("Lisi@sina.com");
        user.setAddress("Beijing");
        
        return user;
    }
    
    public static Animal createAnimal1() {
        Animal animal = new Animal();
        animal.setId("100");
        animal.setName("Tom");
        animal.setType("Dog");
        animal.setHeight(1);
        
        return animal;
    }
    
    public static Animal createAnimal2() {
        Animal animal = new Animal();
        animal.setId("200");
        animal.setName("Ketty");
        animal.setType("Cat");
        animal.setHeight(2);
        
        return animal;
    }
}