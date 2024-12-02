/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.classer;
import javax.swing.*;
import java.awt.*;
import project.excel.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author duatm
 */
public class ClassBilling extends JFrame {
    private String classSelect;
    private CopyOnWriteArrayList<String> currentStudentList;
    
    public ClassBilling() throws IOException {
        setTitle("수업 담당자창");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10, 10, 10, 10);
        
        currentStudentList = new CopyOnWriteArrayList<>();
        LectureExcelReadData readObject = new LectureExcelReadData();

        // 강의 선택 라벨과 콤보박스
        JLabel lectureLabel = new JLabel("청구하실 강의를 선택하세요");
        detail.gridx = 0;
        detail.gridy = 0;
        add(lectureLabel, detail);

        JComboBox<String> lectureComboBox = new JComboBox<>();
        detail.gridx = 1;
        detail.gridy = 0;

        CopyOnWriteArrayList<String> className = readObject.getLecture();
        for (String i : className) {
            lectureComboBox.addItem(i);
        }
        add(lectureComboBox, detail);

        // 학생 명단 라벨과 리스트
        JLabel studentLabel = new JLabel("강의에 대한 학생 명단입니다.");
        detail.gridx = 0;
        detail.gridy = 1;
        add(studentLabel, detail);

        DefaultListModel<String> studentListModel = new DefaultListModel<>();
        JList<String> studentList = new JList<>(studentListModel);
        studentList.setVisibleRowCount(10); // 표시할 행 개수 설정
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 단일 선택

        JScrollPane scrollPane = new JScrollPane(studentList); // 스크롤 추가
        detail.gridx = 1;
        detail.gridy = 1;
        add(scrollPane, detail);

        // 강의 선택 이벤트 리스너
        lectureComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classSelect = (String) lectureComboBox.getSelectedItem();
                if (classSelect != null) {
                    try {
                        // 학생 명단 업데이트
                        studentListModel.clear(); // 기존 항목 제거
                        currentStudentList.clear();
                        CopyOnWriteArrayList<String> studentName = readObject.getStudent(classSelect);
                        for (String student : studentName) {
                            studentListModel.addElement(student);
                            currentStudentList.add(student);
                        }
                        
                        int rowCount = studentListModel.getSize();
                        if (rowCount > 0) {
                            studentList.setVisibleRowCount(Math.min(rowCount, 50));
                        } else {
                            studentList.setVisibleRowCount(0);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(ClassBilling.this,
                                "학생 명단을 불러오는 중 오류가 발생했습니다: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 수강료 청구 버튼
        JButton billingButton = new JButton("수강료 청구하기");
        detail.gridx = 1;
        detail.gridy = 2;
        billingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(currentStudentList);
            }
        });
        add(billingButton, detail);

        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new ClassBilling();
    }
}