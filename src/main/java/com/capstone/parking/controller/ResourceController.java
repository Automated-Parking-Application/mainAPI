
package com.capstone.parking.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resource")
public class ResourceController {
  @Value("${azure.storage.account-name}")
  private String accountName;

  @Value("${azure.storage.account-key}")
  private String accountKey;

  @Value("${export.path}")
  private String exportPath;

  @PostMapping("/upload")
  public ResponseEntity upload(@RequestParam("file") MultipartFile file) throws IOException {
    String nameOfImage;
    try {
      String constr = "AccountName=" + accountName + ";AccountKey=" + accountKey
          + ";EndpointSuffix=core.windows.net;DefaultEndpointsProtocol=https;";

      BlobContainerClient container = new BlobContainerClientBuilder().connectionString(constr).containerName("qpa")
          .buildClient();

      nameOfImage = System.currentTimeMillis() + file.getOriginalFilename();

      BlobClient blob = container.getBlobClient(nameOfImage);
      blob.upload(file.getInputStream(), file.getSize(), true);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(exportPath + nameOfImage, HttpStatus.OK);
  }
}
