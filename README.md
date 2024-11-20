<p align="center">
  <img src="https://github.com/MaiconMian/BlueVelvet-Init8/blob/main/pictures/LogoBlueVelvetTransparent.png" width="500" />
</p>

<h1 align="center" style="font-weight: bold;"> Blue Velvet Music Store 🎸</h1>
Repositório oficial da equipe Init8_ para o desenvolvimento do projeto "Blue Velvet Music Store", criado como parte da disciplina de Gestão do Ciclo de Vida da Aplicação.
<br><br>
<div align="center">
  <img src="https://img.shields.io/badge/javascript-000?style=for-the-badge&logo=javascript" alt="javascript" />
  <img src="https://img.shields.io/badge/spring-%2390FF00.svg?style=for-the-badge&logo=spring&logoColor=white" alt="spring" />
   <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="java" />
  <img src="https://img.shields.io/badge/html-1E2A47.svg?style=for-the-badge&logo=html5&logoColor=white" alt="html" />
  <img src="https://img.shields.io/badge/bootstrap-5D1A94.svg?style=for-the-badge&logo=bootstrap&logoColor=white" alt="bootstrap" />
  <img src="https://img.shields.io/badge/docker-0079B9.svg?style=for-the-badge&logo=docker&logoColor=white" alt="docker" />
  <img src="https://img.shields.io/badge/jquery-FC2883.svg?style=for-the-badge&logo=jquery&logoColor=white" alt="jquery" />
</div>


