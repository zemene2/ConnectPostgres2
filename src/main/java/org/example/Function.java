package org.example;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Function {
    public String host;
    public String port;
    public String username;
    public String password;
    public String database;
    private Connection connection;
    private Connection connect(){
        this.connection = null;
        try {
           // Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            System.out.println("Good Connection");
            return this.connection;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(("Error"));
            throw new RuntimeException(e);
        }
    }
    public List<USER> Select(){
        Connection connection=connect();
        String query="SELECT * FROM user2";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(query);
            List<USER> listOfObject=new ArrayList<USER>();
            while (resultSet.next()){
                USER useri=new USER();
                useri.setId(resultSet.getInt("id"));
                useri.setName(resultSet.getString("name"));
                listOfObject.add(useri);
            }
            connection.close();
            return listOfObject;
        } catch (SQLException e) {
            System.out.println("ERROR1");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void updateObject(USER user,String NewName){
        Connection connection=connect();
        String query="UPDATE public.\"user2\"\n" +
                "\tSET \"name\"=?\n" +
                "\tWHERE name=?;";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(2,user.getName());
            user.setName(NewName);
            preparedStatement.setString(1,user.getName());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.out.println("ERROR1");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public int insertObject(List<USER> list, String data){

        PostgreSQLConnection Con = new PostgreSQLConnection("localhost","5432","postgres","123","postgres");

        //add object to list
        USER obj = new USER();
        obj.setName(data);
        obj.setId(0);
        list.add(obj);
        //add to table
        Con.insert("user2", "name",data);

        try{
        Con.connect();

        String sqlQuery = "SELECT MAX(\"id\") as max_ID \tFROM public.user2";
        Statement stmt = null;
        stmt = Con.connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery);

        rs.next();

        String formatedValue = String.format("%s", new String(rs.getBytes(1), StandardCharsets.UTF_8));

        int Save =  Integer.parseInt(formatedValue);

            list.get(list.size()-1).setId(Save);

            Con.disconnect();
            return Save;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }

    }
}
