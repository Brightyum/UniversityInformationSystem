package Login;

import java.util.HashMap;
import java.util.Map;

public class RoleConfig {

    private static final Map<String, String> filePaths = new HashMap<>();
    private static final Map<String, Integer> idColumns = new HashMap<>();
    private static final Map<String, Integer> passwordColumns = new HashMap<>();

    static {
        filePaths.put("학생", "Student_data.xlsx");
        filePaths.put("교수", "Professor_data.xlsx");
        filePaths.put("학사 담당자", "Academic_data.xlsx");
        filePaths.put("수강 관리자", "LectureStaff_data.xlsx");

        idColumns.put("학생", 0);
        passwordColumns.put("학생", 9);

        idColumns.put("교수", 0);
        passwordColumns.put("교수", 5);

        idColumns.put("학사 담당자", 0);
        passwordColumns.put("학사 담당자", 3);

        idColumns.put("수강 관리자", 11);
        passwordColumns.put("수강 관리자", 14);
    }

    public static String[] getRoles() {
        return filePaths.keySet().toArray(new String[0]);
    }

    public static String getFilePath(String role) {
        return filePaths.get(role);
    }

    public static int getIdColumn(String role) {
        return idColumns.get(role);
    }

    public static int getPasswordColumn(String role) {
        return passwordColumns.get(role);
    }
}
