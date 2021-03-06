
package com.mycompany.parcinghtml;


import java.io.IOException;
import java.sql.SQLException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;


@Configuration
@EnableTransactionManagement 
public class SpringConfig {

    @Bean
    public DataSource dataSource() {

        return new EmbeddedDatabaseBuilder()
                .setType(DERBY)
                .addScript("classpath:SQLscript.sql")
                .build();
    }

    @Bean 
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public PlayerManagerImpl playerManager() throws SQLException, IOException {
        return new PlayerManagerImpl(dataSource());
    }
    
    @Bean
    public MatchManagerImpl matchManager() throws SQLException, IOException {
        return new MatchManagerImpl(dataSource());
    }
    
}
