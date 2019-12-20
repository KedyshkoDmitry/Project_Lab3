package bsu.rfe.java.group8.lab3.Kedyshko.var7;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {

    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;
    private static Double x, polynomialValue;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public static Double getX() {
        return x;
    }

    public static Double getPolynomialValue() {
        return polynomialValue;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getRowCount() {
        return new Double(Math.ceil((to - from)/step)).intValue() + 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
        x = from + step * row;
        polynomialValue = 0.0;
        for (int i = 0; i < coefficients.length; i++)
            polynomialValue = polynomialValue * x + coefficients[i];
        switch (col) {
            case 0: return x;
            case 1: return polynomialValue;
            default: return ifConsecutive(polynomialValue);
        }
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0: return "Значение X";
            case 1: return "Значение многочлена";
            default: return "Последовательный ряд?";
        }
    }

    public Class<?> getColumnClass(int col) {
        switch (col) {
            case 0:
            case 1: return Double.class;
            default: return Boolean.class;
        }
    }

    public boolean ifConsecutive(Double number) {

        int integer = Math.abs(number.intValue());
        Double tempFract = number - integer;
        tempFract *= 1e5;
        int fract = tempFract.intValue();

        int nNumbersInInteger = String.valueOf(integer).length();
        int nNumbersInFract = 5;

        int[] numbersFromInt = new int[nNumbersInInteger];
        int[] numbersFromFract = new int[nNumbersInFract];

        for (int i = nNumbersInInteger - 1; i >= 0; i--) {
            numbersFromInt[i] = integer % 10;
            integer /= 10;
        }

        for (int i = nNumbersInFract - 1; i >= 0; i--) {
            numbersFromFract[i] = fract % 10;
            fract /= 10;
        }

        if (nNumbersInInteger >= 3)
            for (int j = 0; j < nNumbersInInteger - 2; j++)
                if ((numbersFromInt[j] + 1) == numbersFromInt[j+1] && (numbersFromInt[j] + 2) == (numbersFromInt[j+2]))
                    return true;

        for (int j = 0; j < nNumbersInFract - 2; j++)
            if ((numbersFromFract[j] + 1) == numbersFromFract[j+1] && (numbersFromFract[j] + 2) == (numbersFromFract[j+2]))
                return true;

        return false;
    }

}