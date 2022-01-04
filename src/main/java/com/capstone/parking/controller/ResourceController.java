
package com.capstone.parking.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
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

  @PostMapping("/upload")
  public String upload( @RequestParam("file") MultipartFile file) throws IOException {
    String constr = "AccountName=" + accountName + ";AccountKey=" + accountKey
        + ";EndpointSuffix=core.windows.net;DefaultEndpointsProtocol=https;";

    BlobContainerClient container = new BlobContainerClientBuilder().connectionString(constr).containerName("qpa").buildClient();

    BlobClient blob = container.getBlobClient(file.getOriginalFilename());
    blob.upload(file.getInputStream(), file.getSize(), true);

    return "ok";
  }
}
