$(document).ready(() => {
    populateTable((brand) => `
        <button type="button" class="btn btn-primary btn-view" data-id="${brand.id}">
            <i class="fa fa-eye"></i>
        </button>
        <button type="button" class="btn btn-success btn-edit" data-id="${brand.id}">
            <i class="fa fa-pencil-square-o"></i>
        </button>
        <button type="button" class="btn btn-danger btn-delete" data-id="${brand.id}">
            <i class="fa fa-trash"></i>
        </button>
    `);
});

function populateTable(actions) {
    $.ajax({
        url: 'http://localhost:8090/api/v1/brands',
        type: 'GET',
        xhrFields: {
            withCredentials: true
        },
        success: function (response) {
            const brands = response.data.map(brand => ({
                id: brand.id,
                name: brand.brandName,
                image: brand.image
                    ? `<img src="data:image/png;base64,${brand.image}" alt="${brand.brandName}" style="width: 50px; height: 50px; object-fit: cover;">`
                    : '<span>No Image</span>',
                actions: actions(brand)
            }));

            $('#dataTableContent').DataTable({
                data: brands,
                columns: [
                    { data: 'id', responsivePriority: 3 },
                    { data: 'image', responsivePriority: 4 },
                    { data: 'name', responsivePriority: 1 },
                    { data: 'actions', responsivePriority: 2 }
                ],
                order: [[1, 'asc']],
                responsive: true,
                autoWidth: false
            });

            $('#dataTableContent').on('click', '.btn-view', function () {
                const brandId = $(this).data('id');
                $('#viewError').hide();

                $.ajax({
                    url: `http://localhost:8090/api/v1/brands/${brandId}`,
                    type: 'GET',
                    xhrFields: { withCredentials: true },
                    success: function (response) {
                        const brand = response.data;

                        $('#viewName').text(brand.brandName);
                        $('#viewCategories').text(brand.category);
                        const mainImageSrc = brand.image
                            ? `data:image/jpeg;charset=utf-8;base64,${brand.image}`
                            : 'https://via.assets.so/img.jpg?w=300&h=300&tc=gray&bg=#cecece&t=Image+not+found';
                        $('#mainImage').attr('src', mainImageSrc);

                        $('#modalView').modal('show');
                    },
                    error: function () {
                        $('#viewError').text('Error: Failed to load brand details').show();
                    }
                });
            });

            $('#dataTableContent').on('click', '.btn-edit', function () {
                console.log('oi')
                const brandId = $(this).data('id');
                $('#modalEdit').modal('show');

                $.ajax({
                    url: `http://localhost:8090/api/v1/brands/${brandId}`,
                    type: 'GET',
                    xhrFields: { withCredentials: true },
                    success: function (response) {
                        const brand = response.data;

                        $("#editBrandId").val(brand.id);
                        $('#editBrandName').val(brand.brandName);

                    },
                    error: function () {
                        $('#editError').text('Error: Failed to load category details').show();
                    }
                });
            });

            $('#dataTableContent').on('click', '.btn-delete', function () {
                const brandId = $(this).data('id');
                $('#deleteError').hide();
                $('#modalDelete').modal('show');

                $('#confirmDelete').off().on('click', function () {
                    $.ajax({
                        url: `http://localhost:8090/api/v1/brands/${brandId}`,
                        xhrFields: { withCredentials: true },
                        type: 'DELETE',
                        success: function () {
                            $('#modalDelete').modal('hide');
                            location.reload();
                        },
                        error: function () {
                            $('#deleteError').text('Error: Failed to delete brand').show();
                        }
                    });
                });
            });
        },
        error: function () {
            console.error('Failed to fetch brands');
        }
    });
}

$(document).ready(() => {
    $('#btnCreateBrand').on('click', function () {
        $('#createForm')[0].reset();
        $('#createImagePreview img').hide(); 
        $('#modalCreate').modal('show'); 
    });

    $.ajax({
        url: "http://localhost:8090/api/v1/categories",
        method: "GET",
        
        success: (response) => {
            console.log(response.data);



            const categorySelects = [
                document.getElementById('editCategoryBrands'),
                document.getElementById('createCategoryBrands')
            ];
    
            categorySelects.forEach(categorySelect => {
                if (categorySelect) {
                    response.data.forEach((category)=>{
                        categorySelect.append(new Option(category.categoryName, category.id));
                    });
                } 
            });
            
            
        },
        error: (e) => {
            console.log(e);
            $('#editError').text(`Failed to create user`).show();
        },
    });

    const previewImage = (file, containerId) => {
        if (!file) return;

        const reader = new FileReader();
        reader.onload = (e) => {
            $(`${containerId} img`).attr('src', e.target.result).show();
        };
        reader.readAsDataURL(file);
    };

    $('#createBrandImage').on('change', function () {
        const file = this.files[0];
        previewImage(file, '#createImagePreview');
    });

    $('#createForm').submit(function (event) {
        event.preventDefault();

        const selectedCategories = [];
        $('#createCategoryBrands option:selected').each(function () {
            selectedCategories.push($(this).val()); 
        });

        const data = {
            brandName: $('#createBrandName').val(),
            category: selectedCategories,
        };
        console.log(data);

        const mainImageFile = $('#createBrandImage')[0].files[0];

        const convertImageToBase64 = (file, callback) => {
            const reader = new FileReader();
            reader.onload = () => callback(reader.result.split(',')[1]);
            reader.readAsDataURL(file);
        };

        const submitData = () => {
            const baseUrl = "http://localhost:8090/api/v1"
            const url =  `${baseUrl}/brands`;
            const method = "POST";

            $.ajax({
                url,
                method,
                xhrFields: {
                    withCredentials: true
                },
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: () => {
                    $('#modalCreate').modal('hide');
                    location.reload();
                },
                error: (e) => {
                    console.log(e);
                    $('#editError').text("Failed to create brand").show();
                },
            });
        };

        if (mainImageFile) {
            convertImageToBase64(mainImageFile, mainImageBase64 => {
                data.image = mainImageBase64;
                submitData();
            });
        }
    });

    $('#editForm').submit(function (event) {
        event.preventDefault();
        const id = $('#editBrandId').val();

        const selectedCategories = [];
        $('#editCategoryBrands option:selected').each(function () {
            selectedCategories.push($(this).val()); 
        });


        const data = {
            brandName: $('#editBrandName').val(),
            category: $('#editCategoryBrands .badges').map((_, el) => parseInt($(el).data('id'))).get(),
        };
        console.log(data);

        const mainImageFile = $('#editBrandImage')[0].files[0];

        const convertImageToBase64 = (file, callback) => {
            const reader = new FileReader();
            reader.onload = () => callback(reader.result.split(',')[1]);
            reader.readAsDataURL(file);
        };

        const submitData = () => {
            const baseUrl = "http://localhost:8090/api/v1"
            const url =  `${baseUrl}/brands/${id}`;
            const method = "PUT";

            $.ajax({
                url,
                method,
                xhrFields: {
                    withCredentials: true
                },
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: () => {
                    $('#modalEdit').modal('hide');
                    location.reload();
                },
                error: (e) => {
                    console.log(e);
                    $('#editError').text("Failed to create brand").show();
                },
            });
        };

        if (mainImageFile) {
            convertImageToBase64(mainImageFile, mainImageBase64 => {
                data.image = mainImageBase64;
                submitData();
            });
        }
    });
});

window.populateTable = populateTable;
