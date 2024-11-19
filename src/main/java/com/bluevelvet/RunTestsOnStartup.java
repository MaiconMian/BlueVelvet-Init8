package com.bluevelvet;

import com.bluevelvet.controller.AuthenticationController;
import com.bluevelvet.model.*;
import com.bluevelvet.repository.*;
import com.bluevelvet.service.RoleService;
import java.util.Base64;
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

    @Override
    public void run(String... args) throws Exception {

        // adicionando imagem basica que será usada em todas as inserções
        String imagemBase = "iVBORw0KGgoAAAANSUhEUgAAAGUAAABeCAIAAAC1o032AAAACXBIWXMAAA7EAAAOxAGVKw4bAAAA3ElEQVR4nO3QwQ3AIBDAsNL9dz5WIC+EZE8QZc3Mx7H/dsBj/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8ajYNfwO5QbHnsQAAAABJRU5ErkJggg==";
        byte[] imageBytes = Base64.getDecoder().decode(imagemBase);

        // insert roles test
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

        // create a user
        User user = new User();
        user.setName("Florentino");
        user.setLastName("Souza");
        user.setEmail("florentino@bluevelvet.com");
        user.setPassword(new BCryptPasswordEncoder().encode("florentino123"));
        user.getRoles().add(role1);
        user.setStatus(true);
        try {
            userRepository.save(user);
            role1.getUsers().add(user);
            roleService.saveRole(role1);
        } catch (Exception e) {}

        // insert brands test
        Brand brand = new Brand();
        brand.setBrandName("Brand Test 1");
        try {
            brandRepository.save(brand);
        } catch (Exception e) {}

        // insert category test
        Category category = new Category();
        category.setCategoryName("Category Test 1");
        try {
            categoryRepository.save(category);
        } catch (Exception e) {}

        // create 10 products
        for (int i = 1; i <= 10; i++){

            String nome = "Product " + i;
            String shortDescription = "Short description for product " + i;
            String longDescription = "Long description for product " + i;

            Product product = new Product();
            product.setName(nome);
            product.setShortDescription(shortDescription);
            product.setLongDescription(longDescription);
            product.setImage(imageBytes);
            product.setPrice(150.0f);
            product.setDiscount(5.0f);
            product.setStatus(true);
            product.setHasStock(true);
            product.setWidth(10.0f);
            product.setLength(20.0f);
            product.setHeight(30.0f);
            product.setCost(100.0f);
            product.setCreationTime(LocalDateTime.now());
            product.setUpdateTime(LocalDateTime.now());
            product.setBrand(brand);
            product.getCategories().add(category);

            try {
                productRepository.save(product);
            } catch (Exception e) {}

            category.getProducts().add(product);
            brand.getProducts().add(product);

            try {
                productRepository.save(product);
            } catch (Exception e) {}

        }

        try {
            categoryRepository.save(category);
        } catch (Exception e) {}

        try {
            brandRepository.save(brand);
        } catch (Exception e) {}

    }
}
