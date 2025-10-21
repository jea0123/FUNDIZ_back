package com.example.funding.controller;

import com.example.funding.common.FileUploader;
import com.example.funding.dto.ResponseDto;
import com.example.funding.mapper.AttachMapper;
import com.example.funding.model.Attach;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attach")
@RequiredArgsConstructor
public class AttachController {

    private final FileUploader fileUploader;
    private final AttachMapper attachMapper;

    @PostMapping("/upload/qna")
    public ResponseEntity<ResponseDto<List<Attach>>> uploadQna(@ModelAttribute List<MultipartFile> files) throws Exception {
        commonUpload(files, "QnA");
        return ResponseEntity.ok(ResponseDto.success(200, "QnA 파일 업로드 성공", null));
    }

    @PostMapping("/upload/cm")
    public ResponseEntity<ResponseDto<List<Attach>>> uploadCm(@ModelAttribute List<MultipartFile> files) throws Exception {
        commonUpload(files, "CM");
        return ResponseEntity.ok(ResponseDto.success(200, "CM 파일 업로드 성공", null));
    }

    @PostMapping("/upload/rv")
    public ResponseEntity<ResponseDto<List<Attach>>> uploadRv(@ModelAttribute List<MultipartFile> files) throws Exception {
        commonUpload(files, "RV");
        return ResponseEntity.ok(ResponseDto.success(200, "RV 파일 업로드 성공", null));
    }

    @PostMapping("/upload/ntc")
    public ResponseEntity<ResponseDto<List<Attach>>> uploadNtc(@ModelAttribute List<MultipartFile> files) throws Exception {
        commonUpload(files, "NTC");
        return ResponseEntity.ok(ResponseDto.success(200, "NTC 파일 업로드 성공", null));
    }

    @PostMapping("/upload/iq")
    public ResponseEntity<ResponseDto<List<Attach>>> uploadIq(@ModelAttribute List<MultipartFile> files) throws Exception {
        commonUpload(files, "IQ");
        return ResponseEntity.ok(ResponseDto.success(200, "IQ 파일 업로드 성공", null));
    }

    @PostMapping("/upload/rp")
    public ResponseEntity<ResponseDto<List<Attach>>> uploadRp(@ModelAttribute List<MultipartFile> files) throws Exception {
        commonUpload(files, "RP");
        return ResponseEntity.ok(ResponseDto.success(200, "RP 파일 업로드 성공", null));
    }

    @PostMapping("/upload/pj")
    public ResponseEntity<ResponseDto<List<Attach>>> uploadPj(@ModelAttribute List<MultipartFile> files) throws Exception {
        commonUpload(files, "PJ");
        return ResponseEntity.ok(ResponseDto.success(200, "PJ 파일 업로드 성공", null));
    }

    @PostMapping("/upload/cr")
    public ResponseEntity<ResponseDto<List<Attach>>> uploadCr(@ModelAttribute List<MultipartFile> files) throws Exception {
        commonUpload(files, "CR");
        return ResponseEntity.ok(ResponseDto.success(200, "CR 파일 업로드 성공", null));
    }

    @DeleteMapping("/delete/{attachId}")
    public ResponseEntity<ResponseDto<String>> delete(@PathVariable Long attachId) {
        attachMapper.delete(attachId);
        return ResponseEntity.ok(ResponseDto.success(200, "파일 삭제 성공", null));
    }

    @PostMapping("/image")
    public ResponseEntity<ResponseDto<String>> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        String uploaded = fileUploader.upload(file);
        return ResponseEntity.ok(ResponseDto.success(200, "이미지 업로드 성공", uploaded));
    }

    private void commonUpload(List<MultipartFile> files, String code) throws Exception {
        for (MultipartFile file : files) {
            String uploaded = fileUploader.upload(file);

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            Attach attach = Attach.builder()
                .fileName(originalFilename)
                .filePath(uploaded)
                .fileType(file.getContentType())
                .fileExt(extension)
                .code(code)
                .build();

            attachMapper.insert(attach);
        }
    }
}
