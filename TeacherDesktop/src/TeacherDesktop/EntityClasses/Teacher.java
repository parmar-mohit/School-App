package TeacherDesktop.EntityClasses;

import org.json.JSONObject;

public class Teacher {

    private final String firstname;
    private final String lastname;
    private String gender;
    private final String phone;
    private String email;

    public Teacher(JSONObject teacherJsonObject) {
        firstname = teacherJsonObject.getString("firstname");
        lastname = teacherJsonObject.getString("lastname");
        phone = teacherJsonObject.getString("phone");
        if (teacherJsonObject.has("gender")) {
            gender = teacherJsonObject.getString("gender");
        }
        if (teacherJsonObject.has("email")) {
            email = teacherJsonObject.getString("email");
        }
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        String teacherString = Character.toUpperCase(firstname.charAt(0)) + firstname.substring(1) + " ";
        teacherString += Character.toUpperCase(lastname.charAt(0)) + lastname.substring(1) + " ";
        teacherString += "(" + phone + ")";
        return teacherString;
    }
}
