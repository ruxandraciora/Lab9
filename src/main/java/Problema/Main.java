package Problema;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void afisare_persoane(Statement statement) throws SQLException {
        String sql = "select * from persoane";
        try(ResultSet rs1 = statement.executeQuery(sql))
        {
            while(rs1.next())
            {
                System.out.println("Id: "+rs1.getInt("id")+"\nNume: "+rs1.getString("nume")+"\nVarsta: "+rs1.getInt("varsta")+"\n");
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void afisare_excursii(Statement statement) throws SQLException {
        String sql2 = "select * from excursii";
        try(ResultSet rs2 = statement.executeQuery(sql2))
        {
            while(rs2.next())
            {
                System.out.println("Id persoana: "+rs2.getInt("id_persoana")+"\nId excursie: "+rs2.getInt("id_excursie")+"\nDestinatia: "+rs2.getString("destinatia")+"\nAnul: "+rs2.getInt("anul")+"\n");
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void adaugare_persoana(Connection connection)
    {
        Scanner read = new Scanner(System.in);
        System.out.println("Introduceti numele: ");
        String nume = read.nextLine();

        System.out.println("Introduceti varsta: ");
        int varsta = read.nextInt();

        String line = "INSERT INTO persoane(nume,varsta) VALUES(?,?)";
        try(PreparedStatement ps = connection.prepareStatement(line)) {
            ps.setString(1, nume);
            ps.setInt(2, varsta);

            ps.executeUpdate();

        } catch(SQLException e)
        {
            System.out.println(line);
            e.printStackTrace();
        }
    }

    public static void adaugare_excursie(Connection connection)
    {
        Scanner read = new Scanner(System.in);
        System.out.println("Introduceti id-ul persoanei: ");
        int id = read.nextInt();
        System.out.println("Introduceti destinatia: ");
        String destinatie = read.next();
        System.out.println("Introduceti anul: ");
        int an = read.nextInt();

        String sql = "INSERT INTO excursii(id_persoana,destinatia,anul) VALUES (?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1,id);
            ps.setString(2,destinatie);
            ps.setInt(3,an);
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void afisare_persoane_excursii(Statement statement)
    {
        String sql = "SELECT A.*, B.* FROM persoane A, excursii B WHERE A.id = B.id_persoana";
        try(ResultSet rs = statement.executeQuery(sql))
        {
            while(rs.next())
            {
                System.out.println("Id: "+rs.getInt("id")+"\nNume: "+rs.getString("nume")+"\nVarsta: "+rs.getInt("varsta")+"\n");
                System.out.println("Id persoana: "+rs.getInt("id_persoana")+"\nId excursie: "+rs.getInt("id_excursie")+"\nDestinatia: "+rs.getString("destinatia")+"\nAnul: "+rs.getInt("anul")+"\n");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void cautare_excursie(Connection connection)
    {
        Scanner read = new Scanner(System.in);
        System.out.println("Introduceti numele persoanei: ");
        String nume = read.next();
        int ok=1;

        String sql = "SELECT A.* FROM excursii A, persoane B WHERE A.id_persoana=B.id AND B.nume = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql))
        {
            ok=1;
            ps.setString(1,nume);
            try(ResultSet rs1 = ps.executeQuery())
            {
                while(rs1.next()) {
                    if(ok==1) {
                        System.out.println(nume + " a participat in urmatoarele excursii: ");
                        ok = 0;
                    }
                    System.out.println(rs1.getString("destinatia"));
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        if(ok==1)
            System.out.println(nume + " nu a fost in nicio excursie.");
    }

    public static void afisare_persoane_destinatie(Connection connection)
    {
        Scanner read = new Scanner(System.in);
        System.out.println("Introduceti destinatia: ");
        String destinatie = read.next();
        int ok=1;

        String sql = "SELECT A.nume FROM persoane A, excursii B WHERE A.id = B.id_persoana AND B.destinatia = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1,destinatie);
            try(ResultSet rs = ps.executeQuery())
            {
                while(rs.next())
                {
                    if(ok==1)
                    {
                        System.out.println("Persoanele care au fost in "+ destinatie +": ");
                        ok=0;
                    }
                    System.out.println(rs.getString("nume"));
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        if(ok==1)
        {
            System.out.println("Nicio persoana nu a fost in " + destinatie + ".");
        }
    }

    public static void an_excursie(Connection connection)
    {
        Scanner read = new Scanner(System.in);
        System.out.println("Introduceti anul: ");
        int an = read.nextInt();
        int ok=1;

        String sql = "SELECT A.nume FROM persoane A, excursii B WHERE A.id = B.id_persoana AND B.anul = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1,an);

            try(ResultSet rs = ps.executeQuery())
            {
                while(rs.next())
                {
                    if(ok==1)
                    {
                        System.out.println("Persoanele care au facut excursii in anul " + an + ": ");
                        ok=0;
                    }
                    System.out.println(rs.getString("nume"));
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        if(ok==1)
        {
            System.out.println("Nicio persoana nu a facut excursii in anul " + an + ".");
        }
    }

    public static void stergere_excursie(Connection connection)
    {
        Scanner read = new Scanner(System.in);
        System.out.println("Introduceti id-ul excursiei pe care doriti sa o stergeti: ");
        int id = read.nextInt();
        int ok = 0;

        String sql = "DELETE FROM excursii WHERE id_excursie = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1,id);
            ok = ps.executeUpdate();
            if(ok != 0)
                System.out.println("Excursia a fost stearsa!");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        if(ok==0)
            System.out.println("Excursia cu id-ul " + id + " nu a fost gasita.");

    }

    public static void stergere_persoana(Connection connection)
    {
        Scanner read = new Scanner(System.in);
        System.out.println("Introduceti id-ul persoanei pe care doriti sa o stergeti: ");
        int id = read.nextInt();
        int ok = 0;

        String sql = "DELETE FROM persoane WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1,id);
            ok = ps.executeUpdate();
            if(ok != 0)
                System.out.println("Persoana a fost stearsa!");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        if(ok == 0)
            System.out.println("Persoana cu id-ul " + id + " nu a fost gasita.");
    }
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/lab9";


        Connection connection = DriverManager.getConnection(url, "root", "rootpassword");
        Statement statement = connection.createStatement();

        Scanner read = new Scanner(System.in);
        int optiune = 0;
        do {
            System.out.println("0. Exit.");
            System.out.println("1. Adaugare persoana.");
            System.out.println("2. Adaugare excursie.");
            System.out.println("3. Afisarea persoanelor si excursiile in care au fost.");
            System.out.println("4. Afisarea excursiilor in care a fost o anumita persoana.");
            System.out.println("5. Afisarea persoanelor care au vizitat o anumita destinatie.");
            System.out.println("6. Afisarea persoanelor care au facut excursii intr-un anumit an.");
            System.out.println("7. Stergerea unei excursii.");
            System.out.println("8. Stergerea unei persoane.");
            System.out.println("Introduceti optiunea: ");
            optiune = read.nextInt();
            switch(optiune)
            {
                case 0: break;
                case 1: adaugare_persoana(connection);
                afisare_persoane(statement);
                break;
                case 2: adaugare_excursie(connection);
                afisare_excursii(statement);
                break;
                case 3: afisare_persoane_excursii(statement);
                break;
                case 4: cautare_excursie(connection);
                break;
                case 5: afisare_persoane_destinatie(connection);
                break;
                case 6: an_excursie(connection);
                break;
                case 7: stergere_excursie(connection);
                    afisare_excursii(statement);
                break;
                case 8: stergere_persoana(connection);
                    afisare_persoane(statement);
                break;

            }
        }while(optiune!=0);

        connection.close();
        statement.close();
    }
}
