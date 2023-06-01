package com.example.productionwebapi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import com.example.productionwebapi.service.ProductService;
import com.example.productionwebapi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class ProductionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenProductObject_whenProduct_thenReturnSavedProduct() throws Exception{

        Product product=Product.builder()
                .name("elma")
                .price(25.0)
                .quantity(2)
                .build();
        given(productService.createProduct(any(Product.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.name",
                        is(product.getName())))
                .andExpect(jsonPath("$.price",
                        is(product.getPrice())))
                .andExpect(jsonPath("$.quantity",
                        is(product.getQuantity())));

    }

    @Test
    public void givenListOfProduct_whenGetAllProducts_thenReturnProductList() throws Exception{

        List<Product> listOfProducts = new ArrayList<>();
        listOfProducts.add(Product.builder().name("elma").price(25.0).quantity(2).build());
        listOfProducts.add(Product.builder().name("muz").price(14.0).quantity(1).build());

        given(productService.getAllProduct()).willReturn(listOfProducts);

        ResultActions response = mockMvc.perform(get("/products"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfProducts.size())));

    }
    @Test
    public void givenProductId_whenGetProductById_thenReturnProductObject() throws Exception{

        long productId = 1L;
        Product product=Product.builder()
                .name("elma")
                .price(25.0)
                .quantity(2)
                .build();

        given(productService.getProductById(productId)).willReturn(product);

        ResultActions response = mockMvc.perform(get("/products/{id}", productId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.quantity", is(product.getQuantity())))
                .andExpect(jsonPath("$.price", is(product.getPrice())));

    }
    @Test
    public void givenUpdatedProduct_whenUpdateProduct_thenReturnUpdateProductObject() throws Exception{

        long productId = 1L;
        Product savedProduct= Product.builder()
                .name("elma")
                .price(25.0)
                .quantity(2)
                .build();

        Product updateProduct =Product.builder()
                .name("muz")
                .price(14.0)
                .quantity(1)
                .build();

        given(productService.getProductById(productId)).willReturn(savedProduct);
        given(productService.updateProduct(any(Product.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateProduct)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updateProduct.getName())))
                .andExpect(jsonPath("$.quantity", is(updateProduct.getQuantity())))
                .andExpect(jsonPath("$.price", is(updateProduct.getPrice())));
    }

    @Test
    public void givenProductId_whenDeleteProduct_thenReturn200() throws Exception{
        long productId = 1L;
        willDoNothing().given(productService).deleteProduct(productId);
        ResultActions response = mockMvc.perform(delete("/products/{id}", productId));
        response.andExpect(status().isOk())
                .andDo(print());
    }

}
