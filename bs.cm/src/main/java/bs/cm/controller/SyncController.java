package bs.cm.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import bs.cm.model.Csrelation;
import bs.cm.model.Customer;
import bs.cm.model.Service;
import bs.cm.service.CSRelationService;
import bs.cm.service.CustomerService;
import bs.cm.service.ServiceService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

@Controller
public class SyncController extends ActionSupport {

	private Logger logger = Logger.getLogger(getClass());

	@Resource
	private CustomerService customerService;

	@Resource
	private ServiceService serviceService;

	@Resource
	private CSRelationService cSRelationService;

	@RequestMapping("/mobile/getAll.do")
	public void getAll(String token, String userId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		String sessionId = request.getSession().getId();
		logger.info("token = " + token + ", sessionId = " + sessionId);
		logger.info("userId = " + userId);
		JsonResult jsonResult = new JsonResult();
		Integer userIdInt = Integer.valueOf(userId);
		List<Customer> customerList = customerService.selectByUserid(userIdInt);
		List<Service> serviceList = serviceService.selectByUserid(userIdInt);
		List<Csrelation> csrelationList = cSRelationService.selectByUserid(userIdInt);

		jsonResult.add("customerList", customerList);
		jsonResult.add("servcieList", serviceList);
		jsonResult.add("csrelationList", csrelationList);

		response.getWriter().write(jsonResult.toJson());
	}

	@RequestMapping("/mobile/add.do")
	public void add(String token, String customerStr, String serviceStr,
			String relationStr, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		String sessionId = request.getSession().getId();
		logger.info("customerStr = " + customerStr);
		logger.info("serviceStr = " + serviceStr);
		logger.info("relationStr = " + relationStr);
		logger.info("token = " + token + ", sessionId = " + sessionId);

		JsonResult jsonResult = new JsonResult();
		if (!StringUtils.isEmpty(customerStr)) {
			List<Customer> customerList = new Gson().fromJson(customerStr,
					new TypeToken<List<Customer>>() {
					}.getType());
			for (Customer customer : customerList) {
				customerService.insertSelective(customer);
			}
		}
		if (!StringUtils.isEmpty(serviceStr)) {
			List<Service> serviceList = new Gson().fromJson(serviceStr,
					new TypeToken<List<Service>>() {
					}.getType());
			for (Service service : serviceList) {
				serviceService.insert(service);
			}

		}
		if (!StringUtils.isEmpty(relationStr)) {
			List<Csrelation> csrelationList = new Gson().fromJson(relationStr,
					new TypeToken<List<Csrelation>>() {
					}.getType());
			for (Csrelation csrelation : csrelationList) {
				cSRelationService.insert(csrelation);
			}
		}

		response.getWriter().write(jsonResult.toJson());

	}
}
