document.addEventListener("DOMContentLoaded", () => {
    const endpoint = "http://localhost:8090/api/v1/auth/me";

    const userImage = document.getElementById("user-image");
    const dropdownUserImage = document.getElementById("dropdown-user-image");
    const userName = document.getElementById("user-name");
    const userEmail = document.getElementById("user-email");

    function base64ToImage(base64String) {
        return `data:image/png;base64,${base64String}`;
    }

    async function fetchUserData() {
        try {
            const response = await fetch(endpoint, {
                method: "GET",
                credentials: "include", 
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                throw new Error("Erro ao buscar os dados do usuário");
            }
            const data = await response.json();

            if (data?.data) {
                const { name, lastName, email, image } = data.data;

                userName.textContent = `${name} ${lastName}`;
                userEmail.textContent = email;

                const imageUrl = image ? base64ToImage(image) : "../assets/images/user-placeholder.png";
                userImage.src = imageUrl;
                dropdownUserImage.src = imageUrl;
            }
        } catch (error) {
            console.error("Erro ao carregar os dados do usuário:", error);
        }
    }

    fetchUserData();

});
document.getElementById('logout-button').addEventListener('click', async function(event) {
    event.preventDefault(); 

    try {
        const response = await fetch('http://localhost:8090/api/v1/auth/logout', {
            method: 'POST', 
            credentials: 'include'
        });

        if (response.ok) {
            console.log('Logout realizado com sucesso');
            window.location.href = '/login.html'; 
        } else {
            console.error('Erro ao fazer logout:', response.statusText);
        }
    } catch (error) {
        console.error('Erro ao enviar requisição de logout:', error);
    }
});