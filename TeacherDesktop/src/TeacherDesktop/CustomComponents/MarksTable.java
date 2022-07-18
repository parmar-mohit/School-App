package TeacherDesktop.CustomComponents;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MarksTable {

    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;

    public MarksTable(){
        String columns[] = {"SID","Name","Roll No","Marks"};
        tableModel = new DefaultTableModel(columns,0);
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                if( column <= 2 ){
                    return false;
                }else{
                    return true;
                }
            }
        };
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800,250));

        //Editing Table
        setTableColumnWidths();
        table.setDefaultEditor(Object.class,new TableEditor());
    }

    private void setTableColumnWidths(){
        //Editing Table
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(350);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
    }

    public void setStudentList(JSONArray studentJsonArray){
        while( tableModel.getRowCount() > 0 ){
            tableModel.removeRow(0);
        }

        for( int i = 0; i < studentJsonArray.length(); i++ ){
            JSONObject studentJsonObject = studentJsonArray.getJSONObject(i);
            int sid = studentJsonObject.getInt("sid");
            String firstname = studentJsonObject.getString("firstname");
            String lastname = studentJsonObject.getString("lastname");
            int rollNo = studentJsonObject.getInt("roll_no");
            tableModel.addRow(new Object[]{sid,formatName(firstname,lastname),rollNo});
        }
    }

    public void clear(){
        while( tableModel.getRowCount() > 0 ){
            tableModel.removeRow(0);
        }
    }

    private String formatName(String firstname,String lastname){
        String name = Character.toUpperCase(firstname.charAt(0))+firstname.substring(1)+" ";
        name += Character.toUpperCase(lastname.charAt(0))+lastname.substring(1);
        return name;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public int getRowCount() {
        return table.getRowCount();
    }

    public Object getValueAt(int row, int column){
        return table.getValueAt(row,column);
    }
}

class TableEditor extends DefaultCellEditor {
    public TableEditor(){
        super(new JTextField());
        setClickCountToStart(1);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        final JTextField cellEdit = (JTextField) super.getTableCellEditorComponent(table,value,isSelected,row,column);
        cellEdit.setText((String)value);
        table.setSurrendersFocusOnKeystroke(true);

        //Adding Listeners
        cellEdit.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if( !Character.isDigit( e.getKeyChar() ) ){
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        return cellEdit;
    }
}