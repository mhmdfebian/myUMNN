package id.ac.umn.myumn.Grade;

public class GradeModel {

    private int nilaiuts;
    private int nilaiuas;
    private int nilaitugas;
    private String subject;
    private String gradeid;

    private GradeModel(int nilaiuts, int nilaiuas, int nilaitugas, String gradeid, String subject) {
        this.nilaiuts = nilaiuts;
        this.nilaiuas = nilaiuas;
        this.nilaitugas = nilaitugas;
        this.gradeid = gradeid;
        this.subject = subject;
    }

    private GradeModel() {

    }

    public int getNilaiuts() {
        return nilaiuts;
    }

    public void setNilaiuts(int nilaiuts) {
        this.nilaiuts = nilaiuts;
    }

    public int getNilaiuas() {
        return nilaiuas;
    }

    public void setNilaiuas(int nilaiuas) {
        this.nilaiuas = nilaiuas;
    }

    public int getNilaitugas() {
        return nilaitugas;
    }

    public void setNilaitugas(int nilaitugas) {
        this.nilaitugas = nilaitugas;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGradeid() {
        return gradeid;
    }

    public void setGradeid(String gradeid) {
        this.gradeid = gradeid;
    }
}
