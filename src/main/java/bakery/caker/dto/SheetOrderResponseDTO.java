package bakery.caker.dto;

import bakery.caker.domain.Sheet;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SheetOrderResponseDTO {
    List<Sheet> sheetList;

    @Builder
    public SheetOrderResponseDTO(List<Sheet> sheetList){
        this.sheetList = sheetList;
    }
}
