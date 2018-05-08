package edu.umass.cs.dispersibility.provider;


import edu.umass.cs.dispersibility.provider.db.AppDao;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Sarthak Nandi on 30/4/18.
 */
@Controller
public class FileUploadController {

  @Autowired
  private AppDao appDao;
  @Autowired
  private DispersibleServiceCreator dispersibleServiceCreator;

  @GetMapping("/")
  public String index() {
    return "home";
  }

  @GetMapping("/uploaded")
  public String uploaded() {
    return "uploaded";
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
  public String uploadFile(@RequestParam("jar-file") MultipartFile uploadFile,
                                 @RequestParam("service") String service,
                                 @RequestParam("app-class") String appClassName,
                                 @RequestParam("initial-state") String initialState,
                                 RedirectAttributes redirectAttributes) {
    service = service.trim();
    appClassName = appClassName.trim();

    System.out.println("Service: " + service);
    System.out.println("App class name: " + appClassName);
    System.out.println("Initial state: " + initialState);

    String filename = uploadFile.getOriginalFilename();

    try {
      ByteBuffer jar = ByteBuffer.wrap(uploadFile.getBytes());
      appDao.insertApp(service, appClassName, filename, jar);

      boolean created = dispersibleServiceCreator.createService(service, initialState);

      if (created) {
        return "redirect:/uploaded";
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    redirectAttributes.addFlashAttribute("message", "Upload failed");
    return "redirect:/";
  }

  @GetMapping("/files/{service}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String service) {
    return appDao.getByServiceName(service)
        .map(app -> {
          Resource file = new ByteArrayResource(app.getJar().array());
          String fileHeader = "attachment; filename=\"" + app.getJarFileName() + "\"";
          return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, fileHeader).body(file);
        })
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }

}
