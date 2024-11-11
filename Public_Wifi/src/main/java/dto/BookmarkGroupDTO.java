package dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkGroupDTO {
	
	private Long id;
	private String groupName;
//	private int sortOrder;
	private Timestamp registerTime;
	private Timestamp updateTime;
	
}
