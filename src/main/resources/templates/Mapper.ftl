<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace=" ${mapper_package}.${table_name}Mapper">

    <resultMap id="${table_name?uncap_first}Po" type="${entity_package}.${table_name}Po">
    <#if model_column?exists>
        <#list model_column as model>
            <#if model.isPk>
        <id column="${model.columnName?uncap_first}" property="${model.camelColumnName?uncap_first}" jdbcType="${model.jdbcType}" />
            </#if>
            <#if !model.isPk>
        <result column="${model.columnName?uncap_first}" property="${model.camelColumnName?uncap_first}" jdbcType="${model.jdbcType}" />
            </#if>
        </#list>
    </#if>
    </resultMap>

</mapper>