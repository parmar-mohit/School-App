package TeacherDesktop.Dialog;

import TeacherDesktop.FileFilter.ImageFilter;
import TeacherDesktop.Server.ServerConnection;
import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;
import com.toedter.calendar.JDateChooser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.util.Date;

public class UpdateStudentDialog extends JDialog implements ActionListener, KeyListener {

    private final JLabel panelNameLabel;
    private final JLabel imageLabel;
    private final JLabel firstnameLabel;
    private final JLabel lastnameLabel;
    private final JLabel emailLabel;
    private final JLabel phoneLabel;
    private final JLabel genderLabel;
    private final JLabel dobLabel;
    private final JLabel standardLabel;
    private final JLabel divisionLabel;
    private final JLabel rollNoLabel;
    private final JLabel fatherFirstnameLabel;
    private final JLabel fatherLastnameLabel;
    private final JLabel fatherPhoneLabel;
    private final JLabel fatherEmailLabel;
    private final JLabel motherFirstnameLabel;
    private final JLabel motherLastnameLabel;
    private final JLabel motherPhoneLabel;
    private final JLabel motherEmailLabel;
    private final JLabel messageLabel;
    private final JTextField firstnameTextField;
    private final JTextField lastnameTextField;
    private final JTextField emailTextField;
    private final JTextField phoneTextField;
    private final JTextField rollNoTextField;
    private final JTextField fatherFirstnameTextField;
    private final JTextField fatherLastnameTextField;
    private final JTextField fatherPhoneTextField;
    private final JTextField fatherEmailTextField;
    private final JTextField motherFirstnameTextField;
    private final JTextField motherLastnameTextField;
    private final JTextField motherPhoneTextField;
    private final JTextField motherEmailTextField;
    private final JComboBox genderComboBox;
    private final JComboBox standardComboBox;
    private final JComboBox divisionComboBox;
    private final JDateChooser dobDateChooser;
    private final JButton selectImageButton;
    private final JButton updateStudentButton;
    private final JPanel imagePanel;
    private String studentImgLocation;
    private final ServerConnection serverConnection;
    private final JSONArray classroomArray;
    private JSONObject studentJsonObject;

