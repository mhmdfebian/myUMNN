package id.ac.umn.myumn.Event;

public class EventModel {

    private String eventdate;
    private String eventdesc;
    private String eventlocation;
    private String eventname;
    private String eventtime;
    private String eventid;

    private EventModel(String eventdate, String eventdesc, String eventlocation, String eventname, String eventtime, String eventid) {
        this.eventdate = eventdate;
        this.eventdesc = eventdesc;
        this.eventlocation = eventlocation;
        this.eventname = eventname;
        this.eventtime = eventtime;
        this.eventid = eventid;
    }

    private EventModel() {

    }

    public String getEventdate() {
        return eventdate;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public String getEventdesc() {
        return eventdesc;
    }

    public void setEventdesc(String eventdesc) {
        this.eventdesc = eventdesc;
    }

    public String getEventlocation() {
        return eventlocation;
    }

    public void setEventlocation(String eventlocation) {
        this.eventlocation = eventlocation;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventtime() {
        return eventtime;
    }

    public void setEventtime(String eventtime) {
        this.eventtime = eventtime;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }
}
