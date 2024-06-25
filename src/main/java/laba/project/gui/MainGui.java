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
  private JComboBox<String> comboBox; // Добавляем ComboBox

  private Import data;
  private Excel excel;

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
    comboBox = new JComboBox<>(); // Инициализируем ComboBox
    

    importButton.addActionListener(this);
    exportButton.addActionListener(this);
    exitButton.addActionListener(this);

    mainPane.add(importButton);
    mainPane.add(exportButton);
    mainPane.add(comboBox); // Добавляем ComboBox на панель
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
  }

  private void exportAction() {
    if (data.getxArray() == null) {
      JOptionPane.showMessageDialog(this, "No import data", "No import data",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    JFileChooser fileopen = new JFileChooser();
    fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileopen.setAcceptAllFileFilterUsed(false);
    int ret = fileopen.showDialog(this, "Choose folder");
    if (ret != JFileChooser.APPROVE_OPTION) {
      JOptionPane.showMessageDialog(this, "Folder wasn't choosen",
                                    "Folder wasn't choosen",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      excel.ExportToFile(fileopen.getSelectedFile().getAbsolutePath(), data);
    } catch (WrongException e) {
      JOptionPane.showMessageDialog(this, "Oooops: " + e.getMessage(), "Oooops",
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
    String filename = fileopen.getSelectedFile().getAbsolutePath();
    List<String> sheetNames = excel.getSheetNames(filename); // Получаем названия всех листов
    comboBox.removeAllItems(); // Очищаем ComboBox
    for (String sheetName : sheetNames) {
      comboBox.addItem(sheetName); // Добавляем названия листов в ComboBox
    }
    comboBox.setSelectedIndex(0); // Выбираем первый лист по умолчанию
  } catch (WrongException e) {
    JOptionPane.showMessageDialog(this, "Ой: " + e.getMessage(), "Ой",
                                  JOptionPane.ERROR_MESSAGE);
  }
}
}
