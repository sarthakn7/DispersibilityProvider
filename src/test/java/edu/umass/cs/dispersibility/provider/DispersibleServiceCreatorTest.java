package edu.umass.cs.dispersibility.provider;

import edu.umass.cs.dispersibility.provider.db.AppDao;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sarthak Nandi on 7/5/18.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DispersibleServiceCreatorTest {

  @MockBean
  private AppDao appDao; // To avoid requirement of Cassandra

  @Autowired
  private DispersibleServiceCreator dispersibleServiceCreator;

  @Test
  public void createServiceTest() {
    boolean created = dispersibleServiceCreator.createService("LinWritesLocReads", "20");

    Assertions.assertThat(created).isTrue();
  }

}