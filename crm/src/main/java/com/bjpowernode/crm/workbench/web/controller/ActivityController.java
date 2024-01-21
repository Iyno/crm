package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
//        调用service层方法，查询所有的用户
        List<User>  userList=userService.queryAllUsers();
//        把数据保存到request中

        request.setAttribute("userList",userList);
        return "workbench/activity/index";

    }
/**
 * 方法定义成父类，返回的是子类的对象，多态
 * 路径最后的名和方法名要一致saveCreateActivity.do--saveCreateActivity
 */

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession httpSession){
        User user=(User)httpSession.getAttribute(Contants.SESSION_USER);
//        封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject=new ReturnObject();
//        查数据一般不会考虑异常，改变数据库的插入等需要考虑异常
        try {
//        调用service层方法，保存创建的市场活动
            int ret = activityService.saveCreateActivity(activity);
            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }

        }catch (Exception e){
            e.printStackTrace();
            returnObject.setMessage("系统忙，请稍后重试...");

        }

        return returnObject;

    }

}
