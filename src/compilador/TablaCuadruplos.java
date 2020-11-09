import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class TablaCuadruplos extends JPanel {
    public static JTable tablaCuadruplos;
    public static ModeloTabla modeloCuadruplos;
    private JScrollPane scrollTable;

    public TablaCuadruplos() {
        iniciarTabla();
        scrollTable = new JScrollPane(tablaCuadruplos, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTable.getViewport().setBackground(Color.decode("#303939"));
        scrollTable.setPreferredSize(new Dimension(490, 150));
        add(scrollTable);
    }

    private void iniciarTabla() {
        modeloCuadruplos = new ModeloTabla();

        tablaCuadruplos = new JTable(modeloCuadruplos) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);

                Color alternateColor = Color.decode("#394D59");
                Color whiteColor = Color.decode("#4D6873");
                if (!comp.getBackground().equals(getSelectionBackground())) {
                    Color c = (row % 2 == 0 ? alternateColor : whiteColor);
                    comp.setBackground(c);
                    comp.setForeground(new Color(230, 230, 230));
                }
                if(row %4 == 0&& column == 3){
                    comp.setForeground(Color.yellow);
                }else if(row == 1 || row % 5 == 0 && column ==3){
                    comp.setForeground(Color.GREEN);
                }
                return comp;
            }
        };
        defineColumnas();
    }

    private void defineColumnas() {
        modeloCuadruplos.addColumn("Operador");
        modeloCuadruplos.addColumn("Operando 1");
        modeloCuadruplos.addColumn("Operando 2");
        modeloCuadruplos.addColumn("Resultado");
        tablaCuadruplos.setRowHeight(20);
        tablaCuadruplos.setFont(new Font("Default", 0, 16));

        TableColumnModel columnas = tablaCuadruplos.getColumnModel();
        columnas.getColumn(0).setMaxWidth(80);
        columnas.getColumn(1).setMaxWidth(100);
        columnas.getColumn(2).setMaxWidth(100);

    }
}