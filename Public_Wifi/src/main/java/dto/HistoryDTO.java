package dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDTO {
	
	private int id;
	private String lat;
	private String lnt;
	private Timestamp searchTime;
	
}
