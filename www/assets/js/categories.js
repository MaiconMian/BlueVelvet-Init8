function fetchBrands() {
    return Promise.all([
        $.get('http://localhost:8090/api/v1/brands')
    ]);
}

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

function populateModal(){

}

function populateBrandFields(){
   
}

$(document).ready(() => {

    populateTable((category) => `
    <button type="button" class="btn btn-primary btn-view" data-id="${category.id}">
        <i class="fa fa-eye"></i>
    </button>
    <button type="button" id="btnEditCategory"class="btn btn-success btn-edit" data-id="${category.id}">
        <i class="fa fa-pencil-square-o"></i>
    </button>
    <button type="button" class="btn btn-danger btn-delete" data-id="${category.id}">
        <i class="fa fa-trash"></i>
    </button>
    `);
    // Abrir modal de criação
    $('#btnCreateCategory').on('click', function () {
        $('#createForm')[0].reset(); // Limpar o formulário
        $('#createImagePreview img').hide(); // Esconder pré-visualização da imagem
        $('#modalCreate').modal('show'); // Mostrar modal
    });

    $.ajax({
        url: "http://localhost:8090/api/v1/brands",
        method: "GET",
        
        success: (response) => {
            console.log(response.data);
            const brandSelect = document.getElementById('categoryBrands');
            brandSelect.append(new Option("Select a brand", ""));

            response.data.forEach((brand)=>{
                brandSelect.append(new Option(brand.brandName, brand.id));
            });
        },
        error: (e) => {
            console.log(e);
            $('#editError').text(`Failed to create user`).show();
        },
    });

    $('.btnEdit').on('click', function() {
        console.log('Button clicked!');
    });

    // Pré-visualizar imagem antes do envio
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

    // Submeter o formulário de criação
    $('#createForm').submit(function (event) {
        event.preventDefault();

        const data = {
            categoryName: $('#createCategoryName').val(),
        };

        const mainImageFile = $('#createCategoryImage')[0].files[0];

        const convertImageToBase64 = (file, callback) => {
            const reader = new FileReader();
            reader.onload = () => callback(reader.result.split(',')[1]);
            reader.readAsDataURL(file);
        };

        const submitData = () => {
            const baseUrl = "http://localhost:8090/api/v1"
            const url =  `${baseUrl}/categories`;
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
                    $('#editError').text(`Failed to create user`).show();
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