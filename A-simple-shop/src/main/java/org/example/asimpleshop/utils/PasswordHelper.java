package org.example.asimpleshop.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.lang.util.ByteSource;

public class PasswordHelper {

    /** 算法名，需与 ShiroConfig 的 setHashAlgorithmName 一致 */
    public static final String ALGORITHM_NAME = "md5";

    /** 迭代次数，需与 ShiroConfig 的 setHashIterations 一致 */
    public static final int HASH_ITERATIONS = 1;

    /** 随机盐：Shiro 自带的密码学安全随机数，32 位十六进制 */
    public static String randomSalt() {
        return new SecureRandomNumberGenerator().nextBytes().toHex();
    }

    /** 加盐哈希：注意参数顺序是算法+明文+盐+迭代次数，写反了永远对不上 */
    public static String encrypt(String rawPassword, String salt) {
        return new SimpleHash(ALGORITHM_NAME, rawPassword,
                ByteSource.Util.bytes(salt), HASH_ITERATIONS).toHex();
    }

    /** 手动校验，留给单元测试用；走 Shiro 登录时用不到 */
    public static boolean matches(String rawPassword, String salt, String storedHash) {
        return encrypt(rawPassword, salt).equals(storedHash);
    }
}
