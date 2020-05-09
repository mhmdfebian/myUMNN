package id.ac.umn.myumn.Enrollment;

public class EnrollmentModel {

    private String subject;
    private String time;
    private String timeend;
    private String day;
    private String courseid;

    public EnrollmentModel(String subject, String time, String timeend, String day, String courseid) {
        this.subject = subject;
        this.time = time;
        this.timeend = timeend;
        this.day = day;
        this.courseid = courseid;
    }
    public EnrollmentModel(){

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }
}
