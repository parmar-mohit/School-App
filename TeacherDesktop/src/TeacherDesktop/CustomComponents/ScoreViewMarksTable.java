package TeacherDesktop.CustomComponents;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ScoreViewMarksTable {
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JScrollPane scrollPane;

    public ScoreViewMarksTable() {
        String[] columns = {"SID", "Name", "Roll No", "Marks"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        //Editing Table
        setTableColumnWidths();
    }

    private void setTableColumnWidths() {
        //Editing Table
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(350);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
    }

    public void setStudentScoreList(JSONArray studentJsonArray) {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        for (int i = 0; i < studentJsonArray.length(); i++) {
            JSONObject studentScoreJsonObject = studentJsonArray.getJSONObject(i);
            int sid = studentScoreJsonObject.getInt("sid");
            String firstname = studentScoreJsonObject.getString("firstname");
            String lastname = studentScoreJsonObject.getString("lastname");
            int rollNo = studentScoreJsonObject.getInt("roll_no");
            int marks = studentScoreJsonObject.getInt("score");
            tableModel.addRow(new Object[]{sid, formatName(firstname, lastname), rollNo, marks});
        }
    }

    private String formatName(String firstname, String lastname) {
        String name = Character.toUpperCase(firstname.charAt(0)) + firstname.substring(1) + " ";
        name += Character.toUpperCase(lastname.charAt(0)) + lastname.substring(1);
        return name;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}
