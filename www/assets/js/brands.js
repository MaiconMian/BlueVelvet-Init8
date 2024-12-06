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
                        $('#viewDescription').text(brand.description || 'No description available');

                        $('#modalView').modal('show');
                    },
                    error: function () {
                        $('#viewError').text('Error: Failed to load brand details').show();
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

        const formData = new FormData();
        formData.append('brandName', $('#createBrandName').val());
        const fileInput = $('#createBrandImage')[0].files[0];
        if (fileInput) {
            formData.append('image', fileInput);
        }

        $.ajax({
            url: 'http://localhost:8090/api/v1/brands', 
            type: 'POST',
            xhrFields: { withCredentials: true },
            processData: false, 
            contentType: false,
            data: formData,
            success: () => {
                $('#modalCreate').modal('hide'); 
                location.reload(); 
            },
            error: () => {
                $('#createError').text('Failed to create brand').show();
            }
        });
    });
});

window.populateTable = populateTable;
