package com.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 範例實體。換專案時複製此檔案並重新命名欄位即可。
 */
@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends BaseEntity {

    @NotBlank
    private String name;

    private String description;
}
