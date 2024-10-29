import java.sql.*;

public class MoviesDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Create an updatable ResultSet
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM Movies");

            // i. Display details of all the Movies from the table
            System.out.println("Details of all Movies:");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Genre: %s, IMDB Rating: %.1f, Year: %d%n",
                        rs.getInt("ID"),
                        rs.getString("Movie_Name"),
                        rs.getString("Genre"),
                        rs.getFloat("IMDB_Rating"),
                        rs.getInt("Year"));
            }

            // ii. Display details of 5th Movie from the table
            rs.absolute(5);
            System.out.printf("Details of 5th Movie: ID: %d, Name: %s, Genre: %s, IMDB Rating: %.1f, Year: %d%n",
                    rs.getInt("ID"),
                    rs.getString("Movie_Name"),
                    rs.getString("Genre"),
                    rs.getFloat("IMDB_Rating"),
                    rs.getInt("Year"));

            // iii. Insert a new row into the table using PreparedStatement
            String insertSQL = "INSERT INTO Movies (Movie_Name, Genre, IMDB_Rating, Year) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                pstmt.setString(1, "New Movie");
                pstmt.setString(2, "Drama");
                pstmt.setFloat(3, 8.5f);
                pstmt.setInt(4, 2024);
                pstmt.executeUpdate();
            }
            // Display all details after insertion
            rs = stmt.executeQuery("SELECT * FROM Movies");
            System.out.println("Details of all Movies after insertion:");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Genre: %s, IMDB Rating: %.1f, Year: %d%n",
                        rs.getInt("ID"),
                        rs.getString("Movie_Name"),
                        rs.getString("Genre"),
                        rs.getFloat("IMDB_Rating"),
                        rs.getInt("Year"));
            }

            // iv. Delete a row from the table where the IMDB_Rating is less than 5
            String deleteSQL = "DELETE FROM Movies WHERE IMDB_Rating < 5";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Deleted " + rowsAffected + " movies with IMDB Rating less than 5.");
            }

            // v. Update the Genre of a movie with ID as 10 to “Sci-fi”
            String updateSQL = "UPDATE Movies SET Genre = ? WHERE ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                pstmt.setString(1, "Sci-fi");
                pstmt.setInt(2, 10);
                int rowsUpdated = pstmt.executeUpdate();
                System.out.println("Updated " + rowsUpdated + " movie(s) with ID 10 to Genre 'Sci-fi'.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
