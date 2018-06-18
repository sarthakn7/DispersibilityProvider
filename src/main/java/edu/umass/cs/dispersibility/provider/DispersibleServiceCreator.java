package edu.umass.cs.dispersibility.provider;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Sarthak Nandi on 7/5/18.
 */
@Service
public class DispersibleServiceCreator {

  @Value("${create-service.jar-path}")
  private String jarPath;
  @Value("${create-service.properties-path}")
  private String propertiesPath;
  @Value("${create-service.keystore-pass}")
  private String keystorePassword;
  @Value("${create-service.keystore-path}")
  private String keystorePath;
  @Value("${create-service.truststore-pass}")
  private String trustStorePassword;
  @Value("${create-service.truststore-path}")
  private String trustStorePath;
  @Value("${create-service.logging-config}")
  private String loggingConfigFilePath;
  @Value("${create-service.log4j-config}")
  private String log4jConfigFilePath;


  public boolean createService(String service, String initialState) {
    ProcessBuilder pb = new ProcessBuilder(getProcessParameters(service, initialState));
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

  private String[] getProcessParameters(String service, String initialState) {
    return new String[]{"java",
                        "-jar",
                        "-Djavax.net.ssl.keyStorePassword=" + keystorePassword,
                        "-Djavax.net.ssl.keyStore=" + keystorePath,
                        "-Djavax.net.ssl.trustStorePassword=" + trustStorePath,
                        "-Djavax.net.ssl.trustStore=" + trustStorePath,
                        "-ea",
                        "-Djava.util.logging.config.file=" + loggingConfigFilePath,
                        "-Dlog4j.configuration=" + log4jConfigFilePath,
                        "-DgigapaxosConfig=" + propertiesPath,
                        jarPath,
                        service,
                        initialState};
  }
}
