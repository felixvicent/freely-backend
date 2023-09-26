package com.freely.backend.web.activity;

import com.freely.backend.activity.ActivityService;
import com.freely.backend.project.ActivityStatusEnum;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.activity.dto.ActivityForm;
import com.freely.backend.web.project.dto.ActivityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getByStatus(@RequestParam ActivityStatusEnum status, @AuthenticationPrincipal UserAccount user) {
        List<ActivityDTO> activities = activityService.findByStatus(status, user);

        return ResponseEntity.ok(activities);
    }

    @PostMapping
    public ResponseEntity<ActivityDTO> store(@RequestBody ActivityForm form, @AuthenticationPrincipal UserAccount user) {
        return ResponseEntity.ok().body(activityService.create(form, user));
    }

    @PutMapping("/{activityId}")
    public ResponseEntity<ActivityDTO> update(@PathVariable UUID activityId, @RequestBody ActivityForm form, @AuthenticationPrincipal UserAccount user) {
        return ResponseEntity.ok().body(activityService.update(form, activityId, user));
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<?> delete(@PathVariable UUID activityId) {
        activityService.remove(activityId);

        return ResponseEntity.noContent().build();
    }
}
