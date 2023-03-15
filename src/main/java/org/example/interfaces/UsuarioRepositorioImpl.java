package org.example.interfaces;

import org.example.model.Usuario;
import org.example.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorioImpl implements UsuarioRepo<Usuario> {

    private Connection getConnection() throws SQLException {

        return ConexionBD.getInstance();
    }

    @Override
    public List listar() {

        List<Usuario> usuarios = new ArrayList<>();

        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery("select * from usuarios")) {

            while (resultSet.next()) {

                Usuario usuario = getUsuario(resultSet);
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    @Override
    public Usuario porId(int codigo) {

        Usuario usuario = null;

        try (PreparedStatement preparedStatement = getConnection()
                .prepareStatement("select * from usuarios where id_usuarios = ?")) {

            preparedStatement.setInt(1, codigo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    usuario = getUsuario(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;

    }

    @Override
    public void guardar(Usuario usuario) {

        String sql;

        if (usuario.getCodigo() != null && usuario.getCodigo() > 0) {
            sql = "update usuarios set username = ?,password = ?, email = ? where id_usuarios = ?";
        } else {

            sql = "insert into usuarios(username,password,email) values (?,?,?)";
        }

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {

            preparedStatement.setString(1, usuario.getUsername());
            preparedStatement.setString(2, usuario.getPassword());
            preparedStatement.setString(3, usuario.getEmail());

            if (usuario.getCodigo() != null && usuario.getCodigo() > 0) {

                preparedStatement.setInt(4, usuario.getCodigo());
            }

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void eliminar(int id) {

        try (PreparedStatement preparedStatement = getConnection()
                .prepareStatement("delete from usuarios where id_usuarios = ?")) {

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static Usuario getUsuario(ResultSet resultSet) throws SQLException {

        Usuario usuario = new Usuario();
        usuario.setCodigo(resultSet.getInt("id_usuarios"));
        usuario.setUsername(resultSet.getString("username"));
        usuario.setPassword(resultSet.getString("password"));
        usuario.setEmail(resultSet.getString("email"));

        return usuario;
    }
}
