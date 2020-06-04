package org.example;

import org.example.services.MyService;
import org.example.entity.Log;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import java.util.Date;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MyViewModel2 {

	@WireVariable
	private MyService myService;
	private ListModelList<Log> logListModel;
	private Log selectedLog;
    private String operationMessage;

	@Init
	public void init() {
		List<Log> logList = myService.getLogs();
		logListModel = new ListModelList<Log>(logList);
		selectedLog = new Log();
		operationMessage = "";
	}

	public ListModel<Log> getLogListModel() {
		return logListModel;
	}

	public Log getSelectedLog() {
		return selectedLog;
	}

	public void setSelectedLog(Log selectedLog) {
		this.selectedLog = selectedLog;
	}

	public String getOperationMessage() {
		return operationMessage;
	}

	public void setOperationMessage(String operationMessage) {
		this.operationMessage = operationMessage;
	}

	@Command
	@NotifyChange({"operationMessage","logListModel", "selectedLog"})
	public void addLog() {
		if( selectedLog == null ||  Strings.isBlank( selectedLog.getMessage() )) {
			operationMessage = "addLog(): selectedLog is null or empty!";
			return;
		}
		Log newLog = new Log();
		newLog.setMessage( selectedLog.getMessage() );
		newLog = myService.addLog(newLog);
		logListModel.add(newLog);
		operationMessage = "addLog():  added selectedLog " + newLog.toString();
	}

	@Command
	@NotifyChange({"operationMessage","logListModel", "selectedLog"})
	public void deleteLog(@BindingParam("log") Log log) {
		if( selectedLog == null ||  selectedLog.getId() == null ) {
			operationMessage = "deleteLog(): selectedLog is null or has no ID!";
			return;
		}
		myService.deleteLog(selectedLog);
		logListModel.remove(selectedLog);
		operationMessage = "addLog():  removed selectedLog " + selectedLog.toString();
		selectedLog = new Log();

		List<Log> logList = myService.getLogs();
		logListModel = new ListModelList<Log>(logList);
	}


	@Command
	@NotifyChange({"operationMessage","logListModel", "selectedLog"})
	public void editLog(@BindingParam("log") Log log) {
		if( selectedLog == null ||  selectedLog.getId() == null ) {
			operationMessage = "editLog(): selectedLog is null or has no ID!";
			return;
		}
		Log ref = myService.getLog(selectedLog.getId());
		int listIndex = logListModel.indexOf(ref);
		ref.setMessage(selectedLog.getMessage());
		ref.setDate( new Date() );
		myService.editLog(ref);
		logListModel.set(listIndex, ref);
		operationMessage = "editLog():  updated selectedLog " + ref.toString();
	}

	@Command
	@NotifyChange({"operationMessage","logListModel", "selectedLog"})
	public void getLog(@BindingParam("id") Integer id ){
		/*
		selectedLog = myService.getLog( id );
		if( selectedLog == null  ) {
			operationMessage = "getLog(): this id " + id.toString() + " was not found!";
			return;
		}
		*/
		List<Log> logList = myService.getById(id);
		if( logList.isEmpty() ){
			operationMessage = "getLog(): this id " + id.toString() + " was not found!";
			return;
		}
		selectedLog = logList.get(0);
		logListModel = new ListModelList<Log>(logList);
		operationMessage = "getLog():  found selectedLog " + selectedLog.toString();
	}

}
