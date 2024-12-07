/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
package project.excel;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClassLectureReadDataManualTest {

    public static void main(String[] args) {
        // 테스트 클래스 인스턴스 생성
        ClassLectureReadData reader = new ClassLectureReadData();

        // 테스트 1: readClassData() 테스트
        try {
            CopyOnWriteArrayList<String> lectureData = reader.readClassData();
            if (!lectureData.isEmpty()) {
                System.out.println("Test 1 Passed: readClassData() returned data.");
                System.out.println("Data: " + lectureData);
            } else {
                System.out.println("Test 1 Failed: readClassData() returned empty data.");
            }
        } catch (IOException e) {
            System.out.println("Test 1 Failed: readClassData() threw an exception: " + e.getMessage());
        }

        // 테스트 2: readProfessorData() 테스트
        try {
            String testClass = "자바4"; // 예제 데이터
            CopyOnWriteArrayList<String> professorData = reader.readProfessorData(testClass);

            if (!professorData.isEmpty()) {
                System.out.println("Test 2 Passed: readProfessorData('" + testClass + "') returned data.");
                System.out.println("Data: " + professorData);
            } else {
                System.out.println("Test 2 Failed: readProfessorData('" + testClass + "') returned empty data.");
            }
        } catch (IOException e) {
            System.out.println("Test 2 Failed: readProfessorData() threw an exception: " + e.getMessage());
        }
    }
}
