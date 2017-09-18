package main.controller;

import io.swagger.annotations.ApiOperation;
import main.services.DatabaseServices;
import main.view.CatagoryVO;
import main.modal.Catagory;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import main.view.CatagoryReadVO;
import main.view.CatagoryUpdateVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catagory")
public class CatagoryController {

    @Inject
    private DatabaseServices databaseServices;

   
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "Parent is will be 0 for root catagory",response = Iterable.class)
    public ResponseEntity addCatagory(@RequestBody(required = true) CatagoryVO catagoryVO)
            throws SQLException {
        Catagory cat = new Catagory();
        cat.setParentId(catagoryVO.getParentId());
        cat.setName(catagoryVO.getName());
        databaseServices.addCatagoryByParent(cat);
        return new ResponseEntity("Catagory created successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity addProduct(@RequestBody(required = true) CatagoryUpdateVO catagoryUpdateVO
            ) throws SQLException {
        Catagory cat = new Catagory();
        cat.setParentId(catagoryUpdateVO.getParentId());
        cat.setName(catagoryUpdateVO.getName());
        cat.setId(catagoryUpdateVO.getId());
        databaseServices.updateCatagory(cat);
        return new ResponseEntity("Catagory updated successfully", HttpStatus.OK);
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteCatagory(@RequestParam(value="id") int id
            ) throws SQLException {
        databaseServices.deleteCatagory(id);
        return new ResponseEntity("Catagory deleted successfully", HttpStatus.OK);
    }
    
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    @ResponseBody
    public List<CatagoryReadVO> getAllCatagory(
            ) throws SQLException {
        return databaseServices.getAllCatagory();
       
    }
}
