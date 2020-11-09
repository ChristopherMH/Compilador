import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

public class Main extends JFrame implements KeyListener, ActionListener, UndoableEditListener {

    private JTextArea txtConsola, txtLineas, txtMensaje;
    private int lineas;
    private JScrollPane scrollLineas, scrollConsola, scrollMensaje;
    private ArrayList<String> identificadores;
    private JButton btnCompilar, btnAbrirArchivo;
    private static String ruta;
    private UndoManager undoManager;
    private JTabbedPane tabs;
    private String temporales[] = { "chris1", "uriel1"};

    public Main() {
        lineas = 1;
        identificadores = new ArrayList<>();
        hazInterfaz();
    }

    private void hazInterfaz() {
        setTitle("Compilador");
        setSize(560, 730);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setAlwaysOnTop(true);
        getContentPane().setBackground(Color.decode("#1A2226"));

        undoManager = new UndoManager();
        undoManager.setLimit(50);

        txtLineas = new JTextArea();
        txtLineas.setEditable(false);
        txtLineas.setFocusable(false);
        txtLineas.setForeground(Color.WHITE);
        txtLineas.setFont(new Font("Default", 0, 16));
        txtLineas.setBackground(Color.decode("#303939"));
        txtLineas.setText("1\n");

        txtConsola = new JTextArea();
        txtConsola.setFont(new Font("Default", 0, 16));
        txtConsola.addKeyListener(this);
        txtConsola.setTabSize(1);
        txtConsola.getDocument().addUndoableEditListener(this);
        txtConsola.setBackground(Color.decode("#303A40"));
        txtConsola.setForeground(Color.WHITE);

        scrollConsola = new JScrollPane(txtConsola);
        scrollConsola.setBounds(30, 0, 510, 320);
        scrollConsola.getVerticalScrollBar().addAdjustmentListener(e -> {
            scrollLineas.getVerticalScrollBar().setValue(scrollConsola.getVerticalScrollBar().getValue());
        });

        scrollLineas = new JScrollPane(txtLineas);
        scrollLineas.setBounds(0, 0, 30, 320);
        scrollLineas.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txtMensaje = new JTextArea();
        txtMensaje.setBorder(BorderFactory.createBevelBorder(1));
        txtMensaje.setEditable(false);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.addKeyListener(this);
        txtMensaje.setBackground(Color.decode("#303A40"));
        txtMensaje.setForeground(Color.white);
        txtMensaje.setFont(new Font("", 0, 16));
        txtMensaje.setFocusable(false);

        scrollMensaje = new JScrollPane(txtMensaje);
        scrollMensaje.setBounds(30, 510, 400, 175);
        scrollMensaje.setEnabled(true);
        scrollMensaje.setBackground(Color.decode("#303A40"));
        scrollMensaje.setForeground(Color.WHITE);

        tabs= new JTabbedPane();
        tabs.setBounds(30, 320, 490, 185);

        tabs.addTab("Tabla de Símbolos",new TablaSimbolos());
        tabs.addTab("Cuádruplos", new TablaCuadruplos());

        btnCompilar = new JButton("Compilar");
        btnCompilar.setBounds(435, 510, 85, 40);
        btnCompilar.addActionListener(this);
        btnCompilar.setBackground(Color.decode("#58FA58"));

        btnAbrirArchivo = new JButton("Abrir");
        btnAbrirArchivo.setBounds(435, 550, 85, 40);
        btnAbrirArchivo.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Abrir archivo para compilar...");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos TXT", "txt");
            fileChooser.setFileFilter(filter);
            int seleccion = fileChooser.showOpenDialog(this);

