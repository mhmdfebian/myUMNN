package id.ac.umn.myumn.Course;

public class CourseModel{

    private String subject;
    private String courseid;
    private String title;
    private String topic;

    private CourseModel(String subject, String courseid, String title, String topic){
        this.subject = subject;
        this.courseid = courseid;
        this.title = title;
        this.topic = topic;
    }

    private CourseModel(){

    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
