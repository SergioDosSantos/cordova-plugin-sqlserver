
var exec = require('cordova/exec');

var PLUGIN_NAME = 'SqlServer';

var SqlServer = {
		
		init: function(server, instance, username, password, database, success, error) {
			exec(success, error, PLUGIN_NAME, 'init', [server, instance, username, password, database]);
		},  
		testConnection: function(success, error) {
			exec(success, error, PLUGIN_NAME, 'testConnection', null);
		},  
		execute: function(query, success, error) {
			exec(success, error, PLUGIN_NAME, 'execute', [query]);
		},
  
};

module.exports = SqlServer;
