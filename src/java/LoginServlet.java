import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL for SQL Server connection
    private static final String JDBC_URL = "jdbc:sqlserver://DESKTOP-FAPV9II\\KHOADEV;databaseName=account";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "12345";

    // SQL query to validate the user
    private static final String SQL_QUERY = "SELECT * FROM account WHERE username=? AND password=?";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Load the SQL Server JDBC Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish connection
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {

                // Set the parameters for the query
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if login is successful
                if (resultSet.next()) {
                    // Login success, redirect to another page
                    response.sendRedirect("success.jsp");  // Replace with your success page
                } else {
                    // Login failed, stay on the login page and show error message
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<html><body>");
                    out.println("<h3>Invalid username or password. Please try again.</h3>");
                    out.println("</body></html>");
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("SQL Server Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Optionally, redirect to the login page or handle GET requests
        response.sendRedirect("index.html");  
    }
}
