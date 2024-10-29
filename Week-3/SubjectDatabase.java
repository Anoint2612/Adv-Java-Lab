package Anoint;

import java.sql.*;

public class SubjectDatabase {

    private static final String URL = "jdbc:mysql://localhost:3306/DB_30";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Create a statement to retrieve data
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT CODE, NAME, Department, Credits FROM Subject"); // Selecting specific columns

            // i. Update the Name of the subject from "Java Programming Lab" to "Advanced Java Programming Lab"
            while (resultSet.next()) {
                if (resultSet.getString("CODE").equals("CSL56")) {
                    resultSet.updateString("NAME", "Advanced Java Programming Lab");
                    resultSet.updateRow();
                    System.out.println("Updated subject name for CODE CSL56.");
                    break; // Exit loop after updating
                }
            }

            // ii. Delete the subject "System Programming"
            String deleteSQL = "DELETE FROM Subject WHERE NAME = ?"; // Ensure table name matches
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.setString(1, "System Programming");
                int rowsDeleted = preparedStatement.executeUpdate();
                System.out.println("Deleted " + rowsDeleted + " rows with subject name 'System Programming'.");
            }

            // iii. Display details of all the Subjects
            resultSet.beforeFirst(); // Move cursor before the first row
            System.out.println("\nSubject Details:");
            while (resultSet.next()) {
                String code = resultSet.getString("CODE");
                String name = resultSet.getString("NAME");
                String department = resultSet.getString("Department");
                int credits = resultSet.getInt("Credits");
                System.out.println("Code: " + code + ", Name: " + name + ", Department: " + department + ", Credits: " + credits);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
