function populateTable(actions) {
    $.ajax({
        url: 'http://localhost:8090/api/v1/users',
        type: 'GET',
        xhrFields: {
            withCredentials: true
        },
        success: function (response) {
            const users = response.data.map(user => ({
                id: user.id,
                image: `<img src="data:image/jpeg;charset=utf-8;base64,${user.image}" alt="${user.name}" style="width: 50px; height: auto;">`,
                name: `${user.name} ${user.lastName}`,
                roles: user.roles
                    .map(rol => `<span class="badge badge-primary mr-1">${rol.name}</span>`)
                    .join(' '),
                actions: actions(user)
            }));

            $('#dataTableContent').DataTable({
                data: users,
                columns: [     
                    { data: 'id', responsivePriority: 4 },
                    { data: 'image', responsivePriority: 1 },
                    { data: 'name', responsivePriority: 2 },
                    { data: 'roles', responsivePriority: 3 },
                    { data: 'actions', responsivePriority: 5 }
                ],
                order: [[2, 'asc']],
                responsive: true,
                autoWidth: false
            });

            $('#dataTableContent').on('click', '.btn-view', function () {
                const userId = $(this).data('id');
                $('#viewError').hide();

                $.ajax({
                    url: `http://localhost:8090/api/v1/users/${userId}`,
                    type: 'GET',
                    xhrFields: {
                        withCredentials: true
                    },
                    success: function (response) {
                        const user = response.data;

                        $('#viewName').text(`${user.name} ${user.lastName}`);
                        $('#viewEmail').text(user.email);

                        const mainImageSrc = user.image
                            ? `data:image/jpeg;charset=utf-8;base64,${user.image}`
                            : 'https://via.assets.so/img.jpg?w=300&h=300&tc=gray&bg=#cecece&t=Image+not+found';
                        $('#mainImage').attr('src', mainImageSrc);

                        $('#tagStatus')
                            .text(user.status ? 'Active' : 'Inactive')
                            .removeClass('badge-danger badge-success')
                            .addClass(user.status ? 'badge-success' : 'badge-danger');

                        $('#modalView').modal('show');
                    },
                    error: function () {
                        $('#viewError').text('Error: Failed to load product details').show();
                    }
                });
            });

            $('#dataTableContent').on('click', '.btn-delete', function () {
                const userId = $(this).data('id');
                $('#deleteError').hide();
                $('#modalDelete').modal('show');

                $('#confirmDelete').off().on('click', function () {
                    $.ajax({
                        url: `http://localhost:8090/api/v1/users/${userId}`,
                        xhrFields: {
                            withCredentials: true
                        },
                        type: 'DELETE',
                        success: function () {
                            $('#modalDelete').modal('hide');
                            location.reload();
                        },
                        error: function () {
                            $('#deleteError').text('Error: Failed to delete User').show();
                        }
                    });
                });
            });
        }
    });
}

window.populateTable = populateTable;