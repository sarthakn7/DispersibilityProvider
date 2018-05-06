package edu.umass.cs.dispersibility.provider;


import edu.umass.cs.dispersibility.provider.storage.StorageFileNotFoundException;
import edu.umass.cs.dispersibility.provider.storage.StorageService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Sarthak Nandi on 30/4/18.
 */
@Controller
public class FileUploadController {

  private final StorageService storageService;

  @Autowired
  public FileUploadController(StorageService storageService) {
    this.storageService = storageService;
  }

  @GetMapping("/")
  public String homePage() {
    return "home";
  }

  /**
   * POST /uploadFile -> receive and locally save a file.
   *
   * @param uploadfile The uploaded file as Multipart file parameter in the
   * HTTP request. The RequestParam name must be the same of the attribute
   * "name" in the input tag with type file.
   *
   * @return An http OK status in case of success, an http 4xx status in case
   * of errors.
   */
  @PostMapping(value = "/upload")
  @ResponseBody
  public ResponseEntity<?> uploadFile(@RequestParam("jar-file") MultipartFile uploadfile,
                                      @RequestParam("service") String service,
                                      @RequestParam("app-class") String appClassName) {

    System.out.println("Service: " + service);
    System.out.println("App class name: " + appClassName);

    String filename = uploadfile.getOriginalFilename();

    try {
      // Get the filename and build the local file path

//      String directory = env.getProperty("netgloo.paths.uploadedFiles");
      String directory = "/home/sarthak/IdeaProjects/DispersibilityProvider/upload-dir";
      String filepath = Paths.get(directory, filename).toString();

      // Save the file locally
      BufferedOutputStream stream =
          new BufferedOutputStream(new FileOutputStream(new File(filepath)));
      stream.write(uploadfile.getBytes());
      stream.close();
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
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

  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

    Resource file = storageService.loadAsResource(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                                      "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @PostMapping("/")
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {

    storageService.store(file);
    redirectAttributes.addFlashAttribute("message",
                                         "You successfully uploaded " + file.getOriginalFilename() + "!");

    return "redirect:/";
  }

  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException e) {
    return ResponseEntity.notFound().build();
  }

}
