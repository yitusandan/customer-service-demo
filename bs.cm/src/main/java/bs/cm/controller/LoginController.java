package bs.cm.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import bs.cm.model.User;
import bs.cm.service.UserService;

import com.opensymphony.xwork2.ActionSupport;

@Controller
public class LoginController extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7051977626608855833L;

	private Logger logger = Logger.getLogger(getClass());

	@Resource
	private UserService userService;

	@RequestMapping("/mobile/login.do")
	public void login(String accountname, String password,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		JsonResult jsonResult = new JsonResult();
		logger.info("username = " + accountname + ", password = " + password);
		if (StringUtils.isEmpty(accountname)) {
			jsonResult.setErr(1, "用户名为空");
			response.getWriter().write(jsonResult.toJson());
			return;
		}

		if (StringUtils.isEmpty(password)) {
			jsonResult.setErr(1, "密码为空");
			response.getWriter().write(jsonResult.toJson());
			return;
		}

		User user = userService.getUserByAccountname(accountname);
		if (user == null) {
			jsonResult.setErr(1, "该用户还未注册");
			response.getWriter().write(jsonResult.toJson());
			return;
		}

		if (!password.equals(user.getPassword())) {
			jsonResult.setErr(1, "密码错误");
			response.getWriter().write(jsonResult.toJson());
			return;
		}

		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(-1);
		logger.info("sessionId = " + session.getId());
		user.setSessionToken(session.getId());
		jsonResult.add("user", user);
		response.getWriter().write(jsonResult.toJson());

		// return "/index.jsp";
	}

	@RequestMapping("/mobile/signup.do")
	public void signup(String username, String accountname, String password,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		logger.info("username = " + username + ", accountname = " + accountname
				+ ", password = " + password);

		JsonResult jsonResult = new JsonResult();

		if (StringUtils.isEmpty(username)) {
			return;
		}
		if (StringUtils.isEmpty(accountname)) {
			return;
		}
		if (StringUtils.isEmpty(password)) {
			return;
		}

		User user = userService.getUserByAccountname(accountname);
		if (user != null) {
			jsonResult.setErr(1, "账号已存在，请直接登陆");
			return;
		}

		user = new User();
		user.setUsername(username);
		user.setAccountname(accountname);
		user.setPassword(password);

		userService.insert(user);

		user = userService.getUserByAccountname(accountname);
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(-1);
		logger.info("sessionId = " + session.getId());
		user.setSessionToken(session.getId());

		jsonResult.add("user", user);
		response.getWriter().write(jsonResult.toJson());
	}
}
