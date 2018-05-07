package edu.umass.cs.dispersibility.provider;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author Sarthak Nandi on 7/5/18.
 */
public class DispersibleServiceCreatorTest {

  @Test
  public void createServiceTest() {
    DispersibleServiceCreator dispersibleServiceCreator = new DispersibleServiceCreator("create-service-jar/create-service.jar", "create-service-jar/dispersible.properties");


    boolean created = dispersibleServiceCreator.createService("LinWritesLocReads", "20");

    Assertions.assertThat(created).isTrue();
  }

}