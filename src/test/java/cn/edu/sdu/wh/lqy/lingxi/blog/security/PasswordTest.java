package cn.edu.sdu.wh.lqy.lingxi.blog.security;

import cn.edu.sdu.wh.lqy.lingxi.blog.utils.PasswordUtils;
import org.junit.Test;

public class PasswordTest {

    @Test
    public void testPwd() {
        String salt = "Z3o6er";/*PasswordUtils.getSalt();*/
        String md5 = PasswordUtils.getMd5("123456", "admin", salt);
        System.out.println(salt); //ofmY4Z
        System.out.println(PasswordUtils.getCredentialsSalt("admin", salt)); //lqyofmY4Z
        System.out.println(md5); //407806075d6b882d52b40108a62360de

//        System.out.println("005e45443ffe8cb72d5d27c95844fb20".equalsIgnoreCase(md5));

//        User lqy = userService.getUserByLoginName("lqy");
//
//
//        AuthorizingUser authorizingUser = new AuthorizingUser();
//        BeanUtils.copyProperties(lqy, authorizingUser);
//
//        String credentialsSalt = authorizingUser.getCredentialsSalt();
//        ByteSource source = ByteSource.Util.bytes(credentialsSalt);
//        System.out.println(new String(source.getBytes()));
//
//        UsernamePasswordToken token = new UsernamePasswordToken(lqy.getUserName(), "123456");
//
//        Subject subject = SecurityUtils.getSubject();
//        subject.login(token);

    }
}
