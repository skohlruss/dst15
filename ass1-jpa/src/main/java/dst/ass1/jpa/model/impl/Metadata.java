package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IMetadata;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
@Entity
public class Metadata implements IMetadata {

    @Id
    @GeneratedValue
    private Long id;
    private String course;

    @ElementCollection
    private List<String> settings;


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
        return null;
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
