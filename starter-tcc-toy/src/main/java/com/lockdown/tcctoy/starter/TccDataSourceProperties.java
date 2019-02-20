package com.lockdown.tcctoy.starter;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="tcc.transaction.datasource")
public class TccDataSourceProperties extends DataSourceProperties {

}
