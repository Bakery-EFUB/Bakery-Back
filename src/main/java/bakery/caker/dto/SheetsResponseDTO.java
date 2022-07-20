package bakery.caker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SheetsResponseDTO{
    private List<SheetResponseDTO> sheetResponseDTOs;

    @Builder
    public SheetsResponseDTO(List<SheetResponseDTO> sheetResponseDTOs){
        this.sheetResponseDTOs = sheetResponseDTOs;
    }
}
