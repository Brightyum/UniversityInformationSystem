package Login;

import CourseManagement.ProfessorServicesUi;
import CourseManagement.StudentServicesUi;
import javax.swing.*;
import project.ui.AcademicUi;
import project.classer.ClassesUi;
import java.io.IOException;

public class NavigationManager {

    // 역할 및 이름에 따른 화면 전환
    public static void navigate(String role, String name) throws IOException {
        switch (role) {
            case "Student":
                new StudentServicesUi(name); // 학생 UI 생성자에 이름 전달
                break;
            case "Professor":
                new ProfessorServicesUi(name); // 교수 UI 생성자에 이름 전달
                break;
            case "Academic":
                new AcademicUi(); // 학사 담당자는 이름 필요 없음
                break;
            case "LectureStaff":
                new ClassesUi(); // 수업 담당자는 이름 필요 없음
                break;
            default:
                JOptionPane.showMessageDialog(null, "알 수 없는 역할입니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
