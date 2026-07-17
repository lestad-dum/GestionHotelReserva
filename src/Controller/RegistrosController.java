package controller;

import dao.ClienteDAO;
import dao.HabitacionDAO;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import model.Cliente;
import model.Habitacion;
import view.BuscarCodigo;
import view.Registros;
import model.Reserva;
import service.ReservaFactory;
import service.ReservaService;
import java.time.LocalDate;
import java.time.ZoneId;
import view.GestionReserva;



public class RegistrosController {

    private final Registros vista;
    private final ClienteDAO clienteDAO;
    private final HabitacionDAO habitacionDAO;
    private final ReservaService reservaService;

    private boolean dniValidado = false;
    private String ultimoDniValidado = "";

    public RegistrosController(Registros vista) {
        this.vista = vista;
        this.clienteDAO = new ClienteDAO();
        this.habitacionDAO = new HabitacionDAO();
        this.reservaService = new ReservaService();

        iniciarEventos();
        cargarHabitaciones();
    }

    private void iniciarEventos() {

        vista.getBotonValidarDni().addActionListener(e -> {
            validarDni();
        });

        vista.getBotonRegistrarCliente().addActionListener(e -> {
            registrarCliente();
        });

        vista.getBotonBuscarCodigo().addActionListener(e -> {
            abrirBuscarCodigo();
        });
        
        vista.getBotonGestionReserva().addActionListener(e -> {
         abrirGestionReservas();
         });
        vista.getBotonRegistrarReserva().addActionListener(e -> {
        registrarReserva();
        });
        
        vista.getTabbedPane().addChangeListener(e -> {

    int pestaña = vista.getTabbedPane().getSelectedIndex();

    if (pestaña == 0) {
        vista.limpiarCamposCliente();
    }

    if (pestaña == 1) {
        vista.limpiarCamposReserva();
    }

});
    }

    private void validarDni() {

        String dni = vista.getDniCliente();

        dniValidado = false;
        ultimoDniValidado = "";

        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Ingrese el DNI del cliente.",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!dni.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(
                    vista,
                    "El DNI debe contener exactamente 8 números.",
                    "DNI inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean existe = clienteDAO.existeDni(dni);

        if (existe) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Ya existe un cliente registrado con ese DNI.",
                    "DNI registrado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        dniValidado = true;
        ultimoDniValidado = dni;

        JOptionPane.showMessageDialog(
                vista,
                "DNI disponible. Puede registrar al cliente.",
                "Validación correcta",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void abrirGestionReservas() {

    javax.swing.JFrame ventana = new javax.swing.JFrame("Gestión de Reservas");

    GestionReserva panel = new GestionReserva();

    new GestionReservaController(panel);

    ventana.setContentPane(panel);
    ventana.pack();
    ventana.setLocationRelativeTo(null);
    ventana.setVisible(true);
}
    
    private void registrarCliente() {

        String dni = vista.getDniCliente();
        String nombres = vista.getNombresCliente();
        String apellidos = vista.getApellidosCliente();
        String correo = vista.getCorreoCliente();
        String telefono = vista.getTelefonoCliente();

        if (dni.isEmpty()
                || nombres.isEmpty()
                || apellidos.isEmpty()
                || correo.isEmpty()
                || telefono.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Complete todos los campos.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!dni.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(
                    vista,
                    "El DNI debe contener exactamente 8 números.",
                    "DNI inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!telefono.matches("\\d{9}")) {
            JOptionPane.showMessageDialog(
                    vista,
                    "El teléfono debe contener exactamente 9 números.",
                    "Teléfono inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Ingrese un correo válido.",
                    "Correo inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!dniValidado || !dni.equals(ultimoDniValidado)) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Primero debe validar el DNI.",
                    "DNI no validado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Cliente cliente = new Cliente();

        cliente.setDni(dni);
        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono);

        boolean registrado = clienteDAO.registrarCliente(cliente);

        if (registrado) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Cliente registrado correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            vista.limpiarCamposCliente();

            dniValidado = false;
            ultimoDniValidado = "";

        } else {
            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo registrar al cliente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void abrirBuscarCodigo() {

        BuscarCodigo panelBuscar = new BuscarCodigo();

        JDialog ventanaBuscar = new JDialog(
                vista,
                "Buscar código de cliente",
                true
        );

        ventanaBuscar.setContentPane(panelBuscar);
        ventanaBuscar.pack();
        ventanaBuscar.setResizable(false);
        ventanaBuscar.setLocationRelativeTo(vista);

        ventanaBuscar.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        new BuscarCodigoController(
                panelBuscar,
                vista,
                ventanaBuscar
        );

        ventanaBuscar.setVisible(true);
    }
    
   private void cargarHabitaciones() {

    vista.getComboHabitaciones().removeAllItems();

    List<Habitacion> habitaciones =
            habitacionDAO.listarHabitaciones();

    for (Habitacion habitacion : habitaciones) {
        vista.getComboHabitaciones().addItem(habitacion);
    }
}
   private void registrarReserva() {

    String codigoTexto = vista.getCodigoClienteReserva();

    if (codigoTexto.isEmpty()) {
        JOptionPane.showMessageDialog(
                vista,
                "Primero busque el código del cliente.",
                "Cliente requerido",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    Object objetoSeleccionado =
            vista.getHabitacionSeleccionada();

    if (!(objetoSeleccionado instanceof Habitacion)) {
        JOptionPane.showMessageDialog(
                vista,
                "Seleccione una habitación.",
                "Habitación requerida",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    if (vista.getFechaIngreso() == null
            || vista.getFechaSalida() == null) {

        JOptionPane.showMessageDialog(
                vista,
                "Seleccione la fecha de ingreso y salida.",
                "Fechas requeridas",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    long clienteId;

    try {
        clienteId = Long.parseLong(codigoTexto);

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
                vista,
                "El código del cliente no es válido.",
                "Código inválido",
                JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    Habitacion habitacion =
            (Habitacion) objetoSeleccionado;

    LocalDate fechaIngreso = vista.getFechaIngreso()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

    LocalDate fechaSalida = vista.getFechaSalida()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

    if (!fechaSalida.isAfter(fechaIngreso)) {

        JOptionPane.showMessageDialog(
                vista,
                "La fecha de salida debe ser posterior "
                + "a la fecha de ingreso.",
                "Fechas inválidas",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    if (fechaIngreso.isBefore(LocalDate.now())) {

        JOptionPane.showMessageDialog(
                vista,
                "La fecha de ingreso no puede ser anterior "
                + "a la fecha actual.",
                "Fecha inválida",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    Reserva reserva = ReservaFactory.crearReserva(
            clienteId,
            habitacion.getId(),
            fechaIngreso,
            fechaSalida
    );

    boolean registrado =
            reservaService.registrarReserva(reserva);

    if (registrado) {

        JOptionPane.showMessageDialog(
                vista,
                "Reserva registrada correctamente.\n"
                + "Habitación: "
                + habitacion.getNumero()
                + " - "
                + habitacion.getTipo(),
                "Reserva exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        vista.limpiarCamposReserva();

    } else {

        JOptionPane.showMessageDialog(
                vista,
                "La habitación no está disponible "
                + "para las fechas seleccionadas.",
                "Habitación no disponible",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
}