# Requisitos
Para a utilização completa da aplicação, é necessário ter instalado:
- Java 17 
- [docker-compose](https://docs.docker.com/compose/install/)
- [Maven](https://maven.apache.org/install.html)

# Inicialização

Para iniciar a aplicação, é necessário clonar o repositório:
```bash
git clone https://github.com/MaiconMian/BlueVelvet-Init8
cd BlueVelvet-Init8

```
Após isso, basta rodar o script responsável por compilar a aplicação e subir os contâiners, verifique previamente se o docker foi iniciado:

### Linux
```bash
./run.sh
```

### Windows
```bash
.\run.ps1
```

Caso seja de interesse, é possível customizar a execução da aplicação com variáveis de ambiente que estão presentes no arquivo **.env**:

```
MYSQL_DATABASE=${MYSQL_DATABASE:-mydatabase}        # Nome da DB
MYSQL_PASSWORD=${MYSQL_PASSWORD:-rootpassword}      # Senha do usuário root da DB
DB_PORT=${DB_PORT:-3309}                            # Porta do MySQL
SPRING_PORT=${SPRING_PORT:-8090}                    # Porta do Spring (hosteando back-end)
NGINX_PORT=${NGINX_PORT:-8080}                      # Porta do Nginx (hosteando front-end)
```


# Utilização

Com os contâiners em execução, é possível acessar o servidor Spring Boot em `http://localhost:$SPRING_PORT`, (por padrão, `http://localhost:8090`)

### Tela Inicial

O servidor Nginx que hospeda a aplicação está disponível no endereço `http://localhost:$NGINX_PORT` (por padrão, `http://localhost:8080`). Ao acessar esse endereço, o navegador redireciona automaticamente para a tela inicial da aplicação, onde apenas os produtos da loja são exibidos, com responsividade para telas menores, sem funcionalidades adicionais disponíveis.

![TELAINICIAL](https://github.com/MaiconMian/BlueVelvet-Init8/blob/main/pictures/InitialScreen.png)

Para acessar o dashboard do administrador, é necessário utilizar o botão de login localizado no canto superior direito da tela, que redireciona para a página de autenticação.

![LOGIN](https://github.com/MaiconMian/BlueVelvet-Init8/blob/main/pictures/LoginPage.png)

Para testes, o usuário padrão de administrador é:
```bash
  email: florentino@bluevelvet.com
  senha: florentino123
```

Após isso, a dashbord do administrador é mostrada, onde é possivel adicionar e remover produtos. É importante ressaltar que se tentar acessar a página sem autenticação (`http://localhost:8080/admin.html`, por exemplo) o usuário é bloqueado e levado a pagina de login novamente.

<p align="center">
  <img src="https://github.com/MaiconMian/BlueVelvet-Init8/blob/main/pictures/AdminDashbord.png" width="730" style="display: inline-block;"/>
  <img src="https://github.com/MaiconMian/BlueVelvet-Init8/blob/main/pictures/Responsive.png" width="175" style="display: inline-block;"/>
</p>

É possível ver as informações do administrador logado clicando no ícone no canto superior direito.

# Modelagem
Para representarmos o domínio da BlueVelvet, fizemos a seguinte modelagem no banco de dados:

![DER](https://github.com/MaiconMian/BlueVelvet-Init8/blob/main/pictures/DER.png)

Que gerou as seguintes classes e relações no JPA:

<p align="center">
  <img src="https://github.com/MaiconMian/BlueVelvet-Init8/blob/main/pictures/MER.png" width="600" />
</p>


# Equipe

<p align="center">
  <img src="https://github.com/MaiconMian/BlueVelvet-Init8/blob/main/pictures/TeamLogo.png" width="500" />
</p>


<p align="center">
  A equipe da Init8_ é formada por 8 estudantes da Universidade Federal de Alfenas:
</p>


<div align="center">
  <table>
    <tr>
      <td align="center">
        <a href="#">
          <img src="https://avatars.githubusercontent.com/u/183776951?v=4" width="40px;" alt="Caio Henrique Foto"/><br>
          <sub>
            <b><a href="https://github.com/caiohmatos" target="_blank"><small>Caio H</small></a></b>
          </sub>
        </a>
      </td>
      <td align="center">
        <a href="#">
          <img src="https://avatars.githubusercontent.com/u/97255740?v=4" width="40px;" alt="Felipe Correia Foto"/><br>
          <sub>
            <b><a href="https://github.com/FelipeACorreia" target="_blank"><small>Felipe C</small></a></b>
          </sub>
        </a>
      </td>
      <td align="center">
        <a href="#">
          <img src="https://avatars.githubusercontent.com/u/166859556?v=4" width="40px;" alt="Gustavo Rodrigo Foto"/><br>
          <sub>
            <b><a href="https://github.com/GRodrigues34" target="_blank"><small>Gustavo R</small></a></b>
          </sub>
        </a>
      </td>
      <td align="center">
        <a href="#">
          <img src="https://avatars.githubusercontent.com/u/139499789?v=4" width="40px;" alt="Nycole Paulino Foto"/><br>
          <sub>
            <b><a href="https://github.com/LeonardoIX" target="_blank"><small>Leonardo A</small></a></b>
          </sub>
        </a>
      </td>
      <td align="center">
        <a href="#">
          <img src="https://avatars.githubusercontent.com/u/79428758?v=4" width="40px;" alt="Leticia Freitas Foto"/><br>
          <sub>
            <b><a href="https://github.com/leticiafreit" target="_blank"><small>Leticia F</small></a></b>
          </sub>
        </a>
      </td>
      <td align="center">
        <a href="#">
          <img src="https://avatars.githubusercontent.com/u/137798623?v=4" width="40px;" alt="Maicon Mian Foto"/><br>
          <sub>
            <b><a href="https://github.com/MaiconMian" target="_blank"><small>Maicon M</small></a></b>
          </sub>
        </a>
      </td>
      <td align="center">
        <a href="#">
          <img src="https://avatars.githubusercontent.com/u/153788630?v=4" width="40px;" alt="Nycole Paulino Foto"/><br>
          <sub>
            <b><a href="https://github.com/NycolePaulino" target="_blank"><small>Nycole P</small></a></b>
          </sub>
        </a>
      </td>
      <td align="center">
        <a href="#">
          <img src="https://avatars.githubusercontent.com/u/152667761?v=4" width="40px;" alt="Pedro Botelho Foto"/><br>
          <sub>
            <b><a href="https://github.com/0xbhsu" target="_blank"><small>Pedro B</small></a></b>
          </sub>
        </a>
      </td>
    </tr>
  </table>
</div>




