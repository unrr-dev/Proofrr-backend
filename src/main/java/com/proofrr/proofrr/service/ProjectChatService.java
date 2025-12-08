package com.proofrr.proofrr.service;

import com.proofrr.proofrr.model.Project;
import com.proofrr.proofrr.model.ProjectChatMessage;
import com.proofrr.proofrr.model.ProjectVisitor;
import com.proofrr.proofrr.repository.ProjectChatMessageRepository;
import com.proofrr.proofrr.repository.ProjectRepository;
import com.proofrr.proofrr.repository.ProjectVisitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;

@Service
public class ProjectChatService {

    private final ProjectRepository projectRepository;
    private final ProjectVisitorRepository visitorRepository;
    private final ProjectChatMessageRepository chatMessageRepository;

    public ProjectChatService(ProjectRepository projectRepository,
                              ProjectVisitorRepository visitorRepository,
                              ProjectChatMessageRepository chatMessageRepository) {
        this.projectRepository = projectRepository;
        this.visitorRepository = visitorRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Transactional
    public ProjectVisitor registerVisitor(String shareUuid, String displayName) {
        if (!StringUtils.hasText(displayName)) {
            throw new IllegalArgumentException("displayName is required");
        }

        Project project = findProjectByShareUuid(shareUuid);

        ProjectVisitor visitor = new ProjectVisitor();
        visitor.setProject(project);
        visitor.setDisplayName(displayName.trim());
        visitor.setLastSeenAt(Instant.now());
        return visitorRepository.save(visitor);
    }

    public List<ProjectChatMessage> getMessagesByShareUuid(String shareUuid) {
        Project project = findProjectByShareUuid(shareUuid);
        return chatMessageRepository.findByProjectOrderByCreatedAtAsc(project);
    }

    public List<ProjectChatMessage> getMessagesByProjectId(Long projectId) {
        Project project = findProjectById(projectId);
        return chatMessageRepository.findByProjectOrderByCreatedAtAsc(project);
    }

    @Transactional
    public ProjectChatMessage saveVisitorMessage(String shareUuid,
                                                 Long visitorId,
                                                 String senderName,
                                                 String content) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content is required");
        }
        Project project = findProjectByShareUuid(shareUuid);
        ProjectVisitor visitor = requireVisitor(project, visitorId);

        if (!StringUtils.hasText(senderName)) {
            senderName = visitor.getDisplayName();
        }

        visitor.setLastSeenAt(Instant.now());
        visitorRepository.save(visitor);

        ProjectChatMessage message = new ProjectChatMessage();
        message.setProject(project);
        message.setVisitor(visitor);
        message.setSenderName(senderName.trim());
        message.setContent(content);
        return chatMessageRepository.save(message);
    }

    private Project findProjectByShareUuid(String shareUuid) {
        if (!StringUtils.hasText(shareUuid)) {
            throw new IllegalArgumentException("shareUuid is required");
        }
        return projectRepository.findByShareUuid(shareUuid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shareUuid"));
    }

    private Project findProjectById(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("projectId is required");
        }
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    private ProjectVisitor requireVisitor(Project project, Long visitorId) {
        if (visitorId == null) {
            throw new IllegalArgumentException("visitorId is required");
        }
        return visitorRepository.findByIdAndProject(visitorId, project)
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found for this project"));
    }
}
