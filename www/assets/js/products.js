function populateTable(actions) {
    $.ajax({
        url: 'http://localhost:8090/api/v1/products',
        type: 'GET',
        success: function (response) {
            const products = response.data.map(product => ({
                id: product.id,
                image: `<img src="data:image/jpeg;charset=utf-8;base64,${product.image}" alt="${product.name}" style="width: 50px; height: auto;">`,
                name: product.name,
                brand: `<span class="badge badge-success mr-1">${product.brand.brandName}</span>`,
                categories: product.categories
                    .map(cat => `<span class="badge badge-primary mr-1">${cat.categoryName}</span>`)
                    .join(' '),
                actions: actions(product)
            }));

            $('#products').DataTable({
                data: products,
                columns: [
                    { data: 'id' },
                    { data: 'image' },
                    { data: 'name' },
                    { data: 'brand' },
                    { data: 'categories' },
                    { data: 'actions' }
                ]
            });

            $('#products').on('click', '.btn-view', function () {
                const productId = $(this).data('id');
                $('#viewError').hide();

                $.ajax({
                    url: `http://localhost:8090/api/v1/products/${productId}`,
                    type: 'GET',
                    success: function (response) {
                        const product = response.data;

                        $('#viewName').text(product.name);
                        $('#viewBrand').text(product.brand.brandName);
                        $('#viewShortDescription').text(product.shortDescription);
                        $('#viewLongDescription').text(product.longDescription);

                        const mainImageSrc = product.image
                            ? `data:image/jpeg;charset=utf-8;base64,${product.image}`
                            : 'https://via.assets.so/img.jpg?w=300&h=300&tc=gray&bg=#cecece&t=Image+not+found';
                        $('#mainImage').attr('src', mainImageSrc);

                        const carouselInner = $('#carouselInner');
                        carouselInner.empty();

                        if (product.photos && product.photos.length > 0) {
                            product.photos.forEach((photo, index) => {
                                carouselInner.append(`
                                    <div class="carousel-item ${index === 0 ? 'active' : ''}">
                                        <img src="data:image/jpeg;charset=utf-8;base64,${photo.image}" class="d-block w-100 rounded" alt="Photo ${index + 1}">
                                    </div>
                                `);
                            });
                        }

                        $('#tagStatus')
                            .text(product.status ? 'Active' : 'Inactive')
                            .removeClass('badge-danger badge-success')
                            .addClass(product.status ? 'badge-success' : 'badge-danger');

                        $('#tagStock')
                            .text(product.hasStock ? 'In Stock' : 'Out of Stock')
                            .removeClass('badge-danger badge-success')
                            .addClass(product.hasStock ? 'badge-success' : 'badge-danger');

                        $('#tagPrice').text(`$${product.price}`).addClass('badge-primary');

                        $('#viewWidth').text(product.width + ' in');
                        $('#viewHeight').text(product.height + ' in');
                        $('#viewLength').text(product.length + ' in');

                        $('#modalView').modal('show');
                    },
                    error: function () {
                        $('#viewError').text('Error: Failed to load product details').show();
                    }
                });
            });

            $('#products').on('click', '.btn-delete', function () {
                const productId = $(this).data('id');
                $('#deleteError').hide();
                $('#modalDelete').modal('show');

                $('#confirmDelete').off().on('click', function () {
                    $.ajax({
                        url: `http://localhost:8090/api/v1/products/${productId}`,
                        // AUTENTICAÇÃO
                        // beforeSend: function(request) {
                        //     request.setRequestHeader("Authorization", `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInJvbGVzIjpbMV0sImVtYWlsIjoidGVzdEBnbWFpbC5jb20iLCJleHAiOjE3MzE5MDIxNTZ9.SfQXzk97YUUlvSynSMC29WUsvK8CN2juNmTIAo8pRiI`);
                        // },
                        type: 'DELETE',
                        success: function () {
                            $('#modalDelete').modal('hide');
                            location.reload();
                        },
                        error: function () {
                            $('#deleteError').text('Error: Failed to delete product').show();
                        }
                    });
                });
            });
        }
    });
}

window.populateTable = populateTable;