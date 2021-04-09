package com.yelanlan.scoremanagersystem.RepositoryImpl.DTO;

import java.io.Serializable;
import java.util.List;

public class MenuTreeDTO implements Serializable {
    private static final long serialVersionUID = -5874348901280584219L;
    private String id;
    private String title;
    private boolean expand;
    private boolean contextmenu;
    private boolean checked;
    private List<MenuTreeDTO> children;

    public MenuTreeDTO() {
    }

    public MenuTreeDTO(String id, String title, boolean expand, boolean contextmenu) {
        this.id = id;
        this.title = title;
        this.expand = expand;
        this.contextmenu = contextmenu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public List<MenuTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuTreeDTO> children) {
        this.children = children;
    }

    public boolean isContextmenu() {
        return contextmenu;
    }

    public void setContextmenu(boolean contextmenu) {
        this.contextmenu = contextmenu;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
