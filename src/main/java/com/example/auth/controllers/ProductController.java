package com.example.auth.controllers;

import com.example.auth.entities.Product;
import com.example.auth.dtos.ProductRequestDTO;
import com.example.auth.dtos.ProductResponseDTO;
import com.example.auth.exceptions.ProductNotFoundException;
import com.example.auth.security.TokenService;
import com.example.auth.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/create")
    @Operation(summary = "Create a new product",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequestDTO.class),
                            examples = @ExampleObject(value = """
{
  "name": "Tenis Nike Dunk Low",
  "price": 899.90
}
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    public ResponseEntity createProduct(@RequestBody @Valid ProductRequestDTO body){
        Product createdProduct = productService.createProduct(body);
        return ResponseEntity.ok(new ProductResponseDTO(createdProduct));
    }

    @GetMapping()
    @Operation(summary = "Return a list of all products",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<?> getAllProductsAPI(HttpServletRequest request){
        HttpSession session = request.getSession(false); // Obtém a sessão sem criar uma se não existir
        if (session != null) {
            String token = (String) session.getAttribute("token");
            if (token != null && tokenService.validateToken(token)) {
                List<ProductResponseDTO> products = productService.getAllProducts();
                return ResponseEntity.ok(products);

            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session not found");
    }


    @ControllerAdvice
    public static class GlobalExceptionHandler {
        @ExceptionHandler(ProductNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
        public ResponseEntity<String> handleProductNotFound(ProductNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
