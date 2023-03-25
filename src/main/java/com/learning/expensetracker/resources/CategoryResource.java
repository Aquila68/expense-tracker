package com.learning.expensetracker.resources;

import com.learning.expensetracker.models.Category;
import com.learning.expensetracker.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(path="/api/categories")
public class CategoryResource {
    private static final String USERID="userId";
    @Autowired
    private CategoryService categoryService;

    @GetMapping(path="")
    public List<Category> getAllCategories(HttpServletRequest request){
        String userId =(String)request.getAttribute(USERID);
        return categoryService.fetchAllCategories(userId);
    }

    @PostMapping(path = "")
    public String createCategory(HttpServletRequest request,@RequestBody Map<String,Object> categoryMap){
        Category category=new Category();

        category.setUserId((String)request.getAttribute(USERID));
        category.setCategoryId((String)categoryMap.get("categoryId"));
        category.setTitle((String) categoryMap.get("title"));
        category.setDescription( (String) categoryMap.get("description"));
        category.setTotalExpense((Double) categoryMap.get("totalExpense"));

        return categoryService.addCategory(category);

    }

    @GetMapping(path="/{categoryId}")
    public ResponseEntity<Category> findCategoryById(HttpServletRequest request, @PathVariable String categoryId){
        String userId=(String)request.getAttribute(USERID);
        Category category=categoryService.fetchCategoryById(categoryId,userId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping(path = "/{categoryId}")
    public ResponseEntity<Map<String,Boolean>> updateCategory(HttpServletRequest request,
                                                              @PathVariable String categoryId,@RequestBody Map<String,Object> categoryMap){

        Category category=new Category();

        String userId=(String)request.getAttribute(USERID);
        category.setUserId(userId);
        category.setCategoryId((String)categoryMap.get("categoryId"));
        category.setTitle((String) categoryMap.get("title"));
        category.setDescription( (String) categoryMap.get("description"));
        category.setTotalExpense((Double) categoryMap.get("totalExpense"));

        categoryService.updateCategory(category);
        Map<String,Boolean> map=new HashMap<>();
        map.put("Success",true);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @DeleteMapping(path = "/{categoryId}")
    public ResponseEntity<String> deleteCategory(HttpServletRequest request,@PathVariable String categoryId){
        String userId=(String)request.getAttribute(USERID);
        categoryService.deleteCategory(categoryId,userId);
        return new ResponseEntity<>("Deletion successful",HttpStatus.OK);
    }

}
