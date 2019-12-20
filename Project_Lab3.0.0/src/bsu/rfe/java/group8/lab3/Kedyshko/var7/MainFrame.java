package bsu.rfe.java.group8.lab3.Kedyshko.var7;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import javax.swing.JTextField;
@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
    // РАЗМЕРЫ ОКНА
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private Double[] coefficients;
    private JFileChooser fileChooser = null;

    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;

    // ПОЛЯ ВВОДА
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box horizontalBoxResult;

    // ВИЗУАЛИЗАТОР ЯЧЕЕК И МОДЕЛЬ ДАННЫХ
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    private GornerTableModel data;

    // КОНСТРУКТОР
    public MainFrame(Double[] coefficients)
    {
        super("Табулирование многочлена на отрезке по схеме Горнера");
        this.coefficients = coefficients;

        // ОТЦЕНТРОВКА И ЗАДАЧА РАЗМЕРОВ ОКНА
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

        // МЕНЮ
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);
        JMenu referenceMenu = new JMenu("Справка");
        menuBar.add(referenceMenu);

        // ПОДМЕНЮ
        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл")
        {
            public void actionPerformed(ActionEvent event)
            {
                if (fileChooser == null)
                {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    saveToTextFile(fileChooser.getSelectedFile());
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);

        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика")
        {
            public void actionPerformed(ActionEvent event)
            {
                if (fileChooser == null)
                {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    saveToGraphicsFile(fileChooser.getSelectedFile());
            }
        };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);

        Action searchValueAction = new AbstractAction("Найти значение многочлена")
        {
            public void actionPerformed(ActionEvent event)
            {
                String value =  JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска",
                                "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedle(value);
                getContentPane().repaint();
            }
        };
        searchValueMenuItem = tableMenu.add(searchValueAction);
        searchValueMenuItem.setEnabled(true);

        Action aboutProgramAction = new AbstractAction("О программе")
        {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        new String[] {"Кедышко Дмитрий", "8 группа"}, "Окно сообщения", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        searchValueMenuItem = referenceMenu.add(aboutProgramAction);

        // ПОЛЯ ВВОДА ГРАНИЦ И ШАГА
        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        JLabel labelForTo = new JLabel("до:");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        JLabel labelForStep = new JLabel("с шагом:");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());

        Box horizontalBoxRange = Box.createHorizontalBox();
        horizontalBoxRange.setBorder(BorderFactory.createBevelBorder(1));
        horizontalBoxRange.add(Box.createHorizontalGlue());
        horizontalBoxRange.add(labelForFrom);
        horizontalBoxRange.add(Box.createHorizontalStrut(10));
        horizontalBoxRange.add(textFieldFrom);
        horizontalBoxRange.add(Box.createHorizontalStrut(20));
        horizontalBoxRange.add(labelForTo);
        horizontalBoxRange.add(Box.createHorizontalStrut(10));
        horizontalBoxRange.add(textFieldTo);
        horizontalBoxRange.add(Box.createHorizontalStrut(20));
        horizontalBoxRange.add(labelForStep);
        horizontalBoxRange.add(Box.createHorizontalStrut(10));
        horizontalBoxRange.add(textFieldStep);
        horizontalBoxRange.add(Box.createHorizontalGlue());

        // РАЗМЕР И РАСПОЛОЖЕНИЕ ОБЛАСТИ ПОЛЕЙ ВВОДА
        horizontalBoxRange.setPreferredSize(new Dimension(new Double(horizontalBoxRange.getMaximumSize().getWidth()).intValue(),
                new Double(horizontalBoxRange.getMinimumSize().getHeight()).intValue() * 2));
        getContentPane().add(horizontalBoxRange, BorderLayout.NORTH);

        // ПОЛЯ КНОПОК
        JButton buttonCalculate = new JButton("Вычислить");
        buttonCalculate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                try
                {
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);
                    JTable table = new JTable(data);
                    table.setDefaultRenderer(Double.class, renderer);
                    table.setRowHeight(30);
                    horizontalBoxResult.removeAll();
                    horizontalBoxResult.add(new JScrollPane(table));
                    getContentPane().validate();
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой",
                            "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                horizontalBoxResult.removeAll();
                horizontalBoxResult.add(new JPanel());
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                getContentPane().validate();
            }
        });

        Box horizontalBoxButtons = Box.createHorizontalBox();
        horizontalBoxButtons.setBorder(BorderFactory.createBevelBorder(1));
        horizontalBoxButtons.add(Box.createHorizontalGlue());
        horizontalBoxButtons.add(buttonCalculate);
        horizontalBoxButtons.add(Box.createHorizontalStrut(30));
        horizontalBoxButtons.add(buttonReset);
        horizontalBoxButtons.add(Box.createHorizontalGlue());

        // РАЗМЕР И РАСПОЛОЖЕНИЕ ОБЛАСТИ КНОПОК
        horizontalBoxButtons.setPreferredSize(new Dimension(new Double(horizontalBoxButtons.getMaximumSize().getWidth()).intValue(),
                new Double(horizontalBoxButtons.getMinimumSize().getHeight()).intValue() * 2));
        getContentPane().add(horizontalBoxButtons, BorderLayout.SOUTH);

        // РАСПОЛОЖЕНИЕ ОБЛАСТИ ПОЛЯ ВЫВОДА
        horizontalBoxResult = Box.createHorizontalBox();
        horizontalBoxResult.add(new JPanel());
        getContentPane().add(horizontalBoxResult, BorderLayout.CENTER);
    }

    // ЗАПИСЬ В БИНАРНЫЙ ФАЙЛ
    protected void saveToGraphicsFile(File selectedFile)
    {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            for (int i = 0; i < data.getRowCount(); i++)
            {
                out.writeDouble((Double) data.getValueAt(i, 0));
                out.writeDouble((Double) data.getValueAt(i, 1));
            }
            out.close();
        } catch (Exception e) { }
    }

    // ЗАПИСЬ В ТЕКСТОВЫЙ ФАЙЛ
    protected void saveToTextFile(File selectedFile)
    {
        try {
            PrintStream out = new PrintStream(selectedFile);
            out.println("Результаты табулирования многочлена по схеме Горнера");
            out.print("Многочлен: ");
            for (int i = 0; i < coefficients.length; i++)
            {
                out.print(coefficients[i] + "*X^" +
                        (coefficients.length - i - 1));
                if (i != coefficients.length - 1)
                    out.print(" + ");
            }
            out.println("");
            out.println("Интервал от " + data.getFrom() + " до " +
                    data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");
            for (int i = 0; i < data.getRowCount(); i++)
            {
                out.println("Значение в точке " + data.getValueAt(i, 0) + " равно " + data.getValueAt(i, 1));
            }
            out.close();
        } catch (FileNotFoundException e) { }
    }

    // ГЛАВНЫЙ МЕТОД КЛАССА
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
            System.exit(-1);
        }
        Double[] coefficients = new Double[args.length];
        int i = 0;
        try
        {
            for (String arg : args)
            {
                coefficients[i++] = Double.parseDouble(arg);
            }
        }
        catch (NumberFormatException ex)
        {
            System.out.println("Ошибка преобразования строки '" + args[i] + "' в число типа Double");
            System.exit(-2);
        }
        MainFrame frame = new MainFrame(coefficients);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
