package TeacherDesktop.Panel;

import TeacherDesktop.EntityClasses.Subject;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import com.toedter.calendar.JDateChooser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class NewExamPanel extends JPanel implements KeyListener, ActionListener {

    private JLabel panelNameLabel,examNameLabel,dateLabel,totalMarksLabel,subjectLabel,messageLabel;
    private JSpinner examNameSpinner;
    private JDateChooser examDateDateChooser;
    private JTextField totalMarksTextField;
    private JComboBox subjectComboBox;
    private ServerConnection serverConnection;
    private String phone;
    private ArrayList<String> examNameList;
    private ArrayList<Subject> subjectList;

    public NewExamPanel(ServerConnection serverConnection,String phone){
        //Initialising Members
        this.serverConnection = serverConnection;
        this.phone = phone;
        getInfo();
        panelNameLabel = new JLabel("New Exam");
        examNameLabel = new JLabel("Exam Name : ");
        SpinnerModel examNameSpinnerModel;
        if( examNameList.size() == 0){
            examNameSpinnerModel = new SpinnerListModel(new Object[]{""});
        }else{
            examNameSpinnerModel = new SpinnerListModel(examNameList.toArray());
        }
        examNameSpinner = new JSpinner(examNameSpinnerModel);
        dateLabel = new JLabel("Date of Exam  : ");
        examDateDateChooser = new JDateChooser();
        totalMarksLabel = new JLabel("Total Marks : ");
        totalMarksTextField = new JTextField(20);
        subjectLabel = new JLabel("Subject (Standard:Division) : ");
        subjectComboBox = new JComboBox(subjectList.toArray());
        messageLabel = new JLabel();

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,18));
        examNameSpinner.setPreferredSize(new Dimension(180,25));
        examDateDateChooser.setPreferredSize(new Dimension(180,25));
        subjectComboBox.setPreferredSize(new Dimension(180,25));

        //Adding Listeners
        totalMarksTextField.addKeyListener(this);
        subjectComboBox.addActionListener(this);

        //Editing Panels
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panel
        add(panelNameLabel, Constraint.setPosition(0,0,2,1));
        add(examNameLabel,Constraint.setPosition(0,1,Constraint.RIGHT));
        add(examNameSpinner,Constraint.setPosition(1,1,Constraint.LEFT));
        add(dateLabel,Constraint.setPosition(0,2,Constraint.RIGHT));
        add(examDateDateChooser,Constraint.setPosition(1,2,Constraint.LEFT));
        add(totalMarksLabel,Constraint.setPosition(0,3,Constraint.RIGHT));
        add(totalMarksTextField,Constraint.setPosition(1,3,Constraint.LEFT));
        add(subjectLabel,Constraint.setPosition(0,4,Constraint.RIGHT));
        add(subjectComboBox,Constraint.setPosition(1,4,Constraint.LEFT));
        add(messageLabel,Constraint.setPosition(0,5,2,1));
    }

    private void getInfo(){
        JSONObject jsonObject = serverConnection.getExamAndSubjectList(phone);
        JSONArray examNameArray = jsonObject.getJSONArray("exam_name");
        examNameList = new ArrayList<>();
        for( int i = 0; i < examNameArray.length(); i++){
            examNameList.add(examNameArray.getString(i));
        }

        JSONArray subjectListJsonArray = jsonObject.getJSONArray("subject_list");
        subjectList = new ArrayList<>();
        for( int i = 0; i < subjectListJsonArray.length(); i++){
            subjectList.add(new Subject(subjectListJsonArray.getJSONObject(i)));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if( !Character.isDigit(e.getKeyChar()) ){
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}