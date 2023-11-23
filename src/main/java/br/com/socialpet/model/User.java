package br.com.socialpet.model;

import br.com.socialpet.dao.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/user")
public class User extends HttpServlet {

    private String email;
    private String password;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        email = request.getParameter("login");
        password = request.getParameter("password");

        boolean credentialsValid = checkCredentials(email, password);

        if (credentialsValid) {
            System.out.println("Credenciais válidas para Email: " + email + " e Senha: " + password);

            // Obter dados do usuário
            DadosUsuario userData = getDadosUsuario(email, password);

            // Armazenar dados do usuário no request
            request.setAttribute("userData", userData);

            // Encaminhar para a página user.jsp
            request.getRequestDispatcher("pages/user.jsp").forward(request, response);
        } else {
            System.out.println("Credenciais inválidas para Email: " + email + " e Senha: " + password);
            // Adicionar mensagem de erro (opcional)
            request.setAttribute("error", "Credenciais inválidas. Tente novamente.");
            // Encaminhar de volta para a página de login (ou exibir a mensagem de erro na mesma página)
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private DadosUsuario getDadosUsuario(String email, String password) {
        // Modifique sua consulta SQL para recuperar os dados necessários
        String query = "SELECT id, nome, data_nascimento, email, logradouro, cidade, cep, estado FROM cadastrar WHERE email = ? AND senha = ?";
        DadosUsuario userData = new DadosUsuario();

        String url = "jdbc:mysql://bk1jwhvz2w0bcspk7qld-mysql.services.clever-cloud.com:3306/bk1jwhvz2w0bcspk7qld";
        String usuario = "uvmexunuh952emqc";
        String senha = "ahTkmHGBRFHkidXgPXz3";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Preencher o objeto UserData com os dados do usuário
                    userData.setNome(resultSet.getString("nome"));
                    userData.setDataNascimento(resultSet.getString("data_nascimento"));
                    userData.setEmail(resultSet.getString("email"));
                    userData.setLogradouro(resultSet.getString("logradouro"));
                    userData.setCidade(resultSet.getString("cidade"));
                    userData.setCep(resultSet.getString("cep"));
                    userData.setEstado(resultSet.getString("estado"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userData;
    }

    private boolean checkCredentials(String email, String password) {
        String query = "SELECT senha FROM cadastrar WHERE email = ? AND senha = ?";
        boolean credentialsValid = false;

        String url = "jdbc:mysql://bk1jwhvz2w0bcspk7qld-mysql.services.clever-cloud.com:3306/bk1jwhvz2w0bcspk7qld";
        String usuario = "uvmexunuh952emqc";
        String senha = "ahTkmHGBRFHkidXgPXz3";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                credentialsValid = resultSet.next(); // Verifica se há pelo menos uma linha correspondente
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return credentialsValid;
    }
}
