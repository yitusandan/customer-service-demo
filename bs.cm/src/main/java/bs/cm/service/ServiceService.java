package bs.cm.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import bs.cm.mapper.ServiceMapper;

@Service
public class ServiceService {

	@Resource
	private ServiceMapper serviceMapper;

	public List<bs.cm.model.Service> selectAll() {
		return serviceMapper.selectAll();
	}

	public List<bs.cm.model.Service> selectByUserid(Integer userid) {
		return serviceMapper.selectByUserid(userid);
	}

	public void insert(bs.cm.model.Service service) {
		serviceMapper.insertSelective(service);
	}

}
