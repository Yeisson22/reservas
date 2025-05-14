import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

public class GestorDeReservas extends JFrame {
    private GestorReservas gestor;
    private JTextField nombreField, emailField, movilField;
    private JComboBox<String> recursoComboBox;
    private JComboBox<Integer> anioComboBox, diaComboBox,horaInicioBox,minutoInicioBox,horaFinBox,minutoFinBox;
    private JComboBox<String> mesComboBox; // Cambiado a String para los nombres de los meses
    private JTextArea resultadoArea;


    public GestorDeReservas() {
        gestor = new GestorReservas();
        setTitle("Gestor de Reservas");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));



        // Campos de entrada
        add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        add(nombreField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Movil:"));
        movilField = new JTextField();
        add(movilField);

        add(new JLabel("Recurso:"));
        recursoComboBox = new JComboBox<>(new String[]{
                "Seleccionar recurso",
                "Aula101", "Aula102", "Aula103",
                "Computador",
                "Puesto de Trabajo #1", "Puesto de Trabajo #2", "Puesto de Trabajo #3"});
        add(recursoComboBox);

        // Año de Reserva
        add(new JLabel("Año de Reserva:"));
        anioComboBox = new JComboBox<>();
        for (int i = 2025; i <= 2030; i++) { // Cambia el rango según sea necesario
            anioComboBox.addItem(i);
        }
        add(anioComboBox);

        // Mes de Inicio
        add(new JLabel("Mes de Inicio:"));
        mesComboBox = new JComboBox<>(new String[]{
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        });
        add(mesComboBox);

        add(new JLabel("Día de Inicio:"));
        diaComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            diaComboBox.addItem(i);
        }
        add(diaComboBox);


        add(new JLabel("Hora de Inicio:"));
        horaInicioBox = new JComboBox<>();
        for (int i = 0; i <= 23; i++) {
            horaInicioBox.addItem(i);
        }
        add(horaInicioBox);

        add(new JLabel("Minutos de Inicio:"));
        minutoInicioBox = new JComboBox<>();
        for (int i = 0; i <= 59; i++) {
            minutoInicioBox.addItem(i);
        }
        add(minutoInicioBox);

        add(new JLabel("Hora de Fin:"));
        horaFinBox = new JComboBox<>();
        for (int i = 0; i <= 23; i++) {
            horaFinBox.addItem(i);
        }
        add(horaFinBox);

        add(new JLabel("Minutos de Fin:"));
        minutoFinBox = new JComboBox<>();
        for (int i = 0; i <= 59; i++) {
            minutoFinBox.addItem(i);
        }
        add(minutoFinBox);

        JButton reservarButton = new JButton("Hacer Reserva");
        add(reservarButton);

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        add(new JScrollPane(resultadoArea));

        reservarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hacerReserva();
            }
        });
    }

    private void hacerReserva() {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String movil = movilField.getText();
        String recurso = (String) recursoComboBox.getSelectedItem();
        // Obtener el recurso seleccionado

        // Validar campos
        if (nombre.isEmpty() || email.isEmpty() || movil.isEmpty() || recurso.isEmpty()) {
            resultadoArea.setText("Por favor, complete todos los campos.");
            return;
        }

        int anio = (Integer) anioComboBox.getSelectedItem();
        int mes = mesComboBox.getSelectedIndex(); // Obtener el índice del mes seleccionado
        int dia = (Integer) diaComboBox.getSelectedItem();
        int horaI =(Integer) horaInicioBox.getSelectedItem();
        int minutoI =(Integer) minutoInicioBox.getSelectedItem();
        int horaF =(Integer) horaFinBox.getSelectedItem();
        int minutoF =(Integer) minutoFinBox.getSelectedItem();



        // Crear las fechas de inicio y fin
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.set(Calendar.YEAR,anio);
        calendarInicio.set(Calendar.MONTH,mes);
        calendarInicio.set(Calendar.DAY_OF_MONTH,dia);
        calendarInicio.set(Calendar.HOUR_OF_DAY,horaI);
        calendarInicio.set(Calendar.MINUTE,minutoI);
        calendarInicio.set(Calendar.SECOND,0);
        calendarInicio.set(Calendar.MILLISECOND,0);
        Date fechaInicio = calendarInicio.getTime();

        Calendar calendarFin = Calendar.getInstance();
        calendarFin.set(Calendar.YEAR,anio);
        calendarFin.set(Calendar.MONTH,mes);
        calendarFin.set(Calendar.DAY_OF_MONTH,dia);
        calendarFin.set(Calendar.HOUR_OF_DAY,horaF);
        calendarFin.set(Calendar.MINUTE,minutoF);
        calendarFin.set(Calendar.SECOND,0);
        calendarFin.set(Calendar.MILLISECOND,0);
        Date fechaFin = calendarFin.getTime();


        // Hacer la reserva
        boolean reservaExitosa = gestor.hacerReserva(nombre, email, movil, recurso, fechaInicio, fechaFin);
        if (reservaExitosa) {
            resultadoArea.setText("Reserva exitosa para " + nombre + " en " + recurso + " desde " + fechaInicio + " hasta " + fechaFin);
        } else {
            resultadoArea.setText("Error al hacer la reserva. Intente nuevamente.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestorDeReservas frame = new GestorDeReservas();
            frame.setVisible(true);
        });
    }
}