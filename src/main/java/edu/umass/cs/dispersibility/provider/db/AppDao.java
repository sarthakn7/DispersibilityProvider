package edu.umass.cs.dispersibility.provider.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import java.nio.ByteBuffer;
import java.util.Optional;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Sarthak Nandi on 23/4/18.
 */
@Service
public class AppDao {

  private final Cluster cluster;
  private final Session session;

  private final AppAccessor accessor;

  public AppDao(@Value("${cassandra.host}") String host, @Value("${cassandra.port}") int port,
                @Value("${cassandra.keyspace}") String keyspace) {
    cluster = Cluster.builder()
        .addContactPoint(host)
        .withPort(port)
        .build();
    session = cluster.connect(keyspace);

    MappingManager manager = new MappingManager(session);
    accessor = manager.createAccessor(AppAccessor.class);
  }

  public void insertApp(String serviceName, String appClassName, String jarFileName, ByteBuffer jar) {
    accessor.insertApp(serviceName, appClassName, jarFileName, jar);
  }

  public Optional<App> getByServiceName(String service) {
    return Optional.ofNullable(accessor.getByServiceName(service));
  }

  @PreDestroy
  public void close() {
    session.close();
    cluster.close();
  }
}