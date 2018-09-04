package com.ceb.platform.autumnframework.cebfreemarkergenerator.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ceb.platform.autumnframework.cebfreemarkergenerator.config.ColumnConfig;
import com.sun.xml.internal.ws.util.StringUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @descript 
 *   获取数据字段，根据数据字段以及模板产生文件
 * @author Administrator
 *
 */
public class CodeGeneratorUtil {

	private static Properties props;
	
	private final String javaPath = "\\src\\main\\java\\";
	
	private final String resourcesPath = "\\src\\main\\resources\\";
	
	private final String clazzPath = System.getProperty("user.dir");
	
	public void generate() throws Exception {
		InputStream is = null;
		try {
			props = new Properties();
			is = CodeGeneratorUtil.class.getClassLoader().getResourceAsStream("config/generator.properties");
			props.load(is);
			
			String driverClassName = getProperty("driverClassName");
			String url = getProperty("url");
			String userName = getProperty("userName");
			String password = getProperty("password");
			String tableName = getProperty("tableName");
			//获取连接
			Connection connection = getConnection(driverClassName,url,userName,password);
			
			//获取表
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			String[] tableNames = tableName.split(",");
			for(String table :tableNames) {
				ResultSet tableSet = databaseMetaData.getTables(null,  "%", table, new String[]{"TABLE"});
				if(tableSet!=null) {
					while(tableSet.next()) {
						String table_name = tableSet.getString("TABLE_NAME");
						String changeTableName = replaceUnderLineAndUpperCase(table_name);
			            ResultSet primaryResultSet = databaseMetaData.getPrimaryKeys(null, null, table_name);
			            ResultSet columnResultSet = databaseMetaData.getColumns(null, "%", table_name, "%");
						List<String> primaryKeys = new ArrayList<>();
						List<ColumnConfig> columnConfigList = new ArrayList<ColumnConfig>();
						while(primaryResultSet.next()) {
							primaryKeys.add(primaryResultSet.getString("COLUMN_NAME"));
						}
						ColumnConfig columnConfig = null;
						while(columnResultSet.next()) {
							columnConfig = new ColumnConfig();
							columnConfig.setColumnName(columnResultSet.getString("COLUMN_NAME"));
			                // 获取字段类型
			                columnConfig.setColumnType(columnResultSet.getString("TYPE_NAME"));
			                // 转换字段名称，如 sys_name 变成 SysName
			                columnConfig.setCamelColumnName(replaceUnderLineAndUpperCase(columnResultSet.getString("COLUMN_NAME")));
			                // 字段在数据库的注释
			                columnConfig.setColumnComment(columnResultSet.getString("REMARKS"));
			                columnConfig.setJdbcType(columnConfig.getColumnType());
			                if (columnConfig.getColumnType().equals("VARCHAR") || columnConfig.getColumnType().equals("TEXT")) {
			                    columnConfig.setJavaType("String");
			                    columnConfig.setJdbcType("VARCHAR");
			                } else if (columnConfig.getColumnType().equals("TIMESTAMP")
			                        || columnConfig.getColumnType().equals("DATETIME")) {
			                    columnConfig.setJavaType("Date");
			                    columnConfig.setJdbcType("TIMESTAMP");
			                } else if (columnConfig.getColumnType().equals("INT")) {
			                    columnConfig.setJavaType("Integer");
			                    columnConfig.setJdbcType("INTEGER");
			                } else if (columnConfig.getColumnType().equals("BIGINT")) {
			                    columnConfig.setJavaType("Long");
			                } else if (columnConfig.getColumnType().equals("TINYINT")) {
			                    // columnConfig.setJavaType("Byte");
			                    columnConfig.setJavaType("Boolean");
			                } else if (columnConfig.getColumnType().equals("DOUBLE")) {
			                    columnConfig.setJavaType("Double");
			                } else if (columnConfig.getColumnType().equals("DECIMAL")) {
			                    columnConfig.setJavaType("BigDecimal");
			                    columnConfig.setJdbcType("DECIMAL");
			                } else if (columnConfig.getColumnType().equals("FLOAT")) {
			                    columnConfig.setJavaType("Float");
			                } else if (columnConfig.getColumnType().equals("SMALLINT")) {
			                    columnConfig.setJavaType("Integer");
			                    columnConfig.setJdbcType("TINYINT");
			                }
			                columnConfigList.add(columnConfig);
			            }
			            
			            for (ColumnConfig aClass : columnConfigList) {
			                for (String primaryKey : primaryKeys) {
			                    if (primaryKey.equals(aClass.getColumnName())) {
			                        aClass.setPK(true);
			                    }
			                }
			                
			            }
			            
			            // 生成实体
			            generateEntityFile(columnConfigList,changeTableName,table_name);
			            System.out.println("生成 PO实体成功");
			            // 生成实体
			            generateBOFile(columnConfigList,changeTableName,table_name);
			            System.out.println("生成BO实体成功");
			            // 生成实体
			            generateVOFile(columnConfigList,changeTableName,table_name);
			            System.out.println("生成 VO实体成功");
			            // 生成mapper.xml
			            generateMapperFile(columnConfigList,changeTableName,table_name);
			            System.out.println("生成xml文件成功");
			            // 生成服务层接口文件
			            generateMapperInterfaceFile(columnConfigList,changeTableName,table_name);
			            System.out.println("生成mapper文件成功");
			            // 生成Provider
			            generateProviderFile(columnConfigList,changeTableName,table_name);
			            System.out.println("生成provider文件成功");
					}
				}
			}
			
		}catch(Exception e) {
			 throw new RuntimeException(e);
		}finally {
			is.close();
		}
	}
	
	
	private void generateEntityFile(List<ColumnConfig> columnConfigList,String changeTableName,String table_name) throws IOException, TemplateException {

		String entityPackage = getProperty("entityPackage");
		entityPackage = entityPackage.replace(".", "\\")+"\\";
		final String suffix = "Po.java";
        final String path = clazzPath+ javaPath+ entityPackage + changeTableName + suffix;
        final String templateName = "PO.ftl";
        File file = new File(clazzPath+ javaPath+ entityPackage );
        if (!file.exists())
        {
        	 if (file.mkdirs())
             {
                System.out.println("创建 文件夹成功："+clazzPath+ javaPath+ entityPackage);
             }
        }
        File mapperFile = new File(path);
        
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("model_column", columnConfigList);
        generateFileByTemplate(templateName, mapperFile, dataMap, changeTableName,table_name);
		
	}
	
