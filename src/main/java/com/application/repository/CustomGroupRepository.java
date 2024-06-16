package com.application.repository;


public interface CustomGroupRepository {
    void updateLeader(Integer groupId, Integer userId);

    void updateName(Integer groupId, String groupName);

    void updateDescription(Integer groupId, String groupDesc);

    boolean existingLeader(Integer groupId, Integer leaderId);

    boolean existingName(String name, Integer groupId);
}
