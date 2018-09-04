package ${mapper_package};

import java.util.List;
import ${entity_package}.${table_name}Po;
import ${provider_package}.${table_name}Provider;
import org.apache.ibatis.annotations.*;

/**
* 描述：${table_annotation} 服务实现层接口
* @author ${author}
* @date ${date}
*/

public interface ${table_name}Mapper {

  @InsertProvider(type = ${table_name}Provider.class, method = "add${table_name}")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int add${table_name}(${table_name}Po ${table_name?uncap_first}Po);

  @UpdateProvider(type = ${table_name}Provider.class, method = "update${table_name}")
  int update${table_name}(${table_name}Po ${table_name?uncap_first}Po);

  @DeleteProvider(type = ${table_name}Provider.class, method = "delete${table_name}")
  int delete${table_name}(<#rt>
  <#if pk_column?exists><#rt>
    <#list pk_column as model>
      <#lt>@Param("${model.camelColumnName?uncap_first}") ${model.javaType} ${model.camelColumnName?uncap_first}<#if model_has_next>, </#if><#rt>
      </#list>
    </#if>
  <#lt>);

  @SelectProvider(type = ${table_name}Provider.class, method = "select${table_name}")
  @ResultMap("${table_name?uncap_first}Po")
  ${table_name}Po select${table_name}(<#rt>
  <#if pk_column?exists><#rt>
    <#list pk_column as model>
      <#lt>@Param("${model.camelColumnName?uncap_first}") ${model.javaType} ${model.camelColumnName?uncap_first}<#if model_has_next>, </#if><#rt>
    </#list>
  </#if><#lt>);
  
  @SelectProvider(type = ${table_name}Provider.class,method = "select$(table_name)List")
  @ResultMap("${table_name?uncap_first}Po")
  List< ${table_name}Po > select${table_name}List(${table_name}Po ${table_name?uncap_first}Po);
  
  @InsertProvider(type = ${table_name}Provider.class, method = "batchInsert${table_name}")
  int batchInsert${table_name}(@Param("${table_name?uncap_first}PoList") List<${table_name}Po>  ${table_name?uncap_first}PoList);
  
  

}