package com.dhq.dimen;

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

//    private static DimenEntity dimenEntity;

    private static String rootUrl = "/res/values";

    private static String targetDp = "320,360,384,400,420,480,560,640,720";

    private static ArrayList<String> widths = new ArrayList<String>();

    public static void main(String[] args) {

//        outputDimenFiles();

    }


    /**
     * 输出dimen文件
     *
     * @param designWidth 设计图宽度
     * @param outPrefix   demins前缀
     * @param outPath     demins文件输出路径
     */
    public static void outputDimenFiles(String designWidth, String outPrefix, String outPath) {
        //获取UI设计图的值
        try {
            designPx = Integer.parseInt(designWidth);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //获取将要转换的宽度值
        widths.add("0");//0 为默认尺寸

        List<String> strings = Arrays.asList(targetDp.split(","));
        widths.addAll(strings);

        for (int i = 0; i < widths.size(); i++) {
            int width_new = Integer.parseInt(widths.get(i));

            String st = convertStreamToString(width_new, outPrefix);
            String newFilePath = getDimenUrl(outPath, width_new);

            writeFile(newFilePath, st);

        }
    }

    /**
     * 生成对应尺寸的dimens文件
     *
     * @param width 输出尺寸
     * @return
     */
    public static String convertStreamToString(float width, String outPrefix) {

        //默认的dimen是320dp的
        if (width < 1) {
            width = defaultPx;
        }

        float scale = width / designPx;//计算缩放的倍数（输出尺寸/设计尺寸）
        StringBuilder sb = new StringBuilder("<resources>" + "\r\n");
        try {

            sb.append("<dimen name=\"" + outPrefix + "_" + 0.5 + "dp\">" + 0.5 * scale + "dp</dimen>"
                    + "\r\n");

            //生成的dp尺寸
            for (int i = 1; i <= designPx; i++) {
                sb.append("<dimen name=\"" + outPrefix + "_" + i + "dp\">" + i * scale + "dp</dimen>"
                        + "\r\n");
            }
//            //生成的sp尺寸
//            for (int j = 1; j <= 50; j++) {
//                sb.append("<dimen name=\"" + dimenEntity.textName + j + "\">" + j * scale + "sp</dimen>"
//                        + "\r\n");
//                System.out.println("<dimen name=\"sp" + designPx + "_" + j + "\">" + j * scale + "sp</dimen>");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append("</resources>" + "\r\n");
        return sb.toString();

    }

    /**
     * 将要输出的内容写入对应文件
     *
     * @param sPath demins 对应的输出文件目录
     * @param st    demins 对应的输出内容
     */

    public static void writeFile(String sPath, String st) {
        try {

            //文件地址
            String filepath = sPath + "/dimens.xml";

            File file = new File(sPath);
            // 判断目录或文件是否存在
            if (!file.exists()) {
                // 如果不存在 创建文件目录
                file.mkdirs();
            } else {
                //文件目录在，删除文件
                deleteFile(filepath);
            }

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
     * @param outPath
     * @param widthDp
     * @return
     */
    private static String getDimenUrl(String outPath, int widthDp) {
        StringBuilder builder = new StringBuilder();
        if (outPath == null && "".equals(outPath)) {
            builder.append("." + rootUrl);
        } else {
            builder.append(outPath + rootUrl);
        }

        if (widthDp > 0) {
            builder.append("-sw" + widthDp + "dp");
        }
        return builder.toString();
    }

}


