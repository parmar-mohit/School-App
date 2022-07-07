package TeacherDesktop.Panel;

import TeacherDesktop.EntityClasses.Teacher;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CreateSubjectPanel extends JPanel {

    private JLabel subjectNameLabel,subjectTeacherLabel;
    public JTextField subjectNameTextField;
    public JComboBox subjectTeacherComboBox;
    private ArrayList<Teacher> teacherArrayList;
    public String oldSubjectName,oldSubjectTeacher;
    public CreateSubjectPanel(ArrayList<Teacher> teacherArrayList, int no){
        oldSubjectName = "null";
        oldSubjectTeacher = "null";
        //Intialising Member Variables
        this.teacherArrayList = teacherArrayList;
        subjectNameLabel = new JLabel("Subject Name "+no+" : ");
        subjectNameTextField = new JTextField(20);
        subjectTeacherLabel = new JLabel("Subject Teacher : ");
        subjectTeacherComboBox = new JComboBox();

        //Filling SubjectIncharge ComboBox
        fillSubjectTeacherComboBox();

        //Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panel
        add(subjectNameLabel, Constraint.setPosition(0,0,Constraint.RIGHT));
        add(subjectNameTextField,Constraint.setPosition(1,0,Constraint.LEFT));
        add(subjectTeacherLabel,Constraint.setPosition(2,0,Constraint.RIGHT));
        add(subjectTeacherComboBox,Constraint.setPosition(3,0,Constraint.LEFT));
    }

    private void fillSubjectTeacherComboBox(){
        for( int i = 0; i < teacherArrayList.size(); i++ ){
            subjectTeacherComboBox.insertItemAt(teacherArrayList.get(i),0);
        }
    }
}
