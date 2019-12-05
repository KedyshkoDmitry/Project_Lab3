package bsu.rfe.java.group8.lab3.Kedyshko.var7;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel
{
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;
    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients)
    {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }
    public Double getFrom() {        return from;    }
    public Double getTo() {        return to;    }
    public Double getStep() {        return step;    }
    public int getColumnCount() {        return 3;    }

    public int getRowCount()
    {
        return new Double(Math.ceil((to-from)/step)).intValue() + 1;
    }

    public int checkSequence(double number)
    {
        int flag = 0;
        int k = 0;
        int counter = 0;

        int dim = String.valueOf(Math.abs(number)).length();
        int[] massiveNumber = new int[dim];
        for(int i = 0; i < dim - k; i++)
        {
            if(String.valueOf(Math.abs(number)).charAt(i) > 47)
                massiveNumber[i] =  String.valueOf(Math.abs(number)).charAt(i + k) - 48;
            else
            {
                k++;
                massiveNumber[i] =  String.valueOf(Math.abs(number)).charAt(i + k) - 48;
            }
        }

        for (int i = 0; i < dim - 1; i++)
        {
            System.out.println(counter + " " + massiveNumber[i]);
            if (massiveNumber[i] == massiveNumber[i + 1] - 1)
            {
                counter++;
            }
            else
                counter = 0;
            if (counter >= 2)
            {
                flag = 1;
                break;
            }
        }
        return flag;
    }

    public Object getValueAt(int row, int col)
    {
        double x = from + step*row;
        Double result = -123.456;
        if (col==0)
        {
            return x;
        }
        if (col==1)
        {
// Если запрашивается значение 2-го столбца, то это значение
// многочлена

// Вычисление значения в точке по схеме Горнера.
// Вспомнить 1-ый курс и реализовать
// ...
            return result;
        }
        else
        {
            return checkSequence(result);
        }
    }
    public String getColumnName(int col)
    {
        switch (col)
        {
            case 0:
                return "Значение X";
            case 1:
                return "Значение многочлена";
            default:
                return "Последовательный ряд?";
        }
    }
    public Class<?> getColumnClass(int col)
    {
        return Double.class;
    }
}

