package br.com.murilo.view;

import br.com.murilo.core.DuplicateFileFinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MainView extends JFrame {

    private JTextField directoryField;

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

        JTextArea outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

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

                DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinder();
                List<File> duplicates = duplicateFileFinder.mainProcess(directory);

                if (duplicates.isEmpty()) {
                    outputArea.setText("No duplicates found");
                } else {
                    outputArea.setText("Duplicates found:\n" + duplicates.stream().map(File::getAbsolutePath).collect(Collectors.joining("\n")));
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
