package com.my.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.my.enums.FileType;

public class FileUtil{

	public static void main(String[] args) throws IOException{
		System.out.println(getType("d:\\400_400_p3_g0_-1_-1_r0_q75.jpeg"));
	}

	public static String getFileName(String path){
		if(StringUtils.contains(path, "/")){
			path = path.substring(path.lastIndexOf("/") + 1);
		}
		if(StringUtils.contains(path, "\\")){
			path = path.substring(path.lastIndexOf("\\") + 1);
		}
		return path;
	}

	/**
	 * 取文件后缀
	 */
	public static String getExtension(File file){
		return file != null ? getExtension(file.getName()) : "";
	}

	/**
	 * 取文件后缀
	 */
	public static String getExtension(String filename){
		if(StringUtils.contains(filename, ".")){
			int length = filename.length();
			int i = filename.lastIndexOf('.');
			if(i > 0 && i < length - 1){
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return "";
	}

	/**
	 * 创建文件路径的文件夹
	 */
	public final static void ensureFolder(String path){
		if(StringUtils.contains(path, ".")){
			path = UrlUtil.fixUrl(path);
			path = path.replaceAll("//", "/");
			if(StringUtils.contains(path, "/")){
				path = path.substring(0, path.lastIndexOf("/"));
				createFolder(path);
			}
		}
	}

	/**
	 * 复制单个文件
	 * @param file 准备复制的文件源
	 * @param newPathFile 拷贝到新绝对路径带文件名
	 */
	public static void copyFile(File file, String newPathFile){
		try{
			if(isFile(file) && StringUtils.isNotEmpty(newPathFile)){
				copyFile(new FileInputStream(file), newPathFile);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 复制单个文件
	 * @param file 准备复制的文件源
	 * @param newPathFile 拷贝到新绝对路径带文件名
	 */
	public static void copyFile(InputStream inStream, String newPathFile){
		try{
			if(inStream != null && StringUtils.isNotEmpty(newPathFile)){
				ensureFolder(newPathFile);
				int byteread = 0;
				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1024 * 10];
				while((byteread = inStream.read(buffer)) != -1){
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 复制单个文件
	 * @param oldPathFile 准备复制的文件源
	 * @param newPathFile 拷贝到新绝对路径带文件名
	 */
	public static void copyFile(String oldPathFile, String newPathFile){
		if(StringUtils.isNotEmpty(oldPathFile)){
			copyFile(new File(oldPathFile), newPathFile);
		}
	}

	/**
	 * 复制整个文件夹的内容
	 * @param oldPath 准备拷贝的目录
	 * @param newPath 指定绝对路径的新目录
	 */
	public static void copyFolder(String oldPath, String newPath){
		try{
			if(StringUtils.isNotEmpty(oldPath) && StringUtils.isNotEmpty(newPath)){
				File a = new File(oldPath);
				if(a == null || !a.isDirectory()){
					return;
				}
				createFolder(newPath);
				String[] file = a.list();
				File temp = null;
				for(int i = 0; i < file.length; i++){
					if(oldPath.endsWith(File.separator)){
						temp = new File(oldPath + file[i]);
					}else{
						temp = new File(oldPath + File.separator + file[i]);
					}
					if(temp.isFile()){
						FileInputStream input = new FileInputStream(temp);
						FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
						byte[] b = new byte[1024 * 10];
						int len;
						while((len = input.read(b)) != -1){
							output.write(b, 0, len);
						}
						output.flush();
						output.close();
						input.close();
					}
					if(temp.isDirectory() && !temp.getName().contains(".svn")){// 如果是子文件夹
						copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 创建文件夹
	 */
	public static void createFolder(String realPath){
		if(StringUtils.isNotEmpty(realPath)){
			File file = new File(realPath);
			if(file != null && !file.isDirectory()){
				file.mkdirs();
			}
		}
	}

	/**
	 * 创建ZIP文件
	 * @param sourcePath 文件或文件夹路径
	 * @param zipPath 生成的zip文件存在路径（包括文件名）
	 */
	public static boolean createZip(String sourcePath, String zipPath){
		if(StringUtils.isEmpty(sourcePath) || StringUtils.isEmpty(zipPath)){
			return false;
		}
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try{
			fos = new FileOutputStream(zipPath);
			zos = new ZipOutputStream(fos);
			writeZip(new File(sourcePath), "", zos);
		}catch(Exception e){
			return false;
		}finally{
			try{
				if(zos != null){
					zos.close();
				}
			}catch(IOException e){
				return false;
			}

		}
		return true;
	}

	/**
	 * 删除指定文件夹下所有文件
	 * @param path 文件夹完整绝对路径
	 */
	public static void deleteAllFile(String path){
		if(StringUtils.isNotEmpty(path)){
			File file = new File(path);
			if(file != null && file.exists() && file.isDirectory()){
				String[] tempList = file.list();
				File temp = null;
				for(int i = 0; i < tempList.length; i++){
					if(path.endsWith(File.separator)){
						temp = new File(path + tempList[i]);
					}else{
						temp = new File(path + File.separator + tempList[i]);
					}
					if(temp.isFile()){
						temp.delete();
					}
					if(temp.isDirectory()){
						deleteAllFile(path + File.separator + tempList[i]);// 先删除文件夹里面的文件
						deleteFolder(path + File.separator + tempList[i]);// 再删除空文件夹
					}
				}
			}
		}
	}

	/**
	 * 删除文件
	 */
	public static void deleteFile(String filePathAndName){
		try{
			if(StringUtils.isNotEmpty(filePathAndName)){
				File myDelFile = new File(filePathAndName);
				if(myDelFile != null && myDelFile.exists()){
					myDelFile.delete();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 */
	public static void deleteFile(File file){
		if(isFile(file)){
			file.delete();
		}
	}

	/**
	 * 删除文件夹
	 * @param folderPath 文件夹完整绝对路径
	 */
	public static void deleteFolder(String folderPath){
		try{
			if(StringUtils.isNotEmpty(folderPath)){
				deleteAllFile(folderPath); // 删除完里面所有内容
				File myFilePath = new File(folderPath);
				if(myFilePath != null && myFilePath.isDirectory()){
					myFilePath.delete(); // 删除空文件夹
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否文件
	 */
	public static boolean isFile(File file){
		return file != null && file.exists() && file.isFile();
	}

	/**
	 * 判断是否文件夹
	 */
	public static boolean isFolder(File file){
		return file != null && file.exists() && file.isDirectory();
	}

	/**
	 * 判断文件是否存在
	 */
	public static boolean isFileExist(String filePath){
		if(StringUtils.isNotEmpty(filePath)){
			File file = new File(filePath);
			return file != null && file.isFile();
		}
		return false;
	}

	/**
	 * 判断文件夹是否存在
	 */
	public static boolean isFolderExist(String filePath){
		if(StringUtils.isNotEmpty(filePath)){
			File file = new File(filePath);
			return file != null && file.isDirectory();
		}
		return false;
	}

	/**
	 * 移动文件
	 */
	public static void moveFile(String oldPath, String newPath){
		if(StringUtils.isNotEmpty(oldPath) && StringUtils.isNotEmpty(newPath)){
			copyFile(oldPath, newPath);
			deleteFile(oldPath);
		}
	}

	/**
	 * 移动目录
	 */
	public static void moveFolder(String oldPath, String newPath){
		if(StringUtils.isNotEmpty(oldPath) && StringUtils.isNotEmpty(newPath)){
			copyFolder(oldPath, newPath);
			deleteFolder(oldPath);
		}
	}

	private static final int buffer = 2048;

	/**
	 * 解压缩
	 */
	public static void unZip(String path){
		if(StringUtils.isNotEmpty(path)){
			int count = -1;
			int index = -1;
			String savepath = "";
			boolean flag = false;
			savepath = path.substring(0, path.lastIndexOf("\\")) + "\\";
			try{
				BufferedOutputStream bos = null;
				ZipEntry entry = null;
				FileInputStream fis = new FileInputStream(path);
				ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
				while((entry = zis.getNextEntry()) != null){
					byte data[] = new byte[buffer];

					String temp = entry.getName();

					flag = isPics(temp);
					if(!flag){
						continue;
					}

					index = temp.lastIndexOf("/");
					if(index > -1){
						temp = temp.substring(index + 1);
					}
					temp = savepath + temp;

					File f = new File(temp);
					f.createNewFile();

					FileOutputStream fos = new FileOutputStream(f);
					bos = new BufferedOutputStream(fos, buffer);

					while((count = zis.read(data, 0, buffer)) != -1){
						bos.write(data, 0, count);
					}

					bos.flush();
					bos.close();
				}
				zis.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public static boolean isPics(String filename){
		boolean flag = false;
		if(StringUtils.isNotEmpty(filename)){
			if(filename.endsWith(".jpg") || filename.endsWith(".gif") || filename.endsWith(".bmp") || filename.endsWith(".png")){
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 压缩文件夹为zip文件
	 */
	public static void parseZip(String zipPath, String currentPath){
		if(StringUtils.isNotEmpty(zipPath) && StringUtils.isNotEmpty(currentPath)){
			OutputStream os = null;
			InputStream is = null;
			ZipFile zipFile = null;
			try{
				zipFile = new ZipFile(zipPath);
				Enumeration zipList = zipFile.entries();
				ZipEntry zipEntry = null;
				byte[] buf = new byte[1024 * 10];
				int i = 0;
				while(zipList.hasMoreElements()){
					zipEntry = (ZipEntry)zipList.nextElement();
					if(zipEntry.isDirectory()){
						continue;
					}
					if(zipEntry.getName().contains(".")){
						String fileext = zipEntry.getName().substring(zipEntry.getName().lastIndexOf("."));
						if(fileext.equals(".jpg") || fileext.equals(".gif") || fileext.equals(".bmp")){
							os = new BufferedOutputStream(new FileOutputStream(currentPath + File.separator + System.currentTimeMillis() + String.valueOf(i) + fileext));
							is = new BufferedInputStream(zipFile.getInputStream(zipEntry));
							int readLen = 0;
							// 以ZipEntry为参数得到一个InputStream，并写到OutputStream中
							while((readLen = is.read(buf, 0, buffer)) != -1){
								os.write(buf, 0, readLen);
							}
							if(is != null){
								is.close();
							}
							if(os != null){
								os.close();
							}
						}
					}
					i++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					if(is != null){
						is.close();
					}
					if(os != null){
						os.close();
					}
					if(zipFile != null){
						zipFile.close();
					}
				}catch(IOException e){
					e.printStackTrace();
				}
				FileUtil.deleteFile(zipPath);
			}
		}
	}

	/**
	 * 读取文本文件内容
	 */
	public static String readTxt(File file, String encoding) throws IOException{
		StringBuilder str = new StringBuilder();
		String st = "";
		try{
			FileInputStream fs = new FileInputStream(file);
			InputStreamReader isr;
			if(encoding == null || encoding.equals("")){
				isr = new InputStreamReader(fs);
			}else{
				isr = new InputStreamReader(fs, encoding);
			}
			BufferedReader br = new BufferedReader(isr);
			try{
				String data = "";
				while((data = br.readLine()) != null){
					str.append(data + " ");
				}
			}catch(Exception e){
				str.append(e.toString());
			}
			st = str.toString();
			br.close();
		}catch(IOException es){
			st = "";
		}
		return st;
	}

	private static void writeZip(File file, String parentPath, ZipOutputStream zos){
		if(file.exists()){
			if(file.isDirectory()){//处理文件夹
				parentPath += file.getName() + File.separator;
				File[] files = file.listFiles();
				for(File f : files){
					writeZip(f, parentPath, zos);
				}
			}else{
				FileInputStream fis = null;
				try{
					fis = new FileInputStream(file);
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024 * 10];
					int len;
					while((len = fis.read(content)) != -1){
						zos.write(content, 0, len);
						zos.flush();
					}

				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try{
						if(fis != null){
							fis.close();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static final List<File> getSubFiles(File file){
		List<File> files = new ArrayList<File>();
		if(file != null){
			getFiles(file, files);
		}
		return files;
	}

	private static final void getFiles(File file, List<File> files){
		if(file.isFile()){
			files.add(file);
		}else if(file.isDirectory()){
			for(File f : file.listFiles()){
				getFiles(f, files);
			}
		}
	}

	public static final List<String> readLines(File file){
		try{
			return FileUtils.readLines(file, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static final String readFileToString(File file){
		try{
			return FileUtils.readFileToString(file, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static final void writeLines(File file, List lines){
		try{
			FileUtils.writeLines(file, "UTF-8", lines, false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static final void appendLines(File file, List lines){
		try{
			FileUtils.writeLines(file, "UTF-8", lines, true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static final void writeStringToFile(File file, String data){
		try{
			FileUtils.writeStringToFile(file, data, "UTF-8", false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static final void appendStringToFile(File file, String data){
		try{
			FileUtils.writeStringToFile(file, data, "UTF-8", true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static final byte[] toBytes(File file){
		try{
			return FileUtils.readFileToByteArray(file);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	 /** 
     * 根据byte数组，生成文件 
     */  
	public static File toFile(byte[] bfile, String filePath,String fileName) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            File dir = new File(filePath);  
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
                dir.mkdirs();  
            }  
            file = new File(filePath+File.separator+fileName);
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
        return file;
    }  
	/**
	 * 将文件头转换成16进制字符串
	 * @param 原生byte
	 * @return 16进制字符串
	 */
	private static String bytesToHexString(byte[] src){

		StringBuilder stringBuilder = new StringBuilder();
		if(src == null || src.length <= 0){
			return null;
		}
		for(int i = 0; i < src.length; i++){
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if(hv.length() < 2){
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 得到文件头
	 * @param filePath 文件路径
	 * @return 文件头
	 * @throws IOException
	 */
	private static String getFileContent(String filePath){
		byte[] b = new byte[28];
		InputStream inputStream = null;
		try{
			inputStream = new FileInputStream(filePath);
			inputStream.read(b, 0, 28);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(inputStream != null){
				try{
					inputStream.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return bytesToHexString(b);
	}

	/**
	 * 判断文件类型
	 * @param filePath 文件路径
	 * @return 文件类型
	 */
	public static FileType getType(String filePath){
		String fileHead = getFileContent(filePath);

		if(fileHead == null || fileHead.length() == 0){
			return null;
		}

		fileHead = fileHead.toUpperCase();

		FileType[] fileTypes = FileType.values();

		for(FileType type : fileTypes){
			if(fileHead.startsWith(type.getValue())){
				return type;
			}
		}
		return null;
	}

	public static final File mustBeFile(Object object){
		if(object == null){
			throw new RuntimeException("文件参数不能为空");
		}
		if(object instanceof File){
			return (File)object;
		}else if(object instanceof String){
			return new File(UrlUtil.fixUrl(object.toString()));
		}
		throw new RuntimeException("错误的文件参数类型");
	}

}
