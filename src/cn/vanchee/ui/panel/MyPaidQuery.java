package cn.vanchee.ui.panel;

import cn.vanchee.model.PaidDetail;
import cn.vanchee.model.Resource;
import cn.vanchee.model.User;
import cn.vanchee.ui.MainApp;
import cn.vanchee.ui.table.PaidTableModel;
import cn.vanchee.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author vanchee
 * @date 13-1-31
 * @package cn.vanchee.ui
 * @verson v1.0.0
 */
public class MyPaidQuery extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(MyPaidQuery.class);

    private MainApp mainApp;

    private DigitalTextField jtfId;
    private DigitalTextField jtfIid;
    private JTextField jtfOwner;
    private JTextField jtfFruit;
    private JTextField showDateFrom;
    private JTextField showDateTo;
    private JComboBox jcbCensored;

    private JPanel searchPanel;
    private JScrollPane dataPanel;

    private GridBagConstraints gbc;
    private GridBagLayout gbl;
    private int gridX, gridY, gridWidth, gridHeight, anchor, fill, iPadX, iPadY;
    private double weightX, weightY;
    private Insets insert = null;

    private List<PaidDetail> result;
    private boolean init = true;

    public MyPaidQuery(MainApp mainApp) {
        this.mainApp = mainApp;
        getContent();
    }

    private void getContent() {
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();

        addSearchPanel();
        addDataPanel();

        this.setLayout(gbl);
    }

    private void addSearchPanel() {
        Dimension inputDimension = Constants.getInputDimension();
        Dimension dateDimension = Constants.getDateDimension();

        searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder("查询条件"));
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel jlId = new JLabel("还款单号");
        searchPanel.add(jlId);

        jtfIid = new DigitalTextField();
        jtfIid.setPreferredSize(inputDimension);
        jtfIid.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    refreshData();
                }
            }
        });
        searchPanel.add(jtfIid);

        JLabel jlIid = new JLabel("货号");
        searchPanel.add(jlIid);

        jtfId = new DigitalTextField();
        jtfId.setPreferredSize(inputDimension);
        jtfId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    refreshData();
                }
            }
        });
        searchPanel.add(jtfId);

        JLabel jlOwner = new JLabel("货主");
        searchPanel.add(jlOwner);

        jtfOwner = new JTextField();
        jtfOwner.setPreferredSize(inputDimension);
        jtfOwner.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    refreshData();
                }
            }
        });
        String[] consumers = MyFactory.getConsumerService().listNames();
        AutoCompleteExtender consumerAce = new AutoCompleteExtender(jtfOwner, consumers, null);
        consumerAce.setMatchDataAsync(true);
        consumerAce.setSizeFitComponent();
        consumerAce.setMaxVisibleRows(10);
        consumerAce.setCommitListener(new AutoCompleteExtender.CommitListener() {
            public void commit(String value) {
                jtfOwner.setText(value);
            }
        });
        searchPanel.add(jtfOwner);

        JLabel jlFruit = new JLabel("货品");
        searchPanel.add(jlFruit);

        jtfFruit = new JTextField();
        jtfFruit.setPreferredSize(inputDimension);
        jtfFruit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    refreshData();
                }
            }
        });
        String[] fruits = MyFactory.getFruitService().listNames();
        AutoCompleteExtender fruitAce = new AutoCompleteExtender(jtfFruit, fruits, null);
        fruitAce.setMatchDataAsync(true);
        fruitAce.setSizeFitComponent();
        fruitAce.setMaxVisibleRows(10);
        fruitAce.setCommitListener(new AutoCompleteExtender.CommitListener() {
            public void commit(String value) {
                jtfFruit.setText(value);
            }
        });
        searchPanel.add(jtfFruit);

        JLabel jlCensored = new JLabel("审核状态");
        searchPanel.add(jlCensored);

        String censored[] = new String[]{"全部", "未审", "通过", "不通过"};
        jcbCensored = new JComboBox(censored);
        searchPanel.add(jcbCensored);

        JLabel jlDateFrom = new JLabel("日期");
        searchPanel.add(jlDateFrom);

        DateChooser dateChooserFrom = DateChooser.getInstance("yyyy-MM-dd");
        showDateFrom = new JTextField("开始日期");
        showDateFrom.setPreferredSize(dateDimension);
        dateChooserFrom.register(showDateFrom);
        searchPanel.add(showDateFrom);

        JLabel jlDateTo = new JLabel("——");
        searchPanel.add(jlDateTo);

        DateChooser dateChooserTo = DateChooser.getInstance("yyyy-MM-dd");
        showDateTo = new JTextField("结束日期");
        showDateTo.setPreferredSize(dateDimension);
        dateChooserTo.register(showDateTo);
        searchPanel.add(showDateTo);

        JButton searchBtn = new JButton("查询");
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        searchPanel.add(searchBtn);

        gridX = 0; // X0
        gridY = 0; // Y0
        gridWidth = 1; // 横占一个单元格
        gridHeight = 1; // 列占三个单元格
        weightX = 1.0; // 当窗口放大时，长度随之放大
        weightY = 0.0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 35; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(searchPanel, gbc);
        this.add(searchPanel);
    }

    private void addDataPanel() {

        int id = -1;
        if (!init && !"".equals(jtfId.getText())) {
            try {
                id = Integer.parseInt(jtfId.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "进货号必须是数字");
                return;
            }
        }

        int iid = -1;
        if (!init && !"".equals(jtfIid.getText())) {
            try {
                iid = Integer.parseInt(jtfIid.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "进货号必须是数字");
                return;
            }
        }

        int oid = MyFactory.getOwnerService().getIdByName4Query(jtfOwner.getText());
        int fid = MyFactory.getFruitService().getIdByName4Query(jtfFruit.getText());
        int censored = init ? -1 : jcbCensored.getSelectedIndex() - 1;

        Date f = null;
        String from = showDateFrom.getText();
        if (!from.equals("开始日期") && !"".equals(from)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                f = sdf.parse(from);
            } catch (ParseException e) {
                log.error(e.getMessage());
            }
        }

        Date e = null;
        String end = showDateTo.getText();
        if (!end.equals("结束日期") && !"".equals(end)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                e = sdf.parse(end);
            } catch (ParseException e1) {
                log.error(e1.getMessage());
            }
        }

        User user = MyFactory.getCurrentUser();
        int uid = -1;
        if (!MyFactory.getResourceService()
                .hasRight(MyFactory.getCurrentUser(), Resource.GET_OTHERS_DATA)) {
            uid = user.getId();
        }

        result = MyFactory.getPaidDetailService().queryMyPaidDetail(id, iid, oid, fid, censored, f, e, uid);
        String[] columnNames = new String[]{"还款单号", "货号", "货主", "货品", "还款", "还款日期", "审核状态", "操作", "审核"};
        if (!MyFactory.getResourceService().hasRight(user, Resource.CENSORED)) {
            result = MyFactory.getPaidDetailService().selectCensoredReverse(result, Constants.CENSORED_PASS);
            columnNames = new String[]{"还款单号", "货号", "货主", "货品", "还款", "还款日期", "审核状态", "操作"};
        }
        PaidTableModel paidTableModel = new PaidTableModel(result, columnNames);
        JTable table = new JTable(paidTableModel);
        table.setAutoCreateRowSorter(true);

        int[] colors = new int[result.size()];
        for (int i = 0, length = result.size(); i < length; i++) {
            colors[i] = result.get(i).getColor();
        }
        TableColumnModel tableColumnModel = table.getColumnModel();
        MyColorTableCellRenderer renderer = new MyColorTableCellRenderer();
        renderer.setColor(colors);
        for (int i = 0, length = tableColumnModel.getColumnCount(); i < length; i++) {
            TableColumn tableColumn = tableColumnModel.getColumn(i);
            tableColumn.setCellRenderer(renderer);
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = ((JTable) e.getSource()).getSelectedRow();
                int column = ((JTable) e.getSource()).getSelectedColumn();
                if (e.getClickCount() > 1) {
                    if (column != 7 && column != 8) {
                        toUpdate(row);
                        return;
                    }
                }
                if (column == 7) {
                    delete(row);
                    return;
                }
                if (column == 8) {
                    censored(row);
                    return;
                }
            }
        });
        dataPanel = new JScrollPane(table);
        dataPanel.setViewportView(table);

        gridX = 0; // X1
        gridY = 1; // Y0
        gridWidth = 1; // 横占一个单元格
        gridHeight = 5; // 列占两个单元格
        weightX = 1.0; // 当窗口放大时，长度随之放大
        weightY = 1.0; // 当窗口放大时，高度随之放大
        anchor = GridBagConstraints.NORTH; // 当组件没有空间大时，使组件处在北部
        fill = GridBagConstraints.BOTH; // 当格子有剩余空间时，填充空间
        insert = new Insets(0, 0, 0, 10); // 组件彼此的间距
        iPadX = 0; // 组件内部填充空间，即给组件的最小宽度添加多大的空间
        iPadY = 0; // 组件内部填充空间，即给组件的最小高度添加多大的空间
        gbc = new GridBagConstraints(gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor,
                fill, insert, iPadX, iPadY);
        gbl.setConstraints(dataPanel, gbc);
        this.add(dataPanel);

        init = false;
    }

    private void refreshData() {
        this.remove(dataPanel);
        addDataPanel();
        this.validate();
        this.repaint();
    }

    private void toUpdate(int row) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.MY_PAID_W)) {
            return;
        }
        PaidDetail selectedRow = result == null ? null : result.get(row);
        if (selectedRow != null) {
            PaidAdd paidAdd = new PaidAdd(mainApp);
            paidAdd.update(selectedRow);
            mainApp.changeRightPanel(paidAdd);
        }
    }

    private void delete(int row) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.MY_PAID_W)) {
            return;
        }
        PaidDetail selectedRow = result == null ? null : result.get(row);
        if (selectedRow != null) {
            if (JOptionPane.showConfirmDialog(null, "确定要删除？") == JOptionPane.OK_OPTION) {
                if (MyFactory.getPaidDetailService().delete(selectedRow.getId())) {
                    JOptionPane.showMessageDialog(null, "删除成功！");
                    refreshData();
                }
            }
        }
    }

    private void censored(int row) {
        if (!MyFactory.getResourceService().hasRight(MyFactory.getCurrentUser(), Resource.CENSORED)) {
            return;
        }
        PaidDetail selectedRow = result == null ? null : result.get(row);
        if (selectedRow != null) {
            int result = JOptionPane.showConfirmDialog(null, "点击“是”审核通过，“否”审核不通过，“取消”则不作任何操作");
            if (result != JOptionPane.CANCEL_OPTION) {
                if (MyFactory.getPaidDetailService().censored(selectedRow.getId(),
                        result == JOptionPane.YES_OPTION ? true : false)) {
                    JOptionPane.showMessageDialog(null, "审核成功！");
                    refreshData();
                    ;
                }
            }
        }
    }
}
