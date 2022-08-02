package TeacherDesktop.Dialog;

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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;

public class UpdateExamDialog extends JDialog implements ActionListener, KeyListener {
    private final JLabel panelNameLabel;
    private final JLabel examNameLabel;
    private final JLabel dateLabel;
    private final JLabel totalMarksLabel;
    private final JLabel subjectLabel;
    private final JLabel messageLabel;
    private final ExamNameSelector examNameSelector;
    private final JDateChooser examDateDateChooser;
    private final JTextField totalMarksTextField;
    private final JComboBox subjectComboBox;
    private final ServerConnection serverConnection;
    private ArrayList<String> examNameArrayList;
    private ArrayList<Subject> subjectArrayList;
    private final MarksTable table;
    private final JButton updateExam;
    private Thread getStudentListThread;
    private final JSONObject examJsonObject;

    public UpdateExamDialog(JFrame parent, JSONObject examJsonObject, JSONArray studentScoreJsonArray, ServerConnection serverConnection) {
        super(parent);
        //Initialising Members
        this.examJsonObject = examJsonObject;
        this.serverConnection = serverConnection;
        getInfo();
        panelNameLabel = new JLabel("Update Exam");
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
        updateExam = new JButton("Update Exam");

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        examNameSelector.setPreferredSize(new Dimension(180, 25));
        examDateDateChooser.setPreferredSize(new Dimension(180, 25));
        subjectComboBox.setPreferredSize(new Dimension(180, 25));
        updateExam.setBackground(Constant.BUTTON_BACKGROUND);
        updateExam.setPreferredSize(Constant.BUTTON_SIZE);

        //Setting Values
        examNameSelector.setExamName(examJsonObject.getString("exam_name"));
        examDateDateChooser.setDate(new Date(examJsonObject.getLong("date")));
        totalMarksTextField.setText(examJsonObject.getInt("total_marks") + "");
        for (int i = 0; i < subjectComboBox.getItemCount(); i++) {
            if (subjectComboBox.getItemAt(i).toString().equals(new Subject(examJsonObject.getJSONObject("subject")).toString())) {
                subjectComboBox.setSelectedIndex(i);
                break;
            }
        }
        table.setStudentListWithScore(studentScoreJsonArray);

        //Adding Listeners
        totalMarksTextField.addKeyListener(this);
        subjectComboBox.addActionListener(this);
        updateExam.addActionListener(this);

        //Editing Dialog
        setTitle("Edit Student");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.SCHOOL_LOGO));
        setVisible(true);
        setSize(new Dimension(1000, 600));
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Constant.CARD_PANEL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        //Adding Components to Panel
        add(panelNameLabel, Constraint.setPosition(0, 0, 4, 1));
        add(examNameLabel, Constraint.setPosition(0, 1, Constraint.RIGHT));
        add(examNameSelector, Constraint.setPosition(1, 1, Constraint.LEFT));
        add(dateLabel, Constraint.setPosition(2, 1, Constraint.RIGHT));
        add(examDateDateChooser, Constraint.setPosition(3, 1, Constraint.LEFT));
        add(totalMarksLabel, Constraint.setPosition(0, 2, Constraint.RIGHT));
        add(totalMarksTextField, Constraint.setPosition(1, 2, Constraint.LEFT));
        add(subjectLabel, Constraint.setPosition(2, 2, Constraint.RIGHT));
        add(subjectComboBox, Constraint.setPosition(3, 2, Constraint.LEFT));
        add(table.getScrollPane(), Constraint.setPosition(0, 3, 4, 1));
        add(messageLabel, Constraint.setPosition(0, 4, 4, 1));
        add(updateExam, Constraint.setPosition(0, 5, 4, 1));
        revalidate();
        repaint();
    }

    private void getInfo() {
        JSONObject jsonObject = serverConnection.getExamAndSubjectList(examJsonObject.getJSONObject("subject").getString("phone"));
        JSONArray examNameArray = jsonObject.getJSONArray("exam_name");
        examNameArrayList = new ArrayList<>();
        for (int i = 0; i < examNameArray.length(); i++) {
            examNameArrayList.add(examNameArray.getString(i));
        }

        JSONArray subjectListJsonArray = jsonObject.getJSONArray("subject_list");
        subjectArrayList = new ArrayList<>();
        for (int i = 0; i < subjectListJsonArray.length(); i++) {
            subjectArrayList.add(new Subject(subjectListJsonArray.getJSONObject(i)));
        }
    }

    private void fillSubjectComboBox() {
        for (int i = 0; i < subjectArrayList.size(); i++) {
            subjectComboBox.insertItemAt(subjectArrayList.get(i), 0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == subjectComboBox) {
            getStudentListThread = new Thread() {
                @Override
                public void run() {
                    Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();

                    if (selectedSubject != null) {
                        JLabel messageLabel = new JLabel("Getting Student Details,Please Wait...");
                        JProgressBar progressBar = new JProgressBar(0, 100);
                        progressBar.setStringPainted(true);
                        progressBar.setPreferredSize(new Dimension(500, 300));

                        remove(table.getScrollPane());
                        remove(UpdateExamDialog.this.messageLabel);
                        updateExam.setVisible(false);
                        add(messageLabel, Constraint.setPosition(0, 3, 4, 1));
                        add(progressBar, Constraint.setPosition(0, 4, 4, 1));
                        revalidate();
                        repaint();

                        JSONArray studentJsonArray = serverConnection.getStudentListForExam(selectedSubject.getStandard(), selectedSubject.getDivision(), progressBar);
                        table.setStudentList(studentJsonArray);

                        remove(messageLabel);
                        remove(progressBar);
                        updateExam.setVisible(true);
                        add(table.getScrollPane(), Constraint.setPosition(0, 3, 4, 1));
                        add(UpdateExamDialog.this.messageLabel, Constraint.setPosition(0, 4, 4, 1));
                        revalidate();
                        repaint();
                    }
                }
            };
            getStudentListThread.start();
        } else if (e.getSource() == updateExam) {
            JSONObject examJsonObject = new JSONObject();
            examJsonObject.put("exam_id", this.examJsonObject.getInt("exam_id"));

            String examName = examNameSelector.getExamName();
            if (examName.equals("")) {
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

                if (table.getValueAt(i, 3) == null || table.getValueAt(i, 3).toString().equals("")) {
                    messageLabel.setText("Enter Marks for Student with Roll No : " + table.getValueAt(i, 2));
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                }

                int marks;
                try {
                    marks = Integer.parseInt(table.getValueAt(i, 3).toString());
                } catch (Exception excp) {
                    messageLabel.setText("Marks for Roll No : " + table.getValueAt(i, 2) + " should be a Number");
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                }

                if (marks > totalMarks) {
                    messageLabel.setText("Entered Marks for Student with Roll No : " + table.getValueAt(i, 2) + " is greater than Total Marks");
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                }
                scoreJsonObject.put("marks", marks);

                scoreJsonArray.put(scoreJsonObject);
            }

            examJsonObject.put("score", scoreJsonArray);

            int response = serverConnection.updateExam(examJsonObject);
            if (response == 0) {
                messageLabel.setText("Exam Data Updated");
                Constraint.labelDeleteAfterTime(messageLabel);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (!Character.isDigit(e.getKeyChar())) {
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    protected void finalize() {
        getStudentListThread.stop();
    }
}