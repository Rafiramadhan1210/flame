package com.example.flame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Autowired private ForumThreadRepository threadRepository;
    @Autowired private ForumReplyRepository replyRepository;

    // GET semua thread per game
    @GetMapping("/game/{gameId}")
    public List<ForumThread> getThreadsByGame(@PathVariable Long gameId) {
        return threadRepository.findByGameIdOrderByCreatedAtDesc(gameId);
    }

    // POST buat thread baru
    @PostMapping("/thread")
    public Map<String, Object> buatThread(@RequestBody ForumThread thread) {
        Map<String, Object> response = new HashMap<>();
        if (thread.getUsername() == null || thread.getUsername().isBlank()) {
            response.put("success", false);
            response.put("message", "Login diperlukan untuk membuat thread.");
            return response;
        }
        try {
            threadRepository.save(thread);
            response.put("success", true);
            response.put("message", "Thread berhasil dibuat");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gagal: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/thread/{threadId}")
    public ResponseEntity<ForumThread> getThreadById(@PathVariable Long threadId) {
        return threadRepository.findById(threadId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/thread/{id}")
    public Map<String, Object> updateThread(@PathVariable Long id, @RequestBody ForumThread updated) {
        Map<String, Object> response = new HashMap<>();
        return threadRepository.findById(id).map(existing -> {
            if (!existing.getUsername().equals(updated.getUsername())) {
                response.put("success", false);
                response.put("message", "Hanya pemilik thread yang dapat mengedit.");
                return response;
            }
            existing.setJudul(updated.getJudul());
            existing.setKonten(updated.getKonten());
            threadRepository.save(existing);
            response.put("success", true);
            response.put("message", "Thread berhasil diperbarui");
            return response;
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Thread tidak ditemukan.");
            return response;
        });
    }

    @DeleteMapping("/thread/{id}")
    public Map<String, Object> deleteThread(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Map<String, Object> response = new HashMap<>();
        return threadRepository.findById(id).map(existing -> {
            if (!existing.getUsername().equals(body.get("username"))) {
                response.put("success", false);
                response.put("message", "Hanya pemilik thread yang dapat menghapus.");
                return response;
            }
            replyRepository.deleteByThreadId(existing.getId());
            threadRepository.deleteById(id);
            response.put("success", true);
            response.put("message", "Thread berhasil dihapus");
            return response;
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Thread tidak ditemukan.");
            return response;
        });
    }

    // GET semua reply dalam thread
    @GetMapping("/thread/{threadId}/replies")
    public List<ForumReply> getReplies(@PathVariable Long threadId) {
        return replyRepository.findByThreadIdOrderByCreatedAtAsc(threadId);
    }

    // POST tambah reply
    @PostMapping("/reply")
    public Map<String, Object> tambahReply(@RequestBody ForumReply reply) {
        Map<String, Object> response = new HashMap<>();
        if (reply.getUsername() == null || reply.getUsername().isBlank()) {
            response.put("success", false);
            response.put("message", "Login diperlukan untuk mengirim balasan.");
            return response;
        }
        try {
            replyRepository.save(reply);
            response.put("success", true);
            response.put("message", "Reply berhasil ditambahkan");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gagal: " + e.getMessage());
        }
        return response;
    }

    @PutMapping("/reply/{id}")
    public Map<String, Object> updateReply(@PathVariable Long id, @RequestBody ForumReply updated) {
        Map<String, Object> response = new HashMap<>();
        return replyRepository.findById(id).map(existing -> {
            if (!existing.getUsername().equals(updated.getUsername())) {
                response.put("success", false);
                response.put("message", "Hanya pemilik balasan yang dapat mengedit.");
                return response;
            }
            existing.setKonten(updated.getKonten());
            replyRepository.save(existing);
            response.put("success", true);
            response.put("message", "Balasan berhasil diperbarui");
            return response;
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Balasan tidak ditemukan.");
            return response;
        });
    }

    @DeleteMapping("/reply/{id}")
    public Map<String, Object> deleteReply(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Map<String, Object> response = new HashMap<>();
        return replyRepository.findById(id).map(existing -> {
            if (!existing.getUsername().equals(body.get("username"))) {
                response.put("success", false);
                response.put("message", "Hanya pemilik balasan yang dapat menghapus.");
                return response;
            }
            replyRepository.deleteById(id);
            response.put("success", true);
            response.put("message", "Balasan berhasil dihapus");
            return response;
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Balasan tidak ditemukan.");
            return response;
        });
    }

    // ===== ENDPOINT UNTUK HALAMAN PROFILE =====

    // GET semua thread yang dibuat oleh seorang user
    @GetMapping("/user/{username}/threads")
    public List<ForumThread> getThreadsByUsername(@PathVariable String username) {
        return threadRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    // GET semua reply yang dibuat oleh seorang user, dilengkapi info thread induknya
    @GetMapping("/user/{username}/replies")
    public List<Map<String, Object>> getRepliesByUsername(@PathVariable String username) {
        List<ForumReply> replies = replyRepository.findByUsernameOrderByCreatedAtDesc(username);
        List<Map<String, Object>> hasil = new ArrayList<>();

        for (ForumReply reply : replies) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", reply.getId());
            item.put("konten", reply.getKonten());
            item.put("threadId", reply.getThreadId());
            item.put("createdAt", reply.getCreatedAt());

            threadRepository.findById(reply.getThreadId()).ifPresent(thread -> {
                item.put("threadJudul", thread.getJudul());
                item.put("gameId", thread.getGameId());
            });

            hasil.add(item);
        }
        return hasil;
    }
}