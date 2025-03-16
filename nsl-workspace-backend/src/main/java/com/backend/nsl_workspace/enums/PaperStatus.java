package com.backend.nsl_workspace.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaperStatus {
    DRAFT,
    SUBMITTED,
    UNDER_REVIEW,
    MAJOR_REVISION,
    MINOR_REVISION,
    REJECTED,
    PUBLISHED,
}