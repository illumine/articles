package org.example.services.impl;

import org.example.entity.Log;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.lang.Strings;

@Repository
public class LogDao {

	@PersistenceContext
	private EntityManager em;

	@Transactional(readOnly = true)
	public List<Log> queryAll() {
		Query query = em.createQuery("SELECT l FROM Log l");
		List<Log> result = query.getResultList();
		return result;
	}

	@Transactional(readOnly = true)
	public List<Log> queryByCriteria(Integer id, String textToMatch){
		List<Log> result = null;
		if( id!=0 && ! Strings.isBlank( textToMatch) ) {
			Query query = em.createQuery("SELECT l FROM Log l WHERE l.id=:id and l.message LIKE :searchKeyword");
			query.setParameter("id", id);
			query.setParameter("searchKeyword", "%" + textToMatch + "%");
			result = query.getResultList();
		}else if ( id!=0 && Strings.isBlank( textToMatch) ) {
			Query query = em.createQuery("SELECT l FROM Log l WHERE l.id=:id");
			query.setParameter("id", id);
			result = query.getResultList();
		}else if ( id==0 && !Strings.isBlank( textToMatch) ) {
			Query query = em.createQuery("SELECT l FROM Log l WHERE l.message LIKE :searchKeyword");
			query.setParameter("searchKeyword", "%" + textToMatch + "%");
			result = query.getResultList();
		}else
			//impossible to get here...
			;
		return result;
	}


	@Transactional(readOnly = true)
	public List<Log> queryId(Integer id ) {

		Query query = em.createQuery("SELECT l FROM Log l WHERE l.id=:id");
		query.setParameter("id",id);
		List<Log> result = query.getResultList();
		return result;
	}


	@Transactional(readOnly = true)
	public Log get(Integer id) {
		return em.find(Log.class, id);
	}

	@Transactional
	public Log save(Log log) {
		em.persist(log);
		em.flush();
		return log;
	}

	@Transactional
	public void delete(Log log) {
		Log r = get(log.getId());
		if(r != null) {
			em.remove(r);
		}
	}

	@Transactional
	public Log edit(Log log) {
		return em.merge(log);
	}
}
