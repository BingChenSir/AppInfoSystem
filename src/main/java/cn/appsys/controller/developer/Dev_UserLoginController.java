package cn.appsys.controller.developer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import cn.appsys.pojo.Devuser;
import cn.appsys.service.devuser.DevUserService;

@Controller
@RequestMapping("/devLogin")
public class Dev_UserLoginController {

	private Logger logger = Logger.getLogger(Dev_UserLoginController.class);
	
	@Resource
	private DevUserService devUserService;
	
	@RequestMapping("/login")
	public String login(){
		logger.debug("login  ==============================");
		return "devlogin";
	}
	
	@RequestMapping(value="/doLogin",method=RequestMethod.POST)
	public String doLogin(@RequestParam String devCode,
						  @RequestParam	String devPassword,
						  HttpServletRequest request,
						  HttpSession session){
		logger.debug("doLogin ==============================");
		Devuser devUser = null;
		//调用service方法验证登录
		devUser = devUserService.getLoginUser(devCode, devPassword);
		if(null != devUser){
			//登录成功
			session.setAttribute("devUserSession",devUser);
			return "redirect:/devLogin/devuser/main";
		}else{
			//登录失败
			//页面跳转（login.jsp）带出提示信息--转发
			request.setAttribute("error", "用户名或密码不正确");
			return "devlogin";
		}
	}
	
	@RequestMapping("/devuser/main")
	public String main(HttpSession session){
		if(session.getAttribute("devUserSession") == null){
			return "redirect:/devLogin/login";
		}
		return "developer/main";
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpSession session){
		//清除session
		session.invalidate();
		return "devlogin";
	}
	
}
