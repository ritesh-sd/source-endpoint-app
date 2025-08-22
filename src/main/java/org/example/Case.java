package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Case {
    private Long id;
    private String name;
    private String description;
    private String status;
    private String createdDate;
    private String updatedDate;
    private String assignedTo;
}
