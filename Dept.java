package Anoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Dept {
    public static void main(String[] args) {
        // JDBC URL, username, and password of MySQL server
        String url = "jdbc:mysql://localhost:3306/DB_30"; // Change to your database name
        String user = "root"; // Replace with your MySQL username
        String password = ""; // Replace with your MySQL password

        // Establish the connection
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Scanner scanner = new Scanner(System.in)) {

            // i. Display details of all the Departments using Statement Object.
            displayAllDepartments(conn);

            // ii. Display details of all the Departments which are established in a specific year using PreparedStatement object.
            System.out.print("Enter the year to find departments established in that year: ");
            int year = scanner.nextInt();
            displayDepartmentsByYear(conn, year);

            // iii. Display details of all the Departments by reading Dept_ID and Department Name from the user using PreparedStatement object.
            System.out.print("Enter Department ID: ");
            int deptId = scanner.nextInt();
            System.out.print("Enter Department Name: ");
            String deptName = scanner.next();
            displayDepartmentByIdAndName(conn, deptId, deptName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllDepartments(Connection conn) throws SQLException {
        String query = "SELECT * FROM Department";
        try (var stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("All Departments:");
            while (rs.next()) {
                System.out.println("Dept_ID: " + rs.getInt("Dept_ID") +
                                   ", Name: " + rs.getString("NAME") +
                                   ", Year Established: " + rs.getInt("Year_Estd") +
                                   ", Head Name: " + rs.getString("Head_name") +
                                   ", No of Employees: " + rs.getInt("No_of_Employees"));
            }
        }
    }

    private static void displayDepartmentsByYear(Connection conn, int year) throws SQLException {
        String query = "SELECT * FROM Department WHERE Year_Estd = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, year);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Departments established in " + year + ":");
                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    System.out.println("Dept_ID: " + rs.getInt("Dept_ID") +
                                       ", Name: " + rs.getString("NAME") +
                                       ", Year Established: " + rs.getInt("Year_Estd") +
                                       ", Head Name: " + rs.getString("Head_name") +
                                       ", No of Employees: " + rs.getInt("No_of_Employees"));
                }
                if (!hasResults) {
                    System.out.println("No departments found for the year " + year);
                }
            }
        }
    }

    private static void displayDepartmentByIdAndName(Connection conn, int deptId, String deptName) throws SQLException {
        String query = "SELECT * FROM Department WHERE Dept_ID = ? AND NAME = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, deptId);
            pstmt.setString(2, deptName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Department found:");
                    System.out.println("Dept_ID: " + rs.getInt("Dept_ID") +
                                       ", Name: " + rs.getString("NAME") +
                                       ", Year Established: " + rs.getInt("Year_Estd") +
                                       ", Head Name: " + rs.getString("Head_name") +
                                       ", No of Employees: " + rs.getInt("No_of_Employees"));
                } else {
                    System.out.println("No department found with the provided ID and Name.");
                }
            }
        }
    }
}
