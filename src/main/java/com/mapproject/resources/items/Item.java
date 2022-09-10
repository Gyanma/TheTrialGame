package com.mapproject.resources.items;

public class Item {

    private final int id;
    private String name;
    private String nameWithDetArticle;
    private String nameWithIndetArticle;
    private String description;
    private boolean findable;
    private boolean used = false;

    private String manualDescription;

    public Item(int id) {
        this.id = id;
    }

    public Item(int id, String name, String nameWithDetArticle, String nameWithIndetArticle,
            String description, String manualDescription) {
        this.id = id;
        this.name = name;
        this.nameWithDetArticle = nameWithDetArticle;
        this.nameWithIndetArticle = nameWithIndetArticle;
        this.description = description;
        this.manualDescription = manualDescription;
        this.findable = true;
        this.used = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameWithDetArticle() {
        return nameWithDetArticle;
    }

    public void setNameWithDetArticle(String nameWithDetArticle) {
        this.nameWithDetArticle = nameWithDetArticle;
    }

    public String getNameWithIndetArticle() {
        return nameWithIndetArticle;
    }

    public void setNameWithIndetArticle(String nameWithIndetArticle) {
        this.nameWithIndetArticle = nameWithIndetArticle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFindable() {
        return findable;
    }

    public void setFindable(boolean findable) {
        this.findable = findable;
    }

    public String getManualDescription() {
        return manualDescription;
    }

    public void setManualDescription(String manualDescription) {
        this.manualDescription = manualDescription;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
