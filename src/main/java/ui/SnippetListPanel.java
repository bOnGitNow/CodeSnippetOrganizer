package ui;

import model.Snippet;
import service.SearchService;
import service.SnippetService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SnippetListPanel extends JPanel {

    private final DefaultListModel<Snippet> listModel = new DefaultListModel<>();
    private final JList<Snippet> snippetList = new JList<>(listModel);
    private final JTextField searchField = new JTextField(20);
    private final JTextArea codeViewer = new JTextArea(10, 40);
    private final JComboBox<String> languageFilter = new JComboBox<>();

    private final SnippetService service;
    private final SearchService searchService;
    private SnippetForm form;

    public SnippetListPanel(SnippetService service, SearchService searchService) {
        this.service = service;
        this.searchService = searchService;
        buildUI();
    }

    public void setForm(SnippetForm form) {
        this.form = form;
    }

    private void buildUI() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createTitledBorder("Snippets"));

        // 🔍 TOP BAR
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchBar.add(new JLabel("Search:"));
        searchBar.add(searchField);

        JButton searchBtn = new JButton("Go");

        // 🌐 Language filter
        languageFilter.addItem("All");
        languageFilter.addItem("Java");
        languageFilter.addItem("Python");
        languageFilter.addItem("SQL");

        searchBar.add(new JLabel("Language:"));
        searchBar.add(languageFilter);
        searchBar.add(searchBtn);

        add(searchBar, BorderLayout.NORTH);

        // 🎯 ACTIONS
        searchBtn.addActionListener(e ->
                refresh(searchField.getText(), (String) languageFilter.getSelectedItem())
        );

        searchField.addActionListener(e ->
                refresh(searchField.getText(), (String) languageFilter.getSelectedItem())
        );

        languageFilter.addActionListener(e ->
                refresh(searchField.getText(), (String) languageFilter.getSelectedItem())
        );

        // 📋 LIST
        snippetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        snippetList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showSelected();
        });

        add(new JScrollPane(snippetList), BorderLayout.CENTER);

        // 🧾 CODE VIEWER
        codeViewer.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        codeViewer.setEditable(false);
        codeViewer.setLineWrap(true);
        codeViewer.setWrapStyleWord(true);

        JPanel south = new JPanel(new BorderLayout(4, 4));
        south.add(new JScrollPane(codeViewer), BorderLayout.CENTER);

        // ✏️ BUTTONS
        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        actionButtons.add(editBtn);
        actionButtons.add(deleteBtn);

        south.add(actionButtons, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());
    }

    // 🔥 MAIN LOGIC
    public void refresh(String keyword, String selectedLang) {
        listModel.clear();

        List<Snippet> results = searchService.search(keyword);

        for (Snippet s : results) {

            // 🌐 Language filter
            if (!selectedLang.equals("All") &&
                    !s.getLanguage().equalsIgnoreCase(selectedLang)) {
                continue;
            }

            // 🔍 SEARCH MODE → show title + language
            if (keyword != null && !keyword.isBlank()) {
                listModel.addElement(new Snippet(
                        s.getId(),
                        s.getTitle() + " [" + s.getLanguage() + "]",
                        s.getLanguage(),
                        s.getCode(),
                        s.getTags()
                ));
            } else {
                // 🧾 NORMAL MODE → only title
                listModel.addElement(s);
            }
        }
    }

    private void showSelected() {
        Snippet s = snippetList.getSelectedValue();
        if (s != null) {
            codeViewer.setText(s.getCode()); // only here code is shown
        }
    }

    private void editSelected() {
        Snippet s = snippetList.getSelectedValue();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Select a snippet first.");
            return;
        }
        if (form != null) form.loadForEdit(s);
    }

    private void deleteSelected() {
        Snippet s = snippetList.getSelectedValue();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Select a snippet first.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(
                this,
                "Delete \"" + s.getTitle() + "\"?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (ok == JOptionPane.YES_OPTION) {
            service.deleteSnippet(s.getId());
            refresh(searchField.getText(), (String) languageFilter.getSelectedItem());
            codeViewer.setText("");
        }
    }
}