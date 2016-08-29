package com.nepxion.thunder.test.service;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalServiceImpl implements AnimalService {
    private static final Logger LOG = LoggerFactory.getLogger(AnimalServiceImpl.class);
    
    private List<Animal> animals;

    public AnimalServiceImpl() {
        animals = new ArrayList<Animal>();
        animals.add(EntityFactory.createAnimal1());
        animals.add(EntityFactory.createAnimal2());
    }

    @Override
    public Animal getAnimal(String name) {
        LOG.info("服务端-收到异步广播消息：name={}", name);
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Animal animal : animals) {
            if (StringUtils.equals(animal.getName(), name)) {
                return animal;
            }
        }
        
        return null;
    }
}