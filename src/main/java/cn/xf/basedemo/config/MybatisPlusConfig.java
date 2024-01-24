package cn.xf.basedemo.config;

import com.baomidou.dynamic.datasource.plugin.MasterSlaveAutoRoutingPlugin;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan("cn.xf.basedemo.mappers")
@EnableTransactionManagement
public class MybatisPlusConfig {

	/**
	 * mybatis_plus插件
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		//分页插件
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		// 乐观锁插件
		interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		return interceptor;
	}

	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return new ConfigurationCustomizer() {
			@Override
			public void customize(MybatisConfiguration configuration) {
				configuration.setMapUnderscoreToCamelCase(true);
			}
		};
	}

	/**
	 * 读写分离
	 *
	 * @return MasterSlaveAutoRoutingPlugin
	 */
	@Bean
	public MasterSlaveAutoRoutingPlugin masterSlaveAutoRoutingPlugin() {
		return new MasterSlaveAutoRoutingPlugin();
	}

}
