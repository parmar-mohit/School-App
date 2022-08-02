package TeacherDesktop.Panel;

import TeacherDesktop.CardPanel.ExamCardPanel;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ExamPanel extends JPanel implements ActionListener {

    private final JLabel panelNameLabel;
    private final JButton newExamButton;
    private JButton backButton;
    private final JScrollPane scrollPane;
    private final JPanel examListPanel;
    private ArrayList<ExamCardPanel> examCardPanelArrayList;
    private NewExamPanel newExamPanel;
    private final ServerConnection serverConnection;
    private final String phone;
    private Thread fillExamCardThread;

    public ExamPanel(ServerConnection serverConnection, String phone) {
        //Initialising Members
        this.serverConnection = serverConnection;
        this.phone = phone;
        panelNameLabel = new JLabel("Exams");
        Image img = new ImageIcon(Constant.ADD_ICON).getImage();
        img = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        newExamButton = new JButton("New Exam", new ImageIcon(img));
        examListPanel = new JPanel();
        scrollPane = new JScrollPane(examListPanel);
        examCardPanelArrayList = new ArrayList<>();

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        newExamButton.setBackground(Constant.BUTTON_BACKGROUND);
        scrollPane.setMinimumSize(Constant.SCROLLPANE_SIZE);
        scrollPane.setPreferredSize(Constant.SCROLLPANE_SIZE);
        examListPanel.setLayout(new GridBagLayout());

        //Adding Listeners
        newExamButton.addActionListener(this);

        //Editing Panel
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panel
        add(panelNameLabel, Constraint.setPosition(0, 0, 2, 1));
        add(newExamButton, Constraint.setPosition(1, 1, Constraint.RIGHT));
        add(scrollPane, Constraint.setPosition(0, 2, 2, 1));

        //Filling Exam Card
        fillExamCard();
    }

    public void fillExamCard() {
        fillExamCardThread = new Thread() {
            @Override
            public void run() {
                if (examCardPanelArrayList.size() > 0) {
                    examListPanel.removeAll();
                }
                examCardPanelArrayList = new ArrayList<>();

                JLabel messageLabel = new JLabel("Getting Exam Details,Please Wait...");
                JProgressBar progressBar = new JProgressBar(0, 100);
                progressBar.setPreferredSize(new Dimension(500, 30));
                progressBar.setStringPainted(true);

                //Removing ScrollPane and adding progressbar
                remove(scrollPane);
                newExamButton.setVisible(false);
                add(messageLabel, Constraint.setPosition(0, 2, 2, 1));
                add(progressBar, Constraint.setPosition(0, 3, 2, 1));
                revalidate();
                repaint();

                JSONArray examJsonArray = serverConnection.getExamListForTeacher(phone, progressBar);

                //Removing ProgressBar and adding scrollpane
                remove(messageLabel);
                remove(progressBar);
                newExamButton.setVisible(true);
                add(scrollPane, Constraint.setPosition(0, 2, 2, 1));
                revalidate();
                repaint();

                for (int i = 0; i < examJsonArray.length(); i++) {
                    ExamCardPanel examCardPanel = new ExamCardPanel(examJsonArray.getJSONObject(i), serverConnection, ExamPanel.this);
                    examListPanel.add(examCardPanel, Constraint.setPosition(0, examCardPanelArrayList.size()));
                    examCardPanelArrayList.add(examCardPanel);
                    revalidate();
                    repaint();
                }
            }
        };
        fillExamCardThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newExamButton) {
            //Setting Components Invisible
            newExamButton.setVisible(false);
            scrollPane.setVisible(false);

            //Intialising Components
            backButton = new JButton("Back");
            newExamPanel = new NewExamPanel(serverConnection, phone);

            //Editing Components
            backButton.setBackground(Constant.BUTTON_BACKGROUND);

            //Adding Listeners
            backButton.addActionListener(this);

            //Adding Components to Panel
            add(backButton, Constraint.setPosition(1, 1, Constraint.RIGHT));
            add(newExamPanel, Constraint.setPosition(0, 2, 2, 1));
        } else if (e.getSource() == backButton) {
            //Removing Components
            remove(backButton);
            remove(newExamPanel);

            //Setting Components Visible
            newExamButton.setVisible(true);
            scrollPane.setVisible(true);

            fillExamCard();
        }
        revalidate();
        repaint();
    }

    protected void finalize() {
        fillExamCardThread.stop();
    }
}
