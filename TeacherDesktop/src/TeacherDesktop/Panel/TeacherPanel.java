package TeacherDesktop.Panel;

import TeacherDesktop.CardPanel.TeacherCardPanel;
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

public class TeacherPanel extends JPanel implements ActionListener {

    private JLabel panelNameLabel;
    private JScrollPane scrollPane;
    private JPanel teacherListPanel;
    private ArrayList<TeacherCardPanel> teacherCardPanelArrayList;
    private JButton createTeacherIdButton,backButton;
    private CreateTeacherPanel createTeacherPanel;
    private ServerConnection serverConnection;
    private Thread fillTeacherCardThread;

    public TeacherPanel(ServerConnection serverConnection){
        //Initialising Member Variables
        this.serverConnection = serverConnection;
        panelNameLabel = new JLabel("Teachers");
        teacherListPanel = new JPanel();
        teacherCardPanelArrayList = new ArrayList<>();
        scrollPane = new JScrollPane(teacherListPanel);
        Image img = new ImageIcon(Constant.ADD_ICON).getImage();
        img = img.getScaledInstance(30,30,Image.SCALE_DEFAULT);
        createTeacherIdButton = new JButton("Create Teacher Id",new ImageIcon(img));

        //Editing Members
        panelNameLabel.setFont(new Font("SansSerif", Font.BOLD,22));
        createTeacherIdButton.setBackground(Constant.BUTTON_BACKGROUND);
        teacherListPanel.setLayout(new GridBagLayout());
        scrollPane.setMinimumSize(Constant.SCROLLPANE_SIZE);
        scrollPane.setPreferredSize(Constant.SCROLLPANE_SIZE);

        //Adding Listeners
        createTeacherIdButton.addActionListener(this);

        //Editing Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panel
        add(panelNameLabel,Constraint.setPosition(0,0,2,1));
        add(createTeacherIdButton,Constraint.setPosition(1,1,Constraint.RIGHT));
        add(scrollPane, Constraint.setPosition(0,2,2,1));

        //Filling TeacherCards
        fillTeacherCard();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == createTeacherIdButton ){
            //Setting Components to Invisible
            scrollPane.setVisible(false);
            createTeacherIdButton.setVisible(false);

            //Instating createTeacherPanel and BackButton
            createTeacherPanel = new CreateTeacherPanel(serverConnection);
            backButton = new JButton("Back");

            //Editing Components
            backButton.setBackground(Constant.BUTTON_BACKGROUND);

            //Adding ActionListener
            backButton.addActionListener(this);

            //Adding Components to Frame
            add(backButton,Constraint.setPosition(1,1,Constraint.RIGHT));
            add(createTeacherPanel,Constraint.setPosition(0,2,2,1));
        }else if( e.getSource() == backButton ){
            //Setting Components to Visible
            scrollPane.setVisible(true);
            createTeacherIdButton.setVisible(true);

            //Removing Components
            remove(backButton);
            remove(createTeacherPanel);

            //Refreshing TeacherCardList
            fillTeacherCard();
        }
        revalidate();
        repaint();
    }

    public void fillTeacherCard() {
        fillTeacherCardThread = new Thread() {
            @Override
            public void run() {
                if (teacherCardPanelArrayList.size() > 0) {
                    teacherListPanel.removeAll();
                    teacherCardPanelArrayList = new ArrayList<>();
                }

                JLabel messageLabel = new JLabel("Getting Teacher Details, Please Wait...");
                JProgressBar progressBar = new JProgressBar(0,100);
                //removing Scrollpane from panel and adding progressbar to show progress while getting data
                remove(scrollPane);
                createTeacherIdButton.setVisible(false);
                add(messageLabel, Constraint.setPosition(0, 2,2,1));
                add(progressBar, Constraint.setPosition(0, 3,2,1));
                progressBar.setPreferredSize(new Dimension(500, 30));
                progressBar.setStringPainted(true);
                revalidate();
                repaint();

                JSONArray teacherJsonArray = serverConnection.getTeacherList(progressBar);

                //Removing progressBar and adding ScrollPane
                remove(messageLabel);
                remove(progressBar);
                createTeacherIdButton.setVisible(true);
                add(scrollPane, Constraint.setPosition(0, 2,2,1));
                revalidate();
                repaint();

                for (int i = 0; i < teacherJsonArray.length(); i++) {
                    JSONObject teacherJsonObject = teacherJsonArray.getJSONObject(i);
                    if (!teacherJsonObject.getString("phone").equals(Constant.PRINCIPAL_USERNAME)) {
                        TeacherCardPanel teacherCardPanel = new TeacherCardPanel(teacherJsonObject, serverConnection, TeacherPanel.this);
                        teacherCardPanel.setPreferredSize(new Dimension(900, 220));
                        teacherListPanel.add(teacherCardPanel, Constraint.setPosition(0, teacherCardPanelArrayList.size()));
                        teacherCardPanelArrayList.add(teacherCardPanel);
                        revalidate();
                        repaint();
                    }
                }
            }
        };
        fillTeacherCardThread.start();
    }

    protected void finalize(){
        if( fillTeacherCardThread != null ){
            fillTeacherCardThread.stop();
        }
    }
}
