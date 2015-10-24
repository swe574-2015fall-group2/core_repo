package edu.boun.swe74.pinkelephant.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseDao {

	@PersistenceContext(unitName="pinkelephant-pu")
    protected EntityManager em;
}
