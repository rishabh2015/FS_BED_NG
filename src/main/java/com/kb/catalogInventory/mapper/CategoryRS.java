package com.kb.catalogInventory.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kb.catalogInventory.datatable.Category;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer”, “handler"})
public class CategoryRS implements Comparable<CategoryRS>, Serializable {

    private Long id;
    private String categoryName;
    private String categoryIcon;
    private String categoryStage;
    private Set<Category> childrenCategories;

    @Override
    public int compareTo(@NotNull CategoryRS o) {
        return 0;
    }
}
