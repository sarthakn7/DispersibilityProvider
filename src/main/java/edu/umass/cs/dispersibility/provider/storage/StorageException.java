package edu.umass.cs.dispersibility.provider.storage;

/**
 * @author Sarthak Nandi on 30/4/18.
 */
public class StorageException extends RuntimeException {

  public StorageException(String message) {
    super(message);
  }

  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
