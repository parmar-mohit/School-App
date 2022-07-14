package TeacherDesktop.Panel;

import TeacherDesktop.CardPanel.StudentCardPanel;
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

public class MyClassroomPanel extends JPanel implements ActionListener {

    private JLabel panelNameLabel;
    private JButton createStudentIdButton,backButton;
    private CreateStudentPanel studentPanel;
    private JScrollPane scrollPane;
    private JPanel studentListPanel;
    private ArrayList<StudentCardPanel> studentCardPanelArrayList;
    private ServerConnection serverConnection;
    private String phone;
    private JSONArray classroomJsonArray;
    private Thread fillStudentCardThread;

    public MyClassroomPanel(ServerConnection serverConnection,String phone,JSONArray classroomJsonArray){
        //Initialising Members
        this.serverConnection = serverConnection;
        this.phone = phone;
        this.classroomJsonArray = classroomJsonArray;
        panelNameLabel = new JLabel("My Classroom");
        Image img = new ImageIcon(Constant.ADD_ICON).getImage();
        img = img.getScaledInstance(30,30,Image.SCALE_DEFAULT);
        createStudentIdButton = new JButton("Create Student Id",new ImageIcon(img));
        studentListPanel = new JPanel();
        scrollPane = new JScrollPane(studentListPanel);
        studentCardPanelArrayList = new ArrayList<>();

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif",Font.BOLD,22));
        createStudentIdButton.setBackground(Constant.BUTTON_BACKGROUND);
        scrollPane.setMinimumSize(Constant.SCROLLPANE_SIZE);
        scrollPane.setPreferredSize(Constant.SCROLLPANE_SIZE);
        studentListPanel.setLayout(new GridBagLayout());

        //Adding Listeners
        createStudentIdButton.addActionListener(this);

        //Editing Panel
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Frame
        add(panelNameLabel, Constraint.setPosition(0,0,2,1));
        add(createStudentIdButton,Constraint.setPosition(1,1,Constraint.RIGHT));
        add(scrollPane,Constraint.setPosition(0,2,2,1));
        revalidate();
        repaint();

        //Filling Student Card
        fillStudentCard();
    }
    public void fillStudentCard(){
        fillStudentCardThread = new Thread() {
            @Override
            public void run() {
                if (studentCardPanelArrayList.size() > 0) {
                    studentListPanel.removeAll();
                    studentCardPanelArrayList = new ArrayList<>();
                }

                JLabel messageLabel = new JLabel("Getting Student Details, Please Wait..");
                JProgressBar progressBar = new JProgressBar(0, 100);

                //removing Scrollpane from panel and adding progressbar to show progress while getting data
                remove(scrollPane);
                createStudentIdButton.setVisible(false);
                add(messageLabel, Constraint.setPosition(0, 2, 2, 1));
                add(progressBar, Constraint.setPosition(0, 3, 2, 1));
                progressBar.setPreferredSize(new Dimension(500, 30));
                progressBar.setStringPainted(true);
                revalidate();
                repaint();

                JSONArray studentListJsonArray = serverConnection.getStudentListForClassroomIncharge(phone, progressBar);

                //Removing progressBar and adding ScrollPane
                remove(messageLabel);
                remove(progressBar);
                createStudentIdButton.setVisible(true);
                add(scrollPane, Constraint.setPosition(0, 2, 2, 1));
                revalidate();
                repaint();

                for (int i = 0; i < studentListJsonArray.length(); i++) {
                    JSONObject studentJsonObject = studentListJsonArray.getJSONObject(i);
                    StudentCardPanel studentCardPanel = new StudentCardPanel(studentJsonObject, serverConnection, classroomJsonArray, MyClassroomPanel.this);
                    studentCardPanel.setPreferredSize(new Dimension(900, 350));
                    studentListPanel.add(studentCardPanel, Constraint.setPosition(0, studentCardPanelArrayList.size()));
                    studentCardPanelArrayList.add(studentCardPanel);
                    revalidate();
                    repaint();
                }
            }
        };
        fillStudentCardThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == createStudentIdButton ){
            //Setting Components Invisible
            createStudentIdButton.setVisible(false);
            scrollPane.setVisible(false);

            //Creating new Components
            backButton = new JButton("Back");
            studentPanel = new CreateStudentPanel(serverConnection,classroomJsonArray);

            //Editing Components
            backButton.setBackground(Constant.BUTTON_BACKGROUND);
            studentPanel.setBackground(Constant.PANEL_BACKGROUND);

            //Adding Listeners
            backButton.addActionListener(this);

            //Adding Components to Panel
            add(backButton, Constraint.setPosition(1, 1, Constraint.RIGHT));
            add(studentPanel, Constraint.setPosition(0, 2, 2, 1));
        }else if( e.getSource() == backButton ){
            //Setting Components Visible
            createStudentIdButton.setVisible(true);
            scrollPane.setVisible(true);

            //Removing Components from panel
            remove(backButton);
            remove(studentPanel);

            fillStudentCard();
        }
        revalidate();
        repaint();
    }

    protected void finalize(){
        if( fillStudentCardThread != null ){
            fillStudentCardThread.stop();
        }
    }
}
