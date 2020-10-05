package ehhCabeza;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame implements KeyListener, ActionListener {

    private JTextArea txtConsola, txtLineas, txtMensaje;
    private int lineas;
    private JScrollPane scrollLineas, scrollConsola, scrollMensaje;
    private JTable tablaSimbolos;
    private ModeloTabla modelo;
    private JScrollPane scrollTable;
    private ArrayList<String> identificadores;
    private JButton btn;
    private static String ruta;

    public Main() {
        lineas = 1;
        identificadores = new ArrayList<>();
        hazInterfaz();
    }

    private void hazInterfaz() {
        setTitle("Compilador");
        setSize(550, 730);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);

        txtLineas = new JTextArea();
        txtLineas.setEnabled(false);
        txtLineas.setForeground(Color.BLACK);
        txtLineas.setFont(new Font("", 0, 12));
        txtLineas.setBorder(BorderFactory.createLineBorder(Color.black));
        txtLineas.setText("1\n");

        txtConsola = new JTextArea();
        txtConsola.setFont(new Font("", 0, 12));
        txtConsola.addKeyListener(this);

        scrollConsola = new JScrollPane(txtConsola);
        scrollConsola.setBounds(30, 0, 400, 400);
        scrollConsola.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                scrollLineas.getVerticalScrollBar().setValue(scrollConsola.getVerticalScrollBar().getValue());
            }
        });

        scrollLineas = new JScrollPane(txtLineas);
        scrollLineas.setBounds(0, 0, 25, 400);
        scrollLineas.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txtMensaje = new JTextArea();
        txtMensaje.setBorder(BorderFactory.createBevelBorder(1));
        txtMensaje.setEnabled(false);

        scrollMensaje = new JScrollPane(txtMensaje);
        scrollMensaje.setBounds(30, 400, 400, 300);
        scrollMensaje.setEnabled(false);

        iniciarTabla();
        scrollTable = new JScrollPane(tablaSimbolos);
        scrollTable.setBounds(435, 0, 100, 400);

        btn = new JButton("Compilar");
        btn.setBounds(435, 410, 100, 50);
        btn.addActionListener(this);

        add(btn);
        add(scrollTable);
        add(scrollLineas);
        add(scrollConsola);
        add(scrollMensaje);

        setVisible(true);
    }

    private void iniciarTabla() {
        modelo = new ModeloTabla();
        tablaSimbolos = new JTable(modelo);
        defineColumnas();
    }

    private void defineColumnas() {
        ((DefaultTableModel) tablaSimbolos.getModel()).setColumnCount(0); // Borramos las columnas que haya (en caso de)
        modelo.addColumn("<html><h4>&nbsp Identificador");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            txtLineas.append(++lineas + "\n");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        txtMensaje.setForeground(Color.BLACK);
//		try {
//			ArrayList<Token> tokens = Lexer.lex(txtConsola.getText());
//			// Obtener salida de resultados
//			txtMensaje.setText("");
//			identificadores.clear();
//			((DefaultTableModel) tablaSimbolos.getModel()).setNumRows(0);
//			int contador = 1;
//			for (Token token : tokens) {
//				txtMensaje.append("Tipo: " + token.getTipo() + "   Valor: " + token.getValor() + "\n");
//				txtMensaje.setDisabledTextColor(Color.BLACK);
//				if (token.getTipo().equals("IDENTIFICADOR ")) {
//					if (identificadores.contains(token.getValor())) {
//						throw new Exception("Identificador duplicado en línea " + contador);
//					} else {
//						modelo.addRow(new String[] { token.getValor() });
//						identificadores.add(token.getValor());
//					}
//				}
//				contador++;
//			}
//		} catch (RuntimeException ex) {
//			txtMensaje.setDisabledTextColor(Color.red);
//			txtMensaje.setText("Error en línea " + ex.getMessage());
//		} catch (Exception exc) {
//			txtMensaje.setDisabledTextColor(Color.red);
//			txtMensaje.setText(exc.getMessage());
//		}

    }

    private void generarArchivo() {
        String ruta = "codigo.txt";
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
        generarArchivo();
        compilar();
    }

    public void compilar() {

        Analiza analizador = new Analiza("codigo.txt");
        ArrayList<String> a1 = analizador.resultado;
        ArrayList<Token> tk = analizador.tokenRC;
        Tabla t;
        Sintactico s;

//			for(int i = 0; i < tk.size(); i++)
//			{ 
//				System.out.println( tk.get(i).getToken() + "\t\t" + tk.get(i).getTipo() );
//			}

        txtMensaje.setText("");
        txtMensaje.setDisabledTextColor(Color.BLACK);
        for (int i = 0; i < a1.size(); i++) {
            txtMensaje.append(a1.get(i) + " \n");
        }

        if (a1.get(0).equals("No hay errores lexicos")) {
            s = new Sintactico(analizador.tokenRC);
            t = new Tabla(analizador.tokenRC);

        }

    }
}

class ModeloTabla extends DefaultTableModel {

    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    public boolean isCellEditable(int row, int co) {
        return false;
    }

}
