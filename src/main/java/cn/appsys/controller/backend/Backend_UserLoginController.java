package cn.appsys.controller.backend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import cn.appsys.pojo.Backenduser;
import cn.appsys.service.backenduser.BackendUserService;

@Controller
@RequestMapping("/userLogin")
public class Backend_UserLoginController {

	private Logger logger = Logger.getLogger(Backend_UserLoginController.class);
	
	@Resource
	private BackendUserService backendUserService;
	
	@RequestMapping("/login")
	public String login(){
		logger.debug("login  ==============================");
		return "backendlogin";
	}
	
	@RequestMapping(value="/doLogin",method=RequestMethod.POST)
	public String doLogin(@RequestParam String userCode,
						  @RequestParam	String userPassword,
						  HttpServletRequest request,
						  HttpSession session){
		logger.debug("doLogin ==============================");
		Backenduser backend_user = null;
		//调用service方法验证登录
		System.err.println(" =======================>>>>>>  "+userPassword);
		backend_user = backendUserService.getLoginUser(userCode, userPassword);
		if(null != backend_user){
			//登录成功
			session.setAttribute("userSession",backend_user);
			return "redirect:/userLogin/backend/main";
		}else{
			//登录失败
			//页面跳转（login.jsp）带出提示信息--转发
			request.setAttribute("error", "用户名或密码不正确");
			return "backendlogin";
		}
	}
	
	@RequestMapping("/backend/main")
	public String main(HttpSession session){
		
		if(session.getAttribute("userSession") == null){
			return "redirect:/userLogin/login";
		}
		return "backend/main";
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpSession session){
		//清除session
		session.invalidate();
		return "backendlogin";
	}
	
}
