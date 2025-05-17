import java.sql.*;
import java.util.Date;

public class ControladorReservas {
    private Connection connection;

    public ControladorReservas() {
        try {
            connection = DatabaseConnection.getConnection();
            if (connection != null) {
                System.out.println("Conexión establecida correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        }
    }

    public boolean altaUsuario(String nombre, String email, String movil) {
        String sql = "INSERT INTO usuarios (nombre, email, movil) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, email);
            pstmt.setString(3, movil);
            pstmt.executeUpdate();
            return true; // Usuario agregado exitosamente
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Código de error para duplicados en MySQL
                System.out.println("Error: El correo electrónico ya está en uso.");
            } else {
                e.printStackTrace(); // Manejo de otros errores
            }
            return false; // No se pudo agregar el usuario
        }
    }

    public boolean hacerReserva(String nombre, String email, String movil, String recurso, Date fechaInicio, Date fechaFin) {
        // Verificar si el correo electrónico ya está en uso
        boolean usuarioExistente = correoEnUso(email );
        int idUsuario = -1; // Variable para almacenar el id del usuario

        // Si el usuario no existe, crear uno nuevo
        if (!usuarioExistente) {
            if (!altaUsuario(nombre, email, movil)) {
                System.out.println("No se pudo crear el usuario. Ya registrado.");
                return false; //salir
            }
        }

        // Obtener el id del usuario
        String sqlUser  = "SELECT id_usuario FROM usuarios WHERE email = ?";
        try (PreparedStatement infoUser  = connection.prepareStatement(sqlUser )) {
            infoUser .setString(1, email);
            ResultSet rs = infoUser .executeQuery();
            if (rs.next()) {
                idUsuario = rs.getInt("id_usuario"); // Obtener el id del usuario
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si hay un error al obtener el id, salir
        }

        // Verificar si hay conflictos de horario
        if (hayConflictoHorario(recurso, fechaInicio, fechaFin)) {
            System.out.println("Error: Conflicto de horario para el recurso " + recurso);
            return false;
        }

        // Iniciar la transacción
        try {
            connection.setAutoCommit(false); // Desactivar el autocommit

            // Insertar la reserva
            String sql = "INSERT INTO reservas (id_usuario, fecha_reserva, recurso, hora_inicio, hora_fin) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                pstmt.setDate(2, new java.sql.Date(fechaInicio.getTime()));
                pstmt.setString(3, recurso); // Usar el id del usuario obtenido
                pstmt.setTime(4, new java.sql.Time(fechaInicio.getTime()));
                pstmt.setTime(5, new java.sql.Time(fechaFin.getTime()));
                pstmt.executeUpdate();
            }

            // Confirmar la transacción
            connection.commit();
            return true; // Reserva realizada exitosamente
        } catch (SQLException e) {
            try {
                connection.rollback(); // Revertir cambios en caso de error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace(); // Manejo de errores
            return false; // No se pudo realizar la reserva
        } finally {
            try {
                connection.setAutoCommit(true); // Volver a activar el autocommit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean correoEnUso(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true si el correo ya está en uso
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // El correo no está en uso
    }

    private boolean hayConflictoHorario(String recurso, Date fechaInicio, Date fechaFin) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE recurso = ? AND fecha_reserva = ? AND (" +
                "(hora_inicio < ? AND hora_fin > ?) OR " +    // caso 1 y 2
                "(hora_inicio >= ? AND hora_inicio < ?) OR " + // caso 3
                "(hora_inicio <= ? AND hora_fin >= ?) " +      // caso 4 y 5
                ")";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, recurso);
            pstmt.setDate(2, new java.sql.Date(fechaInicio.getTime()));
            java.sql.Time inicio = new java.sql.Time(fechaInicio.getTime());
            java.sql.Time fin = new java.sql.Time(fechaFin.getTime());
            pstmt.setTime(3, fin);     // hora_inicio < nuevaFin
            pstmt.setTime(4, inicio);  // hora_fin > nuevaInicio
            pstmt.setTime(5, inicio);  // hora_inicio >= nuevaInicio
            pstmt.setTime(6, fin);     // hora_inicio < nuevaFin
            pstmt.setTime(7, inicio);  // hora_inicio <= nuevaInicio
            pstmt.setTime(8, fin);     // hora_fin >= nuevaFin

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Conflicto detectado
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta SQL de conflicto de horario:");
            e.printStackTrace();
        }
        return false; // No hay conflicto
    }
}