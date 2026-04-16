package service;

import db.SnippetDAO;
import model.Snippet;
import java.util.List;

public class SnippetService {
    private final SnippetDAO dao = new SnippetDAO();

    public void addSnippet(String title, String language, String code, String tags) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title cannot be empty");
        dao.insert(new Snippet(title, language, code, tags));
    }

    public void updateSnippet(Snippet s) { dao.update(s); }
    public void deleteSnippet(int id)    { dao.delete(id); }
    public List<Snippet> getAllSnippets() { return dao.getAll(); }
}