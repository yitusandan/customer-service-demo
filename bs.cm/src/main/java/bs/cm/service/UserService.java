package bs.cm.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import bs.cm.mapper.UserMapper;
import bs.cm.model.User;

@Service
public class UserService {

	@Resource
	private UserMapper userMapper;

	public User getUserByAccountname(String acccountname) {
		return userMapper.selectByAccountname(acccountname);
	}

	public int insert(User user) {
		return userMapper.insertSelective(user);
	}

}
