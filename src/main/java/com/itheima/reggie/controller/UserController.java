package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (phone != null && !phone.equals("") ){
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("生成的验证码为："+code);

            //发送短信
            //SMSUtils.sendMessage("","",phone,code);
            session.setAttribute(phone,code);
            return R.success("发送成功");
        }

        return R.error("发送失败");
    }
    @PostMapping("login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        String sessionCode = (String) session.getAttribute(phone);
        if (sessionCode != null && sessionCode.equals(code)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

}
