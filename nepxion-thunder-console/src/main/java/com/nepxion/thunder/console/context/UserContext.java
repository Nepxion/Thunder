package com.nepxion.thunder.console.context;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.entity.UserEntity;

public class UserContext {
    private static UserEntity userEntity;

    public static UserEntity getUserEntity() {
        return userEntity;
    }

    public static void setUserEntity(UserEntity userEntity) {
        UserContext.userEntity = userEntity;
    }
}