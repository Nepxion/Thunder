package com.nepxion.thunder.common.entity;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Arrays;
import java.util.List;

import com.nepxion.thunder.common.constant.ThunderConstants;

public class UserFactory {
    public static UserEntity createAdministrator() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(ThunderConstants.USER_ADMIN_NAME);
        userEntity.setPassword(ThunderConstants.USER_ADMIN_PASSWORD);
        userEntity.setType(UserType.ADMINISTRATOR);
        userEntity.setOperations(getAllOperations());
        
        return userEntity;
    }
    
    public static UserEntity createAdminUser(String name, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);
        userEntity.setPassword(password);
        userEntity.setType(UserType.ADMIN_USER);
        userEntity.setOperations(getAllOperations());
        
        return userEntity;
    }
    
    public static UserEntity createUser(String name, String password, List<UserOperation> operations) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);
        userEntity.setPassword(password);
        userEntity.setType(UserType.USER);
        userEntity.setOperations(operations);
        
        return userEntity;
    }
    
    public static List<UserOperation> getAllOperations() {
        return Arrays.asList(UserOperation.SERVICE_CONTROL, UserOperation.REMOTE_CONTROL, UserOperation.SAFETY_CONTROL, UserOperation.USER_CONTROL);
    }
    
    public static int getUserCompareValue(UserType type) {
        switch (type) {
            case ADMINISTRATOR:
                return 0;
            case ADMIN_USER:
                return 1;
            case USER:
                return 2;
        }

        return -1;
    }
}