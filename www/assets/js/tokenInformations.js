const jwt = document.cookie
    .split('; ')
    .find((row) => row.startsWith('jwt'))
    ?.split('=')[1];


if (jwt) {
    try {
        const decoded = jwt_decode(jwt);    
        const email = decoded.email || 'Email não disponível';
        const roleId = decoded.roles?.[0] || 'Role não disponível';

        const roleMap = {
            1: 'Admin',   
            2: 'Usuário'  
        };

        const roleName = roleMap[roleId] || `Role ID: ${roleId}`;
            
        const userInfo = document.getElementById('user-info');
        userInfo.textContent = `Logged in as: ${email} (Role: ${roleName})`;
    } catch (error) {
        console.error('Erro ao decodificar o token:', error);
    }
} else {
    console.error('JWT não encontrado!');
}

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