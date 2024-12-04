package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.CTLFormula;
import model.KripkeStructure;
import model.State;
import utils.Util;

public class CTLCheckerUI extends JFrame {
    private final JTextField ctlFormula;
    private final JLabel modelTitle;
    private final JTextArea resultArea;
    private final JTextArea modelText;
    private final JComboBox<String> jComboBox;
    private final JFrame jFrame;

    private static KripkeStructure kripke;

    public CTLCheckerUI() {
        
        Color primaryColor = new Color(63, 81, 181); 
        Color secondaryColor = new Color(0, 150, 136); 
        Color backgroundColor = new Color(245, 245, 245); 

        jFrame = new JFrame("CTL Model Checker");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(1000, 800));
        jFrame.setLayout(new BorderLayout(10, 10));
        jFrame.getContentPane().setBackground(backgroundColor);

        
        JLabel headerLabel = new JLabel("CTL Model Checker", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(primaryColor);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        jFrame.add(headerLabel, BorderLayout.NORTH);

        JPanel membersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        membersPanel.setBackground(backgroundColor);
        JTextArea membersTextArea = new JTextArea(
            "Group Members:\n" +
            "Sai Shanmukkha Surapaneni              siv30@txstate.edu\n" +
            "Syaila Syam Sunder Thambabathula       oko17@txstate.edu\n" +
            "Venkata Praveen Kumar Sana             qno18@txstate.edu\n" +
            "Sai Bharani Bikkina                    jfi18@txstate.edu"
        );
        membersTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        membersTextArea.setEditable(false);
        membersTextArea.setBackground(backgroundColor);
        membersTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        membersPanel.add(membersTextArea);
        jFrame.add(membersPanel, BorderLayout.SOUTH);

        
        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBorder(new EmptyBorder(0, 20, 0, 20)); 
        mainContentPanel.setBackground(backgroundColor);

        
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filePanel.setBackground(backgroundColor);
        JButton uploadButton = new JButton("Upload File");
        uploadButton.setFont(new Font("Arial", Font.PLAIN, 20));
        uploadButton.setBackground(secondaryColor);
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFocusPainted(false);
        uploadButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        uploadButton.addActionListener(new UploadFileListener());
        filePanel.add(new JLabel("Upload Model Checker File:", SwingConstants.LEFT));
        filePanel.add(uploadButton);

        
        JPanel modelPanel = new JPanel(new BorderLayout());
        modelPanel.setBackground(backgroundColor);
        modelTitle = new JLabel("Model");
        modelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        modelTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        modelText = new JTextArea(15, 70);
        modelText.setEditable(false);
        modelText.setFont(new Font("Monospaced", Font.PLAIN, 18));
        modelText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane modelScrollPane = new JScrollPane(modelText);
        modelPanel.add(modelTitle, BorderLayout.NORTH);
        modelPanel.add(modelScrollPane, BorderLayout.CENTER);

        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.setBackground(backgroundColor);
        jComboBox = new JComboBox<>();
        jComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        ctlFormula = new JTextField(30);
        ctlFormula.setFont(new Font("Arial", Font.PLAIN, 20));
        inputPanel.add(new JLabel("Select Model State:"));
        inputPanel.add(jComboBox);
        inputPanel.add(new JLabel("Enter CTL Formula:"));
        inputPanel.add(ctlFormula);

        
        JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
        resultPanel.setBackground(backgroundColor);
        JButton checkButton = new JButton("Check Result");
        checkButton.setFont(new Font("Arial", Font.PLAIN, 20));
        checkButton.setBackground(primaryColor);
        checkButton.setForeground(Color.WHITE);
        checkButton.setFocusPainted(false);
        checkButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        checkButton.addActionListener(new CheckActionListener());
        resultArea = new JTextArea(5, 70);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        resultArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        resultPanel.add(checkButton, BorderLayout.NORTH);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        
        mainContentPanel.add(filePanel, BorderLayout.NORTH);
        mainContentPanel.add(modelPanel, BorderLayout.CENTER);
        mainContentPanel.add(inputPanel, BorderLayout.SOUTH);
        jFrame.add(mainContentPanel, BorderLayout.CENTER);
        jFrame.add(resultPanel, BorderLayout.SOUTH);

        
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
    }

    private void ClearModel() {
        modelText.setText("");
        modelTitle.setText("Model");
        if (jComboBox.getSelectedIndex() != -1) {
            DefaultComboBoxModel<String> theModel = (DefaultComboBoxModel<String>) jComboBox.getModel();
            theModel.removeAllElements();
        }
    }

    class UploadFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClearModel();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new java.io.File("."));
            fileChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int value = fileChooser.showOpenDialog(jFrame);
            if (value == JFileChooser.APPROVE_OPTION) {
                try {
                    File filePath = fileChooser.getSelectedFile();
                    if (filePath == null) {
                        throw new IOException("No file selected!");
                    }

                    String file = Util.readFile(filePath.getAbsolutePath());
                    kripke = new KripkeStructure(Util.cleanText(file));

                    ClearModel();
                    for (String s : kripke.getStates()) {
                        jComboBox.addItem(s);
                    }
                    String modelName = filePath.getName();
                    modelTitle.setText(modelName);
                    modelText.setText(kripke.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class CheckActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resultArea.setText("");
            try {
                if (kripke == null) {
                    throw new Exception("Please load a Kripke model!");
                }
                if (ctlFormula.getText().isEmpty()) {
                    throw new Exception("Please enter a CTL formula!");
                }

                String originalExpression = ctlFormula.getText();
                String expression = originalExpression.replaceAll("\\s", "");
                String checkedStateID = Objects.requireNonNull(jComboBox.getSelectedItem()).toString();

                State checkedState = new State(checkedStateID);
                CTLFormula ctlFormulaObj = new CTLFormula(expression, checkedState, kripke);
                
                boolean isSatisfy = ctlFormulaObj.IsSatisfy();
                String message = Util.GetMessage(isSatisfy, originalExpression, checkedStateID);
                resultArea.setText(message);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(jFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
