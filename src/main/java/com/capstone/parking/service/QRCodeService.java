package com.capstone.parking.service;

public interface QRCodeService {

  byte[] generateQRCode(String qrContent, int width, int height) throws Exception;

}
