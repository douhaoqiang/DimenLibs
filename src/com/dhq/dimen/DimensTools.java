package com.dhq.dimen;

import com.dhq.dimen.entity.DimenEntity;
import com.dhq.dimen.jar.PropertyUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator on 2015/9/9.
 */
public class DimensTools {

    private static int designPx = 720;//UI设计的宽度尺寸
    private static int defaultPx = 320;

    private static DimenEntity dimenEntity;

    private static String rootUrl = "./res/values";

    private static ArrayList<String> widths = new ArrayList<String>();

    public static void main(String[] args) {

        dimenEntity = PropertyUtils.readProperties();


        //获取UI设计图的值
        try {
            designPx = Integer.parseInt(dimenEntity.designPx);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
//
//        //获取将要转换的宽度值
        widths.add("0");//0 为默认尺寸
//        for (int i = 1; i < args.length; i++) {
//            widths.add(args[i]);
//        }

        List<String> strings = Arrays.asList(dimenEntity.targetDp.split(","));
        widths.addAll(strings);

        for (int i = 0; i < widths.size(); i++) {
            int width_new = Integer.parseInt(widths.get(i));

            String st = convertStreamToString(width_new);
            String newFilePath = getDimenUrl(width_new);

            DeleteFolder(newFilePath, st);

        }

    }

    /**
     * 生成变化后的dimens文件
     */
    public static String convertStreamToString(float width) {

        //默认的dimen是320dp的
        if (width < 1) {
            width = defaultPx;
        }

        float scale = width / designPx;//计算缩放的倍数
        StringBuilder sb = new StringBuilder("<resources>" + "\r\n");
        try {

            sb.append("<dimen  name=\"" + dimenEntity.startName + 0.5 + "\">" + 0.5 * scale + "dp</dimen>"
                    + "\r\n");

            //生成的dp文件
            for (int i = 1; i <= designPx; i++) {
                sb.append("<dimen  name=\"" + dimenEntity.startName + i + "\">" + i * scale + "dp</dimen>"
                        + "\r\n");
                System.out.println("<dimen  name=\"dp" + designPx + "_" + i + "\">" + i * scale + "dp</dimen>");
            }
            //生成的sp文件
            for (int j = 1; j <= 50; j++) {
                sb.append("<dimen  name=\"" + dimenEntity.textName + j + "\">" + j * scale + "sp</dimen>"
                        + "\r\n");
                System.out.println("<dimen  name=\"sp" + designPx + "_" + j + "\">" + j * scale + "sp</dimen>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append("</resources>" + "\r\n");
        return sb.toString();

    }


    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */

    public static void DeleteFolder(String sPath, String content) {

        //文件地址
        String feilpath = sPath + "/dimens.xml";

        File file = new File(sPath);
        // // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在
            file.mkdirs();//创建文件目录
        } else {//文件目录在，删除文件
            deleteFile(feilpath);
        }


        writeFile(feilpath, content);


    }

    /**
     * 存为新文件
     */

    public static void writeFile(String filepath, String st) {
        try {
//        	File file = new File(filepath);
            FileWriter fw = new FileWriter(filepath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(st);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */

    public static boolean deleteFile(String sPath) {

        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }

        return flag;

    }

    /**
     * 获取要写入内容的文件目录
     *
     * @param widthDp
     * @return
     */
    private static String getDimenUrl(int widthDp) {
        StringBuilder builder = new StringBuilder(rootUrl);
        if (widthDp > 0) {
            builder.append("-sw" + widthDp + "dp");
        }
        return builder.toString();
    }

}


