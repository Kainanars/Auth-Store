package com.example.auth.controllers;

import com.example.auth.dtos.ProductResponseDTO;
import com.example.auth.security.TokenService;
import com.example.auth.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController()
@RequestMapping("product")
public class ProductControllerModelView {
    @Autowired
    private ProductService productService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/create")
    public ModelAndView productCreate(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Obtém a sessão sem criar uma se não existir
        if (session != null) {
            String token = (String) session.getAttribute("token");
            if (token != null && tokenService.validateToken(token)) {
                ModelAndView modelAndView = new ModelAndView("createProducts");
                modelAndView.addObject("token", token);

                return modelAndView;
            }
        }
        return new ModelAndView("unauthorized");
    }

    @GetMapping
    public ModelAndView getAllProducts(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            String token = (String) session.getAttribute("token");
            if (token != null && tokenService.validateToken(token)) {
                String username = (String) session.getAttribute("username");
                Integer role = (Integer) session.getAttribute("role");

                List<ProductResponseDTO> products = productService.getAllProducts();

                // Adiciona a lista de produtos ao modelo
                ModelAndView modelAndView = new ModelAndView("products");
                modelAndView.addObject("products", products);
                modelAndView.addObject("token", token);
                modelAndView.addObject("username", username);
                modelAndView.addObject("role", role);


                return modelAndView;
            }
        }
        return new ModelAndView("unauthorized"); // Retorna uma página indicando acesso não autorizado
    }
}
