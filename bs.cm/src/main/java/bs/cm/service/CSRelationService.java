package bs.cm.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import bs.cm.mapper.CsrelationMapper;
import bs.cm.model.Csrelation;

@Service
public class CSRelationService {

	@Resource
	private CsrelationMapper csrelationMapper;

	public List<Csrelation> selectAll() {
		return csrelationMapper.selectAll();
	}

	public List<Csrelation> selectByUserid(Integer userid) {
		return csrelationMapper.selectByUserid(userid);
	}

	public void insert(Csrelation csrelation) {
		csrelationMapper.insertSelective(csrelation);
	}

}
