package com.y3k;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import android.util.Log;

import java.util.Date;

public class SqlServer extends CordovaPlugin {

    private static final String TAG = "SqlServer";

    private String server;
    private String instance;
    private String username;
    private String password;
    private String database;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("init")) {
            init(args, callbackContext);
            return true;

        } else if (action.equals("testConnection")) {
            testConnection(args, callbackContext);
            return true;

        } else if (action.equals("executeQuery")) {
        	executeQuery(args, callbackContext);
            return true;

        } else if (action.equals("execute")) {
        	execute(args, callbackContext);
            return true;

        }
        return false;
    }

    private void init(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        server = args.getString(0);
        instance = args.getString(1);
        username = args.getString(2);
        password = args.getString(3);
        database = args.getString(4);

        PluginResult result = new PluginResult(PluginResult.Status.OK, "Plugin initialized");

        if (server == null || "".equals(server)) {
            result = new PluginResult(PluginResult.Status.ERROR, "Parameter server missing or invalid");
        }
        if (instance == null || "".equals(instance)) {
            result = new PluginResult(PluginResult.Status.ERROR, "Parameter instance missing or invalid");
        }
        if (username == null || "".equals(username)) {
            result = new PluginResult(PluginResult.Status.ERROR, "Parameter username missing or invalid");
        }
        if (password == null || "".equals(password)) {
            result = new PluginResult(PluginResult.Status.ERROR, "Parameter password missing or invalid");
        }
        if (database == null || "".equals(database)) {
            result = new PluginResult(PluginResult.Status.ERROR, "Parameter database missing or invalid");
        }

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            result = new PluginResult(PluginResult.Status.ERROR, ex.getMessage());
        }

        callbackContext.sendPluginResult(result);
    }

    private void testConnection(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        PluginResult result;

        try {
            String jdbcConnectionString = "jdbc:jtds:sqlserver://" + server + "/" + database + ";instance=" + instance;
            Connection conn = DriverManager.getConnection(jdbcConnectionString, username, password);
            if (conn != null) {
                if (!conn.isClosed()) {
                    conn.close();
                }
            }
            result = new PluginResult(PluginResult.Status.OK, "Connection succeeded");
        } catch (Exception ex) {
            result = new PluginResult(PluginResult.Status.ERROR, "Connection problem : " + ex.getMessage());
        }
        callbackContext.sendPluginResult(result);
    }

    private void executeQuery(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        ResultSet rs = null;
        Connection conn = null;
        Statement stmt = null;

        try {
            String query = args.getString(0);
            if (query == null || "".equals(query)) {
                PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Parameter query missing or invalid");
                callbackContext.sendPluginResult(result);
                return;
            }

            String jdbcConnectionString = "jdbc:jtds:sqlserver://" + server + "/" + database + ";instance=" + instance;
            conn = DriverManager.getConnection(jdbcConnectionString, username, password);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            JSONArray json = new JSONArray();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                JSONObject obj = new JSONObject();
                for (int i = 1; i <= numColumns; i++) {
                    String column_name = rsmd.getColumnName(i);
                    obj.put(column_name, rs.getObject(column_name));
                }
                json.put(obj);
            }
            PluginResult result = new PluginResult(PluginResult.Status.OK, json);
            callbackContext.sendPluginResult(result);

        } catch (Exception ex) {
            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Error : " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    if (!conn.isClosed()) {
                        conn.close();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void execute(JSONArray args, final CallbackContext callbackContext) throws JSONException {
        ResultSet rs = null;
        Connection conn = null;
        Statement stmt = null;

        try {
            String query = args.getString(0);
            if (query == null || "".equals(query)) {
                PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Parameter query missing or invalid");
                callbackContext.sendPluginResult(result);
                return;
            }

            String jdbcConnectionString = "jdbc:jtds:sqlserver://" + server + "/" + database + ";instance=" + instance;
            conn = DriverManager.getConnection(jdbcConnectionString, username, password);

            stmt = conn.createStatement();
            stmt.execute(query);

            PluginResult result = new PluginResult(PluginResult.Status.OK, "Ok");
            callbackContext.sendPluginResult(result);

        } catch (Exception ex) {
            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Error : " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    if (!conn.isClosed()) {
                        conn.close();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}