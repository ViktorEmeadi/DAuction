package com.example.DAuction.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
   FAILED("FAILED"), PENDING("PENDING"), SUCCESSFUL("SUCCESSFUL");
    private final String name;
}
