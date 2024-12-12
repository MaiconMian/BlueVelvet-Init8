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
                image: `<img src="data:image/jpeg;charset=utf-8;base64,${category.image}" alt="${category.categoryName}" style="width: 50px; height: auto;">`,
                actions: actions(category)
            }));

            $('#dataTableContent').DataTable({
                data: categories,
                columns: [
                    { data: 'id', responsivePriority: 4 },
                    { data: 'image', responsivePriority: 2 },
                    { data: 'name', responsivePriority: 1 },
                    { data: 'actions', responsivePriority: 3 }
                ],
                order: [[1, 'asc']],
                responsive: true,
                autoWidth: false
            });

            $('#dataTableContent').on('click', '.btn-view', function () {
                const categoryId = $(this).data('id');
                $('#viewError').hide();

                $.ajax({
                    url: `http://localhost:8090/api/v1/categories/${categoryId}`,
                    type: 'GET',
                    xhrFields: { 
                        withCredentials: true 
                    },
                    success: function (response) {
                        const category = response.data;
            
                        $('#viewName').text(category.categoryName);
            
                        const mainImageSrc = category.image
                            ? `data:image/jpeg;charset=utf-8;base64,${category.image}`
                            : 'https://via.placeholder.com/300?text=Image+not+found';
                        $('#mainImage').attr('src', mainImageSrc);
                        
                        $('#modalView').modal('show');
                    },
                    error: function () {
                        $('#viewError').text('Error: Failed to load category details').show();
                    }
                });
            });

            $('#dataTableContent').on('click', '.btn-delete', function () {
                const categoryId = $(this).data('id');
                $('#deleteError').hide();
                $('#modalDelete').modal('show');

                $('#confirmDelete').off().on('click', function () {
                    $.ajax({
                        url: `http://localhost:8090/api/v1/categories/${categoryId}`,
                        xhrFields: { 
                            withCredentials: true 
                        },
                        type: 'DELETE',
                        success: function () {
                            $('#modalDelete').modal('hide');
                            location.reload();
                        },
                        error: function (e) {
                            $('#deleteError').text('Error: Failed to delete category').show();
                        }
                    });
                });
            });
        }
    });
}

window.populateTable = populateTable;