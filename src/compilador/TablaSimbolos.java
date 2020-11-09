import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class TablaSimbolos extends JPanel {
    private JTable tablaSimbolos;
    public static ModeloTabla modeloTabla;
    private JScrollPane scrollTable;

    public TablaSimbolos(){
        iniciarTabla();
        scrollTable = new JScrollPane(tablaSimbolos, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTable.getViewport().setBackground(Color.decode("#303939"));
        scrollTable.setPreferredSize(new Dimension(490 ,150));
        add(scrollTable);
    }

    private void iniciarTabla() {
        modeloTabla = new ModeloTabla();

        tablaSimbolos = new JTable(modeloTabla) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                Color alternateColor = Color.decode("#394D59");
                Color whiteColor = Color.decode("#4D6873");
                if (!comp.getBackground().equals(getSelectionBackground())) {
                    Color c = (row % 2 == 0 ? alternateColor : whiteColor);
                    comp.setBackground(c);
                    comp.setForeground(new Color(230, 230, 230));
                }
                return comp;
            }
        };

        tablaSimbolos.getTableHeader().setReorderingAllowed(false); // Evitar mover columnas
        tablaSimbolos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Evitar multiselección
        defineColumnas();
    }

    private void defineColumnas() {
        modeloTabla.addColumn("No.");
        modeloTabla.addColumn("Modificador");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Identificador");
        modeloTabla.addColumn("Valor");
        modeloTabla.addColumn("Renglón");
        modeloTabla.addColumn("No. de Token");

        tablaSimbolos.setRowHeight(25);
        tablaSimbolos.setFont(new Font("Default", 0, 16));
        TableColumnModel columnas = tablaSimbolos.getColumnModel();
        columnas.getColumn(0).setMaxWidth(30);
        columnas.getColumn(1).setMaxWidth(100);
        columnas.getColumn(2).setMaxWidth(70);
        columnas.getColumn(3).setMaxWidth(80);
        columnas.getColumn(4).setMaxWidth(60);
        columnas.getColumn(5).setMaxWidth(60);
        columnas.getColumn(6).setMaxWidth(100);
    }
}
