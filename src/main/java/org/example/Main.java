package org.example;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        String port = "5432";
        String user = "postgres";
        String pass = "123";
        String database = "postgres";
        PostgreSQLConnection psqlc = new PostgreSQLConnection(host, port, user, pass, database);
        //insert value to user
        String insert = psqlc.getResults("insert into public.\"User\" values(8)");
        System.out.println(insert);

        //select all from user
        String selectAll = psqlc.getResults("SELECT * FROM public.\"User\"\n");
        System.out.println(selectAll);


    }
}