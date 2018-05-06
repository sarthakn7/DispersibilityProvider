package edu.umass.cs.dispersibility.provider.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import java.nio.ByteBuffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sarthak Nandi on 6/5/18.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "apps")
public class App {

  @PartitionKey
  private String service;

  @Column(name = "app_class_name")
  private String appClassName;

  @Column(name = "jar_file_name")
  private String jarFileName;

  @Column
  private ByteBuffer jar;
}
