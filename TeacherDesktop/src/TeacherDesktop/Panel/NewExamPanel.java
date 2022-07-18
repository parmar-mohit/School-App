package TeacherDesktop.Panel;

import TeacherDesktop.CustomComponents.ExamNameSelector;
import TeacherDesktop.CustomComponents.MarksTable;
import TeacherDesktop.EntityClasses.Subject;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import com.toedter.calendar.JDateChooser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;

public class NewExamPanel extends JPanel implements KeyListener, ActionListener {

    private JLabel panelNameLabel,examNameLabel,dateLabel,totalMarksLabel,subjectLabel,messageLabel;
    private ExamNameSelector examNameSelector;
    private JDateChooser examDateDateChooser;
    private JTextField totalMarksTextField;
    private JComboBox subjectComboBox;
    private ServerConnection serverConnection;
    private String phone;
    private ArrayList<String> examNameArrayList;
    private ArrayList<Subject> subjectArrayList;
    private MarksTable table;
    private JButton addNewExamButton;
    private Thread getStudentListThread;

    public NewExamPanel(ServerConnection serverConnection,String phone){
        //Initialising Members
        this.serverConnection = serverConnection;
        this.phone = phone;
        getInfo();
        panelNameLabel = new JLabel("New Exam");
        examNameLabel = new JLabel("Exam Name : ");
        examNameSelector = new ExamNameSelector();
        examNameSelector.setExamList(examNameArrayList);
        dateLabel = new JLabel("Date of Exam  : ");
        examDateDateChooser = new JDateChooser();
        totalMarksLabel = new JLabel("Total Marks : ");
        totalMarksTextField = new JTextField(20);
        subjectLabel = new JLabel("Subject (Standard:Division) : ");
        subjectComboBox = new JComboBox();
        fillSubjectComboBox();
        table = new MarksTable();
        messageLabel = new JLabel();
        addNewExamButton = new JButton("Add New Exam");

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,18));
        examNameSelector.setPreferredSize(new Dimension(180,25));
        examDateDateChooser.setPreferredSize(new Dimension(180,25));
        subjectComboBox.setPreferredSize(new Dimension(180,25));
        addNewExamButton.setBackground(Constant.BUTTON_BACKGROUND);
        addNewExamButton.setPreferredSize(Constant.BUTTON_SIZE);

        //Adding Listeners
        totalMarksTextField.addKeyListener(this);
        subjectComboBox.addActionListener(this);
        addNewExamButton.addActionListener(this);

        //Editing Panels
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panel
        add(panelNameLabel, Constraint.setPosition(0,0,4,1));
        add(examNameLabel,Constraint.setPosition(0,1,Constraint.RIGHT));
        add(examNameSelector,Constraint.setPosition(1,1,Constraint.LEFT));
        add(dateLabel,Constraint.setPosition(2,1,Constraint.RIGHT));
        add(examDateDateChooser,Constraint.setPosition(3,1,Constraint.LEFT));
        add(totalMarksLabel,Constraint.setPosition(0,2,Constraint.RIGHT));
        add(totalMarksTextField,Constraint.setPosition(1,2,Constraint.LEFT));
        add(subjectLabel,Constraint.setPosition(2,2,Constraint.RIGHT));
        add(subjectComboBox,Constraint.setPosition(3,2,Constraint.LEFT));
        add(table.getScrollPane(),Constraint.setPosition(0,3,4,1));
        add(messageLabel,Constraint.setPosition(0,4,4,1));
        add(addNewExamButton,Constraint.setPosition(0,5,4,1));
    }

    private void getInfo(){
        JSONObject jsonObject = serverConnection.getExamAndSubjectList(phone);
        JSONArray examNameArray = jsonObject.getJSONArray("exam_name");
        examNameArrayList = new ArrayList<>();
        for( int i = 0; i < examNameArray.length(); i++){
            examNameArrayList.add(examNameArray.getString(i));
        }

        JSONArray subjectListJsonArray = jsonObject.getJSONArray("subject_list");
        subjectArrayList = new ArrayList<>();
        for( int i = 0; i < subjectListJsonArray.length(); i++){
            subjectArrayList.add(new Subject(subjectListJsonArray.getJSONObject(i)));
        }
    }

    private void fillSubjectComboBox(){
        for(int i = 0; i < subjectArrayList.size(); i++){
            subjectComboBox.insertItemAt(subjectArrayList.get(i),0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == subjectComboBox) {
            getStudentListThread = new Thread() {
                @Override
                public void run() {
                    Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();

                    if( selectedSubject != null ) {
                        JLabel messageLabel = new JLabel("Getting Student Details,Please Wait...");
                        JProgressBar progressBar = new JProgressBar(0, 100);
                        progressBar.setStringPainted(true);

                        remove(table.getScrollPane());
                        remove(NewExamPanel.this.messageLabel);
                        addNewExamButton.setVisible(false);
                        add(messageLabel, Constraint.setPosition(0, 3, 4, 1));
                        add(progressBar, Constraint.setPosition(0, 4, 4, 1));

                        JSONArray studentJsonArray = serverConnection.getStudentListForExam(selectedSubject.getStandard(), selectedSubject.getDivision(), progressBar);
                        table.setStudentList(studentJsonArray);

                        remove(messageLabel);
                        remove(progressBar);
                        addNewExamButton.setVisible(true);
                        add(table.getScrollPane(), Constraint.setPosition(0, 3, 4, 1));
                        add(NewExamPanel.this.messageLabel, Constraint.setPosition(0, 4, 4, 1));
                    }
                }
            };
            getStudentListThread.start();
        }else if( e.getSource() == addNewExamButton ) {
            JSONObject examJsonObject = new JSONObject();

            String examName = examNameSelector.getExamName();
            if ( examName.equals("")) {
                messageLabel.setText("Enter Exam Name");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            examJsonObject.put("exam_name", examName.toLowerCase());

            Date examDate = examDateDateChooser.getDate();
            if (examDate == null) {
                messageLabel.setText("Enter Exam Date");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            } else if (examDate.after(new Date())) {
                messageLabel.setText("Select Proper Date");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            examJsonObject.put("date", examDate.getTime());

            if (totalMarksTextField.getText().equals("")) {
                messageLabel.setText("Enter Total Marks");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            int totalMarks = Integer.parseInt(totalMarksTextField.getText());
            examJsonObject.put("total_marks", totalMarks);

            if (subjectComboBox.getSelectedItem() == null) {
                messageLabel.setText("Select Subject");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
            examJsonObject.put("subject_id", selectedSubject.getSubjectId());

            JSONArray scoreJsonArray = new JSONArray();
            for (int i = 0; i < table.getRowCount(); i++) {
                JSONObject scoreJsonObject = new JSONObject();
                scoreJsonObject.put("sid", (int) table.getValueAt(i, 0));

                if ( table.getValueAt(i, 3) == null ) {
                    messageLabel.setText("Enter Marks for Student with Roll No : " + table.getValueAt(i, 2));
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                }

                int marks = Integer.parseInt(table.getValueAt(i, 3).toString());
                if (marks > totalMarks) {
                    messageLabel.setText("Entered Marks for Student with Roll No : " + table.getValueAt(i, 2) + " is greater than Total Marks");
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                }
                scoreJsonObject.put("marks", marks);

                scoreJsonArray.put(scoreJsonObject);
            }

            examJsonObject.put("score", scoreJsonArray);

            int response = serverConnection.addNewExam(examJsonObject);
            if( response == 0 ){
                messageLabel.setText("Exam Data added to Database");
                Constraint.labelDeleteAfterTime(messageLabel);
                examNameSelector.clear();
                examDateDateChooser.setDate(null);
                totalMarksTextField.setText("");
                subjectComboBox.setSelectedItem(null);
                table.clear();
            }
        }
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

    protected void finalize(){
        getStudentListThread.stop();
    }
}