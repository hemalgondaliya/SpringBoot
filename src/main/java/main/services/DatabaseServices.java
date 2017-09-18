package main.services;

import java.io.IOException;
import main.modal.Catagory;
import main.modal.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.view.CatagoryReadVO;

import org.springframework.stereotype.Service;
import main.view.ProductResultVO;
import org.json.JSONException;

@Service
public class DatabaseServices {

    private final String USER = "root";
    private final String PASS = "";

    private String insertProductQuery = "INSERT INTO `product`(`name`, `catagory_id`, `price`) VALUES (?,?,?)";

    private String insertCatagory = "INSERT INTO `catagory`(`name`, `parent_id`) VALUES (?,?)";

    private String updateCatagory = "UPDATE `catagory` SET `name` = ?, `parent_id` = ? WHERE `catagory`.`id` = ?";
    
    private String updateProduct = "UPDATE `product` SET `name` = ?, `catagory_id` = ?, `price` = ? WHERE `product`.`id` = ?";
    
    private String deleteProduct = "DELETE FROM `product` WHERE `product`.`id` = ?";
    
    private String deleteCatagory = "DELETE FROM `catagory` WHERE `catagory`.`id` = ?";
    
    private String selectProduct = "SELECT * FROM `product` WHERE `product`.`id` = ?";
    
    private String selectCatagory = "SELECT * FROM `catagory` WHERE `catagory`.`parent_id` = 0";

    /**
     *
     * @param product
     * @throws SQLException
     *  create new product
     */
    public void addProduct(Product product) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(insertProductQuery);
        stmt.setString(1,product.getName());
        stmt.setInt(2,product.getCatagoryId());
        stmt.setInt(3,product.getPrice());
        stmt.execute();
        conn.close();
    }

    /**
     *
     * @param catagory
     * @throws SQLException
     *  Create new catagory
     */
    public void addCatagoryByParent(Catagory catagory) throws SQLException {
        //TODO: validate the parent_id is correct
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(insertCatagory);
        stmt.setString(1,catagory.getName());
        stmt.setInt(2,catagory.getParentId());
        stmt.execute();
        conn.close();
    }

    /**
     *
     * @param catagory
     * @throws SQLException
     *  update the catagory
     */
    public void updateCatagory(Catagory catagory) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(updateCatagory);
        stmt.setString(1,catagory.getName());
        stmt.setInt(2,catagory.getParentId());
        stmt.setInt(3,catagory.getId());
        stmt.execute();
        conn.close();
    }
    
    /**
     * 
     * @param product
     * @throws SQLException 
     * Update the product
     */
    public void updateProduct(Product product) throws SQLException{
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(updateProduct);
        stmt.setString(1,product.getName());
        stmt.setInt(2,product.getCatagoryId());
        stmt.setInt(3,product.getPrice());
        stmt.setInt(4,product.getId());
        stmt.execute();
        conn.close();
    }
    
    /**
     * 
     * @param id
     * @throws SQLException 
     * delete product by id
     */
    public void deleteProduct(int id) throws SQLException{
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(deleteProduct);
        stmt.setInt(1, id);
        stmt.execute();
        conn.close();
    }
    
    /**
     * 
     * @param id
     * @return
     * @throws SQLException 
     * Get the product by id
     */
    public ProductResultVO getProduct(int id,String requestedCurrency) throws SQLException, IOException, JSONException{
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(selectProduct);
        stmt.setInt(1, id);
        ResultSet res = stmt.executeQuery();
        ProductResultVO resultVO = new ProductResultVO();
        float rate = CurrencyService.getCurrentRate(requestedCurrency);
            while (res.next()) {
                resultVO.setName(res.getString("name"));
                resultVO.setCurrency(requestedCurrency);
                resultVO.setPrice(rate * Float.parseFloat(res.getString("price")));
               }
        return resultVO;
    }
    
    /**
     * 
     * @param id
     * @throws SQLException
     * Delete catagory by id (Cascade delete will be performed)
     */
    public void deleteCatagory(int id) throws SQLException{
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(deleteCatagory);
        stmt.setInt(1, id);
        stmt.execute();
        conn.close();
    }

    /**
     * 
     * @return
     * @throws SQLException 
     * Get all root catagory with id
     */
    public List<CatagoryReadVO> getAllCatagory() throws SQLException{
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(selectCatagory);
        List<CatagoryReadVO> catagoryList = new ArrayList<>();
        while(rs.next()){
            CatagoryReadVO cat = new CatagoryReadVO();
            cat.setId(rs.getInt("id"));
            cat.setName(rs.getString("name"));
            catagoryList.add(cat);
        }
       return catagoryList;
    }

    /**
     * 
     * @return 
     * get database connection
     */
    private Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test11",USER,PASS);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
