// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LayoutPicker.java

package com.dhq.dimen.jar;

import org.w3c.dom.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Referenced classes of package com.winsland.androidtool:
//			FileUtils

public class LayoutPicker {

    HashMap dimenMap;
    int screenWidthDp;
    private boolean foundPickDimens;

    public LayoutPicker(int dp, HashMap map) {
        dimenMap = null;
        screenWidthDp = 320;
        if (map == null) {
            dimenMap = new HashMap();
        } else {
            dimenMap = map;
        }
        if (dp > 0) {
            screenWidthDp = dp;
        }
    }

    public HashMap pickDimens(Document document) {
        Element rootElement = document.getDocumentElement();
        dispatchNode(rootElement);
        return dimenMap;
    }

    private void dispatchNode(Node node) {
        pickDimen(node);
        if (node.hasChildNodes()) {
            for (Node subNode = node.getFirstChild(); subNode != null; subNode = subNode.getNextSibling())
                if (subNode.hasChildNodes())
                    dispatchNode(subNode);
                else
                    pickDimen(subNode);

        }
    }

    private void changeDimenAttribute(Attr attr, float dimen, String unit) {
        attr.setValue(getDimenStr(dimen, unit));
    }

    private String getDimenStr(float dimen, String unit) {
        return (new StringBuilder("@com.dhq.dimen/")).append(genKey(dimen, unit)).toString();
    }

    private String pickDimenKey(String val) {
        if (val.startsWith("@com.dhq.dimen/"))
            return val.substring("@com.dhq.dimen/".length()).trim();
        else
            return null;
    }

    private void pickDimen(Node node) {
        NamedNodeMap map = node.getAttributes();
        if (map == null)
            return;
        for (int i = 0; i < map.getLength(); i++) {
            Attr attr = (Attr) map.item(i);
            float f = pickDimen(attr.getNodeValue());
            if (f != 0.0F) {
                String unit = getUnit(attr);
                changeDimenAttribute(attr, f, unit);
                String key = genKey(f, unit);
                if (dimenMap.get(key) == null)
                    dimenMap.put(key, genVal(f, unit));
            }
        }

    }

    private float pickDimen(String val) {
//		float f;
//		String unit;
//		String str;
//		f = 0.0F;
//		unit = null;
//		String pattern = "([+-]?(\\d+)(\\.\\d+)?).*";
//		Matcher m = Pattern.compile(pattern).matcher(val);
//		if (!m.find())
//			break MISSING_BLOCK_LABEL_104;
//		str = m.group(1);
//		if (str.length() + 2 != val.length())
//			break MISSING_BLOCK_LABEL_104;
//		f = Float.parseFloat(str);
//		if (str.length() >= val.length())
//			break MISSING_BLOCK_LABEL_104;
//		unit = val.substring(str.length());
//		if (unit.equalsIgnoreCase("dp") || unit.equalsIgnoreCase("sp"))
//			return f;
//		break MISSING_BLOCK_LABEL_104;
//		Exception e;
//		e;
//		e.printStackTrace();
        return 0.0F;
    }

    private String getUnit(Node node) {
        String unit = node.getNodeValue();
        return unit.substring(unit.length() - 2);
    }

    private String genKey(float dp, String unit) {
        String key = (new StringBuilder(String.valueOf(unit))).append(screenWidthDp).append("_").append(dp >= 0.0F ? ((Object) (Float.valueOf(dp))) : ((Object) ((new StringBuilder("_")).append(-dp).toString()))).toString();
        if (key.endsWith(".0"))
            return key.substring(0, key.length() - 2);
        else
            return key.replace(".", "_");
    }

    private String genVal(float dp, String unit) {
        int idp = (int) dp;
        if (Math.abs(dp - (float) idp) != 0.0F)
            return (new StringBuilder()).append(dp).append(unit).toString();
        else
            return (new StringBuilder()).append(idp).append(unit).toString();
    }

    public HashMap pickDimens(File file, List dimenLst) {
        byte bytes[] = FileUtils.readFile2Buf(file.getAbsolutePath());
        try {
            foundPickDimens = false;
            String docStr = pickDimens(new String(bytes, "UTF-8"), dimenLst);
            System.out.println((new StringBuilder("pickDimens ")).append(file.getName()).append(foundPickDimens ? " foundPickDimens!" : " pass!").toString());
            if (docStr != null && foundPickDimens) {
                System.out.println((new StringBuilder("writeLayoutFile: ")).append(file.getAbsolutePath()).append(" len=").append(docStr.length()).toString());
                FileUtils.writeBuf2File(file.getAbsolutePath(), docStr.getBytes("UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println((new StringBuilder("pickDimens ")).append(file.getName()).append(" UnsupportedEncodingException").toString());
            e.printStackTrace();
        }
        return dimenMap;
    }

    private String pickDimens(String docStr, List dimenLst) {
        StringBuilder sb = new StringBuilder();
        String lines[] = docStr.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() > 0)
                lines[i] = lines[i].replace("\r", "");
            if (lines[i].length() > 0) {
                String pattern = "\\s.+?\\s*=\\s*\\\"\\s*(.+?)\\s*\\\"";
                StringBuilder lineSb = new StringBuilder();
                Matcher m = Pattern.compile(pattern).matcher(lines[i]);
                boolean found = false;
                String beginStr = null;
                String endStr = null;
                int lastEnd = 0;
                while (m.find()) {
                    found = true;
                    beginStr = lines[i].substring(lastEnd, m.start(1));
                    lastEnd = m.end() - 1;
                    endStr = lines[i].substring(lastEnd);
                    String val = m.group(1);
                    if (dimenLst != null) {
                        String key = pickDimenKey(val);
                        if (key != null && key.length() > 0 && !dimenLst.contains(key))
                            dimenLst.add(key);
                    }
                    float f = pickDimen(val);
                    if (f != 0.0F) {
                        foundPickDimens = true;
                        String unit = val.substring(val.length() - 2);
                        String dimKey = genKey(f, unit);
                        if (dimenMap.get(dimKey) == null)
                            dimenMap.put(dimKey, genVal(f, unit));
                        System.out.println((new StringBuilder("pickDimens lines[")).append(i).append("]: ").append(val).toString());
                        lineSb.append(beginStr);
                        lineSb.append(getDimenStr(f, unit));
                        System.out.println((new StringBuilder("pickDimens lines[")).append(i).append("]: ").append(val).toString());
                    } else {
                        lineSb.append(beginStr);
                        lineSb.append(val);
                    }
                }
                if (found)
                    lineSb.append(endStr);
                else
                    lineSb.append(lines[i]);
                sb.append(lineSb.toString());
            }
            sb.append("\r\n");
        }

        return sb.toString();
    }
}
