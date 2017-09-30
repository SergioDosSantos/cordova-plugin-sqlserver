# cordova-plugin-sqlserver
Cordova Plugin to connect to SQL Server without services

Sometimes we need to access to databse directly without any server as middleware. 
The purpose of this plugin is to avoid using services to access data directly.

It can be used on Cordova, PhoneGap and Ionic.

This version is compatible with iOS and Android platforms.

# Instalation

  You can download the plugin and add it to your project as a local plugin
  
```
cordova plugin add /path/to/folder/sqlserver-plugin
```

  It is also possible to install via repo url directly
  
```
cordova plugin add https://github.com/SergioDosSantos/cordova-plugin-sqlserver.git
```

# How to use it

  After add the plugin just intialize it with database parameters server, instance, username, password, database name. For example:

```javascript
SqlServer.init("192.168.0.120", "SQLEXPRESS", "sa", "01234567", "dinademo", function(event) {
  alert(JSON.stringify(event));
}, function(error) {
  alert(JSON.stringify(error));
});
```

On success it will return "Plugin initialized"

After that you can test your database connection with

```javascript
SqlServer.testConnection(function(event) {
  alert(JSON.stringify(event));
}, function(error) {
  alert("Error : " + JSON.stringify(error));
});				
```
On succes in this case it will return "Connection succeeded"

# Query execution

At this moment there is two general purpose methods:

* executeQuery : Just execute the query on server side and return a JSON formatted array
* execute: Execute an INSERT, UPDATE, DELETE and return "Ok" when succed or the database error on fail

# executeQuery method 

Once the plugin is initialized you can execute a query on SQL Server by doing 

```javascript
SqlServer.executeQuery("select * from test_table where test_code=1", function(event) {
  alert(JSON.stringify(event));
}, function(error) {
  alert("Error : " + JSON.stringify(error));
});				
```
 
You can call a Store Procedure also

```javascript
SqlServer.executeQuery("exec i_store_test '500048', '1', 'MMMM'", function(event) {
  alert(JSON.stringify(event));
}, function(error) {
  alert("Error : " + JSON.stringify(error));
});
```
 
# execute method

In order to execute an INSERT, DELETE or UPDATE just use somethig like

```javascript
SqlServer.execute("update table_test set field_test=22 where key_test=500048", function(event) {
  alert("Update complete : " + JSON.stringify(event));
}, function(error) {
  alert("Error : " + JSON.stringify(error));
});
```

# Important
  
If you need subsequent calls to the database in the ios version you will not be able to do the following

```javascript
SqlServer.executeQuery("select * from test_table where test_code=1", function(event) {
  alert(JSON.stringify(event));
}, function(error) {
  alert("Error : " + JSON.stringify(error));
});				

SqlServer.executeQuery("exec i_store_test '500048', '1', 'MMMM'", function(event) {
  alert(JSON.stringify(event));
}, function(error) {
  alert("Error : " + JSON.stringify(error));
});
```

You must do this in the following way to avoid EXCE_BAD_ACCESS error on ios platforms

```javascript
SqlServer.executeQuery("select * from test_table where test_code=1", function(event) {
    
  // On first call completed
  SqlServer.executeQuery("exec i_store_test '500048', '1', 'MMMM'", function(event) {
    alert(JSON.stringify(event));
  }, function(error) {
    alert("Error : " + JSON.stringify(error));
  });
  
}, function(error) {
  alert("Error : " + JSON.stringify(error));
});				

```

# How to Contribute

Contributors are welcome! And we need your contributions to keep the project moving forward. 
You can report bugs to sdossantos@y3k-it.com or contribute with code.

# Credits

* SQLCliente for Xcode https://github.com/martinrybak/SQLClient

# For more information about us visit

* QREventos http://www.qreventos.com
* Y3K-it http://www.y3k-it.com
