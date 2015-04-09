package dst.ass2.ws.dto;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import dst.ass1.jpa.model.IClassroom;
import dst.ass1.jpa.model.ILectureStreaming;

@XmlRootElement(name = "stats")
public class StatisticsDTO {

	private String name;
	private List<StreamingDTO> streamings;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StreamingDTO> getStreamings() {
		return streamings;
	}

	public void setStreamings(List<StreamingDTO> streamings) {
		this.streamings = streamings;
	}

	public void addStreaming(ILectureStreaming streaming) {
		if (streamings == null)
			streamings = new LinkedList<StreamingDTO>();
		int students = 0;
		for (IClassroom c : streaming.getClassrooms()) {
			students += c.getStudentCapacity();
		}
		StreamingDTO edto = new StreamingDTO(streaming.getStart(), streaming.getEnd(), students);
		streamings.add(edto);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ");
		sb.append(name);
		sb.append("\n");
		for (StreamingDTO streaming : streamings) {
			sb.append(streaming.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

}
