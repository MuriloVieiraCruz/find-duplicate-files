package br.com.murilo.view;

import br.com.murilo.core.DuplicateFileFinder;
import br.com.murilo.view.components.table.DuplicateTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainView extends JFrame {

    private JTextField directoryField;
    private JProgressBar progressBar;
    private JTable table;

    public MainView() {
        setWindowsLookAndFeel();
        createView();

        setTitle("DFF Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createView() {

        DuplicateTableModel model = new DuplicateTableModel(new ArrayList<>());

        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new GridBagLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel directoryLabel = new JLabel("Enter directory:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(directoryLabel, gbc);

        directoryField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        topPanel.add(directoryField, gbc);

        JButton searchButton = new JButton("Search");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        topPanel.add(searchButton, gbc);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(progressBar, gbc);

        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));
        table.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        table.setFillsViewportHeight(true);
        panel.add(table, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        scrollPane.setPreferredSize(new Dimension(400, 200));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Duplicates Found:"));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        panel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(deleteButton);
        bottomPanel.add(buttonPanel, BorderLayout.WEST);

        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JCheckBox checkBox = new JCheckBox("Filter Old Files");
        checkBoxPanel.add(checkBox);
        bottomPanel.add(checkBoxPanel, BorderLayout.EAST);


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directoryPath = directoryField.getText();

                if (directoryPath == null || directoryPath.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please, enter a valid directory");
                    return;
                }

                File directory = new File(directoryPath);

                if (!directory.isDirectory()) {
                    JOptionPane.showMessageDialog(panel, "The provided path is not a directory");
                    return;
                }

                progressBar.setVisible(true);
                searchButton.setEnabled(false);

                SwingWorker<List<File>, Void> worker = new SwingWorker<>() {

                    @Override
                    protected List<File> doInBackground() throws Exception {
                        DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinder();
                        return duplicateFileFinder.mainProcess(directory);
                    }

                    @Override
                    protected void done() {
                        try {
                            List<File> duplicates = get();
                            if (duplicates.isEmpty()) {
                                JOptionPane.showMessageDialog(panel, "No duplicates found");
                            } else {
                                DuplicateTableModel model = new DuplicateTableModel(duplicates);
                                table.setModel(model);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "An error occurred during the search.");
                        } finally {
                            progressBar.setVisible(false);
                            searchButton.setEnabled(true);
                        }
                    }
                };
                worker.execute();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                DuplicateTableModel model = (DuplicateTableModel) table.getModel();

                if (selectedRows.length >= 1 && !model.isEmpty()) {
                    int option = JOptionPane.showConfirmDialog(panel,
                            "You really want to remove these files?",
                            "Remove",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        List<File> selectedFiles = new ArrayList<>();

                        for (int selectedRow : selectedRows) {
                            selectedFiles.add(model.get(selectedRow));
                        }

                        selectedFiles.forEach(file -> {
                            if (file.delete()) {
                                JOptionPane.showMessageDialog(panel,"File " + file.getAbsolutePath() + " deleted successfully.");
                            } else {
                                JOptionPane.showMessageDialog(panel,"Failed to delete file " + file.getAbsolutePath() + ".");
                            }
                        });

                        model.remove(selectedFiles);
                        table.updateUI();
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Please, select one line at least");
                }
            }
        });
    }

    private void setWindowsLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
