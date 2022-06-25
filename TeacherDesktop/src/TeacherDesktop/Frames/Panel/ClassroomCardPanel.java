package TeacherDesktop.Frames.Panel;

import TeacherDesktop.Frames.Dialog.UpdateClassroomDialog;
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

    private JLabel standardLabel,divisionLabel,teacherInchargeLabel;
    private JButton expandButton,collapseButton, editButton;
    private JPanel subjectListPanel;
    private JSONObject classroomJsonObject;
    private ServerConnection serverConnection;
    private ClassroomPanel parent;

    public ClassroomCardPanel(JSONObject classroomJsonObject,ServerConnection serverConnection,ClassroomPanel parent){
        //Initialising Members
        this.classroomJsonObject = classroomJsonObject;
        this.serverConnection = serverConnection;
        this.parent = parent;
        standardLabel = new JLabel("Standard : "+classroomJsonObject.getInt("standard"));
        divisionLabel = new JLabel("Division : "+classroomJsonObject.getString("division"));
        String teacherIncharge = getTeacherInchargeString(classroomJsonObject.getString("firstname"),classroomJsonObject.getString("lastname"),classroomJsonObject.getString("teacher_incharge"));
        teacherInchargeLabel = new JLabel("Teacher Incharge : "+teacherIncharge);
        Image img = new ImageIcon(Constant.EXPAND_ICON).getImage();
        img = img.getScaledInstance(15,15,Image.SCALE_DEFAULT);
        expandButton = new JButton(new ImageIcon(img));

        //Editing Components
        expandButton.setBackground(Constant.CARD_PANEL);

        //Adding Listeners
        expandButton.addActionListener(this);

        //Editing Panel
        setLayout(new GridBagLayout());
        setBackground(Constant.CARD_PANEL);

        //Adding Components to Panel
        add(standardLabel, Constraint.setPosition(0,0));
        add(divisionLabel,Constraint.setPosition(1,0));
        add(teacherInchargeLabel,Constraint.setPosition(2,0));
        add(expandButton,Constraint.setPosition(3,0,Constraint.RIGHT));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == expandButton ){
            //Setting Components Invisible
            expandButton.setVisible(false);

            //Creating Components
            subjectListPanel = new JPanel();
            Image img = new ImageIcon(Constant.COLLAPSE_ICON).getImage();
            img = img.getScaledInstance(15,15,Image.SCALE_DEFAULT);
            collapseButton = new JButton(new ImageIcon(img));
            editButton = new JButton("Edit");

            //Editing Components
            collapseButton.setBackground(Constant.CARD_PANEL);
            subjectListPanel.setLayout(new GridBagLayout());
            subjectListPanel.setBackground(Constant.CARD_PANEL);
            editButton.setBackground(Constant.BUTTON_BACKGROUND);

            //Adding Listeners
            collapseButton.addActionListener(this);
            editButton.addActionListener(this);

            //Getting Panel Size
            Dimension panelSize = getPreferredSize();

            //Adding Components to subjecListPanel
            JSONArray subjectListJsonArray = classroomJsonObject.getJSONArray("subject_list");
            for( int i = 0; i < subjectListJsonArray.length(); i++ ){
                JSONObject subjectJsonObject = subjectListJsonArray.getJSONObject(i);
                String subjectName = subjectJsonObject.getString("subject_name");
                JLabel subjectNameLabel = new JLabel("Subject Name : "+Character.toUpperCase(subjectName.charAt(0))+subjectName.substring(1));
                String teacherIncharge = getTeacherInchargeString(subjectJsonObject.getString("firstname"),subjectJsonObject.getString("lastname"),subjectJsonObject.getString("subject_incharge"));
                JLabel subjectInchargeLabel = new JLabel("Subject Incharge : "+teacherIncharge);

                //Adding Components to Frame
                subjectListPanel.add(subjectNameLabel,Constraint.setPosition(0,i,Constraint.LEFT));
                subjectListPanel.add(subjectInchargeLabel,Constraint.setPosition(1,i,Constraint.LEFT));
                panelSize.height += 30;
            }

            //Setting Panel Size
            setPreferredSize(panelSize);

            //Adding Components to Panel
            add(collapseButton,Constraint.setPosition(3,0,Constraint.RIGHT));
            add(subjectListPanel,Constraint.setPosition(0,1,4,1));
            add(editButton,Constraint.setPosition(0,2,4,1));
        }else if( e.getSource() == collapseButton ){
            //Setting Components Visible
            expandButton.setVisible(true);

            //Removing Components from Panel
            remove(collapseButton);
            remove(subjectListPanel);
            remove(editButton);

            //Setting PanelSize
            setPreferredSize(new Dimension(900,100));
        }else if( e.getSource() == editButton ){
            JDialog dialog = new UpdateClassroomDialog((JFrame)SwingUtilities.getWindowAncestor(this),classroomJsonObject,serverConnection);
            dialog.addWindowListener(this);
        }
    }

    private String getTeacherInchargeString(String firstname, String lastname, String phone){
        String teacherIncharge = Character.toUpperCase(firstname.charAt(0))+firstname.substring(1)+" ";
        teacherIncharge += Character.toUpperCase(lastname.charAt(0))+lastname.substring(1)+" ";
        teacherIncharge += "("+phone+")";
        return teacherIncharge;
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