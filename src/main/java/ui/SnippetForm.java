package ui;

import model.Snippet;
import service.SnippetService;

import javax.swing.*;
import java.awt.*;

public class SnippetForm extends JPanel {

    private final JTextField titleField    = new JTextField();
    private final JTextField languageField = new JTextField();
    private final JTextField tagsField     = new JTextField();
    private final JTextArea  codeArea      = new JTextArea();

    private final JButton saveButton  = new JButton("Save");
    private final JButton clearButton = new JButton("Clear");

    private final SnippetService service;
    private final Runnable onSaved;
    private Snippet editingSnippet;

    public SnippetForm(SnippetService service, Runnable onSaved) {
        this.service = service;
        this.onSaved = onSaved;
        buildUI();
        wireButtons();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Add / Edit Snippet"));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        // 🔹 TITLE
        gc.gridx = 0; gc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gc);

        gc.gridx = 1;
        titleField.setPreferredSize(new Dimension(250, 28));
        formPanel.add(titleField, gc);

        // 🔹 LANGUAGE
        gc.gridx = 0; gc.gridy++;
        formPanel.add(new JLabel("Language:"), gc);

        gc.gridx = 1;
        languageField.setPreferredSize(new Dimension(250, 28));
        formPanel.add(languageField, gc);

        // 🔹 TAGS
        gc.gridx = 0; gc.gridy++;
        formPanel.add(new JLabel("Tags:"), gc);

        gc.gridx = 1;
        tagsField.setPreferredSize(new Dimension(250, 28));
        formPanel.add(tagsField, gc);

        // 🔹 CODE AREA
        gc.gridx = 0; gc.gridy++;
        gc.anchor = GridBagConstraints.NORTH;
        formPanel.add(new JLabel("Code:"), gc);

        gc.gridx = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1;

        codeArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        codeArea.setLineWrap(true);
        codeArea.setWrapStyleWord(true);

        JScrollPane codeScroll = new JScrollPane(codeArea);
        codeScroll.setPreferredSize(new Dimension(300, 200));

        formPanel.add(codeScroll, gc);

        add(formPanel, BorderLayout.CENTER);

        // 🔹 BUTTONS
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void wireButtons() {
        saveButton.addActionListener(e -> saveSnippet());
        clearButton.addActionListener(e -> clearForm());
    }

    private void saveSnippet() {
        String title = titleField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (editingSnippet == null) {
            service.addSnippet(
                    title,
                    languageField.getText().trim(),
                    codeArea.getText().trim(),
                    tagsField.getText().trim()
            );
        } else {
            editingSnippet.setTitle(title);
            editingSnippet.setLanguage(languageField.getText().trim());
            editingSnippet.setCode(codeArea.getText().trim());
            editingSnippet.setTags(tagsField.getText().trim());

            service.updateSnippet(editingSnippet);
            editingSnippet = null;
        }

        clearForm();
        onSaved.run(); // 🔥 refresh list
    }

    public void loadForEdit(Snippet s) {
        editingSnippet = s;

        titleField.setText(s.getTitle());
        languageField.setText(s.getLanguage());
        codeArea.setText(s.getCode());
        tagsField.setText(s.getTags());

        saveButton.setText("Update");
    }

    private void clearForm() {
        titleField.setText("");
        languageField.setText("");
        codeArea.setText("");
        tagsField.setText("");

        saveButton.setText("Save");
        editingSnippet = null;
    }
}