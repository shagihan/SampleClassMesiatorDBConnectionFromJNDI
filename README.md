## Sample class mediator to call database

When calling to the databse with the class mediator from a mediaton extention of the WSO2 products, The better approach is to define datasource in the master-datasource.xml file and access that data source from the class mediator, This sample class mediator is created to cater that requiremnt.

When using Datasource with a JNDI Context, It will manage the connection pool and you could specify all the necessary parameters  [1]  in the datasource configurations in the master-datasource.xml file.

**The behaviour of the attached class mediator.**
 * Defined the data source in the *master-datasource.xml* file.
```xml
	<datasource>
            <name>WSO2_EXTERNAL_DB</name>
            <description>The datasource used for registry and user manager</description>
            <jndiConfig>
                <name>jdbc/WSO2ExternalDB</name>
            </jndiConfig>
            <definition type="RDBMS">
                <configuration>
                    <url>jdbc:mysql://localhost:3306/apimgt?useSSL=false</url>
                    <username>root</username>
                    <password>root</password>
                    <driverClassName>com.mysql.jdbc.Driver</driverClassName>
                    <maxActive>80</maxActive>
                    <maxWait>60000</maxWait>
                    <minIdle>5</minIdle>
                    <testOnBorrow>true</testOnBorrow>
                    <validationQuery>SELECT 1</validationQuery>
                    <validationInterval>30000</validationInterval>
                </configuration>
            </definition>
        </datasource>`
```

 * add mediation extension as follows,
```xml
<sequence xmlns="http://ws.apache.org/ns/synapse" name="dasample">
 <log level="custom">
    <property name="TRACE" value="API Mediation Extension"/>
 </log>
 <class name="org.wso2.sample.dbconn.DBConnectionFromJNDI">
</class&amp;gt;
<log level="custom">
    <property name="Database version from class mediator : " value="get-property('DatabaseResult')"/>
 </log>
</sequence>
```

 * And the logs are getting printed as follows.
```
[2019-04-17 12:01:54,917]  INFO - LogMediator TRACE = API Mediation Extension
[2019-04-17 12:01:54,921]  INFO - DBConnectionFromJNDI Database Version : 5.7.22
[2019-04-17 12:01:54,922]  INFO - LogMediator Database version from class mediator :  = 5.7.22
[2019-04-17 12:06:45,994]  INFO - AndesRecoveryTask Running DB sync task.
```

[1] [https://tomcat.apache.org/tomcat-7.0-doc/jndi-resources-howto.html#JDBC_Data_Sources](https://tomcat.apache.org/tomcat-7.0-doc/jndi-resources-howto.html#JDBC_Data_Sources)
