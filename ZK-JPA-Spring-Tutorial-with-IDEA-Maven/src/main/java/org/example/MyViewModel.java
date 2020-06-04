package org.example;

import org.example.services.MyService;
import org.example.entity.Log;

import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MyViewModel {

	@WireVariable
	private MyService myService;
	private ListModelList<Log> logListModel;
	private String message;
	private String id;

	@Init
	public void init() {
		List<Log> logList = myService.getLogs();
		logListModel = new ListModelList<Log>(logList);
	}

	public ListModel<Log> getLogListModel() {
		return logListModel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Command
	public void addLog() {
		if(Strings.isBlank(message)) {
			return;
		}
		Log log = new Log(message);
		log = myService.addLog(log);
		logListModel.add(log);
	}

	@Command
	public void deleteLog(@BindingParam("log") Log log) {
		myService.deleteLog(log);
		logListModel.remove(log);
	}


	@Command
	public void editLog(@BindingParam("log") Log log) {
		Log ref = myService.getLog(log.getId());
		int listIndex = logListModel.indexOf(ref);
		ref.setMessage(message);
		ref.setDate( new Date() );
		myService.editLog(ref);
		logListModel.set(listIndex, ref);
	}

	@Command
	@NotifyChange("message")
	public void getLog(){
		if(Strings.isBlank(id)) {
			return;
		}
		Log log = myService.getLog(  Integer.parseInt(id) );
		message = log.getMessage();
		System.out.println("THe message is " + message + " id " + id);
	}

}
