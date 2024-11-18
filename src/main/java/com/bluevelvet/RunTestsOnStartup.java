package com.bluevelvet;

import com.bluevelvet.controller.AuthenticationController;
import com.bluevelvet.model.*;
import com.bluevelvet.repository.*;
import com.bluevelvet.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RunTestsOnStartup implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Override
    public void run(String... args) throws Exception {
        logger.debug("ESTOU FUNFANTE");
        Product product1 = new Product();
        product1.setName("Produto de Teste 1");
        product1.setShortDescription("Descrição curta do Produto 1");
        product1.setLongDescription("Descrição longa detalhando o Produto de Teste 1");
        product1.setPrice(150.0f);
        product1.setDiscount(5.0f);
        product1.setStatus(true);
        product1.setHasStock(true);
        product1.setWidth(10.0f);
        product1.setLength(20.0f);
        product1.setHeight(30.0f);
        product1.setCost(100.0f);
        product1.setCreationTime(LocalDateTime.now());
        product1.setUpdateTime(LocalDateTime.now());

        Product product2 = new Product();
        product2.setName("Produto de Teste 2");
        product2.setShortDescription("Descrição curta do Produto 2");
        product2.setLongDescription("Descrição longa detalhando o Produto de Teste 2");
        product2.setPrice(200.0f);
        product2.setDiscount(10.0f);
        product2.setStatus(true);
        product2.setHasStock(true);
        product2.setWidth(15.0f);
        product2.setLength(25.0f);
        product2.setHeight(35.0f);
        product2.setCost(150.0f);
        product2.setCreationTime(LocalDateTime.now());
        product2.setUpdateTime(LocalDateTime.now());

        Brand brand = new Brand();
        brand.setBrandName("RGV");
        try {
            brandRepository.save(brand);
        } catch (Exception e) {}

        Category category = new Category();
        category.setCategoryName("Discos de Vinil");
        try {
            categoryRepository.save(category);
        } catch (Exception e) {}

        Role role1 = new Role();
        role1.setName("ADMIN");
        role1.setDescription("A role for administrator in BV");

        Role role2 = new Role();
        role2.setName("EDITOR");
        role2.setDescription("A role for editor in BV");

        Role role3 = new Role();
        role3.setName("USER");
        role3.setDescription("A role for user in BV");

        try {
            roleRepository.save(role1);
            roleRepository.save(role2);
            roleRepository.save(role3);
        } catch (Exception e) {}

        product1.setBrand(brand);
        product2.setBrand(brand);
        product1.getCategories().add(category);
        try {
            productRepository.save(product1);
            productRepository.save(product2);
        } catch (Exception e) {}

        category.getProducts().add(product1);
        try {
            categoryRepository.save(category);
        } catch (Exception e) {}

        try {
            productRepository.save(product1);
            productRepository.save(product2);
        } catch (Exception e) {}

        User user = new User();
        user.setName("my");
        user.setLastName("user");
        user.setEmail("test@gmail.com");
        user.setPassword(new BCryptPasswordEncoder().encode("mypass"));
        user.getRoles().add(role1);
        user.setStatus(true);
        try {
            userRepository.save(user);
            role1.getUsers().add(user);
            roleService.saveRole(role1);
        } catch (Exception e) {}
    }
}
