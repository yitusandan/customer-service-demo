package bs.cm.mapper;

import bs.cm.model.User;

public interface UserMapper {
    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed May 20 23:02:27 CST 2015
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed May 20 23:02:27 CST 2015
	 */
	int insert(User record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed May 20 23:02:27 CST 2015
	 */
	int insertSelective(User record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed May 20 23:02:27 CST 2015
	 */
	User selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed May 20 23:02:27 CST 2015
	 */
	int updateByPrimaryKeySelective(User record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed May 20 23:02:27 CST 2015
	 */
	int updateByPrimaryKey(User record);

	User selectByAccountname(String username);
}