	private void generateBOFile(List<ColumnConfig> columnConfigList,String changeTableName,String table_name) throws IOException, TemplateException {

		String boPackage = getProperty("boPackage");
		boPackage = boPackage.replace(".", "\\")+"\\";
		final String suffix = "Bo.java";
        final String path = clazzPath+ javaPath+ boPackage + changeTableName + suffix;
        final String templateName = "BO.ftl";
        File file = new File(clazzPath+ javaPath+ boPackage );
        if (!file.exists())
        {
        	 if (file.mkdirs())
             {
                System.out.println("创建 文件夹成功："+clazzPath+ javaPath+ boPackage);
             }
        }
        File mapperFile = new File(path);
        
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("model_column", columnConfigList);
        generateFileByTemplate(templateName, mapperFile, dataMap, changeTableName,table_name);
		
	}
	
	private void generateVOFile(List<ColumnConfig> columnConfigList,String changeTableName,String table_name) throws IOException, TemplateException {

		String voPackage = getProperty("voPackage");
		voPackage = voPackage.replace(".", "\\")+"\\";
		final String suffix = "Vo.java";
        final String path = clazzPath+ javaPath+ voPackage + changeTableName + suffix;
        final String templateName = "VO.ftl";
        File file = new File(clazzPath+ javaPath+ voPackage );
        if (!file.exists())
        {
        	 if (file.mkdirs())
             {
                System.out.println("创建 文件夹成功："+clazzPath+ javaPath+ voPackage);
             }
        }
        File mapperFile = new File(path);
        
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("model_column", columnConfigList);
        generateFileByTemplate(templateName, mapperFile, dataMap, changeTableName,table_name);
		
	}
	
    private void generateMapperFile(List<ColumnConfig> columnConfigList,String changeTableName,String table_name) throws Exception {
        final String suffix = "Mapper.xml";
        String mapperPath = getProperty("mapperPath");
        mapperPath = mapperPath.replace(".",  "\\")+"\\";
        final String path = clazzPath + resourcesPath + mapperPath + changeTableName + suffix;
        final String templateName = "Mapper.ftl";
        File file = new File(clazzPath+ resourcesPath+ mapperPath );
        if (!file.exists())
        {
        	 if (file.mkdirs())
             {
                System.out.println("创建 文件夹成功："+clazzPath+ resourcesPath+ mapperPath);
             }
        }
        File mapperFile = new File(path);
        
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("model_column", columnConfigList);
        generateFileByTemplate(templateName, mapperFile, dataMap, changeTableName,table_name);
        
    }
	
