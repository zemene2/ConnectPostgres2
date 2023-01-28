package org.example;

import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 *
 */

public class PostgreSQLConnection {

    public String host;
    public String port;
    public String username;
    public String password;
    public String database;
    public Connection connection;

    public PostgreSQLConnection(String host, String port, String username, String password, String database) {
        super();
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    private String getResults(PreparedStatement sqlQuery) {
        try {
            String result = "";
//            this.connect();
            String txt = sqlQuery.toString();
            Statement stmt = null;
            stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(txt);
            ResultSetMetaData rsMeta = rs.getMetaData();
            //count -> number of columns in the table
            int count = rsMeta.getColumnCount();
            int i, j = 1;
            result += "\n| ";
            //Display Coloumns Name
            while (j <= count) {

//                String format = "%1$-" + rsMeta.getColumnDisplaySize(j) + "s";
                String formatedValue = String.format("%s" ,rsMeta.getColumnLabel(j));
                //Add current column name
                result += formatedValue + " | ";
                j++;
            }
            // "new String" is an empty list in the same length as "result"
            //This line print "---------------------------------------------"
            result += "\n" + new String(new char[result.length()]).replace("\0", "-");
            while (rs.next()) {
                i = 1;
                result += "\n| ";
                while (i <= count) {
//                    String format = "%1$-" + rsMeta.getColumnDisplaySize(i) + "s";
                    //Adding data from the columns
                    String formatedValue = String.format("%s", new String(rs.getBytes(i), StandardCharsets.UTF_8));

                    String theLabel = String.format("%s" ,rsMeta.getColumnLabel(i));
                    int space_len = theLabel.length()-formatedValue.length();
                    if ( space_len < 0 ){
                        space_len *= -1;
                    }
                    String spaces = new String(new char[space_len]).replace("\0", " ");

                    result += formatedValue + spaces + " | ";
                    i++;
                }
            }
            this.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return "";
        }
    }

    public void select(String table, String columns, String condition) {

        try {
            connect();
            PreparedStatement statement;
            //Empty query
            String query;
            if(condition != "no condition") {
//            "SELECT "ID", first_name, last_name FROM public."USER" WHERE "ID" = "ID"";
                //Preparing statement for injection
                query = "SELECT " + columns + " FROM public.\"" + table + "\" WHERE \"id\" = ? ORDER BY \"ID\"  ";
                //Replacing (?) in values
                statement = this.connection.prepareStatement(query);
                statement.setString(1, condition);
            }else{
                query = "SELECT " + columns + " FROM public.\"" + table + "\"  ORDER BY \"id\"  ";
                //SELECT * FROM public."USER2"
                statement = this.connection.prepareStatement(query);
            }


            String res = getResults(statement);
            System.out.println(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void insert(String table, String columns, String data){

        try {
            connect();
            PreparedStatement statement;
            //Empty query
            String query = "INSERT INTO public.\""+table+"\"("+columns+") VALUES ( ? )";
            //Preparing statement for injection
            statement = this.connection.prepareStatement(query);
            //Replacing (?, ?) in values
            statement.setString(1, data);

            String res = getResults(statement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public void update(String table, String column, String data, String column_condition, String data_condition){

        try {
            connect();
            String query = "UPDATE \""+table+"\" SET "+ column +" = ? WHERE  " + column_condition + " = ? ";
            PreparedStatement statement;
            statement = this.connection.prepareStatement(query);
            statement.setString(1,data);
            statement.setString(2,data_condition);
            String res = getResults(statement);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //UPDATE public."USER" SET "ID"=?, first_name=?, last_name=? WHERE <condition>;

    }

    public void delete(String table, String condition_data){

        try {
            connect();
            PreparedStatement statement;
            String query = "DELETE FROM TABLE \"" +table + "\" WHERE \"ID\" = \'?\' ";
            statement = this.connection.prepareStatement(query);
            statement.setString(1, condition_data);
            String res = getResults(statement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public void connect() throws Exception {
        Class.forName("org.postgresql.Driver");
        this.connection = null;
        this.connection = DriverManager.getConnection(
                "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
    }

    public void disconnect() throws Exception {
        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
        }
    }
}
