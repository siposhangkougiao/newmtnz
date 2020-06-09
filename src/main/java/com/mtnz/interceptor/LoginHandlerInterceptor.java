package com.mtnz.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mtnz.controller.app.user.model.LoginSalt;
import com.mtnz.service.system.user.LoginSaltService;
import com.mtnz.util.Md5Util;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mtnz.entity.system.User;
import com.mtnz.util.Const;
import com.mtnz.util.Jurisdiction;

/**
 * 类名称：LoginHandlerInterceptor.java 类描述：
 * @author FH 作者单位： 联系方式： 创建时间：2015年1月1日
 * @version 1.6
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

	@Resource
	LoginSaltService loginSaltService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String path = request.getServletPath();
		String ip = request.getRemoteAddr();

		System.out.println("请求的ip是:"+ip);
		System.out.println("请求的路径是:"+path);
		//System.out.println("ip:"+request.getHeader("ip"));
		if(!path.equals("/app/user/login")&&!path.equals("/app/mystore/getcode")&&!path.equals("/app/mystore/saveStore")
				&&!path.equals("/app/statistical")&&!path.contains("jpg")&&!path.equals("/app/user/verification")
				&&!path.equals("/app/user/ForgetPassowrd")&&!path.equals("/app/store/loagin")
				&&!path.equals("/app/appusernew/code")&&!path.equals("/app/appusernew/login")){
			String token = request.getHeader("token");
			String storeId = request.getHeader("storeId");
			System.out.println("token:"+token+",storeId:"+storeId);
			if(StringUtils.isBlank(token)||StringUtils.isBlank(storeId)){
				//response.setStatus(-802);//缺少参数
				response.addHeader("status","-802");
				return false;
			}
			/*LoginSalt loginSalt = loginSaltService.select(storeId);
			if(loginSalt==null){
				//response.setStatus(-803);//用户未找到
				response.addHeader("status","-803");
				return false;
			}*/
			String salt = "Nsakj";
			String mytoken = Md5Util.md5(storeId,salt);
			System.out.println(">>>>>>>"+mytoken);
			if(!token.equals(mytoken)){
				//response.setStatus(-801);//token不正确
				response.addHeader("status","-801");
				return false;
			}
		}

		if (path.matches(Const.NO_INTERCEPTOR_PATH)) {
			return true;
		} else {
			// shiro管理的session
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession();
			User user = (User) session.getAttribute(Const.SESSION_USER);
			if (user != null) {
				path = path.substring(1, path.length());
				boolean b = Jurisdiction.hasJurisdiction(path);
				if (!b) {
					response.sendRedirect(request.getContextPath() + Const.LOGIN);
				}
				return b;
			} else {
				// 登陆过滤
				response.sendRedirect(request.getContextPath() + Const.LOGIN);
				return false;
				// return true;
			}
		}
	}

}
