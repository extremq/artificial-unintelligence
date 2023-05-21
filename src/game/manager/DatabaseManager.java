package game.manager;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager instance = null;

    public static synchronized DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();

        return instance;
    }

    DatabaseManager() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:res/Database.db");
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS scores " +
                    "(name TEXT," +
                    " score INTEGER)";
            stmt.execute(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.exit(0);
        }
    }

    public void addScore(String name, long score) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:res/Database.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO scores (name, score) VALUES ('" + name + "', '" + score + "');";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.exit(0);
        }
    }

    public String[][] getScores() {
        String[][] scores = new String[5][2];
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:res/Database.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM scores ORDER BY score desc;");
            int counter = 0;
            while (rs.next()) {
                if (counter == 5)
                    break;

                String name = rs.getString("name");
                int score = rs.getInt("score");

                scores[counter][0] = name;
                scores[counter][1] = String.valueOf(score);

                counter++;
            }

            rs.close();
            stmt.close();
            c.close();
        }
        catch (Exception exp) {
            exp.printStackTrace();
        }
        return scores;
    }
}
