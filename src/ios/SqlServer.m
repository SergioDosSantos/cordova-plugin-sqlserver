#import "SqlServer.h"

#import <Cordova/CDVAvailability.h>
#import "SQLClient.h"

@implementation SqlServer 

@synthesize server, instance, username, password, database;

- (void)pluginInitialize {
}

- (void)init:(CDVInvokedUrlCommand *)command {
    
    self.server = [command.arguments objectAtIndex:0];
    self.instance = [command.arguments objectAtIndex:1];
    self.username = [command.arguments objectAtIndex:2];
    self.password = [command.arguments objectAtIndex:3];
    self.database = [command.arguments objectAtIndex:4];

    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Plugin initialized"];

	if (self.server == nil || [self.server length] == 0) {
	    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Parameter server missing or invalid"];
	}
	if (self.instance == nil || [self.instance length] == 0) {
	    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Parameter instance missing or invalid"];
	}
	if (self.username == nil || [self.username length] == 0) {
	    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Parameter username missing or invalid"];
	}
	if (self.password == nil || [self.password length] == 0) {
	    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Parameter password missing or invalid"];
	}
	if (self.database == nil || [self.database length] == 0) {
	    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Parameter database missing or invalid"];
	}

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(NSString*)getServer {
    NSString *serverName = [NSString stringWithFormat:@"%@\\%@", self.server, self.instance];
    return serverName;
}

- (void)testConnection:(CDVInvokedUrlCommand *)command {

    SQLClient* client = [SQLClient sharedInstance];
    client.delegate = self;
    [client connect:[self getServer] username:self.username password:self.password database:self.database completion:^(BOOL success) {
        if (success) {
            [client execute:@"select 1 as field" completion:^(NSArray* results) {
                [client disconnect];
	                
                  CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Connection succeeded"];
                 [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
	                
            }];
        }
        else {
            CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Error"];
            [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        }
    }];
}


- (void)executeQuery:(CDVInvokedUrlCommand *)command {

 	NSString *query = [command.arguments objectAtIndex:0];

	if (query == nil || [query length] == 0) {
		CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Parameter query missing or invalid"];
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        return;
	}

    SQLClient* client = [SQLClient sharedInstance];
    client.delegate = self;
    [client connect:[self getServer] username:self.username password:self.password database:self.database completion:^(BOOL success) {
        if (success) {
            
            NSString *sql = [NSString stringWithFormat:@"%@", query];
            [client execute:sql completion:^(NSArray* results) {
                
                NSData *jsonData2 = [NSJSONSerialization dataWithJSONObject:results options:NSJSONWritingPrettyPrinted error:nil];
                NSString *jsonString = [[NSString alloc] initWithData:jsonData2 encoding:NSUTF8StringEncoding];
                CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:jsonString];
                    
                [client disconnect];
                [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];

            }];
        
        }
        else {
            CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Error"];
            [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        }
    }];

}

- (void)execute:(CDVInvokedUrlCommand *)command {

 	NSString *query = [command.arguments objectAtIndex:0];

	if (query == nil || [query length] == 0) {
		CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Parameter query missing or invalid"];
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        return;
	}

    SQLClient* client = [SQLClient sharedInstance];
    client.delegate = self;
    [client connect:[self getServer] username:self.username password:self.password database:self.database completion:^(BOOL success) {
        if (success) {
            
            NSString *sql = [NSString stringWithFormat:@"%@", query];
            [client execute:sql completion:^(NSArray* results) {
                
                CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Ok"];

                [client disconnect];
                [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];

            }];
        
        }
        else {
            CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Error"];
            [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        }
    }];

}

- (void)error:(NSString *)error code:(int)code severity:(int)severity {
    NSLog(@"%@", error);
}


@end
