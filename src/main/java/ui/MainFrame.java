package ui;

import service.SearchService;
import service.SnippetService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Code Snippet Organizer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        SnippetService service       = new SnippetService();
        SearchService  searchService = new SearchService();

        SnippetListPanel listPanel = new SnippetListPanel(service, searchService);
        SnippetForm form = new SnippetForm(service, () -> listPanel.refresh("", "All"));
        listPanel.setForm(form);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, listPanel);
        split.setDividerLocation(370);
        add(split, BorderLayout.CENTER);

        listPanel.refresh("","All");
    }
}