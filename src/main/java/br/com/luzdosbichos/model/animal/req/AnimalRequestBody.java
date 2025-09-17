package br.com.luzdosbichos.model.animal.req;

import br.com.luzdosbichos.model.animal.AnimalBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalRequestBody {

    private MultipartFile image;

    @ToString.Exclude
    private AnimalBase animal;
}