    public UpdateStudentDialog(ServerConnection serverConnection, JSONArray classroomArray, JSONObject studentJsonObject, JFrame parent) {
        //Initialising Members
        super(parent);
        this.serverConnection = serverConnection;
        this.classroomArray = classroomArray;
        this.studentJsonObject = studentJsonObject;
        imagePanel = new JPanel();
        panelNameLabel = new JLabel("Update Student ID");
        firstnameLabel = new JLabel("Firstname : ");
        firstnameTextField = new JTextField(20);
        lastnameLabel = new JLabel("Lastname : ");
        lastnameTextField = new JTextField(20);
        emailLabel = new JLabel("Email : ");
        emailTextField = new JTextField(20);
        phoneLabel = new JLabel("Phone : ");
        phoneTextField = new JTextField(20);
        genderLabel = new JLabel("Gender : ");
        genderComboBox = new JComboBox(new String[]{"Male", "Female", "Other"});
        dobLabel = new JLabel("Date of Birth : ");
        dobDateChooser = new JDateChooser();
        standardLabel = new JLabel("Standard : ");
        standardComboBox = new JComboBox();
        divisionLabel = new JLabel("Division : ");
        divisionComboBox = new JComboBox();
        rollNoLabel = new JLabel("Roll No : ");
        rollNoTextField = new JTextField(20);
        fatherFirstnameLabel = new JLabel("Father's Firstname : ");
        fatherFirstnameTextField = new JTextField(20);
        fatherLastnameLabel = new JLabel("Father's Lastname : ");
        fatherLastnameTextField = new JTextField(20);
        fatherPhoneLabel = new JLabel("Father's Phone : ");
        fatherPhoneTextField = new JTextField(20);
        fatherEmailLabel = new JLabel("Father's Email : ");
        fatherEmailTextField = new JTextField(20);
        motherFirstnameLabel = new JLabel("Mother's Firstname : ");
        motherFirstnameTextField = new JTextField(20);
        motherLastnameLabel = new JLabel("Mother's Lastname : ");
        motherLastnameTextField = new JTextField(20);
        motherPhoneLabel = new JLabel("Mother's Phone : ");
        motherPhoneTextField = new JTextField(20);
        motherEmailLabel = new JLabel("Mother's Email : ");
        motherEmailTextField = new JTextField(20);
        messageLabel = new JLabel();
        updateStudentButton = new JButton("Update Student ID");

        //Filling ComboBoxes
        fillStandardComboBox();

        //Editing ImagePanel
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.setBackground(Constant.CARD_PANEL);
        studentImgLocation = null;
        imageLabel = new JLabel(getImage());
        selectImageButton = new JButton("Select Image");

        //Adding Components to ImagePanel
        imagePanel.add(imageLabel, Constraint.setPosition(0, 0));
        imagePanel.add(selectImageButton, Constraint.setPosition(0, 1));

        //Editing Components
        panelNameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        dobDateChooser.setPreferredSize(new Dimension(120, 25));
        standardComboBox.setPreferredSize(new Dimension(120, 25));
        divisionComboBox.setPreferredSize(new Dimension(120, 25));
        updateStudentButton.setBackground(Constant.BUTTON_BACKGROUND);
        updateStudentButton.setPreferredSize(Constant.BUTTON_SIZE);

        //AddingListeners
        selectImageButton.addActionListener(this);
        phoneTextField.addKeyListener(this);
        standardComboBox.addActionListener(this);
        rollNoTextField.addKeyListener(this);
        fatherPhoneTextField.addKeyListener(this);
        motherPhoneTextField.addKeyListener(this);
        updateStudentButton.addActionListener(this);

        //Setting Values in InputFields
        firstnameTextField.setText(studentJsonObject.getString("firstname"));
        lastnameTextField.setText(studentJsonObject.getString("lastname"));
        if (!studentJsonObject.getString("email").equals("null")) {
            emailTextField.setText(studentJsonObject.getString("email"));
        }
        if (!studentJsonObject.getString("phone").equals("null")) {
            phoneTextField.setText(studentJsonObject.getString("phone"));
        }
        genderComboBox.setSelectedItem(studentJsonObject.getString("gender"));
        dobDateChooser.setDate(new Date(studentJsonObject.getLong("dob")));
        standardComboBox.setSelectedItem(studentJsonObject.getInt("standard"));
        divisionComboBox.setSelectedItem(studentJsonObject.getString("division"));
        rollNoTextField.setText(studentJsonObject.getInt("roll_no") + "");
        if (!studentJsonObject.getString("father_firstname").equals("null")) {
            fatherFirstnameTextField.setText(studentJsonObject.getString("father_firstname"));
            fatherLastnameTextField.setText(studentJsonObject.getString("father_lastname"));
            fatherPhoneTextField.setText(studentJsonObject.getString("father_phone"));
        }
        if (!studentJsonObject.getString("father_email").equals("null")) {
            fatherEmailTextField.setText(studentJsonObject.getString("father_email"));
        }

        if (!studentJsonObject.getString("mother_firstname").equals("null")) {
            motherFirstnameTextField.setText(studentJsonObject.getString("mother_firstname"));
            motherLastnameTextField.setText(studentJsonObject.getString("mother_lastname"));
            motherPhoneTextField.setText(studentJsonObject.getString("mother_phone"));
        }
        if (!studentJsonObject.getString("mother_email").equals("null")) {
            motherEmailTextField.setText(studentJsonObject.getString("mother_email"));
        }

        //Editing Dialog
        setTitle("Edit Student");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.SCHOOL_LOGO));
        setVisible(true);
        setSize(new Dimension(1000, 600));
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Constant.CARD_PANEL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        //Adding Components to Frame
        add(panelNameLabel, Constraint.setPosition(0, 0, 5, 1));
        add(imagePanel, Constraint.setPosition(0, 1, 1, 9));
        add(firstnameLabel, Constraint.setPosition(1, 1, Constraint.LEFT));
        add(firstnameTextField, Constraint.setPosition(2, 1, Constraint.LEFT));
        add(lastnameLabel, Constraint.setPosition(3, 1, Constraint.LEFT));
        add(lastnameTextField, Constraint.setPosition(4, 1, Constraint.LEFT));
        add(emailLabel, Constraint.setPosition(1, 2, Constraint.LEFT));
        add(emailTextField, Constraint.setPosition(2, 2, Constraint.LEFT));
        add(phoneLabel, Constraint.setPosition(3, 2, Constraint.LEFT));
        add(phoneTextField, Constraint.setPosition(4, 2, Constraint.LEFT));
        add(genderLabel, Constraint.setPosition(1, 3, Constraint.LEFT));
        add(genderComboBox, Constraint.setPosition(2, 3, Constraint.LEFT));
        add(dobLabel, Constraint.setPosition(3, 3, Constraint.LEFT));
        add(dobDateChooser, Constraint.setPosition(4, 3, Constraint.LEFT));
        add(standardLabel, Constraint.setPosition(1, 4, Constraint.LEFT));
        add(standardComboBox, Constraint.setPosition(2, 4, Constraint.LEFT));
        add(divisionLabel, Constraint.setPosition(3, 4, Constraint.LEFT));
        add(divisionComboBox, Constraint.setPosition(4, 4, Constraint.LEFT));
        add(rollNoLabel, Constraint.setPosition(1, 5, Constraint.LEFT));
        add(rollNoTextField, Constraint.setPosition(2, 5, Constraint.LEFT));
        add(fatherFirstnameLabel, Constraint.setPosition(1, 6, Constraint.LEFT));
        add(fatherFirstnameTextField, Constraint.setPosition(2, 6, Constraint.LEFT));
        add(fatherLastnameLabel, Constraint.setPosition(3, 6, Constraint.LEFT));
        add(fatherLastnameTextField, Constraint.setPosition(4, 6, Constraint.LEFT));
        add(fatherPhoneLabel, Constraint.setPosition(1, 7, Constraint.LEFT));
        add(fatherPhoneTextField, Constraint.setPosition(2, 7, Constraint.LEFT));
        add(fatherEmailLabel, Constraint.setPosition(3, 7, Constraint.LEFT));
        add(fatherEmailTextField, Constraint.setPosition(4, 7, Constraint.LEFT));
        add(motherFirstnameLabel, Constraint.setPosition(1, 8, Constraint.LEFT));
        add(motherFirstnameTextField, Constraint.setPosition(2, 8, Constraint.LEFT));
        add(motherLastnameLabel, Constraint.setPosition(3, 8, Constraint.LEFT));
        add(motherLastnameTextField, Constraint.setPosition(4, 8, Constraint.LEFT));
        add(motherPhoneLabel, Constraint.setPosition(1, 9, Constraint.LEFT));
        add(motherPhoneTextField, Constraint.setPosition(2, 9, Constraint.LEFT));
        add(motherEmailLabel, Constraint.setPosition(3, 9, Constraint.LEFT));
        add(motherEmailTextField, Constraint.setPosition(4, 9, Constraint.LEFT));
        add(messageLabel, Constraint.setPosition(0, 10, 5, 1));
        add(updateStudentButton, Constraint.setPosition(0, 11, 5, 1));
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectImageButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setDialogTitle("Select Image of Student");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(new ImageFilter());

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                studentImgLocation = selectedFile.getPath();
                Image img = new ImageIcon(studentImgLocation).getImage();
                img = img.getScaledInstance(150, 150, Image.SCALE_DEFAULT);
                imageLabel.setIcon(new ImageIcon(img));
            }
        } else if (e.getSource() == standardComboBox) {
            fillDivisionComboBox();
        } else if (e.getSource() == updateStudentButton) {
            String firstname = firstnameTextField.getText();
            if (firstname.equals("")) {
                messageLabel.setText("Enter Firstname");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            JSONObject studentJsonObject = new JSONObject();
            studentJsonObject.put("sid", this.studentJsonObject.getInt("sid"));
            studentJsonObject.put("firstname", firstname.toLowerCase());

            String lastname = lastnameTextField.getText();
            if (lastname.equals("")) {
                messageLabel.setText("Enter Lastname");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            studentJsonObject.put("lastname", lastname.toLowerCase());

            String email = emailTextField.getText();
            if (email.equals("")) {
                studentJsonObject.put("email", "null");
            } else {
                if (!Constraint.emailCheck(email)) {
                    messageLabel.setText("Enter a Valid Email");
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                } else {
                    studentJsonObject.put("email", email);
                }
            }

            String phone = phoneTextField.getText();
            if (phone.equals("")) {
                studentJsonObject.put("phone", "null");
            } else {
                if (phone.length() < 10) {
                    messageLabel.setText("Phone No should be 10 digits");
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                } else {
                    studentJsonObject.put("phone", phone);
                }
            }

            String gender = (String) genderComboBox.getSelectedItem();
            studentJsonObject.put("gender", gender);


            Date dob = dobDateChooser.getDate();
            if (dob == null) {
                messageLabel.setText("Select Date of Birth");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            studentJsonObject.put("dob", dob.getTime());

            if (standardComboBox.getSelectedItem() == null) {
                messageLabel.setText("Select Standard");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            studentJsonObject.put("standard", (int) standardComboBox.getSelectedItem());

            if (divisionComboBox.getSelectedItem() == null) {
                messageLabel.setText("Select Division");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            studentJsonObject.put("division", divisionComboBox.getSelectedItem());

            if (rollNoTextField.getText().equals("")) {
                messageLabel.setText("Enter Roll No");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            }
            studentJsonObject.put("roll_no", Integer.parseInt(rollNoTextField.getText()));

            String fatherFirstname = fatherFirstnameTextField.getText();
            String fatherLastname = fatherLastnameTextField.getText();
            String fatherPhone = fatherPhoneTextField.getText();
            String fatherEmail = fatherEmailTextField.getText();
            studentJsonObject.put("father_old_phone", this.studentJsonObject.getString("father_phone"));
            if (fatherFirstname.equals("") && fatherLastname.equals("") && fatherPhone.equals("") && fatherEmail.equals("")) {
                studentJsonObject.put("father_firstname", "null");
                studentJsonObject.put("father_lastname", "null");
                studentJsonObject.put("father_new_phone", "null");
                studentJsonObject.put("father_email", "null");
            } else if (fatherFirstname.equals("")) {
                messageLabel.setText("Enter Father's Firstname");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            } else if (fatherLastname.equals("")) {
                messageLabel.setText("Enter Father's Lastname");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            } else if (fatherPhone.equals("")) {
                messageLabel.setText("Enter Father's Phone");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            } else if (fatherPhone.length() < 10) {
                messageLabel.setText("Father's Phone must be 10 digits");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            } else {
                studentJsonObject.put("father_firstname", fatherFirstname.toLowerCase());
                studentJsonObject.put("father_lastname", fatherLastname.toLowerCase());
                studentJsonObject.put("father_new_phone", fatherPhone);

                if (fatherEmail.equals("")) {
                    studentJsonObject.put("father_email", "null");
                } else {
                    if (!Constraint.emailCheck(fatherEmail)) {
                        messageLabel.setText("Enter a valid Father Email");
                        Constraint.labelDeleteAfterTime(messageLabel);
                        return;
                    } else {
                        studentJsonObject.put("father_email", fatherEmail.toLowerCase());
                    }
                }
            }


            String motherFirstname = motherFirstnameTextField.getText();
            String motherLastname = motherLastnameTextField.getText();
            String motherPhone = motherPhoneTextField.getText();
            String motherEmail = motherEmailTextField.getText();
            studentJsonObject.put("mother_old_phone", this.studentJsonObject.getString("mother_phone"));
            if (motherFirstname.equals("") && motherLastname.equals("") && motherPhone.equals("") && motherEmail.equals("")) {
                studentJsonObject.put("mother_firstname", "null");
                studentJsonObject.put("mother_lastname", "null");
                studentJsonObject.put("mother_new_phone", "null");
                studentJsonObject.put("mother_email", "null");
            } else if (motherFirstname.equals("")) {
                messageLabel.setText("Enter Mother's Firstname");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            } else if (motherLastname.equals("")) {
                messageLabel.setText("Enter Mother's Lastname");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            } else if (motherPhone.equals("")) {
                messageLabel.setText("Enter Mother's Phone");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            } else if (motherPhone.length() < 10) {
                messageLabel.setText("Mother' Phone must be 10 digits");
                Constraint.labelDeleteAfterTime(messageLabel);
                return;
            } else {
                studentJsonObject.put("mother_firstname", motherFirstname.toLowerCase());
                studentJsonObject.put("mother_lastname", motherLastname.toLowerCase());
                studentJsonObject.put("mother_new_phone", motherPhone);

                if (motherEmail.equals("")) {
                    studentJsonObject.put("mother_email", "null");
                } else {
                    if (!Constraint.emailCheck(motherEmail)) {
                        messageLabel.setText("Enter a valid Mother Email");
                        Constraint.labelDeleteAfterTime(messageLabel);
                        return;
                    } else {
                        studentJsonObject.put("mother_email", motherEmail.toLowerCase());
                    }
                }
            }


            if (studentImgLocation != null) {
                try {
                    BufferedImage studentImg = ImageIO.read(new File(studentImgLocation));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(studentImg, "jpg", baos);
                    byte[] imgArray = baos.toByteArray();
                    String studentImgString = Base64.getEncoder().encodeToString(imgArray);
                    studentJsonObject.put("img", studentImgString);
                } catch (Exception excp) {
                    messageLabel.setText("Some Error Occurred Please try Again");
                    Constraint.labelDeleteAfterTime(messageLabel);
                    return;
                }
            }

            messageLabel.setText("Updating Student Id,Please Wait");
            int responseCode = serverConnection.updateStudentId(studentJsonObject);
            if (responseCode == 0) {
                messageLabel.setText("Student Id Updated");
                Constraint.labelDeleteAfterTime(messageLabel);
                if (!studentJsonObject.has("img")) {
                    studentJsonObject.put("img", this.studentJsonObject.getString("img"));
                }
                studentJsonObject.remove("father_old_phone");
                studentJsonObject.remove("mother_old_phone");
                studentJsonObject.put("father_phone", studentJsonObject.getString("father_new_phone"));
                studentJsonObject.put("mother_phone", studentJsonObject.getString("mother_new_phone"));
                studentJsonObject.remove("father_new_phone");
                studentJsonObject.remove("mother_new_phone");
                this.studentJsonObject = studentJsonObject;
            }
        }
    }

    private void fillStandardComboBox() {
        for (int i = 0; i < classroomArray.length(); i++) {
            standardComboBox.insertItemAt(classroomArray.getJSONObject(i).getInt("standard"), i);
        }
    }

    private void fillDivisionComboBox() {
        int standard = (int) standardComboBox.getSelectedItem();

        divisionComboBox.removeAllItems();

        for (int i = 0; i < classroomArray.length(); i++) {
            JSONObject standardJsonObject = classroomArray.getJSONObject(i);
            if (standardJsonObject.getInt("standard") == standard) {
                JSONArray divisionArray = standardJsonObject.getJSONArray("division");
                for (int j = 0; j < divisionArray.length(); j++) {
                    divisionComboBox.insertItemAt(divisionArray.getString(j), j);
                }
                break;
            }
        }
    }

    private ImageIcon getImage() {
        byte[] imgArray = Base64.getDecoder().decode(studentJsonObject.getString("img"));
        ByteArrayInputStream bais = new ByteArrayInputStream(imgArray);
        Image studentImg = new ImageIcon(Constant.MALE_AVATAR).getImage();
        try {
            studentImg = ImageIO.read(bais);
        } catch (Exception e) {
            e.printStackTrace();
        }
        studentImg = studentImg.getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        return new ImageIcon(studentImg);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (!Character.isDigit(e.getKeyChar()) || ((JTextField) e.getSource()).getText().length() > 9) {
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
