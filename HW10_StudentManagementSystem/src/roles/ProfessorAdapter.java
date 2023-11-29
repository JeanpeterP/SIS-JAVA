package roles;

import java.util.ArrayList;

public class ProfessorAdapter {
    public static Professor createProfessor(String name, String id, String username, String password) {
        return new Professor(id, name, username, password, new ArrayList<>());
    }
}
