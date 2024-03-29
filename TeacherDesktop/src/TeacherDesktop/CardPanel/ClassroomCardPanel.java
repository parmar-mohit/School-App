package TeacherDesktop.CardPanel;

import TeacherDesktop.Dialog.UpdateClassroomDialog;
import TeacherDesktop.EntityClasses.Subject;
import TeacherDesktop.EntityClasses.Teacher;
import TeacherDesktop.Panel.ClassroomPanel;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ClassroomCardPanel extends JPanel implements ActionListener, WindowListener {

    private final JLabel standardLabel;
    private final JLabel divisionLabel;
    private final JLabel teacherInchargeLabel;
    private final JButton expandButton;
    private JButton collapseButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel subjectListPanel;
    private final JSONObject classroomJsonObject;
    private final ServerConnection serverConnection;
    private final ClassroomPanel parent;

    public ClassroomCardPanel(JSONObject classroomJsonObject, ServerConnection serverConnection, ClassroomPanel parent) {
        //Initialising Members
        this.classroomJsonObject = classroomJsonObject;
        this.serverConnection = serverConnection;
        this.parent = parent;
        standardLabel = new JLabel("Standard : " + classroomJsonObject.getInt("standard"));
        divisionLabel = new JLabel("Division : " + classroomJsonObject.getString("division"));
        Teacher classroomTeacher = new Teacher(classroomJsonObject.getJSONObject("teacher"));
        teacherInchargeLabel = new JLabel("Teacher Incharge : " + classroomTeacher);
        Image img = new ImageIcon(Constant.EXPAND_ICON).getImage();
        img = img.getScaledInstance(15, 15, Image.SCALE_DEFAULT);
        expandButton = new JButton(new ImageIcon(img));

        //Editing Components
        expandButton.setBackground(Constant.CARD_PANEL);

        //Adding Listeners
        expandButton.addActionListener(this);

        //Editing Panel
        setLayout(new GridBagLayout());
        setBackground(Constant.CARD_PANEL);

        //Adding Components to Panel
        add(standardLabel, Constraint.setPosition(0, 0));
        add(divisionLabel, Constraint.setPosition(1, 0));
        add(teacherInchargeLabel, Constraint.setPosition(2, 0));
        add(expandButton, Constraint.setPosition(3, 0, Constraint.RIGHT));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == expandButton) {
            //Setting Components Invisible
            expandButton.setVisible(false);

            //Creating Components
            subjectListPanel = new JPanel();
            Image img = new ImageIcon(Constant.COLLAPSE_ICON).getImage();
            img = img.getScaledInstance(15, 15, Image.SCALE_DEFAULT);
            collapseButton = new JButton(new ImageIcon(img));
            updateButton = new JButton("Update");
            deleteButton = new JButton("Delete");

            //Editing Components
            collapseButton.setBackground(Constant.CARD_PANEL);
            subjectListPanel.setLayout(new GridBagLayout());
            subjectListPanel.setBackground(Constant.CARD_PANEL);
            updateButton.setBackground(Constant.BUTTON_BACKGROUND);
            deleteButton.setBackground(Color.RED);

            //Adding Listeners
            collapseButton.addActionListener(this);
            updateButton.addActionListener(this);
            deleteButton.addActionListener(this);

            //Getting Panel Size
            Dimension panelSize = getPreferredSize();

            //Adding Components to subjecListPanel
            JSONArray subjectListJsonArray = classroomJsonObject.getJSONArray("subject_list");
            for (int i = 0; i < subjectListJsonArray.length(); i++) {
                JSONObject subjectJsonObject = subjectListJsonArray.getJSONObject(i);
                JLabel subjectNameLabel = new JLabel("Subject Name : " + Subject.getSubjectName(subjectJsonObject.getString("subject_name")));
                Teacher subjectTeacher = new Teacher(subjectJsonObject.getJSONObject("teacher"));
                JLabel subjectInchargeLabel = new JLabel("Subject Incharge : " + subjectTeacher);

                //Adding Components to Frame
                subjectListPanel.add(subjectNameLabel, Constraint.setPosition(0, i, Constraint.LEFT));
                subjectListPanel.add(subjectInchargeLabel, Constraint.setPosition(1, i, Constraint.LEFT));
                panelSize.height += 30;
            }

            //Setting Panel Size
            setPreferredSize(panelSize);

            //Adding Components to Panel
            add(collapseButton, Constraint.setPosition(3, 0, Constraint.RIGHT));
            add(subjectListPanel, Constraint.setPosition(0, 1, 4, 1));
            add(updateButton, Constraint.setPosition(0, 2, 2, 1));
            add(deleteButton, Constraint.setPosition(2, 2, 2, 1));
        } else if (e.getSource() == collapseButton) {
            //Setting Components Visible
            expandButton.setVisible(true);

            //Removing Components from Panel
            remove(collapseButton);
            remove(subjectListPanel);
            remove(updateButton);
            remove(deleteButton);

            //Setting PanelSize
            setPreferredSize(new Dimension(900, 100));
        } else if (e.getSource() == updateButton) {
            JDialog dialog = new UpdateClassroomDialog((JFrame) SwingUtilities.getWindowAncestor(this), classroomJsonObject, serverConnection);
            dialog.addWindowListener(this);
        } else if (e.getSource() == deleteButton) {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this classroom? All data including data of students in this class will be deleted.");

            if (result == JOptionPane.YES_OPTION) {
                int response = serverConnection.deleteClassroom(classroomJsonObject);
                if (response == 0) {
                    parent.fillClassroomCard();
                    parent.revalidate();
                    parent.repaint();
                }
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        parent.fillClassroomCard();
        parent.revalidate();
        parent.repaint();
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}