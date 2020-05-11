package id.ac.umn.myumn.Dashboard;

public class DashboardModel {

    private String subject;
    private String title;
    private String date;
    private String time;

    public DashboardModel(String subject, String title, String date, String time) {
        this.subject = subject;
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public DashboardModel() {

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
