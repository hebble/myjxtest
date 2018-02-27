//package com.my.mytest;
//
//import java.awt.Image;  
//import java.awt.color.ColorSpace;  
//import java.awt.image.BufferedImage;  
//import java.awt.image.ColorConvertOp;  
//import java.io.File;  
//import java.io.FileInputStream;  
//import java.io.FileNotFoundException;  
//import java.io.FileOutputStream;  
//import java.io.IOException;  
//import java.io.OutputStream;  
//import java.util.Arrays;  
//  
//import javax.imageio.ImageIO;  
//import javax.imageio.ImageReadParam;  
//import javax.imageio.ImageReader;  
//import javax.imageio.stream.FileImageInputStream;  
//import javax.imageio.stream.ImageInputStream;  
//  
//import org.opencv.core.Mat;  
//import org.opencv.core.MatOfRect;  
//import org.opencv.core.Rect;  
//import org.opencv.core.Size;  
//import org.opencv.imgcodecs.Imgcodecs;  
//import org.opencv.objdetect.CascadeClassifier;  
//
//public class OpencvTest {
//
//	 private static void test(String fileName) {  
//	        int index = fileName.lastIndexOf("\\");  
//	        String suffix = index != -1 ? fileName.substring(index + 1) : ".png";  
//	        int[] rectPosition = detectFace(fileName);  
//	        System.out.println("x=" + rectPosition[0] + " y=" + rectPosition[1] + " width=" + rectPosition[2] + " height="  
//	                + rectPosition[3]);  
//	  
//	        int x = rectPosition[0];  
//	        int y = rectPosition[1];  
//	        int w = rectPosition[2];  
//	        int h = rectPosition[3];  
//	        int[] imgRect = new int[2];  
//	        try {  
//	            imgRect = getImageWidth(fileName);  
//	        } catch (FileNotFoundException e) {  
//	            e.printStackTrace();  
//	        } catch (IOException e) {  
//	            e.printStackTrace();  
//	        }  
//	        /* 
//	         * if(x == 0 || y == 0 || w == 0 || h == 0 ){ 
//	         * System.out.println("人脸识别失败:" + fileName + " 把身份证变成黑白照片再次进行识别"); 
//	         * String destFile = "d:/data/temp" + suffix; 
//	         * //convertBackWhiteImage(fileName, destFile); changeImge(new 
//	         * File(fileName)); rectPosition = detectFace(fileName); // rectPosition 
//	         * = detectFace(destFile); System.out.println("x=" + rectPosition[0] + 
//	         * " y=" + rectPosition[1] + " width=" + rectPosition[2] + " height=" + 
//	         * rectPosition[3]); 
//	         *  
//	         * x = rectPosition[0]; y = rectPosition[1]; w = rectPosition[2]; h = 
//	         * rectPosition[3]; } 
//	         */  
//	        if (x == 0 || y == 0 || w == 0 || h == 0) {  
//	  
//	            x = imgRect[0];  
//	            y = imgRect[1];  
//	            w = (int) (x * avatarPer);  
//	            h = w;  
//	            System.out.println("人脸识别失败:" + fileName + " 采用默认识别");  
//	  
//	        }  
//	        /* 
//	         * int cutX = x/4 + 20; int cutY = (y + h ) *2; 
//	         */  
//	        int cutX = x / 2 - 20;  
//	  
//	        String destFile = "d:/data/idcard" + suffix;  
//	  
//	        int width = imgRect[0] - cutX - 30;  
//	        // ImageUtils.cut(fileName, destFile, cutX, cutY, width/2, 120);  
//	        // ImageUtils.cut(fileName, "d:/data/avatar.jpg", x, y, w, h);  
//	        int cutHeight = 140;  
//	        int imgHieght = imgRect[1];  
//	        int topSpace = (int) (imgHieght * avatarPer);  
//	        int height = (int) (imgHieght * avatarSpacePer);  
//	        int cutY = y + h + height;  
//	        if (imgHieght - cutY < cutHeight - 5) {  
//	            cutY = imgHieght - (int) (imgHieght / 3.8) - 10;  
//	        }  
//	        if (imgHieght < 500) {  
//	            cutY = cutY + 20;  
//	        }  
//	        new Main().cutImage(fileName, destFile, cutX, cutY, width, cutHeight);  
//	        Test.test(destFile);  
//	    } 
//}
