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
import java.util.List;

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

        Permissions permissionProductCreate = new Permissions();
        permissionProductCreate.setName("PERMISSION_PRODUCT_CREATE");
        permissionProductCreate.setDescription("Permission to create a product in BV");

        Permissions permissionProductEdit = new Permissions();
        permissionProductEdit.setName("PERMISSION_PRODUCT_EDIT");
        permissionProductEdit.setDescription("Permission to edit a product in BV");

        Permissions permissionProductDelete = new Permissions();
        permissionProductDelete.setName("PERMISSION_PRODUCT_DELETE");
        permissionProductDelete.setDescription("Permission to delete a product in BV");

        Permissions permissionProductView = new Permissions();
        permissionProductView.setName("PERMISSION_PRODUCT_VIEW");
        permissionProductView.setDescription("Permission to view a product in BV");

        Permissions permissionUserCreate = new Permissions();
        permissionUserCreate.setName("PERMISSION_USER_CREATE");
        permissionUserCreate.setDescription("Permission to create a user in BV");

        Permissions permissionUserEdit = new Permissions();
        permissionUserEdit.setName("PERMISSION_USER_EDIT");
        permissionUserEdit.setDescription("Permission to edit a user in BV");

        Permissions permissionUserDelete = new Permissions();
        permissionUserDelete.setName("PERMISSION_USER_DELETE");
        permissionUserDelete.setDescription("Permission to delete a user in BV");

        Permissions permissionUserView = new Permissions();
        permissionUserView.setName("PERMISSION_USER_VIEW");
        permissionUserView.setDescription("Permission to view a user in BV");

        Permissions permissionBrandCreate = new Permissions();
        permissionBrandCreate.setName("PERMISSION_BRAND_CREATE");
        permissionBrandCreate.setDescription("Permission to create a brand in BV");

        Permissions permissionBrandEdit = new Permissions();
        permissionBrandEdit.setName("PERMISSION_BRAND_EDIT");
        permissionBrandEdit.setDescription("Permission to edit a brand in BV");

        Permissions permissionBrandDelete = new Permissions();
        permissionBrandDelete.setName("PERMISSION_BRAND_DELETE");
        permissionBrandDelete.setDescription("Permission to delete a brand in BV");

        Permissions permissionBrandView = new Permissions();
        permissionBrandView.setName("PERMISSION_BRAND_VIEW");
        permissionBrandView.setDescription("Permission to view a brand in BV");

        Permissions permissionCategoryCreate = new Permissions();
        permissionCategoryCreate.setName("PERMISSION_CATEGORY_CREATE");
        permissionCategoryCreate.setDescription("Permission to create a category in BV");

        Permissions permissionCategoryEdit = new Permissions();
        permissionCategoryEdit.setName("PERMISSION_CATEGORY_EDIT");
        permissionCategoryEdit.setDescription("Permission to edit a category in BV");

        Permissions permissionCategoryDelete = new Permissions();
        permissionCategoryDelete.setName("PERMISSION_CATEGORY_DELETE");
        permissionCategoryDelete.setDescription("Permission to delete a category in BV");

        Permissions permissionCategoryView = new Permissions();
        permissionCategoryView.setName("PERMISSION_CATEGORY_VIEW");
        permissionCategoryView.setDescription("Permission to view a category in BV");

        Permissions permissionClientCreate = new Permissions();
        permissionClientCreate.setName("PERMISSION_CLIENT_CREATE");
        permissionClientCreate.setDescription("Permission to create a client in BV");

        Permissions permissionClientEdit = new Permissions();
        permissionClientEdit.setName("PERMISSION_CLIENT_EDIT");
        permissionClientEdit.setDescription("Permission to edit a client in BV");

        Permissions permissionClientDelete = new Permissions();
        permissionClientDelete.setName("PERMISSION_CLIENT_DELETE");
        permissionClientDelete.setDescription("Permission to delete a client in BV");

        Permissions permissionClientView = new Permissions();
        permissionClientView.setName("PERMISSION_CLIENT_VIEW");
        permissionClientView.setDescription("Permission to view a client in BV");

        Permissions permissionShippingView = new Permissions();
        permissionShippingView.setName("PERMISSION_SHIPPING_VIEW");
        permissionShippingView.setDescription("Permission to view a shipping in BV");

        Permissions permissionShippingUpdateStatus = new Permissions();
        permissionShippingUpdateStatus.setName("PERMISSION_SHIPPING_UPDATE_STATUS");
        permissionShippingUpdateStatus.setDescription("Permission to update a shipping status in BV");

        Permissions permissionOrderCreate = new Permissions();
        permissionOrderCreate.setName("PERMISSION_ORDER_CREATE");
        permissionOrderCreate.setDescription("Permission to create an order in BV");

        Permissions permissionOrderEdit = new Permissions();
        permissionOrderEdit.setName("PERMISSION_ORDER_EDIT");
        permissionOrderEdit.setDescription("Permission to edit an order in BV");

        Permissions permissionOrderDelete = new Permissions();
        permissionOrderDelete.setName("PERMISSION_ORDER_DELETE");
        permissionOrderDelete.setDescription("Permission to delete an order in BV");

        Permissions permissionOrderView = new Permissions();
        permissionOrderView.setName("PERMISSION_ORDER_VIEW");
        permissionOrderView.setDescription("Permission to view an order in BV");

        Permissions permissionQuestionView = new Permissions();
        permissionQuestionView.setName("PERMISSION_QUESTION_VIEW");
        permissionQuestionView.setDescription("Permission to view a question in BV");

        Permissions permissionQuestionAnswer = new Permissions();
        permissionQuestionAnswer.setName("PERMISSION_QUESTION_ANSWER");
        permissionQuestionAnswer.setDescription("Permission to answer a question in BV");

        Permissions permissionReviewView = new Permissions();
        permissionReviewView.setName("PERMISSION_REVIEW_VIEW");
        permissionReviewView.setDescription("Permission to view a review in BV");

        Permissions permissionReviewApprove = new Permissions();
        permissionReviewApprove.setName("PERMISSION_REVIEW_APPROVE");
        permissionReviewApprove.setDescription("Permission to approve a review in BV");

        Permissions permissionReviewDelete = new Permissions();
        permissionReviewDelete.setName("PERMISSION_REVIEW_DELETE");
        permissionReviewDelete.setDescription("Permission to delete a review in BV");

        Permissions permissionRoleCreate = new Permissions();
        permissionRoleCreate.setName("PERMISSION_ROLE_CREATE");
        permissionRoleCreate.setDescription("Permission to create a role in BV");

        Permissions permissionRoleEdit = new Permissions();
        permissionRoleEdit.setName("PERMISSION_ROLE_EDIT");
        permissionRoleEdit.setDescription("Permission to edit a role in BV");

        Permissions permissionRoleDelete = new Permissions();
        permissionRoleDelete.setName("PERMISSION_ROLE_DELETE");
        permissionRoleDelete.setDescription("Permission to delete a role in BV");

        Permissions permissionRoleView = new Permissions();
        permissionRoleView.setName("PERMISSION_ROLE_VIEW");
        permissionRoleView.setDescription("Permission to view a role in BV");

        Permissions permissionPermissionCreate = new Permissions();
        permissionPermissionCreate.setName("PERMISSION_PERMISSION_CREATE");
        permissionPermissionCreate.setDescription("Permission to create a permission in BV");

        Permissions permissionPermissionEdit = new Permissions();
        permissionPermissionEdit.setName("PERMISSION_PERMISSION_EDIT");
        permissionPermissionEdit.setDescription("Permission to edit a permission in BV");

        Permissions permissionPermissionDelete = new Permissions();
        permissionPermissionDelete.setName("PERMISSION_PERMISSION_DELETE");
        permissionPermissionDelete.setDescription("Permission to delete a permission in BV");

        Permissions permissionPermissionView = new Permissions();
        permissionPermissionView.setName("PERMISSION_PERMISSION_VIEW");
        permissionPermissionView.setDescription("Permission to view a permission in BV");

        List<Permissions> allPermissions = null;

        try {
            allPermissions = List.of(
                    permissionProductCreate, permissionProductEdit, permissionProductDelete, permissionProductView,
                    permissionUserCreate, permissionUserEdit, permissionUserDelete, permissionUserView,
                    permissionBrandCreate, permissionBrandEdit, permissionBrandDelete, permissionBrandView,
                    permissionCategoryCreate, permissionCategoryEdit, permissionCategoryDelete, permissionCategoryView,
                    permissionClientCreate, permissionClientEdit, permissionClientDelete, permissionClientView,
                    permissionShippingView, permissionShippingUpdateStatus,
                    permissionOrderCreate, permissionOrderEdit, permissionOrderDelete, permissionOrderView,
                    permissionQuestionView, permissionQuestionAnswer,
                    permissionReviewView, permissionReviewApprove, permissionReviewDelete,
                    permissionRoleCreate, permissionRoleEdit, permissionRoleDelete, permissionRoleView,
                    permissionPermissionCreate, permissionPermissionEdit, permissionPermissionDelete, permissionPermissionView
            );

            permissionsRepository.saveAll(allPermissions);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Role adminRole = new Role();
        if (allPermissions != null) {
            adminRole.setName("ADMIN");
            adminRole.setDescription("A role for administrator in BV");
            adminRole.getPermissions().addAll(allPermissions);
            allPermissions.forEach(permission -> permission.getRoles().add(adminRole));
        }

        Role salesManagerRole = new Role();
        salesManagerRole.setName("SALES_MANAGER");
        salesManagerRole.setDescription("A role for sales manager in BV");
        List<Permissions> salesManagerPermissions = List.of(
                permissionProductView, permissionClientCreate, permissionClientEdit,
                permissionClientView, permissionOrderCreate, permissionOrderEdit,
                permissionOrderView, permissionShippingView, permissionShippingUpdateStatus
        );
        salesManagerRole.getPermissions().addAll(salesManagerPermissions);
        salesManagerPermissions.forEach(permission -> permission.getRoles().add(salesManagerRole));

        Role editorRole = new Role();
        editorRole.setName("EDITOR");
        editorRole.setDescription("A role for editor in BV");
        List<Permissions> editorPermissions = List.of(
                permissionProductCreate, permissionProductEdit, permissionProductView,
                permissionCategoryCreate, permissionCategoryEdit, permissionCategoryView,
                permissionReviewView, permissionReviewApprove, permissionReviewDelete
        );
        editorRole.getPermissions().addAll(editorPermissions);
        editorPermissions.forEach(permission -> permission.getRoles().add(editorRole));

        Role shippingManagerRole = new Role();
        shippingManagerRole.setName("SHIPPING_MANAGER");
        shippingManagerRole.setDescription("A role for shipping manager in BV");
        List<Permissions> shippingManagerPermissions = List.of(
                permissionShippingView, permissionShippingUpdateStatus, permissionOrderView
        );
        shippingManagerRole.getPermissions().addAll(shippingManagerPermissions);
        shippingManagerPermissions.forEach(permission -> permission.getRoles().add(shippingManagerRole));

        Role assistantRole = new Role();
        assistantRole.setName("ASSISTANT");
        assistantRole.setDescription("A role for assistant in BV");
        List<Permissions> assistantPermissions = List.of(
                permissionCategoryView, permissionBrandView, permissionClientView,
                permissionOrderView, permissionQuestionView
        );
        assistantRole.getPermissions().addAll(assistantPermissions);
        assistantPermissions.forEach(permission -> permission.getRoles().add(assistantRole));

        try {
            roleRepository.saveAll(List.of(adminRole, salesManagerRole, editorRole, shippingManagerRole, assistantRole));


            for (Permissions permission : allPermissions) {
                permissionsRepository.save(permission);
            }
            salesManagerPermissions.forEach(permission -> permissionsRepository.save(permission));
            editorPermissions.forEach(permission -> permissionsRepository.save(permission));
            shippingManagerPermissions.forEach(permission -> permissionsRepository.save(permission));
            assistantPermissions.forEach(permission -> permissionsRepository.save(permission));
        } catch (Exception e) {
            e.printStackTrace();
        }


        User user = new User();
        user.setName("Florentino");
        user.setLastName("Souza");
        user.setEmail("florentino@bluevelvet.com");
        user.setPassword(new BCryptPasswordEncoder().encode("florentino123"));
        user.getRoles().add(adminRole);
        user.setStatus(true);

        // create a user
        User user2 = new User();
        user2.setName("Tiago");
        user2.setLastName("Souza");
        user2.setEmail("tiago@bluevelvet.com");
        user2.setPassword(new BCryptPasswordEncoder().encode("tiago123"));
        user2.getRoles().add(assistantRole);
        user2.setStatus(true);

        try {
            userRepository.save(user);
            adminRole.getUsers().add(user);
            roleService.saveRole(adminRole);
        } catch (Exception e) {}

        try {
            userRepository.save(user2);
            assistantRole.getUsers().add(user2);
            roleService.saveRole(assistantRole);
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
