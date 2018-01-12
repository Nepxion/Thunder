package com.nepxion.thunder;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

import com.nepxion.thunder.test.core.AnimalService;
import com.nepxion.thunder.test.core.CoreInvoker;
import com.nepxion.thunder.test.core.UserService;

@EnableAutoConfiguration
@ImportResource({ "classpath*:netty-client-context.xml" })
public class NettyClientCommandLineApplication implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private AnimalService animalService;

    @Override
    public void run(String... strings) throws Exception {
        CoreInvoker.invoke1(userService, animalService);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(NettyClientCommandLineApplication.class, args);
    }
}