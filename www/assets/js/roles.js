function populateTable(actions) {
    $.ajax({
        url: 'http://localhost:8090/api/v1/roles',
        type: 'GET',
        xhrFields: {
            withCredentials: true
        },
        success: function (response) {
            const roles = response.data.map(role => ({
                id: role.id,
                name: role.name,
                description: role.description,
                actions: actions(role)
            }));

            $('#dataTableContent').DataTable({
                data: roles,
                columns: [     
                    { data: 'id', responsivePriority: 3 },
                    { data: 'name', responsivePriority: 1 },
                    { data: 'description', responsivePriority: 2 },
                    { data: 'actions', responsivePriority: 4 }
                ],
                order: [[1, 'asc']],
                responsive: true,
                autoWidth: false
            });

           

            $('#dataTableContent').on('click', '.btn-delete', function () {
                const userId = $(this).data('id');
                $('#deleteError').hide();
                $('#modalDelete').modal('show');

                $('#confirmDelete').off().on('click', function () {
                    $.ajax({
                        url: `http://localhost:8090/api/v1/roles/${userId}`,
                        xhrFields: {
                            withCredentials: true
                        },
                        type: 'DELETE',
                        success: function () {
                            $('#modalDelete').modal('hide');
                            location.reload();
                        },
                        error: function () {
                            $('#deleteError').text('Error: Failed to delete role').show();
                        }
                    });
                });
            });
        }
    });
}

$(document).ready(function () {

    $('#dataTableContent').on('click', '.btn-view', function () {
        const roleId = $(this).data('id');
        $('#viewError').hide();

        $.ajax({
            url: `http://localhost:8090/api/v1/roles/${roleId}`,
            xhrFields: {
                withCredentials: true
            },
            type: 'GET',
            success: function (response) {
                const role = response.data;
                const rolePermissionList = document.getElementById('rolePermissionList');

                $('#viewName').text(role.name);
                $('#viewDescription').text(role.description);
                
                rolePermissionList.innerHTML = '';

                role.permissions.forEach((permission)=>{
                    rolePermissionList.innerHTML += `
                        <li class="list-group-item list-group-item-action">${permission.name}</li>
                    
                    `;
                });
                $('#modalView').modal('show');

            },
            error: function () {
                $('#viewError').text('Error: Failed to load role details').show();
            }
        });
    });
});
window.populateTable = populateTable;