package main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import main.services.DatabaseServices;
import main.view.ProductVO;
import main.modal.Product;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.inject.Inject;
import main.view.ProductResultVO;
import main.view.ProductUpdateVO;
import main.view.ReadProductVO;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Inject
    private DatabaseServices databaseServices;
    
    @ApiOperation(value = "Create new product", response = Iterable.class)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addProduct(@RequestBody(required = true) ProductVO productVO
    ) throws SQLException {
        Product product = new Product();
        product.setName(productVO.getName());
        product.setCatagoryId(productVO.getCatagoryId());
        product.setPrice(productVO.getPrice());
        databaseServices.addProduct(product);
        return new ResponseEntity("Product created successfully", HttpStatus.OK);
 
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity updateProduct(@RequestBody(required = true) ProductUpdateVO productUpdateVO
    ) throws SQLException {

        Product product = new Product();
        product.setId(productUpdateVO.getId());
        product.setName(productUpdateVO.getName());
        product.setCatagoryId(productUpdateVO.getCatagoryId());
        product.setPrice(productUpdateVO.getPrice());
        databaseServices.updateProduct(product);
        return new ResponseEntity("Product updated successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteProduct(@RequestParam(value = "id") int id
    ) throws SQLException {
        databaseServices.deleteProduct(id);
        return new ResponseEntity("Product deleted successfully", HttpStatus.OK);
    }

    /**
     *
     * @param readProductVO
     * @return
     */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    @ResponseBody
    public ProductResultVO getProductbyId(@RequestBody(required = true) ReadProductVO readProductVO) throws SQLException, IOException, JSONException {
        ProductResultVO result
                = databaseServices.getProduct(readProductVO.getId(), readProductVO.getCurrency());
        return result;
    }

}
