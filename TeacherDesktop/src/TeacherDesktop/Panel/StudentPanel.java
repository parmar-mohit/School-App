package TeacherDesktop.Panel;

import TeacherDesktop.CardPanel.StudentCardPanel;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StudentPanel extends JPanel {

    private final JLabel panelNameLabel;
    private CreateStudentPanel studentPanel;
    private final JScrollPane scrollPane;
    private final JPanel studentListPanel;
    private ArrayList<StudentCardPanel> studentCardPanelArrayList;
    private final ServerConnection serverConnection;
    private final String phone;
    private Thread fillStudentCardThread;

    public StudentPanel(ServerConnection serverConnection, String phone) {
        //Initialising Members
        this.serverConnection = serverConnection;
        this.phone = phone;
        panelNameLabel = new JLabel("Students");
        studentListPanel = new JPanel();
        scrollPane = new JScrollPane(studentListPanel);
        studentCardPanelArrayList = new ArrayList<>();

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        scrollPane.setMinimumSize(new Dimension(1000, 500));
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        studentListPanel.setLayout(new GridBagLayout());

        //Filling Student Card
        fillStudentCard();

        //Editing Panel
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Frame
        add(panelNameLabel, Constraint.setPosition(0, 0));
        add(scrollPane, Constraint.setPosition(0, 2));
    }

    private void fillStudentCard() {
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
                add(messageLabel, Constraint.setPosition(0, 2));
                add(progressBar, Constraint.setPosition(0, 3));
                progressBar.setPreferredSize(new Dimension(500, 30));
                progressBar.setStringPainted(true);
                revalidate();
                repaint();

                JSONArray studentListJsonArray = serverConnection.getStudentListForSubjectTeacher(phone, progressBar);

                //Removing progressBar and adding ScrollPane
                remove(messageLabel);
                remove(progressBar);
                add(scrollPane, Constraint.setPosition(0, 2));
                revalidate();
                repaint();

                for (int i = 0; i < studentListJsonArray.length(); i++) {
                    JSONObject studentJsonObject = studentListJsonArray.getJSONObject(i);
                    StudentCardPanel studentCardPanel = new StudentCardPanel(studentJsonObject);
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

    protected void finalize() {
        if (fillStudentCardThread != null) {
            fillStudentCardThread.stop();
        }
    }
}
