package TeacherDesktop.EntityClasses;

import org.json.JSONObject;

public class Subject {
    private int subjectId;
    private String subjectName;
    private int standard;
    private String division;
    private String phone;

    public Subject(JSONObject subjectJsonObject){
        subjectId = subjectJsonObject.getInt("subject_id");
        subjectName = subjectJsonObject.getString("subject_name");
        standard = subjectJsonObject.getInt("standard");
        division = subjectJsonObject.getString("division");
        phone = subjectJsonObject.getString("phone");
    }

    public static String getSubjectName(String subjectName) {
        String[] subjectNameArray = subjectName.split(" ");
        String subjectNameString = new String();
        for( int i = 0; i < subjectNameArray.length; i++ ){
            subjectNameString += Character.toUpperCase(subjectNameArray[i].charAt(0)) + subjectNameArray[i].substring(1);
        }
        return subjectNameString;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getStandard() {
        return standard;
    }

    public String getDivision() {
        return division;
    }

    @Override
    public String toString() {
        String subjectString = getSubjectName(subjectName);
        subjectString += "("+standard+":"+division+")";
        return subjectString;
    }
}
