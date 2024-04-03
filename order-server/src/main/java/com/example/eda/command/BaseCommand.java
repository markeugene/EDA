package com.example.eda.command;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BaseCommand {
    private String id;

    public BaseCommand(String id) {
        this.id = id;
    }
}
