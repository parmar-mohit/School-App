package TeacherDesktop.CardPanel;

import TeacherDesktop.CustomComponents.ExamNameSelector;
import TeacherDesktop.CustomComponents.ScoreViewMarksTable;
import TeacherDesktop.Dialog.UpdateExamDialog;
import TeacherDesktop.EntityClasses.Subject;
import TeacherDesktop.Panel.ExamPanel;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamCardPanel extends JPanel implements ActionListener, WindowListener {

    private JLabel examNameLabel,examDateLabel,totalMarksLabel,subjectLabel;
    private JButton expandButton,collapseButton,updateButton,deleteButton;
    private ScoreViewMarksTable table;
    private JSONObject examJsonObject;

    private ServerConnection serverConnection;
    private ExamPanel parent;
    private JSONArray studentScoreJsonArray;

    public ExamCardPanel(JSONObject examJsonObject, ServerConnection serverConnection, ExamPanel parent){
        //Initialising Member Variables
        this.examJsonObject = examJsonObject;
        this.serverConnection = serverConnection;
        this.parent = parent;
        examNameLabel = new JLabel("Exam Name : "+ ExamNameSelector.formatExamName(examJsonObject.getString("exam_name")));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        examDateLabel = new JLabel("Exam Date : "+sdf.format(new Date(examJsonObject.getLong("date"))));
        Image img = new ImageIcon(Constant.EXPAND_ICON).getImage();
        img = img.getScaledInstance(15,15,Image.SCALE_DEFAULT);
        expandButton = new JButton(new ImageIcon(img));
        totalMarksLabel = new JLabel("Total Marks : "+examJsonObject.getInt("total_marks"));
        subjectLabel = new JLabel("Subject : "+new Subject(examJsonObject.getJSONObject("subject")));

        //Editing Components
        expandButton.setBackground(Constant.CARD_PANEL);

        //Adding Listeners
        expandButton.addActionListener(this);

        //Editing Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.CARD_PANEL);
        setPreferredSize(new Dimension(900,100));

        //Adding Components to Panel
        add(examNameLabel, Constraint.setPosition(0,0,Constraint.LEFT));
        add(examDateLabel,Constraint.setPosition(1,0,Constraint.LEFT));
        add(expandButton,Constraint.setPosition(2,0,1,2));
        add(totalMarksLabel,Constraint.setPosition(0,1,Constraint.LEFT));
        add(subjectLabel,Constraint.setPosition(1,1,Constraint.LEFT));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == expandButton ){
            new Thread() {
                @Override
                public void run() {
                    //Setting Expand Button Invisible
                    expandButton.setVisible(false);

                    //Creating Components
                    Image img = new ImageIcon(Constant.COLLAPSE_ICON).getImage();
                    img = img.getScaledInstance(15, 15, Image.SCALE_DEFAULT);
                    collapseButton = new JButton(new ImageIcon(img));
                    table = new ScoreViewMarksTable();
                    updateButton = new JButton("Update");
                    deleteButton = new JButton("Delete");

                    //Editing Components
                    collapseButton.setBackground(Constant.CARD_PANEL);
                    deleteButton.setBackground(Color.RED);

                    //Creating Temporary Component
                    JLabel messageLabel = new JLabel("Getting Score, Please Wait...");
                    JProgressBar progressBar = new JProgressBar(0, 100);
                    progressBar.setPreferredSize(new Dimension(500, 30));

                    //Adding Temporary Components on Screen
                    add(messageLabel, Constraint.setPosition(0, 2, 2, 1));
                    add(progressBar, Constraint.setPosition(0, 3, 2, 1));
                    revalidate();
                    repaint();
                    setPreferredSize(new Dimension(900, 500));

                    studentScoreJsonArray = serverConnection.getScoreOfExam(examJsonObject.getInt("exam_id"), progressBar);
                    table.setStudentScoreList(studentScoreJsonArray);

                    //Removing Temporary Object
                    remove(messageLabel);
                    remove(progressBar);
                    revalidate();
                    repaint();

                    //Adding Listeners to Component
                    collapseButton.addActionListener(ExamCardPanel.this);
                    updateButton.addActionListener(ExamCardPanel.this);
                    deleteButton.addActionListener(ExamCardPanel.this);

                    //Adding Components on Panel
                    add(collapseButton, Constraint.setPosition(2, 0, 1, 2));
                    add(table.getScrollPane(), Constraint.setPosition(0, 2, 2, 1));
                    add(updateButton,Constraint.setPosition(0,3));
                    add(deleteButton,Constraint.setPosition(1,3));
                    setPreferredSize(new Dimension(900, 450));
                }
            }.start();
        }else if( e.getSource() == collapseButton ){
            //Removing Components
            remove(collapseButton);
            remove(table.getScrollPane());
            remove(updateButton);
            remove(deleteButton);

            //Setting expandButtonVisible
            expandButton.setVisible(true);

            //Other Changes
            setPreferredSize(new Dimension(900,100));
        }else if( e.getSource() == updateButton ){
            JDialog dialog = new UpdateExamDialog((JFrame)SwingUtilities.getWindowAncestor(this),examJsonObject,studentScoreJsonArray,serverConnection);
            dialog.addWindowListener(this);
        }else if( e.getSource() == deleteButton ){
            int result = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete this exam ?");

            if( result == JOptionPane.YES_OPTION ){
                int response = serverConnection.deleteExam(examJsonObject.getInt("exam_id"));
                if( response == 0 ){
                    parent.fillExamCard();
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
        parent.fillExamCard();
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
