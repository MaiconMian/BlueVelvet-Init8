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

const populateModal = (isEdit, brand = null) => {
    $('#editBrandId').val(isEdit ? brand.id : "");

    $('#editName')
        .val(isEdit ? brand.brandName : "")
        .attr('maxlength', 100)
        .on('input', function () {
            const currentLength = $(this).val().length;
            $('#nameCounter').text(`${currentLength}/100`);
        });
    updateCounter('editName', 'nameCounter', 100, isEdit);

    $('#mainImagePreview').empty();

    if (isEdit && brand.image) {
        const mainImageContainer = $(`
            <div class="position-relative">
                <img src="data:image/png;base64,${brand.image}" class="img-thumbnail w-100" alt="Main Image Preview">
                <button id="deleteImageBtn" type="button" class="btn btn-danger btn-sm position-absolute top-0 end-0 p-1 delete-btn" aria-label="Remove Image">
                    &times;
                </button>
            </div>
        `);
        $('#mainImagePreview').append(mainImageContainer);

        mainImageContainer.find('#deleteImageBtn').on('click', () => {
            $('#mainImagePreview').empty();
        });
    }

    fetchCategories().then(([categories]) => {
        const categoriesSelect = $('#editCategories').empty();
        categories.data.forEach((cat) => {
            categoriesSelect.append(new Option(cat.categoryName, cat.id));
        });
    });
};

const updateCounter = (fieldId, counterId, maxLength, isEdit) => {
    if (isEdit) {
        const currentLength = $(`#${fieldId}`).val().length;
        $(`#${counterId}`).text(`${currentLength}/${maxLength}`);
    } else {
        $(`#${counterId}`).text(`0/${maxLength}`);
    }
};

function fetchCategories() {
    return Promise.all([
        $.get('http://localhost:8090/api/v1/categories')
    ]);
}

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

                        const categoriesView = $('#viewCategories').empty();
                        if(brand.category && brand.category.length > 0){
                            brand.category.forEach(category => {
                                const badge = $(`<span class="badge badge-primary mr-2 mb-2">${category.categoryName}</span>`);
                                categoriesView.append(badge);
                            });
                        } else {
                            categoriesView.html('<span class="badge badge-danger mr-2 mb-2">No categories</span>');
                        }

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
                const brandId = $(this).data('id');
                $('#modalEdit').modal('show');

                $.ajax({
                    url: `http://localhost:8090/api/v1/brands/${brandId}`,
                    type: 'GET',
                    xhrFields: { withCredentials: true },
                    success: function (response) {
                        const brand = response.data;
                        populateModal(true, brand);
                        $('#modalEdit').modal('show');
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
                        xhrFields: { 
                            withCredentials: true 
                        },
                        type: 'DELETE',
                        success: function () {
                            $('#modalDelete').modal('hide');
                            location.reload();
                        },
                        error: function (e) {
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
    const previewImage = (file, containerId) => {
        if (!file) return;

        const reader = new FileReader();
        reader.onload = (e) => {
            const imgContainer = $(`
                <div class="position-relative">
                    <img src="${e.target.result}" class="img-thumbnail w-100" alt="Main Image Preview">
                    <button id="deleteImageBtn" type="button" class="btn btn-sm btn-danger position-absolute delete-btn" aria-label="Remove Image">
                        &times;
                    </button>
                </div>
            `);
            $(containerId).html(imgContainer);

            imgContainer.find('#deleteImageBtn').on('click', () => {
                $(containerId).html('<p>No image selected</p>');
            });
        };
        reader.readAsDataURL(file);
    };

    function showSuccessMessage(message) {
        const successAlert = $(`
            <div class="alert alert-success" role="alert" style="position: fixed; top: 20px; right: 20px; z-index: 1050;">
                ${message}
            </div>
        `);
        $('body').append(successAlert);
        setTimeout(() => {
            successAlert.fadeOut(() => successAlert.remove());
            location.reload();
        }, 1000);
    }

    $('#editImage').on('change', function () {
        const file = this.files[0];
        previewImage(file, '#mainImagePreview');
    });

    const handleSubmit = (isEdit) => {
        const brandId = $('#editBrandId').val();

        const selectedCategories = [];
        $('#editCategories option:selected').each(function () {
            selectedCategories.push(parseInt($(this).val())); 
        });

        const data = {
            brandName: $('#editName').val(),
            category: selectedCategories
        };

        const mainImageElement = $('#mainImagePreview img');
        if (mainImageElement.length) {
            data.image = mainImageElement.attr('src').split(',')[1];
        }

        const submitData = () => {
            const baseUrl = "http://localhost:8090/api/v1"
            const url = isEdit ? `${baseUrl}/brands/${brandId}` : `${baseUrl}/brands`;
            const method = isEdit ? "PUT" : "POST";

            $.ajax({
                url,
                method,
                xhrFields: {
                    withCredentials: true
                },
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: () => {
                    showSuccessMessage(`Brand ${isEdit ? 'updated' : 'created'} successfully!`);
                    $('#modalEdit').modal('hide');
                },
                error: () => {
                    $('#editError').text(`Failed to ${isEdit ? 'update' : 'create'} brand`).show();
                },
            });
        };

        submitData();
    };

    $('#btnCreateBrand').on('click', () => {
        populateModal(false);
        $('#modalEdit').modal('show');
    });

    $('#editForm').submit(function (event) {
        event.preventDefault();
        const isEdit = !!$('#editBrandId').val();
        if (!isEdit && !$('#mainImagePreview img').length) {
            $('#editError').text('Please provide a main image for the brand.').show();
            return;
        }
        handleSubmit(isEdit);
    });
});

window.populateTable = populateTable;
