
package edu.eci.arsw.parallelism.api;

import edu.eci.arsw.parallelism.core.PiDigitsService;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pi")
public class PiDigitsController {

    private final PiDigitsService service;

    public PiDigitsController(PiDigitsService service) {
        this.service = service;
    }

    @GetMapping("/digits")
    public PiResponse digits(
            @RequestParam @Min(0) int start,
            @RequestParam @Min(1) int count
    ) {
        String digits = service.calculateSequential(start, count);
        return new PiResponse(start, count, digits);
    }
}
