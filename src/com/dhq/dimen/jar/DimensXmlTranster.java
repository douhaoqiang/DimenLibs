package com.dhq.dimen.jar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DimensXmlTranster {

    private static int designScreenWidthPx = 1080;//表示UI设计图的宽度尺寸
    private static final String defaultDimensType[] = {
            "", "360"
    };
    private static String dimensType[] = null;
    DocumentBuilderFactory builderFactory;

    public DimensXmlTranster() {
        builderFactory = DocumentBuilderFactory.newInstance();
    }

    public Document parse(String filePath) {
        Document document = null;
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            document = builder.parse(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    public void transform(Map dimesMap) {
        for (int i = 0; i < dimensType.length; i++)
            genXml(dimesMap, dimensType[i]);

    }

    private void genXml(Map dimesMap, String dimenType) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            document.setXmlVersion("1.0");
            Element root = document.createElement("resources");
            document.appendChild(root);
            genNode(dimesMap, document, root, dimenType);
            File dir;
            if (dimenType == null || dimenType.length() == 0)
                dir = new File("res/values");
            else
                dir = new File((new StringBuilder("res/values-sw")).append(dimenType).append("dp").toString());
            dir.mkdirs();
            File file = new File(dir, "dimens.xml");
            writeDocument(document, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeDocument(Document document, File file) {
        System.out.println((new StringBuilder("writeDocument ")).append(file.getAbsolutePath()).toString());
        try {
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transFormer = transFactory.newTransformer();
            transFormer.setOutputProperty("indent", "yes");
            DOMSource domSource = new DOMSource(document);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult xmlResult = new StreamResult(baos);
            transFormer.transform(domSource, xmlResult);
            String domStr = baos.toString("UTF-8");
            domStr = format(domStr);
            out.write(domStr.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(file.getAbsolutePath());
    }

    public static String format(String str) {
        StringBuilder sb = new StringBuilder();
        String lines[] = str.split("\r\n");
        sb.append(lines[0]);
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() == 0) {
                sb.append("\r\n\r\n");
            } else {
                String parts[] = lines[i].split(" ");
                String indentStr = "";
                int indent = 0;
                for (int j = 0; j < parts.length; j++) {
                    if (parts[j].length() > 0)
                        break;
                    indent++;
                    indentStr = (new StringBuilder(String.valueOf(indentStr))).append(" ").toString();
                }

                if (lines[i - 1].length() > 0)
                    sb.append("\r\n");
                if (indent > 0)
                    sb.append(indentStr);
                indentStr = (new StringBuilder("    ")).append(indentStr).toString();
                int prevEq = 1000;
                for (int j = indent; j < parts.length; j++) {
                    if (prevEq == j - 1)
                        sb.append((new StringBuilder("\r\n")).append(indentStr).toString());
                    else if (j > indent)
                        sb.append(" ");
                    sb.append(parts[j]);
                    if (parts[j].contains("="))
                        prevEq = j;
                }

            }
        }
        return sb.toString();
    }

    private void genNode(Map dimesMap, Document document, Element root, String dimenType) {
        int factors = getFactor(dimenType);
        if (factors > designScreenWidthPx)
            System.out.println((new StringBuilder("Warn: designScreenWidthPx overflow : ")).append(factors).append(">").append(designScreenWidthPx).toString());
        Set set = dimesMap.entrySet();
        Element pageElement;
        for (Iterator iterator = set.iterator(); iterator.hasNext(); root.appendChild(pageElement)) {
            Map.Entry entry = (Map.Entry) iterator.next();
            pageElement = document.createElement("com/dhq/dimen");
            pageElement.setAttribute("name", (String) entry.getKey());
            pageElement.setTextContent((new StringBuilder()).append(transValue((String) entry.getValue(), factors, designScreenWidthPx)).toString());
        }

    }

    private int getFactor(String dimenType) {
        if (dimenType == null || dimenType.length() == 0)
            return 320;
        int ret = 320;
        try {
            ret = Integer.parseInt(dimenType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private String transValue(String val, int numerator, int denominator) {
        float f = 0.0F;
        String unit = "dp";
        String pattern = "([+-]?(\\d+)(\\.\\d+)?).*";
        Matcher m = Pattern.compile(pattern).matcher(val);
        if (m.find()) {
            String str = m.group(1);
            f = Float.parseFloat(str);
            f = (f * (float) numerator) / (float) denominator;
            if (str.length() < val.length())
                unit = val.substring(str.length());
            else
                unit = "dp";
            if (!unit.equalsIgnoreCase("dp") && !unit.equalsIgnoreCase("sp"))
                System.out.println((new StringBuilder("Warn: transValue ")).append(val).append("unit incorrect!").toString());
        } else {
            System.out.println((new StringBuilder("Warn: transValue ")).append(val).append("com.dhq.dimen not found!").toString());
        }
        return (new StringBuilder(String.valueOf(trimZero((new StringBuilder()).append(f).toString())))).append(unit).toString();
    }

    private String trimZero(String fStr) {
        String zeroStr = ".0000000000";
        int idx = fStr.indexOf(".");
        if (idx > 0) {
            String zStr = fStr.substring(idx);
            if (zeroStr.startsWith(zStr))
                return fStr.substring(0, idx);
        }
        return fStr;
    }

    private static boolean checkDimenKey(HashMap dimesMap, List dimenLst) {
        ArrayList delLst = new ArrayList();
        Set set = dimesMap.keySet();
        for (Iterator iter = set.iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            if (!dimenLst.contains(key))
                delLst.add(key);
        }

        if (delLst.size() > 0) {
            String key;
            for (Iterator iterator = delLst.iterator(); iterator.hasNext(); dimesMap.remove(key)) {
                key = (String) iterator.next();
                System.out.println((new StringBuilder("checkDimenKey: ")).append(key).append(" not used!").toString());
            }

            return true;
        } else {
            return false;
        }
    }


    public static void main(String args[]) {
        System.out.println("Usage: dimensXmlTranster [720|layout720] [check] 360 480 ...");
        Document document = null;
        boolean bCheck = false;
        HashMap layoutDpMap = null;
        List dimenLst = null;
        if (args != null && args.length > 0) {
            if (args[0].startsWith("layout")) {
                String dimStr = args[0].substring("layout".length());
                if (dimStr.length() > 0) {
                    dimStr = dimStr.trim();
                    try {
                        designScreenWidthPx = Integer.parseInt(dimStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    designScreenWidthPx = 320;
                }
            } else {
                try {
                    designScreenWidthPx = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }

            if (args.length > 1) {
                int idx = 1;
                if (args[1].equals("check")) {
                    dimenLst = new ArrayList();
                    idx++;
                }
                if (idx < args.length) {
                    dimensType = new String[args.length - 1];
                    int cnt = 0;
                    dimensType[cnt++] = "";
                    for (; idx < args.length; idx++) {
                        dimensType[cnt++] = args[idx];
                    }
                }
            }

            if (dimensType == null || dimensType.length == 0) {
                dimensType = defaultDimensType;
            }
        }

        DimensXmlTranster transter = new DimensXmlTranster();

        if (designScreenWidthPx == 320) {
            document = transter.parse("res/values/dimens.xml");
        } else {
            document = transter.parse((new StringBuilder("res/values-sw")).append(designScreenWidthPx).append("dp/dimens.xml").toString());
        }
        Element rootElement = document.getDocumentElement();
        NodeList nodes = rootElement.getChildNodes();
        LinkedHashMap dimesMap = new LinkedHashMap();
        NodeList nodeList = rootElement.getElementsByTagName("com/dhq/dimen");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String id = element.getAttribute("name");
                String val = element.getTextContent();
                dimesMap.put(id, val);
            }

        }

        if (dimenLst != null) {
            checkDimenKey(dimesMap, dimenLst);
        }
        transter.transform(dimesMap);
    }


}
