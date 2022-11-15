package com.huii.admin.common.utils;

import com.huii.admin.common.annotation.FiledName;
import com.huii.admin.common.annotation.NotField;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel简单工具类
 * *
 * 注解1 FiledName 获取sheet名称
 * 注解2 NotField 被注解的字段不会被解析
 * *
 * List<T> list 数据段
 * Class<?> clazz 实体类
 * 数据段与实体类类型需对应
 * *
 * @author huii
 * @param <T>
 */
@Data
@Component
public class ExcelUtil<T> {

    public void createExcel(List<T> list, Class<?> clazz, HttpServletResponse response) {
        //获取实体类字段
        List<String> nameList = getFiledName(clazz);
        int fieldNums = nameList.size();
        //创建POI对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        //设置sheet标题
        String name = clazz.getAnnotation(FiledName.class).value();
        HSSFSheet sheet = workbook.createSheet(name);
        //设置文件标题
        String fileName = convertTime();
        //设置首行字段
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < fieldNums; i++) {
            row.createCell(i).setCellValue(nameList.get(i));
        }
        //填充数据段
        for (int i = 0; i < list.size(); i++) {
            HSSFRow row_ = sheet.createRow(i + 1);
            for (int j = 0; j <fieldNums; j++) {
                row_.createCell(j).setCellValue(castObject(invokeGet(list.get(i),nameList.get(j))));
            }
        }
        try {
            OutputStream os = response.getOutputStream();
            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((fileName + ".xls").getBytes("gb2312"), "ISO8859-1"));
            workbook.write(os);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getName(){
        return convertTime();
    }

    /**
     * 反射获取实体类字段
     * @param clazz class类型
     * @return list
     */
    public static List<String> getFiledName(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        List<String> list = new ArrayList<>();
        for (Field  f : fields) {
            NotField annotation = f.getAnnotation(NotField.class);
            if(ObjectUtils.isEmpty(annotation)){
             list.add(f.getName());
            }
        }
        return list;
    }

    /**
     * 反射获取方法
     * @param objectClass
     * @param fieldName
     * @return
     */
    public static Method getGetMethod(Class objectClass, String fieldName) {
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            return objectClass.getMethod(sb.toString());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 反射get
     * @param o
     * @param fieldName
     * @return
     */
    public static Object invokeGet(Object o, String fieldName) {
        Method method = getGetMethod(o.getClass(), fieldName);
        try {
            return method.invoke(o, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射设置方法
     * @param objectClass
     * @param fieldName
     * @return
     */
    public static Method getSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射set
     * @param o
     * @param fieldName
     * @param value
     */
    public static void invokeSet(Object o, String fieldName, Object value) {
        Method method = getSetMethod(o.getClass(), fieldName);
        try {
            method.invoke(o, new Object[] { value });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换object类型
     * 有需要可自行添加
     * @param object object
     * @return string
     */
    public static String castObject(Object object){
        String str = "";
        if(object instanceof Long){
            str+=Long.parseLong(object.toString());
        }
        if(object instanceof Integer){
            str+=Integer.parseInt(object.toString());
        }
        if(object instanceof LocalDateTime){
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            str+=((LocalDateTime) object).format(fmt);
        }
        if(object instanceof String){
            str+=object;
        }
        return str;
    }

    /**
     * 获取时间
     * @return
     */
    public static String convertTime() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime time = LocalDateTime.now();
        return fmt.format(time);
    }

}
