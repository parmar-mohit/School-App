package TeacherDesktop.Panel;

import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class CreateSubjectPanel extends JPanel {

    private JLabel subjectNameLabel,subjectTeacherLabel;
    public JTextField subjectNameTextField;
    public JComboBox subjectTeacherComboBox;
    private JSONArray teacherListJsonArray;

    public String oldSubjectName,oldSubjectTeacher;
    public CreateSubjectPanel(JSONArray teacherListJsonArray, int no){
        oldSubjectName = "null";
        oldSubjectTeacher = "null";
        //Intialising Member Variables
        this.teacherListJsonArray = teacherListJsonArray;
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
        for( int i = 0; i < teacherListJsonArray.length(); i++){
            JSONObject jsonObject = teacherListJsonArray.getJSONObject(i);
            String firstname = jsonObject.getString("firstname");
            String lastname = jsonObject.getString("lastname");
            String phone = jsonObject.getString("phone");

            if(!phone.equals(Constant.PRINCIPAL_USERNAME+"")) {
                String x = Character.toUpperCase(firstname.charAt(0)) + firstname.substring(1) + " ";
                x += Character.toUpperCase(lastname.charAt(0)) + lastname.substring(1) + " ";
                x += "(" + jsonObject.getString("phone") + ")";

                subjectTeacherComboBox.insertItemAt(x, 0);
            }
        }
    }
}
