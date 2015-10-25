package com.boun.config;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolType;
import com.netflix.astyanax.connectionpool.impl.Slf4jConnectionPoolMonitorImpl;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfig {

    @Value("${cassandra.cluster.name}")
    private String clusterName;

    @Value("${cassandra.keyspace}")
    private String keySpace;

    @Value("${cassandra.cluster.seeds}")
    private String seeds;

    @Value("${cassandra.seed.port}")
    private int port;

    @Value("${cassandra.cql.version}")
    private String cqlVersion;

    @Value("${cassandra.target.version}")
    private String targetVersion;

    @Bean
    public Keyspace keyspace(){
      AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
                                              .forCluster(clusterName)
                                              .forKeyspace(keySpace)
                                              .withConnectionPoolConfiguration(
                                                      new ConnectionPoolConfigurationImpl(keySpace)
                                                              .setSeeds(seeds)
                                                              .setPort(port)
                                                      //                                          .setAuthenticationCredentials(
                                                      //                         new SimpleAuthenticationCredentials(new String(PRINCIPAL), new String(CREDENTIAL)))
                                              )
                                              .withAstyanaxConfiguration(
                                                      new AstyanaxConfigurationImpl()
                                                              .setCqlVersion(cqlVersion)
                                                              .setTargetCassandraVersion(targetVersion)
                                                              .setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
                                                              .setConnectionPoolType(ConnectionPoolType.TOKEN_AWARE))

                                              .withConnectionPoolMonitor(new Slf4jConnectionPoolMonitorImpl())
                                              .buildKeyspace(ThriftFamilyFactory.getInstance());
        context.start();
        return context.getClient();
    }

}
