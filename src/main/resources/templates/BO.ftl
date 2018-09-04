package ${bo_package};

import java.util.Date;

/**
* 描述：${table_annotation}模型
* @author ${author}
* @date ${date}
*/
public class ${table_name}Bo {

<#if model_column?exists>
  <#list model_column as model>
  <#if (model.columnType = 'VARCHAR' || model.columnType = 'TEXT')>
  private String ${model.camelColumnName?uncap_first};
  </#if>
  <#if (model.columnType = 'TIMESTAMP' || model.columnType = 'DATETIME') >
  private Date ${model.camelColumnName?uncap_first};
  </#if>
  <#if model.columnType = 'INT' || model.columnType = 'SMALLINT'>
  private Integer ${model.camelColumnName?uncap_first};
  </#if>
  <#if model.columnType = 'BIGINT' >
  private Long ${model.camelColumnName?uncap_first};
  </#if>
  <#if model.columnType = 'TINYINT' >
  private Boolean ${model.camelColumnName?uncap_first};
  </#if>
  <#if model.columnType = 'DOUBLE' >
  private Double ${model.camelColumnName?uncap_first};
  </#if>
  <#if model.columnType = 'DECIMAL' >
  private BigDecimal ${model.camelColumnName?uncap_first};
  </#if>
  <#if model.columnType = 'FLOAT' >
  private Float ${model.camelColumnName?uncap_first};
  </#if>
  </#list>
</#if>   

<#if model_column?exists>
    <#list model_column as model>
        <#if (model.columnType = 'VARCHAR' || model.columnType = 'TEXT')>
  public String get${model.camelColumnName}() {
    return this.${model.camelColumnName?uncap_first};
  }

  public void set${model.camelColumnName}(String ${model.camelColumnName?uncap_first}) {
    this.${model.camelColumnName?uncap_first} = ${model.camelColumnName?uncap_first};
  }

        </#if>
        <#if model.columnType = 'TIMESTAMP' || model.columnType = 'DATETIME'>
  public Date get${model.camelColumnName}() {
    return this.${model.camelColumnName?uncap_first};
  }

  public void set${model.camelColumnName}(Date ${model.camelColumnName?uncap_first}) {
    this.${model.camelColumnName?uncap_first} = ${model.camelColumnName?uncap_first};
  }

        </#if>
        <#if model.columnType = 'INT' || model.columnType = 'SMALLINT'>
  public Integer get${model.camelColumnName}() {
    return this.${model.camelColumnName?uncap_first};
  }

  public void set${model.camelColumnName}(Integer ${model.camelColumnName?uncap_first}) {
    this.${model.camelColumnName?uncap_first} = ${model.camelColumnName?uncap_first};
  }

        </#if>
        <#if model.columnType = 'BIGINT' >
  public Long get${model.camelColumnName}() {
    return this.${model.camelColumnName?uncap_first};
  }

  public void set${model.camelColumnName}(Long ${model.camelColumnName?uncap_first}) {
    this.${model.camelColumnName?uncap_first} = ${model.camelColumnName?uncap_first};
  }

        </#if>
        <#if model.columnType = 'TINYINT' >
  public Boolean get${model.camelColumnName}() {
    return this.${model.camelColumnName?uncap_first};
  }

  public void set${model.camelColumnName}(Boolean ${model.camelColumnName?uncap_first}) {
    this.${model.camelColumnName?uncap_first} = ${model.camelColumnName?uncap_first};
  }

        </#if>
        <#if model.columnType = 'DECIMAL' >
  public BigDecimal get${model.camelColumnName}() {
     return this.${model.camelColumnName?uncap_first};
  }

  public void set${model.camelColumnName}(BigDecimal ${model.camelColumnName?uncap_first}) {
    this.${model.camelColumnName?uncap_first} = ${model.camelColumnName?uncap_first};
  }

        </#if>
        <#if model.columnType = 'DOUBLE' >
  public Double get${model.camelColumnName}() {
    return this.${model.camelColumnName?uncap_first};
  }

  public void set${model.camelColumnName}(Double ${model.camelColumnName?uncap_first}) {
    this.${model.camelColumnName?uncap_first} = ${model.camelColumnName?uncap_first};
  }

        </#if>
        <#if model.columnType = 'FLOAT' >
  public Float get${model.camelColumnName}() {
    return this.${model.camelColumnName?uncap_first};
  }

  public void set${model.camelColumnName}(Float ${model.camelColumnName?uncap_first}) {
    this.${model.camelColumnName?uncap_first} = ${model.camelColumnName?uncap_first};
  }
        </#if>
    </#list>
</#if>
}