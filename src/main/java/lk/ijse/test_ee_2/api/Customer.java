package lk.ijse.test_ee_2.api;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "customer",urlPatterns = "/customer",
                initParams = {
                    @WebInitParam(name = "db-user",value = "root"),
                    @WebInitParam(name = "db-pw",value = "1234"),
                    @WebInitParam(name = "db-url",value = "jdbc:mysql://localhost:3306/javaEE"),
                    @WebInitParam(name = "db-class",value= "com.mysql.cj.jdbc.Driver")
                }
                ,loadOnStartup = 5
)
public class Customer extends HttpServlet {
    Connection connection;
    String SAVE_DATA = "INSERT INTO Customer(customer_name,email,city) VALUES (?,?,?)";
    String READ_DATA = "SELECT * FROM Customer";



    @Override
    public void init() throws ServletException {
        String userName = getServletConfig().getInitParameter("db-user");
        String password = getServletConfig().getInitParameter("db-pw");
        String url = getServletConfig().getInitParameter("db-url");



        try {
            Class.forName(getServletConfig().getInitParameter("db-class"));
            this.connection =DriverManager.getConnection(url,userName,password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String customer_name = req.getParameter("customer_name");
        String email = req.getParameter("email");
        String city = req.getParameter("city");

        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html");

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_DATA);
            preparedStatement.setString(1,customer_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,city);

            if (preparedStatement.executeUpdate() !=0){
                System.out.println("Data Save");
            }else {
                System.out.println("Not Save");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String id = req.getParameter("customer_name");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_DATA);
//            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery(READ_DATA);

            while (resultSet.next()){
                writer.println(resultSet.getString(1));
                writer.println(resultSet.getString(2));
                writer.println(resultSet.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }







    }

    String UPDATE_CUSTOMER =  "UPDATE  Customer set customer_name = ? , email = ?  WHERE customer_name = ?";
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String customer_name = req.getParameter("customer_name");
        String email = req.getParameter("email");
        String city = req.getParameter("city");

        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html");

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER);

            preparedStatement.setString(1,email);
            preparedStatement.setString(2,city);
            preparedStatement.setString(3,customer_name);

            if (preparedStatement.executeUpdate() !=0){
                System.out.println("update  Customer");
            }else {
                System.out.println("Delete Customer");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String DELETE_CUSTOMER = "DELETE FROM Customer WHERE customer_name = ?";
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String deleteCutName = req.getParameter("customer_name");

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CUSTOMER);
            preparedStatement.setString(1,deleteCutName);

            if (preparedStatement.executeUpdate() !=0){
                System.out.println("Delete");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
