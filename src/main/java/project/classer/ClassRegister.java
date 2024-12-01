/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.classer;

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
public class ClassRegister extends JFrame {
    public ClassRegister() {
        setTitle("새로운 강좌 등록");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10, 10, 10, 10);

        // 라벨
        JLabel registerLabel = new JLabel("새로운 강좌를 입력하세요(구분자는 ',')");
        detail.gridx = 0;
        detail.gridy = 0;
        add(registerLabel, detail);

        // 텍스트 필드
        JTextField newClassText = new JTextField(30);
        detail.gridx = 1;
        detail.gridy = 0;
        add(newClassText, detail);

        // 강좌 생성 버튼
        JButton registerButton = new JButton("강좌 생성하기");
        detail.gridx = 1;
        detail.gridy = 1;
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = newClassText.getText();
                ArrayList<String> registerList = new ArrayList<>();

                if (!input.isEmpty()) {
                    String[] parts = input.split(",");
                    registerList.addAll(Arrays.asList(parts));
                }

                // 강좌 생성 데이터 출력
                for (String s : registerList) {
                    System.out.print(s + " ");
                }

                // 데이터 저장
                LectureExcelSaveData saveData = new LectureExcelSaveData();
                saveData.saveData(registerList);
            }
        });
        add(registerButton, detail);

        // 테이블 초기화
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        detail.gridx = 0;
        detail.gridy = 3;
        detail.gridwidth = 2; // 테이블이 두 칸 차지
        add(scrollPane, detail);

        // 새로고침 버튼
        JButton loadButton = new JButton("새로고침");
        detail.gridx = 0;
        detail.gridy = 2;
        detail.gridwidth = 1;
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LectureExcelReadData excelReader = new LectureExcelReadData();
                    CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data = excelReader.readLectureStaffExcel();
                    CopyOnWriteArrayList<String> columns = excelReader.getColumn(data);
                    
                    if (!data.isEmpty()) {
                        data.remove(0);
                    }
                    DefaultTableModel defaultTable = new DefaultTableModel();
                    defaultTable.setColumnIdentifiers(columns.toArray());

                    for (CopyOnWriteArrayList<Object> row : data) {
                        defaultTable.addRow(row.toArray());
                    }

                    table.setModel(defaultTable);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(ClassRegister.this,
                            "Excel 데이터를 읽는 중 오류가 발생했습니다: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(loadButton, detail);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClassRegister frame = new ClassRegister();
            frame.setVisible(true);
        });
    }
}