            if (seleccion == JFileChooser.APPROVE_OPTION)
                leerArchivo(fileChooser.getSelectedFile().getAbsolutePath());
        });
        btnAbrirArchivo.setBackground(new Color(240, 240, 240));

        add(btnCompilar);
        add(btnAbrirArchivo);
        add(scrollLineas);
        add(scrollConsola);
        add(scrollMensaje);
        add(tabs);

        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            txtLineas.append(++lineas + "\n");
        }
        if ((e.getKeyCode() == KeyEvent.VK_Z) && (e.isControlDown())) {
            try {
                undoManager.undo();
            } catch (Exception cue) {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        if ((e.getKeyCode() == KeyEvent.VK_Y) && (e.isControlDown())) {
            try {
                undoManager.redo();
            } catch (Exception cue) {
                Toolkit.getDefaultToolkit().beep();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        /*if ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE){

        }*/

        /*if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            String[] lines = txtConsola.getText().split("\r\n|\r|\n");
            System.out.println(txtConsola.getText());
            for (int i=0;i<lines.length;i++){
                txtLineas.setText("");
                txtLineas.append(++i + "\n");
            }
        }*/
    }



    private void leerArchivo(String path) {
        try {
            txtConsola.setText("");
            txtLineas.setText("");
            lineas = 0;
            FileReader archivo = new FileReader(path);
            BufferedReader br;
            br = new BufferedReader(archivo);
            String line = "";

            while ((line = br.readLine()) != null) {
                txtConsola.append(line + "\n");
                txtLineas.append(++lineas + "\n");
            }
        } catch (Exception e) {
        }
    }

    private void generarArchivo() {
        ruta = "codigo.txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(txtConsola.getText());

            bw.close();
        } catch (Exception ex) {

        }
    }


    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (txtConsola.getText().trim().isEmpty()) return;
        generarArchivo();
        compilar(ruta);
    }

    public void compilar(String ruta) {

        Lexico lexico = new Lexico(ruta);

        ArrayList<String> erroresLexicos = lexico.resultado;
        Tabla tabla;
        Sintactico sintactico = null;

        txtMensaje.setText("");
        for (int i = 0; i < erroresLexicos.size(); i++) {
            txtMensaje.append(erroresLexicos.get(i) + " \n");
        }

        if (erroresLexicos.get(0).equals("No hay errores lexicos")) {
            sintactico = new Sintactico(lexico.tokenRC);
            tabla = new Tabla(lexico.tokenRC);
        }

        ArrayList<String> erroresSintacticos = sintactico.resultadoSintactico;
        ArrayList<String> erroresSemanticos = sintactico.resultadoSemantico;
        ArrayList<String> cuadruplos = sintactico.cuadruplos;

        if (erroresSintacticos.size() == 0)

        if (erroresSintacticos.size() == 0 && erroresSemanticos.size() == 0) {
            txtMensaje.append("No hay errores sintácticos \n");
            txtMensaje.append("No hay errores semánticos \n");
            txtMensaje.setBorder(BorderFactory.createLineBorder(Color.GREEN,2));
            if(!cuadruplos.isEmpty()){
                TablaCuadruplos.modeloCuadruplos.setRowCount(0);
                for (int i = 0; i < cuadruplos.size(); i++){
                    String tokens[] =  cuadruplos.get(i).split(" ");
                    TablaCuadruplos.modeloCuadruplos.addRow(new String[]{"","","","CUÁDRUPLO #"+(i+1)});
                    TablaCuadruplos.modeloCuadruplos.addRow(new String[]{"","","",cuadruplos.get(i)});
                    TablaCuadruplos.modeloCuadruplos.addRow(new String[]{tokens[3],tokens[2],tokens[4],temporales[i]});
                    TablaCuadruplos.modeloCuadruplos.addRow(new String[]{tokens[5],temporales[i],tokens[6],tokens[0]});
                }
            }
            return;
        }
        txtMensaje.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        if (erroresSintacticos.size() == 0)
            txtMensaje.append("No hay errores sintácticos \n");
        else {
            for (int i = 0; i < erroresSintacticos.size(); i++)
                txtMensaje.append(erroresSintacticos.get(i) + " \n");
            return;
        }
        if (erroresSemanticos.size() == 0)
            txtMensaje.append("No hay errores semánticos \n");
        else {
            for (int i = 0; i < erroresSemanticos.size(); i++)
                txtMensaje.append(erroresSemanticos.get(i) + " \n");
        }

    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        undoManager.addEdit(e.getEdit());
    }
}

class ModeloTabla extends DefaultTableModel {

    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    public boolean isCellEditable(int row, int co) {
        return false;
    }
}