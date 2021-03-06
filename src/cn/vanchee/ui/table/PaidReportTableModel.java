package cn.vanchee.ui.table;

import cn.vanchee.model.PaidVo;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author vanchee
 * @date 13-2-8
 * @package cn.vanchee.test
 * @verson v1.0.0
 */
public class PaidReportTableModel implements TableModel {

    private List<PaidVo> paidVoList;
    private String[] columnNames;
    private Class<?>[] columns;

    public PaidReportTableModel(List<PaidVo> paidVoList, String[] columnNames) {
        this.paidVoList = paidVoList;
        this.columnNames = columnNames;
        columns = new Class[]{
                String.class,
                Double.class,
                Double.class,
                Double.class};
    }

    public int getRowCount() {
        return paidVoList.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < 0 || columnIndex > columns.length) {
            return null;
        }
        return columns[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        PaidVo paidVo = paidVoList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return paidVo.getName();
            case 1:
                return paidVo.getShouldPaid();
            case 2:
                return paidVo.getHadPaid();
            case 3:
                return paidVo.getShouldPaid() - paidVo.getHadPaid();
            default:
                return "";
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex != 0) {
            return false;
        }
        return false;
    }

    public String getColumnName(int column) {
        if (column < 0 || column > columns.length)
            return null;
        return columnNames[column];
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }
}