package com.ceb.platform.autumnframework.cebfreemarkergenerator.config;

/**
 * 获取数据库类型
 * @author Administrator
 *
 */
public class ColumnConfig {

  /** 数据库字段名称  **/
  private String columnName;
  /** 数据库字段类型 **/
  private String columnType;
  /** jdbc字段类型 **/
  private String jdbcType;
  /** 驼峰式字段名称 **/
  private String camelColumnName;
  /** 数据库字段注释 **/
  private String columnComment;
  /** 是否主键 **/
  private boolean isPK;
  /** java数据类型  **/
  private String javaType;
  
  public String getColumnName() {
	return columnName;
  }
  public void setColumnName(String columnName) {
	this.columnName = columnName;
  }
  public String getColumnType() {
	return columnType;
  }
  public void setColumnType(String columnType) {
	this.columnType = columnType;
  }
  public String getJdbcType() {
	return jdbcType;
  }
  public void setJdbcType(String jdbcType) {
	this.jdbcType = jdbcType;
  }
  public String getCamelColumnName() {
	return camelColumnName;
  }
  public void setCamelColumnName(String camelColumnName) {
	this.camelColumnName = camelColumnName;
  }
  public String getColumnComment() {
	return columnComment;
  }
  public void setColumnComment(String columnComment) {
	this.columnComment = columnComment;
  }
  public boolean getIsPk() {
	return isPK;
  }
  public void setPK(boolean isPK) {
	this.isPK = isPK;
  }
  public String getJavaType() {
	return javaType;
  }
  public void setJavaType(String javaType) {
	this.javaType = javaType;
  }
}
