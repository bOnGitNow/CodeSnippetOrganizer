package service;

import db.SnippetDAO;
import model.Snippet;
import java.util.List;

public class SearchService {
    private final SnippetDAO dao = new SnippetDAO();

    public List<Snippet> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return dao.getAll();
        return dao.search(keyword);
    }
}