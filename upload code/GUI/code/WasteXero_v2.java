package WasteXero_v2;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.*;

import javax.imageio.ImageIO;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class WasteXero_v2 extends JFrame{
    /**
     * some useful constants
     */
    private final String [] op = {"Yes","Cancel"};
    private final String path = "csv_file\\";//The address of the file
    private final String[] columns={"ID","FOOD","TIMES","WEIGHT","CO2"};
    private int[] columnWidth = {20, 200, 35, 35, 40};

    /**
     * screen data
     */
    private int windowWidth;
    private int windowHeight;
    private Toolkit kit;
    private Dimension screenSize;
    private int screenWidth;
    private int screenHeight;

    private List<dateList> nameSet;
    private File[] fileArray;
    private Map<String,List<Double>> foodStatistics;

    /**
     * top panel components
     */
    private JPanel pnlTopOverall;
    private JButton btnRefresh;//refresh btn
    private DefaultListModel leftListModel = new DefaultListModel();
    /**
     * left panel components
     */
    private BorderPanel pnlLeftOverall;
    private JList<String> leftList;//list of different date

    public WasteXero_v2(){
        nameSet = new ArrayList<>();
        foodStatistics = new HashMap<>();

        this.setTitle("WasteXero");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(true);
        this.setSize(900, 600);
        // 屏幕居中
        windowWidth = this.getWidth(); // 获得窗口宽
        windowHeight = this.getHeight(); // 获得窗口高
        kit = Toolkit.getDefaultToolkit(); // 定义工具包
        screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
        screenWidth = screenSize.width; // 获取屏幕的宽
        screenHeight = screenSize.height; // 获取屏幕的高
        this.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight
                / 2 - windowHeight / 2);
        this.setLocationRelativeTo(null);

        loadFileName();
        initData();
        initUI();

        this.setResizable(false);

    }

    private void loadFileName(){
        try {
            File file = new File(path);// get the folder list
            fileArray = file.listFiles();// the array of files, array.getname() can get names
            assert fileArray != null;
            if (fileArray.length != 0) {
                for (File aFile : fileArray) { // record weights in the weightMap, if food exists, then add new value, else add a new key
                    String name = aFile.getName().split(".csv")[0];
                    dateList date = new dateList(name);
                    if (!nameSet.contains(date))
                        nameSet.add(date);
                }
            }
        }
        catch (NullPointerException e1){
            System.out.println("No data!");
        }
        catch (Exception e){
            System.out.println("Something unexpected happened!");
        }
        finally {
            nameSet.sort(new Comparator<dateList>() {
                @Override
                public int compare(dateList name1, dateList name2) {
                    if (!name1.year.equals(name2.year))
                        return Integer.parseInt(name1.year) - Integer.parseInt(name2.year);
                    else{
                        if (name1.monthNum != name2.monthNum)
                            return name1.monthNum - name2.monthNum;
                        else
                            return Integer.parseInt(name1.day) - Integer.parseInt(name2.day);
                    }
                }
            });
            for (dateList name1 : nameSet) {
                leftListModel.addElement(name1.toString());
            }
        }
    }

    private double overallWeight;
    private int overallTimes;
    private double overallCO2;
    private void initData(){
        overallCO2 = 0;
        overallTimes = 0;
        overallWeight = 0;
        try {
            if (fileArray.length != 0) {
                for (File aFile : fileArray) { // record weights in the weightMap, if food exists, then add new value, else add a new key
                    List<List<String>> CSVContent = readCSV(aFile.getName());

                    if (fileArray != null) {
                        for (int i = 0; i < CSVContent.size(); i++) {
                            List<Double> aList;
                            if (i == 0) {
                                StringBuilder sb = new StringBuilder(CSVContent.get(i).get(0));
//                                while (!((sb.charAt(0) >= 'a' && sb.charAt(0) <= 'z') || (sb.charAt(0) >= 'A' && sb.charAt(0) <= 'Z')))
//                                    sb.deleteCharAt(0);

                                if (foodStatistics.containsKey(sb.toString())){
                                    aList = foodStatistics.get(sb.toString());
                                    aList.set(0,aList.get(0) + Double.parseDouble(CSVContent.get(i).get(1)));
                                    aList.set(1,aList.get(1) + Double.parseDouble(CSVContent.get(i).get(2).substring(0,CSVContent.get(i).get(2).length() - 1)));
                                    aList.set(2,aList.get(2) + Double.parseDouble(CSVContent.get(i).get(3).substring(0,CSVContent.get(i).get(3).length() - 1)));

                                }
                                else{
                                    aList = new ArrayList<>();
                                    aList.add(Double.parseDouble(CSVContent.get(i).get(1)));
                                    aList.add(Double.parseDouble(CSVContent.get(i).get(2).substring(0,CSVContent.get(i).get(2).length() - 1)));
                                    aList.add(Double.parseDouble(CSVContent.get(i).get(3).substring(0,CSVContent.get(i).get(3).length() - 1)));

                                }
                                overallTimes += Double.parseDouble(CSVContent.get(i).get(1));
                                overallWeight += Double.parseDouble(CSVContent.get(i).get(2).substring(0,CSVContent.get(i).get(2).length() - 1));
                                overallCO2 += Double.parseDouble(CSVContent.get(i).get(3).substring(0,CSVContent.get(i).get(3).length() - 1));
                            }
                            else{
                                if (foodStatistics.containsKey(CSVContent.get(i).get(0))){
                                    aList = foodStatistics.get(CSVContent.get(i).get(0));
                                    aList.set(0,aList.get(0) + Double.parseDouble(CSVContent.get(i).get(1)));
                                    aList.set(1,aList.get(1) + Double.parseDouble(CSVContent.get(i).get(2).substring(0,CSVContent.get(i).get(2).length() - 1)));
                                    aList.set(2,aList.get(2) + Double.parseDouble(CSVContent.get(i).get(3).substring(0,CSVContent.get(i).get(3).length() - 1)));

                                }
                                else{
                                    aList = new ArrayList<>();
                                    aList.add(Double.parseDouble(CSVContent.get(i).get(1)));
                                    aList.add(Double.parseDouble(CSVContent.get(i).get(2).substring(0,CSVContent.get(i).get(2).length() - 1)));
                                    aList.add(Double.parseDouble(CSVContent.get(i).get(3).substring(0,CSVContent.get(i).get(3).length() - 1)));

                                }
                                overallTimes += Double.parseDouble(CSVContent.get(i).get(1));
                                overallWeight += Double.parseDouble(CSVContent.get(i).get(2).substring(0,CSVContent.get(i).get(2).length() - 1));
                                overallCO2 += Double.parseDouble(CSVContent.get(i).get(3).substring(0,CSVContent.get(i).get(3).length() - 1));
                            }
                            foodStatistics.put(CSVContent.get(i).get(0),aList);
                        }
                    }
                }
            }
        }
        catch (NullPointerException e1){
            System.out.println("No data!");
        }
        catch (Exception e){
            System.out.println("Something unexpected happened!");
        }
    }

    private class dateList{

        private final String[] monthTransfer = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        private String year;
        private String month;
        private String day;
        private int monthNum;

        dateList(String strDate){
            String []time = strDate.split("_");

            try{
                this.year = time[0];
                this.day = time[2];
                monthNum = Integer.parseInt(time[1]);
                this.month = monthTransfer[monthNum - 1];
            }
            catch (Exception e){
                System.out.println("File name error.");
            }
        }

        public String toString(){
            if (this.day.length() == 2)
                return this.month + " " + this.day + ", " + this.year;
            return this.month + "   " + this.day + ", " + this.year;
        }

    }

    private void initUI(){
        topPane();
        leftPane();
        rightPane();
    }

    /**
     * initialize top pane
     */
    private void topPane(){
        pnlTopOverall = new JPanel(null);
        pnlTopOverall.setLocation(0,0);
        pnlTopOverall.setSize(265,50);

        btnRefresh = new JButton("Refresh");

        this.add(pnlTopOverall);
        pnlTopOverall.add(btnRefresh);

        btnRefresh.setBounds(30,10,205,35);

        btnRefresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                Process proc;
                try {
                    proc = Runtime.getRuntime().exec("python D:\\WasteXero2.0\\download.py");// 执行py文件
                    //用输入输出流来截取结果
                    BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                    in.close();
                    proc.waitFor();
                } catch (IOException e2) {
                    e2.printStackTrace();
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }

                leftListModel.clear();
                nameSet.clear();
                leftList.setModel(leftListModel);
                loadFileName();
                leftList.setModel(leftListModel);
                rightTabbedPane.setEnabledAt(1, false);
                rightTabbedPane.setTitleAt(1, "Branch");
                rightTabbedPane.setSelectedIndex(0);

                initData();

                a.setText("The overall number of waste species is:  " + foodStatistics.size());
                b.setText("The overall count of statistics is:  " + overallTimes);
                c.setText("The overall waste weight is about:  "
                        + new DecimalFormat("0.0000").format(overallWeight)+" g");
                d.setText("The total CO2 emissions are about:  "
                        + new DecimalFormat("0.0000").format(overallCO2)+" g");
            }
        });
    }

    /**
     * initialize left pane
     */
    private void leftPane(){
        pnlLeftOverall = new BorderPanel("Date");
        leftList = new JList<>(leftListModel);
        leftList.setFont(new Font("Times New Roman", Font.BOLD,30));
        leftList.setBackground(Color.WHITE);
        leftList.setVisibleRowCount(4);
        leftList.setFixedCellHeight(40);
        leftList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        leftList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane jspList=new JScrollPane(leftList);
        jspList.setBounds(10, 20, 245, 490);

        pnlLeftOverall.add(jspList);
        pnlLeftOverall.setLayout(null);

        this.add(pnlLeftOverall);
        pnlLeftOverall.setLocation(0,47);
        pnlLeftOverall.setSize(260,515);

        leftList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (leftList.getSelectedValue() == null || leftList.getSelectedValue().equals(""))
                    return;

                String getPath = getWholeName(leftList.getSelectedValue());
                List<List<String>> CSVContent = readCSV(getPath);

                if (leftList.getSelectedValue() != null && !leftList.getSelectedValue().equals(""))
                    model=new DefaultTableModel(columns,CSVContent.size());
                else
                    model=new DefaultTableModel(columns,1);
                jtable.setModel(model);

                if (fileArray != null) {
                    if (fileArray.length != 0) {
                        rightTabbedPane.setSelectedIndex(1);
                    }
                    if (leftList.getSelectedValue() != null) {
                        rightTabbedPane.setTitleAt(1, leftList.getSelectedValue());
                        rightTabbedPane.setEnabledAt(1, true);
                        for (int i = 0; i < CSVContent.size(); i++) {
                            jtable.setValueAt(i + 1, i, 0);
                            if (i == 0) {
                                StringBuilder sb = new StringBuilder(CSVContent.get(i).get(0));
//                                while (!((sb.charAt(0) >= 'a' && sb.charAt(0) <= 'z') || (sb.charAt(0) >= 'A' && sb.charAt(0) <= 'Z')))
//                                    sb.deleteCharAt(0);
                                jtable.setValueAt(sb.toString(), i, 1);
                            }
                            else
                                jtable.setValueAt(CSVContent.get(i).get(0), i, 1);

                            jtable.setValueAt(CSVContent.get(i).get(1), i, 2);
                            jtable.setValueAt(CSVContent.get(i).get(2), i, 3);
                            jtable.setValueAt(CSVContent.get(i).get(3), i, 4);
                        }
                    }
                }

                //自动调整
                //FitTableColumns(jtable);

                TableColumnModel tcm = jtable.getColumnModel();
                for (int i = 0; i < 5;i++){
                    TableColumn tc =tcm.getColumn(i);
                    tc.setPreferredWidth(columnWidth[i]);
                }
            }
        });
    }

    public void FitTableColumns(JTable myTable) {               //設置table的列寬隨內容調整
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();
        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(
                    column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(myTable,
                            column.getIdentifier(), false, false, -1, col)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++){
                int preferedWidth = (int) myTable.getCellRenderer(row, col)
                        .getTableCellRendererComponent(myTable,
                                myTable.getValueAt(row, col), false, false,
                                row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column);
            column.setWidth(width + myTable.getIntercellSpacing().width);
        }
    }

    private List<List<String>> readCSV(String filePath){
        List<List<String>> CSVContent = new ArrayList<>();
        File csv = new File(path + filePath);

        try{
            BufferedReader textFile = new BufferedReader(new FileReader(csv));
            String lineData;

            boolean read = false;
            while ((lineData = textFile.readLine()) != null){
                if (!read) {
                    read = true;
                    continue;
                }
                String []Data = lineData.replace('_',' ').split(",");
                List<String> datalist = new ArrayList<>(Arrays.asList(Data));
                CSVContent.add(datalist);
            }
            textFile.close();
        }catch (FileNotFoundException e1){
            System.out.println("No such Files!");
        }catch (IOException e2){
            System.out.println("IO Exception!");
        }catch (Exception e3){
            System.out.println("Error!");
        }
        return CSVContent;
    }

    /**
     * right panel components
     */
    private BorderPanel pnlRightOverall;
    private JTabbedPane rightTabbedPane;
    private JPanel pnlOverview;
    private JPanel pnlBranch;
    private JLabel timeLabel;
    private JLabel a;
    private JLabel b;
    private JLabel c;
    private JLabel d;
    private DefaultPieDataset dataSet;
    private JButton btnPieTimes;
    private JButton btnPieWeight;
    private JButton btnPieCO2;
    private JTable jtable=null;
    private DefaultTableModel model;
    private Timer time;
    private JLabel tips;
    private JPanel pnlTable;
    /**
     * initialize right pane
     */
    private void rightPane(){
        pnlRightOverall = new BorderPanel("Statistics");
        this.add(pnlRightOverall);
        pnlRightOverall.setLocation(266,5);
        pnlRightOverall.setSize(615,555);
        pnlRightOverall.setLayout(null);

        pnlOverview=new JPanel(null);
        JScrollPane jspOverview = new JScrollPane(pnlOverview);
        jspOverview.setBounds(10, 20, 597, 525);

        a = new JLabel("The overall number of waste species is:  " + foodStatistics.size());
        pnlOverview.add(a);
        a.setBounds(30,20,1000,25);
        a.setFont(new Font("Times New Roman",Font.PLAIN,20));

        b = new JLabel("The overall count of statistics is:  " + overallTimes);
        pnlOverview.add(b);
        b.setBounds(30,55,1000,25);
        b.setFont(new Font("Times New Roman",Font.PLAIN,20));

        c = new JLabel("The overall waste weight is about:  "
                + new DecimalFormat("0.0000").format(overallWeight)+" g");
        pnlOverview.add(c);
        c.setBounds(30,90,1000,25);
        c.setFont(new Font("Times New Roman",Font.PLAIN,20));

        d = new JLabel("The total CO2 emissions are about:  "
                + new DecimalFormat("0.0000").format(overallCO2)+" g");
        pnlOverview.add(d);
        d.setBounds(30,125,1000,25);
        d.setFont(new Font("Times New Roman",Font.PLAIN,20));

        pnlBranch = new JPanel(null);
        JScrollPane jsp3 = new JScrollPane(pnlBranch);
        jsp3.setBounds(0, 20, 597, 505);

        rightTabbedPane = new JTabbedPane();
        rightTabbedPane.setLocation(5,15);
        rightTabbedPane.setSize(605,535);
        rightTabbedPane.addTab("Overview",null, jspOverview,"First panel");
        rightTabbedPane.addTab("Branch",null, jsp3,"Second panel");
        rightTabbedPane.setEnabledAt(1,false);
        pnlRightOverall.add(rightTabbedPane);

        timeLabel = getTimeLabel();
        pnlRightOverall.add(timeLabel);

        jtable =new JTable(model){
            public boolean isCellEditable(int rowIndex, int ColIndex){
                return false;
            }
        };

        jtable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtable.setRowHeight(30);

        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        jtable.setDefaultRenderer(Object.class, r);

        if (leftList.getSelectedValue() != null && !leftList.getSelectedValue().equals("")) {
            String getPath = getWholeName(leftList.getSelectedValue());
            List<List<String>> CSVContent = readCSV(getPath);
            model = new DefaultTableModel(columns, CSVContent.size());
        }
        else
            model=new DefaultTableModel(columns,1);
        jtable.setModel(model);

        TableColumnModel columnModel = jtable.getColumnModel();
        int count=columnModel.getColumnCount();
        for(int i=0; i<count; i++){
            javax.swing.table.TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(columnWidth[i]);
        }
        pnlBranch.setLayout(new BorderLayout());

        JTableHeader myt = jtable.getTableHeader();
        myt.setFont(new Font("Times New Roman", Font.BOLD, 20));
        jtable.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        pnlTable = new JPanel(new BorderLayout());

        pnlBranch.add(pnlTable, BorderLayout.CENTER);
        pnlBranch.setAlignmentX(5);
        pnlBranch.setAlignmentY(50);

        pnlTable.add(myt, BorderLayout.NORTH);
        pnlTable.add(jtable, BorderLayout.CENTER);

        jtable.setBounds(5,5,587,495);

        btnPieTimes = new JButton("Pie Chart of amount");
        pnlOverview.add(btnPieTimes);
        btnPieTimes.setBounds(30,160,150,30);

        btnPieWeight = new JButton("Pie Chart of weights");
        pnlOverview.add(btnPieWeight);
        btnPieWeight.setBounds(30,195,150,30);

        btnPieCO2 = new JButton("Pie Chart of CO2");
        pnlOverview.add(btnPieCO2);
        btnPieCO2.setBounds(30,230,150,30);

        jtable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() >= 2) {
                    int column = jtable.getSelectedColumn();
                    if (column > 1) {

                        int height = 500;
                        int width = 700;

                        JFrame frameHistogram = new JFrame();//构造一个新的JFrame，作为新窗口。
                        frameHistogram.setBounds(// 让新窗口与SwingTest窗口示例错开50像素。
                                new Rectangle(
                                        screenWidth / 2 - width / 2,
                                        screenHeight / 2 - height / 2,
                                        width, height)
                        );

                        frameHistogram.setTitle(columns[column] + " statistics on " + leftList.getSelectedValue());
                        frameHistogram.setLayout(null);

                        JPanel pnlHistogram = (JPanel) frameHistogram.getContentPane();
                        pnlHistogram.setLayout(new BorderLayout());

                        String getPath = getWholeName(leftList.getSelectedValue());
                        List<List<String>> CSVContent = readCSV(getPath);

                        BarChart fm = new BarChart(CSVContent,column);

                        JScrollPane jsp4=new JScrollPane(fm.frame1);
                        jsp4.setBounds(0, 0, 245, 490);
                        pnlHistogram.add("Center", jsp4);

                        frameHistogram.setVisible(true);
                        frameHistogram.setResizable(true);
                    }
                }


            }
        });
        btnPieTimes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fileArray == null){
                    JOptionPane.showMessageDialog(null, "No Data Found!",
                            "Empty", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    if (fileArray.length == 0) {
                        JOptionPane.showMessageDialog(null, "No Data Found!",
                                "Empty", JOptionPane.WARNING_MESSAGE);
                    } else {
                        dataSet = new DefaultPieDataset();
                        if (foodStatistics.keySet().size() != 0) {
                            for (String k : foodStatistics.keySet()) {
                                dataSet.setValue(k + '(' + foodStatistics.get(k).get(0)
                                        + "/" + new DecimalFormat("0.00").format(
                                        foodStatistics.get(k).get(0) * 100.0 / overallTimes)
                                        + "%)",foodStatistics.get(k).get(0));
                            }
                        }
                        JFreeChart chart = ChartFactory.createPieChart("Percentages of total times",
                                dataSet, true, true, true);
                        Font ftt = new Font("黑体", Font.BOLD, 20);
                        Font ft = new Font("SimSun", Font.PLAIN, 15);

                        LegendTitle legend;
                        TextTitle txtTitle;
                        PiePlot categoryPlot;

                        legend = chart.getLegend();
                        txtTitle = chart.getTitle();
                        categoryPlot = (PiePlot) chart.getPlot();

                        txtTitle.setFont(ftt); // 设置标题字体
                        categoryPlot.setLabelFont(ft);// 设置图片上固定指示文字字体
                        legend.setItemFont(ft);// 设置图例字体

                        ChartFrame chartFrame = new ChartFrame("Waste Pie Chart", chart);
                        chartFrame.pack();

                        chartFrame.setSize(860, 540);
                        // 屏幕居中
                        chartFrame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight
                                / 2 - windowHeight / 2);
                        chartFrame.setLocationRelativeTo(null);
                        chartFrame.setVisible(true);
                    }
                }
            }
        });
        btnPieWeight.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fileArray == null){
                    JOptionPane.showMessageDialog(null, "No Data Found!",
                            "Empty", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    if (fileArray.length == 0) {
                        JOptionPane.showMessageDialog(null, "No Data Found!",
                                "Empty", JOptionPane.WARNING_MESSAGE);
                    } else {
                        dataSet = new DefaultPieDataset();
                        if (foodStatistics.keySet().size() != 0) {
                            for (String k : foodStatistics.keySet()) {
                                dataSet.setValue(k + '(' +
                                        new DecimalFormat("0.00").format(foodStatistics.get(k).get(1))
                                        + "g/" + new DecimalFormat("0.00").format(
                                        foodStatistics.get(k).get(1) * 100.0 / overallWeight)
                                        + "%)",foodStatistics.get(k).get(1));
                            }
                        }
                        JFreeChart chart = ChartFactory.createPieChart("Percentages of total weights",
                                dataSet, true, true, true);
                        Font ftt = new Font("黑体", Font.BOLD, 20);
                        Font ft = new Font("SimSun", Font.PLAIN, 15);

                        LegendTitle legend;
                        TextTitle txtTitle;
                        PiePlot categoryPlot;

                        legend = chart.getLegend();
                        txtTitle = chart.getTitle();
                        categoryPlot = (PiePlot) chart.getPlot();

                        txtTitle.setFont(ftt); // 设置标题字体
                        categoryPlot.setLabelFont(ft);// 设置图片上固定指示文字字体
                        legend.setItemFont(ft);// 设置图例字体

                        ChartFrame chartFrame = new ChartFrame("Waste Pie Chart", chart);
                        chartFrame.pack();

                        chartFrame.setSize(860, 540);
                        // 屏幕居中
                        chartFrame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight
                                / 2 - windowHeight / 2);
                        chartFrame.setLocationRelativeTo(null);
                        chartFrame.setVisible(true);
                    }
                }
            }
        });
        btnPieCO2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fileArray == null){
                    JOptionPane.showMessageDialog(null, "No Data Found!",
                            "Empty", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    if (fileArray.length == 0) {
                        JOptionPane.showMessageDialog(null, "No Data Found!",
                                "Empty", JOptionPane.WARNING_MESSAGE);
                    } else {
                        dataSet = new DefaultPieDataset();
                        if (foodStatistics.keySet().size() != 0) {
                            for (String k : foodStatistics.keySet()) {
                                dataSet.setValue(k + '(' +
                                        new DecimalFormat("0.00").format(foodStatistics.get(k).get(2))
                                        + "g/" + new DecimalFormat("0.00").format(
                                        foodStatistics.get(k).get(2) * 100.0 / overallCO2)
                                        + "%)",foodStatistics.get(k).get(2));
                            }
                        }
                        JFreeChart chart = ChartFactory.createPieChart("Percentages of total CO2 emission",
                                dataSet, true, true, true);
                        Font ftt = new Font("黑体", Font.BOLD, 20);
                        Font ft = new Font("SimSun", Font.PLAIN, 15);

                        LegendTitle legend;
                        TextTitle txtTitle;
                        PiePlot categoryPlot;

                        legend = chart.getLegend();
                        txtTitle = chart.getTitle();
                        categoryPlot = (PiePlot) chart.getPlot();

                        txtTitle.setFont(ftt); // 设置标题字体
                        categoryPlot.setLabelFont(ft);// 设置图片上固定指示文字字体
                        legend.setItemFont(ft);// 设置图例字体

                        ChartFrame chartFrame = new ChartFrame("Waste Pie Chart", chart);
                        chartFrame.pack();

                        chartFrame.setSize(860, 540);
                        // 屏幕居中
                        chartFrame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight
                                / 2 - windowHeight / 2);
                        chartFrame.setLocationRelativeTo(null);
                        chartFrame.setVisible(true);
                    }
                }
            }
        });
    }

    private JLabel getTimeLabel() {
        if (timeLabel == null) {
            timeLabel = new JLabel("");
            timeLabel.setBounds(430,12,200,20);
            timeLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
            timeLabel.setForeground(Color.black);
            timeLabel.setText(new SimpleDateFormat("EEE  hh:mm aa  dd MMM,yyyy",
                    Locale.ENGLISH).format(new Date()));
            time = new Timer(30000, arg0 ->timeLabel.setText(new SimpleDateFormat(
                    "EEE  hh:mm aa  dd MMM,yyyy", Locale.ENGLISH).format(new Date())));
            time.start();
        }
        return timeLabel;
    }

    private String getWholeName(String listSelectedValue){
        char [] rawValue = listSelectedValue.toCharArray();
        String year,month = null,day = null;
        StringBuilder sb = new StringBuilder();
        int subCnt = 0;
        for (int i = 0;i < rawValue.length;i++){
            if (subCnt == 0){
                if (rawValue[i] != ' ')
                    sb.append(rawValue[i]);
                else{
                    subCnt++;
                    switch (sb.toString()){
                        case "Jan": month = "01"; break;
                        case "Feb": month = "02"; break;
                        case "Mar": month = "03"; break;
                        case "Apr": month = "04"; break;
                        case "May": month = "05"; break;
                        case "Jun": month = "06"; break;
                        case "Jul": month = "07"; break;
                        case "Aug": month = "08"; break;
                        case "Sep": month = "09"; break;
                        case "Oct": month = "10"; break;
                        case "Nov": month = "11"; break;
                        case "Dec": month = "12"; break;
                        default:month = "";
                    }
                    sb.delete(0,sb.length());
                }
            } else if (subCnt == 1){
                if (rawValue[i] == ' ')
                    continue;
                if (rawValue[i] !=',')
                    sb.append(rawValue[i]);
                else{
                    subCnt++;
                    day = sb.toString();
                    sb.delete(0,sb.length());
                }
            } else
                sb.append(rawValue[i]);
        }
        year = sb.toString();
        assert day != null;
        if (day.length() == 1)
            day = '0' + day;
        return (year + '_' + month + '_' + day + ".csv").trim();
    }
}