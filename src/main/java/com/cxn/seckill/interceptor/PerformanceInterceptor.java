package com.cxn.seckill.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;

/**
 * 性能分析拦截器，用于输出每条SQL语句及其执行时间
 * 1.可设置maxTime，用于在sql执行超过maxTime后抛出异常
 * 2.可设置performanceSwitch，用于控制该拦截器是否使用
 *
 * @Date 2018-04-02
 */
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class PerformanceInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(PerformanceInterceptor.class);
    private static final String DRUID = "com.alibaba.druid.pool.DruidPooledPreparedStatement";
    private static final String C3P0 = "com.mchange.v2.c3p0.impl.NewProxyPreparedStatement";
    /**
     * SQL 执行最大时长，超过抛出异常，有利于发现问题
     * 默认设置为1000ms
     */
    private long maxTime = 1000;
    /**
     * 性能分析器开关：默认打开
     */
    private boolean performanceSwitch = true;
    private Method druidGetSQLMethod;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //如果性能分析器开关打开，则进行性能分析
        if (performanceSwitch) {
            Statement statement;
            Object firstArg = invocation.getArgs()[0];
            if (Proxy.isProxyClass(firstArg.getClass())) {
                statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
            } else {
                statement = (Statement) firstArg;
            }
            try {
                statement = (Statement) SystemMetaObject.forObject(statement).getValue("stmt.statement");
            } catch (Exception e) {
                logger.error("PerformanceInterceptor get stmt.statement error!");
            }
            //获取待执行的sql
            String originalSql = getOriginalSql(statement);
            // 计算执行 SQL 耗时
            long start = System.currentTimeMillis();
            Object result = invocation.proceed();
            long timing = System.currentTimeMillis() - start;
            // 格式化 SQL 打印执行结果
            StringBuilder str = new StringBuilder(100);
            str.append("Cost Time ");
            str.append(timing);
            str.append(" ms.");
            str.append("\n Execute SQL：").append(originalSql);
            String sqlInfo = str.toString();
            if (this.getMaxTime() >= 1 && timing > this.getMaxTime()) {
                logger.error("{} The SQL execution time is too large, please optimize !", sqlInfo);
                throw new MybatisPluginException(sqlInfo + " The SQL execution time is too large, please optimize !");
            }
            logger.info(sqlInfo);
            return result;
        } else {
            return invocation.proceed();
        }
    }

    /**
     * 获取待执行的ql
     *
     * @param statement
     * @return 待执行的sql
     */
    private String getOriginalSql(Statement statement) {
        String originalSql = null;
        String stmtClassName = statement.getClass().getName();
        if (DRUID.equals(stmtClassName)) {
            try {
                if (Objects.equals(druidGetSQLMethod, null)) {
                    Class<?> clazz = Class.forName(DRUID);
                    druidGetSQLMethod = clazz.getMethod("getSql");
                }
                //执行该Method.invoke方法的参数是执行这个方法的对象owner，和参数数组args
                Object stmtSql = druidGetSQLMethod.invoke(statement);
                if (!Objects.equals(stmtSql, null) && stmtSql instanceof String) {
                    originalSql = (String) stmtSql;
                }
            } catch (Exception ignored) {
                logger.error("get druid sql error!");
            }
        } else if (C3P0.equals(stmtClassName)) {
            try {
                Object sqlStatement = SystemMetaObject.forObject(statement).getValue("inner");
                if (!Objects.equals(sqlStatement, null)) {
                    originalSql = sqlStatement.toString();
                }
            } catch (Exception ignored) {
                logger.error("get c3p0 sql error!");
            }
        }

        //格式化sql
        if (Objects.equals(originalSql, null)) {
            originalSql = statement.toString();
        }
        int index = originalSql.indexOf(':');
        if (index > 0) {
            originalSql = originalSql.substring(index + 1, originalSql.length());
        }
        return originalSql;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties prop) {
        this.setProperties(prop);
    }

    public long getMaxTime() {
        return maxTime;
    }

    public PerformanceInterceptor setMaxTime(long maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public boolean isPerformanceSwitch() {
        return performanceSwitch;
    }

    public void setPerformanceSwitch(boolean performanceSwitch) {
        this.performanceSwitch = performanceSwitch;
    }
}
