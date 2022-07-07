package TeacherDesktop.Dialog;

import TeacherDesktop.EntityClasses.Teacher;
import TeacherDesktop.Panel.CreateSubjectPanel;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UpdateClassroomDialog extends JDialog implements ActionListener {

    private JSONObject classroomJsonObject;
    private ServerConnection serverConnection;
    private JLabel panelNameLabel,standardLabel,divisionLabel, teacherInchargeLabel,messageLabel;
    private JComboBox teacherInchargeComboBox;
    private JScrollPane scrollPane;
    private JPanel subjectListPanel;
    private ArrayList<CreateSubjectPanel> subjectList;
    private JButton addSubjectButton,removeSubjectButton, updateClassrooomButton;
    private ArrayList<Teacher> teacherArrayList;
    public UpdateClassroomDialog(JFrame parent,JSONObject classroomJsonObject, ServerConnection serverConnection){
        super(parent);
        //Initialising Members
        this.classroomJsonObject = classroomJsonObject;
        this.serverConnection = serverConnection;
        panelNameLabel = new JLabel("Update Classroom");
        standardLabel =  new JLabel("Standard : "+classroomJsonObject.getInt("standard"));
        divisionLabel = new JLabel("Division : "+classroomJsonObject.getString("division"));
        teacherInchargeLabel = new JLabel("Teacher Incharge : ");
        teacherInchargeComboBox = new JComboBox();
        subjectList = new ArrayList<>();
        subjectListPanel = new JPanel();
        scrollPane = new JScrollPane(subjectListPanel);
        addSubjectButton = new JButton("Add Subject");
        removeSubjectButton = new JButton("Remove Subject");
        messageLabel = new JLabel();
        updateClassrooomButton = new JButton("Update Classroom");

        //Filling TeacherInchargeComboBox
        fillTeacherInchargeComboBox();

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,18));
        scrollPane.setMinimumSize(new Dimension(750,220));
        scrollPane.setPreferredSize(new Dimension(750,220));
        addSubjectButton.setBackground(Constant.BUTTON_BACKGROUND);
        addSubjectButton.setPreferredSize(Constant.BUTTON_SIZE);
        updateClassrooomButton.setBackground(Constant.BUTTON_BACKGROUND);
        updateClassrooomButton.setPreferredSize(Constant.BUTTON_SIZE);
        removeSubjectButton.setPreferredSize(Constant.BUTTON_SIZE);
        removeSubjectButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Adding Listeners
        addSubjectButton.addActionListener(this);
        removeSubjectButton.addActionListener(this);
        updateClassrooomButton.addActionListener(this);

        //Editing Dialog
        setTitle("Edit Classroom");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.SCHOOL_LOGO));
        setSize(new Dimension(900,500));
        setVisible(true);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Constant.CARD_PANEL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        //Adding components to Panel
        add(panelNameLabel,Constraint.setPosition(0,0,2,1));
        add(standardLabel, Constraint.setPosition(0,1));
        add(divisionLabel,Constraint.setPosition(1,1));
        add(teacherInchargeLabel,Constraint.setPosition(0,2,Constraint.RIGHT));
        add(teacherInchargeComboBox,Constraint.setPosition(1,2,Constraint.LEFT));
        add(scrollPane,Constraint.setPosition(0,3,2,1));
        add(addSubjectButton,Constraint.setPosition(0,4));
        add(removeSubjectButton,Constraint.setPosition(1,4));
        add(messageLabel,Constraint.setPosition(0,5,2,1));
        add(updateClassrooomButton,Constraint.setPosition(0,6,2,1));

        //Editing SubjectListPanel
        subjectListPanel.setLayout(new GridBagLayout());
        JLabel subjectListLabel = new JLabel("Subject List");
        subjectListLabel.setFont(new Font("Serif",Font.PLAIN,16));
        subjectListPanel.add(subjectListLabel, Constraint.setPosition(0,0));

        //Subjects
        JSONArray subjectListJsonArray = classroomJsonObject.getJSONArray("subject_list");
        for( int i = 1; i <= subjectListJsonArray.length(); i++){
            JSONObject subjectJsonObject = subjectListJsonArray.getJSONObject(i-1);

            //Adding SubjectPanel
            CreateSubjectPanel subjectPanel = new CreateSubjectPanel(teacherArrayList,i);
            subjectPanel.oldSubjectName = subjectJsonObject.getString("subject_name");
            subjectPanel.oldSubjectTeacher = subjectJsonObject.getJSONObject("teacher").getString("phone");
            subjectPanel.subjectNameTextField.setText(subjectJsonObject.getString("subject_name"));
            Teacher subjectTeacher = new Teacher(subjectJsonObject.getJSONObject("teacher"));
            for( int j = 0; j < teacherArrayList.size(); j++) {
                if( subjectTeacher.toString().equals(teacherArrayList.get(j).toString()) ){
                    subjectPanel.subjectTeacherComboBox.setSelectedItem(teacherArrayList.get(j));
                    break;
                }
            }
            subjectList.add(subjectPanel);
            subjectListPanel.add(subjectPanel,Constraint.setPosition(0,subjectList.size()));
        }
        revalidate();
        repaint();
    }

    private void fillTeacherInchargeComboBox(){
        JSONArray teacherListJsonArray = serverConnection.getTeacherList();
        teacherArrayList = new ArrayList<>();
        for( int i = 0; i < teacherListJsonArray.length(); i++){
            if( !teacherListJsonArray.getJSONObject(i).getString("phone").equals(Constant.PRINCIPAL_USERNAME) ) {
                Teacher teacher = new Teacher(teacherListJsonArray.getJSONObject(i));
                teacherArrayList.add(teacher);
                if( teacher.toString().equals(new Teacher(classroomJsonObject.getJSONObject("teacher")).toString()) ){
                    teacherInchargeComboBox.addItem(teacher);
                }else {
                    teacherInchargeComboBox.insertItemAt(teacher, 0);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == addSubjectButton ){
            //Adding SubjectPanel
            CreateSubjectPanel subjectPanel = new CreateSubjectPanel(teacherArrayList,subjectList.size()+1);
            subjectList.add(subjectPanel);
            subjectListPanel.add(subjectPanel,Constraint.setPosition(0,subjectList.size()));
            revalidate();
            repaint();
        }else if( e.getSource() == removeSubjectButton ){
            //Removing SubjectPanel
            CreateSubjectPanel lastSubjectPanel = subjectList.get(subjectList.size()-1);
            subjectListPanel.remove(lastSubjectPanel);
            subjectList.remove(lastSubjectPanel);
            revalidate();
            repaint();
        }else if( e.getSource() == updateClassrooomButton){

            JSONObject infoJsonObject = new JSONObject();
            infoJsonObject.put("standard",classroomJsonObject.getInt("standard"));
            infoJsonObject.put("division",classroomJsonObject.getString("division"));

            if( teacherInchargeComboBox.getSelectedItem() == null ){
                messageLabel.setText("Select Teacher Incharge");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            infoJsonObject.put("teacher_incharge",((Teacher)teacherInchargeComboBox.getSelectedItem()).getPhone());

            //Getting Subject List
            if( subjectList.size() == 0 ){
                messageLabel.setText("Minimum 1 Subject is required to create a Classroom");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }

            JSONArray subjectJsonArray = new JSONArray();
            for( int i = 0; i < subjectList.size(); i++){
                CreateSubjectPanel subjectPanel = subjectList.get(i);

                if( subjectPanel.subjectNameTextField.getText().equals("") ){
                    messageLabel.setText("Enter Subject Name for Subject No : "+(i+1));
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                }
                JSONObject subjectJsonObject = new JSONObject();
                subjectJsonObject.put("old_subject_name",subjectPanel.oldSubjectName);
                subjectJsonObject.put("new_subject_name", subjectPanel.subjectNameTextField.getText().toLowerCase());

                if( subjectPanel.subjectTeacherComboBox.getSelectedItem() == null ){
                    messageLabel.setText("Select Subject Teacher for Subject No : "+(i+1));
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                }
                subjectJsonObject.put("old_subject_teacher",subjectPanel.oldSubjectTeacher);
                subjectJsonObject.put("new_subject_teacher",((Teacher)subjectPanel.subjectTeacherComboBox.getSelectedItem()).getPhone());
                subjectJsonArray.put(subjectJsonObject);
            }

            infoJsonObject.put("subject_list",subjectJsonArray);
            int response = serverConnection.updateClassroom(infoJsonObject);

            if( response == 0 ){
                messageLabel.setText("Classroom Updated");
                Constraint.labelDeleteAfterTime(messageLabel);
            }else if( response == 1 ){
                messageLabel.setText("Some Error Occurred");
                Constraint.labelDeleteAfterTime(messageLabel);
            }
        }
    }
}
