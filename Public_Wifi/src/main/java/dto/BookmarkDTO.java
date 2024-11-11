package dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkDTO {

	private Long id;
//	private Long sortOrder;
	private Long wifiId;
	private Long groupId;
	private String mainNm;
	private String groupName;
	private Timestamp registerTime;
	private Timestamp updateTime;
}
