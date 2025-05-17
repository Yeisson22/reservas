import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

public class InterfazReservas extends JFrame {
    private ControladorReservas gestor;

    private JTextField nombreField, emailField, movilField;
    private JComboBox<String> recursoComboBox, mesComboBox;
    private JComboBox<Integer> anioComboBox, diaComboBox,horaInicioBox,minutoInicioBox,horaFinBox,minutoFinBox;
    private JTextArea resultadoArea;


    public InterfazReservas() {
        gestor = new ControladorReservas();
        setTitle("Gestor de Reservas");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contenidoPanel = new JPanel();
        contenidoPanel.setLayout(new BoxLayout(contenidoPanel,BoxLayout.Y_AXIS));
        contenidoPanel.setBackground(new Color(230,240,255));
        contenidoPanel.setBorder(new EmptyBorder(10,15,10,15));

        JPanel formPanel =new JPanel(new GridLayout(0,2,10,10));//panel principal
        formPanel.setBackground(new Color(230,240,255));

        // Campos de entrada
        formPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        formPanel.add(nombreField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Movil:"));
        movilField = new JTextField();
        formPanel.add(movilField);

        formPanel.add(new JLabel("Recurso:"));
        recursoComboBox = new JComboBox<>(new String[]{
                "Seleccionar recurso",
                "Aula101", "Aula102", "Aula103",
                "Computador",
                "Puesto de Trabajo #1", "Puesto de Trabajo #2", "Puesto de Trabajo #3"});
        formPanel.add(recursoComboBox);

        // Año de Reserva
        formPanel.add(new JLabel("Año de Reserva:"));
        anioComboBox = new JComboBox<>();
        for (int i = 2025; i <= 2030; i++) { // Cambia el rango según sea necesario
            anioComboBox.addItem(i);
        }
        formPanel.add(anioComboBox);

        // Mes de Inicio
        formPanel.add(new JLabel("Mes de Inicio:"));
        mesComboBox = new JComboBox<>(new String[]{
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        });
        formPanel.add(mesComboBox);

        formPanel.add(new JLabel("Día de Inicio:"));
        diaComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            diaComboBox.addItem(i);
        }
        formPanel.add(diaComboBox);


        formPanel.add(new JLabel("Hora de Inicio:"));
        horaInicioBox = new JComboBox<>();
        for (int i = 0; i <= 23; i++) {
            horaInicioBox.addItem(i);
        }
        formPanel.add(horaInicioBox);

        formPanel.add(new JLabel("Minutos de Inicio:"));
        minutoInicioBox = new JComboBox<>();
        for (int i = 0; i <= 59; i++) {
            minutoInicioBox.addItem(i);
        }
        formPanel.add(minutoInicioBox);

        formPanel.add(new JLabel("Hora de Fin:"));
        horaFinBox = new JComboBox<>();
        for (int i = 0; i <= 23; i++) {
            horaFinBox.addItem(i);
        }
        formPanel.add(horaFinBox);

        formPanel.add(new JLabel("Minutos de Fin:"));
        minutoFinBox = new JComboBox<>();
        for (int i = 0; i <= 59; i++) {
            minutoFinBox.addItem(i);
        }
        formPanel.add(minutoFinBox);
        contenidoPanel.add(formPanel);


        JButton reservarButton = new JButton("Hacer Reserva");
        reservarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservarButton.setBackground(new Color(100,150,255));
        reservarButton.setForeground(Color.white);
        reservarButton.setFocusPainted(false);
        reservarButton.setMaximumSize(new Dimension(200,40));

        JPanel botonPanel = new JPanel();
        botonPanel.setBackground(new Color(230,240,255));
        botonPanel.add(reservarButton);
        contenidoPanel.add(Box.createVerticalStrut(10));
        contenidoPanel.add(botonPanel);

        resultadoArea = new JTextArea(6,40);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(new Color(200,220,255));
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN,14));
        JScrollPane resultadosScroll = new JScrollPane(resultadoArea);
        contenidoPanel.add(Box.createVerticalStrut(10));
        contenidoPanel.add(resultadosScroll);

        // Scroll principal para todo
        JScrollPane scrollPrincipal = new JScrollPane(contenidoPanel);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(12);
        setContentPane(scrollPrincipal);

        reservarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hacerReserva();
            }
        });
    }

    private void hacerReserva() {
        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();
        String movil = movilField.getText().trim();
        String recurso = (String) recursoComboBox.getSelectedItem();
        // Obtener el recurso seleccionado

        // Validar campos
        if (nombre.isEmpty() || email.isEmpty() || movil.isEmpty() || recurso == null || recurso.equals("Seleccionar recurso")) {
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
        calendarInicio.set(anio,mes,dia,horaI,minutoI);
        calendarInicio.set(Calendar.MILLISECOND,0);
//        calendarInicio.set(Calendar.DAY_OF_MONTH,dia);
//        calendarInicio.set(Calendar.HOUR_OF_DAY,horaI);
//        calendarInicio.set(Calendar.MINUTE,minutoI);
//        calendarInicio.set(Calendar.SECOND,0);
//        calendarInicio.set(Calendar.MILLISECOND,0);
        Date fechaInicio = calendarInicio.getTime();

        Calendar calendarFin = Calendar.getInstance();
        calendarFin.set(anio,mes,dia,horaF,minutoF);
        calendarFin.set(Calendar.MILLISECOND,0);
//        calendarFin.set(Calendar.DAY_OF_MONTH,dia);
//        calendarFin.set(Calendar.HOUR_OF_DAY,horaF);
//        calendarFin.set(Calendar.MINUTE,minutoF);
//        calendarFin.set(Calendar.SECOND,0);
//        calendarFin.set(Calendar.MILLISECOND,0);
        Date fechaFin = calendarFin.getTime();

        if (!fechaFin.after(fechaInicio)) {
            resultadoArea.setText("La Fecha y la Hora de Fin Deben Ser Posteriores a la de Inicio");
            return;
        }

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
            InterfazReservas frame = new InterfazReservas();
            frame.setVisible(true);
        });
    }
}