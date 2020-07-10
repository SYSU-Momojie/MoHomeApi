package com.cn.momojie.utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.ss.usermodel.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtils {

	private static final FastDateFormat DF_BY_DATETIME_PATTERN = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

	public static <T> List<T> getModel(InputStream is, Class<T> clazz, Map<String, String> fieldMap) {
		String[] fields = new String[fieldMap.size()];
		String[] title = new String[fieldMap.size()];
		Set<String> keySet = fieldMap.keySet();
		keySet.toArray(fields);
		for (int i = 0; i < fields.length; i++) {
			title[i] = fieldMap.get(fields[i]);
		}
		return getModel(is, clazz, title, fields);
	}

	public static <T> List<T> getModel(InputStream is, Class<T> clazz, String[] title, String[] fields) {
		return getModel(is, clazz, title, fields, 0);
	}

	public static <T> List<T> getModel(InputStream is, Class<T> clazz, String[] title, String[] fields, int sheetNum) {
		List<T> list = new ArrayList<>();
		try (Workbook wb = WorkbookFactory.create(is)) {
			int startSheet = sheetNum;
			Sheet sheet = wb.getSheetAt(startSheet);
			Map<String, Integer> loc = getRowNum(sheet, title);
			Integer rowNum = loc.get("rowNum");// 获取标题所在行
			if (rowNum == null) {
				// 没有找到标题
				log.warn("导入结果找不到标题所在行");
			} else {
				int nl = 5;
				int n = 0; // 连续出现5个空行之后，结束
				while (true) {
					Row row = sheet.getRow(++rowNum);// 获得数据所在行
					// System.out.println("rowNum="+rowNum+" row="+row);
					if (row == null) {
						n++;
						if (nl == n) {
							break;
						}
					} else {
						T t = clazz.newInstance();
						int len = title.length;
						for (int i = 0; i < len; i++) {
							Integer rn = loc.get(title[i]);// 获得第i个标题所在的列
							// System.out.println("i="+i+" "+"rn="+rn);
							if (rn != null) {
								Cell mcCell = row.getCell(rn);// 获得数据所在单元格
								if (mcCell != null) {
									Field field = clazz.getDeclaredField(fields[i]);// 获得字段
									setFieldValue(t, field, mcCell);
								}
							}
						}
						list.add(t);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

	private static <T> void setFieldValue(T t, Field field, Cell mcCell) throws ParseException {
		Object obj = null;
		Class<?> declaringClass = field.getType();

		if (Date.class.isAssignableFrom(declaringClass)){
			obj = mcCell.getDateCellValue();
		} else {
			mcCell.setCellType(CellType.STRING);// 改单元格格式为字符型
			String value = mcCell.getStringCellValue().trim();// 获得单元格的值
			if (StringUtils.isBlank(value)) {
				obj = value;
			} else if (Integer.class.isAssignableFrom(declaringClass)) {
				obj = Integer.valueOf(value);
			} else if (Long.class.isAssignableFrom(declaringClass)) {
				obj = Long.valueOf(value);
			} else if (Double.class.isAssignableFrom(declaringClass)) {
				obj = Double.valueOf(value);
			} else if (BigDecimal.class.isAssignableFrom(declaringClass)) {
				obj = new BigDecimal(value);
			} else {
				obj = value;
			}
		}
		try {
			PropertyUtils.setProperty(t, field.getName(), obj);
		} catch (Exception e) {
			log.error("反射写入 {} 失败", field.getName(), e);
		}
	}

	/**
	 * 获得数据所在列和行
	 *
	 * @param sheet
	 * @return
	 */
	private static Map<String, Integer> getRowNum(Sheet sheet, String[] title) {
		int num = 4;// 纵向找4行
		Map<String, Integer> loc = new HashMap<>(title.length + 2);
		for (int i = 0; i < num; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				short lastCellNum = row.getLastCellNum();
				for (String name : title) {
					int rn = getRow(name, row, lastCellNum);
					if (rn != -1) {
						loc.put(name, rn);
						loc.put("rowNum", i);
					}
				}
			}
			if (loc.get("rowNum") != null) {
				// 已找到行列
				break;
			}
		}
		return loc;
	}

	/**
	 * 获得name所在列
	 *
	 * @param name
	 * @param row
	 * @return
	 */
	private static int getRow(String name, Row row, int num) {
		// int num=10;//横向找前10列
		for (int i = 0; i < num; i++) {
			Cell cell = row.getCell(i);
			if (cell != null) {
				String value = null;
				try {
					value = cell.getStringCellValue();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				if (StringUtils.isNotBlank(value)) {
					value = value.trim(); // NOSONAR
					if (name.equals(value)) {
						return i;
					}
				}
			}
		}
		return -1;
	}
}
