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
    @Autowired
    private PermissionsRepository permissionsRepository;

    @Override
    public void run(String... args) throws Exception {

        // adicionando imagem basica que será usada em todas as inserções
        String imagemBase = "iVBORw0KGgoAAAANSUhEUgAAAGUAAABeCAIAAAC1o032AAAACXBIWXMAAA7EAAAOxAGVKw4bAAAA3ElEQVR4nO3QwQ3AIBDAsNL9dz5WIC+EZE8QZc3Mx7H/dsBj/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8avxq/Gr8ajYNfwO5QbHnsQAAAABJRU5ErkJggg==";
        byte[] imageBytes = Base64.getDecoder().decode(imagemBase);

        // creating permissions
        Permissions permission1 = new Permissions();
        permission1.setName("PERMISSION_EDIT_CREATE_PRODUCT");
        permission1.setDescription("Permission for edit a product in BV");

        Permissions permission2 = new Permissions();
        permission2.setName("PERMISSION_VIEW_PRODUCT_DETAIL");
        permission2.setDescription("Permission for view a product detail a product in BV");

        Permissions permission4 = new Permissions();
        permission4.setName("PERMISSION_DELETE_PRODUCT");
        permission4.setDescription("Permission for delete a product in BV");

        Permissions permission5 = new Permissions();
        permission5.setName("PERMISSION_CUST_PRODUCT");
        permission5.setDescription("Permission for alter cust a product in BV");

        Permissions permission6 = new Permissions();
        permission6.setName("PERMISSION_USER_VIEW");
        permission6.setDescription("Permission for view a user in BV");

        Permissions permission7 = new Permissions();
        permission7.setName("PERMISSION_CREATE_EDIT_USER");
        permission7.setDescription("Permission for create and edit a user in BV");

        Permissions permission8 = new Permissions();
        permission8.setName("PERMISSION_DELETE_USER");
        permission8.setDescription("Permission for delet a user in BV");

        Permissions permission9 = new Permissions();
        permission9.setName("PERMISSION_VIEW_BRAND");
        permission9.setDescription("Permission to view a brand in BV");

        Permissions permission10 = new Permissions();
        permission10.setName("PERMISSION_CREATE_EDIT_BRAND");
        permission10.setDescription("Permission to create and edit a brand in BV");

        Permissions permission11 = new Permissions();
        permission11.setName("PERMISSION_DELETE_BRAND");
        permission11.setDescription("Permission to delete a brand in BV");

        Permissions permission12 = new Permissions();
        permission12.setName("PERMISSION_VIEW_CATEGORY");
        permission12.setDescription("Permission to view a category in BV");

        Permissions permission13 = new Permissions();
        permission13.setName("PERMISSION_CREATE_EDIT_CATEGORY");
        permission13.setDescription("Permission to create and edit a category in BV");

        Permissions permission14 = new Permissions();
        permission14.setName("PERMISSION_DELETE_CATEGORY");
        permission14.setDescription("Permission to delete a category in BV");

        Permissions permission15 = new Permissions();
        permission15.setName("PERMISSION_VIEW_CLIENT");
        permission15.setDescription("Permission to view a client in BV");

        Permissions permission16 = new Permissions();
        permission16.setName("PERMISSION_CREATE_EDIT_CLIENT");
        permission16.setDescription("Permission to create and edit a client in BV");

        Permissions permission17 = new Permissions();
        permission17.setName("PERMISSION_DELETE_CLIENT");
        permission17.setDescription("Permission to delete a client in BV");

        Permissions permission18 = new Permissions();
        permission18.setName("PERMISSION_VIEW_SHIPPING");
        permission18.setDescription("Permission to view a shipping in BV");

        Permissions permission19 = new Permissions();
        permission19.setName("PERMISSION_UPDATE_SHIPPING_STATUS");
        permission19.setDescription("Permission to update shipping status in BV");

        Permissions permission20 = new Permissions();
        permission20.setName("PERMISSION_VIEW_ORDER");
        permission20.setDescription("Permission to view an order in BV");

        Permissions permission21 = new Permissions();
        permission21.setName("PERMISSION_CREATE_EDIT_ORDER");
        permission21.setDescription("Permission to create and edit an order in BV");

        Permissions permission22 = new Permissions();
        permission22.setName("PERMISSION_DELETE_ORDER");
        permission22.setDescription("Permission to delete an order in BV");

        Permissions permission23 = new Permissions();
        permission23.setName("PERMISSION_VIEW_QUESTION");
        permission23.setDescription("Permission to view a question in BV");

        Permissions permission24 = new Permissions();
        permission24.setName("PERMISSION_ANSWER_QUESTION");
        permission24.setDescription("Permission to answer a question in BV");

        Permissions permission25 = new Permissions();
        permission25.setName("PERMISSION_VIEW_REVIEW");
        permission25.setDescription("Permission to view a review in BV");

        Permissions permission26 = new Permissions();
        permission26.setName("PERMISSION_APPROVE_REVIEW");
        permission26.setDescription("Permission to approve a review in BV");

        Permissions permission27 = new Permissions();
        permission27.setName("PERMISSION_DELETE_REVIEW");
        permission27.setDescription("Permission to delete a review in BV");

        try {
            permissionsRepository.save(permission1);
            permissionsRepository.save(permission2);
            permissionsRepository.save(permission4);
            permissionsRepository.save(permission6);
            permissionsRepository.save(permission7);
            permissionsRepository.save(permission8);
            permissionsRepository.save(permission9);
            permissionsRepository.save(permission10);
            permissionsRepository.save(permission11);
            permissionsRepository.save(permission12);
            permissionsRepository.save(permission13);
            permissionsRepository.save(permission14);
            permissionsRepository.save(permission15);
        } catch (Exception e) {
        }

        // insert role admin with all permissions
        Role role1 = new Role();
        role1.setName("ADMIN");
        role1.setDescription("A role for administrator in BV");

        role1.getPermissions().add(permission1);
        role1.getPermissions().add(permission2);
        role1.getPermissions().add(permission4);
        role1.getPermissions().add(permission6);
        role1.getPermissions().add(permission7);
        role1.getPermissions().add(permission8);
        role1.getPermissions().add(permission9);
        role1.getPermissions().add(permission10);
        role1.getPermissions().add(permission11);
        role1.getPermissions().add(permission12);
        role1.getPermissions().add(permission13);
        role1.getPermissions().add(permission14);
        role1.getPermissions().add(permission15);

        permission1.getRoles().add(role1);
        permission2.getRoles().add(role1);
        permission4.getRoles().add(role1);
        permission6.getRoles().add(role1);
        permission7.getRoles().add(role1);
        permission8.getRoles().add(role1);
        permission9.getRoles().add(role1);
        permission10.getRoles().add(role1);
        permission11.getRoles().add(role1);
        permission12.getRoles().add(role1);
        permission13.getRoles().add(role1);
        permission14.getRoles().add(role1);
        permission15.getRoles().add(role1);

        Role role2 = new Role();
        role2.setName("SALES_MANAGER");
        role2.setDescription("A role for sales manager in BV");

        role2.getPermissions().add(permission5);
        role2.getPermissions().add(permission15);
        role2.getPermissions().add(permission16);
        role2.getPermissions().add(permission17);
        role2.getPermissions().add(permission18);
        role2.getPermissions().add(permission19);
        role2.getPermissions().add(permission20);
        role2.getPermissions().add(permission21);
        role2.getPermissions().add(permission22);

        permission5.getRoles().add(role2);
        permission15.getRoles().add(role2);
        permission16.getRoles().add(role2);
        permission17.getRoles().add(role2);
        permission18.getRoles().add(role2);
        permission19.getRoles().add(role2);
        permission20.getRoles().add(role2);
        permission21.getRoles().add(role2);
        permission22.getRoles().add(role2);

        Role role3 = new Role();
        role3.setName("EDITOR");
        role3.setDescription("A role for editor in BV");

        role3.getPermissions().add(permission1);
        role3.getPermissions().add(permission2);
        role3.getPermissions().add(permission4);
        role3.getPermissions().add(permission5);
        role3.getPermissions().add(permission9);
        role3.getPermissions().add(permission10);
        role3.getPermissions().add(permission11);
        role3.getPermissions().add(permission12);
        role3.getPermissions().add(permission13);
        role3.getPermissions().add(permission14);

        permission1.getRoles().add(role3);
        permission2.getRoles().add(role3);
        permission4.getRoles().add(role3);
        permission5.getRoles().add(role3);
        permission9.getRoles().add(role3);
        permission10.getRoles().add(role3);
        permission11.getRoles().add(role3);
        permission12.getRoles().add(role3);
        permission13.getRoles().add(role3);
        permission14.getRoles().add(role3);

        Role role4 = new Role();
        role4.setName("SHIPPING_MANAGER");
        role4.setDescription("A role for shiping_Manager in BV");

        role4.getPermissions().add(permission2);
        role4.getPermissions().add(permission20);
        role4.getPermissions().add(permission19);

        permission2.getRoles().add(role4);
        permission20.getRoles().add(role4);
        permission19.getRoles().add(role4);

        Role role5 = new Role();
        role5.setName("Assistant");
        role5.setDescription("A role for assistant in BV");

        role5.getPermissions().add(permission23);
        role5.getPermissions().add(permission24);
        role5.getPermissions().add(permission25);
        role5.getPermissions().add(permission26);
        role5.getPermissions().add(permission27);

        permission23.getRoles().add(role5);
        permission24.getRoles().add(role5);
        permission25.getRoles().add(role5);
        permission26.getRoles().add(role5);
        permission27.getRoles().add(role5);

        try {
            roleRepository.save(role1);
            roleRepository.save(role2);
            roleRepository.save(role3);
            roleRepository.save(role4);
            roleRepository.save(role5);
        } catch (Exception e) {}

        try {
            permissionsRepository.save(permission1);
            permissionsRepository.save(permission2);
            permissionsRepository.save(permission4);
            permissionsRepository.save(permission6);
            permissionsRepository.save(permission7);
            permissionsRepository.save(permission8);
            permissionsRepository.save(permission9);
            permissionsRepository.save(permission10);
            permissionsRepository.save(permission11);
            permissionsRepository.save(permission12);
            permissionsRepository.save(permission13);
            permissionsRepository.save(permission14);
            permissionsRepository.save(permission15);
        } catch (Exception e) {
        }

        // create a user
        User user = new User();
        user.setName("Florentino");
        user.setLastName("Souza");
        user.setEmail("florentino@bluevelvet.com");
        user.setPassword(new BCryptPasswordEncoder().encode("florentino123"));
        user.getRoles().add(role1);
        user.setStatus(true);

        // create a user
        User user2 = new User();
        user2.setName("Tiago");
        user2.setLastName("Souza");
        user2.setEmail("tiago@bluevelvet.com");
        user2.setPassword(new BCryptPasswordEncoder().encode("tiago123"));
        user2.getRoles().add(role5);
        user2.setStatus(true);

        try {
            userRepository.save(user);
            role1.getUsers().add(user);
            roleService.saveRole(role1);
        } catch (Exception e) {}

        try {
            userRepository.save(user2);
            role5.getUsers().add(user2);
            roleService.saveRole(role5);
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
