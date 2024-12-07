/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.excel;

/**
 *
 * @author user
 */
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class LectureExcelReadDataTest {

    public static void main(String[] args) throws IOException {
        LectureExcelReadData reader = new LectureExcelReadData();

        // 테스트 1: readLectureStaffExcel()
        try {
            CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> lectureData = reader.readLectureStaffExcel();
            System.out.println("성공");
            for (CopyOnWriteArrayList<Object> row : lectureData) {
                System.out.println(row);
            }
        } catch (Exception e) {
            System.out.println("실패 " + e.getMessage());
        }

        // 테스트 2: getColumn()
        try {
            CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> lectureData = reader.readLectureStaffExcel();
            CopyOnWriteArrayList<String> columnData = reader.getColumn(lectureData);
            System.out.println("성공.");
            System.out.println(columnData);
        } catch (Exception e) {
            System.out.println("실패" + e.getMessage());
        }

        // 테스트 3: getClasses()
        try {
            CopyOnWriteArrayList<String> classes = reader.getClasses();
            System.out.println("성공.");
            System.out.println(classes);
        } catch (Exception e) {
            System.out.println("실패" + e.getMessage());
        }

        // 테스트 4: getLecture()
        try {
            CopyOnWriteArrayList<String> lectures = reader.getLecture();
            System.out.println("성공.");
            System.out.println(lectures);
        } catch (Exception e) {
            System.out.println("실패 " + e.getMessage());
        }

        // 테스트 5: getStudent(String classSelect)
        try {
            String testClass = "자바4"; // 테스트 강의 이름
            CopyOnWriteArrayList<String> students = reader.getStudent(testClass);
            System.out.println("성공");
            System.out.println(students);
        } catch (Exception e) {
            System.out.println("실패" + e.getMessage());
        }

        // 테스트 6: getPossibleLecture(String name)
        try {
            String testName = "염승욱"; // 테스트 학생 이름
            CopyOnWriteArrayList<String> possibleLectures = reader.getPossibleLecture(testName);
            if (possibleLectures != null) {
                System.out.println("성공");
                System.out.println(possibleLectures);
            } else {
                System.out.println("테스트 6 리턴값 없음.");
            }
        } catch (Exception e) {
            System.out.println("실패 " + e.getMessage());
        }
        
         // Test 7: getConfirmLecture(String name)
        try {
            String testName = "염승욱"; // 테스트용 학생 이름
            CopyOnWriteArrayList<String> confirmLectures = reader.getConfirmLecture(testName);
            System.out.println("성공");
            System.out.println(confirmLectures);
        } catch (Exception e) {
            System.out.println("실패" + e.getMessage());
        }

        // Test 8: getScore(String name)
        try {
            String testName = "염승욱"; // 테스트용 학생 이름
            Map<String, String> scores = reader.getScore(testName);
            if (scores != null) {
                System.out.println("성공.");
                System.out.println(scores);
            } else {
                System.out.println("테스트 리턴값 없음");
            }
        } catch (Exception e) {
            System.out.println("실패" + e.getMessage());
        }

        // Test 9: getLectureStudent(String name, String select)
        try {
            String professorName = "염승욱"; // 테스트용 교수 이름
            String lectureName = "염승욱,설효주"; // 테스트용 강좌 이름
            Map<String, List<String>> lectureStudents = reader.getLectureStudent(professorName, lectureName);
            System.out.println("성공.");
            System.out.println(lectureStudents);
        } catch (Exception e) {
            System.out.println("실패 " + e.getMessage());
        }

        // Test 10: getStudentInformation(String name, String select)
        try {
            String professorName = "염승욱"; // 테스트용 교수 이름
            String lectureName = "자바4"; // 테스트용 강좌 이름
            Map<String, List<String>> studentInfo = reader.getStudentInformation(professorName, lectureName);
            System.out.println("성공.");
            System.out.println(studentInfo);
        } catch (Exception e) {
            System.out.println("실패" + e.getMessage());
        }

        // Test 11: lectureName(String name)
        try {
            String professorName = "염승욱"; // 테스트용 교수 이름
            CopyOnWriteArrayList<String> lectureNames = reader.lectureName(professorName);
            System.out.println("성공.");
            System.out.println(lectureNames);
        } catch (Exception e) {
            System.out.println("실패" + e.getMessage());
        }
    }
}