	 private void generateMapperInterfaceFile(List<ColumnConfig> columnConfigList,String changeTableName,String table_name) throws Exception {
	        final String suffix = "Mapper.java";
	        String mapperPackage = getProperty("mapperPackage");
	        mapperPackage = mapperPackage.replace(".",  "\\")+"\\";
	        final String path = clazzPath+ javaPath+ mapperPackage + changeTableName + suffix;
	        File file = new File(clazzPath+ javaPath+ mapperPackage );
	        if (!file.exists())
	        {
	        	 if (file.mkdirs())
	             {
	                System.out.println("创建 文件夹成功："+clazzPath+ javaPath+ mapperPackage );
	             }
	        }
	        final String templateName = "Interface.ftl";
	        File mapperFile = new File(path);
	        Map<String, Object> dataMap = new HashMap<>();
	        List<ColumnConfig> pkColumns = new ArrayList<>();
	        for (ColumnConfig columnClass : columnConfigList) {
	            if (columnClass.getIsPk()) {
	                pkColumns.add(columnClass);
	            }
	        }
	        dataMap.put("pk_column", pkColumns);
	        dataMap.put("model_column", columnConfigList);
	        generateFileByTemplate(templateName, mapperFile, dataMap, changeTableName,table_name);
	    }
	    
	    private void generateProviderFile(List<ColumnConfig> columnConfigList,String changeTableName,String table_name) throws Exception {
	        final String suffix = "Provider.java";
	        String providerPackage = getProperty("providerPackage");
	        providerPackage = providerPackage.replace(".",  "\\")+"\\";
	        final String path = clazzPath+ javaPath+ providerPackage + changeTableName + suffix;
	        File file = new File(clazzPath+ javaPath+ providerPackage );
	        if (!file.exists())
	        {
	        	 if (file.mkdirs())
	             {
	                System.out.println("创建 文件夹成功："+clazzPath+ javaPath+ providerPackage );
	             }
	        }
	        final String templateName = "Provider.ftl";
	        File mapperFile = new File(path);
	        Map<String, Object> dataMap = new HashMap<>();
	        List<ColumnConfig> pkColumns = new ArrayList<>();
	        for (ColumnConfig columnClass : columnConfigList) {
	            if (columnClass.getIsPk()) {
	                pkColumns.add(columnClass);
	            }
	        }
	        dataMap.put("pk_column", pkColumns);
	        String[] strings = new String[pkColumns.size()];
	        for(int i =0;i<pkColumns.size();i++) {
	        	strings[i] = pkColumns.get(i).getColumnName();
	        }
	        dataMap.put("pk_array", strings);
	        dataMap.put("model_column", columnConfigList);
	        generateFileByTemplate(templateName, mapperFile, dataMap, changeTableName,table_name);
	    }


	private void generateFileByTemplate(String templateName, 
			File file, Map<String, Object> dataMap,String changeTableName,String table_name) throws IOException, TemplateException {
		
		String author = getProperty("author");
		String currentDate = getProperty("currentDate");
		String tableAnnotation = getProperty("tableAnnotation");
		String entityPackage = getProperty("entityPackage");
		String providerPackage = getProperty("providerPackage");
		String repositoryPackage = getProperty("repositoryPackage");
		String mapperPackage = getProperty("mapperPackage");
		String boPackage = getProperty("boPackage");
		String voPackage = getProperty("voPackage");
		
		
		Template template = FreeMarkerTemplateUtil.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(file);
        
        
        dataMap.put("table_name_small", table_name);
        dataMap.put("table_name", changeTableName);
        dataMap.put("author", author);
        dataMap.put("date", currentDate);
        dataMap.put("entity_package", entityPackage);
        dataMap.put("provider_package", providerPackage);
        dataMap.put("repository_package", repositoryPackage);
        dataMap.put("bo_package", boPackage);
        dataMap.put("vo_package", voPackage);
        dataMap.put("mapper_package", mapperPackage);
        dataMap.put("table_annotation", tableAnnotation);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 10240);
        template.process(dataMap, out);		
	}


	public static String getProperty(String key){
        return props.getProperty(key);
    }
	
	/**
	 * 获取连接
	 * @param driverClassName
	 * @param url
	 * @param userName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	 public Connection getConnection(String driverClassName,
			 String url,String userName,String password) throws Exception {
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url, userName, password);
        return connection;
	 }
	 
	 public String replaceUnderLineAndUpperCase(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(str);
        int count = sb.indexOf("_");
        while (count != 0) {
            int num = sb.indexOf("_", count);
            count = num + 1;
            if (num != -1) {
                char ss = sb.charAt(count);
                char ia = (char) (ss - 32);
                sb.replace(count, count + 1, ia + "");
            }
        }
        String result = sb.toString().replaceAll("_", "");
        return StringUtils.capitalize(result);
	}
}
