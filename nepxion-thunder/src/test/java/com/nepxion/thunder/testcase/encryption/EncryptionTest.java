package com.nepxion.thunder.testcase.encryption;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.util.EncryptionUtil;

public class EncryptionTest {
    private static final Logger LOG = LoggerFactory.getLogger(EncryptionTest.class);

    @Test
    public void test() throws Exception {
        String password = "abcd1234";
        LOG.info("MD5={}", EncryptionUtil.encryptMD5(password));
        LOG.info("SHA={}", EncryptionUtil.encryptSHA(password));
        LOG.info("SHA256={}", EncryptionUtil.encryptSHA256(password));
        LOG.info("SHA512={}", EncryptionUtil.encryptSHA512(password));
    }
}