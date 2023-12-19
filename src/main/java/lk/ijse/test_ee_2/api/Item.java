package lk.ijse.test_ee_2.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.test_ee_2.dto.ItemDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "item",urlPatterns = "/item",
        initParams = {
                @WebInitParam(name = "db-user",value = "root"),
                @WebInitParam(name = "db-pw",value = "1234"),
                @WebInitParam(name = "db-url",value = "jdbc:mysql://localhost:3306/javaEE"),
                @WebInitParam(name = "db-class",value= "com.mysql.cj.jdbc.Driver")
        }
        ,loadOnStartup = 5
)
public class Item extends HttpServlet {
    Connection connection;
    @Override
    public void init() throws ServletException {
        String userName = getServletConfig().getInitParameter("db-user");
        String password = getServletConfig().getInitParameter("db-pw");
        String url = getServletConfig().getInitParameter("db-url");



        try {
            Class.forName(getServletConfig().getInitParameter("db-class"));
            this.connection = DriverManager.getConnection(url,userName,password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //Insert Data
    String SAVE_DATA = "INSERT INTO Item(code,descr,qty,unitPrice) VALUES (?,?,?,?)";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Jsonb jsonb = JsonbBuilder.create();
        List<ItemDTO> itemDTOList = jsonb.fromJson(req.getReader(), new ArrayList<ItemDTO>() {}.getClass().getGenericSuperclass());

        for (ItemDTO itemDTO : itemDTOList) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_DATA)) {
                preparedStatement.setString(1, itemDTO.getCode());
                preparedStatement.setString(2, itemDTO.getDescr());
                preparedStatement.setInt(3, itemDTO.getQty());
                preparedStatement.setDouble(4, itemDTO.getUnitPrice());

                int rowsAffected = preparedStatement.executeUpdate();

                System.out.println(rowsAffected + " rows affected");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }


    //Read Data
    //String READ_DATA = "SELECT * FROM ITEM";
    String READ_DATA = "SELECT * FROM item WHERE code = ?";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Jsonb jsonb = JsonbBuilder.create();
        ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);
        String searchCode = itemDTO.getCode();


        try {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_DATA);
            preparedStatement.setString(1,searchCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.print(resultSet.getString(1)+" ");
                System.out.print(resultSet.getString(2)+" ");
                System.out.print(resultSet.getString(3)+" ");
                System.out.print(resultSet.getString(4)+" ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    //Update Data
    String UPDATE_DATA = "UPDATE item SET  item.descr =?,item.qty=?,item.unitPrice=? WHERE item.code = ?";
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DATA);
            preparedStatement.setString(1,itemDTO.getDescr());
            preparedStatement.setInt(2,itemDTO.getQty());
            preparedStatement.setDouble(3,itemDTO.getUnitPrice());
            preparedStatement.setString(4,itemDTO.getCode());

            if (preparedStatement.executeUpdate() !=0){
                System.out.println("Update");
            }else{
                System.out.println("Not Update");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    String DELETE_DATA = "DELETE FROM  item WHERE code = ?";
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Jsonb jsonb = JsonbBuilder.create();
        ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DATA);
            preparedStatement.setString(1,itemDTO.getCode());

            if (preparedStatement.executeUpdate() !=0){
                System.out.println("Delete Item");
            }else
            {
                System.out.println("Not Delete");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
