function populateTable(actions) {
    $.ajax({
        url: 'http://localhost:8090/api/v1/categories',
        type: 'GET',
        xhrFields: {
            withCredentials: true
        },
        success: function (response) {
            const categories = response.data.map(category => ({
                id: category.id,
                name: category.categoryName,
                image: category.image
                    ? `<img src="data:image/png;base64,${category.image}" alt="${category.categoryName}" style="width: 50px; height: 50px; object-fit: cover;">`
                    : '<span>No Image</span>',
                actions: actions(category)
            }));

            $('#dataTableContent').DataTable({
                data: categories,
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

            // Visualizar categoria
            $('#dataTableContent').on('click', '.btn-view', function () {
                const categoryId = $(this).data('id');
                $('#viewError').hide();

                $.ajax({
                    url: `http://localhost:8090/api/v1/categories/${categoryId}`,
                    type: 'GET',
                    xhrFields: { withCredentials: true },
                    success: function (response) {
                        const category = response.data;

                        $('#viewName').text(category.categoryName);
                        $('#viewDescription').text(category.description || 'No description available');

                        $('#modalView').modal('show');
                    },
                    error: function () {
                        $('#viewError').text('Error: Failed to load category details').show();
                    }
                });
            });

            // Deletar categoria
            $('#dataTableContent').on('click', '.btn-delete', function () {
                const categoryId = $(this).data('id');
                $('#deleteError').hide();
                $('#modalDelete').modal('show');

                $('#confirmDelete').off().on('click', function () {
                    $.ajax({
                        url: `http://localhost:8090/api/v1/categories/${categoryId}`,
                        xhrFields: { withCredentials: true },
                        type: 'DELETE',
                        success: function () {
                            $('#modalDelete').modal('hide');
                            location.reload();
                        },
                        error: function () {
                            $('#deleteError').text('Error: Failed to delete category').show();
                        }
                    });
                });
            });
        },
        error: function () {
            console.error('Failed to fetch categories');
        }
    });
}

// Lógica do Modal de Criação
$(document).ready(() => {
    // Abrir modal de criação
    $('#btnCreateCategory').on('click', function () {
        $('#createForm')[0].reset(); // Limpar formulário
        $('#createImagePreview img').hide(); // Esconder pré-visualização da imagem
        $('#modalCreate').modal('show');
    });

    // Pré-visualizar imagem no formulário de criação
    const previewImage = (file, containerId) => {
        if (!file) return;

        const reader = new FileReader();
        reader.onload = (e) => {
            $(`${containerId} img`).attr('src', e.target.result).show();
        };
        reader.readAsDataURL(file);
    };

    $('#createCategoryImage').on('change', function () {
        const file = this.files[0];
        previewImage(file, '#createImagePreview');
    });

    // Submeter formulário de criação
    $('#createForm').submit(function (event) {
        event.preventDefault();

        const formData = new FormData();
        formData.append('categoryName', $('#createCategoryName').val());
        const fileInput = $('#createCategoryImage')[0].files[0];
        if (fileInput) {
            formData.append('image', fileInput);
        }

        $.ajax({
            url: 'http://localhost:8090/api/v1/categories',
            type: 'POST',
            xhrFields: { withCredentials: true },
            processData: false, // Não processar o FormData
            contentType: false, // Configurar automaticamente o Content-Type
            data: formData,
            success: () => {
                $('#modalCreate').modal('hide');
                location.reload();
            },
            error: () => {
                $('#createError').text('Failed to create category').show();
            }
        });
    });
});

window.populateTable = populateTable;
