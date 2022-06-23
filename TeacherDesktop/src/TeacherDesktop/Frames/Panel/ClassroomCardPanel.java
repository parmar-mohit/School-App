package TeacherDesktop.Frames.Panel;

import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClassroomCardPanel extends JPanel implements ActionListener {

    private JLabel standardLabel,divisionLabel,teacherInchargeLabel;
    private JButton expandButton,collapseButton;
    private JPanel subjectListPanel;
    private JSONObject classroomJsonObject;

    public ClassroomCardPanel(JSONObject classroomJsonObject){
        //Initialising Members
        this.classroomJsonObject = classroomJsonObject;
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

            //Editing Components
            collapseButton.setBackground(Constant.CARD_PANEL);
            subjectListPanel.setLayout(new GridBagLayout());
            subjectListPanel.setBackground(Constant.CARD_PANEL);

            //Adding Listeners
            collapseButton.addActionListener(this);

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
        }else if( e.getSource() == collapseButton ){
            //Setting Components Visible
            expandButton.setVisible(true);

            //Removing Components from Panel
            remove(collapseButton);
            remove(subjectListPanel);

            //Setting PanelSize
            setPreferredSize(new Dimension(900,100));
        }
    }

    private String getTeacherInchargeString(String firstname, String lastname, String phone){
        String teacherIncharge = Character.toUpperCase(firstname.charAt(0))+firstname.substring(1)+" ";
        teacherIncharge += Character.toUpperCase(lastname.charAt(0))+lastname.substring(1)+" ";
        teacherIncharge += "("+phone+")";
        return teacherIncharge;
    }
}