package com.ingem.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingem.interview.exception.CustomRequestErrorResponse;
import com.ingem.interview.model.Product;
import com.ingem.interview.repository.ProductRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = InterviewApplicationTests.class)
@AutoConfigureMockMvc
public class ProductControllerIT {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    private Product initProduct() {
        Product testProduct = new Product();
        testProduct.setCode("8888888888");
        testProduct.setName("testName");
        testProduct.setDescription("testDescription");
        testProduct.setPriceHrk(new BigDecimal(999));
        testProduct.setAvailable(Boolean.TRUE);
        return testProduct;
    }

    private void cleanUp(String code) {
        Optional<Product> existingProduct = productRepository.findByCode(code);
        if (existingProduct.isPresent()) {
            productRepository.delete(existingProduct.get());
        }
    }

    @Test
    @Order(1)
    void testCreateProduct() throws Exception {
        Product testProduct = initProduct();
        cleanUp(testProduct.getCode());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus() == 200);
        assertNotNull(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Product.class).getId());
    }

    @Test
    @Order(2)
    void testCreateProductDuplicateCode() throws Exception {
        Product testProduct = initProduct();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("Duplicate code", objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomRequestErrorResponse.class).getErrors().get("code").get(0));
    }

    @Test
    @Order(3)
    void testCreateProductIdNotNull() throws Exception {
        Product testProduct = initProduct();
        testProduct.setId(999L);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("Must be null", objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomRequestErrorResponse.class).getErrors().get("id").get(0));
    }

    @Test
    @Order(4)
    void testCreateProductMethodNotValidArgumentException() throws Exception {
        Product testProduct = initProduct();
        testProduct.setCode("12");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus() == 400);
    }

    @Test
    @Order(5)
    void testGetAllProducts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<Product> products = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));
        assertThat(products.size() > 0);
    }

    @Test
    @Order(6)
    void testGetProductById() throws Exception {
        Product testProduct = productRepository.findAll().get(0);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", String.valueOf(testProduct.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Product product = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Product.class);
        assertEquals(testProduct, product);
    }


    @Test
    @Order(7)
    void testGetProductByIdNotFound() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("Product not found", objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomRequestErrorResponse.class).getErrors().get("id").get(0));
    }

    @Test
    @Order(8)
    void testUpdateProduct() throws Exception {
        Product testProduct = productRepository.findAll().get(0);
        testProduct.setName("Edited Name");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Product product = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Product.class);
        assertEquals(testProduct, product);
    }

    @Test
    @Order(9)
    void testUpdateProductByIdNotFound() throws Exception {
        Product testProduct = productRepository.findAll().get(0);
        testProduct.setId(999L);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("Product not found", objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomRequestErrorResponse.class).getErrors().get("id").get(0));
    }

    @Test
    @Order(10)
    void testUpdateProductDuplicateCode() throws Exception {
        String testCode = "9999999999";
        cleanUp(testCode);
        Product tempProduct = initProduct();
        tempProduct.setId(null);
        tempProduct.setCode(testCode);
        productRepository.save(tempProduct);
        Product testProduct = productRepository.findAll().get(0);
        testProduct.setCode(testCode);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("Duplicate code", objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomRequestErrorResponse.class).getErrors().get("code").get(0));
        cleanUp(testCode);
    }

    @Test
    @Order(11)
    void testUpdateProductMethodNotValidArgumentException() throws Exception {
        Product testProduct = productRepository.findAll().get(0);
        testProduct.setCode("12");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus() == 400);
    }

    @Test
    @Order(12)
    void testDeleteProduct() throws Exception {
        Product testProduct = productRepository.findAll().get(0);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", String.valueOf(testProduct.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        assertFalse(productRepository.findById(testProduct.getId()).isPresent());
    }

    @Test
    @Order(13)
    void testDeleteProductByIdNotFound() throws Exception {
        Product testProduct = productRepository.findAll().get(0);
        testProduct.setId(999L);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("Product not found", objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomRequestErrorResponse.class).getErrors().get("id").get(0));
    }

}
