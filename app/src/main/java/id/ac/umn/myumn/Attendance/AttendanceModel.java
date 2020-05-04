package id.ac.umn.myumn.Attendance;

public class AttendanceModel{



    private String subject;
    private String attendanceid;
    private String week;
    private String present;
    private String date;



    private AttendanceModel(String subject, String week, String present, String date, String attendanceid){
        this.subject = subject;
        this.attendanceid = attendanceid;
        this.week = week;
        this.present = present;
        this.date = date;
    }

    private AttendanceModel(){

    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAttendanceid() {
        return attendanceid;
    }

    public void setAttendanceid(String attendanceid) {
        this.attendanceid = attendanceid;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
