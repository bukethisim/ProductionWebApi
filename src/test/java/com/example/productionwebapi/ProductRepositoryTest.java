package com.example.productionwebapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
//import org.junit.runner.RunWith;
import com.example.productionwebapi.model.Product;
import com.example.productionwebapi.repository.ProductRepository;

//@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
	private ProductRepository productRepository;

    @Autowired
	private TestEntityManager entityManager;


	/*
    @Test
    public void testSaveProduct(){
        Product product= new Product();
		product.setName("elma");
		product.setPrice(25.0);
		product.setQuantity(2);
        Long id = entityManager.persistAndGetId(newProduct, Long.class);
        assertNotNull(id);
		productRepository.save(product);
    }
    */
	@Test
	public void testUpdate(){
		Product product=getProduct();
		productRepository.save(product);

		Product savedProduction = productRepository.findById(product.getId()).get();
		savedProduction.setName("ananas");
		savedProduction.setPrice(1);
		Product updateProduct=productRepository.save(savedProduction);
		assertThat(updateProduct.getName()).isEqualTo("ananas");
		assertThat(updateProduct.getPrice()).isEqualTo(1);

	}
	@Test
	public void testFindById() {
		Product product = getProduct();
		productRepository.save(product);
		Product result = productRepository.findById(product.getId()).get();
		assertEquals(product.getId(), result.getId());
	}

	@Test
	public void testSave() {
		Product product = getProduct();
		productRepository.save(product);
		Product found = productRepository.findById(product.getId()).get();
		assertEquals(product.getId(), found.getId());
	}

	@Test
	public void testDeleteProduct() {
		Product product = getProduct();
		productRepository.save(product);
		productRepository.deleteById(product.getId());
		List<Product> result = new ArrayList<>();
		productRepository.findAll().forEach(e -> result.add(e));
		assertEquals(result.size(), 0);
	}
	@Test
	public void testFindAll() {
		Product product = getProduct();
		productRepository.save(product);
		List<Product> result = new ArrayList<>();
		productRepository.findAll().forEach(e -> result.add(e));
		assertEquals(result.size(), 1);
	}

	private Product getProduct() {
		 Product product=Product.builder()
				.name("elma")
				.price(25.0)
				.quantity(2)
				.build();
		return product;
	}

}
