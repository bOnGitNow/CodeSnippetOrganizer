import db.SnippetDAO;
import ui.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        System.out.println("APP STARTING..."); // debug

        try {
            SnippetDAO.createTable();
            
            SnippetDAO.loadInitialData(); // 🔥 ADD THIS
            System.out.println("DB READY"); // debug
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
            System.out.println("UI LOADED"); // debug
        });
    }
}