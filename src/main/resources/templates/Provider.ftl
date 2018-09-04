package ${provider_package};

import java.util.List;

import ${entity_package}.${table_name}Po;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class ${table_name}Provider {

    protected String getColumns() {
        
        String id = "";
        <#if pk_column?exists>
          <#list pk_column as pk>
              id+="${pk.columnName}" + ",";
          </#list>
        </#if>
        return id + getColumnsWidthOutId();
    }
    
     protected String getColumnsWidthOutId() {
     
       String columns = "";
       <#if model_column?exists>
            <#list model_column as model>
                <#if pk_array?seq_contains("${model.columnName}")>
                  
				<#else>
				   columns+= "${model.columnName}"+",";
                </#if>
            </#list>
       </#if>
       return columns.substring(0,columns.length()-1);
     }

    public String add${table_name}(${table_name}Po ${table_name?uncap_first}Po) {
      return new SQL(){
        {
        
         INSERT_INTO("${table_name_small}");
    <#if model_column?exists>
        <#list model_column as model>
                if (${table_name?uncap_first}Po.get${model.camelColumnName}() != null) {
                    VALUES("${model.columnName}", "<#noparse>#</#noparse>{${model.camelColumnName?uncap_first}}");
                }
        </#list>
    </#if>
        }
      }.toString();
    }
    
    public String update${table_name}(${table_name}Po ${table_name?uncap_first}Po) {
        return new SQL() {
            {
                UPDATE("${table_name_small}");
    <#if model_column?exists>
        <#list model_column as model>
                if (${table_name?uncap_first}Po.get${model.camelColumnName}() != null) {
                    SET("${model.columnName} = <#noparse>#</#noparse>{${model.camelColumnName?uncap_first}}");
                }
        </#list>
    </#if>
    <#if pk_column?exists>
        <#list pk_column as model>
                WHERE("${model.columnName} = <#noparse>#</#noparse>{${model.camelColumnName?uncap_first}}");
        </#list>
    </#if>
            }
        }.toString();
    }
    
    public String delete${table_name}(<#rt>
    <#if pk_column?exists><#rt>
        <#list pk_column as model>
            <#lt>@Param("${model.camelColumnName?uncap_first}") ${model.javaType} ${model.camelColumnName?uncap_first}<#if model_has_next>, </#if><#rt>
        </#list>
    </#if><#lt>) {
        return new SQL() {
            {
                DELETE_FROM("${table_name_small}");
    <#if pk_column?exists>
        <#list pk_column as model>
                WHERE("${model.columnName} = <#noparse>#</#noparse>{${model.camelColumnName?uncap_first}}");
        </#list>
    </#if>
            }
        }.toString();
    }

    public String select${table_name}(<#rt>
    <#if pk_column?exists><#rt>
        <#list pk_column as model>
            <#lt>@Param("${model.camelColumnName?uncap_first}") ${model.javaType} ${model.camelColumnName?uncap_first}<#if model_has_next>, </#if><#rt>
        </#list>
    </#if><#lt>) {
        return new SQL() {
            {
                SELECT(getColumns());
                FROM("${table_name_small}");
            <#if pk_column?exists>
                <#list pk_column as model>
                WHERE("${model.columnName} = <#noparse>#</#noparse>{${model.camelColumnName?uncap_first}}");
                </#list>
            </#if>
            }
        }.toString();
    }
    
    
    public String   select${table_name}List(${table_name}Po ${table_name?uncap_first}Po){
    
       return new SQL() {
            {
                SELECT(getColumns());
                FROM("${table_name_small}");
                <#if model_column?exists>
		        <#list model_column as model>
                if (${table_name?uncap_first}Po.get${model.camelColumnName}() != null) {
                	WHERE("${model.columnName} = <#noparse>#</#noparse>{${model.camelColumnName?uncap_first}}");
                }
		        </#list>
			    </#if>
            }
        }.toString();
    
    }
    
    
    public String  batchInsert${table_name}(@Param("${table_name?uncap_first}PoList") List<${table_name}Po>  ${table_name?uncap_first}PoList){
    
         StringBuilder template = new StringBuilder("INSERT INTO ${table_name_small} ( "+getColumns()+") VALUES ");
         String partten = "(";
         <#if model_column?exists>
            <#list model_column as model>
                   partten+= "<#noparse>#</#noparse>{${table_name?uncap_first}PoList[:input].${model.columnName}}"+",";
            </#list>
       </#if>
         partten = partten.substring(0,partten.length()-1);
         partten += ")";
      
          for (int i = 0; i < ${table_name?uncap_first}PoList.size(); i++) {
            template.append(partten.replaceAll(":input", String.valueOf(i)));
            if (i < ${table_name?uncap_first}PoList.size() - 1) {
                template.append(",");
            }
        }
        return template.toString();
    }

}