package com.nepxion.thunder.boot;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

import com.nepxion.thunder.boot.service.AnimalService;
import com.nepxion.thunder.boot.service.UserService;

@EnableAutoConfiguration
@ImportResource({ "classpath*:netty-client-context.xml" })
public class NettyClientBootRunnerTest implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private AnimalService animalService;

    @Override
    public void run(String... strings) throws Exception {
        ClientTest.test1(userService, animalService);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(NettyClientBootRunnerTest.class, args);
    }
}