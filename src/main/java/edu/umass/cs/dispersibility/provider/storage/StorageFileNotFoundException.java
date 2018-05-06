package edu.umass.cs.dispersibility.provider.storage;

/**
 * @author Sarthak Nandi on 30/4/18.
 */
public class StorageFileNotFoundException extends StorageException {

  public StorageFileNotFoundException(String message) {
    super(message);
  }

  public StorageFileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
