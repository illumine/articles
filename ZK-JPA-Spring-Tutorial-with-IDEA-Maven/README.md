# ZK JPA Spring Tutorial with IDEA Maven

## Abstract
In this article we will implement a complete example of [CRUD (Create, Retrieve, Update, Delete](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) 
operations on a database table.


To do so we will create a WEB based form that allows the user to perform the CRUD operations. The form will be based on [ZK framework](https://www.zkoss.org/).


The design pattern we will follow to implement our application is the  [MVVM Pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)


In the service layer, the CRUD operations will be handled by [JPA](https://www.javaworld.com/article/3379043/what-is-jpa-introduction-to-the-java-persistence-api.html). 


The overal development will take place in [Intelij IDEA](https://www.jetbrains.com/idea/) and the 
build and dependency resolver tool we use will be [Apache Maven](https://maven.apache.org/). 


## The Example Case
We use something very primitive: a simple Log. What is a Log? A message with a date! So, our simple application will offer the users
a simple web form where the user can Create, Retrieve, Update and Delete a simple Log entry. This is a simple [UML Use Case Diagram](https://en.wikipedia.org/wiki/Use_case_diagram)
that describes our application:

![UML Use Case Diagram](img/uml_log_use_case.png "UML Use Case Diagram")


The UML Use Case Diagram, illustrates the _Functional Specifications_ of our project.


## Designing the Web Form
Whenever you design a WEB based application, regardless of its complexity, you always start with a pencil and a piece of paper!
Doing a non functional prototyping directly on the IDE using the framework you are  about to use may lead you to an unclear design 
that does not lead to a correct, easy to use and handy web form. For that reason back to the basics: pencil, rubber and paper:


![Paperwork Diagram](img/paperwork.jpg "Old school paperwork for a simple prototype")

Lets exmplain how we think of the paperwork design:
Basic sections:\
* Search: is the section where the Retrieve operations take place. The user searches a Log entry with some _Selection Criteria._
* Udate/New: is the section where the user either Edits/Updates an existing log, or Creates a new or Deletes the existing. Actually 
this space on the form deals with the _Current Log_.
* List of Logs: is the form space where the Logs are displayed. Initially some of them are displayed, when user defines some Criteria 
and press Search/Find button, then this section displays the results. When user clicks on a Log from the list, then this Log becomes
the _current Log_ and its details populate the section Update/New.
Help sections:\
* Operation Results: is the section where the user gets some _feedback or error messages_ are displayed.
* Pagination details: is the section that controls the List of Logs allows the user to move forward or backwards etc...


In the next sections, we will implement the paperwork design to a Web form based on [ZK framework](https://www.zkoss.org/).



## Creating the Project in Intellij IDEA
The overal development will take place in [Intelij IDEA](https://www.jetbrains.com/idea/). You can download it from https://www.jetbrains.com/idea/download/

The reason that I implemented the article on IDEA is that this tool it is the leading platform at least in the time this article was written.
In my [previous ZK training](https://github.com/illumine/articles/tree/master/ZK-training), 
I use to work with Eclipse, but that was 5 years ago. I think IDEA needs some attention, nowdays it is the best of breed.


Read the official instructions from ZKoss in order to setup your in Intelij IDEA:
https://www.zkoss.org/wiki/ZK_Installation_Guide/Quick_Start/Create_and_Run_Your_First_ZK_Application_with_IntelliJ_and_ZKIdea


In my case, I came up to this error: _"Java: release version 5 not supported"_ but to resolve it 
read the solution nr. 3 from [this article](https://dev.to/techgirl1908/intellij-error-java-release-version-5-not-supported-376)



## The JPA Model of our Log Project
JPA is a Java Framework that persists Java Objects (or Beans as they sometimes refered) on a database or in general, to 
a local or a remote disk. The JPA implementation we are going to use is [Hibernate](https://hibernate.org/). A s


So a simple Log entry as a plain Java Class can be like:


<code>
    public class Log{
       Integer id;
       String message;
       java.util.Date date;
 
       public Log(){id=0; message=""; date= new java.util.Date(); }
     }
</code> 



In order to transform it to a JPA Entiry Bean, the following Java Annotations are applied:



<code>
  @Entity
  public class Log implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@Column(nullable = false)
	String message;

	@Temporal(TemporalType.TIMESTAMP)
	Date date;

	public Log() {
		this.date = new Date();
	}

	public Log(String message) {
		this();
		this.message = message;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
</code>


According to the DAO/Adapter pattern, the following components are used:

_Log_: the JPA Entiry Bean that is our actual data object. Consider each _Log_ object as a table row.

_LogDao_: the Data Access Object, a class that implements methods for CRUD operations on the Log. Consider the _LogDao_ class as the _Log_ **table adapter**.

_MyService_: interface that denotes the DB Transaction methods.  Consider the _MyService_ interface as the  **DB adapter type library**.

_MyServiceImpl_: the class implementing MyService interface. Consider the _MyServiceImpl_ class as the  **DB adapter**.
_MyServiceImpl_ actually, 
implements all the operations that are going/returning to the Database, it is the Database logic itself.

_MyServiceImpl_ is a collection of references of DAOs and Entities that are required in order to implement the transaction logic.

Bottom line of the design pattern: whatever needs to deal with the Database, it requires a reference to  _MyServiceImpl_ object

Follows a UML Class Diagram of our design:

![UML Class Diagram of our design](img/uml_log_use_case.png "UML Class Diagram of our design")

The JPA settings are defined in the file persistence.xml where the HSQL DB is used to store the JPA entities.


<code>
  <?xml version="1.0" encoding="UTF-8"?>
  <persistence xmlns="http://java.sun.com/xml/ns/persistence" version="2.0">

	<persistence-unit name="myapp" transaction-type="RESOURCE_LOCAL">
		<provider>org.hibernate.ejb.HibernatePersistence</provider>
		<properties>
			<property name="hibernate.dialect" value="org.hibernate.dialect.HSQLDialect" />
			<property name="hibernate.connection.driver_class" value="org.hsqldb.jdbcDriver" />
			<property name="hibernate.connection.username" value="sa" />
			<property name="hibernate.connection.password" value="" />
			<property name="hibernate.show_sql" value="true" />
			<!-- data store in data/db under project folder -->
			<property name="hibernate.connection.url" value="jdbc:hsqldb:file:data/db" />
			<!-- db is not persistent to disk
			<property name="hibernate.connection.url" value="jdbc:hsqldb:mem:data/store" /> -->
			<property name="hibernate.hbm2ddl.auto" value="update" />
			<!-- drop table every time
			<property name="hibernate.hbm2ddl.auto" value="create" /> -->
		</properties>
	</persistence-unit>
   </persistence>
</code>


## The ZK Visual Design

Implementing the Graphical User Interface - the GUI - as most of us know it, include the compilation of the 
ZK ZUL file that provides the presentation layer of our application. Recalling from the previous section _Designing the Web Form_
the implementation has as follows:



![Implementing the Graphical User Interface](img/zk-the-web-form.png "Implementing the Graphical User Interface")

This is the index3.zul file.

As you can probably see, all the sections from the paperwork are placed in the Web Form exactly like the paper work.

Some details aroun it:

We used several nested `hbox` and `vbox` layout managers to arrange the web form laout. See here for [ZK hbox](https://www.zkoss.org/wiki/ZK_Component_Reference/Layouts/Hbox)
and [ZK vbox](https://www.zkoss.org/wiki/ZK_Component_Reference/Layouts/Vbox)
 
 
The ZK visual input methods are `textbox, intbox, and datebox`


The Log entities are presented using a [ZK listBox](https://www.zkoss.org/wiki/ZK_Component_Reference/Data/Listbox).



## The MVVM Pattern Implementation of our Project
The [MVVM Pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) in some small words has the 
following aspects:

1. Isolate the XML presentation design from the actual Controller. The presentation design - the view - is the `index.zul` file, 
The visual controller class is the `MyViewModel3.java` file. The visual controller class controlls all the 
visual controls of the our Web Form. 


2. Wire the Data Structures of the Visual Components of the Controller Class to the XML presentation design. The data structures 
handled by the controler class in the MVVM Pattern terminology is often called the **State**


3. Wire the Methods of the Controller Class to the XML presentation design. The methods offered 
 by the controler class in the MVVM Pattern terminology is often called the **Behavior**
 
 
Lets examine how exactly the Wiring of MVVM State and Behavior from the **Visual Model index.zul** file to the 
Java class implementing the **Visual Model Controller or the View Model** of the model

In the `index3.zul` file we can see the following sections:


<code>
    <zk>
           <window viewModel="@id('vm')@init('org.example.MyViewModel3')"
                 width="800px" border="normal" title="ZK JPA CRUD Operations on ListBox">
            ...
</code>


As you can probably see, the full class name `org.example.MyViewModel3` of the **Visual Model** is specified.
Whith this declaration, whenever the `index3.zul` is initiated, the method `org.example.MyViewModel3.init()` is called.


In the ZUL file, we can refer to the Java class `org.example.MyViewModel3` members using the variable `vm` specified in the
`id` section of the `viewModel` XML attribute.


Lets see the java viewModel class: `org.example.MyViewModel3` 



<code>
     @VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
        public class MyViewModel3 {

	        public class Criteria{
	    	.....
	        }

	 @WireVariable
	 private MyService myService;
	 private Criteria logCriteria;
	 private ListModelList<Log> logListModel;
	 private Log selectedLog;
     private String operationMessage;
</code>




The annotation `@WireVariable`  states that all those attribute members of the `org.example.MyViewModel3` view model class
can be subsequently refered in the ZK .zul file with the use of the the variable `vm` specified in the
`id` section of the `viewModel` XML attribute.\

Creating a ViewModel is like creating a POJO, and it exposes its properties like JavaBean through setter and getter methods.
A ViewModel must always provides Getter and Setter methods to allow ZK engine access its State members! missing implementing the
getter/setter methods, will result in a runtime Exception when the artifact is deployed on the Application Server!


     
Lets take a look to the section of the ZUL file dedicated to the Search Criteria of a Log entity:



<code>
      <vbox hflex="1">
                Search Criteria
                <separator orient="horizontal" bar="true"/>
                <hbox hflex="1">
                    Substring:
                    <textbox hflex="1" value="@load(vm.logCriteria.text),@save(vm.logCriteria.text)" placeholder="search for log string?"/>
                </hbox>
                <hbox hflex="1">
                    ID:
                    <intbox hflex="1" value="@load(vm.logCriteria.id),@save(vm.logCriteria.id)" constraint="no negative,no zero" />
                </hbox>
                <hbox hflex="1">
                    <button label="Search" onClick="@command('getLogByCriteria', criteria = vm.logCriteria )"/>
                    <button label="Reload/clear" onClick="@command('reloadAll')"/>
                </hbox>
            </vbox>

</code>





As we can see here, both  `textbox` and `intbox` load and save their content (their state) in the attribute members of view model class:
`@load(vm.logCriteria.text),@save(vm.logCriteria.text)` loads and saves to `org.example.MyViewModel3.logCriteria.text` and
`@load(vm.logCriteria.id),@save(vm.logCriteria.id)` loads and saves to `org.example.MyViewModel3.logCriteria.id`


Now on the seecond part: How  the **behavior** is wired from the zul file to the Java class? 
Notice the declaration of the handling of the `onClick` event: this wires the event to the method  
`org.example.MyViewModel3.getLogByCriteria` and binds the method's argument `criteria = vm.logCriteria` in other words
the `criteria` variable will take the actual state of  `vm.logCriteria`.

See the implementation of the method: `org.example.MyViewModel3.getLogByCriteria`




<code>
	  @Command
	  @NotifyChange({"operationMessage","logListModel", "selectedLog"})
	  public void  getLogByCriteria( @BindingParam("criteria") Criteria criteria){
		  if( criteria.getId() == 0 && Strings.isBlank( criteria.getText())  ) {
			  operationMessage = "getLogByCriteria(): text is blank or id is 0!";
			  return;
		  }

		  List<Log> logList = myService.getByCriteria( criteria.id, criteria.text);
		  if( logList.isEmpty() ){
			  operationMessage = "getLogByCriteria(): nothing found for " + criteria.toString();
			  return;
		  }
		  selectedLog = logList.get(0);
		  logListModel = new ListModelList<Log>(logList);
		  operationMessage = "getLogByCriteria():  found selectedLog " + selectedLog.toString();
	   }
</code>


The method is annotated with `@Command` meaning that this method is handling some event that is wired from the ZUL view.
When this method is called and finished, the state variables `{"operationMessage","logListModel", "selectedLog"}`
of the ZUL view are notified thus are reloaded from the ViewModel class!

![DataBinding of the example](img/mvvm-design-example.png "DataBinding of the example")

More on view model:\
ZK implementation of the MVVM pattern:\
http://books.zkoss.org/zk-mvvm-book/8.0/viewmodel/index.html

The MVVM pattern in ZK: a complete example\
https://www.zkoss.org/wiki/ZK_Getting_Started/Get_ZK_Up_and_Running_with_MVVM


## The deployment descriptors
The Java application server we want to deploy our example is [Apache Tomcat](http://tomcat.apache.org/)
To do so the following descriptors are used:

![War contents](img/war-contents.png "War contents")

## Tomcat Deployment on IDEA

![Tomcat Deployment on IDEA](img/tomcat-setup.png "Tomcat Deployment on IDEA")



## Resources and References

### ZK With Maven, JPA, Spring using IDEA
Project setup in Intelij IDEA:\
https://www.zkoss.org/wiki/ZK_Installation_Guide/Quick_Start/Create_and_Run_Your_First_ZK_Application_with_IntelliJ_and_ZKIdea



IntelliJ - Error:java: release version 5 not supported \
Select the solution #3\
https://dev.to/techgirl1908/intellij-error-java-release-version-5-not-supported-376



### MVVM Patern
ZK implementation of the MVVM pattern:\
http://books.zkoss.org/zk-mvvm-book/8.0/viewmodel/index.html

The MVVM pattern in ZK: a complete example\
https://www.zkoss.org/wiki/ZK_Getting_Started/Get_ZK_Up_and_Running_with_MVVM

