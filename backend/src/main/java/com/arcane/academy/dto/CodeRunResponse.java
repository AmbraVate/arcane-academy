package com.arcane.academy.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CodeRunResponse {
    private String output;
    private String error;
    private RunStatus status;

    public enum RunStatus { SUCCESS, COMPILE_ERROR, RUNTIME_ERROR, TIMEOUT, ERROR }

    public static CodeRunResponse success(String output) {
        return CodeRunResponse.builder().output(output).status(RunStatus.SUCCESS).build();
    }
    public static CodeRunResponse compilationError(String error) {
        return CodeRunResponse.builder().error(error).status(RunStatus.COMPILE_ERROR).build();
    }
    public static CodeRunResponse runtimeError(String error) {
        return CodeRunResponse.builder().error(error).status(RunStatus.RUNTIME_ERROR).build();
    }
    public static CodeRunResponse error(String error) {
        return CodeRunResponse.builder().error(error).status(RunStatus.ERROR).build();
    }
}
