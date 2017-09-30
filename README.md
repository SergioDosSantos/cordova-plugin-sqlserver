# cordova-plugin-sqlserver
Cordova Plugin to connect to SQL Server without services

Sometimes we need to access to databse directly without any server as middleware. 
The purpose of this plugin is to avoid using services to access data directly.

It can be used on Cordova, PhoneGap and Ionic.

This version is compatible with the ios platform, the Android version is in progress.

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

  ```
  SqlServer.init("192.168.0.120", "SQLEXPRESS", "sa", "01234567", "dinademo", function(event) {
	  alert(JSON.stringify(event));
  }, function(error) {
    alert(JSON.stringify(error));
  });
  ```

On success it will return "Plugin initialized"

After that you can test your database connection with

  ```
  SqlServer.testConnection(function(event) {
    alert(JSON.stringify(event));
  }, function(error) {
    alert("Error : " + JSON.stringify(error));
  });				
  ```
On succes in this case it will return "Connection succeeded"

# Query execution

At this moment there is a general purpose method, just execute the query on server side and return a JSON array

  ```
  SqlServer.execute("select * from test_table where test_code=1", function(event) {
    alert(JSON.stringify(event));
  }, function(error) {
    alert("Error : " + JSON.stringify(error));
  });				
  ```
  
# How to Contribute

Contributors are welcome! And we need your contributions to keep the project moving forward. 
You can report bugs to sdossantos@y3k-it.com or contribute with code.