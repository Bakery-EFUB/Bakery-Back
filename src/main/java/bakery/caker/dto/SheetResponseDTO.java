package bakery.caker.dto;

import bakery.caker.domain.Sheet;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class SheetResponseDTO {
    Map<String, Sheet> sheetList;

    @Builder
    public SheetResponseDTO(Map<String, Sheet> sheetList){
        this.sheetList = sheetList;
    }
}
