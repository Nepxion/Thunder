package com.nepxion.thunder.security;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.protocol.ProtocolRequest;
import com.nepxion.thunder.protocol.ProtocolResponse;

public interface SecurityExecutor extends ThunderDelegate {
    // 鉴权
    boolean execute(ProtocolRequest request, ProtocolResponse response);

    // 鉴权    
    boolean execute(HttpServletRequest request, HttpServletResponse response) throws IOException;
}