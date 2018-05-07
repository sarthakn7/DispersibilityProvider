package edu.umass.cs.dispersibility.provider;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Sarthak Nandi on 7/5/18.
 */
@Service
public class DispersibleServiceCreator {

  private final String jarPath;
  private final String propertiesPath;

  @Autowired
  public DispersibleServiceCreator(@Value("${create-service.jar-path}") String jarPath,
                                   @Value("${create-service.properties-path}") String propertiesPath) {
    this.jarPath = jarPath;
    this.propertiesPath = propertiesPath;
  }

  public boolean createService(String service, String initialState) {
    ProcessBuilder pb = new ProcessBuilder("java",
                                           "-jar",
                                           "-Djavax.net.ssl.keyStorePassword=qwerty",
                                           "-Djavax.net.ssl.keyStore=/home/sarthak/IdeaProjects/gigapaxos-s7/conf/keyStore.jks",
                                           "-Djavax.net.ssl.trustStorePassword=qwerty",
                                           "-Djavax.net.ssl.trustStore=/home/sarthak/IdeaProjects/gigapaxos-s7/conf/trustStore.jks",
                                           "-ea",
                                           "-Djava.util.logging.config.file=/home/sarthak/IdeaProjects/gigapaxos-s7/conf/logging.properties",
                                           "-Dlog4j.configuration=/home/sarthak/IdeaProjects/gigapaxos-s7/conf/log4j.properties",
                                           "-DgigapaxosConfig=" + propertiesPath,
                                           jarPath,
                                           service,
                                           initialState);
    pb.inheritIO();

    try {
      Process process = pb.start();
      int exitCode = process.waitFor();
      if (exitCode == 0) {
        System.out.println("Received exit code : " + exitCode);
        return true;
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return false;
  }
}
