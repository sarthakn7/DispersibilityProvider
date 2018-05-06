package edu.umass.cs.dispersibility.provider;


import edu.umass.cs.dispersibility.provider.db.AppDao;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sarthak Nandi on 30/4/18.
 */
@Controller
public class FileUploadController {

  @Autowired
  private AppDao appDao;

  @GetMapping("/")
  public String homePage() {
    return "home";
  }

  /**
   * POST /uploadFile -> receive and locally save a file.
   *
   * @param uploadFile The uploaded file as Multipart file parameter in the
   * HTTP request. The RequestParam name must be the same of the attribute
   * "name" in the input tag with type file.
   * @return An http OK status in case of success, an http 4xx status in case
   * of errors.
   */
  @PostMapping(value = "/upload")
  @ResponseBody
  public ResponseEntity<?> uploadFile(@RequestParam("jar-file") MultipartFile uploadFile,
                                      @RequestParam("service") String service,
                                      @RequestParam("app-class") String appClassName) {

    System.out.println("Service: " + service);
    System.out.println("App class name: " + appClassName);

    String filename = uploadFile.getOriginalFilename();

    try {
      ByteBuffer jar = ByteBuffer.wrap(uploadFile.getBytes());
      appDao.insertApp(service, appClassName, filename, jar);

    } catch (IOException e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

//  @GetMapping("/")
//  public String listUploadedFiles(Model model) {
//
//    model.addAttribute("files", storageService.loadAll()
//        .map(path -> MvcUriComponentsBuilder
//            .fromMethodName(FileUploadController.class, "serveFile",
//                            path.getFileName().toString()).build().toString())
//        .collect(Collectors.toList()));
//
//    return "uploadForm";
//  }

  @GetMapping("/files/{service}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String service) {
    return appDao.getByServiceName(service)
        .map(app -> createFileResource(app.getJarFileName(), app.getJar()))
        .map(file -> ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\"" + file.getFilename()
                                                + "\"").body(file))
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  private Resource createFileResource(String fileName, ByteBuffer content) {
    try {
      // Get the filename and build the local file path

//      String directory = env.getProperty("netgloo.paths.uploadedFiles");
      String directory = "/home/sarthak/IdeaProjects/DispersibilityProvider/upload-dir";
      Path file = Paths.get(directory, fileName);
      String filePath = file.toString();

      // Save the file locally
      BufferedOutputStream stream =
          new BufferedOutputStream(new FileOutputStream(new File(filePath)));
      stream.write(content.array());
      stream.close();

      return new UrlResource(file.toUri());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
