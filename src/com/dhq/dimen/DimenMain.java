package com.dhq.dimen;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * 图形界面入口
 * Created by douhaoqiang on 2017/8/22.
 */
public class DimenMain {


    private static JTextField designWidth;//设计图尺寸
    private static JTextArea realWidth;//设计图尺寸
    private static JTextField outPathText;//输出路径显示
    private static JButton outPathBtn;//输出路径选择按钮
    private static JButton jbuName;//输出按钮

    private static Container con = new Container();//

    private static JFileChooser chooser = new JFileChooser();// 文件选择器

    public static void main(String[] args) {

        initGUI();

        setOnclickListener();

    }

    //定义一个qq界面的方法
    public static void initGUI() {
        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        int leftMagin = 10;//左边距
        int topMagin = 10;//上边距
        int centerMagin = 10;//控件垂直间距
        int rowY = topMagin;//每一行的Y坐标

        //实例化一个JFrame类的对象
        JFrame dimensView = new JFrame();
        //设置窗体的标题属性
        dimensView.setTitle("界面适配方案");
        //设置窗体的大小属性
        dimensView.setSize(420, 300);
        //设置窗体的位置属性
        dimensView.setLocation(new Point((int) (lx / 2) - 200, (int) (ly / 2) - 200));// 设定窗口出现位置
        //设置窗体关闭时退出程序
        dimensView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //设置禁止调整窗体的大小
        dimensView.setResizable(false);

        dimensView.setContentPane(con);


        //设计尺寸
        int designHight = 30;//控件高度

        JLabel designName = new JLabel("设计尺寸");
        designName.setBounds(leftMagin, rowY, 60, designHight);

        designWidth = new JTextField();
        designWidth.setBounds(70, rowY, 180, designHight);

        JLabel designUnit = new JLabel("PX");
        designUnit.setBounds(260, rowY, 30, designHight);

        con.add(designName);
        con.add(designWidth);
        con.add(designUnit);


        //输出格式拼接
        int targetHight = 60;//控件高度
        rowY = rowY + centerMagin + designHight;

        JLabel outDesignName = new JLabel("真实尺寸");
        outDesignName.setBounds(leftMagin, rowY, 60, targetHight);

        realWidth = new JTextArea();
        realWidth.setLineWrap(true);
        realWidth.setWrapStyleWord(true);
        realWidth.setBounds(70, rowY, 180, targetHight);
        realWidth.setBorder(new LineBorder(new java.awt.Color(127, 157, 185), 1, false));
        realWidth.setText(DimensTools.targetDp);


        JLabel realUnit = new JLabel("dp");
        realUnit.setBounds(260, rowY, 30, targetHight);

        con.add(outDesignName);
        con.add(realWidth);
        con.add(realUnit);

        //输出路径
        int outPathHight = 30;//控件高度
        rowY = rowY + centerMagin + targetHight;
        JLabel outPathLab = new JLabel("输出路径");
        outPathLab.setBounds(leftMagin, rowY, 60, outPathHight);
        File directory = new File("");
        outPathText = new JTextField(directory.getAbsolutePath());// 文件的路径
        outPathText.setBounds(70, rowY, 200, outPathHight);
        outPathBtn = new JButton("...");// 选择
        outPathBtn.setBounds(280, rowY, 30, outPathHight);
        con.add(outPathLab);
        con.add(outPathText);
        con.add(outPathBtn);

        int outButtonHight = 30;//控件高度
        rowY = rowY + centerMagin + targetHight;
        jbuName = new JButton("输     出");
        jbuName.setBounds(110, rowY + 20, 200, outButtonHight);
        //将jButton对象添加到容器JFrame对象上
        con.add(jbuName);

        dimensView.setVisible(true);


    }


    private static void setOnclickListener() {
        jbuName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DimensTools.outputDimenFiles(designWidth.getText(), designWidth.getText(), outPathText.getText());
                JPanel jPanel = new JPanel();
                JOptionPane.showMessageDialog(jPanel, "生成适配文件成功", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });
        outPathBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 设定只能选择到文件
                int state = chooser.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
                if (state == 1) {
                    return;// 撤销则返回
                } else {
                    File f = chooser.getSelectedFile();// f为选择到的文件
                    outPathText.setText(f.getAbsolutePath());
                }
            }
        });


    }


}