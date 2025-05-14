import java.util.Date;

public class Reserva {
    private Usuario usuario;
    private String recurso; // e.g., Aula, Equipo, Puesto
    private Date fechaInicio;
    private Date fechaFin;

    public Reserva(Usuario usuario, String recurso, Date fechaInicio, Date fechaFin) {
        this.usuario = usuario;
        this.recurso = recurso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Validar si hay solapamiento
    public boolean solapaCon(Reserva otraReserva) {
        return (this.fechaInicio.before(otraReserva.fechaFin) && this.fechaFin.after(otraReserva.fechaInicio));
    }

    // Getters
    public String getRecurso() {
        return recurso;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
