package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * url要和当前controller方法处理完请求之后，响应信息返回的页面的资源目录保持一致
     * @return
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){

//        请求转发到登录页面
        return "settings/qx/user/login";
    }

    /**
     * 处理登录请求，返回哪里
     * @param loginAct
     * @param loginPwd
     * @param isRemPwd
     * @param ip
     * @return
     */
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, String ip, HttpServletRequest request
    , HttpSession session, HttpServletResponse response){

//        根据查询结果，生成响应信息
        ReturnObject returnObject = new ReturnObject();
//        封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
//        调用service层方法，查询用
        User user = userService.queryUserByLoginActAndPwd(map);
//        根据查询结果，生成响应信息
        if (user == null){
//            用户名或密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
//            return "用户名或密码错误";
        }else {
//            进一步判断账号是否合法
//            user.getExpireTime();
//            new Date();
//            两个字符串的比较compareTo;判断有没有过期
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String nowStr = sdf.format(new Date());

            if(DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime()) > 0){
//                登录失败，账号已失效
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登录失败，账号已失效");

            }
            else if("0".equals(user.getLockState())){
//                登录失败，账号已锁定；判断是不是被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登录失败，账号已锁定");
            }
            else if(!user.getAllowIps().contains(request.getRemoteAddr())){
//                登录失败，ip地址受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登录失败，ip地址受限");
            }
            else {
//                登录成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
//                将user对象保存到session中
                session.setAttribute(Contants.SESSION_USER,user);
//                如果需要记住密码，则往外写cookie
                if("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct",user.getLoginAct());
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd",user.getLoginPwd());
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2);
                }else {
//                    不需要记住密码，清除cookie
                    Cookie c1 = new Cookie("loginAct","");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd","");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }

        }

        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
//        清空cookie
        Cookie c1 = new Cookie("loginAct","");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd","");
        c2.setMaxAge(0);
        response.addCookie(c2);
//        销毁session
        session.invalidate();
//        跳转到首页,请求转发和重定向的区别：请求转发是一次请求，重定向是两次请求


        return "redirect:/";//借用springmvc来重定向，respense.sendRedirect("/");
    }


}
