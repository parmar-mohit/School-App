package TeacherDesktop.CustomComponents;

import TeacherDesktop.Static.Constant;
import TeacherDesktop.Static.Constraint;

import javax.swing.*;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ExamNameSelector extends JPanel implements ActionListener {
    
    private JTextField examNameTextField;
    private JComboBox examNameComboBox;
    private JButton swapButton;
    
    public ExamNameSelector(){
        //Initialising Component
        examNameTextField = new JTextField(15);
        examNameComboBox = new JComboBox();
        Image img = new ImageIcon(Constant.SWAP_ICON).getImage();
        img = img.getScaledInstance(25,25,Image.SCALE_DEFAULT);
        swapButton = new JButton(new ImageIcon(img));

        //Editing Components
        examNameTextField.setMinimumSize(new Dimension(150,25));
        examNameTextField.setPreferredSize(new Dimension(150,25));
        examNameTextField.setMaximumSize(new Dimension(150,25));
        examNameComboBox.setMinimumSize(new Dimension(150,25));
        examNameComboBox.setPreferredSize(new Dimension(150,25));
        examNameComboBox.setMaximumSize(new Dimension(150,25));
        examNameComboBox.setVisible(false);
        swapButton.setMinimumSize(new Dimension(25,25));
        swapButton.setPreferredSize(new Dimension(25,25));
        swapButton.setMaximumSize(new Dimension(25,25));

        //Adding Listeners
        examNameComboBox.addActionListener(this);
        swapButton.addActionListener(this);


        //Editing Panel Details
        setLayout(new GridBagLayout());
        setBackground(Constant.PANEL_BACKGROUND);

        //Adding Components to Panels
        add(examNameTextField,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(5,5,5,0),5,5));
        add(examNameComboBox,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(5,5,5,0),5,5));
        add(swapButton,new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(5,0,5,5),5,5));
    }

    public void setExamList(ArrayList<String> examNameArrayList){
        examNameComboBox.removeAllItems();

        for( int i = 0; i < examNameArrayList.size(); i++ ){
            examNameComboBox.insertItemAt( formatExamName(examNameArrayList.get(i)),0);
        }
    }

    public String getExamName(){
        return examNameTextField.getText();
    }

    public void clear(){
        examNameTextField.setText("");
        examNameComboBox.setSelectedItem(null);
    }

    private String formatExamName(String examName){
        String examNameSplitArray[] = examName.split(" ");
        String exam = new String();
        for( int i = 0; i < examNameSplitArray.length; i++){
            String currentWord = examNameSplitArray[i];
            exam += Character.toUpperCase(currentWord.charAt(0)) + currentWord.substring(1)+ " ";
        }

        return exam.substring(0,exam.length()-1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == swapButton ){
            if( examNameTextField.isVisible() ){
                examNameTextField.setVisible(false);
                examNameComboBox.setVisible(true);
            }else{
                examNameTextField.setVisible(true);
                examNameComboBox.setVisible(false);
            }
            revalidate();
            repaint();
        }else if( e.getSource() == examNameComboBox ) {
            if (examNameComboBox.getSelectedItem() != null) {
                examNameTextField.setText(examNameComboBox.getSelectedItem().toString());
            }
        }
    }
}
