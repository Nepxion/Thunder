package com.nepxion.thunder.console.controller;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;

import com.nepxion.swing.element.ElementNode;
import com.nepxion.thunder.common.entity.UserEntity;
import com.nepxion.thunder.common.entity.UserOperation;
import com.nepxion.thunder.common.entity.UserType;
import com.nepxion.thunder.console.context.RegistryContext;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.registry.zookeeper.ZookeeperRegistryExecutor;
import com.nepxion.thunder.registry.zookeeper.ZookeeperUserWatcherCallback;

public class UserController {
    public static List<UserEntity> getUserList() throws Exception {
        return RegistryContext.getDefaultRegistryExecutor().retrieveUserList();
    }

    public static UserEntity getUser(String name) throws Exception {
        return RegistryContext.getDefaultRegistryExecutor().retrieveUser(name);
    }

    public static UserEntity validateUser(String name, String password) throws Exception {
        UserEntity userEntity = getUser(name);
        if (userEntity == null) {
            return null;
        }
        
        if (userEntity.validatePassword(password)) {
            return userEntity;
        }

        return null;
    }

    public static void createUser(UserEntity userEntity) throws Exception {
        RegistryContext.getDefaultRegistryExecutor().persistUser(userEntity);
    }

    public static void deleteUser(UserEntity userEntity) throws Exception {
        RegistryContext.getDefaultRegistryExecutor().deleteUser(userEntity);
    }

    public static void addUserWatcher(UserEntity userEntity, ZookeeperUserWatcherCallback<UserEntity> callback) throws Exception {
        ZookeeperRegistryExecutor registryExecutor = (ZookeeperRegistryExecutor) RegistryContext.getDefaultRegistryExecutor();
        registryExecutor.addUserWatcher(userEntity, callback);
    }

    public static ElementNode[] getUserNodes() throws Exception {
        List<UserEntity> userEntityList = getUserList();

        ElementNode[] userEntityNodes = new ElementNode[userEntityList.size()];
        for (int i = 0; i < userEntityList.size(); i++) {
            UserEntity userEntity = userEntityList.get(i);
            userEntityNodes[i] = createUserNode(userEntity);
        }

        return userEntityNodes;
    }

    public static ElementNode createUserNode(UserEntity userEntity) {
        String name = userEntity.getName();
        UserType type = userEntity.getType();

        String typeName = getUserTypeName(type);
        String typeIcon = getUserTypeIcon(type, true);

        return new ElementNode(name, name, ConsoleIconFactory.getSwingIcon(typeIcon), typeName, userEntity);
    }

    public static ElementNode[] getUserTypeNodes(UserType type) {
        ElementNode[] typeNodes = new ElementNode[1];
        typeNodes[0] = createUserTypeNode(type);

        return typeNodes;
    }

    public static ElementNode[] getNewUserTypeNodes(UserType loginType) {
        ElementNode[] typeNodes = null;
        if (loginType == UserType.ADMINISTRATOR) {
            typeNodes = new ElementNode[2];
            typeNodes[0] = createUserTypeNode(UserType.ADMIN_USER);
            typeNodes[1] = createUserTypeNode(UserType.USER);
        } else if (loginType == UserType.ADMIN_USER) {
            typeNodes = new ElementNode[1];
            typeNodes[0] = createUserTypeNode(UserType.USER);
        } else if (loginType == UserType.USER) {
            typeNodes = new ElementNode[0];
        }

        return typeNodes;
    }

    public static ElementNode createUserTypeNode(UserType type) {
        String typeName = getUserTypeName(type);
        String typeIcon = getUserTypeIcon(type, false);

        return new ElementNode(type.toString(), typeName, ConsoleIconFactory.getSwingIcon(typeIcon), typeName, type);
    }

    public static String getUserTypeName(UserType type) {
        switch (type) {
            case ADMINISTRATOR:
                return ConsoleLocale.getString("administrator");
            case ADMIN_USER:
                return ConsoleLocale.getString("admin_user");
            case USER:
                return ConsoleLocale.getString("user");
        }

        return null;
    }

    public static String getUserTypeIcon(UserType type, boolean largeStyle) {
        switch (type) {
            case ADMINISTRATOR:
                if (largeStyle) {
                    return "user_1_24.png";
                } else {
                    return "user_1_16.png";
                }
            case ADMIN_USER:
                if (largeStyle) {
                    return "user_2_24.png";
                } else {
                    return "user_2_16.png";
                }
            case USER:
                if (largeStyle) {
                    return "user_3_24.png";
                } else {
                    return "user_3_16.png";
                }
        }

        return null;
    }

    public static ElementNode[] getUserOperationNodes(UserEntity userEntity) {
        UserType type = userEntity.getType();
        List<UserOperation> operations = userEntity.getOperations();

        ElementNode[] operationNodes = getUserOperationNodes(type);
        for (int i = 0; i < operationNodes.length; i++) {
            UserOperation userOperation = (UserOperation) operationNodes[i].getUserObject();
            for (UserOperation operation : operations) {
                if (userOperation == operation) {
                    operationNodes[i].setSelected(true);
                    break;
                }
            }
        }
        
        return operationNodes;
    }

    public static ElementNode[] getUserOperationNodes(UserType type) {
        ElementNode[] operationNodes = null;
        if (type == UserType.ADMINISTRATOR || type == UserType.ADMIN_USER) {
            operationNodes = new ElementNode[4];
            operationNodes[0] = createUserOperationNode(UserOperation.SERVICE_CONTROL);
            operationNodes[1] = createUserOperationNode(UserOperation.REMOTE_CONTROL);
            operationNodes[2] = createUserOperationNode(UserOperation.SAFETY_CONTROL);
            operationNodes[3] = createUserOperationNode(UserOperation.USER_CONTROL);
        } else if (type == UserType.USER) {
            operationNodes = new ElementNode[3];
            operationNodes[0] = createUserOperationNode(UserOperation.SERVICE_CONTROL);
            operationNodes[1] = createUserOperationNode(UserOperation.REMOTE_CONTROL);
            operationNodes[2] = createUserOperationNode(UserOperation.SAFETY_CONTROL);
        }

        return operationNodes;
    }

    
    public static ElementNode createUserOperationNode(UserOperation operation) {
        String operationName = getUserOperationName(operation);

        return new ElementNode(operation.toString(), operationName, ConsoleIconFactory.getSwingIcon("status.png"), operationName, operation);
    }

    public static String getUserOperationName(UserOperation operation) {
        switch (operation) {
            case SERVICE_CONTROL:
                return ConsoleLocale.getString("service_control");
            case REMOTE_CONTROL:
                return ConsoleLocale.getString("remote_control");
            case SAFETY_CONTROL:
                return ConsoleLocale.getString("safety_control");
            case USER_CONTROL:
                return ConsoleLocale.getString("user_control");
        }

        return null;
    }
}