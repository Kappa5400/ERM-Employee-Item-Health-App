package com.healthapp.itemhealth.config;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

  // Datasource hooks up our code to the database
  @Autowired private DataSource dataSource;


  // On init we run this flyway 'migration' class
  // it has the datasource hookup the connection to postgres
  // finds the migration files in the recources/db/migration folder
  // and runs them in order.
  // baselineOnMigrate sets it so that flyway creates schema_history if not detected
  // cleandisabled so don't delete db
  @Bean(initMethod = "migrate")
  public Flyway flyway() {
    return Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/migration")
        .baselineOnMigrate(true)
        .cleanDisabled(true)
        .load();
  }
}
