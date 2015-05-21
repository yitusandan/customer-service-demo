package bs.cm.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import bs.cm.mapper.CustomerMapper;
import bs.cm.model.Customer;

@Service
public class CustomerService {

	@Resource
	private CustomerMapper customerMapper;


	public List<Customer> selectAll() {
		return customerMapper.selectAll();
	}

	public List<Customer> selectByUserid(Integer userid) {
		return customerMapper.selectByUserid(userid);
	}
	
	public void insertSelective(Customer customer) {
		customerMapper.insertSelective(customer);
	}
}
