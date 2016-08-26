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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.thunder.common.util.EncryptionUtil;

public class UserEntity implements Serializable {
    private static final long serialVersionUID = 5359982137894996735L;
    
    private String name;
    private String password;
    private UserType type;
    private List<UserOperation> operations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = encryptPassword(password);
    }
    
    public boolean validatePassword(String password) {
        return StringUtils.equals(this.password, encryptPassword(password));
    }
    
    public boolean validatePassword(UserEntity userEntity) {
        return StringUtils.equals(this.password, userEntity.password);
    }
    
    private String encryptPassword(String password) {
        try {
            password = EncryptionUtil.encryptSHA256(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public List<UserOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<UserOperation> operations) {
        if (operations == null) {
            throw new IllegalArgumentException("Operations can't be null");
        }
        
        this.operations = new ArrayList<UserOperation>();

        for (UserOperation operation : operations) {
            if (Collections.frequency(this.operations, operation) < 1) {
                this.operations.add(operation);
            }
        }
    }
    
    public boolean hasOperation(UserOperation operation) {
        if (CollectionUtils.isEmpty(operations)) {
            return false;
        }
        
        for (UserOperation userOperation : operations) {
            if (userOperation == operation) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (name != null) {
            result = 37 * result + name.hashCode();
        }

        if (password != null) {
            result = 37 * result + password.hashCode();
        }

        if (type != null) {
            result = 37 * result + type.hashCode();
        }

        if (operations != null) {
            result = 37 * result + operations.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserEntity)) {
            return false;
        }

        UserEntity userEntity = (UserEntity) object;
        if (StringUtils.equals(this.name, userEntity.name)
                && StringUtils.equals(this.password, userEntity.password)
                && this.type == userEntity.type
                && CollectionUtils.isEqualCollection(this.operations, userEntity.operations)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name=");
        builder.append(name);
        builder.append(", type=");
        builder.append(type);
        builder.append(", operations=");
        builder.append(operations);

        return builder.toString();
    }
}