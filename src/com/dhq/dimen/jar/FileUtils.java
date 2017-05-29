// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FileUtils.java

package com.dhq.dimen.jar;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class FileUtils {

    public FileUtils() {
    }

    public static String readTxtFile2Buf(String path) {
        String data = "";
        InputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (in != null) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            String line = "";
            try {
                while ((line = rd.readLine()) != null)
                    data = (new StringBuilder(String.valueOf(data))).append(line).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        } else {
            return null;
        }
    }

    public static void writeBuf2File(String path, byte buffer[]) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (os != null)
            try {
                os.write(buffer);
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static boolean saveObject(String dstPath, Object obj) {
        boolean saveOk = false;
        if (dstPath == null || obj == null)
            return false;
        File file = new File(dstPath);
        if (file.exists()) {
            file.delete();
            file = new File(dstPath);
        } else {
            int idx = dstPath.lastIndexOf("/");
            if (idx < 0)
                idx = dstPath.lastIndexOf("\\");
            String dir = null;
            if (idx > 0)
                dir = dstPath.substring(0, idx);
            if (dir != null) {
                file = new File(dir);
                file.mkdirs();
            }
        }
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bytearrayoutputstream);
            oos.writeObject(obj);
            oos.close();
            saveOk = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (saveOk)
            writeBuf2File(dstPath, bytearrayoutputstream.toByteArray());
        return saveOk;
    }

    public static Object readObject(String dstPath) {
        if (dstPath == null)
            return null;
        File file = new File(dstPath);
        if (!file.exists() || file.length() == 0L)
            return null;
        ObjectInputStream ois = null;
        Object objois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            try {
                objois = ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objois;
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream is = new FileInputStream(oldPath);
                FileOutputStream os = new FileOutputStream(newPath);
                byte buffer[] = new byte[1444];
                while ((byteread = is.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    os.write(buffer, 0, byteread);
                }
                is.close();
            }
        } catch (Exception e) {
            System.out.println("���Ƶ����ļ���������");
            e.printStackTrace();
        }
    }

    public static void copyFolderFile(String filePath, String dstPath)
            throws IOException {
        if (filePath != null && filePath.length() > 0) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                if (!(new File(dstPath)).exists())
                    (new File(dstPath)).mkdir();
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++)
                    copyFolderFile(files[i].getAbsolutePath(), (new StringBuilder(String.valueOf(dstPath))).append(File.separator).append(files[i].getName()).toString());

            } else {
                copyFile(filePath, dstPath);
            }
        }
    }

    public static byte[] readFile2Buf(String path) {
        RandomAccessFile randomFile = null;
        byte bytes[] = null;
        int byteread = 0;
        try {
            randomFile = new RandomAccessFile(path, "r");
            long fileLength = randomFile.length();
            bytes = new byte[(int) fileLength];
            byteread = randomFile.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null)
                try {
                    randomFile.close();
                } catch (IOException ioexception) {
                }
            return bytes;
        }
    }

    public static void deleteFolderFile(String filePath, boolean deleteThisPath)
            throws IOException {
        if (filePath != null && filePath.length() > 0) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++)
                    deleteFolderFile(files[i].getAbsolutePath(), true);

            }
            if (deleteThisPath)
                if (!file.isDirectory())
                    file.delete();
                else if (file.listFiles().length == 0)
                    file.delete();
        }
    }

    public static void downloadFile(String urlString, String filename)
            throws Exception {
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        byte bs[] = new byte[1024];
        OutputStream os = new FileOutputStream(filename);
        int len;
        while ((len = is.read(bs)) != -1)
            os.write(bs, 0, len);
        os.close();
        is.close();
    }
}
