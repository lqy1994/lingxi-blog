package cn.edu.sdu.wh.lqy.lingxi.blog.controller.admin;

import cn.edu.sdu.wh.lqy.lingxi.blog.constant.WebConstant;
import cn.edu.sdu.wh.lqy.lingxi.blog.controller.BaseController;
import cn.edu.sdu.wh.lqy.lingxi.blog.exception.LingXiException;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Bo.ApiResponse;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.User;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.dto.LogActions;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.ILogService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IUserService;
import cn.edu.sdu.wh.lqy.lingxi.blog.utils.RSAUtils;
import cn.edu.sdu.wh.lqy.lingxi.blog.utils.StringUtils;
import cn.edu.sdu.wh.lqy.lingxi.blog.utils.TaleUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户后台登录/登出
 */
@Controller
//@RequestMapping("/admin")
@Transactional(rollbackFor = LingXiException.class)
public class AdminLoginController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminLoginController.class);

    @Autowired
    private IUserService usersService;

    @Autowired
    private ILogService logService;
    //
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/login")
    public String login(Model model) {

        Map<String, Object> publicKeyMap = RSAUtils.getPublicKeyMap();
        model.addAttribute("publicKeyMap", publicKeyMap);

        return "admin/user/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiResponse login(HttpServletRequest request, @RequestParam("userName") String userName,
                             @RequestParam("password") String password, @RequestParam("code") String code,
                             @RequestParam("rememberMe") Boolean rememberMe) {

        if (org.apache.commons.lang.StringUtils.isEmpty(code) || "null".equalsIgnoreCase(code.trim())) {
            return ApiResponse.fail(102, "验证码错误");
        }

        Session session = super.getSession();
        String sessionCode = (String) session.getAttribute("_code");
        if (!code.toLowerCase().equals(sessionCode)) {
            return ApiResponse.fail(102, "验证码错误");
        }
        LOGGER.info("GifCode: " + sessionCode);

        String realPassword = RSAUtils.decryptStringByJs(password);
        LOGGER.info("realPassword:{}", realPassword);

        UsernamePasswordToken token = new UsernamePasswordToken(userName, realPassword);
        token.setRememberMe(rememberMe);

        Subject currentUser = SecurityUtils.getSubject();

        try {
            currentUser.login(token);
            User user = usersService.getUserByName(userName);
            session.setAttribute(WebConstant.LOGIN_SESSION_KEY, user);
            return ApiResponse.ok(200);
        } catch (UnknownAccountException e) {
            return  ApiResponse.fail(103,"未知账户");
        } catch (DisabledAccountException e) {
            return  ApiResponse.fail(104, "账户冻结，请联系管理员");

        }

    }


//    @PostMapping(value = "login")
//    @ResponseBody
//    public ApiResponse doLogin(@RequestParam String username,
//                               @RequestParam String password,
//                               @RequestParam(required = false) String remeber_me,
//                               HttpServletRequest request,
//                               HttpServletResponse response) {
//
//        String errorCount = redisTemplate.opsForValue().get("login_error_count");
//        Integer errCnt = 0;
//        if (StringUtils.isNotNull(errorCount)) {
//            errCnt = Integer.valueOf(errorCount);
//        } else {
//            redisTemplate.opsForValue().set("login_error_count", (errCnt + 1) + "", 10 * 60, TimeUnit.SECONDS);
//        }
//        try {
//            User user = usersService.login(username, password);
//            request.getSession().setAttribute(WebConstant.LOGIN_SESSION_KEY, user);
//            if (StringUtils.isNotBlank(remeber_me)) {
//                TaleUtils.setCookie(response, user.getUid());
//            }
//            logService.insertLog(LogActions.LOGIN.getAction(), null, request.getRemoteAddr(), user.getUid());
//        } catch (Exception e) {
//            errCnt = errCnt == 0 ? 1 : errCnt + 1;
//            if (errCnt > 3) {
//                return ApiResponse.fail("您输入密码已经错误超过3次，请10分钟后尝试");
//            }
//            redisTemplate.opsForValue().set("login_error_count", errCnt + "", 10 * 60, TimeUnit.SECONDS);
//            String msg = "登录失败";
//            if (e instanceof LingXiException) {
//                msg = e.getMessage();
//            } else {
//                LOGGER.error(msg, e);
//            }
//            return ApiResponse.fail(msg);
//        }
//        return ApiResponse.ok();
//    }

    /**
     * 注销
     *
     * @param session
     * @param response
     */
    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        session.removeAttribute(WebConstant.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(WebConstant.USER_IN_COOKIE, "");
        cookie.setValue(null);
        cookie.setMaxAge(0);// 立即销毁cookie
        cookie.setPath("/");
        response.addCookie(cookie);
        try {
            response.sendRedirect("/admin/login");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("注销失败", e);
        }
    }

}
