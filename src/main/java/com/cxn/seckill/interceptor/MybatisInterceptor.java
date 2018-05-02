package com.cxn.seckill.interceptor;

import com.cxn.seckill.annotation.SqlNotInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Objects;
import java.util.Properties;

/**
 * mybatis拦截器，拦截UPDATE DELETE，防止全表更新
 * 1. @SqlNotInterceptor标识某个方法，跳过mybatis拦截器
 *
 **/
@Component
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})})
public class MybatisInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(MybatisInterceptor.class);

    /**
     * SQL类型 UPDATE
     */
    private static final String CONST_UPDATE = "UPDATE";

    /**
     * SQL类型 DELETE
     */
    private static final String CONST_DELETE = "DELETE";

    /**
     * JSqlParser 开源的SQL解析工具 http://jsqlparser.sourceforge.net/
     */
    CCJSqlParserManager parserManager = new CCJSqlParserManager();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        // 由于mappedStatement为protected的，所以要通过反射获取
        MetaObject statementHandler = SystemMetaObject.forObject(handler);
        //mappedStatement中有我们需要的方法id
        MappedStatement mappedStatement = (MappedStatement) statementHandler.getValue("delegate.mappedStatement");
        // 获取拦截到的方法名字符串
        String namespace = mappedStatement.getId();

        // 获取方法上的注解，如果dao层方法上注解了NoSqlInterceptor，则该方法跳过sql拦截
        Annotation annotation = getAnnotation(namespace);
        // 如果dao层方法上注解了NoSqlInterceptor，则该方法跳过sql拦截
        if (Objects.equals(annotation, null)) {
            // 获取到要执行的sql
            BoundSql boundSql = handler.getBoundSql();
            String sql = boundSql.getSql();
            // 获得sql类型
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            String sqlCommandTypeName = sqlCommandType.name();
            // DROP、TRUNCATE都属于UPDATE类型
            if (CONST_UPDATE.equals(sqlCommandTypeName)) {

                String lowerCaseSql = sql.trim().toLowerCase();
                if (lowerCaseSql.startsWith("truncate") || lowerCaseSql.startsWith("drop")) {
                    logger.error("方法：{}，SQL语句：{}，不允许执行（1.UPDATE语句无where条件 2.DROP 3.TRUNCATE）。", namespace, sql);
                    throw new MybatisPluginException("方法：" + namespace + "，SQL语句：" + sql + "，不允许执行。");
                }
                // 将sql转成Update对象
                Update update = (Update) parserManager.parse(new StringReader(sql));
                Expression where = update.getWhere();
                if (!validWhere(where)) {
                    logger.error("方法：{}，SQL语句：{}，不允许执行（1.UPDATE语句无where条件 2.DROP 3.TRUNCATE）。", namespace, sql);
                    throw new MybatisPluginException("方法：" + namespace + "，SQL语句：" + sql + "，不允许执行。");
                }
            } else if (CONST_DELETE.equals(sqlCommandTypeName)) {
                // 将sql转成Delete对象
                Delete delete = (Delete) parserManager.parse(new StringReader(sql));
                Expression where = delete.getWhere();
                if (!validWhere(where)) {
                    logger.error("方法：{}，SQL语句：{}，不允许执行（DELETE语句没有where条件）。", namespace, sql);
                    throw new MybatisPluginException("方法：" + namespace + "，SQL语句：" + sql + "，不允许执行。");
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 通过反射，获取方法上的注解。如果dao层方法上注解了NoSqlInterceptor，则该方法跳过sql拦截
     *
     * @param namespace dao层所在的命名空间
     * @return
     * @throws Throwable
     */
    private Annotation getAnnotation(String namespace) {
        try {
            Annotation annotation = null;
            if (!StringUtils.isEmpty(namespace)) {
                //根据明明空间，获取dao的类名称
                String className = namespace.substring(0, namespace.lastIndexOf('.'));
                //获取dao中，当前执行的方法名称
                String methodName = namespace.substring(namespace.lastIndexOf('.') + 1, namespace.length());
                Method[] methods = Class.forName(className).getMethods();
                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        annotation = method.getAnnotation(SqlNotInterceptor.class);
                        break;
                    }
                }
            }
            return annotation;
        } catch (Exception e) {
            logger.error("获取方法注解异常", e);
            return null;
        }
    }

    /**
     * 解析where语句，判断其合法性
     * 1.where语句为null
     *
     * @return
     */
    public boolean validWhere(Expression where) {
        return !Objects.equals(where, null) && !StringUtils.isEmpty(where.toString());
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.setProperties(properties);
    }

}
