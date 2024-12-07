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
import java.util.ArrayList;

public class LectureExcelSaveDataTest {

    public static void main(String[] args) {
        LectureExcelSaveData saver = new LectureExcelSaveData();

        // Test 1: saveData()
        try {
            ArrayList<String> testData = new ArrayList<>();
            testData.add("CS101");
            testData.add("Intro to Computer Science");
            testData.add("Prof. Kim");
            testData.add("50");
            testData.add("3");
            saver.saveData(testData);
            System.out.println("Test 1 Passed: saveData() successfully saved data.");
        } catch (Exception e) {
            System.out.println("Test 1 Failed: " + e.getMessage());
        }

        // Test 2: finalConfirm()
        try {
            boolean result = saver.finalConfirm("10", "50", "Prof. Kim", "CS101");
            if (result) {
                System.out.println("Test 2 Passed: finalConfirm() successfully updated data.");
            } else {
                System.out.println("Test 2 Failed: Class not found.");
            }
        } catch (Exception e) {
            System.out.println("Test 2 Failed: " + e.getMessage());
        }

        // Test 3: modifyClass()
        try {
            boolean result = saver.modifyClass("CS101", "Updated Class,Prof. Lee,40");
            if (result) {
                System.out.println("Test 3 Passed: modifyClass() successfully modified the class.");
            } else {
                System.out.println("Test 3 Failed: Class not found.");
            }
        } catch (Exception e) {
            System.out.println("Test 3 Failed: " + e.getMessage());
        }

        // Test 4: deleteClass()
        try {
            boolean result = saver.deleteClass("UpdatedClass");
            if (result) {
                System.out.println("Test 4 Passed: deleteClass() successfully deleted the class.");
            } else {
                System.out.println("Test 4 Failed: Class not found.");
            }
        } catch (Exception e) {
            System.out.println("Test 4 Failed: " + e.getMessage());
        }

    }
}

