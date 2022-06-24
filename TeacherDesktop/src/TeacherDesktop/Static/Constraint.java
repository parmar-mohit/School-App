package TeacherDesktop.Static;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constraint {
    /* This Class provides static function that can be used to
    provide layout constraints for Gridbaglayout
    */

    public static final int LEFT = GridBagConstraints.WEST;
    public static final int RIGHT = GridBagConstraints.EAST;

    public static GridBagConstraints setPosition(int x, int y) {
        return new GridBagConstraints(x, y, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
    }

    public static GridBagConstraints setPosition(int x, int y, int xwidth, int ywidth) {
        return new GridBagConstraints(x, y, xwidth, ywidth, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
    }

    public static GridBagConstraints setPosition(int x, int y, int xwidth, int ywidth, int anchor) {
        return new GridBagConstraints(x, y, xwidth, ywidth, 0, 0, anchor, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
    }

    public static GridBagConstraints setPosition(int x, int y, int anchor) {
        return new GridBagConstraints(x, y, 1, 1, 0, 0, anchor, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
    }

    public static boolean isValidPassword(String password) {
        /* This Function returns a boolean value to indicate whether a password is Valid or not
        for a password to be valid it must contain one uppercase letter, one lowercase letter and one number,
         */
        boolean hasUpperCase = false, hasLowerCase = false, hasNumber = false;
        int i = 0;

        while (!(hasUpperCase && hasLowerCase && hasNumber) && i < password.length()) {
            if (Character.isUpperCase(password.charAt(i))) {
                hasUpperCase = true;
            }

            if (Character.isLowerCase(password.charAt(i))) {
                hasLowerCase = true;
            }

            if (Character.isDigit(password.charAt(i))) {
                hasNumber = true;
            }

            i++;
        }

        return (hasUpperCase && hasLowerCase && hasNumber);
    }

    public static String hashPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByte = digest.digest(passwordBytes);
            String hash = Base64.getEncoder().encodeToString(hashedByte);
            return hash;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static boolean emailCheck(String email){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void labelDeleteAfterTime(JLabel label){
        Timer t = new Timer(5000,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText(null);
            }
        });
        t.setRepeats(false);
        t.start();
    }
}

