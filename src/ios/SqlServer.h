#import <Cordova/CDVPlugin.h>
#import "SQLClient.h"

@interface SqlServer : CDVPlugin <SQLClientDelegate> {
    
    
}

@property(nonatomic, retain) NSString *server;
@property(nonatomic, retain) NSString *instance;
@property(nonatomic, retain) NSString *username;
@property(nonatomic, retain) NSString *password;
@property(nonatomic, retain) NSString *database;

-(void)init:(CDVInvokedUrlCommand *)command;
-(void)testConnection:(CDVInvokedUrlCommand *)command;
-(void)executeQuery:(CDVInvokedUrlCommand *)command;
-(void)execute:(CDVInvokedUrlCommand *)command;


@end
