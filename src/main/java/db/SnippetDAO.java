package db;

import model.Snippet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SnippetDAO {

    public static void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS snippets (
                id       INTEGER PRIMARY KEY AUTOINCREMENT,
                title    TEXT NOT NULL,
                language TEXT,
                code     TEXT,
                tags     TEXT
            )""";
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public int insert(Snippet s) {
        String sql = "INSERT INTO snippets (title,language,code,tags) VALUES (?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getTitle());  ps.setString(2, s.getLanguage());
            ps.setString(3, s.getCode());   ps.setString(4, s.getTags());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public List<Snippet> getAll() {
        List<Snippet> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM snippets ORDER BY id DESC")) {
            while (rs.next())
                list.add(new Snippet(rs.getInt("id"), rs.getString("title"),
                        rs.getString("language"), rs.getString("code"), rs.getString("tags")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Snippet> search(String keyword) {
        List<Snippet> list = new ArrayList<>();
        String sql = "SELECT * FROM snippets WHERE LOWER(title) LIKE ? OR LOWER(tags) LIKE ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String kw = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, kw); ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(new Snippet(rs.getInt("id"), rs.getString("title"),
                        rs.getString("language"), rs.getString("code"), rs.getString("tags")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    public static void loadInitialData() {
        String check = "SELECT COUNT(*) FROM snippets";

        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(check)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // DB is empty → load data
                String sql = new String(
                        java.nio.file.Files.readAllBytes(
                                java.nio.file.Paths.get("data.sql")
                        )
                );

                s.executeUpdate(sql);
                System.out.println("Sample data loaded!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Snippet s) {
        String sql = "UPDATE snippets SET title=?,language=?,code=?,tags=? WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getTitle());  ps.setString(2, s.getLanguage());
            ps.setString(3, s.getCode());   ps.setString(4, s.getTags());
            ps.setInt(5, s.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM snippets WHERE id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}