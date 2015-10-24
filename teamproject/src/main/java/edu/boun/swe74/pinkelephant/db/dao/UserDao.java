package edu.boun.swe74.pinkelephant.db.dao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import edu.boun.swe74.pinkelephant.common.util.ValidatorUtil;
import edu.boun.swe74.pinkelephant.db.model.User;

@Local
@Stateless
public class UserDao extends BaseDao {

	public User getUser(String username, String password) {

		String queryString = "SELECT u from User u where u.username=:username and u.password=:password";

		Query query = em.createQuery(queryString);

		query.setParameter("username", username);
		query.setParameter("password", password);

		List<User> userlist = query.getResultList();
		
		return ValidatorUtil.isNull(userlist) ? null : userlist.get(0);
	}
}
