package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
@Entity
public class Metadata implements IMetadata, Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String course;

    @ElementCollection
    @OrderColumn
    @JoinTable(name = Constants.J_METADATA_SETTINGS)
    private List<String> settings = new ArrayList<>();


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getCourse() {
        return course;
    }

    @Override
    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public List<String> getSettings() {
        return settings;
    }

    @Override
    public void setSettings(List<String> settings) {
        this.settings = settings;
    }

    @Override
    public void addSetting(String setting) {
        settings.add(setting);
    }
}
