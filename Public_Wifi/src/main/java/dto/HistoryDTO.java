package dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryDTO {
	
	private Long id;
	private double lat;
	private double lnt;
	private Timestamp searchTime;
	
}
