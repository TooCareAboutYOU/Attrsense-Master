//package com.attrsense.android.util;
//
//import android.content.Context;
//import android.util.Log;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
///**
// * author : zhangshuai@attrsense.com
// * date : 2022/10/20 9:26
// * mark : custom something
// *                 val data_path=FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/bias1.bin")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/bias1_i.bin")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/bias2.bin")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/bias2_i.bin")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/bias3.bin")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/bias3_i.bin")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/cvt_float_int.py")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/f_table.bin")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/M0_1.txt")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/M0_2.txt")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/M0_3.txt")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/weight1.bin")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/weight2.bin")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/weight3.bin")
// *                 FileUtils.getDataFilePath(requireContext().applicationContext, "engine/data/z_table.bin")
// *
// *                 val decode_model_path= FileUtils.getModelFilePath(requireContext().applicationContext, "engine/decode.dlc")
// *                 val encode_model_path= FileUtils.getModelFilePath(requireContext().applicationContext, "engine/encode.dlc")
// *                 val decode_out_path=requireContext().externalCacheDir?.absolutePath+File.separator+"anf"
// *                 val encode_out_path=requireContext().externalCacheDir?.absolutePath+File.separator+"image"
// *                 val so_path = requireContext().applicationContext.applicationInfo.nativeLibraryDir
// *
// *                 this.initDecoder(decode_model_path, data_path, decode_out_path)
// *                 this.initEncoder(encode_model_path, data_path, encode_out_path)
// *                 setAdspLibraryPath(so_path)
// *
// *
// */
//public class FileUtils {
//
//    /**
//     * 例如：/data/user/0/com.attrsense.android/files/attrsense/engine
//     */
//    public static String getDataFilePath(Context context, String modelName) {
//        copyFileIfNeed(context, modelName);
//        return getDataPath(context)+ File.separator + "engine" + File.separator +"data"+ File.separator;
//    }
//
//    private static String getDataPath(Context context) {
//        return context.getFilesDir() + File.separator + "attrsense";
//    }
//
//    /**
//     * 拷贝asset下的文件到context.getFilesDir()目录下
//     */
//    private static void copyFileIfNeed(Context context, String modelName) {
//        InputStream is = null;
//        OutputStream os = null;
//        try {
//            // 默认存储在data/data/<application name>/file目录下
//            File modelFile = new File(getDataPath(context), modelName);
//            is = context.getAssets().open(modelName);
//            if (modelFile.length() == is.available()) {
//                return;
//            }
//            if (!modelFile.getParentFile().exists()) {
//                modelFile.getParentFile().mkdirs();
//            }
//            if (!modelFile.exists()) {
//                boolean res = modelFile.createNewFile();
//                if (!res) {
//                    return;
//                }
//            } else {
//                if (modelFile.length() > 10) {//表示已经写入一次
//                    return;
//                }
//            }
//            os = new FileOutputStream(modelFile);
//            byte[] buffer = new byte[1024];
//            int length = is.read(buffer);
//            while (length > 0) {
//                os.write(buffer, 0, length);
//                length = is.read(buffer);
//            }
//            os.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (os != null) {
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    /**
//     * 拷贝文件到目录下：
//     * 例如：/data/user/0/com.attrsense.android/files/attrsense/engine/decode.dlc
//     */
//    public static String getModelFilePath(Context context, String fileName) {
//        try {
//            File cacheDir = context.getFilesDir();
//            if (!cacheDir.exists()) {
//                cacheDir.mkdirs();
//            }
//            File outFile = new File(cacheDir + File.separator + "attrsense", fileName);
//
//            if (!outFile.getParentFile().exists()) {
//                outFile.getParentFile().mkdirs();
//            }
//
//            if (!outFile.exists()) {
//                boolean res = outFile.createNewFile();
//                if (!res) {
//                    return null;
//                }
//            } else {
//                if (outFile.length() > 10) {//表示已经写入一次
//                    return outFile.getPath();
//                }
//            }
//            InputStream is = context.getAssets().open(fileName);
//            FileOutputStream fos = new FileOutputStream(outFile);
//            byte[] buffer = new byte[1024];
//            int byteCount;
//            while ((byteCount = is.read(buffer)) != -1) {
//                fos.write(buffer, 0, byteCount);
//            }
//            fos.flush();
//            is.close();
//            fos.close();
//            return outFile.getPath();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
