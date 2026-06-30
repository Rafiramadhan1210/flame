package com.example.flame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired private ContactMessageRepository contactMessageRepository;

    @PostMapping
    public Map<String, Object> kirimPesan(@RequestBody ContactMessage pesan) {
        Map<String, Object> response = new HashMap<>();

        if (pesan.getNama() == null || pesan.getNama().isBlank()
                || pesan.getEmail() == null || pesan.getEmail().isBlank()
                || pesan.getPesan() == null || pesan.getPesan().isBlank()) {
            response.put("success", false);
            response.put("message", "Nama, email, dan pesan wajib diisi.");
            return response;
        }

        try {
            contactMessageRepository.save(pesan);
            response.put("success", true);
            response.put("message", "Pesan berhasil dikirim. Terima kasih!");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gagal mengirim pesan: " + e.getMessage());
        }
        return response;
    }
}