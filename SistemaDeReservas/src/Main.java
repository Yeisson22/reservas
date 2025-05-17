import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ControladorReservas gestor = new ControladorReservas();
        Scanner scanner = new Scanner(System.in);

        // Solicitar el nombre del usuario
        System.out.print("Ingrese el nombre del usuario: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese el email del usuario: ");
        String email = scanner.nextLine();

        System.out.print("Ingrese el numero de movil del usuario: ");
        String movil = scanner.nextLine();

        if (!gestor.altaUsuario(nombre, email, movil)) {
            System.out.println("Hubo un error: No se realizará la reserva.");
            scanner.close();
            return; // Salir si no se pudo agregar el usuario
        }

        boolean continuar = true;

        // Lista de recursos disponibles
        List<String> recursos = new ArrayList<>();
        recursos.add("Aula");
        recursos.add("Equipo");
        recursos.add("Puesto de trabajo");
        // Agrega más recursos según sea necesario

        while (continuar) {
            // Mostrar la lista de recursos
            System.out.println("Seleccione el tipo de recurso a reservar:");
            for (int i = 0; i < recursos.size(); i++) {
                System.out.println((i + 1) + ". " + recursos.get(i));
            }
            System.out.print("Ingrese el número correspondiente: ");
            int opcionRecurso = scanner.nextInt();
            scanner.nextLine();

            if (opcionRecurso < 1 || opcionRecurso > recursos.size()) {
                System.out.println("Opción no válida. Intente de nuevo.");
                continue; // Volver a solicitar el recurso
            }

            String recurso = recursos.get(opcionRecurso - 1); // Obtener el recurso seleccionado

            System.out.print("Ingrese el año de la reserva (ej. 2023) o 'salir' para terminar: ");
            String inputAnio = scanner.nextLine();
            if (inputAnio.equalsIgnoreCase("salir")) {
                continuar = false;
                break;
            }
            int anio = Integer.parseInt(inputAnio);

            System.out.print("Ingrese el mes de inicio (1-12): ");
            int mes = scanner.nextInt() - 1; // Calendar usa 0-11 para los meses

            System.out.print("Ingrese el día de inicio: ");
            int dia = scanner.nextInt();

            System.out.print("Ingrese la hora de inicio (0-23): ");
            int horaInicio = scanner.nextInt();

            System.out.print("Ingrese los minutos de inicio (0-59): ");
            int minutoInicio = scanner.nextInt();

            System.out.print("Ingrese la hora de fin (0-23): ");
            int horaFin = scanner.nextInt();

            System.out.print("Ingrese los minutos de fin (0-59): ");
            int minutoFin = scanner.nextInt();
            scanner.nextLine();

            // Crear las fechas de inicio y fin
            Calendar calendar = Calendar.getInstance();
            calendar.set(anio, mes, dia, horaInicio, minutoInicio);
            Date fechaInicio = calendar.getTime();
            calendar.set(anio, mes, dia, horaFin, minutoFin);
            Date fechaFin = calendar.getTime();

            // Hacer la reserva
            boolean reservaExitosa = gestor.hacerReserva(nombre, email, movil, recurso, fechaInicio, fechaFin);
            System.out.println("Reserva exitosa: " + reservaExitosa);
        }


        scanner.close();
    }
}