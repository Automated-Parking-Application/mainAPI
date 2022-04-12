package com.capstone.parking.service;

import com.capstone.parking.constants.ApaRole;
import com.capstone.parking.entity.ParkingSpaceEntity;
import com.capstone.parking.entity.QrCodeEntity;
import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.model.EmailRequestDto;
import com.capstone.parking.repository.ParkingSpaceRepository;
import com.capstone.parking.repository.QrCodeRepository;
import com.capstone.parking.repository.UserRepository;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Configuration configuration;
    @Autowired
    private QrCodeRepository qrcodeRepository;
    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;
    @Autowired
    private UserRepository userRepository;

    private boolean checkIfHavingAdminPermission(int parkingId, int userId) {
        try {
            ParkingSpaceEntity searchParkingSpace = parkingSpaceRepository.getById(parkingId);
            UserEntity user = userRepository.getById(userId);
            return (searchParkingSpace != null && searchParkingSpace.getOwnerId() == user.getId())
                    || user.getRoleByRoleId().getName().equals(ApaRole.ROLE_SUPERADMMIN);
        } catch (Exception e) {
            return false;
        }
    }

    public ResponseEntity sendMail(int parkingId, int userId, EmailRequestDto request, Map<String, String> model) {
        String response;
        MimeMessage message = mailSender.createMimeMessage();
        if (checkIfHavingAdminPermission(parkingId, userId)) {
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        StandardCharsets.UTF_8.name());
                Template template = configuration.getTemplate("mail-template.html");
                String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                helper.setTo(request.getTo());
                helper.setFrom(request.getFrom());
                helper.setSubject("QR Code Information");
                helper.setText(html, true);

                ArrayList<QrCodeEntity> codeList = (ArrayList<QrCodeEntity>) qrcodeRepository
                        .findAllByParkingId(parkingId);
                for (QrCodeEntity code : codeList) {
                    File tempFile = File.createTempFile(code.getExternalId(), code.getExternalId(), null);
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(code.getCode());
                    helper.addAttachment(code.getExternalId() + System.currentTimeMillis() + ".png", tempFile);
                }

                mailSender.send(message);
                response = "Email has been sent to :" + request.getTo();
                return new ResponseEntity<>(response, HttpStatus.OK);

            } catch (MessagingException | IOException | TemplateException e) {
                response = "Email send failure to :" + request.getTo();
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        } else {
            return new ResponseEntity<>("Cannot access this parking space", HttpStatus.UNAUTHORIZED);

        }

    }
}
