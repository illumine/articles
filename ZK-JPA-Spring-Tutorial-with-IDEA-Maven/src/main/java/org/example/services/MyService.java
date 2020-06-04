package org.example.services;

import org.example.entity.Log;
import java.util.List;

public interface MyService {

	Log addLog(Log log);

	List<Log> getLogs();

	void deleteLog(Log log);

	Log editLog(Log log);

	Log getLog(Integer id);

	List<Log> getById(Integer id);

	List<Log> getByCriteria(Integer id, String textToMatch);
}
