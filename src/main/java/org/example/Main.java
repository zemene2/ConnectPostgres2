package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

//        PostgreSQLConnection psqlc = new PostgreSQLConnection(host, port, user, pass, database);
        Function try1=new Function();
        try1.host= "localhost";
        try1.port = "5432";
        try1.username= "postgres";
        try1.password= "123";
        try1.database= "postgres";
        //Bring the Data
        List<USER> A = new ArrayList<USER>();
        USER zamana=new USER();
        zamana.setName("zemene");
        A=try1.Select();

        //Objects Update
//        try1.updateObject(zamana,"Nehoraii");
       for (int i = 0; i < A.size(); i++) {
           // System.out.print("ID:  "+A.get(i).getId());
           // System.out.println("  Name:   "+ A.get(i).getName());
        }
       //A new obj to the list

//        System.out.print("ID:  "+A.get(A.size()-1).getId());
//        System.out.println("  Name:   "+ A.get(A.size()-1).getName());

        try1.insertObject(A,"Moshe");
        for (int i = 0; i < A.size(); i++) {
             System.out.print("ID:  "+A.get(i).getId());
             System.out.println("  Name:   "+ A.get(i).getName());
        }

        PostgreSQLConnection check = new PostgreSQLConnection("localhost","5432","postgres","123","postgres");
        check.select("user2", "*", "no condition");



    }
}