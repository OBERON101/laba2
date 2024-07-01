package laba.project.gui;

import laba.project.info.Import;
import laba.project.excel.Excel;
import laba.project.exceptions.WrongException;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainGui extends JFrame implements ActionListener {
  private static final int HEIGHT = 600;
  private static final int WIDTH = 800;
  private JButton importButton;
  private JButton exportButton;
  private JButton exitButton;
  private JPanel mainPane;
  private JPanel exitPane;
  private JComboBox<String> comboBox;

  private Import data;
  private Excel excel;
  private String currentFilePath; // Добавляем для хранения пути к файлу

  public MainGui(String name) {
    super(name);
    this.setSize(WIDTH, HEIGHT);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setLayout(new BorderLayout());

    importButton = new JButton("Import excel file");
    exportButton = new JButton("Export data");
    exitButton = new JButton("Exit");
    mainPane = new JPanel(new GridLayout(2, 0));
    exitPane = new JPanel(new BorderLayout());
    comboBox = new JComboBox<>();

    importButton.addActionListener(this);
    exportButton.addActionListener(this);
    exitButton.addActionListener(this);
    comboBox.addActionListener(this);

    mainPane.add(importButton);
    mainPane.add(exportButton);
    mainPane.add(comboBox);
    exitPane.add(exitButton);
    this.add(mainPane, BorderLayout.CENTER);
    this.add(exitPane, BorderLayout.SOUTH);
    data = new Import();
    excel = new Excel();
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    if (event.getSource() == importButton) {
      importAction();
    }
    if (event.getSource() == exportButton) {
      exportAction();
    }
    if (event.getSource() == exitButton) {
      this.dispose();
    }
    if (event.getSource() == comboBox) {
      importDataFromSelectedSheet();
    }
  }

  private void exportAction() {
    if (data.getxArray() == null) {
      JOptionPane.showMessageDialog(this, "Сломалось", "Сломалось",
              JOptionPane.ERROR_MESSAGE);
      return;
    }
    JFileChooser fileopen = new JFileChooser();
    fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileopen.setAcceptAllFileFilterUsed(false);
    int ret = fileopen.showDialog(this, "Choose folder");
    if (ret != JFileChooser.APPROVE_OPTION) {
      JOptionPane.showMessageDialog(this, "Folder wasn't chosen",
              "Folder wasn't chosen",
              JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      excel.ExportToFile(fileopen.getSelectedFile().getAbsolutePath(), data);
    } catch (WrongException e) {
      JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage(), "ERROR:",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  private void importAction() {
    JFileChooser fileopen = new JFileChooser();
    int ret = fileopen.showDialog(null, "Выберите файл");
    if (ret != JFileChooser.APPROVE_OPTION) {
      return;
    }
    try {
      currentFilePath = fileopen.getSelectedFile().getAbsolutePath();
      List<String> sheetNames = excel.getSheetNames(currentFilePath);
      comboBox.removeAllItems();
      for (String sheetName : sheetNames) {
        comboBox.addItem(sheetName);
      }
      comboBox.setSelectedIndex(0);
      importDataFromSelectedSheet();
    } catch (WrongException e) {
      JOptionPane.showMessageDialog(this, "ошибка " + e.getMessage(), "ошибка",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  private void importDataFromSelectedSheet() {
    try {
      int sheetNumber = comboBox.getSelectedIndex();
      data = excel.ImportFromFile(currentFilePath, sheetNumber);

      // Логирование для проверки данных
      if (data != null) {
        System.out.println("Data imported successfully");
        if (data.getxArray() != null) {
          System.out.println("xArray length: " + data.getxArray().length);
        } else {
          System.out.println("xArray is null");
        }
        if (data.getyArray() != null) {
          System.out.println("yArray length: " + data.getyArray().length);
        } else {
          System.out.println("yArray is null");
        }
        if (data.getzArray() != null) {
          System.out.println("zArray length: " + data.getzArray().length);
        } else {
          System.out.println("zArray is null");
        }
      } else {
        System.out.println("Data is null");
      }
    } catch (WrongException e) {
      JOptionPane.showMessageDialog(this, "ошибка " + e.getMessage(), "ошибка",
              JOptionPane.ERROR_MESSAGE);
    }
  }
}
