package com.dhq.dimen;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private static JTextField outPathText;//输出路径显示
    private static JButton outPathBtn;//输出路径选择按钮
    private static JButton jbuName;//输出按钮
    private static JTextField outPrefix;//输出格式前缀
    private static JLabel outText;//输出格式展示

    Container con = new Container();//

    private static JFileChooser chooser = new JFileChooser();// 文件选择器

    public static void main(String[] args) {

//        new DimenMain();

        // 实例化showFrameqq类的对象
        DimenMain showqq = new DimenMain();
        //调用qq界面的方法
        showqq.initGUI();

        setOnclickListener();

    }

    //定义一个qq界面的方法
    public void initGUI() {
        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        int leftMagin=10;//左边距
        int topMagin=10;//上边距
        int centerMagin=10;//控件垂直间距
        int hight=30;//控件高度
        int rowIndex=0;//行坐标
        int rowY=0;//每一行的Y坐标

        //实例化一个JFrame类的对象
        JFrame jf = new JFrame();
        //设置窗体的标题属性
        jf.setTitle("界面适配方案");
        //设置窗体的大小属性
        jf.setSize(420, 250);
        //设置窗体的位置属性
        jf.setLocation(new Point((int) (lx / 2) - 200, (int) (ly / 2)-200));// 设定窗口出现位置
//        jf.setLocation(0, 0);
        //设置窗体关闭时退出程序
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //设置禁止调整窗体的大小
        jf.setResizable(false);

        //实例化一个布局类的对象
//        FlowLayout con = new FlowLayout(FlowLayout.LEFT);
        //设置窗体的布局方式为流式布局
//        jf.setLayout(con);
        jf.setContentPane(con);
        //设计尺寸

        rowY=topMagin+rowIndex*(hight+centerMagin);

        JLabel designName = new JLabel("设计尺寸");
        designName.setBounds(leftMagin,rowY,60,hight);

        designWidth = new JTextField();
        designWidth.setBounds(70,rowY,180,hight);

        JLabel designUnit = new JLabel("PX");
        designUnit.setBounds(260,rowY,30,hight);

        con.add(designName);
        con.add(designWidth);
        con.add(designUnit);

        //输出格式拼接
        rowIndex++;
        rowY=topMagin+rowIndex*(hight+centerMagin);
        JLabel outFormat = new JLabel("输出格式");
        outFormat.setBounds(leftMagin,rowY,60,hight);
        outPrefix = new JTextField();//前缀
        outPrefix.setBounds(70,rowY,180,hight);
        JLabel outUnit = new JLabel("dp");
        outUnit.setBounds(260,rowY,30,hight);
        outText = new JLabel("例 width_56dp");
        outText.setBounds(300,rowY,100,hight);
        con.add(outFormat);
        con.add(outPrefix);
        con.add(outUnit);
        con.add(outText);

        //输出路径
        rowIndex++;
        rowY=topMagin+rowIndex*(hight+centerMagin);
        JLabel outPathLab = new JLabel("输出路径");
        outPathLab.setBounds(leftMagin,rowY,60,hight);
        File directory = new File("");
        outPathText = new JTextField(directory.getAbsolutePath());// 文件的路径
        outPathText.setBounds(70,rowY,200,hight);
        outPathBtn = new JButton("...");// 选择
        outPathBtn.setBounds(280,rowY,30,hight);
        con.add(outPathLab);
        con.add(outPathText);
        con.add(outPathBtn);

//        //实例化一个JCheckBox对象
//        JCheckBox jchName = new JCheckBox("记住密码");
//        //将jchName3对象添加到容器JFrame对象上
//        con.add(jchName);
//        //实例化一个JCheckBox对象
//        JCheckBox jchName2 = new JCheckBox("自动登录");
//        //将jchName3对象添加到容器JFrame对象上
//        con.add(jchName2);


        rowIndex++;
        rowY=topMagin+rowIndex*(hight+centerMagin);
        jbuName = new JButton("输     出");
        jbuName.setBounds(110,rowY+20,200,hight);
        //将jButton对象添加到容器JFrame对象上
        con.add(jbuName);

        jf.setVisible(true);




    }


    private static void setOnclickListener(){
        jbuName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DimensTools.outputDimenFiles(designWidth.getText(),outPrefix.getText(),outPathText.getText());
                JPanel jPanel=new JPanel();
                JOptionPane.showMessageDialog(jPanel, "生成适配文件成功", "提示",JOptionPane.WARNING_MESSAGE);
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

        outPrefix.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                outText.setText("例 " + outPrefix.getText() + "_50dp");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                outText.setText("例 " + outPrefix.getText() + "_50dp");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

    }


}