package com.freely.backend.web.activity;

import com.freely.backend.activity.ActivityService;
import com.freely.backend.activity.ActivityStatusEnum;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.activity.dto.ActivityForm;
import com.freely.backend.web.activity.dto.ActivityDTO;
import com.freely.backend.web.activity.dto.UpdateActivityDescriptionForm;
import com.freely.backend.web.activity.dto.UpdateActivityResponsibleForm;
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
    public ResponseEntity<List<ActivityDTO>> getByStatus(@AuthenticationPrincipal UserAccount user,
                                                         @RequestParam ActivityStatusEnum status,
                                                         @RequestParam(name = "projectId", required = false) UUID projectId,
                                                         @RequestParam(required = false, value = "collaboratorsIds[]") List<UUID> collaboratorsIds) {
        List<ActivityDTO> activities = activityService.findByStatus(status, projectId, collaboratorsIds, user);

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

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityDTO> show(@PathVariable UUID activityId){
        return ResponseEntity.ok(activityService.show(activityId));
    }

    @PutMapping("/{activityId}/responsible")
    public ResponseEntity<?> updateResponsible(@PathVariable UUID activityId,
                                               @RequestBody UpdateActivityResponsibleForm updateActivityResponsibleForm){
        activityService.updateResponsible(activityId, updateActivityResponsibleForm);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{activityId}/description")
    public ResponseEntity<?> updateDescription(@PathVariable UUID activityId,
                                               @RequestBody UpdateActivityDescriptionForm updateActivityDescriptionForm){
        return ResponseEntity.ok(activityService.updateDescription(activityId, updateActivityDescriptionForm));
    }
}
