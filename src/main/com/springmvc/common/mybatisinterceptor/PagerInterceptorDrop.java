package com.springmvc.common.mybatisinterceptor;

import com.springmvc.common.util.Pager;
import com.springmvc.common.util.ReflectUtil;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhangyuguang on 2018/10/18.
 */
/*@Intercepts({
      *//*
       * @Signature(method="prepare" , type = StatementHandler.class ,
       * args={Connection.class , Integer.class })
       *//*
        @Signature(args = { Connection.class,
                Integer.class }, method = "prepare", type = StatementHandler.class) })*/
    //mybatis 版本要求3.4.1以上  spring-mybatis 1.3.2
//@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PagerInterceptorDrop implements Interceptor {

    private String databaseType;
    // 1.
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handle = (RoutingStatementHandler) invocation.getTarget();
        StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handle, "delegate");
        BoundSql boundSql = delegate.getBoundSql();
        Object obj = boundSql.getParameterObject();
     /*   Object obj2 = ReflectUtil.getFieldValue(boundSql, "additionalparameters");*/
        if (obj != null && obj instanceof Map) {
            Map condition = (Map) obj;
            if (condition.containsKey("page")) {

            }
            Pager page = (Pager) condition.get("page");
            MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");

            String sql = boundSql.getSql();
            String pageSql = "oracle".equalsIgnoreCase(databaseType)
                    ? this.getOraclePageSql(page, new StringBuffer(sql))
                    : "mysql".equalsIgnoreCase(databaseType) ? this.getStPageSql(page, new StringBuffer(sql))
                    : this.getMyPageSql(page, new StringBuffer(sql));

            ReflectUtil.setFieldValue(boundSql, "sql", pageSql);

        }

        Object o = invocation.proceed();
        return o;
    }

    private String getMyPageSql(Pager page, StringBuffer sb) {
        int offset = (page.getCurrentPage() - 1) * page.getPageSize();
        sb.append(" limit ").append(offset).append(" , ").append(page.getPageSize() + 1);
        return sb.toString();
    }

    private String getOraclePageSql(Pager page, StringBuffer sb) {
        int offset = (page.getCurrentPage() - 1) * page.getPageSize();
        sb.insert(0, "select u.*, rownum r from (").append(") u where rownum< ").append(offset + page.getPageSize());

        sb.insert(0, "select * from ( ").append(" ) where >= ").append(offset);
        return sb.toString();

    }

    private String getStPageSql(Pager page, StringBuffer sb) {
        int offset = (page.getCurrentPage() - 1) * page.getPageSize();
       /* StringBuffer sb2 = new StringBuffer(
                sb.toString().replaceAll("order\\s+by.+\\)", ")").replaceAll("order\\s+by.+$", "")
                        .replaceAll("ORDER\\s+BY.+\\)", ")").replaceAll("ORDER\\s+BY.+$", ""));

        sb = sb2;
        sb2.append(" order by ").append(page.getSortField());
        if (page.getSortMethod() == 1) {
            sb2.append(" desc ");
        }*/
        sb.append(" limit ").append(page.getPageSize() + 1).append(" offset ").append(offset);
        return sb.toString();
    }


    // 2.
    @Override
    public Object plugin(Object target) {
        if (target instanceof RoutingStatementHandler){
            return Plugin.wrap(target,this);
        }
        return target;
    }
    // 3.
    @Override
    public void setProperties(Properties properties) {
        this.databaseType = properties.getProperty("databaseType");
    }
}
