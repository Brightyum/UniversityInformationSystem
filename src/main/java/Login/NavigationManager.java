package Login;

import CourseManagement.ProfessorServicesUi;
import CourseManagement.StudentServicesUi;
import javax.swing.*;
import project.ui.AcademicUi;
import project.classer.ClassesUi;
import java.io.IOException;

public class NavigationManager {

    // 역할에 따른 화면 전환
    public static void navigate(String role) throws IOException {
        switch (role) {
            case "Student":
                new StudentServicesUi();
                break;
            case "Professor":
                new ProfessorServicesUi();
                break;
            case "Academic":
                new AcademicUi();
                break;
            case "LectureStaff":
                new ClassesUi();
                break;
            default:
                JOptionPane.showMessageDialog(null, "알 수 없는 역할입니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
