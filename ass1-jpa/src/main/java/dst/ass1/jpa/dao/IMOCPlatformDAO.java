package dst.ass1.jpa.dao;

import java.util.List;

import dst.ass1.jpa.model.IMOCPlatform;

public interface IMOCPlatformDAO extends GenericDAO<IMOCPlatform> {
	List<IMOCPlatform> findByName(String name);
}
