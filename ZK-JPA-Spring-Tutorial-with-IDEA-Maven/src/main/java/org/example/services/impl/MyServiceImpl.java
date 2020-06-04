package org.example.services.impl;

import org.example.services.MyService;
import org.example.entity.Log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("myService")
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyServiceImpl implements MyService {

	@Autowired
	LogDao dao;

	public Log addLog(Log log) {
		return dao.save(log);
	}

	public List<Log> getLogs() {
		return dao.queryAll();
	}

	public void deleteLog(Log log) {
		dao.delete(log);
	}

	public Log editLog(Log log) { return dao.edit(log); }

	public Log getLog(Integer id){ return dao.get(id); }

	public  List<Log> getById(Integer id) {
		return dao.queryId(id);
	}

	public  List<Log> getByCriteria(Integer id, String textToMatch) {
		return dao.queryByCriteria(id,textToMatch);
	}
}
