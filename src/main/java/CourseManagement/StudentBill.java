/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CourseManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import project.excel.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author user
 */
public class StudentBill extends JFrame {
    private final int FEE = 50000;
    public StudentBill(String name, CopyOnWriteArrayList<String> data) {
         setTitle("수강료 확인");
         setSize(500, 300);
         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫기 설정

         // JTable 초기화
         String[] columnNames = {"강의명", "강의료"}; // 열 이름
         DefaultTableModel defaultTable = new DefaultTableModel(columnNames, 0);
         
         // 데이터 추가
         for (String row : data) {
             defaultTable.addRow(new Object[]{row, FEE}); // 데이터 추가
         }
         
         int totalPrice = data.size() * FEE;
         defaultTable.addRow(new Object[]{"총 강의료", totalPrice});
         
         JTable table = new JTable(defaultTable); // JTable 생성
         JScrollPane scrollPane = new JScrollPane(table); // 스크롤 추가

         // 레이아웃에 컴포넌트 추가
         add(scrollPane);
     }
}
