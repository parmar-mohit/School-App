package TeacherDesktop.Panel;

import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class CreateClassroomPanel extends JPanel implements KeyListener, ActionListener {

    private ServerConnection serverConnection;
    private JLabel panelNameLabel,standardLabel,divisionLabel, teacherInchargeLabel,messageLabel;
    private JTextField standardTextField,divisionTextField;
    private JComboBox teacherInchargeComboBox;
    private JScrollPane scrollPane;
    private JPanel subjectListPanel;
    private ArrayList<CreateSubjectPanel> subjectList;
    private JButton addSubjectButton,removeSubjectButton, createClassrooomButton;
    private JSONArray teacherListJsonArray;

    public CreateClassroomPanel(ServerConnection serverConnection){
        //Initialising Members
        this.serverConnection = serverConnection;
        panelNameLabel = new JLabel("Create Classroom");
        standardLabel =  new JLabel("Standard : ");
        standardTextField = new JTextField(20);
        divisionLabel = new JLabel("Division : ");
        divisionTextField = new JTextField(20);
        teacherInchargeLabel = new JLabel("Teacher Incharge : ");
        teacherInchargeComboBox = new JComboBox();
        subjectList = new ArrayList<>();
        subjectListPanel = new JPanel();
        scrollPane = new JScrollPane(subjectListPanel);
        addSubjectButton = new JButton("Add Subject");
        removeSubjectButton = new JButton("Remove Subject");
        messageLabel = new JLabel();
        createClassrooomButton = new JButton("Create New Classroom");

        //Filling TeacherInchargeComboBox
        fillTeacherInchargeComboBox();

        //Editing SubjectListPanel
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,18));
        subjectListPanel.setLayout(new GridBagLayout());
        JLabel subjectListLabel = new JLabel("Subject List");
        subjectListLabel.setFont(new Font("Serif",Font.PLAIN,16));
        subjectListPanel.add(subjectListLabel,Constraint.setPosition(0,0));

        //Adding 3 Subjects
        for( int i = 1; i <= 3; i++){
            //Adding SubjectPanel
            CreateSubjectPanel subjectPanel = new CreateSubjectPanel(teacherListJsonArray,i);
            subjectList.add(subjectPanel);
            subjectListPanel.add(subjectPanel,Constraint.setPosition(0,subjectList.size()));
        }
        revalidate();
        repaint();

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,22));
        scrollPane.setMinimumSize(new Dimension(750,220));
        scrollPane.setPreferredSize(new Dimension(750,220));
        addSubjectButton.setBackground(Constant.BUTTON_BACKGROUND);
        addSubjectButton.setPreferredSize(Constant.BUTTON_SIZE);
        createClassrooomButton.setBackground(Constant.BUTTON_BACKGROUND);
        createClassrooomButton.setPreferredSize(Constant.BUTTON_SIZE);
        removeSubjectButton.setPreferredSize(Constant.BUTTON_SIZE);
        removeSubjectButton.setBackground(Constant.BUTTON_BACKGROUND);

        //Adding Listeners
        standardTextField.addKeyListener(this);
        divisionTextField.addKeyListener(this);
        addSubjectButton.addActionListener(this);
        removeSubjectButton.addActionListener(this);
        createClassrooomButton.addActionListener(this);

        //panel details
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding components to Panel
        add(panelNameLabel,Constraint.setPosition(0,0,4,1));
        add(standardLabel, Constraint.setPosition(0,1,Constraint.RIGHT));
        add(standardTextField,Constraint.setPosition(1,1,Constraint.LEFT));
        add(divisionLabel,Constraint.setPosition(2,1,Constraint.RIGHT));
        add(divisionTextField,Constraint.setPosition(3,1,Constraint.LEFT));
        add(teacherInchargeLabel,Constraint.setPosition(0,2,2,1,Constraint.RIGHT));
        add(teacherInchargeComboBox,Constraint.setPosition(2,2,2,1,Constraint.LEFT));
        add(scrollPane,Constraint.setPosition(0,3,4,1));
        add(addSubjectButton,Constraint.setPosition(0,4,2,1));
        add(removeSubjectButton,Constraint.setPosition(2,4,2,1));
        add(messageLabel,Constraint.setPosition(0,5,4,1));
        add(createClassrooomButton,Constraint.setPosition(0,6,4,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == addSubjectButton ){
            //Adding SubjectPanel
            CreateSubjectPanel subjectPanel = new CreateSubjectPanel(teacherListJsonArray,subjectList.size()+1);
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
        }else if( e.getSource() == createClassrooomButton){
            if( standardTextField.getText().equals("") ){
                messageLabel.setText("Enter Standard");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            JSONObject infoJsonObject = new JSONObject();
            infoJsonObject.put("standard",Integer.parseInt(standardTextField.getText()));

            if( divisionTextField.getText().equals("") ){
                messageLabel.setText("Enter Division");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            infoJsonObject.put("division",divisionTextField.getText().toUpperCase());

            if( teacherInchargeComboBox.getSelectedItem() == null ){
                messageLabel.setText("Select Teacher Incharge");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            infoJsonObject.put("teacher_incharge",getPhone(teacherInchargeComboBox.getSelectedItem()+""));

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
                subjectJsonObject.put("subject_name", subjectPanel.subjectNameTextField.getText().toLowerCase());

                if( subjectPanel.subjectTeacherComboBox.getSelectedItem() == null ){
                    messageLabel.setText("Select Subject Teacher for Subject No : "+(i+1));
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                }
                subjectJsonObject.put("subject_teacher",getPhone(subjectPanel.subjectTeacherComboBox.getSelectedItem()+""));
                subjectJsonArray.put(subjectJsonObject);
            }

            infoJsonObject.put("subject_list",subjectJsonArray);
            int response = serverConnection.createClassroom(infoJsonObject);

            if( response == 0 ){
                messageLabel.setText("Classroom Created");
                Constraint.labelDeleteAfterTime(messageLabel);

                //Clearing Fields
                standardTextField.setText(null);
                divisionTextField.setText(null);
                teacherInchargeComboBox.setSelectedItem(-1);

                //Adding 3 Blank Subjects
                subjectList = new ArrayList<>();
                subjectListPanel.removeAll();
                JLabel subjectListLabel = new JLabel("Subject List");
                subjectListLabel.setFont(new Font("Serif",Font.PLAIN,16));
                subjectListPanel.add(subjectListLabel,Constraint.setPosition(0,0));

                for( int i = 1; i <= 3; i++){
                    //Adding SubjectPanel
                    CreateSubjectPanel subjectPanel = new CreateSubjectPanel(teacherListJsonArray,i);
                    subjectList.add(subjectPanel);
                    subjectListPanel.add(subjectPanel,Constraint.setPosition(0,subjectList.size()));
                }
                revalidate();
                repaint();
            }else if( response == 1 ){
                messageLabel.setText("Classroom with same Standard and Division exist");
                Constraint.labelDeleteAfterTime(messageLabel);
            }
        }
    }

    private void fillTeacherInchargeComboBox(){
        teacherListJsonArray = serverConnection.getTeacherList();
        for( int i = 0; i < teacherListJsonArray.length(); i++){
            JSONObject jsonObject = teacherListJsonArray.getJSONObject(i);
            String firstname = jsonObject.getString("firstname");
            String lastname = jsonObject.getString("lastname");
            String phone = jsonObject.getString("phone");

            if(!phone.equals(Constant.PRINCIPAL_USERNAME+"")) {
                String x = Character.toUpperCase(firstname.charAt(0)) + firstname.substring(1) + " ";
                x += Character.toUpperCase(lastname.charAt(0)) + lastname.substring(1) + " ";
                x += "(" + jsonObject.getString("phone") + ")";

                teacherInchargeComboBox.insertItemAt(x, 0);
            }
        }
    }

    private String getPhone(String teacher){
        int last  = teacher.length()-1;
        int start = last - 10;
        return teacher.substring(start,last);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if( e.getSource() == standardTextField ){
            if( Character.isAlphabetic(e.getKeyChar()) || standardTextField.getText().length() > 1 ){
                e.consume();
            }
        }else if( e.getSource() == divisionTextField ){
            if( Character.isDigit(e.getKeyChar()) || divisionTextField.getText().length() > 0 ){
                e.consume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
