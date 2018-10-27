package com.springmvc.common.mybatisinterceptor;

import com.springmvc.common.util.Pages;
import com.springmvc.common.util.ReflectUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhangyuguang on 2018/10/18.
 */


//下面就是要拦截器的方法
//prepare(java.sql.Connection);
/**
 * 这个注解必须加
 * 为的是告诉Mybatis的拦截器要
 * 拦截的方法
 *  //mybatis 版本要求3.4.1以上  spring-mybatis 1.3.2
 */
@Intercepts(@Signature(args = {Connection.class,Integer.class},method = "prepare",type = StatementHandler.class))
public class PagianInterceptor implements Interceptor {
    //存储是哪个数据库，在paginationSql方法好进行判断
    //是在mybatis的核心配置文件mybatis-config.xml中配置的
    private String databaseType;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target =   invocation.getTarget();
        RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler)target;
        //使用工具类得到PreparedStatementHandler
        StatementHandler statementHandler = (StatementHandler) ReflectUtil.getFieldValue(routingStatementHandler,"delegate");
        //这个对象封装了SQL语句
        BoundSql boundSql = statementHandler.getBoundSql();
        //封装的参数的类型
        Object paramObject =  boundSql.getParameterObject();
        if (paramObject == null){
            //保证不做分页也能正常查询
            return  invocation.proceed();
            //throw  new  NullPointerException("paramObject 尚未施例話" );
        }

        if (paramObject != null){
            Map map =   (Map)paramObject;
            String sql = boundSql.getSql();
            //注意这里的from要大写 //得到计算总记录数的sql。
            String countSql = "SELECT COUNT(*)  " + sql.substring(sql.lastIndexOf("FROM"));
            //通过工具类，得到MappedStatement对象
            //工具类是通过http://haohaoxuexi.iteye.com/blog/1851081这篇
            //博客改造过来的
            MappedStatement mappedStatement =(MappedStatement) ReflectUtil.getFieldValue(statementHandler,"mappedStatement");
            //得到通过注解得到参数
            Connection connection =(Connection) invocation.getArgs()[0];
            Pages pages = (Pages) map.get("page");
            //查询总记录数
            setTotalRecord(pages,mappedStatement,connection,countSql);
            //将SQL语句进行封装。
            String changeValue =  pagiantionSql(sql,pages);
            //改变BoundSql对象的私有属性sql的值
            changePrivateAttributeValue(boundSql,"sql",changeValue);


        }else {
            throw  new  RuntimeException("不是 page类型");
        }
        //得到要执行的方法
        Method method = invocation.getMethod();
        //为了看起来像点struts2
        //可以使用这个invocation.proceed()
        Object object = method.invoke(target,invocation.getArgs());

        return object;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof RoutingStatementHandler){
            return Plugin.wrap(target,this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        //获取从mybatis-config.xml中  plugins中的name  <property name="databaseType" value="mysql"></property>
        this.databaseType = properties.getProperty("databaseType");
    }

    /**
     * 查询总记录数的方法，下面就是Mybatis怎么样
     * 执行SQL语句
     * @param pages 封装了参数
     * @param mappedStatement
     * @param connection 链接
     * @param countSql sql语句
     */
    private  void  setTotalRecord(Pages pages , MappedStatement mappedStatement, Connection connection ,String countSql){
        //获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
        //delegate里面的boundSql也是通过mappedStatement.getBoundSql(paramObj)方法获取到的。
        BoundSql boundSql = mappedStatement.getBoundSql(pages);
        //获取到我们自己写在Mapper映射语句中对应的Sql语句
//	       String sql = boundSql.getSql();
        //通过查询Sql语句获取到对应的计算总记录数的sql语句
//	       String countSql = this.getCountSql(sql);
        //通过BoundSql获取对应的参数映射
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        //利用Configuration、查询记录数的Sql语句countSql、
        //参数映射关系parameterMappings和参数对象page建立查询记录数对应的BoundSql对象。
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(),countSql,parameterMappings,pages);
        //通过mappedStatement、参数对象page和BoundSql对象countBoundSql建立一个用于设定参数的ParameterHandler对象

        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,pages,countBoundSql);
        //通过connection建立一个countSql对应的PreparedStatement对象。
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(countSql);
            //通过parameterHandler给PreparedStatement对象设置参数
            parameterHandler.setParameters(pstmt);
            //之后就是执行获取总记录数的Sql语句和获取结果了。
            rs = pstmt.executeQuery();
            if (rs.next()){
                int totalRecord =  rs.getInt(1);
                //给当前的参数page对象设置总记录数
                pages.setCountRecord(totalRecord);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (rs!=null){
                    rs.close();
                }
                if (pstmt!=null){
                    pstmt.close();
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
        }


    }
    /**
     * 判断是哪个数据库
     * 将查询语句的组装成分页语句
     * @param sql
     * @return
     */
    private String  pagiantionSql(String sql , Pages pages){
        if (this.databaseType.equals("mysql")){
            pages.setStartIndex((pages.getCurrentPage()-1)*pages.getPageSize());
            return  mySQLPaginationSql(sql,pages.getStartIndex(),pages.getPageSize());
        }else {
            return oraclePaginationSql(sql,pages);
        }

    }

    private String  oraclePaginationSql(String sql, Pages pages){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM");
        sb.append("(");
        sb.append("SELECT A.* ,ROWNUM RN");
        sb.append("FROM (");
        sb.append(sql);
        sb.append(") A");
        sb.append("WHERE ROWNUM <"+pages.getStartIndex());
        sb.append(")");
        sb.append("WHERE RN >=" +pages.getStartIndex());
        String resultSql = sb.toString();
        return  resultSql;
    }

    private String  mySQLPaginationSql(String sql, int startIndex,int pageSize){
        StringBuffer sb = new StringBuffer();
        sb.append(sql);
        sb.append(" limit "+startIndex+","+pageSize);
        return sb.toString();
    }

    private  static  void   changePrivateAttributeValue(Object target , String attributeName,Object changeValue){
        ObjectFactory objectFactory = new DefaultObjectFactory();
        ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
        ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
        MetaObject metaObject = MetaObject.forObject(target,objectFactory,objectWrapperFactory,reflectorFactory);
        metaObject.setValue(attributeName,changeValue);
    }
}
