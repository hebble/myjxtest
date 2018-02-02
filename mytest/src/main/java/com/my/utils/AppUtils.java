package com.my.utils;


import java.io.Serializable;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.CRC32;

import org.apache.log4j.Logger;

public class AppUtils {
	private static Logger logger = Logger.getLogger(AppUtils.class);

	public static String getDisplayDate(Calendar pCalendar) {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		return pCalendar != null ? format.format(pCalendar.getTime()) : "";
	}

	public static Calendar str2Calendar(String str) {
		Calendar cal = null;
		if (isNotBlank(str)) {
			try {
				SimpleDateFormat e = new SimpleDateFormat("MM/dd/yyyy");
				Date d = e.parse(str);
				cal = Calendar.getInstance();
				cal.setTime(d);
			} catch (ParseException arg3) {
				;
			}
		}

		return cal;
	}

	public static String getCurrentDate() {
		return getDisplayDate(GregorianCalendar.getInstance());
	}

	public static boolean isBlank(String str) {
		return str == null || str.trim().length() <= 0;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isBlank(Object[] objs) {
		return objs == null || objs.length <= 0;
	}

	public static boolean isNotBlank(Object[] objs) {
		return !isBlank(objs);
	}

	public static boolean isBlank(Object objs) {
		return objs == null || "".equals(objs);
	}

	public static boolean isNotBlank(Object objs) {
		return !isBlank(objs);
	}

	public static boolean isBlank(Collection obj) {
		return obj == null || obj.size() <= 0;
	}

	public static boolean isNotBlank(Collection obj) {
		return !isBlank(obj);
	}

	public static boolean isBlank(Set obj) {
		return obj == null || obj.size() <= 0;
	}

	public static boolean isNotBlank(Set obj) {
		return !isBlank(obj);
	}

	public static boolean isBlank(Serializable obj) {
		return obj == null;
	}

	public static boolean isNotBlank(Serializable obj) {
		return !isBlank(obj);
	}

	public static boolean isBlank(Map obj) {
		return obj == null || obj.size() <= 0;
	}

	public static boolean isNotBlank(Map obj) {
		return !isBlank(obj);
	}
	
	public static boolean isOneBlank(Object ...objects) {
		if (AppUtils.isNotBlank(objects)) {
			for (Object object : objects) {
				if (AppUtils.isBlank(object)) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}

	public static boolean isAllNotBlank(Object ...objects) {
		return !isOneBlank(objects);
	}
	
	public static String[] list2Strings(List<String> list) {
		String[] value = null;

		try {
			if (list == null) {
				return null;
			}

			value = new String[list.size()];

			for (int e = 0; e < list.size(); ++e) {
				value[e] = (String) list.get(e);
			}
		} catch (Exception arg2) {
			logger.error("list is null: " + arg2);
		}

		return value;
	}

	public static String list2String(List<Object> list) {
		if (isBlank((Collection) list)) {
			return "";
		} else {
			StringBuffer sbuf = new StringBuffer();
			sbuf.append(list.get(0));

			for (int idx = 1; idx < list.size(); ++idx) {
				sbuf.append(",");
				sbuf.append(list.get(idx));
			}

			return sbuf.toString();
		}
	}

	public static List<String> Strings2List(String[] args) {
		ArrayList list = new ArrayList();

		try {
			if (args == null) {
				return null;
			}

			for (int e = 0; e < args.length; ++e) {
				list.add(args[e]);
			}
		} catch (Exception arg2) {
			logger.error("list is null: " + arg2);
		}

		return list;
	}

	public static String[] getStrings(String str) {
		List values = getStringCollection(str);
		return values.size() == 0 ? null : (String[]) values.toArray(new String[values.size()]);
	}

	public static List<String> getStringCollection(String str) {
		ArrayList values = new ArrayList();
		if (str == null) {
			return values;
		} else {
			StringTokenizer tokenizer = new StringTokenizer(str, ",");
			values = new ArrayList();

			while (tokenizer.hasMoreTokens()) {
				values.add(tokenizer.nextToken());
			}

			return values;
		}
	}

	public static String formatNumber(Long number) {
		if (number == null) {
			return null;
		} else {
			NumberFormat format = NumberFormat.getIntegerInstance();
			format.setMinimumIntegerDigits(8);
			format.setGroupingUsed(false);
			return format.format(number);
		}
	}

	public static Long getCRC32(String value) {
		CRC32 crc32 = new CRC32();
		crc32.update(value.getBytes());
		return Long.valueOf(crc32.getValue());
	}

	public static String arrayToString(String[] strs) {
		if (strs.length == 0) {
			return "";
		} else {
			StringBuffer sbuf = new StringBuffer();
			sbuf.append(strs[0]);

			for (int idx = 1; idx < strs.length; ++idx) {
				sbuf.append(",");
				sbuf.append(strs[idx]);
			}

			return sbuf.toString();
		}
	}

	public static String getDefaultValue(String value, String defaultValue) {
		return isNotBlank(value) ? value : defaultValue;
	}

	public static String encodePassword(String password, String algorithm) {
		byte[] unencodedPassword = password.getBytes();
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception arg6) {
			arg6.printStackTrace();
			return password;
		}

		md.reset();
		md.update(unencodedPassword);
		byte[] encodedPassword = md.digest();
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < encodedPassword.length; ++i) {
			if ((encodedPassword[i] & 255) < 16) {
				buf.append("0");
			}

			buf.append(Long.toString((long) (encodedPassword[i] & 255), 16));
		}

		return buf.toString();
	}

	public static String md5(String password) {
		return encodePassword(password, "md5");
	}

	public static String sha1(String password) {
		return encodePassword(password, "sha1");
	}

	public static boolean isTheSame(Object a, Object b) {
		return a == null && b == null ? true : a != null && a.equals(b);
	}

	public static String removeYanText(String nickName) {
		if (isBlank(nickName)) {
			return "";
		} else {
			byte[] b_text = nickName.getBytes();

			for (int i = 0; i < b_text.length; ++i) {
				if ((b_text[i] & 248) == 240) {
					for (int j = 0; j < 4; ++j) {
						b_text[i + j] = 0;
					}

					i += 3;
				}
			}

			return new String(b_text);
		}
	}
}