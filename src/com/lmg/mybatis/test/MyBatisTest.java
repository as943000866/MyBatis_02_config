package com.lmg.mybatis.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.lmg.mybatis.bean.Employee;
import com.lmg.mybatis.dao.EmployeeMapper;
import com.lmg.mybatis.dao.EmployeeMapperAnnotation;

/**
 * 1. 接口式编程
 * 原生: 			Dao 	===> DaoImpl
 * mybatis: 	Mapper 	===> xxMapper.xml
 * 
 * 2. SqlSession 代表和数据库的一次会话, 用完必须关闭.
 * 3. SqlSession 和 Connection 一样都是非线程安全的.
 * 4. mapper 接口没有实现类, 但是 mybatis 会为这个接口生成一个代理对象.
 * 		(将接口 和 xml 进行绑定)
 * 		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
 * 5. 两个重要的配置文件:
 * 		mybatis 的全局配置文件: 包含数据库连接池信息, 事务管理器信息等... 系统运行环境信息
 * 		sql映射文件:保存了每一个 sql 语句的映射信息:
 * 					将 sql 抽取出来
 *   
 * @author Administrator
 *
 */
public class MyBatisTest {
	
	private SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-conf.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		return sqlSessionFactory;
	}
	/**
	 * 1. 根据xml配置文件 (全局配置文件) 创建一个 SqlSessionFactory 对象
	 * 		有数据源一些运行环境信息
	 * 2. sql 映射文件:配置了每一个 sql, 以及 sql 的封装规则等。
	 * 3. 将 sql 映射文件注册在全局配置文件中
	 * 4. 写代码:
	 * 		 1). 根据全局配置文件得到 SqlSessionFactory
	 * 		 2). 使用 sqlSession 工厂, 获取 sqlSession 对象使用他来执行增删改查
	 * 			 一个 sqlSession 就是代表和数据库的一次会话, 用完关闭
	 * 		 3). 使用 sql 的唯一标识来告诉 MyBatis 执行哪个 sql。sql 都是保存在 sql 映射文件中的。
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException{
		String resource = "mybatis-conf.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		//2.获取sqlSession实例,能直接执行已经映射的sql语句
		//statement Unique identifier matching the statement to use.
		//parameter A parameter object to pass to the statement.
		
		SqlSession session = sqlSessionFactory.openSession();
		try {
			Employee employee = session.selectOne("com.lmg.mybatis.EmployeeMapper.selectEmp", 1);
			System.out.println(employee);
		} finally {
			session.close();
			
		}
		
	}
	
	@Test
	public void test01() throws IOException{
		//1. 获取sqlSessionFactory对象
		SqlSessionFactory sessionFactory = getSqlSessionFactory();
		
		//2. 获取sqlSession对象
		SqlSession session = sessionFactory.openSession();
		
		try {
			
			//3.获取接口的实现类对象
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			
			Employee employee = mapper.getEmpById(1);
			System.out.println(mapper.getClass());
			System.out.println(employee);
		} finally {
			
			session.close();
		}
		
	}
	
	@Test
	public void test02() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		
		try {
			EmployeeMapperAnnotation mapper = openSession.getMapper(EmployeeMapperAnnotation.class);
			Employee empById = mapper.getEmpById(1);
			System.out.println(empById);
			
		}finally{
			openSession.close();
		}
	}
}
