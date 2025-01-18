package br.com.alura.ProjetoAlura.registration;

public class RegistrationReportItem {

    private final String courseName;
    private final String courseCode;
    private final String instructorName;
    private final String instructorEmail;
    private final Long totalRegistrations;

    public RegistrationReportItem(String courseName, String courseCode, String instructorName, String instructorEmail, Long totalRegistrations) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.totalRegistrations = totalRegistrations;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public Long getTotalRegistrations() {
        return totalRegistrations;
    }
}
