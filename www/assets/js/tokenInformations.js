const jwt = document.cookie
    .split('; ')
    .find((row) => row.startsWith('jwt'))
    ?.split('=')[1];

console.log(document.cookie);


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
        userInfo.textContent = `Logado Como: ${email} (Role: ${roleName})`;
        } catch (error) {
            console.error('Erro ao decodificar o token:', error);
        }
    } else {
        console.error('JWT não encontrado!');
}